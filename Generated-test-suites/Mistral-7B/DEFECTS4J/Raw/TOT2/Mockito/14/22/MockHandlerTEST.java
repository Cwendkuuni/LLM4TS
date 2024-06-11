import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class MockHandlerTest {

    private MockHandler<String> mockHandler;
    private InvocationOnMock invocation;
    private Answer answer;

    @BeforeEach
    public void setup() {
        mockHandler = new MockHandler<>();
        answer = Mockito.answer(mock -> "Answer");
        invocation = Mockito.mock(Invocation.class);
    }

    @Test
    public void testHandleMethod_NoStubbing_ReturnsDefaultAnswer() throws Throwable {
        when(invocation.getMethod()).thenReturn(Mockito.mock(java.lang.reflect.Method.class));
        when(invocation.getTarget()).thenReturn(mock(Object.class));

        Object result = mockHandler.handle(invocation);
        verify(invocation).getMethod();
        verify(invocation).getTarget();
        verifyNoMoreInteractions(invocation);

        assertNotNull(result);
        assertEquals("Answer", result);
    }

    @Test
    public void testHandleMethod_Stubbing_ReturnsStubbedAnswer() throws Throwable {
        when(invocation.getMethod()).thenReturn(Mockito.mock(java.lang.reflect.Method.class));
        when(invocation.getTarget()).thenReturn(mock(Object.class));

        when(mockHandler.handle(ArgumentMatchers.any(Invocation.class))).thenReturn(answer);

        Object result = mockHandler.handle(invocation);
        verify(invocation).getMethod();
        verify(invocation).getTarget();
        verify(answer).answer(ArgumentMatchers.any());
        verifyNoMoreInteractions(invocation);

        assertNotNull(result);
        assertEquals("Answer", result);
    }
}