import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_OrdinaryReturnValue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNullsTest.class.getMethod("testAnswer_OrdinaryReturnValue"));
        Object returnValue = smartNulls.answer(invocation);
        assertNull(returnValue);
    }

    @Test
    public void testAnswer_NullReturnValue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNullsTest.class.getMethod("testAnswer_NullReturnValue"));
        Object returnValue = smartNulls.answer(invocation);
        assertNull(returnValue);
    }

    @Test
    public void testAnswer_SmartNullReturnValue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNullsTest.class.getMethod("testAnswer_SmartNullReturnValue"));
        Object returnValue = smartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed testAnswer_SmartNullReturnValue() method on mock", returnValue);
    }

    @Test
    public void testAnswer_FinalReturnType() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = ReturnsSmartNullsTest.class.getMethod("testAnswer_FinalReturnType");
        when(invocation.getMethod()).thenReturn(method);
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE.canImposterise(method.getReturnType())).thenReturn(false);
        Object returnValue = smartNulls.answer(invocation);
        assertNull(returnValue);
    }
}