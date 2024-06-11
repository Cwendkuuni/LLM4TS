
package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {
    
    @Test
    public void testGetExceptionTypes() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        // perform assertions
        // ...
    }
    
    @Test
    public void testGetJavaMethod() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        // perform assertions
        // ...
    }
    
    @Test
    public void testGetName() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        // perform assertions
        // ...
    }
    
    @Test
    public void testGetParameterTypes() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        // perform assertions
        // ...
    }
    
    @Test
    public void testGetReturnType() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        // perform assertions
        // ...
    }
    
    @Test
    public void testIsVarArgs() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        // perform assertions
        // ...
    }
    
    @Test
    public void testIsAbstract() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        // perform assertions
        // ...
    }
    
    @Test
    public void testEquals() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean equals = delegatingMethod.equals(delegatingMethod);
        // perform assertions
        // ...
    }
    
    @Test
    public void testHashCode() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();
        // perform assertions
        // ...
    }
    
    private static class DummyClass {
        public void dummyMethod() {
            // Do nothing
        }
    }
}
