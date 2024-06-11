package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation mockInvocation;

    @Before
    public void setUp() {
        mockInvocation = invocation(Mockito.mock(Object.class), new Object[]{"test"}, new Object[]{1, 2});
        invocationMatcher = new InvocationMatcher(mockInvocation);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = mockInvocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();

        assertThat(actualMethod, is(expectedMethod));
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = mockInvocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertThat(actualInvocation, is(expectedInvocation));
    }

    @Test
    public void testGetMatchers_emptyList() {
        InvocationMatcher invocationMatcherWithEmptyList = new InvocationMatcher(mockInvocation, Collections.emptyList());
        List<Matcher> expectedMatchers = Collections.singletonList(ArgumentMatchers.any());
        List<Matcher> actualMatchers = invocationMatcherWithEmptyList.getMatchers();

        assertThat(actualMatchers, is(expectedMatchers));
    }

    @Test
    public void testGetMatchers_customList() {
        List<Matcher> customMatchers = Arrays.asList(ArgumentMatchers.eq("test1"), ArgumentMatchers.eq("test2"));
        InvocationMatcher invocationMatcherWithCustomList = new InvocationMatcher(mockInvocation, customMatchers);
        List<Matcher> expectedMatchers = customMatchers;
        List<Matcher> actualMatchers = invocationMatcherWithCustomList.getMatchers();

        assertThat(actualMatchers, is(expectedMatchers));
    }

    @Test
    public void testToString() {
        String expectedString = "InvocationMatcher [method=public java.lang.String org.mockito.internal.InvocationTest.testMethod(java.lang.String, int)];";
        String actualString = invocationMatcher.toString();

        assertThat(actualString, is(expectedString));
    }

    @Test
    public void testMatches() {
        Invocation mockInvocationToCompare = invocation(Mockito.mock(Object.class), "test", 1);
        boolean expectedResult = true;
        boolean actualResult = invocationMatcher.matches(mockInvocationToCompare);

        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testSafelyArgumentsMatch_sameArguments() {
        Object[] argumentsToCompare = new Object[]{1, 2};
        boolean expectedResult = true;
        boolean actualResult = invocationMatcher.safelyArgumentsMatch(argumentsToCompare);

        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testSafelyArgumentsMatch_differentArguments() {
        Object[] argumentsToCompare = new Object[]{1, 2, 3};
        boolean expectedResult = false;
        boolean actualResult = invocationMatcher.safelyArgumentsMatch(argumentsToCompare);

        assertThat(actualResult, is(expectedResult));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_nonVarargInvocation() {
        invocationMatcher.captureArgumentsFrom(mockInvocation);
    }

    @Test
    public void testCaptureArgumentsFrom_varargInvocation() {
        Invocation varargInvocation = invocation(Mockito.mock(Object.class), new Object[]{"testArray"}, new Object[]{new Object[]{"test1", "test2"}});
        InvocationMatcher invocationMatcherWithVarargInvocation = new InvocationMatcher(varargInvocation);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(ArgumentMatchers.contains("test1"));
        matchers.add(ArgumentMatchers.contains("test2"));
        invocationMatcherWithVarargInvocation.captureArgumentsFrom(varargInvocation);

        for (int i = 0; i < matchers.size(); i++) {
            Matcher m = matchers.get(i);
            if (m instanceof CapturesArguments) {
                Object capturedArgument = ((CapturesArguments) m).getCapturedValue();
                String expectedArgument = i < varargInvocation.getRawArguments().length ? ((Object[]) varargInvocation.getRawArguments())[i].toString() : null;
                assertThat(capturedArgument, is(expectedArgument));
            }
        }
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidateInvocation = invocation(Mockito.mock(Object.class), "test", 1);
        boolean expectedResult = true;
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidateInvocation);

        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testHasSimilarMethod_differentMethods() {
        Invocation candidateInvocation = invocation(Mockito.mock(Object.class), "test2", 1);
        boolean expectedResult = false;
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidateInvocation);

        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidateInvocation = invocation(Mockito.mock(Object.class), "test", new Object[]{1, 2});
        boolean expectedResult = true;
        boolean actualResult = invocationMatcher.hasSameMethod(candidateInvocation);

        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testHasSameMethod_differentMethods() {
        Invocation candidateInvocation = invocation(Mockito.mock(Object.class), "test2", new Object[]{1, 2, 3});
        boolean expectedResult = false;
        boolean actualResult = invocationMatcher.hasSameMethod(candidateInvocation);

        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(mockInvocation, invocation(Mockito.mock(Object.class), "test", 1));
        List<InvocationMatcher> expectedInvocationMatchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher actualInvocationMatcher = expectedInvocationMatchers.get(0);

        assertThat(actualInvocationMatcher.getMethod(), is(invocationMatcher.getMethod()));
        assertThat(actualInvocationMatcher.getMatchers(), is(invocationMatcher.getMatchers()));

        InvocationMatcher secondInvocationMatcher = expectedInvocationMatchers.get(1);
        assertThat(secondInvocationMatcher.getMethod(), is(invocation(Mockito.mock(Object.class), "test", 1).getMethod()));
        assertThat(secondInvocationMatcher.getMatchers(), is(Collections.singletonList(ArgumentMatchers.any())));
    }
}