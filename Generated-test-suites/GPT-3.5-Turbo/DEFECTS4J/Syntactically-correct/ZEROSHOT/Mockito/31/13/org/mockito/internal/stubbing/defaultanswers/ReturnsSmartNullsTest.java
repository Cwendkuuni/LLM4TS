import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.Method;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockInvocation.getMethod()).thenReturn(getTestMethod("testMethod", null));
        Object result = returnsSmartNulls.answer(mockInvocation);
        Assert.assertNull(result);

        Mockito.when(mockInvocation.getMethod()).thenReturn(getTestMethod("testMethod", "String"));
        result = returnsSmartNulls.answer(mockInvocation);
        Assert.assertEquals("SmartNull returned by unstubbed testMethod method on mock", result);

        Mockito.when(mockInvocation.getMethod()).thenReturn(getTestMethod("toString", null));
        result = returnsSmartNulls.answer(mockInvocation);
        Assert.assertEquals("SmartNull returned by unstubbed toString() method on mock", result);

        Mockito.when(mockInvocation.getMethod()).thenReturn(getTestMethod("otherMethod", null));
        result = returnsSmartNulls.answer(mockInvocation);
        Assert.assertNull(result);
    }

    private Method getTestMethod(String name, String returnType) throws NoSuchMethodException {
        Method method;
        if (returnType != null) {
            method = ReturnsSmartNulls.class.getDeclaredMethod(name);
        } else {
            method = ReturnsSmartNulls.class.getDeclaredMethod(name, (Class<?>[]) null);
        }
        if (returnType != null) {
            method.setReturnType(Class.forName(returnType));
        }
        return method;
    }

}