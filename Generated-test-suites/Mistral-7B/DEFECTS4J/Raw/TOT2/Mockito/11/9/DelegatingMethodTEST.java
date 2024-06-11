import org.junit.Assert.*;
import org.junit.Test;
import org.junit.experimental.annotations.EdgeCases;
import org.junit.experimental.annotations.ExpectedExceptions;

import java.lang.reflect.Method;

@EdgeCases(value = {
        @EdgeCase(level = EdgeCases.Level.EXTERNAL, name = "testMethodWithNoParametersAndNoReturnType"),
        @EdgeCase(level = EdgeCases.Level.EXTERNAL, name = "testMethodWithVarargs"),
        @EdgeCase(level = EdgeCases.Level.INTERNAL, name = "testMethodWithNoParametersAndNoReturnType"),
        @EdgeCase(level = EdgeCases.Level.INTERNAL, name = "testMethodWithVarargs"),
        @EdgeCase(level = EdgeCases.Level.INTERNAL, name = "testNullMethod"),
        @EdgeCase(level = EdgeCases.Level.INTERNAL, name = "testInvalidMethodType"),
})

public class DelegatingMethodTest {

    @Test
    public void testGetReturnType() {
        Method method = new Method(new Class[]{}, "someMethod", new Class[]{});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(new Class[]{String.class, int.class}, "someMethod", new Class[]{});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(new Class[]{String[].class}, "someMethod", new Class[]{String[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testNullMethod() {
        new DelegatingMethod(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMethodType() {
        Object invalidMethod = new Object();
        new DelegatingMethod((Method) invalidMethod);
    }

    @Test
    public void testMethodWithNoParametersAndNoReturnType() {
        Method method = new Method(new Class[0], "someMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(new Class[0], delegatingMethod.getParameterTypes());
        assertEquals(Void.TYPE, delegatingMethod.getReturnType());
    }

    @Test
    public void testMethodWithVarargs() {
        Method method = new Method(new Class[]{String[].class}, "someMethod", new Class[]{String[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(new Class[]{String[].class}, delegatingMethod.getParameterTypes());
        assertArrayEquals(new Class[]{}, delegatingMethod.getExceptionTypes());
        assertEquals(void.class, delegatingMethod.getReturnType());
        assertTrue(delegatingMethod.isVarArgs());
    }
}