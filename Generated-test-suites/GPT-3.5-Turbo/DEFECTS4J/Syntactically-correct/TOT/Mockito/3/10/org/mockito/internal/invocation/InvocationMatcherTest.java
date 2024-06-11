package org.mockito.internal.invocation;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    // Test cases for constructor InvocationMatcher(Invocation invocation, List<Matcher> matchers)

    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Assert
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertTrue(invocationMatcher.getMatchers().isEmpty());
    }


    // Test cases for method getMethod()

    @Test
    public void testGetMethod() {
        // Arrange
        Method expectedMethod = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Method actualMethod = invocationMatcher.getMethod();

        // Assert
        Assert.assertEquals(expectedMethod, actualMethod);
    }


    // Test cases for method getInvocation()

    @Test
    public void testGetInvocation() {
        // Arrange
        Invocation expectedInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(expectedInvocation);

        // Act
        Invocation actualInvocation = invocationMatcher.getInvocation();

        // Assert
        Assert.assertEquals(expectedInvocation, actualInvocation);
    }


    // Test cases for method getMatchers()

    @Test
    public void testGetMatchersWithMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        List<Matcher> expectedMatchers = new ArrayList<>();
        expectedMatchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, expectedMatchers);

        // Act
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        // Assert
        Assert.assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testGetMatchersWithoutMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        List<Matcher> matchers = invocationMatcher.getMatchers();

        // Assert
        Assert.assertTrue(matchers.isEmpty());
    }


    // Test cases for method toString()

    @Test
    public void testToString() {
        // Arrange
        PrintSettings printSettings = mock(PrintSettings.class);
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        when(invocationMatcher.toString()).thenCallRealMethod();
        when(new PrintSettings().print(matchers, invocation)).thenReturn("expected");

        // Act
        String actualString = invocationMatcher.toString();

        // Assert
        Assert.assertEquals("expected", actualString);
    }


    // Test cases for method matches(Invocation actual)

    @Test
    public void testMatchesWithEqualInvocations() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(new InvocationMatcher(invocation), invocation)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.matches(invocation);

        // Assert
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testMatchesWithDifferentMock() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn("mock2");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.matches(actualInvocation);

        // Assert
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.matches(actualInvocation);

        // Assert
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(new ArgumentsComparator().argumentsMatch(new InvocationMatcher(invocation), null)).thenReturn(false);
        Invocation actualInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.matches(actualInvocation);

        // Assert
        Assert.assertFalse(actualResult);
    }


    // Test cases for method hasSimilarMethod(Invocation candidate)

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethodWithVerifiedCandidate() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(candidate.getMock()).thenReturn("mock2");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButOverloadedArgs() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(new InvocationMatcher(invocation), candidate.getArguments())).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        Assert.assertFalse(actualResult);
    }


    // Test cases for method hasSameMethod(Invocation candidate)

    @Test
    public void testHasSameMethodWithEqualMethods() {
        // Arrange
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.hasSameMethod(candidate);

        // Assert
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.hasSameMethod(candidate);

        // Assert
        Assert.assertFalse(actualResult);
    }


    // Test cases for method getLocation()

    @Test
    public void testGetLocation() {
        // Arrange
        Location expectedLocation = mock(Location.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Location actualLocation = invocationMatcher.getLocation();

        // Assert
        Assert.assertEquals(expectedLocation, actualLocation);
    }


    // Test cases for method captureArgumentsFrom(Invocation invocation)

    @Test
    public void testCaptureArgumentsFromWithVarargsMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[]{1, 2, 3});
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Matcher matcher3 = mock(Matcher.class);
        Matcher matcher4 = mock(Matcher.class);
        Matcher matcher5 = mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation,
                Arrays.asList(matcher1, matcher2, matcher3, matcher4, matcher5));
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);
        when(matcher3 instanceof CapturesArguments).thenReturn(true);
        when(matcher4 instanceof CapturesArguments).thenReturn(true);
        when(matcher5 instanceof CapturesArguments).thenReturn(true);

        // Act
        invocationMatcher.captureArgumentsFrom(invocation);

        // Assert
        Assert.assertEquals(1, ((CapturesArguments) matcher1).getCapturedArguments().get(0));
        Assert.assertEquals(2, ((CapturesArguments) matcher2).getCapturedArguments().get(0));
        Assert.assertEquals(3, ((CapturesArguments) matcher3).getCapturedArguments().get(0));
        Assert.assertEquals(2, ((CapturesArguments) matcher4).getCapturedArguments().get(0));
        Assert.assertEquals(3, ((CapturesArguments) matcher5).getCapturedArguments().get(0));
    }

    @Test
    public void testCaptureArgumentsFromWithNonVarargsMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(1);
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(2);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation,
                Arrays.asList(matcher1, matcher2));
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);

        // Act
        invocationMatcher.captureArgumentsFrom(invocation);

        // Assert
        Assert.assertEquals(1, ((CapturesArguments) matcher1).getCapturedArguments().get(0));
        Assert.assertEquals(2, ((CapturesArguments) matcher2).getCapturedArguments().get(0));
    }


    // Test cases for static method createFrom(List<Invocation> invocations)

    @Test
    public void testCreateFrom() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        // Act
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Assert
        Assert.assertEquals(2, invocationMatchers.size());
        Assert.assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        Assert.assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

}