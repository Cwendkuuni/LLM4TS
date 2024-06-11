package org.mockito.internal.creation;

import org.mockito.internal.invocation.MockitoMethod;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DelegatingMethodTest {

    // Test the constructor
    @Test
    public void testConstructor() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getDeclaredMethod("getExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    // Test getExceptionTypes method
    @Test
    public void testGetExceptionTypes() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getDeclaredMethod("getExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    // Test getJavaMethod method
    @Test
    public void testGetJavaMethod() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getDeclaredMethod("getExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    // Test getName method
    @Test
    public void testGetName() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getDeclaredMethod("getName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getName(), delegatingMethod.getName());
    }

    // Test getParameterTypes method
    @Test
    public void testGetParameterTypes() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getDeclaredMethod("getParameterTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    // Test getReturnType method
    @Test
    public void testGetReturnType() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getDeclaredMethod("getReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    // Test isVarArgs method
    @Test
    public void testIsVarArgsTrue() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getDeclaredMethod("isVarArgs");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());
    }

    // Test isAbstract method
    @Test
    public void testIsAbstractTrue() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getDeclaredMethod("isAbstract");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());
    }

    // Test equals method
    @Test
    public void testEquals() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getDeclaredMethod("equals", Object.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.equals(method));
    }

    // Test hashCode method
    @Test
    public void testHashCode() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getDeclaredMethod("hashCode");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(1, delegatingMethod.hashCode());
    }
}