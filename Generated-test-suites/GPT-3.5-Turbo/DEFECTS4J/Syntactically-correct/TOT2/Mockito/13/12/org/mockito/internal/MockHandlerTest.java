import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<>();
    }

    @Test
    public void testHandle_WhenInvocationHasAnswersForStubbing_ThenReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
    }

    @Test
    public void testHandle_WhenVerificationModeIsNotNullAndSameMock_ThenVerify() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(verificationMode).verify(any());
    }

    @Test
    public void testVoidMethodStubbable_WhenCalled_ThenReturnVoidMethodStubbable() {
        Object mock = new Object();

        VoidMethodStubbable<Object> result = mockHandler.voidMethodStubbable(mock);

        assertNotNull(result);
    }

    @Test
    public void testSetAnswersForStubbing_WhenCalled_ThenSetAnswersForStubbing() {
        List<Answer> answers = Arrays.asList(mock(Answer.class));

        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, mockHandler.invocationContainerImpl.getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer_WhenCalled_ThenReturnInvocationContainer() {
        InvocationContainer result = mockHandler.getInvocationContainer();

        assertNotNull(result);
    }
}