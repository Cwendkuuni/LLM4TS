import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.internal.created.PowerMockCreator;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatcherList() {
        Invocation mockInvocation = PowerMockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        Method method = invocationMatcher.getMethod();
        Method expectedMethod = mockInvocation.getMethod();
        assertThat(method, is(expectedMethod));
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatcherList() {
        Invocation mockInvocation = PowerMockito.mock(Invocation.class);
        Matcher mockMatcher = mock(Matcher.class);
        List<Matcher> matcherList = Arrays.asList(mockMatcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matcherList);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        List<Matcher> expectedMatchers = matcherList;
        assertThat(actualMatchers, is(expectedMatchers));
    }

    @Test
    public void testGetMethod() {
        Invocation mockInvocation = PowerMockito.mock(Invocation.class);
        Method expectedMethod = PowerMockCreator.createMethod(Object.class, "testMethod", new Class[0]);
        PowerMockito.when(mockInvocation.getMethod()).thenReturn(expectedMethod);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        Method actualMethod = invocationMatcher.getMethod();

        assertThat(actualMethod, is(expectedMethod));
    }

    @Test
    public void testGetInvocation() {
        Invocation mockInvocation = PowerMockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        Invocation actualInvocation = invocationMatcher.getInvocation();
        Invocation expectedInvocation = mockInvocation;
        assertThat(actualInvocation, is(expectedInvocation));
    }

    @Test
    public void testGetMatchers() {
        Invocation mockInvocation = PowerMockito.mock(Invocation.class);
        List<Matcher> mockMatchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        PowerMockito.when(mockInvocation.argumentsToMatchers()).thenReturn(mockMatchers);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        List<Matcher> expectedMatchers = mockMatchers;
        assertThat(actualMatchers, is(expectedMatchers));
    }

    @Test
    public void testToString() {
        Invocation mockInvocation = PowerMockito.mock(Invocation.class);
        String expectedToString = "Invocation[this=org.mockito.internal.invocation.Invocation@123, method=testMethod()V, arguments=[], invocationSite=InvocationSite{lineNumber=42, methodName=testMethod, className=TestClass}]";
        PowerMockito.when(mockInvocation.toString(any(List.class), any(PrintSettings.class))).thenReturn(expectedToString);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        String actualToString = invocationMatcher.toString();

        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testMatches() {
        Invocation mockInvocation1 = PowerMockito.mock(Invocation.class);
        Invocation mockInvocation2 = PowerMockito.mock(Invocation.class);
        when(mockInvocation1.getMock()).thenReturn(mock(Object.class));
        when(mockInvocation2.getMock()).thenReturn(mock(Object.class));
        when(mockInvocation1.getMethod()).thenReturn(PowerMockito.method(Object.class, "testMethod", new Class[0]));
        when(mockInvocation2.getMethod()).thenReturn(PowerMockito.method(Object.class, "testMethod", new Class[0]));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);

        boolean actualResult = invocationMatcher1.matches(invocationMatcher2);
        boolean expectedResult = true;
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testSafelyArgumentsMatchWithMatchingArguments() {
        Object[] mockArguments = new Object[0];
        InvocationMatcher mockInvocationMatcher = mock(InvocationMatcher.class);
        when(mockInvocationMatcher.safelyArgumentsMatch(mockArguments)).thenReturn(true);

        boolean actualResult = new InvocationMatcher(new Object[0], Arrays.asList(mockInvocationMatcher)).safelyArgumentsMatch(mockArguments);
        boolean expectedResult = true;
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testSafelyArgumentsMatchWithDifferentArguments() {
        Object[] mockArguments = new Object[0];
        InvocationMatcher mockInvocationMatcher = mock(InvocationMatcher.class);
        when(mockInvocationMatcher.safelyArgumentsMatch(mockArguments)).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Object[0], Arrays.asList(mockInvocationMatcher));
        boolean actualResult = invocationMatcher.safelyArgumentsMatch(new Object[]{1, 2, 3});
        boolean expectedResult = false;
        assertThat(actualResult, is(expectedResult));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithThrowable() {
        Object[] mockArguments = new Object[0];
        InvocationMatcher mockInvocationMatcher = mock(InvocationMatcher.class);
        when(mockInvocationMatcher.safelyArgumentsMatch(mockArguments)).thenThrow(new RuntimeException());

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Object[0], Arrays.asList(mockInvocationMatcher));
        invocationMatcher.safelyArgumentsMatch(mockArguments);
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation mockInvocation1 = PowerMockito.mock(Invocation.class);
        Invocation mockInvocation2 = PowerMockito.mock(Invocation.class);
        when(mockInvocation1.getMethod()).thenReturn(PowerMockito.method(Object.class, "testMethod1", new Class[0]));
        when(mockInvocation2.getMethod()).thenReturn(PowerMockito.method(Object.class, "testMethod2", new Class[0]));

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation1);
        boolean actualResult = invocationMatcher.hasSameMethod(new InvocationMatcher(mockInvocation2));
        boolean expectedResult = false;
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        Invocation mockInvocation = PowerMockito.mock(Invocation.class);
        Method expectedMethod = PowerMockito.method(Object.class, "testMethod", new Class[0]);
        PowerMockito.when(mockInvocation.getMethod()).thenReturn(expectedMethod);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation);

        boolean actualResult = invocationMatcher1.hasSameMethod(invocationMatcher2);
        boolean expectedResult = true;
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodsAndDifferentArguments() {
        Invocation mockInvocation1 = PowerMockito.mock(Invocation.class);
        Invocation mockInvocation2 = PowerMockito.mock(Invocation.class);
        when(mockInvocation1.getMethod()).thenReturn(PowerMockito.method(Object.class, "testMethod1", new Class[0]));
        when(mockInvocation2.getMethod()).thenReturn(PowerMockito.method(Object.class, "testMethod2", new Class[0]));

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation1);
        boolean actualResult = invocationMatcher.hasSimilarMethod(new InvocationMatcher(mockInvocation2));
        boolean expectedResult = false;
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndDifferentArguments() {
        Invocation mockInvocation = PowerMockito.mock(Invocation.class);
        Method expectedMethod = PowerMockito.method(Object.class, "testMethod", new Class[0]);
        PowerMockito.when(mockInvocation.getMethod()).thenReturn(expectedMethod);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation);
        Object[] arguments1 = new Object[]{1, 2};
        when(mockInvocation.getArguments()).thenReturn(arguments1);

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation);
        Object[] arguments2 = new Object[]{3, 4};
        when(mockInvocation.getArguments()).thenReturn(arguments2);

        boolean actualResult = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        boolean expectedResult = false;
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndSameArguments() {
        Invocation mockInvocation = PowerMockito.mock(Invocation.class);
        Method expectedMethod = PowerMockito.method(Object.class, "testMethod", new Class[0]);
        PowerMockito.when(mockInvocation.getMethod()).thenReturn(expectedMethod);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation);
        Object[] arguments1 = new Object[]{1, 2};
        when(mockInvocation.getArguments()).thenReturn(arguments1);

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation);
        Object[] arguments2 = new Object[]{1, 2};
        when(mockInvocation.getArguments()).thenReturn(arguments2);

        boolean actualResult = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        boolean expectedResult = true;
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButDifferentMocks() {
        Invocation mockInvocation1 = PowerMockito.mock(Invocation.class);
        Invocation mockInvocation2 = PowerMockito.mock(Invocation.class);
        Method expectedMethod = PowerMockito.method(Object.class, "testMethod", new Class[0]);
        PowerMockito.when(mockInvocation1.getMethod()).thenReturn(expectedMethod);
        PowerMockito.when(mockInvocation2.getMethod()).thenReturn(expectedMethod);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);

        boolean actualResult = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        boolean expectedResult = false;
        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation mockInvocation = PowerMockito.mock(Invocation.class);
        Object[] mockArguments = new Object[]{1, 2, 3};
        Matcher mockMatcher = mock(Matcher.class);
        when(mockMatcher.matches(anyInt())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        invocationMatcher.captureArgumentsFrom(mockInvocation);

        Object[] actualArguments = new Object[matcherList.size()];
        for (int i = 0; i < matcherList.size(); i++) {
            Matcher matcher = matcherList.get(i);
            if (matcher instanceof CapturesArguments) {
                ((CapturesArguments) matcher).getCapturedArgument(0, actualArguments[i]);
            }
        }

        Object capturedArgument = actualArguments[0];
        int expectedArgument = 1;
        assertThat(capturedArgument, is(expectedArgument));
    }
}