import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyList() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());

        Invocation actualInvocation = new Invocation(method, new Object[0], new Location("TestClass.java", 2, 2), null);
        InvocationMatcher actualInvocationMatcher = new InvocationMatcher(actualInvocation);

        Assert.assertTrue(invocationMatcher.matches(actualInvocationMatcher));
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatchers() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[]{Integer.TYPE, String.class}), new Object[]{1, "test"}, new Location("TestClass.java", 1, 1), null);
        Matcher<Integer> integerMatcher = ArgumentsMatcher.isInstanceOf(Integer.TYPE);
        Matcher<String> stringMatcher = ArgumentsMatcher.isInstanceOf(String.class);
        List<Matcher> matchers = Arrays.asList(integerMatcher, stringMatcher);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());

        Object[] actualArguments = new Object[]{2, "test2"};
        Invocation actualInvocation = new Invocation(method, actualArguments, new Location("TestClass.java", 2, 2), null);

        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        Boolean expectedResult = argumentsComparator.argumentsMatch(invocationMatcher, actualInvocation.getArguments());
        Boolean actualResult = invocationMatcher.matches(new InvocationMatcher(actualInvocation, Collections.emptyList()));

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchThrowsExceptionWhenArgumentsDoesNotMatch() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1), null), Collections.emptyList());
        Object[] actualArguments = new Object[]{new Object()};

        new InvocationMatcher(invocationMatcher, actualArguments).matches(invocationMatcher);
    }

    @Test
    public void testHasSimilarMethodReturnsFalseWhenMethodsAreDifferent() {
        Invocation invocation1 = new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1), null);
        Invocation invocation2 = new Invocation(new Method("testMethod2", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1), null);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodReturnsTrueWhenMethodsAreTheSame() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1), null);
        Invocation invocation2 = new Invocation(invocation1.getMethod(), new Object[0], new Location("TestClass.java", 1, 1), null);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test(expected = AssertionError.class)
    public void testHasSimilarMethodThrowsExceptionWhenInvocationsAreDifferent() {
        Invocation invocation1 = new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1), null);
        Invocation invocation2 = new Invocation(new Method("testMethod2", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1), null);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        invocationMatcher1.hasSimilarMethod(invocationMatcher2);
    }

    @Test
    public void testToStringReturnsCorrectStringRepresentation() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1, 1), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        String expectedStringRepresentation = "InvocationMatcher{invocation=MockitoInvocationInterceptor$1@7f77d724, matchers=[]}";
        String actualStringRepresentation = invocationMatcher.toString();

        Assert.assertEquals(expectedStringRepresentation, actualStringRepresentation);
    }
}