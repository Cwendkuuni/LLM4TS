import org.junit.Before;
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
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler();
    }

    @Test
    public void testHandle_stubbingVoidWithStubVoid() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        doReturn(true).when(invocationContainerImpl).hasAnswersForStubbing();
        doReturn(matchersBinder).when(mockHandler).getMockingProgress();
        doReturn(invocationContainerImpl).when(mockHandler).getInvocationContainerImpl();
        doNothing().when(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));

        // Run
        Object result = mockHandler.handle(invocation);

        // Verify
        assertNull(result);
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockHandler).getMockingProgress();
        verify(mockHandler).getInvocationContainerImpl();
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testHandle_verificationModeIsNull() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = null;
        doReturn(matchersBinder).when(mockHandler).getMockingProgress();
        doReturn(verificationMode).when(mockingProgress).pullVerificationMode();
        doReturn(invocationContainerImpl).when(mockHandler).getInvocationContainerImpl();
        doNothing().when(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        doReturn(null).when(invocationContainerImpl).findAnswerFor(invocation);
        doReturn(null).when(mockHandler).invokeDefaultAnswer(any(), any());

        // Run
        Object result = mockHandler.handle(invocation);

        // Verify
        assertNull(result);
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockHandler).getMockingProgress();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(mockHandler).invokeDefaultAnswer(any(), any());
    }

    @Test
    public void testHandle_verificationModeIsMockAwareVerificationMode() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        doReturn(matchersBinder).when(mockHandler).getMockingProgress();
        doReturn(verificationMode).when(mockingProgress).pullVerificationMode();
        doReturn(invocationContainerImpl).when(mockHandler).getInvocationContainerImpl();
        doReturn(invocation.getMock()).when(((MockAwareVerificationMode) verificationMode)).getMock();
        doNothing().when(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        doReturn(null).when(invocationContainerImpl).findAnswerFor(invocation);

        // Run
        Object result = mockHandler.handle(invocation);

        // Verify
        assertNull(result);
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockHandler).getMockingProgress();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(invocation);
    }

    @Test
    public void testHandle_stubbedInvocationIsNull() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = null;
        Answer defaultAnswer = mock(Answer.class);
        doReturn(matchersBinder).when(mockHandler).getMockingProgress();
        doReturn(verificationMode).when(mockingProgress).pullVerificationMode();
        doReturn(invocationContainerImpl).when(mockHandler).getInvocationContainerImpl();
        doNothing().when(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        doReturn(null).when(invocationContainerImpl).findAnswerFor(invocation);
        doReturn(defaultAnswer).when(mockHandler).getDefaultAnswer();
        doReturn(null).when(mockHandler).invokeDefaultAnswer(any(), any());

        // Run
        Object result = mockHandler.handle(invocation);

        // Verify
        assertNull(result);
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockHandler).getMockingProgress();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(mockHandler).getDefaultAnswer();
        verify(mockHandler).invokeDefaultAnswer(any(), any());
    }

    @Test
    public void testHandle_stubbedInvocationIsNotNull() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = null;
        Answer defaultAnswer = mock(Answer.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        doReturn(matchersBinder).when(mockHandler).getMockingProgress();
        doReturn(verificationMode).when(mockingProgress).pullVerificationMode();
        doReturn(invocationContainerImpl).when(mockHandler).getInvocationContainerImpl();
        doNothing().when(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        doReturn(stubbedInvocation).when(invocationContainerImpl).findAnswerFor(invocation);
        doReturn(defaultAnswer).when(mockHandler).getDefaultAnswer();
        doReturn(null).when(stubbedInvocation).answer(invocation);

        // Run
        Object result = mockHandler.handle(invocation);

        // Verify
        assertNull(result);
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockHandler).getMockingProgress();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(mockHandler).getDefaultAnswer();
        verify(stubbedInvocation).answer(invocation);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandle_stubbedInvocationIsNull_invokeDefaultAnswerNotNull() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = null;
        Answer defaultAnswer = mock(Answer.class);
        doReturn(matchersBinder).when(mockHandler).getMockingProgress();
        doReturn(verificationMode).when(mockingProgress).pullVerificationMode();
        doReturn(invocationContainerImpl).when(mockHandler).getInvocationContainerImpl();
        doNothing().when(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        doReturn(null).when(invocationContainerImpl).findAnswerFor(invocation);
        doReturn(defaultAnswer).when(mockHandler).getDefaultAnswer();
        doReturn(new Object()).when(mockHandler).invokeDefaultAnswer(any(), any());

        // Run
        Object result = mockHandler.handle(invocation);

        // Verify
        assertNotNull(result);
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockHandler).getMockingProgress();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(mockHandler).getDefaultAnswer();
        verify(mockHandler).invokeDefaultAnswer(any(), any());
    }

    @Test
    public void testVoidMethodStubbable() {
        // Setup
        Object mock = new Object();
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        VoidMethodStubbable voidMethodStubbable = mock(VoidMethodStubbableImpl.class);
        doReturn(invocationContainerImpl).when(mockHandler).getInvocationContainerImpl();

        // Run
        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);

        // Verify
        assertEquals(voidMethodStubbable, result);
        verify(mockHandler).getInvocationContainerImpl();
    }

    @Test
    public void testGetMockSettings() {
        // Setup
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler(mockSettings);

        // Run
        MockSettingsImpl result = mockHandler.getMockSettings();

        // Verify
        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        // Setup
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        doReturn(invocationContainerImpl).when(mockHandler).getInvocationContainerImpl();

        // Run
        mockHandler.setAnswersForStubbing(answers);

        // Verify
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
        verify(mockHandler).getInvocationContainerImpl();
    }

    @Test
    public void testGetInvocationContainer() {
        // Setup
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        doReturn(invocationContainerImpl).when(mockHandler).getInvocationContainerImpl();

        // Run
        InvocationContainer result = mockHandler.getInvocationContainer();

        // Verify
        assertEquals(invocationContainerImpl, result);
        verify(mockHandler).getInvocationContainerImpl();
    }
}