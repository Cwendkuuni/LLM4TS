import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocation;

    @Before
    public void setUp() {
        Object mock = Mockito.mock(Object.class);
        Method method = getClass().getMethod("testMethod", Object.class, int.class);
        invocation = new InvocationOnMockImpl(mock, method);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testDefaultConstructor() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), notNullValue());
        assertThat(invocationMatcher.getMatchers().isEmpty(), is(true));
    }

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(42));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), notNullValue());
        assertThat(invocationMatcher.getMatchers().size(), is(2));
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();
        assertThat(actualMethod, is(sameInstance(expectedMethod)));
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertThat(actualInvocation, is(sameInstance(expectedInvocation)));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = Collections.singletonList(ArgumentMatchers.eq("test"));
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertThat(actualMatchers, is(sameInstance(expectedMatchers)));
    }

    @Test
    public void testToString() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(42));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expectedToString = "InvocationMatcher{invocation=InvocationMock{method=[Method public void testMethod(Ljava/lang/String;I)], target=org.mockito.internal.InvocationMatcherTest$TestClass@7852f174}, matchers=[ArgumentMatchers.eq(test), ArgumentMatchers.eq(42)]}";
        String actualToString = invocationMatcher.toString();
        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testMatches() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        InvocationOnMock invocationToMatch = new InvocationOnMockImpl(mock, invocation.getMethod());
        Object[] argumentsToMatch = new Object[]{new Object(), 42};
        invocationToMatch.setArguments(argumentsToMatch);
        boolean actualMatches = invocationMatcher.matches(invocationToMatch);
        boolean expectedMatches = true;
        assertThat(actualMatches, is(expectedMatches));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(42));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] argumentsToMatch = new Object[]{new Object(), 42};
        boolean actualMatches = invocationMatcher.safelyArgumentsMatch(argumentsToMatch);
        boolean expectedMatches = true;
        assertThat(actualMatches, is(expectedMatches));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationOnMock invocationToCompare = new InvocationOnMockImpl(mock, invocation.getMethod());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean actualHasSimilarMethod = invocationMatcher.hasSimilarMethod(invocationToCompare);
        boolean expectedHasSimilarMethod = true;
        assertThat(actualHasSimilarMethod, is(expectedHasSimilarMethod));
    }

    @Test
    public void testHasSameMethod() {
        InvocationOnMock invocationToCompare = new InvocationOnMockImpl(mock, invocation.getMethod());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean actualHasSameMethod = invocationMatcher.hasSameMethod(invocationToCompare);
        boolean expectedHasSameMethod = true;
        assertThat(actualHasSameMethod, is(expectedHasSameMethod));
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = invocation.getLocation();
        Location actualLocation = invocationMatcher.getLocation();
        assertThat(actualLocation, is(sameInstance(expectedLocation)));
    }

    @Test(expected = AssertionError.class)
    public void testCaptureArgumentsFromThrowsAssertionErrorOnVarargs() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.instanceOf(String.class), ArgumentMatchers.instanceOf(String[].class));
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(42));
        invocationMatcher.captureArgumentsFrom(invocation);
        Object argument1 = invocationMatcher.matchers.get(0).getValue();
        Object argument2 = invocationMatcher.matchers.get(1).getValue();
        assertThat(argument1, is("test"));
        assertThat(argument2, is(42));
    }

    @Test
    public void testCreateFrom() {
        Object mock = Mockito.mock(Object.class);
        List<Invocation> invocations = Arrays.asList(invocation, new InvocationOnMockImpl(mock, invocation.getMethod()));
        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher expectedInvocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher expectedInvocationMatcher2 = new InvocationMatcher(invocationToCompare);
        List<InvocationMatcher> expectedInvocationMatchers = Arrays.asList(expectedInvocationMatcher1, expectedInvocationMatcher2);
        assertThat(actualInvocationMatchers, is(sameInstance(expectedInvocationMatchers)));
    }
}