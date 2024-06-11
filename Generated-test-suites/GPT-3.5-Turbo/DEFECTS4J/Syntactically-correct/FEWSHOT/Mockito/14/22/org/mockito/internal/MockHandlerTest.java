package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.List;

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

public class MockHandlerTest {
    
    private MockHandler<Object> mockHandler;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
        mockingProgress = new ThreadSafeMockingProgress();
        matchersBinder = new MatchersBinder();
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockHandler = spy(new MockHandler<Object>(mockSettings));
        doReturn(mockingProgress).when(mockHandler).getMockingProgress();
        doReturn(matchersBinder).when(mockHandler).getMatchersBinder();
        doReturn(invocationContainerImpl).when(mockHandler).getInvocationContainer();
    }

    @Test
    public void testHandle_whenInvocationContainerHasAnswersForStubbing_shouldSetMethodForStubbingAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder.ArgumentMatcherStorage argumentMatcherStorage = mock(MatchersBinder.ArgumentMatcherStorage.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_whenVerificationModeNotNull_shouldVerifyAndReturnNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder.ArgumentMatcherStorage argumentMatcherStorage = mock(MatchersBinder.ArgumentMatcherStorage.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(mockingProgress).validateState();
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_whenStubbedInvocationNotNull_shouldCaptureArgumentsAndReturnAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder.ArgumentMatcherStorage argumentMatcherStorage = mock(MatchersBinder.ArgumentMatcherStorage.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(invocationMatcher);
        when(stubbedInvocation.answer(invocation)).thenReturn("answer");

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertEquals("answer", result);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
    }

    @Test
    public void testHandle_whenStubbedInvocationNull_shouldReturnDefaultAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder.ArgumentMatcherStorage argumentMatcherStorage = mock(MatchersBinder.ArgumentMatcherStorage.class);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(invocationMatcher);
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn("defaultAnswer");

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertEquals("defaultAnswer", result);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable_shouldReturnVoidMethodStubbableImpl() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        VoidMethodStubbable<?> stubbable = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertEquals(VoidMethodStubbableImpl.class, stubbable.getClass());
    }

    @Test
    public void testGetMockSettings_shouldReturnMockSettings() {
        // Act
        MockSettingsImpl settings = mockHandler.getMockSettings();

        // Assert
        assertSame(mockSettings, settings);
    }

    @Test
    public void testSetAnswersForStubbing_shouldSetAnswersForStubbing() {
        // Arrange
        List<Answer> answers = mock(List.class);

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_shouldReturnInvocationContainer() {
        // Act
        InvocationContainer container = mockHandler.getInvocationContainer();

        // Assert
        assertSame(invocationContainerImpl, container);
    }
}