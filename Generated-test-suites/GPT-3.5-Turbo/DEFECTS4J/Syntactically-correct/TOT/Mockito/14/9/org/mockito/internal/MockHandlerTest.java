package org.mockito.internal;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
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
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

public class MockHandlerTest {

    private MockHandler<TestObject> mockHandler;
    private Invocation invocation;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        invocation = mock(Invocation.class);
    }

    @Test
    public void testHandle_WhenInvocationContainerImplHasAnswersForStubbing_ReturnsNull() throws Throwable {
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(mockingProgress).getArgumentMatcherStorage();
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testHandle_WhenVerificationModeIsNotNull_VerifyInvoked() throws Throwable {
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(mockingProgress).getArgumentMatcherStorage();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress).validateState();
        verify(invocationContainerImpl).getInvocations();
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_WhenStubbedInvocationIsNull_ReturnsDefaultAnswerResult() throws Throwable {
        Answer<Object> defaultAnswer = mock(Answer.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(defaultAnswer.answer(invocation)).thenReturn("mocked result");

        Object result = mockHandler.handle(invocation);

        assertEquals("mocked result", result);
        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        TestObject mock = new TestObject();
        VoidMethodStubbable<T> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(voidMethodStubbable);
        assertTrue(voidMethodStubbable instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void testGetMockSettings_ReturnsMockSettingsImpl() {
        MockSettingsImpl mockSettingsResult = mockHandler.getMockSettings();

        assertNotNull(mockSettingsResult);
        assertTrue(mockSettingsResult instanceof MockSettingsImpl);
    }

    @Test
    public void testSetAnswersForStubbing_InvokesInvocationContainerSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainerImpl() {
        InvocationContainer invocationContainerResult = mockHandler.getInvocationContainer();

        assertNotNull(invocationContainerResult);
        assertTrue(invocationContainerResult instanceof InvocationContainerImpl);
    }

    class TestObject { }
}