import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ReturnsSmartNullsTest {

    @Test
    public void answer_ReturnsSmartNulls_Test() throws Throwable {
        // Setup
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Execute
        Object result = returnsSmartNulls.answer(invocation);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void intercept_ThrowsException_WhenMethodIsToString_Test() throws Throwable {
        // Setup
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mock(InvocationOnMock.class));
        Method method = Object.class.getMethod("toString");
        Object[] args = new Object[0];
        MethodProxy proxy = mock(MethodProxy.class);

        // Execute & Verify
        assertThrows(Reporter.SmartNullPointerException.class, () -> throwingInterceptor.intercept(new Object(), method, args, proxy));
    }

    @Test
    public void formatMethodCall_ReturnsCorrectFormat_Test() {
        // Setup
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(mock(InvocationOnMock.class));

        // Execute
        String result = throwingInterceptor.formatMethodCall();

        // Verify
        assertEquals("getMethodCall()", result);
    }

    @Test
    public void answer_ReturnsNull_WhenDefaultReturnValueIsNull_Test() throws Throwable {
        // Setup
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Execute
        Object result = returnsSmartNulls.answer(invocation);

        // Verify
        assertNull(result);
    }
}