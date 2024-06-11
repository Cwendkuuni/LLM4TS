import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyList() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        List<Matcher> emptyList = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, emptyList);

        Method method = Mockito.mock(Method.class);
        Assert.assertEquals("Expected invocationMatcher's method to be equal to mocked method", method, invocationMatcher.getMethod());
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndListOfMatchers() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        List<Matcher> listOfMatchers = Collections.singletonList(ArgumentMatchers.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, listOfMatchers);

        Method method = Mockito.mock(Method.class);
        Assert.assertEquals("Expected invocationMatcher's method to be equal to mocked method", method, invocationMatcher.getMethod());
    }

    @Test
    public void testInvocationMatcherMatches() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        List<Matcher> listOfMatchers = Collections.singletonList(ArgumentMatchers.eq("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, listOfMatchers);
        Invocation invocationToMatch = Mockito.mock(Invocation.class);

        Method wantedMethod = Mockito.mock(Method.class);
        Method currentMethod = Mockito.mock(Method.class);
        Mockito.when(invocationToMatch.getMethod()).thenReturn(currentMethod);
        Mockito.when(mockInvocation.getMethod()).thenReturn(wantedMethod);
        Mockito.when(wantedMethod.getName()).thenReturn("testMethod");
        Mockito.when(currentMethod.getName()).thenReturn("testMethod");

        Mockito.when(wantedMethod.equals(currentMethod)).thenReturn(true);
        Mockito.when(mockInvocation.getMock()).thenReturn(invocationToMatch.getMock());

        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        Mockito.when(argumentsComparator.argumentsMatch(invocationMatcher, invocationToMatch.getArguments())).thenReturn(true);

        Assert.assertTrue("Expected invocationMatcher to match", invocationMatcher.matches(invocationToMatch));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMethods() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        List<Matcher> listOfMatchers = Collections.singletonList(ArgumentMatchers.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, listOfMatchers);
        Invocation invocationToMatch = Mockito.mock(Invocation.class);

        Method wantedMethod = Mockito.mock(Method.class);
        Method currentMethod = Mockito.mock(Method.class);
        Mockito.when(invocationToMatch.getMethod()).thenReturn(currentMethod);
        Mockito.when(mockInvocation.getMethod()).thenReturn(wantedMethod);
        Mockito.when(wantedMethod.getName()).thenReturn("testMethod1");
        Mockito.when(currentMethod.getName()).thenReturn("testMethod2");
        Mockito.when(wantedMethod.equals(currentMethod)).thenReturn(false);

        Assert.assertFalse("Expected invocationMatcher not to match", invocationMatcher.matches(invocationToMatch));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMocks() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        List<Matcher> listOfMatchers = Collections.singletonList(ArgumentMatchers.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, listOfMatchers);
        Invocation invocationToMatch = Mockito.mock(Invocation.class);

        Method wantedMethod = Mockito.mock(Method.class);
        Method currentMethod = Mockito.mock(Method.class);
        Mockito.when(invocationToMatch.getMethod()).thenReturn(wantedMethod);
        Mockito.when(mockInvocation.getMethod()).thenReturn(wantedMethod);
        Mockito.when(wantedMethod.getName()).thenReturn("testMethod");
        Mockito.when(mockInvocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocationToMatch.getMock()).thenReturn(Mockito.mock(Object.class, "differentMock"));

        Assert.assertFalse("Expected invocationMatcher not to match", invocationMatcher.matches(invocationToMatch));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchOverloadedMethodsWithDifferentArguments() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        List<Matcher> listOfMatchers = Collections.singletonList(ArgumentMatchers.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, listOfMatchers);
        Invocation invocationToMatch = Mockito.mock(Invocation.class);

        Method wantedMethod = Mockito.mock(Method.class);
        Method currentMethod = Mockito.mock(Method.class);
        Mockito.when(invocationToMatch.getMethod()).thenReturn(currentMethod);
        Mockito.when(mockInvocation.getMethod()).thenReturn(wantedMethod);
        Mockito.when(wantedMethod.getName()).thenReturn("testMethod");
        Mockito.when(currentMethod.getName()).thenReturn("testMethod");
        Mockito.when(wantedMethod.equals(currentMethod)).thenReturn(false);
        Mockito.when(safelyArgumentsMatch(invocationMatcher, invocationToMatch.getArguments())).thenReturn(false);

        Assert.assertFalse("Expected invocationMatcher not to match", invocationMatcher.matches(invocationToMatch));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Collections.emptyList());
        Object[] actualArgs = new Object[0];
        invocationMatcher.safelyArgumentsMatch(actualArgs);
    }

    @Test
    public void testHasSimilarMethodReturnsFalseForDifferentMethods() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        List<Matcher> listOfMatchers = Collections.singletonList(ArgumentMatchers.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, listOfMatchers);
        Invocation invocationToMatch = Mockito.mock(Invocation.class);

        Method wantedMethod = Mockito.mock(Method.class);
        Method currentMethod = Mockito.mock(Method.class);
        Mockito.when(invocationToMatch.getMethod()).thenReturn(currentMethod);
        Mockito.when(mockInvocation.getMethod()).thenReturn(wantedMethod);
        Mockito.when(wantedMethod.getName()).thenReturn("testMethod1");
        Mockito.when(currentMethod.getName()).thenReturn("testMethod2");

        Assert.assertFalse("Expected hasSimilarMethod to return false for different methods", invocationMatcher.hasSimilarMethod(invocationToMatch));
    }

    @Test
    public void testHasSimilarMethodReturnsTrueForTheSameMethod() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        List<Matcher> listOfMatchers = Collections.singletonList(ArgumentMatchers.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, listOfMatchers);
        Invocation invocationToMatch = Mockito.mock(Invocation.class);

        Method wantedMethod = Mockito.mock(Method.class);
        Method currentMethod = Mockito.mock(Method.class);
        Mockito.when(invocationToMatch.getMethod()).thenReturn(currentMethod);
        Mockito.when(mockInvocation.getMethod()).thenReturn(wantedMethod);
        Mockito.when(wantedMethod.getName()).thenReturn("testMethod");
        Mockito.when(currentMethod.getName()).thenReturn("testMethod");
        Mockito.when(wantedMethod.equals(currentMethod)).thenReturn(true);

        Assert.assertTrue("Expected hasSimilarMethod to return true for the same method", invocationMatcher.hasSimilarMethod(invocationToMatch));
    }

    private Object[] createArgumentsArray(Object... objects) {
        return objects;
    }

    private boolean safelyArgumentsMatch(InvocationMatcher invocationMatcher, Object[] actualArgs) {
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        return argumentsComparator.argumentsMatch(invocationMatcher, actualArgs);
    }
}