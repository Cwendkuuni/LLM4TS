import org.mockito.Mockito;
import org.mockito.internal.MockHandler;
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
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class MockHandlerTest {

    @Test
    public void testHandle_InvocationContainerImplHasAnswersForStubbing_ReturnsNull() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(Mockito.mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(Mockito.mock(org.mockito.internal.matchers.ArgumentMatcherStorage.class));

        MockHandler<Object> mockHandler = new MockHandler<Object>(mock(MockSettingsImpl.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testHandle_VerificationModeIsNull_ReturnsNull() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
        Invocation invocation = Mockito.mock(Invocation.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(Mockito.mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(Mockito.mock(org.mockito.internal.matchers.ArgumentMatcherStorage.class));

        MockHandler<Object> mockHandler = new MockHandler<Object>(mock(MockSettingsImpl.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
    }

    @Test
    public void testHandle_VerificationModeIsMockAwareVerificationMode_ReturnsNull() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(Mockito.mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(Mockito.mock(org.mockito.internal.matchers.ArgumentMatcherStorage.class));
        when(verificationMode.getMock()).thenReturn(invocation.getMock());

        MockHandler<Object> mockHandler = new MockHandler<Object>(mock(MockSettingsImpl.class));
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_InvocationContainerImplFindsStubbedInvocation_ReturnsAnswerResult() throws Throwable {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        MockSettingsImpl mockSettings = Mockito.mock(MockSettingsImpl.class);
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = Mockito.mock(StubbedInvocationMatcher.class);
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        Object expectedResult = new Object();

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(Mockito.mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(Mockito.mock(org.mockito.internal.matchers.ArgumentMatcherStorage.class));
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(expectedResult);
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(expectedResult);

        MockHandler<Object> mockHandler = new MockHandler<Object>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertEquals(expectedResult, result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbable() {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<Object>();
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        Object mock = new Object();

        // Act
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(stubbable);
        assertTrue(stubbable instanceof VoidMethodStubbableImpl);
        assertEquals(mock, ((VoidMethodStubbableImpl) stubbable).getMock());
        assertEquals(invocationContainerImpl, ((VoidMethodStubbableImpl) stubbable).getInvocationContainer());
    }

    @Test
    public void testGetMockSettings_ReturnsMockSettings() {
        // Arrange
        MockSettingsImpl mockSettings = Mockito.mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<Object>(mockSettings);

        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Assert
        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing_SetsAnswersForStubbing() {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        List<Answer> answers = Arrays.asList(Mockito.mock(Answer.class));
        MockHandler<Object> mockHandler = new MockHandler<Object>();
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainerImpl).setAnswersForStubbing(eq(answers));
    }

    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainer() {
        // Arrange
        InvocationContainerImpl invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<Object>();
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertEquals(invocationContainerImpl, result);
    }
}