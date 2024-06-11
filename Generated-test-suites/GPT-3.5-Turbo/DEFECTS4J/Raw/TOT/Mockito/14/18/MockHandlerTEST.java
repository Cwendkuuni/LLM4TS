import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationData;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<String> mockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>(new MockHandlerInterfaceImpl());
    }

    @Test
    public void handle_StubbingVoidMethod_InvokesMatcherBinder() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        
        mockHandler.handle(invocation);
        
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
    }

    @Test
    public void handle_VerificationModeIsNotNull_VerifyInvocationData() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(mock(VerificationMode.class));
        
        mockHandler.handle(invocation);
        
        verifyVerificationMode(mockingProgress, invocationContainerImpl);
    }

    @Test
    public void handle_NoStubbedInvocation_InvokesDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn("defaultAnswer");
        
        Object result = mockHandler.handle(invocation);
        
        verify(mockSettings).getDefaultAnswer();
        assertEquals("defaultAnswer", result);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any());
    }

    @Test
    public void voidMethodStubbable_ValidMock_ReturnsVoidMethodStubbableImpl() {
        String mock = "mock";
        
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
        
        assertEquals(VoidMethodStubbableImpl.class, stubbable.getClass());
        assertSame(mock, ((VoidMethodStubbableImpl) stubbable).getMock());
        assertSame(invocationContainerImpl, ((VoidMethodStubbableImpl) stubbable).getInvocationContainer());
    }

    @Test
    public void setAnswersForStubbing_ValidAnswers_SetAnswerForStubbingInInvocationContainer() {
        List<Answer> answers = Arrays.asList(mock(Answer.class));
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainerImpl).setAnswersForStubbing(eq(answers));
    }

    private void verifyVerificationMode(MockingProgress mockingProgress, InvocationContainerImpl invocationContainerImpl) {
        // Verify that verification mode is pulled from mocking progress
        verify(mockingProgress).pullVerificationMode();
        
        // Verify that verification data is created with correct invocations and invocation matcher
        verifyNew(VerificationDataImpl.class).withArguments(eq(invocationContainerImpl.getInvocations()), any(InvocationMatcher.class));
        
        // Verify that the created verification data is used for verification
        verify(verificationMode).verify(any(VerificationData.class));
    }

    class MockHandlerInterfaceImpl implements MockHandlerInterface<String> {
        @Override
        public MockSettingsImpl getMockSettings() {
            return mockSettings;
        }
    }
}