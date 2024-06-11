import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.invocation.MockitoMethod;
import org.mockito.invocation.PrintSettings;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Matcher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Object[] invocationArguments;

    @Before
    public void setUp() {
        MockitoMethod method = mock(MockitoMethod.class);
        invocation = new Invocation(method, null, new Object[0]);
        Object mock = mock(Serializable.class);

        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2"));
        matcher = new InvocationMatcher(invocation, matchers);
        invocationArguments = new Object[]{new Object(), new Object()};
    }

    @Test
    public void testMatches() {
        Invocation invocationToTest = new Invocation(method, null, invocationArguments);
        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest);

        assertTrue(matcher.matches(testMatcher));
    }

    @Test
    public void testMatchesWithEmptyMatcherList() {
        InvocationMatcher testMatcher = new InvocationMatcher(invocation);
        assertTrue(matcher.matches(testMatcher));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation invocationToTest = new Invocation(new MockitoMethod("differentMethodName", new Class[0]), null, invocationArguments);
        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest);

        assertFalse(matcher.matches(testMatcher));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Object mock2 = mock(Serializable.class);
        Invocation invocationToTest = new Invocation(method, null, invocationArguments);
        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest, mock2);

        assertFalse(matcher.matches(testMatcher));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentArguments() {
        Object[] differentArguments = new Object[]{new Object(), new Object()};
        Invocation invocationToTest = new Invocation(method, null, differentArguments);
        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest);

        assertFalse(matcher.matches(testMatcher));
    }

    @Test
    public void testMatchesWithSameMethodButSameArguments() {
        Invocation invocationToTest = new Invocation(method, null, invocationArguments);
        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest);

        assertTrue(matcher.matches(testMatcher));
    }

    @Test
    public void testMatchesWithSameMethodButOverloaded() {
        MockitoMethod overloadedMethod = mock(MockitoMethod.class);
        Invocation invocationToTest = new Invocation(overloadedMethod, null, invocationArguments);
        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest);

        assertFalse(matcher.matches(testMatcher));
    }

    @Test
    public void testMatchesWithSameMethodButSameArgumentsAndOverloaded() {
        MockitoMethod overloadedMethod = mock(MockitoMethod.class);
        Invocation invocationToTest = new Invocation(overloadedMethod, null, invocationArguments);
        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest);

        assertTrue(matcher.safelyArgumentsMatch(testMatcher.getArguments()));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocationToTest = new Invocation(new MockitoMethod("similarNameButOverloaded", new Class[0]), null, invocationArguments);
        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest);

        assertFalse(matcher.hasSimilarMethod(testMatcher));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocationToTest = new Invocation(method, null, invocationArguments);
        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest);

        assertTrue(matcher.hasSameMethod(testMatcher));
    }

    @Test
    public void testToString() {
        String expected = "InvocationMatcher [invocation=" + invocation + ", matchers=" + matchers + "]";
        String actual = matcher.toString();

        assertTrue(expected.equals(actual));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] testArguments = new Object[2];
        matcher.captureArgumentsFrom(new Invocation(method, null, testArguments));

        for (int i = 0; i < matchers.size(); i++) {
            if (matchers.get(i) instanceof CapturesArguments) {
                CapturesArguments testCapturesArguments = (CapturesArguments) matchers.get(i);
                assertEquals(testArguments[i], testCapturesArguments.getValue());
            }
        }
    }

    @Test(expected = RuntimeException.class)
    public void testCaptureArgumentsFromWithInvalidMatcher() {
        matcher.captureArgumentsFrom(new Invocation(method, null, invocationArguments));
    }
}