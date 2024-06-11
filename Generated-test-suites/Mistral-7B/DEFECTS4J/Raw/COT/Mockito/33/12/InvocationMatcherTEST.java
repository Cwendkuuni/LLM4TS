import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_WithMatchers_ShouldReturnCorrectMatcher() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments("parameter1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
        Assert.assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testInvocationMatcher_WithoutMatchers_ShouldReturnInvocationWithEmptyMatcherList() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertTrue(invocationMatcher.getMatchers().isEmpty());
        Assert.assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationMatcher_WithNullInvocation_ShouldThrowException() {
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments("parameter1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(null, matchers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationMatcher_WithNullMatchers_ShouldThrowException() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
    }

    @Test
    public void testToString_ShouldReturnCorrectStringRepresentation() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments("parameter1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = "InvocationMatcher[this = Invocation[this = [Ljava.lang.Object;@1b6d33d6, method = Method(name = testMethod signature = ()V)]," +
                " matchers = [CapturesArguments[this = CapturesArguments@689a7cec, argument = 0]]]";

        Assert.assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches_ShouldReturnTrueIfBothInvocationsAreEquals() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Invocation invocation2 = invocation1;
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_ShouldReturnFalseIfBothInvocationsAreDifferent() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod1", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod2", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_ShouldReturnFalseIfMockObjectsAreDifferent() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Object mockObject1 = new Object();
        Object mockObject2 = new Object();
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, mockObject1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, mockObject2);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_ShouldReturnFalseIfMethodsAreDifferent() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod1", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod2", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_ShouldReturnTrueIfBothInvocationsAreSimilar() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_ShouldReturnFalseIfBothInvocationsAreDifferent() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod1", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod2", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch_ShouldReturnTrueIfArgumentsAreEquals() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Object[] arguments = new Object[0];
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, arguments);

        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testSafelyArgumentsMatch_ShouldReturnFalseIfArgumentsAreDifferent() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Object[] arguments1 = new Object[]{"argument1"};
        Object[] arguments2 = new Object[]{"argument2"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, arguments1);

        Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatch_ShouldThrowExceptionIfArgumentsComparisonFails() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Object[] arguments = new Object[]{new Throwable()};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, arguments);

        invocationMatcher.safelyArgumentsMatch(arguments);
    }

    @Test
    public void testCreateFrom_ShouldReturnListOfCorrectInvocationMatchers() {
        List<Invocation> invocations = new LinkedList<Invocation>() {{
            add(new Invocation(new Object() {}, new Method("testMethod1", new Class[0])));
            add(new Invocation(new Object() {}, new Method("testMethod2", new Class[0])));
        }};

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(2, invocationMatchers.size());
    }
}