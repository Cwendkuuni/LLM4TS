package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.List;
import org.mockito.internal.creation.MockSettingsImpl;
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
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import org.junit.Before;
import org.junit.Test;

public class MockHandlerTest {

    private MockHandler<TestClass> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockSettings = mock(MockSettingsImpl.class);
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle_withStubbingVoid() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        ArgumentMatcherStorage argumentMatcherStorage = mock(ArgumentMatcherStorage.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(invocationMatcher);
        mockHandler.handle(invocation);
        verify(invocationContainerImpl, times(1)).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        ArgumentMatcherStorage argumentMatcherStorage = mock(ArgumentMatcherStorage.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(invocationMatcher);
        mockingProgress.pushVerificationMode(verificationMode);
        mockHandler.handle(invocation);
        verify(invocationContainerImpl, never()).setMethodForStubbing(invocationMatcher);
        verify(mockingProgress, times(1)).pullVerificationMode();
    }

    @Test
    public void testHandle_withStubbingAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        ArgumentMatcherStorage argumentMatcherStorage = mock(ArgumentMatcherStorage.class);
        List<Answer> answers = mock(List.class);
        Answer defaultAnswer = mock(Answer.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("default answer");
        when(invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher)).thenReturn(invocationMatcher);
        String result = (String) mockHandler.handle(invocation);
        assertEquals("default answer", result);
        verify(invocationContainerImpl, times(1)).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockSettings, times(1)).getDefaultAnswer();
        verify(invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable() {
        TestClass mock = mock(TestClass.class);
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
        assertEquals(VoidMethodStubbableImpl.class, stubbable.getClass());
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl result = mockHandler.getMockSettings();
        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        mockHandler.setAnswersForStubbing(answers);
        verify(invocationContainerImpl, times(1)).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer result = mockHandler.getInvocationContainer();
        assertEquals(invocationContainerImpl, result);
    }
}

class TestClass {
    // empty class for testing purposes
}