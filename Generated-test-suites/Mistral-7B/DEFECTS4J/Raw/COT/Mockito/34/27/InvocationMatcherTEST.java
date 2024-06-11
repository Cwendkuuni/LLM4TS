import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImp;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.InvocationMatcher;
import org.mockito.internal.matchers.InvocationMatcherImpl;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = Mockito.mock(InvocationOnMockImp.class);
        matchers = Arrays.asList(ArgThat.isInstanceOf(String.class));
        InvocationMatcher invocationMatcherMock = mock(InvocationMatcher.class);
        FieldSetter.setField(invocationMatcherMock, "invocation", invocation);
        FieldSetter.setField(invocationMatcherMock, "matchers", matchers);
        this.invocationMatcher = invocationMatcherMock;
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyListOfMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndListOfMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testGetMethod() {
        Method method = new Method("testMethod", new Class[]{}, new Class[0]);
        Invocation invocation = new InvocationOnMockImp(method, new Object[0], new Location(2, 3));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method returnedMethod = invocationMatcher.getMethod();
        assertThat(returnedMethod, is(method));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMockImp.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation returnedInvocation = invocationMatcher.getInvocation();
        assertThat(returnedInvocation, is(invocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = Arrays.asList(ArgThat.isInstanceOf(String.class), ArgThat.isInstanceOf(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> returnedMatchers = invocationMatcher.getMatchers();
        assertThat(returnedMatchers, is(matchers));
    }

    @Test
    public void testToString() {
        String methodName = "testMethod";
        Method method = new Method(methodName, new Class[]{}, new Class[0]);
        Invocation invocation = new InvocationOnMockImp(method, new Object[0], new Location(2, 3));
        List<Matcher> matchers = Arrays.asList(ArgThat.isInstanceOf(String.class), ArgThat.isInstanceOf(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString();
        String expected = "InvocationMatcher[this = MockitoInvocationInterceptor$$EnhancerByMockitoCGLIB$$1a7b2e4b, method = testMethod()]";
        assertThat(result, is(expected));
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new InvocationOnMockImp(new Method("testMethod", new Class[]{}, new Class[0]), new Object[0], new Location(2, 3));
        Invocation invocation2 = new InvocationOnMockImp(new Method("testMethod", new Class[]{}, new Class[0]), new Object[0], new Location(4, 5));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        InvocationMatcher stringMatcher = new InvocationMatcher(invocation1, Arrays.asList(stringArgumentCaptor.capture()));

        boolean result = invocationMatcher.matches(invocation2);
        boolean expected = false;

        assertThat(result, is(expected));

        String capturedString = stringArgumentCaptor.getValue();
        assertThat(capturedString, is(""));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{1, "two"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        boolean expected = false;

        assertThat(result, is(expected));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithException() {
        Object[] actualArgs = new Object[]{1, "two"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationMatcherImpl(new Method("testMethod", new Class[]{}, new Class[0]), new Object[0], new Location(2, 3)));

        invocationMatcher.safelyArgumentsMatch(actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new InvocationOnMockImp(new Method("testMethod", new Class[]{}, new Class[0]), new Object[0], new Location(2, 3));
        Invocation invocation2 = new InvocationOnMockImp(new Method("testMethod", new Class[]{}, new Class[0]), new Object[0], new Location(4, 5));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        boolean result = invocationMatcher.hasSimilarMethod(new InvocationMatcherImpl(invocation2.getMethod(), invocation2.getArguments(), invocation2.getLocation()));
        boolean expected = false;

        assertThat(result, is(expected));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new InvocationOnMockImp(new Method("testMethod", new Class[]{}, new Class[0]), new Object[0], new Location(2, 3));
        Invocation invocation2 = new InvocationOnMockImp(new Method("testMethod", new Class[]{}, new Class[0]), new Object[0], new Location(4, 5));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        Method method = invocation1.getMethod();
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationMatcherImpl(method, new Object[0], new Location(0, 0)));

        boolean result = invocationMatcher.hasSameMethod(invocationMatcher2);
        boolean expected = true;

        assertThat(result, is(expected));
    }

    @Test
    public void testToStringWithPrintSettings() {
        String methodName = "testMethod";
        Method method = new Method(methodName, new Class[]{}, new Class[0]);
        Invocation invocation = new InvocationOnMockImp(method, new Object[0], new Location(2, 3));
        List<Matcher> matchers = Arrays.asList(ArgThat.isInstanceOf(String.class), ArgThat.isInstanceOf(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = new PrintSettings();
        String result = invocationMatcher.toString(printSettings);
        String expected = "InvocationMatcher[this = MockitoInvocationInterceptor$$EnhancerByMockitoCGLIB$$1a7b2e4b, method = testMethod()]";
        assertThat(result, is(expected));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new InvocationOnMockImp(new Method("testMethod", new Class[]{}, new Class[0]), new Object[0], new Location(2, 3));
        int expectedInteger = 42;
        String expectedString = "mockito";

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(integerArgumentCaptor.capture(), stringArgumentCaptor.capture()));

        invocationMatcher.captureArgumentsFrom(new InvocationOnMockImp(new Method("testMethod", new Class[]{Integer.TYPE, String.class}, new Class[0]), new Object[]{expectedInteger, expectedString}, new Location(4, 5)));

        int capturedInteger = integerArgumentCaptor.getValue();
        String capturedString = stringArgumentCaptor.getValue();

        assertThat(capturedInteger, is(expectedInteger));
        assertThat(capturedString, is(expectedString));
    }
}