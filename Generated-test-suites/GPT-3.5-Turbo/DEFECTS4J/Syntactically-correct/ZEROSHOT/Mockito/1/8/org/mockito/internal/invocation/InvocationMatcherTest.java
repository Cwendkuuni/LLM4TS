import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.Location;
import org.mockito.invocation.Invocation;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.hamcrest.Matchers;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.invocation.DescribedInvocation;
import org.hamcrest.MatcherAssert;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherMethod() {
        Method method = new Method();
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testInvocationMatcherInvocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testInvocationMatcherMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = new PrintSettings().print(matchers, invocation);
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testInvocationMatcherMatches() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(true, invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[1];
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(true, invocationMatcher.safelyArgumentsMatch(actualArgs));
    }
  
    public void testInvocationMatcherHasSimilarMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocaationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(false, invocationMatcher.hasSimilarMethod(candidate));
    }

    public void testInvocationMatcherHasSameMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocaationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(false, invocationMatcher.hasSameMethod(candidate));
    }

    public void testInvocationMatcherGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }

    public void testInvocationMatcherCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation invocation2 = new Invocation();
        invocationMatcher.captureArgumentsFrom(invocation2);
    }

    public void testInvocationMatcherIsVarargMatcher() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Matcher matcher = Mockito.mock(Matcher.class);
        MatcherDecorator matcherDecorator = new MatcherDecorator(matcher);
        assertEquals(false, invocationMatcher.isVarargMatcher(matcher));
    }

    public void testInvocationMatcherIsVariableArgument() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        int position = 0;
        assertEquals(false, invocationMatcher.isVariableArgument(invocation, position));
    }

    public void testInvocationMatcherCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(Mockito.mock(Invocation.class));
        LinkedList<InvocationMatcher> expected = new LinkedList<>();
        for (Invocation i : invocations) {
            expected.add(new InvocationMatcher(i));
        }
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        assertEquals(expected, actual);
    }
}