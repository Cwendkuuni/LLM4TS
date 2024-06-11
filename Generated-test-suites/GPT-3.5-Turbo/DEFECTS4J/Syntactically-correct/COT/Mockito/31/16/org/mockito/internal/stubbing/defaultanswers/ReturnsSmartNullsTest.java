
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsSmartNull() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock.getMethod()).thenReturn(String.class.getMethod("toString"));
        Object smartNull = answer.answer(invocationOnMock);

        assertNotNull(smartNull);
        assertTrue(smartNull instanceof String);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", smartNull);
    }

    @Test
    public void testAnswer_ReturnsSmartNull_ExceptionHandling() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.whenNew(Reporter.class).withNoArguments().thenReturn(reporter);

        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        answer.answer(invocationOnMock);

        Mockito.verify(reporter).smartNullPointerException(Mockito.any(Location.class));
    }

    @Test
    public void testAnswer_ReturnsSmartNull_FinalReturnType() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();

        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock.getMethod()).thenReturn(String.class.getMethod("getClass"));
        Object smartNull = answer.answer(invocationOnMock);

        assertNull(smartNull);
    }
}
