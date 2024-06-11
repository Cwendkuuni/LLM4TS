
import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.Reporter;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private ObjectMethodsGuru objectMethodsGuru;
    private Reporter reporter;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
        objectMethodsGuru = mock(ObjectMethodsGuru.class);
        reporter = mock(Reporter.class);
    }

    @Test
    public void answer_withDefaultReturnValueNotNull_shouldReturnDefaultReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void answer_withDefaultReturnValueNullAndTypeNotMockable_shouldReturnNull() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("toString"));

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void answer_withDefaultReturnValueNullAndTypeMockable_shouldReturnThrowingInterceptor() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("isEmpty"));

        Object result = returnsSmartNulls.answer(invocation);

        assertTrue(result instanceof ReturnsSmartNulls.ThrowingInterceptor);
    }

    @Test
    public void throwingInterceptor_intercept_withToStringMethod_shouldReturnSmartNullString() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method toStringMethod = Object.class.getMethod("toString");
        when(objectMethodsGuru.isToString(toStringMethod)).thenReturn(true);
        when(invocation.getMethod()).thenReturn(toStringMethod);
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);

        Object result = throwingInterceptor.intercept(null, toStringMethod, null, null);

        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test(expected = RuntimeException.class)
    public void throwingInterceptor_intercept_withNonToStringMethod_shouldThrowException() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method nonToStringMethod = String.class.getMethod("length");
        when(objectMethodsGuru.isToString(nonToStringMethod)).thenReturn(false);
        when(invocation.getMethod()).thenReturn(nonToStringMethod);
        Location location = mock(Location.class);
        whenNew(Location.class).withNoArguments().thenReturn(location);
        doThrow(RuntimeException.class).when(reporter).smartNullPointerException(location);
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);

        throwingInterceptor.intercept(null, nonToStringMethod, null, null);
    }
}
