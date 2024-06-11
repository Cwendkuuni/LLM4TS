import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Stubber stubber;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(Object.class);
        stubber = Mockito.stub(mock);

        Method method = TestClass.class.getMethod("testMethod", int.class, String.class);
        invocation = new Invocation(method, new Object[]{1, "test"}, new Location(TestClass.class, "testMethod(int, String)"));
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertThat(method, is(notNullValue()));
        assertThat(method.getName(), is("testMethod"));
        assertThat(method.getParameterTypes(), is(new Class[]{int.class, String.class}));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocationFromMatcher = invocationMatcher.getInvocation();
        assertThat(invocationFromMatcher, is(invocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchersFromMatcher = invocationMatcher.getMatchers();
        assertThat(matchersFromMatcher, is(invocation.argumentsToMatchers()));
    }

    @Test
    public void testToString() {
        String toString = invocationMatcher.toString();
        String expected = "Invocation on testClass() matching: testMethod(int, String) => [arguments][1 = 1, 2 = test]";
        assertThat(toString, is(expected));
    }

    @Test
    public void testMatches() {
        Invocation otherInvocation = new Invocation(invocation.getMethod(), new Object[]{2, "test"}, new Location(TestClass.class, "testMethod(int, String)"));
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);

        boolean matches = invocationMatcher.matches(otherMatcher);
        boolean expected = false;
        assertThat(matches, is(expected));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] otherArgs = new Object[]{2, "test2"};
        boolean safelyMatches = invocationMatcher.safelyArgumentsMatch(otherArgs);
        boolean expected = false;
        assertThat(safelyMatches, is(expected));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation otherInvocation = new Invocation(invocation.getMethod(), new Object[]{1, "test"}, new Location(TestClass.class, "testMethod(int, String)"));
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(otherMatcher);
        boolean expected = true;
        assertThat(hasSimilarMethod, is(expected));
    }

    @Test
    public void testHasSameMethod() {
        Invocation otherInvocation = new Invocation(invocation.getMethod(), new Object[]{1, "test"}, new Location(TestClass.class, "testMethod(int, String)"));
        boolean hasSameMethod = invocationMatcher.hasSameMethod(new InvocationMatcher(otherInvocation));
        boolean expected = true;
        assertThat(hasSameMethod, is(expected));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Object> capturedArguments = Collections.singletonList(mock);
        invocationMatcher.captureArgumentsFrom(stubber.isA(InvocationMatcher.class));
        verify(stubber).thenReturn(capturedArguments);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String toString = invocationMatcher.toString(printSettings);
        String expected = "Invocation on testClass() matching: testMethod(int, String) => [arguments][1 = 1, 2 = test]";
        assertThat(toString, is(expected));
    }
}

class TestClass {
    public void testMethod(int i, String s) {
    }
}