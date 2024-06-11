import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.mockitoSession;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Method method;
    private List<Matcher> matchers;
    private mockitoSession session;

    @Before
    public void setUp() {
        session = new mockitoSession();
        method = mock(Method.class);
        invocation = new Invocation(new Location("TestClass.java", 1, 1), method, new Object[0]);
        matchers = Arrays.asList(is("test"), is(1));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testGetInvocation() {
        assertThat(invocationMatcher.getInvocation(), is(invocation));
    }

    @Test
    public void testGetMatchers() {
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = new PrintSettings();
        String expected = String.format("invocation: %s%n%s", invocation, matchers.toString());
        assertThat(invocationMatcher.toString(printSettings), is(expected));
    }

    @Test
    public void testMatches() {
        Invocation anotherInvocation = new Invocation(new Location("AnotherTestClass.java", 1, 1), method, new Object[0]);
        InvocationMatcher anotherMatcher = new InvocationMatcher(anotherInvocation);
        assertThat(invocationMatcher.matches(anotherMatcher), is(false));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation anotherInvocation = new Invocation(new Location("AnotherTestClass.java", 1, 1), method, new Object[0]);
        InvocationMatcher anotherMatcher = new InvocationMatcher(anotherInvocation);
        InvocationMatcher spyMatcher = spy(invocationMatcher);
        spyMatcher.invocation = anotherInvocation;
        assertThat(invocationMatcher.hasSimilarMethod(anotherMatcher), is(false));
        assertThat(spyMatcher.hasSimilarMethod(anotherMatcher), is(true));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatch() {
        Invocation anotherInvocation = new Invocation(new Location("AnotherTestClass.java", 1, 1), method, new Object[]{1, 2});
        InvocationMatcher anotherMatcher = new InvocationMatcher(anotherInvocation);
        invocationMatcher.safelyArgumentsMatch(anotherMatcher.getArguments());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] arguments = new Object[]{1, 2};
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        for (Matcher matcher : invocationMatcher.getMatchers()) {
            if (matcher instanceof CapturesArguments) {
                ((CapturesArguments) matcher).captureFrom(arguments[0]);
                assertThat(((CapturesArguments) matcher).getCapturedValue(), is(arguments[0]));
            }
        }
    }
}