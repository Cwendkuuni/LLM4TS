import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<>();
    }

    @Test
    public void handle_InvocationContainerImplHasAnswersForStubbing_ReturnsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);

        when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
        when(invocationContainer.getMethodForStubbing()).thenReturn(invocation);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainer).hasAnswersForStubbing();
        verify(invocationContainer).getMethodForStubbing();
    }

    @Test
    public void handle_VerificationModeIsNull_ReturnsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = null;

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl).hasAnswersForStubbing();
        verify(mockingProgress).pullVerificationMode();
    }
    
    @Test
    public void handle_VerificationModeIsNotNullAndMockAwareVerificationModeAndMocksMatch_VerifyInvoked() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());
        
        mockHandler.handle(invocation);
        
        verify(mockingProgress).pullVerificationMode();
        verify((MockAwareVerificationMode) verificationMode).getMock();
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void voidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        Object mock = mock(Object.class);
        VoidMethodStubbableImpl stubbable = mockHandler.voidMethodStubbable(mock);

        assertEquals(mock, stubbable.getMock());
        assertEquals(invocationContainerImpl, stubbable.getInvocationContainer());
    }

    @Test
    public void setAnswersForStubbing_SetsAnswersForStubbingInInvocationContainer() {
        List<Answer> answers = new ArrayList<>();

        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, invocationContainerImpl.getAnswersForStubbing());
    }

    @Test
    public void getInvocationContainer_ReturnsInvocationContainerImpl() {
        InvocationContainer container = mockHandler.getInvocationContainer();

        assertEquals(invocationContainerImpl, container);
    }
}