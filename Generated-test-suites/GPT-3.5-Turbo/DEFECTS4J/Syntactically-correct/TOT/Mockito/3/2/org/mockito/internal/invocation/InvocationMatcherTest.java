import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private static final Object MOCK = mock(Object.class);
    private static final String METHOD_NAME = "exampleMethod";
    private static final Class<?>[] ARGUMENT_TYPES = {String.class, int.class};
    private static final Object[] ARGUMENTS = {"test", 123};

    @Test
    public void testConstructor_WithInvocationAndMatchers() {
        Invocation invocation = createInvocation(MOCK, METHOD_NAME, ARGUMENT_TYPES, ARGUMENTS);
        List<Matcher> matchers = createMatchers(ARGUMENT_TYPES, ARGUMENTS);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_WithInvocationAndEmptyMatchers() {
        Invocation invocation = createInvocation(MOCK, METHOD_NAME, ARGUMENT_TYPES, ARGUMENTS);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = createInvocation(MOCK, METHOD_NAME, ARGUMENT_TYPES, ARGUMENTS);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();

        assertNotNull(method);
        assertEquals(METHOD_NAME, method.getName());
        assertArrayEquals(ARGUMENT_TYPES, method.getParameterTypes());
    }

    @Test
    public void testToString() {
        Invocation invocation = createInvocation(MOCK, METHOD_NAME, ARGUMENT_TYPES, ARGUMENTS);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String stringRepresentation = invocationMatcher.toString();

        assertNotNull(stringRepresentation);
        assertTrue(stringRepresentation.contains(METHOD_NAME));
        assertTrue(stringRepresentation.contains(ARGUMENTS[0].toString()));
        assertTrue(stringRepresentation.contains(ARGUMENTS[1].toString()));
    }

    @Test
    public void testMatches_WithMatchingInvocation() {
        Invocation invocation = createInvocation(MOCK, METHOD_NAME, ARGUMENT_TYPES, ARGUMENTS);
        InvocationMatcher invocationMatcher = createInvocationMatcher(MOCK, METHOD_NAME, ARGUMENTS);

        boolean matches = invocationMatcher.matches(invocation);

        assertTrue(matches);
    }

    @Test
    public void testMatches_WithMismatchingInvocation() {
        Invocation invocation = createInvocation(MOCK, METHOD_NAME, ARGUMENT_TYPES, ARGUMENTS);
        InvocationMatcher invocationMatcher = createInvocationMatcher(MOCK, "differentMethod", ARGUMENTS);

        boolean matches = invocationMatcher.matches(invocation);

        assertFalse(matches);
    }

    @Test
    public void testMatches_WithMismatchingArguments() {
        Invocation invocation = createInvocation(MOCK, METHOD_NAME, ARGUMENT_TYPES, ARGUMENTS);
        InvocationMatcher invocationMatcher = createInvocationMatcher(MOCK, METHOD_NAME, new Object[]{"test", 456});

        boolean matches = invocationMatcher.matches(invocation);

        assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethod_WithMatchingMethod() {
        Invocation invocation = createInvocation(MOCK, METHOD_NAME, ARGUMENT_TYPES, ARGUMENTS);
        InvocationMatcher invocationMatcher = createInvocationMatcher(MOCK, METHOD_NAME, new Object[]{"test", 456});

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocation);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMethod() {
        Invocation invocation = createInvocation(MOCK, METHOD_NAME, ARGUMENT_TYPES, ARGUMENTS);
        InvocationMatcher invocationMatcher = createInvocationMatcher(MOCK, "differentMethod", ARGUMENTS);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocation);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testCaptureArgumentsFrom_WithVarArgsMethod() {
        MethodInvocation invocation = createInvocation(MOCK, "varArgsMethod", ARGUMENT_TYPES, "arg1", 2, 3, 4, 5);
        InvocationMatcher invocationMatcher = createInvocationMatcher(MOCK, METHOD_NAME, ARGUMENTS);

        invocationMatcher.captureArgumentsFrom(invocation);

        // Validate capture arguments
    }

    private MethodInvocation createInvocation(Object mock, String methodName, Class<?>[] argumentTypes, Object... arguments) {
        MethodInvocation invocation = mock(MethodInvocation.class);
        when(invocation.getMethod()).thenReturn(getMethod(mock, methodName, argumentTypes));
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(arguments);
        return invocation;
    }

    private Method getMethod(Object mock, String methodName, Class<?>... parameterTypes) {
        try {
            return mock.getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to create method for test", e);
        }
    }

    private InvocationMatcher createInvocationMatcher(Object mock, String methodName, Object... arguments) {
        Invocation invocation = createInvocation(mock, methodName, getArgumentsTypes(arguments), arguments);
        return new InvocationMatcher(invocation);
    }

    private Class<?>[] getArgumentsTypes(Object[] arguments) {
        Class<?>[] argumentTypes = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            argumentTypes[i] = arguments[i].getClass();
        }
        return argumentTypes;
    }

    private List<Matcher> createMatchers(Class<?>[] argumentTypes, Object[] arguments) {
        List<Matcher> matchers = new ArrayList<>();
        for (int i = 0; i < arguments.length; i++) {
            matchers.add(equalTo(arguments[i]));
        }
        return matchers;
    }
}