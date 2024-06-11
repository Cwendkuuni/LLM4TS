import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockHandlerTest {

    @Test
    public void testHandleWithAnswersForStubbing() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).hasAnswersForStubbing();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
        assertNull(result);
    }

    @Test
    public void testHandleWithVerificationModeAndSameMock() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);

        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        when(verificationMode.getMock()).thenReturn(invocation.getMock());

        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(invocationContainer).setInvocationForPotentialStubbing(any());
        verify(mockingProgress).reportOngoingStubbing(any());
        verify(invocationContainer).findAnswerFor(any());
        assertNull(result);
    }

    @Test
    public void testHandleWithVerificationModeAndDifferentMock() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);

        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        when(verificationMode.getMock()).thenReturn(mock(Invocation.class).getMock());

        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(invocationContainer, never()).setInvocationForPotentialStubbing(any());
        verify(mockingProgress, never()).reportOngoingStubbing(any());
        verify(invocationContainer, never()).findAnswerFor(any());
        assertNull(result);
    }

    @Test
    public void testHandleWithStubbedInvocation() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

        when(invocationContainer.findAnswerFor(invocation)).thenReturn(mock(StubbedInvocationMatcher.class));

        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = mock(MatchersBinder.class);
        mockHandler.mockingProgress = mock(MockingProgress.class);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).findAnswerFor(invocation);
        assertNotNull(result);
    }

    @Test
    public void testHandleWithoutStubbedInvocation() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.matchersBinder = mock(MatchersBinder.class);
        mockHandler.mockingProgress = mock(MockingProgress.class);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verify(invocationContainer).findAnswerFor(invocation);
        assertNotNull(result);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable() {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;

        // Act
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mockSettings);

        // Assert
        assertNotNull(stubbable);
    }

    @Test
    public void testSetAnswersForStubbing() {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;

        // Act
        mockHandler.setAnswersForStubbing(mock(List.class));

        // Assert
        verify(invocationContainer).setAnswersForStubbing(any());
    }

    @Test
    public void testGetInvocationContainer() {
        // Arrange
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainer;

        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertEquals(invocationContainer, result);
    }
}