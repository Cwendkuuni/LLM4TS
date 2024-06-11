
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {
    @Test
    public void testHandle_WhenInvocationContainerImplHasAnswersForStubbing_ReturnsNull() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);

        MockHandler<Object> mockHandler = new MockHandler<>(mock(MockitMockSettings.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        // Act
        Object result = mockHandler.handle(invocation);
        // Assert
        assertNull(result);
    }

    @Test
    public void testHandle_WhenVerificationModeIsNull_ReturnsNull() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);

        MockHandler<Object> mockHandler = new MockHandler<>(mock(MockitMockSettings.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.matchersBinder = matchersBinder;
        // Act
        Object result = mockHandler.handle(invocation);
        // Assert
        assertNull(result);
    }

    @Test
    public void testHandle_WhenVerificationModeInstanceOfMockAwareVerificationModeAndMockMatchesInvocation_MocksVerificationData() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(verificationMode.getMock()).thenReturn(invocation.getMock());
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        
        MockHandler<Object> mockHandler = new MockHandler<>(mock(MockitMockSettings.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        // Act
        Object result = mockHandler.handle(invocation);
        // Assert
        assertNull(result);
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_WhenVerificationModeInstanceOfMockAwareVerificationModeAndMockDoesNotMatchInvocation_MocksVerificationData() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(verificationMode.getMock()).thenReturn(null);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        
        MockHandler<Object> mockHandler = new MockHandler<>(mock(MockitMockSettings.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        // Act
        Object result = mockHandler.handle(invocation);
        // Assert
        assertNull(result);
        verify(verificationMode, never()).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_WhenStubbedInvocationNotNull_CapturesArgumentsFromInvocationAndReturnsStubbedAnswer() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        
        MockHandler<Object> mockHandler = new MockHandler<>(mock(MockitMockSettings.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        // Act
        Object result = mockHandler.handle(invocation);
        // Assert
        assertNotNull(result);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void testHandle_WhenStubbedInvocationIsNull_ReturnsDefaultAnswer() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        // Act
        Object result = mockHandler.handle(invocation);
        // Assert
        assertNotNull(result);
        verify(defaultAnswer).answer(invocation);
    }

    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mock(MockitMockSettings.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        Object mock = new Object();
        // Act
        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void testSetAnswersForStubbing_SetsAnswersForStubbing() {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mock(MockitMockSettings.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        List<Answer<Object>> answers = mock(List.class);
        // Act
        mockHandler.setAnswersForStubbing(answers);
        // Assert
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainerImpl() {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mock(MockitMockSettings.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        // Act
        InvocationContainerImpl result = mockHandler.getInvocationContainer();
        // Assert
        assertNotNull(result);
        assertEquals(result, invocationContainerImpl);
    }
}

