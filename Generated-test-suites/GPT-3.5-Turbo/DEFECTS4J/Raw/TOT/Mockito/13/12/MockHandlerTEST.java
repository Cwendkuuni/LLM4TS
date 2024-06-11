
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

    private MockHandler mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler(new MockSettingsImpl());
    }

    @Test
    public void testHandle_InvocationContainerImplHasAnswersForStubbing_ReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(invocation.getMock()).thenReturn(mockHandler);

        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        assertNull(mockHandler.handle(invocation));

        verify(mockHandler.invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_VerificationModeIsNull_ReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(invocation.getMock()).thenReturn(mockHandler);

        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(invocationMatcher.getInvocation()).thenReturn(invocation);

        assertNull(mockHandler.handle(invocation));

        verify(mockingProgress).validateState();
        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(mockHandler.invocationContainerImpl).findAnswerFor(invocation);
        verify(invocation).getMock();
    }

    @Test
    public void testHandle_VerificationModeIsNotNull_MockAwareVerificationModeMockSameAsInvocationMock_ReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocation.getMock()).thenReturn(mockHandler);
        when(verificationMode.getMock()).thenReturn(mockHandler);

        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(invocationMatcher.getInvocation()).thenReturn(invocation);

        assertNull(mockHandler.handle(invocation));

        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_VerificationModeIsNotNull_MockAwareVerificationModeMockDifferentFromInvocationMock_ReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        MockHandlerInterface<T> differentMockHandler = mock(MockHandlerInterface.class);

        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocation.getMock()).thenReturn(mockHandler);
        when(verificationMode.getMock()).thenReturn(differentMockHandler);

        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(invocationMatcher.getInvocation()).thenReturn(invocation);

        assertNull(mockHandler.handle(invocation));

        verify(mockingProgress).pullVerificationMode();
        verify(invocation).getMock();
    }

    @Test
    public void testHandle_StubbedInvocationNotNull_CaptureArguments_ReturnAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(invocation.getMock()).thenReturn(mockHandler);

        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(invocationMatcher.getInvocation()).thenReturn(invocation);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("Answer");

        assertEquals("Answer", mockHandler.handle(invocation));

        verify(mockingProgress).validateState();
        verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(mockHandler.invocationContainerImpl).findAnswerFor(invocation);
        verify(invocation).getMock();
    }

    @Test
    public void testHandle_StubbedInvocationNull_ReturnDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArrayList<>());
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(invocation.getMock()).thenReturn(mockHandler);

        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.mockingProgress.validateState()).thenReturn(true);
        when(invocationMatcher.getInvocation()).thenReturn(invocation);
        when(mockHandler.mockSettings.getDefaultAnswer().answer(invocation)).thenReturn("DefaultAnswer");

        assertEquals("DefaultAnswer", mockHandler.handle(invocation));

        verify(mockingProgress).validateState();
        verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.mockSettings).getDefaultAnswer();
    }

    @Test
    public void testVoidMethodStubbable() {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MockHandlerInterface<T> mockHandler = mock(MockHandlerInterface.class);

        mockHandler.invocationContainerImpl = invocationContainer;

        assertNotNull(mockHandler.voidMethodStubbable(mockHandler));
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);

        mockHandler.mockSettings = mockSettings;

        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MockHandlerInterface<T> mockHandler = mock(MockHandlerInterface.class);

        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));

        mockHandler.invocationContainerImpl = invocationContainer;
        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainer).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);

        mockHandler.invocationContainerImpl = invocationContainer;

        assertEquals(invocationContainer, mockHandler.getInvocationContainer());
    }
}
