import org.junit.Test;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockHandlerTest {
    @Test
    public void testHandle_withAnswersForStubbing_setMethodForStubbingAndReturnNull() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(mock(StubbedInvocationMatcher.class));
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_withVerificationModeAndMockSameAsInvocations_verifyInvocation() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(mock(StubbedInvocationMatcher.class));
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_withVerificationModeAndMockNotSameAsInvocations_skipVerification() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(mock(StubbedInvocationMatcher.class));
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(mock(Object.class));
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        mockHandler.handle(invocation);

        // Assert
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(verificationMode, never()).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_withoutStubbedInvocationAndNoDefaultAnswer_returnDefaultAnswerInvocationResult() throws Throwable {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        MockHandler mockHandler = new MockHandler(mockSettings);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        stub(method(invocationContainerImpl.findAnswerFor(any()))).toReturn(null);
        Object ret = new Object();
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocationContainerImpl.resetInvocationForPotentialStubbing(any())).thenReturn(invocationMatcher);
        when(mockSettings.getDefaultAnswer().answer(eq(invocation))).thenReturn(ret);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.mockSettings = mockSettings;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertEquals(ret, result);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainerImpl, never()).setMethodForStubbing(any());
    }

    @Test
    public void testVoidMethodStubbable_returnsNewVoidMethodStubbableImpl() {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler mockHandler = new MockHandler(mockSettings);
        Object mock = mock(Object.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        // Act
        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertEquals(VoidMethodStubbableImpl.class, result.getClass());
        assertEquals(mock, ((VoidMethodStubbableImpl) result).getMock());
        assertEquals(invocationContainerImpl, ((VoidMethodStubbableImpl) result).getInvocationContainer());
    }

    @Test
    public void testGetMockSettings_returnsMockSettings() {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler mockHandler = new MockHandler(mockSettings);

        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Assert
        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing_setsAnswersInInvocationContainer() {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        List<Answer> answers = new ArrayList<>();
        MockHandler mockHandler = new MockHandler(mockSettings);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainerImpl).setAnswersForStubbing(eq(answers));
    }

    @Test
    public void testGetInvocationContainer_returnsInvocationContainer() {
        // Arrange
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler mockHandler = new MockHandler(mockSettings);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertEquals(invocationContainerImpl, result);
    }
}