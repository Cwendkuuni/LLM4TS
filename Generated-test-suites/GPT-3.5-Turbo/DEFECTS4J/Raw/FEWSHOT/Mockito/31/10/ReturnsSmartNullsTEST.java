
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

    @Test
    public void testAnswer() throws Throwable {
        Answer<Object> delegate = new ReturnsMoreEmptyValues();
        InvocationOnMock invocation = Matchers.mock(InvocationOnMock.class);
        Object defaultReturnValue = new Object();
        Object result = returnsSmartNulls.answer(invocation);

        // Verify that ReturnsMoreEmptyValues is called and defaultReturnValue is returned if it is not null
        Mockito.when(delegate.answer(invocation)).thenReturn(defaultReturnValue);
        assertEquals(defaultReturnValue, result);

        // Verify that ReturnsMoreEmptyValues is called and null is returned if defaultReturnValue is null
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        assertNull(result);

        // Verify that ClassImposterizer is used to create a mock object if defaultReturnValue is null 
        // and the return type is mockable
        ClassImposterizer.INSTANCE = Matchers.mock(ClassImposterizer.class);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(Object.class)).thenReturn(true);
        Mockito.when(ClassImposterizer.INSTANCE.imposterise(Matchers.any(MethodInterceptor.class), Matchers.eq(Object.class))).thenReturn(defaultReturnValue);
        assertEquals(defaultReturnValue, result);
        Mockito.verify(ClassImposterizer.INSTANCE).canImposterise(Object.class);
        Mockito.verify(ClassImposterizer.INSTANCE).imposterise(Matchers.any(MethodInterceptor.class), Matchers.eq(Object.class));

        // Verify that null is returned if defaultReturnValue is null and the return type is not mockable
        ClassImposterizer.INSTANCE = Matchers.mock(ClassImposterizer.class);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(Object.class)).thenReturn(false);
        assertNull(result);
        Mockito.verify(ClassImposterizer.INSTANCE).canImposterise(Object.class);
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        InvocationOnMock invocation = Matchers.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString");
        Object[] args = {};
        MethodProxy proxy = Matchers.mock(MethodProxy.class);
        ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        Object result = throwingInterceptor.intercept(null, method, args, proxy);

        // Verify that the intercept method returns the expected value for a toString method
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);

        // Verify that a smartNullPointerException is thrown for any other method
        Location location = Matchers.mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);
        ObjectMethodsGuru objectMethodsGuru = Matchers.mock(ObjectMethodsGuru.class);
        Mockito.when(objectMethodsGuru.isToString(method)).thenReturn(false);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(new ObjectMethodsGuru().isToString(method)).thenReturn(true);
        new Reporter().smartNullPointerException(location);
        assertNull(result);
        Mockito.verify(new ObjectMethodsGuru(), Mockito.never()).isToString(method);
        Mockito.verify(invocation, Mockito.times(2)).getMethod();
        Mockito.verify(invocation).getLocation();
        Mockito.verify(objectMethodsGuru).isToString(method);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testFormatMethodCall() throws NoSuchMethodException {
        InvocationOnMock invocation = Matchers.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString");
        Object[] args = {};
        MethodProxy proxy = Matchers.mock(MethodProxy.class);
        ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        String result = throwingInterceptor.formatMethodCall();

        // Verify that the formatMethodCall method returns the expected value
        assertEquals("toString()", result);

        // Verify that a NoSuchMethodException is thrown if the method does not exist
        throwingInterceptor.formatMethodCall();
    }
}
