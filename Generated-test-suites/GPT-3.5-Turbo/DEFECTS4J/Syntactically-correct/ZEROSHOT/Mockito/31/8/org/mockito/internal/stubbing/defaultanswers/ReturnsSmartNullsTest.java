import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        when(delegate.answer(invocation)).thenReturn(null);
        assertNull(answer.answer(invocation));

        when(delegate.answer(invocation)).thenReturn("return value");
        assertEquals("return value", answer.answer(invocation));
    }
}