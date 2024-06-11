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
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<>(new MockSettingsImpl());
    }

    @Test
    public void handle_withStubbingVoidMethod_stubsInvocationAndReturnsNull() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        ArgumentMatcherStorage argumentMatcherStorage = mock(ArgumentMatcherStorage.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(invocationContainerImpl).hasAnswersForStubbing();
        verify(matchersBinder).bindMatchers(argumentMatcherStorage, invocation);
        verify(mockingProgress).getArgumentMatcherStorage();
    }

    @Test
    public void handle_withVerificationMode_verifyInvokesVerifyOnVerificationMode() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        ArgumentMatcherStorage argumentMatcherStorage = mock(ArgumentMatcherStorage.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockAwareVerificationMode.getMock()).thenReturn(invocation.getMock());
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(mockingProgress).validateState();
        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(argumentMatcherStorage, invocation);
        verify(mockingProgress).getArgumentMatcherStorage();
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void handle_withStubbedInvocation_returnsStubbedResult() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocationMatcher);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("Mocked Result");

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNotNull(result);
        assertEquals("Mocked Result", result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).validateState();
        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(any(ArgumentMatcherStorage.class), eq(invocation));
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(stubbedInvocationMatcher).answer(invocation);
    }

    @Test
    public void handle_withNoStubbedInvocation_returnsDefaultAnswer() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        DefaultAnswer defaultAnswerImplementation = mock(DefaultAnswer.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.as(ERROR_)).thenReturn(defaultAnswerImplementation);
        when(defaultAnswerImplementation.answer(invocation)).thenReturn("Default Result");

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNotNull(result);
        assertEquals("Default Result", result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).validateState();
        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(any(ArgumentMatcherStorage.class), eq(invocation));
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(mockSettings).getDefaultAnswer();
        verify(defaultAnswer).as(ERROR_);
        verify(defaultAnswerImplementation).answer(invocation);
    }

    @Test
    public void voidMethodStubbable_returnsVoidMethodStubbable() {
        // Arrange
        Object mock = new Object();

        // Act
        VoidMethodStubbable<Object> result = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void getMockSettings_returnsMockSettings() {
        // Act
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MockSettingsImpl);
    }

    @Test
    public void setAnswersForStubbing_setsAnswersForStubbing() {
        // Arrange
        List<Answer> answers = new ArrayList<>();

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        assertEquals(answers, mockHandler.invocationContainerImpl.getAnswersForStubbing());
    }

    @Test
    public void getInvocationContainer_returnsInvocationContainer() {
        // Act
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof InvocationContainerImpl);
    }
}