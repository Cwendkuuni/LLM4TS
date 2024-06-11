import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Method method;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        Object mock = new Object();
        Object[] arguments = new Object[]{1, 2, 3};
        method = new Method(InvocationMatcher.class.getName(), "hasSameMethod", new Class[]{Invocation.class});
        invocation = new Invocation(new Location("TestClass.java", 1), method, mock, arguments);
        matchers = Arrays.asList(new CapturesArguments("CapturesArgumentsTest"), new CapturesArguments("CapturesArgumentsTest2"));
        matcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatchers() {
        assertTrue(matcher.matches(new InvocationMatcher(invocation, matchers).getInvocation()));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        List<Matcher> emptyMatchers = Collections.emptyList();
        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation, emptyMatchers);
        InvocationMatcher testMatcher = new InvocationMatcher(invocation);
        assertTrue(emptyMatcher.equals(testMatcher));
    }

    @Test
    public void testGetMethod() {
        Method returnedMethod = matcher.getMethod();
        assertEquals(method, returnedMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation returnedInvocation = matcher.getInvocation();
        assertEquals(invocation, returnedInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> returnedMatchers = matcher.getMatchers();
        assertEquals(matchers, returnedMatchers);
    }

    @Test
    public void testToString() {
        String toString = matcher.toString();
        String expectedToString = "InvocationMatcher[this = Invocation[this = org.mockito.internal.invocation.MockInvocationContext@2a7f56f5, method = MethodReflectionUtil.method(TestClass.class, " +
                "'hasSameMethod', [class [Ljava.lang.Object;]), arguments = [1, 2, 3]], matchers = [CapturesArguments{this = org.mockito.internal.matchers.Arg1@75aa659d, pattern = CapturesArguments.captureAny()}, CapturesArguments{this = org.mockito.internal.matchers.Arg1@4e96f82d, pattern = CapturesArguments.captureAny()}]]";
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testMatches() {
        Invocation testInvocation = new Invocation(new Location("TestClass.java", 2), method, mock, arguments);
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation, matchers);
        assertTrue(matcher.matches(testMatcher.getInvocation()));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{1, 2, "Test"};
        Invocation candidate = new Invocation(new Location("TestClass.java", 3), method, mock, arguments);
        assertFalse(matcher.safelyArgumentsMatch(candidate.getArguments()));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation(new Location("TestClass.java", 4), method, mock, arguments);
        assertTrue(matcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation(new Location("TestClass.java", 5), method, mock, arguments);
        assertTrue(matcher.hasSameMethod(candidate));
    }

    @Test(expected = AssertionError.class)
    public void testHasSameMethodWithDifferentArguments() {
        Object[] differentArguments = new Object[]{1, 2, 4};
        Invocation candidate = new Invocation(new Location("TestClass.java", 6), method, mock, differentArguments);
        matcher.hasSameMethod(candidate);
    }

    @Test
    public void testGetLocation() {
        Location returnedLocation = matcher.getLocation();
        assertEquals(invocation.getLocation(), returnedLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String toString = matcher.toString(printSettings);
        assertTrue(toString.contains("InvocationMatcher[this = Invocation[this = org.mockito.internal.invocation.MockInvocationContext@2a7f56f5, method = MethodReflectionUtil.method(TestClass.class, " +
                "'hasSameMethod', [class [Ljava.lang.Object;]), arguments = [1, 2, 3]], matchers = [CapturesArguments{this = org.mockito.internal.matchers.Arg1@75aa659d, pattern = CapturesArguments.captureAny()}, CapturesArguments{this = org.mockito.internal.matchers.Arg1@4e96f82d, pattern = CapturesArguments.captureAny()}]]"));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation testInvocation = new Invocation(new Location("TestClass.java", 7), method, mock, arguments);
        matcher.captureArgumentsFrom(testInvocation);
        for (Matcher matcher : matchers) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matcher;
                Object capturedArgument = capturesArguments.getValue();
                assertTrue(arguments[0] == capturedArgument || arguments[1] == capturedArgument || arguments[2] == capturedArgument);
            }
        }
    }
}