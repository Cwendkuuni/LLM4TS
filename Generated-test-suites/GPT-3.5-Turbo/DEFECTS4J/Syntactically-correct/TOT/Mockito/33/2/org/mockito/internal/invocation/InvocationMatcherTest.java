import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    // Test Case 1: Test constructor with matchers list
    @Test
    public void testInvocationMatcherConstructorWithMatchersList() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});

        List<Matcher> matchers = Arrays.asList(
                equalTo(5),
                any(String.class)
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    // Test Case 2: Test constructor without matchers list
    @Test
    public void testInvocationMatcherConstructorWithoutMatchersList() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    // Test Case 3: Test getMethod()
    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    // Test Case 4: Test toString()
    @Test
    public void testToString() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = new PrintSettings();

        String expected = invocation.toString(invocation.argumentsToMatchers(), printSettings);

        assertEquals(expected, invocationMatcher.toString());
    }

    // Test Case 5: Test matches()
    @Test
    public void testMatches() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation1 = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});
        Invocation invocation2 = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    // Test Case 6: Test hasSimilarMethod()
    @Test
    public void testHasSimilarMethod() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation1 = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});
        Invocation invocation2 = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    // Test Case 7: Test hasSameMethod()
    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation1 = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});
        Invocation invocation2 = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    // Test Case 8: Test getLocation()
    @Test
    public void testGetLocation() throws NoSuchMethodException {
        Location location = mock(Location.class);
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock(MyClass.class), location, method, new Object[]{5, "test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());
    }

    // Test Case 9: Test toString(PrintSettings printSettings)
    @Test
    public void testToStringWithPrintSettings() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = new PrintSettings();

        String expected = invocation.toString(invocation.argumentsToMatchers(), printSettings);

        assertEquals(expected, invocationMatcher.toString(printSettings));
    }

    // Test Case 10: Test captureArgumentsFrom(Invocation i)
    @Test
    public void testCaptureArgumentsFrom() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});
        Invocation otherInvocation = new Invocation(mock(MyClass.class), method, new Object[]{10, "test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        CapturesArguments matcher = mock(CapturesArguments.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(otherInvocation.getArguments()).thenReturn(new Object[]{10, "test"});
        when(matcher instanceof Matchers.same).thenReturn(true);

        invocationMatcher.captureArgumentsFrom(otherInvocation);

        verify(matcher).captureFrom(10);
        verify(matcher).captureFrom("test");
    }

    // Test Case 11: Test createFrom(List<Invocation> invocations)
    @Test
    public void testCreateFromInvocations() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        Invocation invocation1 = new Invocation(mock(MyClass.class), method, new Object[]{5, "test"});
        Invocation invocation2 = new Invocation(mock(MyClass.class), method, new Object[]{10, "test"});

        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

    public static class MyClass {
        public void myMethod(int param1, String param2) {
            // Some implementation
        }
    }
}