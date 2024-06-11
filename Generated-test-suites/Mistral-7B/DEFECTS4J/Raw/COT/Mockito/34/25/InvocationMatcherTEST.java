import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocation;

    @Before
    public void setUp() {
        Object mock = mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithTwoArguments");
        invocation = new InvocationOnMock(mock, method, new Object[]{1, 2});
    }

    @Test
    public void testConstructorWithInvocationAndEmptyListOfMatchers() {
        List<Matcher> emptyMatchersList = Collections.emptyList();
        invocationMatcher = new InvocationMatcher(invocation, emptyMatchersList);

        Method method = invocation.getMethod();
        Invocation invocationToTest = new InvocationOnMock(mock, method, new Object[]{1, 2});

        assertThat(invocationMatcher.getMethod(), is(method));
        assertThat(invocationMatcher.getInvocation(), sameInstance(invocation));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testConstructorWithInvocationAndListOfMatchers() {
        Matcher arg1Matcher = ArgumentMatchers.anyInt();
        Matcher arg2Matcher = ArgumentMatchers.eq(2);
        List<Matcher> matchers = Arrays.asList(arg1Matcher, arg2Matcher);

        invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = invocation.getArguments();
        int arg1 = (int) arguments[0];
        Object arg2 = arguments[1];

        verify(mock).methodWithTwoArguments(arg1, arg2);

        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testMatchesMethodWithSameNameAndSameMock() {
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(mock, method);
        assertThat(invocationMatcher.matches(anotherInvocationMatcher), is(true));
    }

    @Test
    public void testMatchesMethodWithSameNameButDifferentMock() {
        Object anotherMock = mock(Object.class);
        Invocation anotherInvocation = new InvocationOnMock(anotherMock, method, new Object[]{1, 2});
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(anotherInvocation, Collections.emptyList());

        assertThat(invocationMatcher.matches(anotherInvocationMatcher), is(false));
    }

    @Test
    public void testMatchesMethodWithDifferentNameButSameArguments() {
        Object anotherMock = mock(Object.class);
        Method anotherMethod = Mockito.method(Object.class, "differentMethodName");
        Invocation anotherInvocation = new InvocationOnMock(anotherMock, anotherMethod, new Object[]{1, 2});

        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(anotherInvocation, Collections.emptyList());

        assertThat(invocationMatcher.matches(anotherInvocationMatcher), is(false));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithDifferentTypes() {
        Object anotherMock = mock(Object.class);
        Method anotherMethod = Mockito.method(Object.class, "methodWithTwoArguments");
        Invocation anotherInvocation = new InvocationOnMock(anotherMock, method, new Object[]{1, "two"});

        invocationMatcher.safelyArgumentsMatch(anotherInvocation.getArguments());
    }

    @Test
    public void testHasSimilarMethodWithSameMethodNameAndDifferentArguments() {
        Object anotherMock = mock(Object.class);
        Method anotherMethod = Mockito.method(Object.class, "methodWithTwoArguments");
        Invocation anotherInvocation = new InvocationOnMock(anotherMock, anotherMethod, new Object[]{1, 3});

        assertThat(invocationMatcher.hasSimilarMethod(new InvocationMatcher(anotherInvocation, Collections.emptyList())), is(false));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodNameAndSameArguments() {
        Object anotherMock = mock(Object.class);
        Method anotherMethod = Mockito.method(Object.class, "methodWithTwoArguments");
        Invocation anotherInvocation = new InvocationOnMock(anotherMock, anotherMethod, new Object[]{1, 2});

        assertThat(invocationMatcher.hasSimilarMethod(new InvocationMatcher(anotherInvocation, Collections.emptyList())), is(true));
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expectedToString = "Invoked method: methodWithTwoArguments(java.lang.Object, java.lang.Object)\n" +
                "Arguments are:\n" +
                "[1, 2]\n";

        String actualToString = invocationMatcher.toString(printSettings);

        assertThat(actualToString, is(expectedToString));
    }
}