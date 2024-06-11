import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InternalMockUtility;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertSame(invocation.getMock(), invocationMatcher.getInvocation().getMock());
        Assert.assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(Integer.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertSame(invocation.getMock(), invocationMatcher.getInvocation().getMock());
        Assert.assertEquals(2, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method returnedMethod = invocationMatcher.getMethod();
        Assert.assertSame(method, returnedMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation returnedInvocation = invocationMatcher.getInvocation();
        Assert.assertSame(invocation, returnedInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(Integer.class));
        Mockito.when(invocation.argumentsToMatchers()).thenReturn(matchers);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> returnedMatchers = invocationMatcher.getMatchers();
        Assert.assertSame(matchers, returnedMatchers);
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(Integer.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String toString = invocationMatcher.toString();
        Assert.assertNotNull(toString);

        InternalMockUtility.verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);
        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean matches = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertTrue(matches);

        InternalMockUtility.verifyNoMoreInteractions(invocation1);
        InternalMockUtility.verifyNoMoreInteractions(invocation2);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] actualArgs = new Object[0];

        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(Mockito.any(InvocationMatcher.class), Mockito.any(Object[].class))).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean safelyArgumentsMatch = invocationMatcher.safelyArgumentsMatch(actualArgs);
        Assert.assertTrue(safelyArgumentsMatch);

        InternalMockUtility.verifyNoMoreInteractions(invocation);
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithException() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] actualArgs = new Object[0];

        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(Mockito.any(InvocationMatcher.class), Mockito.any(Object[].class))).thenThrow(new RuntimeException());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.safelyArgumentsMatch(actualArgs);

        InternalMockUtility.verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);
        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertTrue(hasSimilarMethod);

        InternalMockUtility.verifyNoMoreInteractions(invocation1);
        InternalMockUtility.verifyNoMoreInteractions(invocation2);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertFalse(hasSimilarMethod);

        InternalMockito.verifyNoMoreInteractions(invocation1);
        InternalMockito.verifyNoMoreInteractions(invocation2);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);
        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean hasSameMethod = invocationMatcher1.hasSameMethod(invocationMatcher2);
        Assert.assertTrue(hasSameMethod);

        InternalMockito.verifyNoMoreInteractions(invocation1);
        InternalMockito.verifyNoMoreInteractions(invocation2);
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean hasSameMethod = invocationMatcher1.hasSameMethod(invocationMatcher2);
        Assert.assertFalse(hasSameMethod);

        InternalMockito.verifyNoMoreInteractions(invocation1);
        InternalMockito.verifyNoMoreInteractions(invocation2);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Location location = Mockito.mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location returnedLocation = invocationMatcher.getLocation();
        Assert.assertSame(location, returnedLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(Integer.class));
        PrintSettings printSettings = Mockito.mock(PrintSettings.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String toString = invocationMatcher.toString(printSettings);
        Assert.assertNotNull(toString);

        InternalMockito.verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new CapturesArgumentsTestMatcher()));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        InternalMockito.verify(matchers.get(0), Mockito.times(1)).captureFrom(Mockito.anyArray());
        InternalMockito.verifyNoMoreInteractions(invocation);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<InvocationMatcher> invocationMatchers = new LinkedList<>();

        invocationMatchers.add(new InvocationMatcher(invocation1));
        invocationMatchers.add(new InvocationMatcher(invocation2));

        List<InvocationMatcher> returnedInvocationMatchers = InvocationMatcher.createFrom(invocationMatchers);

        Assert.assertEquals(2, returnedInvocationMatchers.size());

        InternalMockito.verifyNoMoreInteractions(invocation1);
        InternalMockito.verifyNoMoreInteractions(invocation2);
    }

    private static class CapturesArgumentsTestMatcher implements ArgumentMatcher<Object[]> {
        @Override
        public boolean matches(Object argument) {
            return argument instanceof Object[];
        }
    }
}