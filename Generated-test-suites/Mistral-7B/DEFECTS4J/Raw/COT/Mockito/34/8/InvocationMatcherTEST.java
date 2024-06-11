import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImp;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.reflection.FieldAccessible;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocationOnMock;
    private ArgumentsComparator argumentsComparator;
    private CapturesArguments capturesArguments;

    @Before
    public void setup() {
        invocationOnMock = Mockito.spy(new InvocationOnMockImp());
        argumentsComparator = Mockito.spy(new ArgumentsComparator());
        capturesArguments = Mockito.spy(new CapturesArguments());

        Method method = Mockito.method(Mockito.class, "methodWithArgs", new Class[]{String.class, int.class, double.class});
        invocationMatcher = new InvocationMatcher(new InvocationOnMock(method, invocationOnMock, null, null, null), Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1), ArgumentMatchers.eq(2.0)));
    }

    @Test
    public void testConstructorWithMatchers() {
        assertThat(invocationMatcher.getMatchers(), is(notNullValue()));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcherWithoutMatchers = new InvocationMatcher(new InvocationOnMockImp());
        assertThat(invocationMatcherWithoutMatchers.getMatchers(), is(sameInstance(Collections.emptyList())));
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = invocationMatcher.getMethod();
        Method actualMethod = invocationMatcher.getInvocation().getMethod();
        assertThat(expectedMethod, is(sameInstance(actualMethod)));
    }

    @Test
    public void testGetInvocation() {
        assertThat(invocationMatcher.getInvocation(), is(sameInstance(invocationOnMock)));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertThat(matchers, is(notNullValue()));
    }

    @Test
    public void testToString() {
        String expectedToString = invocationMatcher.toString();
        String actualToString = invocationMatcher.toString(new PrintSettings());
        assertThat(expectedToString, is(sameInstance(actualToString)));
    }

    @Test
    public void testMatches() {
        InvocationMatcher invocationMatcherToCompare = new InvocationMatcher(new InvocationOnMockImp(), Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1), ArgumentMatchers.eq(2.0)));
        InvocationOnMock invocationToCompare = new InvocationOnMockImp();
        invocationToCompare.arguments = new Object[]{ArgumentMatchers.eq("test"), 1, 2.0};

        boolean result = invocationMatcher.matches(new InvocationOnMockImp(invocationMatcher.getMethod(), invocationMatcher, null, null, null));
        boolean expectedResult = invocationMatcher.matches(invocationToCompare);

        assertThat(result, is(expectedResult));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationOnMock invocationToCompare = new InvocationOnMockImp();
        invocationToCompare.arguments = new Object[]{ArgumentMatchers.eq("test"), 1, 2.0};

        boolean result = invocationMatcher.safelyArgumentsMatch(invocationToCompare.arguments);
        boolean expectedResult = argumentsComparator.argumentsMatch(invocationMatcher, invocationToCompare.arguments);

        assertThat(result, is(expectedResult));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithException() {
        InvocationOnMock invocationToCompare = new InvocationOnMockImp();
        invocationToCompare.arguments = new Object[]{ArgumentMatchers.eq("test"), 1, new Exception("Test Exception")};
        invocationMatcher.safelyArgumentsMatch(invocationToCompare.arguments);
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher invocationMatcherToCompare = new InvocationMatcher(new InvocationOnMockImp(), Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1), ArgumentMatchers.eq(2.0)));
        InvocationOnMock invocationToCompare = new InvocationOnMockImp();
        Method methodToCompare = Mockito.method(Mockito.class, "anotherMethod", new Class[]{int.class, String.class});
        invocationToCompare.method = methodToCompare;

        boolean result = invocationMatcher.hasSimilarMethod(new InvocationOnMockImp(invocationMatcher.getMethod(), invocationMatcher, null, null, null));
        boolean expectedResult = invocationMatcher.hasSimilarMethod(new InvocationOnMockImp(methodToCompare, invocationOnMock, null, null, null));

        assertThat(result, is(expectedResult));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher invocationMatcherToCompare = new InvocationMatcher(new InvocationOnMockImp(), Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1), ArgumentMatchers.eq(2.0)));
        InvocationMatcher invocationMatcherToCompare2 = new InvocationMatcher(new InvocationOnMockImp(), Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1), ArgumentMatchers.eq(2.0)));
        InvocationOnMock invocationToCompare = new InvocationOnMockImp();
        Method methodToCompare = invocationMatcher.getMethod();
        invocationToCompare.method = methodToCompare;

        boolean result = invocationMatcher.hasSameMethod(new InvocationOnMockImp(methodToCompare, invocationMatcher, null, null, null));
        boolean expectedResult = invocationMatcher.hasSameMethod(invocationToCompare);

        assertThat(result, is(expectedResult));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationOnMock invocationToCompare = new InvocationOnMockImp();
        Object[] argumentsToCapture = new Object[]{ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()};
        invocationMatcher.captureArgumentsFrom(invocationToCompare);

        for (Matcher matcher : invocationMatcher.getMatchers()) {
            if (matcher instanceof CapturesArguments) {
                Object capturedArgument = ((CapturesArguments) matcher).getCapturedValue();
                Object actualArgument = invocationToCompare.getArguments()[0];
                assertEquals("Captured argument should match the argument in the invocation", actualArgument, capturedArgument);
            }
        }
    }

    @Test
    public void testHasSimilarMethodWithException() {
        InvocationMatcher invocationMatcherToCompare = new InvocationMatcher(new InvocationOnMockImp(), Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1), ArgumentMatchers.eq(2.0)));
        InvocationOnMock invocationToCompareWithException = new InvocationOnMockImp();
        Method methodToCompare = invocationMatcher.getMethod();
        invocationToCompareWithException.method = methodToCompare;
        invocationToCompareWithException.thrown = new Exception("Test Exception");

        boolean result = invocationMatcher.hasSimilarMethod(new InvocationOnMockImp(methodToCompare, invocationMatcher, null, null, null));
        boolean expectedResult = invocationMatcher.hasSimilarMethod(invocationToCompareWithException);

        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethodWithException() {
        InvocationMatcher invocationMatcherToCompare = new InvocationMatcher(new InvocationOnMockImp(), Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1), ArgumentMatchers.eq(2.0)));
        InvocationOnMock invocationToCompareWithException = new InvocationOnMockImp();
        Method methodToCompare = invocationMatcher.getMethod();
        invocationToCompareWithException.method = methodToCompare;
        invocationToCompareWithException.thrown = new Exception("Test Exception");

        boolean result = invocationMatcher.hasSameMethod(new InvocationOnMockImp(methodToCompare, invocationMatcher, null, null, null));
        boolean expectedResult = false;

        assertThat(result, is(expectedResult));
    }

    private void assertEquals(String message, Object expected, Object actual) {
        try {
            FieldAccessible<Object, Object> fieldAccessible = new FieldAccessible<>();
            Object capturedValue = fieldAccessible.getFieldValue(capturesArguments, "capturedValue");
            assertEquals(message, expected, capturedValue);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}