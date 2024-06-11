
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockHandlerTest {
    private MockHandler<String> mockHandler;
    private MockHandlerInterface<String> mockHandlerInterface;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockHandler = new MockHandler<>(mockSettings);
        mockHandlerInterface = mock(MockHandlerInterface.class);
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(MockingProgress.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle_withAnswersForStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        Answer defaultAnswer = mock(Answer.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verify(mockingProgress, never()).pullVerificationMode();
        verify(mockingProgress, never()).validateState();
        verify(mockingProgress, never()).reportOngoingStubbing(any());
        verify(defaultAnswer, never()).answer(any());
    }

    @Test
    public void testHandle_withoutAnswersForStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        Answer defaultAnswer = mock(Answer.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(mockHandlerInterface.getMockSettings()).thenReturn(mock(MockSettingsImpl.class));

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(any());
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
        verify(defaultAnswer, never()).answer(any());
    }

    @Test
    public void testHandle_withoutAnswersForStubbing_noStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        Answer defaultAnswer = mock(Answer.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(mockHandlerInterface.getMockSettings()).thenReturn(mock(MockSettingsImpl.class));
        when(mockHandlerInterface.getDefaultAnswer()).thenReturn(defaultAnswer);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(any());
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(defaultAnswer).answer(invocation);
    }

    @Test
    public void testVoidMethodStubbable() {
        String mockObject = "mockObject";
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));

        mockHandler.voidMethodStubbable(mockObject);
        verify(invocationContainerImpl).setAnswersForStubbing(eq(answers));
    }
}
