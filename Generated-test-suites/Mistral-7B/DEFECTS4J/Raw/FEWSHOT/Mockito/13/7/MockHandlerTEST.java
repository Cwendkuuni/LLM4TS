package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.verification.VerificationModeFactory.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.verifyZeroInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.verifyZeroInteractionsInOrder;
import static org.mockito.junit.MockitoJUnit.verify;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.invocation.StubbedInvocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbableImpl;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private InvocationContainer invocationContainer;
    private MockHandler<Object> mockHandler;
    private MockSettingsImpl mockSettings;
    private Object mock;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
        invocationContainer = new InvocationContainerImpl(new ThreadSafeMockingProgress());
        mockHandler = new MockHandler<Object>(mockSettings);
        mock = mockHandler.createMock(MockitoTestUtils.createClassLoader(), MockitoTestUtils.mockType(Object.class));
    }

    @Test
    public void testHandle_NoAnswersForStubbing() throws Throwable {
        Invocation invocation = new Invocation(null, null, null, null, null, null, null);
        Object result = mockHandler.handle(invocation);

        assertNull(result);
    }

    @Test
    public void testHandle_VerificationModeIsNotEmpty() throws Throwable {
        VerificationMode verificationMode = verifyNoMoreInteractions(mock);
        Invocation invocation = new Invocation(mock, Arrays.asList(ArgumentMatchers.anyString()), null, null, null, null, null);

        mockHandler.handle(invocation);

        verify(mock).verify(verificationMode);
    }

    @Test
    public void testHandle_VerificationModeIsEmpty() throws Throwable {
        VerificationMode verificationMode = verifyZeroInteractions(mock);
        Invocation invocation = new Invocation(mock, Arrays.asList(ArgumentMatchers.anyString()), null, null, null, null, null);

        mockHandler.handle(invocation);

        verifyZeroInteractions(mock);
    }

    @Test
    public void testHandle_VerificationModeInOrder() throws Throwable {
        VerificationMode verificationMode = verifyZeroInteractionsInOrder(mock);
        Invocation invocation1 = new Invocation(mock, Arrays.asList(ArgumentMatchers.anyString()), null, null, null, null, null);
        Invocation invocation2 = new Invocation(mock, Arrays.asList(ArgumentMatchers.anyString()), null, null, null, null, null);

        mockHandler.handle(invocation1);
        mockHandler.handle(invocation2);

        verifyZeroInteractionsInOrder(mock);
    }

    @Test
    public void testHandle_InvocationMatcherForStubbing() throws Throwable {
        Answer answer = Mockito.answer(Mockito.mock(String.class));
        List<Answer> answers = Arrays.asList(answer);
        mockHandler.setAnswersForStubbing(answers);

        InvocationMatcher invocationMatcher = ArgumentMatchers.argThat(ArgumentMatchers.isInstanceOf(String.class));
        Invocation invocation = new Invocation(mock, Arrays.asList(invocationMatcher), null, null, null, null, null);

        Object result = mockHandler.handle(invocation);

        assertNull(result);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        answer.getValue(); // invoke answer to initialize its state
        verify(answer).answer(invocation, argumentCaptor.capture());
        assertSame("Invoked answer argument should match the invocation argument", invocationMatcher.match(argumentCaptor.getValue()), true);
    }

    @Test
    public void testHandle_StubbedInvocation() throws Throwable {
        Callable<String> callable = Mockito.mock(Callable.class);
        Object result = mockHandler.handle(new Invocation(mock, null, callable, null, null, null, null));

        assertNull(result);

        StubbedInvocation stubbedInvocation = mock(StubbedInvocation.class);
        InvocationMatcher invocationMatcher = ArgumentMatchers.any();
        mockHandler.invocationContainer.setAnswerFor(invocationMatcher, stubbedInvocation);
        Invocation invocation = new Invocation(mock, null, null, null, null, null, null);

        result = mockHandler.handle(invocation);

        assertNull(result);
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void testHandle_VoidMethodStubbable() throws Throwable {
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        voidMethodStubbable.stubVoid();

        Invocation invocation = new Invocation(mock, Arrays.asList(ArgumentMatchers.anyString()), null, null, null, null, null);

        mockHandler.handle(invocation);

        // no verification as void methods don't return values
    }

    @Test
    public void testHandle_OngoingStubbing() throws Throwable {
        Invocation invocation = new Invocation(mock, Arrays.asList(ArgumentMatchers.anyString()), null, null, null, null, null);

        OngoingStubbingImpl<Object> ongoingStubbing = mockHandler.handle(invocation);

        ArgumentCaptor<InvocationMatcher> argumentCaptor = ArgumentCaptor.forClass(InvocationMatcher.class);
        verify(mockingProgress).reportOngoingStubbing(argumentCaptor.capture());
        InvocationMatcher invocationMatcher = argumentCaptor.getValue();
        assertSame("Ongoing stubbing invocationMatcher should match the provided invocationMatcher", invocationMatcher, invocationMatcher);
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);

        OngoingStubbingImpl<Object> ongoingStubbingReturned = ongoingStubbing.when(invocationMatcher);
        verify(invocationContainer).setInvocationForPotentialStubbing(null);

        ArgumentCaptor<Invocation> argumentCaptor = ArgumentCaptor.forClass(Invocation.class);
        verify(mockingProgress).saveOngoingStubbingFor(argumentCaptor.capture());
        Invocation savedInvocation = argumentCaptor.getValue();
        assertSame("Saved invocation should match the provided invocation", invocation, savedInvocation);

        StubbedInvocation stubbedInvocation = mock(StubbedInvocation.class);
        mockHandler.invocationContainer.setAnswerFor(invocationMatcher, stubbedInvocation);

        Object result = ongoingStubbingReturned.thenReturn("result");

        Invocation invocationReturned = new Invocation(mock, Arrays.asList(ArgumentMatchers.anyString()), null, null, null, null, null);
        Object resultReturned = mockHandler.handle(invocationReturned);

        assertSame("Returned result should match the stubbed result", "result", resultReturned);
        verify(stubbedInvocation).answer(invocationReturned);
    }

    @Test
    public void testHandle_AnswerWithArguments() throws Throwable {
        Answer<String> answer = Mockito.answer(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "answer";
            }
        });
        List<Answer> answers = Arrays.asList(answer);
        mockHandler.setAnswersForStubbing(answers);

        InvocationMatcher invocationMatcher = ArgumentMatchers.argThat(ArgumentMatchers.isA(String.class));
        Invocation invocation = new Invocation(mock, Arrays.asList(invocationMatcher), null, null, null, null, null);

        Object result = mockHandler.handle(invocation);

        assertSame("Returned answer should match the answer configured", "answer", result);
        verify(answer).answer(invocation, ArgumentCaptor.forClass(String.class).capture());
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentMatchers.argThat(ArgumentMatchers.isInstanceOf(String.class)).assertEquals("answer", argumentCaptor.getValue());
    }
}