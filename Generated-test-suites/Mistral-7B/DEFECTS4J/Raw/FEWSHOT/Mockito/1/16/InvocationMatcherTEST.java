package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.arrayWithSize;
import static org.mockito.internal.matchers.ArgumentsMatcher.equalTo;
import static org.mockito.internal.matchers.ArgumentsMatcher.not;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameInstance;
import static org.mockito.internal.matchers.ArgumentsMatcher.type;
import static org.mockito.internal.reporting.PrintSettings.print;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testNewInstance() {
        Invocation invocation = createInvocationWithMethod(String.class, "toString", new Object[]{"abc"});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(toStringMethod(), matcher.getMethod());
        assertEquals(Collections.emptyList(), matcher.getMatchers());
    }

    @Test
    public void testNewInstanceWithMatchers() {
        Invocation invocation = createInvocationWithMethod(String.class, "length", new Object[]{"abc"});
        List<Matcher> matchers = Arrays.asList(type(String.class), equalTo("abc"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(lengthMethod(), matcher.getMethod());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testMatchesSimple() {
        Invocation invocation1 = createInvocationWithMethod(String.class, "toString", new Object[]{"abc"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String.class, "toString", new Object[]{"abc"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.matches(invocation1));
        assertTrue(matcher1.matches(matcher2));
        assertTrue(matcher2.matches(invocation1));
        assertTrue(matcher2.matches(matcher2));
    }

    @Test
    public void testMatchesDifferentMethods() {
        Invocation invocation1 = createInvocationWithMethod(String.class, "length", new Object[]{"abc"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(Integer.class, "intValue", new Object[]{123});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
        assertFalse(matcher2.matches(matcher1));
    }

    @Test
    public void testMatchesSameMethodButDifferentArgs() {
        Invocation invocation1 = createInvocationWithMethod(String.class, "length", new Object[]{"abc"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String.class, "length", new Object[]{"def"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
        assertFalse(matcher2.matches(matcher1));
    }

    @Test
    public void testMatchesSameMethodButDifferentInstance() {
        Object mock1 = createMock(String.class);
        Object mock2 = createMock(String.class);
        Invocation invocation1 = createInvocationWithMethod(String.class, "toString", new Object[]{mock1});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String.class, "toString", new Object[]{mock2});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
        assertFalse(matcher2.matches(matcher1));
    }

    @Test
    public void testMatchesSameMethodButVerified() {
        Object mock = createMock(String.class);
        Invocation invocation = createInvocationWithMethod(String.class, "toString", new Object[]{mock});
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        invocation.getMock().verify(mock).toString();

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatchesSameMethodButUnverified() {
        Object mock = createMock(String.class);
        Invocation invocation = createInvocationWithMethod(String.class, "toString", new Object[]{mock});

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertFalse(matcher.matches(invocation));
    }

    @Test
    public void testMatchesSameMethodButOverloaded() {
        Object mock = createMock(String.class);
        Invocation invocation1 = createInvocationWithMethod(String.class, "toString", new Object[]{"abc"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String[].class, "toString", new Object[]{new Object[]{"def"}});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
        assertFalse(matcher2.matches(matcher1));
    }

    @Test
    public void testMatchesSameMethodButDifferentVarargs() {
        Invocation invocation1 = createInvocationWithMethod(String.class, "println", new Object[]{"abc"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String.class, "println", new Object[]{"def", "ghi"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
        assertFalse(matcher2.matches(matcher1));
    }

    @Test
    public void testMatchesSameMethodButSameVarargs() {
        Invocation invocation1 = createInvocationWithMethod(String.class, "println", new Object[]{"abc", "def"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String.class, "println", new Object[]{"abc", "def"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.matches(matcher2));
        assertTrue(matcher2.matches(matcher1));
    }

    @Test
    public void testMatchesSameMethodButDifferentVarargsOrder() {
        Invocation invocation1 = createInvocationWithMethod(String.class, "println", new Object[]{"abc", "def"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String.class, "println", new Object[]{"def", "abc"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
        assertFalse(matcher2.matches(matcher1));
    }

    @Test
    public void testMatchesSameMethodButSameVarargsDifferentTypes() {
        Invocation invocation1 = createInvocationWithMethod(String.class, "println",
                new Object[]{"abc", (Object) 123});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String.class, "println",
                new Object[]{"def", (Object) "ghi"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
        assertFalse(matcher2.matches(matcher1));
    }

    @Test
    public void testMatchesSameMethodButSameVarargsDifferentTypesOrder() {
        Invocation invocation1 = createInvocationWithMethod(String.class, "println",
                new Object[]{"abc", (Object) "def"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String.class, "println",
                new Object[]{"def", (Object) "abc"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
        assertFalse(matcher2.matches(matcher1));
    }

    @Test
    public void testMatchesSameMethodButSameVarargsSameTypes() {
        Invocation invocation1 = createInvocationWithMethod(String.class, "println",
                new Object[]{"abc", "def"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String.class, "println",
                new Object[]{"def", "abc"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.matches(matcher2));
        assertTrue(matcher2.matches(matcher1));
    }

    @Test
    public void testMatchesSameMethodButSameVarargsSameTypesDifferentOrder() {
        Invocation invocation1 = createInvocationWithMethod(String.class, "println",
                new Object[]{"abc", "def"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocationWithMethod(String.class, "println",
                new Object[]{"def", "abc"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.matches(matcher2));
        assertTrue(matcher2.matches(matcher1));
    }

    @Test
    public void testCaptureArguments() {
        Object mock = createMock(String.class);
        Object arg = "abc";
        Invocation invocation = createInvocationWithMethod(String.class, "toString", new Object[]{mock});
        CapturesArguments captor = org.mockito.ArgumentCaptor.forClass(String.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        matcher.captureArgumentsFrom(invocation);

        String captured = captor.getValue();
        assertEquals(arg, captured);
    }

    @Test
    public void testCaptureArgumentsVarargs() {
        Object mock = createMock(String.class);
        Object[] args = new Object[]{"abc", "def"};
        Invocation invocation = createInvocationWithMethod(String.class, "println", args);
        CapturesArguments captor = org.mockito.ArgumentCaptor.forClass(String.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        matcher.captureArgumentsFrom(invocation);

        List<String> captured = captor.getAllValues();
        assertEquals(Arrays.asList("abc", "def"), captured);
    }

    @Test
    public void testCaptureArgumentsVarargsWithVarargMatcher() {
        Object mock = createMock(String.class);
        Object[] args = new Object[]{"abc", "def", "ghi"};
        Invocation invocation = createInvocationWithMethod(String.class, "println", args);
        VarargMatcher captor = ArgumentMatchers.varargs(Arrays.asList(equalTo("abc"), equalTo("def"), equalTo("ghi")));

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        matcher.captureArgumentsFrom(invocation);

        List<Matcher> captured = matcher.getMatchers();
        assertEquals(Arrays.asList(captor), captured);
    }

    @Test
    public void testCaptureArgumentsWithSameMatcherMultipleTimes() {
        Object mock = createMock(String.class);
        Object arg = "abc";
        Invocation invocation = createInvocationWithMethod(String.class, "toString", new Object[]{mock});
        CapturesArguments captor = org.mockito.ArgumentCaptor.forClass(String.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        matcher.captureArgumentsFrom(invocation);
        matcher.captureArgumentsFrom(invocation);

        List<String> captured = new ArrayList<>();
        captured.add(captor.getValue());
        captured.add(captor.getValue());

        assertEquals(captured, Arrays.asList(arg, arg));
    }

    @Test
    public void testCaptureArgumentsWithDifferentMatchers() {
        Object mock = createMock(String.class);
        Object arg1 = "abc";
        Object arg2 = "def";
        Invocation invocation = createInvocationWithMethod(String.class, "toString", new Object[]{mock});
        CapturesArguments captor1 = org.mockito.ArgumentCaptor.forClass(String.class);
        CapturesArguments captor2 = org.mockito.ArgumentCaptor.forClass(String.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        matcher.captureArgumentsFrom(invocation);
        matcher.captureArgumentsFrom(invocation);

        List<String> captured = new ArrayList<>();
        captured.add(captor1.getValue());
        captured.add(captor2.getValue());

        assertEquals(Arrays.asList(arg1, arg2), captured);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(createInvocationWithMethod(String.class, "toString", new Object[]{"abc"}),
                createInvocationWithMethod(String.class, "length", new Object[]{"def"}));
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, matchers.size());
        for (InvocationMatcher matcher : matchers) {
            assertEquals(1, matcher.getMatchers().size());
        }
    }

    private Method toStringMethod() {
        return getMethod(String.class, "toString");
    }

    private Method lengthMethod() {
        return getMethod(String.class, "length");
    }

    private Method getMethod(Class clazz, String methodName) {
        try {
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createMock(Class clazz) {
        return Mockito.mock(clazz);
    }

    private Invocation createInvocationWithMethod(Class clazz, String methodName, Object[] arguments) {
        try {
            Method method = getMethod(clazz, methodName);
            return new Mockito.invocation(method, arguments);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}