package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Danger;
import org.mockito.quality.VerifiedUsage;
import org.mockito.quality.Warnings;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class DelegatingMethodTest {

    private static final String METHOD_NAME = "testMethod";
    private static final Class<?> RETURN_TYPE = String.class;
    private static final Class<?>[] PARAMETER_TYPES = new Class[] { int.class, String.class };

    @Test
    public void testGetExceptionTypes() {
        Method method = createTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertEquals(Arrays.asList(exceptionTypes).toString(), Arrays.asList(new Class[] {}).toString(), "getExceptionTypes should return an empty array if method doesn't declare any exceptions");

        method = createTestMethodWithException();
        delegatingMethod = new DelegatingMethod(method);
        exceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(new Class[] { IOException.class }.toString(), exceptionTypes.toString(), "getExceptionTypes should correctly return the exception types declared by the method");
    }

    @Test
    public void testGetJavaMethod() {
        Method method = createTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertNotNull(delegatingMethod.getJavaMethod());
        assertEquals(method.getName(), delegatingMethod.getName());
        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
        assertArrayEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
        assertEquals(method.isVarArgs(), delegatingMethod.isVarArgs());
        assertEquals(method.isAbstract(), delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method testMethod = createTestMethod();
        Method otherMethod = createOtherMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        DelegatingMethod otherDelegatingMethod = new DelegatingMethod(otherMethod);

        assertFalse(delegatingMethod.equals(null));
        assertFalse(delegatingMethod.equals(otherDelegatingMethod));
        assertFalse(delegatingMethod.equals(new Object()));
        assertTrue(delegatingMethod.equals(delegatingMethod));
        assertTrue(delegatingMethod.equals(new DelegatingMethod(testMethod)));
        assertTrue(testMethod.equals(delegatingMethod.getJavaMethod()));
        assertTrue(testMethod.equals(new DelegatingMethod(testMethod).getJavaMethod()));
    }

    @Test
    public void testHashCode() {
        Method method = createTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod.hashCode();
        DelegatingMethod delegatingMethodCopy = new DelegatingMethod(method);

        int hashCode2 = delegatingMethodCopy.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testGetName() {
        Method method = createTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = createTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertArrayEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = createTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsAbstract() {
        Method method = createTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testIsVarArgs() {
        Method method = createTestMethodWithVarArgs();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isVarArgs());
    }

    @Test
    public void testCreateWithNullMethod() {
        assertThrows(NullPointerException.class, () -> new DelegatingMethod(null));
    }

    @Test
    public void testCreateWithAbstractMethod() {
        Method method = createAbstractMethod();
        assertThrows(IllegalArgumentException.class, () -> new DelegatingMethod(method));
    }

    @Test
    public void testCreateWithMethodWithoutReturnType() {
        Method method = createMethodWithoutReturnType();
        assertThrows(IllegalArgumentException.class, () -> new DelegatingMethod(method));
    }

    @Test
    public void testCreateWithMethodWithoutName() {
        Method method = createMethodWithoutName();
        assertThrows(IllegalArgumentException.class, () -> new DelegatingMethod(method));
    }

    @Test
    public void testCreateWithMethodWithoutParameterTypes() {
        Method method = createMethodWithoutParameterTypes();
        assertThrows(IllegalArgumentException.class, () -> new DelegatingMethod(method));
    }

    @Test
    public void testCreateWithMethodWithNullReturnType() {
        Method method = createMethodWithNullReturnType();
        assertThrows(NullPointerException.class, () -> new DelegatingMethod(method).getReturnType());
    }

    @Test
    public void testCreateWithMethodWithNullParameterTypes() {
        Method method = createMethodWithNullParameterTypes();
        assertThrows(NullPointerException.class, () -> new DelegatingMethod(method).getParameterTypes());
    }

    @Test
    public void testCreateWithMethodWithNullExceptionTypes() {
        Method method = createTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        Whitebox.setInternalState(delegatingMethod, "exceptionTypes", null);
        assertNull(delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testCreateWithMethodWithEmptyExceptionTypes() {
        Method method = createTestMethod();
        Method spyMethod = spy(method);
        Whitebox.setInternalState(spyMethod, "exceptionTypes", new Class[] {});
        DelegatingMethod delegatingMethod = new DelegatingMethod(spyMethod);
        assertArrayEquals(new Class[] {}.toString(), delegatingMethod.getExceptionTypes().toString(), "getExceptionTypes should return an empty array if the method's internal exceptionTypes are empty");
    }

    private Method createTestMethod() {
        return createMethod(RETURN_TYPE, PARAMETER_TYPES, METHOD_NAME);
    }

    private Method createTestMethodWithException() {
        return createMethodWithException(RETURN_TYPE, PARAMETER_TYPES, METHOD_NAME, IOException.class);
    }

    private Method createTestMethodWithVarArgs() {
        return createMethod(RETURN_TYPE, new Class[] { int.class, String[].class }, METHOD_NAME);
    }

    private Method createTestMethodWithNullReturnType() {
        return createMethod(null, PARAMETER_TYPES, METHOD_NAME);
    }

    private Method createTestMethodWithoutName() {
        return createMethod(RETURN_TYPE, PARAMETER_TYPES, (String) null);
    }

    private Method createTestMethodWithoutParameterTypes() {
        return createMethod(RETURN_TYPE, new Class[] {}, METHOD_NAME);
    }

    private Method createMethod(Class<?> returnType, Class<?>[] parameterTypes, String name) {
        try {
            Method method = Class.forName("org.mockito.internal.creation.TestClass").getMethod(name, parameterTypes);
            method.setAccessible(true);
            method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
            method.setReturnType(returnType);
            return method;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method createMethodWithException(Class<?> returnType, Class<?>[] parameterTypes, String name, Class<?> exceptionType) {
        try {
            Method method = Class.forName("org.mockito.internal.creation.TestClass").getMethod(name, parameterTypes);
            method.setAccessible(true);
            method.setModifiers(method.getModifiers() | Modifier.THROWING);
            method.setExceptionTypes(new Class[] { exceptionType });
            method.setReturnType(returnType);
            return method;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method createAbstractMethod() {
        Method method = createMethod(RETURN_TYPE, PARAMETER_TYPES, METHOD_NAME);
        method.setAccessible(true);
        method.setModifiers(method.getModifiers() | Modifier.ABSTRACT);
        return method;
    }

    private Method createMethodWithoutReturnType() {
        Method method = createMethod(null, PARAMETER_TYPES, METHOD_NAME);
        method.setAccessible(true);
        method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
        return method;
    }

    private Method createMethodWithoutName() {
        Method method = createMethod(RETURN_TYPE, PARAMETER_TYPES, (String) null);
        method.setAccessible(true);
        method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
        return method;
    }

    private Method createMethodWithoutParameterTypes() {
        Method method = createMethod(RETURN_TYPE, new Class[] {}, METHOD_NAME);
        method.setAccessible(true);
        method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
        return method;
    }

    private static class TestClass {
        @Danger(Danger.Category.UNCHECKED_EXCEPTION)
        @VerifiedUsage(value = VerifiedUsage.Times.NEVER, message = "This method is never intended to be invoked directly")
        public String testMethod(int i, String[] s) {
            throw new RuntimeException("Test exception");
        }

        public String testMethod(@SuppressWarnings("unused") int i) {
            return "Result";
        }

        public String testMethod(String... strings) {
            return "Result";
        }

        public String testMethod() {
            return "Result";
        }

        public abstract String testMethodAbstract();

        public String testMethodWithNullReturnType() {
            return null;
        }

        public String testMethodWithoutName() {
            return "Result";
        }

        public String testMethodWithoutParameterTypes() {
            return "Result";
        }
    }
}