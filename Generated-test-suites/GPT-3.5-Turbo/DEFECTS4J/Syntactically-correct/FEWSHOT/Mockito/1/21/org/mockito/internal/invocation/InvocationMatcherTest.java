import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.Invocation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;


public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertSame(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertSame(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = new PrintSettings().print(matchers, invocation);
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation actual = createInvocation(method);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] actualArgs = {};
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation candidate = createInvocation(method);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation candidate = createInvocation(method);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Location expected = invocation.getLocation();
        assertEquals(expected, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Method method = createMethod();
        Invocation invocation = createInvocation(method);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocation2 = createInvocation(method);

        try {
            invocationMatcher.captureArgumentsFrom(invocation2);
        } catch (UnsupportedOperationException e) {
            // Test passed
        }
    }

    private Method createMethod() {
        Method method = null;
        try {
            method = InvocationMatcher.class.getDeclaredMethod("getMethod");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    private Invocation createInvocation(Method method) {
        Invocation invocation = new Invocation() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public boolean isVerified() {
                return false;
            }

            @Override
            public void markVerified() {}

            @Override
            public Object getArgumentAt(int index, Class type) {
                return null;
            }

            @Override
            public Object[] getRawArguments() {
                return null;
            }

            @Override
            public void writeReplace() throws java.io.ObjectStreamException {}

            @Override
            public int getSequenceNumber() {
                return 0;
            }

            @Override
            public Location getLocation() {
                return null;
            }
        };
        return invocation;
    }
}