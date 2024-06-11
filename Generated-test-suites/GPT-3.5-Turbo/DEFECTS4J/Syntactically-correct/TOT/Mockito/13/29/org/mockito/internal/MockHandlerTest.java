import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>(new MockHandlerInterface<Object>() {
            @Override
            public MockSettingsImpl getMockSettings() {
                return null;
            }
        });
    }

    @Test
    public void handle_shouldReturnNullForStubbedVoidMethod() throws Throwable {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
        when(invocationContainer.getMethodForStubbing(invocationMatcher)).thenReturn(null);
        when(mockHandler.handle(invocation)).thenCallRealMethod();

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainer).hasAnswersForStubbing();
        verify(mockHandler).getInvocationContainer();
        verify(invocationContainer).getMethodForStubbing(invocationMatcher);
        verify(invocationContainer).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void handle_shouldVerifyInvocationWhenVerificationModeIsNotNullAndMockMatches() throws Throwable {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        ThreadSafeMockingProgress threadSafeMockingProgress = mock(ThreadSafeMockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        DataVerificationMode dataVerificationMode = mock(DataVerificationMode.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
        when(invocationContainer.getInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);
        when(mockHandler.handle(invocation)).thenCallRealMethod();
        when(threadSafeMockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());
        when(mockHandler.getMockingProgress()).thenReturn(threadSafeMockingProgress);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainer).hasAnswersForStubbing();
        verify(mockHandler).getInvocationContainer();
        verify(invocationContainer).getInvocationForPotentialStubbing(invocationMatcher);
        verify(threadSafeMockingProgress).pullVerificationMode();
        verify(verificationMode).verify(isA(VerificationDataImpl.class));
    }

    @Test
    public void handle_shouldSetInvocationForPotentialStubbingWhenVerificationModeIsNull() throws Throwable {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        ThreadSafeMockingProgress threadSafeMockingProgress = mock(ThreadSafeMockingProgress.class);
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
        when(invocationContainer.getInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);
        when(mockHandler.handle(invocation)).thenCallRealMethod();
        when(threadSafeMockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockHandler.getMockingProgress()).thenReturn(threadSafeMockingProgress);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainer).hasAnswersForStubbing();
        verify(mockHandler).getInvocationContainer();
        verify(invocationContainer).getInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainer).setInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainer, never()).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void handle_shouldCaptureArgumentsAndReturnAnswerWhenStubbedInvocationFound() throws Throwable {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        when(invocationContainer.hasAnswersForStubbing()).thenReturn(false);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
        when(invocationContainer.getInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);
        when(mockHandler.handle(invocation)).thenCallRealMethod();
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("answer");
        when(invocationContainer.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(invocationContainer.resetInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        assertEquals("answer", result);
        verify(invocationContainer).hasAnswersForStubbing();
        verify(mockHandler).getInvocationContainer();
        verify(invocationContainer).getInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainer).findAnswerFor(invocation);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void voidMethodStubbable_shouldReturnNewVoidMethodStubbableInstance() {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        Object mock = new Object();
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);

        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(stubbable);
        assertTrue(stubbable instanceof VoidMethodStubbable);
        assertEquals(mock, stubbable.getMock());
    }

    @Test
    public void setAnswersForStubbing_shouldSetAnswersForStubbingInInvocationContainer() {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainer).setAnswersForStubbing(answers);
    }
}