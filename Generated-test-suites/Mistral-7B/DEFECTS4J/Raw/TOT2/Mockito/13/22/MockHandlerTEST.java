import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.VerificationMode;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.Invocation;
import java.lang.reflect.Method;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockHandlerTest {

    private MockHandler<MockitoTestClass> mockHandler;
    private InvocationOnMock invocation;
    private ArgumentCaptor<Answer> answerCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockHandler = new MockHandler<>(mock(MockitoTestClass.class));
        invocation = mock(InvocationOnMock.class);
        answerCaptor = ArgumentCaptor.forClass(Answer.class);
    }

    @Test
    public void testHandleInvocationWithStubbing() throws Throwable {
        when(mockHandler.handle(invocation)).thenReturn(new Object());

        Object result = mockHandler.handle(invocation);

        verify(mockHandler).handle(invocation);
        verifyNoMoreInteractions(mockHandler);
        assertEquals(new Object(), result);
    }

    @Test
    public void testHandleInvocationWithVerification() throws Throwable {
        VerificationMode verificationMode = Mockito.verify(mockHandler).handle(invocation);

        verify(mockHandler).handle(invocation);
        verifyNoMoreInteractions(mockHandler);
    }

    @Test
    public void testHandleNullInvocation() throws Throwable {
        assertThrows(NullPointerException.class, () -> mockHandler.handle(null));
    }

    @Test
    public void testHandleInvocationWithIncorrectArguments() throws Throwable {
        Invocation incorrectInvocation = Mockito.mock(Invocation.class);

        assertThrows(IllegalArgumentException.class, () -> mockHandler.handle(incorrectInvocation));
    }

    @Test
    public void testHandleInvocationWithDifferentTypes() throws Throwable {
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        when(differentInvocation.getTarget()).thenReturn(new Object());
        when(differentInvocation.getMethod()).thenReturn(new Object().getClass().getMethod("differentMethod", int.class));

        assertThrows(InvocationTargetException.class, () -> mockHandler.handle(differentInvocation));
    }

    @Test
    public void testHandleInvocationWithThrownExceptions() throws Throwable {
        Invocation invocationWithException = Mockito.mock(InvocationOnMock.class);
        when(invocationWithException.getMethod()).thenThrow(new RuntimeException("TestException"));

        mockHandler.handle(invocationWithException);

        verify(mockHandler).handle(invocationWithException);
        verify(mockHandler).handleException(Mockito.eq(invocationWithException.getMethod()), Mockito.eq(new RuntimeException("TestException")));
    }

    @Test
    public void testHandleInvocationWithStubbingAnswer() throws Throwable {
        Answer<Integer> answer = Mockito.mock(Answer.class);
        when(mockHandler.handle(invocation)).thenAnswer(answer);

        Integer result = (Integer) mockHandler.handle(invocation);

        verify(mockHandler).handle(invocation);
        verify(answer).answer(invocation);
        verify(answer).get(invocation).equals(result);
    }

    @Test
    public void testHandleInvocationWithDoAnswer() throws Throwable {
        doAnswer(invocation -> {
            assertEquals("Test", invocation.getArgument(0));
            return "Return";
        }).when(mockHandler).handle(Mockito.any(String.class));

        String result = (String) mockHandler.handle("Test");

        verify(mockHandler).handle("Test");
        verify(mockHandler).handle(ArgumentMatchers.any(String.class));
        assertEquals("Return", result);
    }

    @Test
    public void testHandleInvocationWithStubbingVoidMethod() throws Throwable {
        mockHandler.voidMethodStubbable(mock(MockitoTestClass.class)).stubVoid("voidMethod");

        mockHandler.handle(Mockito.mock(InvocationOnMock.class).withMethodCall("voidMethod"));

        verify(mockHandler, times(1)).handle(Mockito.any(InvocationOnMock.class));
        verify(mockHandler, atLeastOnce()).handle(Mockito.any(InvocationOnMock.class).withMethodCall("voidMethod"));
        verifyNoMoreInteractions(mockHandler);
    }
}