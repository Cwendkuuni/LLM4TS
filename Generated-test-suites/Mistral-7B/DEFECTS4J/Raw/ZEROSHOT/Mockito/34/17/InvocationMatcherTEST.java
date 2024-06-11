import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Stubber stubber;
    private Object mock;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        stubber = mock(Stubber.class);
        invocation = new Invocation(new Location(1, 1), mock, new Object[]{}, new Method("testMethod", new Class[]{Integer.class, String.class}));

        List<Matcher> matchers = Arrays.asList(isA(Integer.class), isA(String.class));
        matcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testInvocationMatcherConstructionWithMatchers() {
        assertTrue(matcher instanceof InvocationMatcher);
        assertTrue(matcher.getInvocation() instanceof Invocation);
        assertTrue(matcher.getMatchers().size() == 2);
    }

    @Test
    public void testInvocationMatcherConstructionWithoutMatchers() {
        InvocationMatcher matcherWithoutMatchers = new InvocationMatcher(invocation);
        assertTrue(matcherWithoutMatchers instanceof InvocationMatcher);
        assertTrue(matcherWithoutMatchers.getInvocation() instanceof Invocation);
        assertTrue(matcherWithoutMatchers.getMatchers().size() == 0);
    }

    @Test
    public void testGetMethod() {
        Method method = matcher.getMethod();
        assertTrue(method != null);
        assertTrue(method.getName().equals("testMethod"));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = matcher.getInvocation();
        assertTrue(invocation == this.invocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = matcher.getMatchers();
        assertTrue(matchers.size() == 2);
        assertTrue(matchers.get(0) instanceof org.hamcrest.CoreMatchers);
        assertTrue(matchers.get(1) instanceof org.hamcrest.CoreMatchers);
    }

    @Test
    public void testToString() {
        String expected = String.format("Invocation[%s, %s, %s]", invocation, matchers, new PrintSettings());
        assertTrue(matcher.toString().equals(expected));
    }

    @Test
    public void testMatches() {
        InvocationMatcher otherMatcher = new InvocationMatcher(spy(InvocationMatcher.class), invocation, Arrays.asList(isA(String.class)));
        Invocation otherInvocation = new Invocation(new Location(1, 1), mock, new Object[]{1}, new Method("testMethod", new Class[]{Integer.class, String.class}));
        assertTrue(matcher.matches(invocation));
        assertFalse(matcher.matches(otherInvocation));
        assertTrue(otherMatcher.matches(otherInvocation));
        assertFalse(otherMatcher.matches(invocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        List<Object> otherArguments = Arrays.asList(1, "test");
        Invocation otherInvocation = new Invocation(new Location(1, 1), mock, otherArguments.toArray(), new Method("testMethod", new Class[]{Integer.class, String.class}));
        assertTrue(matcher.safelyArgumentsMatch(invocation.getArguments()));
        assertFalse(matcher.safelyArgumentsMatch(otherArguments.toArray()));
    }

    @Test
    public void testHasSameMethod() {
        Invocation otherInvocation = new Invocation(new Location(1, 1), mock, new Object[]{1}, new Method("testMethod", new Class[]{Integer.class, String.class}));
        assertTrue(matcher.hasSameMethod(invocation));
        assertTrue(matcher.hasSameMethod(otherInvocation));
        assertFalse(matcher.hasSimilarMethod(new Invocation(new Location(1, 1), mock, new Object[]{1, "test"}, new Method("testMethod", new Class[]{Integer.class, String.class, Integer.class}))));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation otherInvocation1 = new Invocation(new Location(1, 1), mock, new Object[]{1}, new Method("testMethod", new Class[]{Integer.class}));
        Invocation otherInvocation2 = new Invocation(new Location(1, 1), mock, new Object[]{1, "test"}, new Method("testMethod", new Class[]{Integer.class, String.class}));
        Invocation otherInvocation3 = new Invocation(new Location(1, 1), mock, new Object[]{1, "test", 1}, new Method("testMethod", new Class[]{Integer.class, String.class, Integer.class}));
        assertTrue(matcher.hasSimilarMethod(invocation));
        assertFalse(matcher.hasSimilarMethod(otherInvocation1));
        assertFalse(matcher.hasSimilarMethod(otherInvocation3));
        assertTrue(matcher.hasSimilarMethod(otherInvocation2));
    }

    @Test
    public void testCaptureArgumentsFromMethod() {
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(capturesArguments.captureFrom(anyObject())).thenReturn(null);

        matcher.captureArgumentsFrom(invocation);
        for (Matcher matcher : matcher.getMatchers()) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturedArguments = (CapturesArguments) matcher;
                verify(capturesArguments).captureFrom(invocation.getArguments()[0]);
                verify(capturesArguments).captureFrom(invocation.getArguments()[1]);
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCaptureArgumentsFromNullInvocation() {
        InvocationMatcher matcher = new InvocationMatcher(null);
        matcher.captureArgumentsFrom(null);
    }
}