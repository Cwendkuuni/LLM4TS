package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.matchers.ArgumentMatchers.any;
import static org.mockito.internal.matchers.ArgumentMatchers.eq;
import static org.mockito.internal.matchers.ArgumentMatchers.isNull;
import static org.mockito.internal.matchers.ArgumentMatchers.notNull;
import static org.mockito.internal.verification.VerificationModeFactory.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.verifyZeroInteractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Answer3;
import org.mockito.stubbing.Answer4;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.AnswerWithMock;
import org.mockito.stubbing.Stub;
import org.mockito.stubbing.Stubber;

import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsImpl.DefaultAnswer;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<MockitoTestObject> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainer invocationContainer;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;
    private Stubber stubber;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler<MockitoTestObject>(mockSettings);
        invocationContainer = mockHandler.getInvocationContainer();
        matchersBinder = mockHandler.matchersBinder;
        mockingProgress = mockHandler.mockingProgress;
        stubber = Mockito.mock(Stubber.class);
    }

    @Test
    public void testHandle_stubbingVoidMethods() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = matchersBinder.bind(eq("methodName"), any());

        // when
        mockHandler.stubVoid("methodName");
        mockHandler.handle(invocation);

        // then
        assertSame(invocationMatcher, invocationContainer.getMethodForStubbing());
        assertNull(invocationContainer.getAnswerFor(invocation));
    }

    @Test
    public void testHandle_stubbingAnswer() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = matchersBinder.bind(eq("methodName"), any());

        Answer answer = Mockito.mock(Answer.class);

        // when
        mockHandler.when(Mockito.mock(Class.class)).thenAnswer(answer);
        mockHandler.handle(invocation);

        // then
        assertSame(invocationMatcher, invocationContainer.getMethodForStubbing());
        assertSame(answer, invocationContainer.getAnswerFor(invocation));
    }

    @Test
    public void testHandle_stubbingAnswerWithMock() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = matchersBinder.bind(eq("methodName"), any());

        MockitoTestObject mock = Mockito.mock(MockitoTestObject.class);

        AnswerWithMock<MockitoTestObject> answer = Mockito.mock(AnswerWithMock.class);
        answer.answer(Mockito.eq(mock));

        // when
        mockHandler.when(Mockito.mock(Class.class)).thenAnswer(answer);
        mockHandler.handle(invocation);

        // then
        assertSame(invocationMatcher, invocationContainer.getMethodForStubbing());
        assertSame(mock, invocationContainer.getCapturedArgumentFor(0));
    }

    @Test
    public void testHandle_stubbingAnswerWithCapture() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = matchersBinder.bind(eq("methodName"), any());

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Answer<String> answer = Mockito.mock(Answer.class);
        answer.answers(new AnswerStub<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return (String) invocation.getArgument(0);
            }
        });

        // when
        mockHandler.when(Mockito.mock(Class.class)).thenAnswer(answer);
        mockHandler.handle(invocation);

        // then
        assertSame(invocationMatcher, invocationContainer.getMethodForStubbing());
        assertSame("expected", invocationContainer.getCapturedArgumentFor(0));
    }

    @Test
    public void testHandle_stubbingAnswerWithArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = matchersBinder.bind(eq("methodName"), any());

        ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);

        Answer<Void> answer = Mockito.mock(Answer.class);
        answer.answers(new AnswerStub<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String arg1 = argumentCaptor1.getValue();
                Integer arg2 = argumentCaptor2.getValue();
                return null;
            }
        });

        // when
        mockHandler.when(Mockito.mock(Class.class)).thenAnswer(answer);
        mockHandler.handle(invocation);

        // then
        assertSame(invocationMatcher, invocationContainer.getMethodForStubbing());
        assertSame("expected1", argumentCaptor1.getValue());
        assertEquals(expected2, argumentCaptor2.getValue());
    }

    @Test
    public void testHandle_stubbingAnswerWithMultipleArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = matchersBinder.bind(eq("methodName"), any());

        ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Long> argumentCaptor3 = ArgumentCaptor.forClass(Long.class);

        Answer<Object[]> answer = Mockito.mock(Answer.class);
        answer.answers(new Answer2<Object[], Object[]>() {
            @Override
            public Object[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return new Object[]{ argumentCaptor1.getValue(), argumentCaptor2.getValue(), argumentCaptor3.getValue() };
            }
        });

        // when
        mockHandler.when(Mockito.mock(Class.class)).thenAnswer(answer);
        mockHandler.handle(invocation);

        // then
        assertSame(invocationMatcher, invocationContainer.getMethodForStubbing());
        assertSame("expected1", argumentCaptor1.getValue());
        assertEquals(expected2, argumentCaptor2.getValue());
        assertEquals(expected3, argumentCaptor3.getValue());
    }

    @Test
    public void testHandle_stubbingAnswerWithThreeArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = matchersBinder.bind(eq("methodName"), any());

        ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Long> argumentCaptor3 = ArgumentCaptor.forClass(Long.class);

        Answer<Object[]> answer = Mockito.mock(Answer.class);
        answer.answers(new Answer3<Object[], Object[], Object[]>() {
            @Override
            public Object[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return new Object[]{ args[0], args[1], args[2] };
            }
        });

        // when
        mockHandler.when(Mockito.mock(Class.class)).thenAnswer(answer);
        mockHandler.handle(invocation);

        // then
        assertSame(invocationMatcher, invocationContainer.getMethodForStubbing());
        assertSame("expected1", argumentCaptor1.getValue());
        assertEquals(expected2, argumentCaptor2.getValue());
        assertEquals(expected3, argumentCaptor3.getValue());
    }

    @Test
    public void testHandle_stubbingAnswerWithFourArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = matchersBinder.bind(eq("methodName"), any());

        ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Long> argumentCaptor3 = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Double> argumentCaptor4 = ArgumentCaptor.forClass(Double.class);

        Answer<Object[]> answer = Mockito.mock(Answer.class);
        answer.answers(new Answer4<Object[], Object[], Object[], Object[]>() {
            @Override
            public Object[] answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return new Object[]{ args[0], args[1], args[2], args[3] };
            }
        });

        // when
        mockHandler.when(Mockito.mock(Class.class)).thenAnswer(answer);
        mockHandler.handle(invocation);

        // then
        assertSame(invocationMatcher, invocationContainer.getMethodForStubbing());
        assertSame("expected1", argumentCaptor1.getValue());
        assertEquals(expected2, argumentCaptor2.getValue());
        assertEquals(expected3, argumentCaptor3.getValue());
        assertEquals(expected4, argumentCaptor4.getValue());
    }

    @Test
    public void testHandle_verify() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = matchersBinder.bind(eq("methodName"), any());

        VerificationMode verificationMode = verifyZeroInteractions(Mockito.mock(Class.class));

        mockHandler.handle(invocation);

        // then
        assertSame(verificationMode, mockingProgress.getVerificationMode());
        verifyZeroInteractions(Mockito.mock(Class.class));

        verificationMode = verifyNoMoreInteractions(Mockito.mock(Class.class));
        mockHandler.handle(invocation);

        // then
        assertSame(verificationMode, mockingProgress.getVerificationMode());
        verifyNoMoreInteractions(Mockito.mock(Class.class));
    }

    @Test
    public void testHandle_resetInvocationForPotentialStubbing() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = matchersBinder.bind(eq("methodName"), any());

        Object answer = mockSettings.getDefaultAnswer().answer(invocation);

        mockHandler.handle(invocation);

        // when
        mockHandler.handle(invocation);

        // then
        assertSame(invocationMatcher, invocationContainer.getMethodForStubbing());
        assertSame(answer, invocationContainer.getReturnValue());

        invocationContainer.setInvocationForPotentialStubbing(invocationMatcher);

        // when
        mockHandler.handle(invocation);

        // then
        assertNull(invocationContainer.getMethodForStubbing());
        assertNull(invocationContainer.getReturnValue());
    }

}