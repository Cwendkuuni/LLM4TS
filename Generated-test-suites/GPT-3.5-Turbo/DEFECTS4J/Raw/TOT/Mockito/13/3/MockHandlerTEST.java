import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    @Mock
    MockSettingsImpl mockSettings;

    @Mock
    InvocationContainerImpl invocationContainerImpl;

    MockHandler<MockHandlerInterface> mockHandler;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandle_hasAnswersForStubbing_returnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);

        assertNull(mockHandler.handle(invocation));

        verify(invocationContainerImpl).hasAnswersForStubbing();
        verifyZeroInteractions(invocationContainerImpl);
    }

    @Test
    public void testHandle_verificationModeIsNotNullAndVerificationModeMockIsSameAsInvocationMock_verify() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Object mock = Mockito.mock(Object.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(mock);
        when(invocation.getMock()).thenReturn(mock);

        assertNull(mockHandler.handle(invocation));

        verify(mockingProgress).validateState();
        verify(invocationContainerImpl, never()).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress).pullVerificationMode();
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_verificationModeIsNull_returnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);

        assertNull(mockHandler.handle(invocation));

        verify(mockingProgress).validateState();
        verify(invocationContainerImpl, never()).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress).pullVerificationMode();
        verifyZeroInteractions(invocationContainerImpl);
    }

    @Test
    public void testHandle_stubbedInvocationNotNull_returnStubbedInvocationAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer<Object> answer = mock(Answer.class);

        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(answer);

        assertEquals(answer, mockHandler.handle(invocation));

        verify(mockingProgress).validateState();
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
    }

    @Test
    public void testHandle_stubbedInvocationNull_returnDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Answer<Object> defaultAnswer = mock(Answer.class);

        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("DefaultAnswer");

        assertEquals("DefaultAnswer", mockHandler.handle(invocation));

        verify(mockingProgress).validateState();
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(defaultAnswer).answer(invocation);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testVoidMethodStubbable_verifyOngoingStubbingReported() {
        T mock = mock(T.class);
        OngoingStubbingImpl<T> ongoingStubbing = mock(OngoingStubbingImpl.class);

        when(mockHandler.invocationContainerImpl).thenReturn(invocationMock);

        mockHandler.voidMethodStubbable(mock);

        verify(mockingProgress).reportOngoingStubbing(ongoingStubbing);
        verify(invocationMock).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testSetAnswersForStubbing_invocationContainerImplSetAnswersForStubbingCalled() {
        List<Answer> answers = new ArrayList<>();

        mockHandler.setAnswersForStubbing(answers);

        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_returnInvocationContainerImpl() {
        assertEquals(invocationContainerImpl, mockHandler.getInvocationContainer());
    }

    @Test
    public void testGetMockSettings_returnMockSettings() {
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }
}