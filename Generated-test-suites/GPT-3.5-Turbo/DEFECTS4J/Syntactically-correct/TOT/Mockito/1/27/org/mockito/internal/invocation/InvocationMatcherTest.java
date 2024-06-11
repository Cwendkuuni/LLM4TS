import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.hamcrest.Matcher;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    // Test case for public InvocationMatcher(Invocation invocation, List<Matcher> matchers)
    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Mockito.mock(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    // Test case for public InvocationMatcher(Invocation invocation)
    @Test
    public void testInvocationMatcherWithoutMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    // Test case for public Method getMethod()
    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    // Test case for public Invocation getInvocation()
    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    // Test case for public List<Matcher> getMatchers()
    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Mockito.mock(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    // Test case for public String toString()
    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        Matcher matcher = Mockito.mock(Matcher.class);
        matchers.add(matcher);
        Mockito.when(invocation.toString()).thenReturn("MockInvocation");
        Mockito.when(matcher.toString()).thenReturn("MockMatcher");
        String expected = new PrintSettings().print(matchers, invocation);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String result = invocationMatcher.toString();

        assertEquals(expected, result);
    }

    // Test case for public boolean matches(Invocation actual)
    @Test
    public void testMatchesWithMatchingInvocation() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        Matcher matcher = Mockito.mock(Matcher.class);
        matchers.add(matcher);
        Mockito.when(invocation1.getMock()).thenReturn("Mock");
        Mockito.when(invocation2.getMock()).thenReturn("Mock");
        Mockito.when(invocation1.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation2.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(matcher.matches(Mockito.any())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        boolean result = invocationMatcher.matches(invocation2);

        assertTrue(result);
    }

    @Test
    public void testMatchesWithNonMatchingInvocation() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        Matcher matcher = Mockito.mock(Matcher.class);
        matchers.add(matcher);
        Mockito.when(invocation1.getMock()).thenReturn("Mock1");
        Mockito.when(invocation2.getMock()).thenReturn("Mock2");
        Mockito.when(invocation1.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation2.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(matcher.matches(Mockito.any())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        boolean result = invocationMatcher.matches(invocation2);

        assertFalse(result);
    }

    // Test case for public boolean hasSimilarMethod(Invocation candidate)
    @Test
    public void testHasSimilarMethodWithSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName");
        Mockito.when(method2.getName()).thenReturn("methodName");
        Mockito.when(invocation2.isVerified()).thenReturn(false);
        Mockito.when(invocation1.getMock()).thenReturn("Mock");
        Mockito.when(invocation2.getMock()).thenReturn("Mock");
        Mockito.when(invocation1.getArguments()).thenReturn(new Object[]{});
        Mockito.when(invocation2.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgsMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName1");
        Mockito.when(method2.getName()).thenReturn("methodName2");
        Mockito.when(invocation2.isVerified()).thenReturn(false);
        Mockito.when(invocation1.getMock()).thenReturn("Mock");
        Mockito.when(invocation2.getMock()).thenReturn("Mock");
        Mockito.when(invocation1.getArguments()).thenReturn(new Object[]{});
        Mockito.when(invocation2.getArguments()).thenReturn(new Object[]{});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithNonSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName1");
        Mockito.when(method2.getName()).thenReturn("methodName2");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        assertFalse(result);
    }

    // Test case for public boolean hasSameMethod(Invocation candidate)
    @Test
    public void testHasSameMethodWithSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName");
        Mockito.when(method2.getName()).thenReturn("methodName");
        Mockito.when(method1.getParameterTypes()).thenReturn(new Class[]{});
        Mockito.when(method2.getParameterTypes()).thenReturn(new Class[]{});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSameMethod(invocation2);

        assertTrue(result);
    }

    @Test
    public void testHasSameMethodWithDifferentMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName1");
        Mockito.when(method2.getName()).thenReturn("methodName2");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSameMethod(invocation2);

        assertFalse(result);
    }

    // Test case for public Location getLocation()
    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Location location = Mockito.mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location result = invocationMatcher.getLocation();

        assertEquals(location, result);
    }

    // Test case for public void captureArgumentsFrom(Invocation invocation)
    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVarArgsMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(method.isVarArgs()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFromWithMatchingMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher matcher = Mockito.mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        Object argument = new Object();
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getRawArguments()).thenReturn(new Object[]{argument});
        Mockito.when(matcher instanceof CapturesArguments).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);

        Mockito.verify((CapturesArguments) matcher).captureFrom(argument);
    }

    @Test
    public void testCaptureArgumentsFromWithoutMatchingMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher matcher = Mockito.mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getRawArguments()).thenReturn(new Object[]{});
        Mockito.when(matcher instanceof CapturesArguments).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);

        Mockito.verify((CapturesArguments) matcher, Mockito.never()).captureFrom(Mockito.any());
    }

    @Test
    public void testCaptureArgumentsFromWithVarargMatcher() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher matcher = Mockito.mock(Matcher.class);
        VarargMatcher varargMatcher = Mockito.mock(VarargMatcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getRawArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        Mockito.when(matcher instanceof VarargMatcher).thenReturn(true);
        Mockito.when((VarargMatcher) matcher).thenReturn(varargMatcher);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);

        Mockito.verify(varargMatcher).captureFrom("arg1");
        Mockito.verify(varargMatcher).captureFrom("arg2");
    }

    private List<InvocationMatcher> createInvocationMatchers(List<Invocation> invocations) {
        List<InvocationMatcher> invocationMatchers = new ArrayList<>();
    
        for (Invocation i : invocations) {
            invocationMatchers.add(new InvocationMatcher(i));
        }
    
        return invocationMatchers;
    }

    // Test case for public static List<InvocationMatcher> createFrom(List<Invocation> invocations)
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        List<InvocationMatcher> expected = createInvocationMatchers(invocations);
        assertEquals(expected, result);
    }
}