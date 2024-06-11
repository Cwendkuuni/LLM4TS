import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private Method method;
    private Object[] arguments;

    @Before
    public void setup() throws Exception {
        Object mock = Mockito.mock(Object.class);
        method = Mockito.method(Object.class, "method", new Class[0]);
        invocation = new Invocation(mock, method, new Object[0], new Location("InvocationMatcherTest.java", 1));
        matchersSetup();
        invocationMatcher = new InvocationMatcher(invocation);
        arguments = new Object[]{1, 2, 3};
    }

    @Test
    public void testInvocationMatcher() {
        assertEquals("InvocationMatcher [invocation=Invocation [mock=[MockForObject@7b81d295], method=public void java.lang.Object.method()], matchers=[], location=Location [file=InvocationMatcherTest.java, line=1]]", invocationMatcher.toString());
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals("InvocationMatcher [invocation=Invocation [mock=[MockForObject@7b81d295], method=public void java.lang.Object.method(), arguments=[1, 2], location=Location [file=InvocationMatcherTest.java, line=1]], matchers=[Matcher [matcher=eq(1)], Matcher [matcher=eq(2)]]]", invocationMatcher.toString());
    }

    @Test
    public void testGetMethod() {
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        String printSettings = "PrintSettings {verbose=true, printStacktrace=true, printFailed = true, printDiff = false}";
        assertEquals("InvocationMatcher [invocation=Invocation [mock=[MockForObject@7b81d295], method=public void java.lang.Object.method(), arguments=[], location=Location [file=InvocationMatcherTest.java, line=1]], matchers=[]]", invocationMatcher.toString(new PrintSettings(true, true, true, false)));
    }

    @Test
    public void testMatches() {
        Invocation candidate = new Invocation(Mockito.mock(Object.class), method, arguments, new Location("InvocationMatcherTest.class", 1));
        assertTrue(invocationMatcher.matches(candidate));
    }

    @Test
    public void testMatchesDifferentMock() {
        Object differentMock = Mockito.mock(Object.class);
        Invocation candidate = new Invocation(differentMock, method, arguments, new Location("InvocationMatcherTest.class", 1));
        assertFalse(invocationMatcher.matches(candidate));
    }

    @Test
    public void testMatchesDifferentMethod() {
        Method differentMethod = Mockito.method(Object.class, "anotherMethod", new Class[0]);
        Invocation candidate = new Invocation(invocation.getMock(), differentMethod, arguments, new Location("InvocationMatcherTest.class", 1));
        assertFalse(invocationMatcher.matches(candidate));
    }

    @Test
    public void testMatchesVerifiedInvocation() {
        Mockito.verify(invocation.getMock()).method();
        Invocation candidate = new Invocation(invocation.getMock(), method, arguments, new Location("InvocationMatcherTest.class", 1));
        assertFalse(invocationMatcher.matches(candidate));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation(invocation.getMock(), method, arguments, new Location("InvocationMatcherTest.class", 2));
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodDifferentMethods() {
        Method differentMethod = Mockito.method(Object.class, "anotherMethod", new Class[0]);
        Invocation candidate = new Invocation(invocation.getMock(), differentMethod, arguments, new Location("InvocationMatcherTest.class", 2));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation(invocation.getMock(), method, arguments, new Location("InvocationMatcherTest.class", 2));
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodDifferentInvocations() {
        Object differentMock = Mockito.mock(Object.class);
        Invocation differentInvocation = new Invocation(differentMock, method, arguments, new Location("InvocationMatcherTest.class", 2));
        assertFalse(invocationMatcher.hasSameMethod(differentInvocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation candidate = new Invocation(invocation.getMock(), method, arguments, new Location("InvocationMatcherTest.class", 2));
        assertTrue(invocationMatcher.safelyArgumentsMatch(candidate.getArguments()));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchException() {
        Invocation candidate = new Invocation(invocation.getMock(), method, new Object[]{1, 2, 4}, new Location("InvocationMatcherTest.class", 2));
        invocationMatcher.safelyArgumentsMatch(candidate.getArguments());
    }

    @Test
    public void testHasSimilarMethodArgumentMismatch() {
        Object differentArgument = 4;
        Invocation candidate = new Invocation(invocation.getMock(), method, new Object[]{arguments[0], differentArgument}, new Location("InvocationMatcherTest.class", 2));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    private void matchersSetup() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
}