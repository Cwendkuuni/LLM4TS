
package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.experimental.methods.MethodFinder;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.internal.creation.DelegatingMethod.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() throws Exception {
        Method getExceptionTypesMethod = MethodFinder.findPublicMethod(DelegatingMethod.class, "getExceptionTypes", new Class[0]);
        MockitoMethod delegatingMethod = new DelegatingMethod(getExceptionTypesMethod);

        Class<?>[] expectedExceptionTypes = new Class[]{Exception1.class, Exception2.class};
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testGetJavaMethod() throws Exception {
        Method getJavaMethodMethod = MethodFinder.findPublicMethod(DelegatingMethod.class, "getJavaMethod", new Class[0]);
        MockitoMethod delegatingMethod = new DelegatingMethod(getJavaMethodMethod);

        Method expectedJavaMethod = new Method("testMethod", new Class[0]);
        Method actualJavaMethod = delegatingMethod.getJavaMethod();

        assertSame(expectedJavaMethod, actualJavaMethod);
    }

    @Test
    public void testEquals() throws Exception {
        Method method1 = MethodFinder.findPublicMethod(DelegatingMethod.class, "getJavaMethod", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = MethodFinder.findPublicMethod(DelegatingMethod.class, "getJavaMethod", new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Method method3 = MethodFinder.findPublicMethod(Object.class, "toString", new Class[0]);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertTrue(delegatingMethod1.equals(delegatingMethod1));
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
        assertFalse(delegatingMethod1.equals(delegatingMethod3));
    }
}

class Exception1 {}
class Exception2 {}