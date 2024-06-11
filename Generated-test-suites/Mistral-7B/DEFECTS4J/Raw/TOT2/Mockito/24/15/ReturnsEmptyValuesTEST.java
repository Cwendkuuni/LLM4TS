import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {
    @Test
    void testToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("toString", (Class[]) null));
        when(invocation.getMock()).thenReturn(mock);

        String result = (String) returnsEmptyValues.answer(invocation);

        verify(mock).toString();
        assertNotNull(result);
        assertEquals("Mock for [" + mock.getClass().getSimpleName() + "], hashCode: " + mock.hashCode(), result);
    }
}