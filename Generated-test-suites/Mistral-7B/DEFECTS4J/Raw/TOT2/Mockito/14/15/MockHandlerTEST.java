import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.*;

public class MockHandlerTest {
    // ...

    @Test
    public void testHandle_nullInvocation() {
        try {
            mockHandler.handle(null);
            fail("Expected IllegalStateException: Invocation for stubbing cannot be null");
        } catch (IllegalStateException expected) {}
    }

    @Test
    public void testHandle_unsupportedMethod() throws Throwable {
        doThrow(new UnsupportedOperationException("Unsupported method"))
                .when(mockHandler)
                .handle(ArgumentMatchers.any(Invocation.class));

        try {
            mockHandler.handle(invocationMock());
            fail("Expected UnsupportedOperationException: Unsupported method");
        } catch (UnsupportedOperationException expected) {}
    }

    @Test
    public void testHandle_multipleStubbingAttempts() throws Throwable {
        when(mockHandler).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Exception in answer");
            }
        }).handle(ArgumentMatchers.any(Invocation.class));

        when(mockHandler).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "second answer";
            }
        }).handle(ArgumentMatchers.any(Invocation.class));

        assertEquals("second answer", mockHandler.handle(invocationMock()));

        verify(invocationContainer, never()).setAnswerFor(ArgumentMatchers.any(InvocationMatcher.class));
    }

    @Test
    public void testHandle_exception() throws Throwable {
        doThrow(new RuntimeException("Exception in handle method"))
                .when(mockHandler)
                .handle(ArgumentMatchers.any(Invocation.class));

        try {
            mockHandler.handle(invocationMock());
            fail("Expected RuntimeException: Exception in handle method");
        } catch (RuntimeException expected) {}

        verifyNoMoreInteractions(mockHandler);
    }

    // ...
}