import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcherImpl;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.util.ArrayList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new Invocation(new Location(1, 1), "methodName", new Object[0], new Class[0], new Class[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        Assert.assertEquals(method.getName(), invocationMatcher.getMethod().getName());
        Assert.assertEquals(invocation.getMock(), invocationMatcher.getInvocation().getMock());
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Object[] arguments = new Object[2];
        Matcher matcher1 = org.mockito.Mockito.anyInt();
        Matcher matcher2 = org.mockito.Mockito.any(String.class);

        Invocation invocation = new Invocation(new Location(1, 1), "methodName", arguments, new Class[0], new Class[2]);

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocation.getMethod();
        Assert.assertEquals(method.getName(), invocationMatcher.getMethod().getName());
        Assert.assertEquals(invocation.getMock(), invocationMatcher.getInvocation().getMock());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testMatches() {
        Object[] actualArguments = new Object[2];
        Invocation actualInvocation = new Invocation(new Location(1, 1), "methodName", actualArguments, new Class[0], new Class[2]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(new Location(1, 1), "methodName", new Object[0], new Class[0], new Class[0]));
        Method method = invocationMatcher.getMethod();

        // matches should return true if invocations have the same method name, same mock, and unverified
        Assert.assertTrue(invocationMatcher.matches(actualInvocation));

        // matches should return false if invocations have different method names, or verified
        Invocation verifiedInvocation = new Invocation(new Location(1, 1), "methodName", actualArguments, new Class[0], new Class[2], true);
        Assert.assertFalse(invocationMatcher.matches(verifiedInvocation));

        // matches should return false if invocations have the same method name, same mock, but different methods
        Invocation differentMethodInvocation = new Invocation(new Location(1, 1), "differentMethodName", actualArguments, new Class[0], new Class[2]);
        Assert.assertFalse(invocationMatcher.matches(differentMethodInvocation));

        // matches should return false if invocations have the same method name, same mock, and arguments do not match
        actualArguments[0] = 1;
        Assert.assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testSafelyArgumentsMatch() throws Throwable {
        Object[] actualArguments = new Object[2];
        Invocation actualInvocation = new Invocation(new Location(1, 1), "methodName", actualArguments, new Class[0], new Class[2]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(new Location(1, 1), "methodName", new Object[0], new Class[0], new Class[0]));

        // safelyArgumentsMatch should return true if arguments match
        actualArguments[0] = 1;
        actualArguments[1] = "test";
        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(actualArguments));

        // safelyArgumentsMatch should return false if arguments do not match
        actualArguments[1] = "differentTest";
        Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(actualArguments));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() throws Throwable {
        Object[] actualArguments = new Object[2];
        Invocation actualInvocation = new Invocation(new Location(1, 1), "methodName", actualArguments, new Class[0], new Class[2]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(new Location(1, 1), "methodName", new Object[0], new Class[0], new Class[0]));

        // safelyArgumentsMatch should throw an exception if an exception is thrown during the comparison
        actualArguments[0] = new RuntimeException("test");
        invocationMatcher.safelyArgumentsMatch(actualArguments);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(new Location(1, 1), "methodName1", new Object[0], new Class[0], new Class[0]);
        Invocation invocation2 = new Invocation(new Location(1, 1), "methodName2", new Object[0], new Class[0], new Class[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // hasSimilarMethod should return false if method names are different
        Assert.assertFalse(invocationMatcher.hasSimilarMethod(invocation2));

        // hasSimilarMethod should return true if methods are the same
        invocation2 = new Invocation(new Location(1, 1), "methodName1", new Object[0], invocation1.getMethod().getDeclaringClass(), new Class[0]);
        Assert.assertTrue(invocationMatcher.hasSimilarMethod(invocation2));

        // hasSimilarMethod should return false if arguments are different and methods are not overloaded
        Object[] arguments1 = new Object[1];
        arguments1[0] = "test1";
        Invocation invocation3 = new Invocation(new Location(1, 1), "methodName1", arguments1, new Class[0], new Class[0]);
        Assert.assertFalse(invocationMatcher.hasSimilarMethod(invocation3));

        // hasSimilarMethod should return true if arguments are the same and methods are overloaded
        Object[] arguments2 = new Object[1];
        arguments2[0] = "test2";
        Invocation invocation4 = new Invocation(new Location(1, 1), "methodName1", arguments2, new Class[0], new Class[0], true);
        Assert.assertTrue(invocationMatcher.hasSimilarMethod(invocation4));
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Location(1, 1), "methodName", new Object[0], new Class[0], new Class[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expectedString = String.format("Invocation %s%n  with: %s", invocation.toString(), invocationMatcher.getMatchers().toString());
        String toString = invocationMatcher.toString();
        Assert.assertEquals(expectedString, toString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(new Location(1, 1), "methodName", new Object[2], new Class[0], new Class[2]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Matcher matcher1 = org.mockito.Mockito.argThat(new ArgumentsCaptor<Integer>().capture());
        Matcher matcher2 = org.mockito.Mockito.argThat(new ArgumentsCaptor<String>().capture());

        invocationMatcher.captureArgumentsFrom(invocation);

        Assert.assertEquals(invocation.getArguments()[0], matcher1.getValue());
        Assert.assertEquals(invocation.getArguments()[1], matcher2.getValue());

        List<Object> capturedArguments = new ArrayList<>();
        capturedArguments.add(1);
        capturedArguments.add("test");

        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        List<Matcher> matchers = argumentsComparator.compare(invocationMatcher, new InvocationMatcherImpl(invocation, capturedArguments));

        Assert.assertEquals(2, matchers.size());
        Assert.assertTrue(matchers.get(0) instanceof CapturesArguments);
        Assert.assertTrue(matchers.get(1) instanceof org.hamcrest.Matcher);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = new Invocation(new Location(1, 1), "methodName1", new Object[0], new Class[0], new Class[0]);
        Invocation invocation2 = new Invocation(new Location(1, 1), "methodName2", new Object[0], new Class[0], new Class[0]);
        invocations.add(invocation1);
        invocations.add(invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Assert.assertEquals(invocation1.getMethod().getName(), invocationMatcher1.getMethod().getName());
        Assert.assertEquals(invocation2.getMethod().getName(), invocationMatcher2.getMethod().getName());
        Assert.assertNotSame(invocationMatcher1, invocationMatcher2);
    }
}