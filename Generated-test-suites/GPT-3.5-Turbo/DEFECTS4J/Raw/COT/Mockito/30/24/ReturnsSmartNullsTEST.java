
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerWithDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = returnsSmartNulls.answer(invocation);
        Assert.assertNull("Default return value should be null", defaultReturnValue);
    }

    @Test
    public void testAnswerWithMockableReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = invocation.getClass().getMethod("getReturnType");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Class<?> returnType = Class.class;
        Mockito.when(returnType.getReturnType()).thenReturn(Object.class);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(true);
        Object result = returnsSmartNulls.answer(invocation);
        Assert.assertNotNull("Return value should not be null for mockable return type", result);
    }

    @Test
    public void testAnswerWithNonMockableReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = invocation.getClass().getMethod("getReturnType");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Class<?> returnType = Class.class;
        Mockito.when(returnType.getReturnType()).thenReturn(Object.class);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(false);
        Object result = returnsSmartNulls.answer(invocation);
        Assert.assertNull("Return value should be null for non-mockable return type", result);
    }

    @Test
    public void testInterceptToStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = invocation.getClass().getMethod("toString");
        Object result = returnsSmartNulls.answer(invocation);
        Assert.assertNotNull("Return value should not be null for toString method", result);
        Assert.assertTrue("Return value should be of type String", result instanceof String);
        Assert.assertTrue("Return value should contain method call", ((String) result).contains("SmartNull returned by unstubbed"));
    }

    @Test
    public void testInterceptNonToStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = invocation.getClass().getMethod("hashCode");
        Location location = Mockito.mock(Location.class);
        ObjectMethodsGuru objectMethodsGuru = Mockito.mock(ObjectMethodsGuru.class);
        Mockito.when(objectMethodsGuru.isToString(method)).thenReturn(false);
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.whenNew(Reporter.class).withNoArguments().thenReturn(reporter);
        Mockito.when(reporter.smartNullPointerException(location)).thenReturn(null);
        Object result = returnsSmartNulls.answer(invocation);
        Assert.assertNull("Return value should be null for non-toString method", result);
    }

    @Test
    public void testInterceptNonToStringMethodWithError() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = invocation.getClass().getMethod("hashCode");
        Location location = Mockito.mock(Location.class);
        ObjectMethodsGuru objectMethodsGuru = Mockito.mock(ObjectMethodsGuru.class);
        Mockito.when(objectMethodsGuru.isToString(method)).thenReturn(false);
        Reporter reporter = Mockito.mock(Reporter.class);
        Mockito.whenNew(Reporter.class).withNoArguments().thenReturn(reporter);
        Mockito.when(reporter.smartNullPointerException(location)).thenThrow(new RuntimeException("Error"));
        Object result = returnsSmartNulls.answer(invocation);
        Assert.assertNull("Return value should be null for non-toString method with error", result);
    }

}
