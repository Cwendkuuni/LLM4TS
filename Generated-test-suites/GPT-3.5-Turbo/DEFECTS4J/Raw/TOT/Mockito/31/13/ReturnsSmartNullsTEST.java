import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @BeforeClass
    public void setupTestClass() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    // Test answer method
    @Test
    public void testAnswerMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMethod().getName()).thenReturn("method");
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    // Test answer method - return default value
    @Test
    public void testAnswerMethod_ReturnDefaultValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertEquals("", result);
    }

    // Test answer method - return smart null for non-mockable type
    @Test
    public void testAnswerMethod_ReturnSmartNullForNonMockableType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    // Test ThrowingInterceptor intercept method - when called method is toString
    @Test
    public void testThrowingInterceptor_intercept_ToStringMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString");
        Location location = mock(Location.class);
        when(invocation.getMethod()).thenReturn(method);
        when(new ObjectMethodsGuru().isToString(method)).thenReturn(true);
        when(invocation.getMethod().getName()).thenReturn("method");
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(invocation);
        Object result = throwingInterceptor.intercept(null, method, new Object[0], (MethodProxy) null);
        assertEquals("SmartNull returned by unstubbed method method on mock", result);
    }

    // Test ThrowingInterceptor intercept method - when called method is not toString
    @Test
    public void testThrowingInterceptor_intercept_NotToStringMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("equals", Object.class);
        Location location = mock(Location.class);
        when(invocation.getMethod()).thenReturn(method);
        when(new ObjectMethodsGuru().isToString(method)).thenReturn(false);
        when(new Reporter().smartNullPointerException(location)).thenReturn(null);
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(invocation);
        Object result = throwingInterceptor.intercept(null, method, new Object[0], (MethodProxy) null);
        assertNull(result);
        verify(new Reporter(), times(1)).smartNullPointerException(location);
    }

    // Test answer method - imposterise method is called
    @Test
    public void testAnswerMethod_ImposteriseMethodCalled() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(TestClass.class);
        Method method = TestClass.class.getMethod("method");
        when(invocation.getMethod()).thenReturn(method);
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(invocation);
        when(ClassImposterizer.INSTANCE.imposterise(throwingInterceptor, TestClass.class)).thenReturn(null);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
        verify(ClassImposterizer.INSTANCE, times(1)).imposterise(throwingInterceptor, TestClass.class);
    }

    private static class TestClass {
        public void method() {}
    }

}