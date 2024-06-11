
package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class DelegatingMethodTest {
    // Test constructor
    @Test
    public void testConstructor() {
        Method method = null;
        try {
            new DelegatingMethod(method);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // Expected
        }

        method = getDummyMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertSame(method, delegatingMethod.getJavaMethod());
    }

    // Test getExceptionTypes
    @Test
    public void testGetExceptionTypes() {
        Method method = getDummyMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
    }

    // Test getName
    @Test
    public void testGetName() {
        Method method = getDummyMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals("dummyMethod", delegatingMethod.getName());
    }

    // Test getParameterTypes
    @Test
    public void testGetParameterTypes() {
        Method method = getDummyMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(0, parameterTypes.length);
    }
    
    // Test getReturnType
    @Test
    public void testGetReturnType() {
        Method method = getDummyMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(void.class, delegatingMethod.getReturnType());
    }
    
    // Test isVarArgs
    @Test
    public void testIsVarArgs() {
        Method method = getDummyMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }
    
    // Test isAbstract
    @Test
    public void testIsAbstract() {
        Method method = getDummyMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());
        
        Method abstractMethod = getAbstractMethod();
        DelegatingMethod delegatingAbstractMethod = new DelegatingMethod(abstractMethod);
        assertTrue(delegatingAbstractMethod.isAbstract());
    }
    
    // Test equals
    @Test
    public void testEquals() {
        Method method = getDummyMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertTrue(delegatingMethod.equals(method));
        
        Method otherMethod = getDummyMethod();
        DelegatingMethod otherDelegatingMethod = new DelegatingMethod(otherMethod);
        
        assertTrue(delegatingMethod.equals(otherDelegatingMethod));
        
        Method anotherMethod = getNonEqualMethod();
        DelegatingMethod anotherDelegatingMethod = new DelegatingMethod(anotherMethod);
        
        assertFalse(delegatingMethod.equals(anotherDelegatingMethod));
    }
    
    // Test hashCode
    @Test
    public void testHashCode() {
        Method method = getDummyMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertEquals(1, delegatingMethod.hashCode());
    }
    
    // Helper method to get a dummy method
    private Method getDummyMethod() {
        try {
            return DummyClass.class.getMethod("dummyMethod");
        } catch (NoSuchMethodException e) {
            fail("Could not find dummyMethod");
        }
        return null;
    }
    
    // Helper method to get an abstract method
    private Method getAbstractMethod() {
        try {
            return DummyClass.class.getMethod("abstractMethod");
        } catch (NoSuchMethodException e) {
            fail("Could not find abstractMethod");
        }
        return null;
    }
    
    // Helper method to get a non-equal method
    private Method getNonEqualMethod() {
        try {
            return DummyClass.class.getMethod("nonEqualMethod");
        } catch (NoSuchMethodException e) {
            fail("Could not find nonEqualMethod");
        }
        return null;
    }
    
    // Dummy class for testing
    private static class DummyClass {
        public void dummyMethod() {
            // Dummy method
        }
        
        public void abstractMethod() {
            // Empty abstract method
        }
        
        public void nonEqualMethod() {
            // Dummy method with different name
        }
    }
}
