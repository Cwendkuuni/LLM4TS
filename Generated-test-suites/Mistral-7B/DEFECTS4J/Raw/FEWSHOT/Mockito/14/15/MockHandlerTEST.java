package org.mockito.internal.MockHandler;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.Answer;
import org.mockito.DefaultAnswer;
import org.mockito.Invocation;
import org.mockito.InvocationMatcher;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationContainer;
import org.mockito.internal.invocation.InvocationMatcherFactory;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.AnswerImpl;
import org.mockito.stubbing.AnswerProvider;
import org.mockito.stubbing.AnswerWrapper;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testHandle_StubbingIsActive() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mock(Object.class);
        Stubber stubber = mockHandler.voidMethodStubbable(mock);

        Invocation invocation = mock(Invocation.class);
        ArgumentMatcher argumentMatcher = mock(ArgumentMatcher.class);

        when(argumentMatcher.matches(mock)).thenReturn(true);

        stubber.stubVoid(invocation, argumentMatcher);

        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainer).setMethodForStubbing(argumentMatcher);
    }

    @Test
    public void testHandle_VerificationModeIsNotNull() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mock(Object.class);

        Invocation invocation = mock(Invocation.class);
        ArgumentMatcher argumentMatcher = mock(ArgumentMatcher.class);

        VerificationMode verificationMode = mock(VerificationMode.class);
        when(verificationMode.verify(Mockito.any())).thenReturn(null);

        mockingProgress.setVerificationMode(verificationMode);

        mockHandler.handle(invocation);

        verify(invocationMatcherFactory).bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        verify(mockingProgress).validateState();
        verify(verificationMode).verify(ArgumentMatchers.argThat(argumentMatcher));
    }

    @Test
    public void testHandle_NoAnswerIsSet() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mock(Object.class);
        Invocation invocation = mock(Invocation.class);

        Object result = mockHandler.handle(invocation);

        Object defaultAnswerResult = mockSettings.getDefaultAnswer().answer(invocation);
        assertSame(defaultAnswerResult, result);
    }

    @Test
    public void testHandle_AnswerIsSet() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mock(Object.class);
        Stubber stubber = mockHandler.voidMethodStubbable(mock);

        Invocation invocation = mock(Invocation.class);
        ArgumentMatcher argumentMatcher = mock(ArgumentMatcher.class);
        when(argumentMatcher.matches(mock)).thenReturn(true);

        Answer answer = new AnswerImpl(new AnswerProvider() {
            @Override
            public Object call(Invocation invocation) throws Throwable {
                return "answer";
            }
        });

        stubber.doAnswer(answer).when(mock).method(ArgumentMatchers.any());

        Object result = mockHandler.handle(invocation);

        assertSame("answer", result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(argumentMatcher);
    }

    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbable() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = mock(Object.class);

        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertSame(voidMethodStubbable, mockHandler.voidMethodStubbable(mock));
    }

    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainer() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();

        assertSame(invocationContainer, mockHandler.getInvocationContainer());
    }

    @Test
    public void testSetAnswersForStubbing_SetsAnswersForStubbing() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        List<Answer> answers = new ArrayList<>();
        Answer answer = new AnswerWrapper(new AnswerImpl(new AnswerProvider() {
            @Override
            public Object call(Invocation invocation) throws Throwable {
                return "answer";
            }
        }));
        answers.add(answer);

        mockHandler.setAnswersForStubbing(answers);

        Invocation invocation = mock(Invocation.class);
        ArgumentMatcher argumentMatcher = mock(ArgumentMatcher.class);

        Answer answerFromContainer = mockHandler.getInvocationContainer().findAnswerFor(invocation);

        assertSame(answer, answerFromContainer);
    }

    @Test
    public void testGetMockSettings_ReturnsMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        MockSettingsImpl mockSettingsFromHandler = mockHandler.getMockSettings();

        assertSame(mockSettings, mockSettingsFromHandler);
    }
}