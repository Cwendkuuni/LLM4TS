
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_returnsSmartNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(ReturnsSmartNulls.class.getDeclaredMethod("toString"));
        when(invocationOnMock.getArguments()).thenReturn(new Object[]{});

        Object result = returnsSmartNulls.answer(invocationOnMock);

        assertNull(result);
    }

    @Test
    public void testAnswer_returnsNullForFinalReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(ReturnsSmartNulls.class.getDeclaredMethod("finalize"));
        when(invocationOnMock.getArguments()).thenReturn(new Object[]{});

        Object result = returnsSmartNulls.answer(invocationOnMock);

        assertNull(result);
    }
}
