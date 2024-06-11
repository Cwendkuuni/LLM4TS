import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "testMethod");
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatcherList() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[]{Integer.class}));
        List<Matcher> matchers = Collections.singletonList(new org.hamcrest.Matcher<Integer>() {
            @Override
            public boolean matches(Object item) {
                return item.equals(1);
            }
        });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "testMethod");
        List<Matcher> matchersList = invocationMatcher.getMatchers();
        Assert.assertEquals(matchersList.size(), 1);
        Assert.assertTrue(matchersList.get(0) instanceof org.hamcrest.Matcher);
    }

    @Test
    public void testInvocationMatcherMatches() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));

        InvocationMatcher testInvocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        Assert.assertTrue("Mockitos should be the same", testInvocationMatcher.matches(invocationMatcher));
        Assert.assertTrue("Same method", testInvocationMatcher.matches(invocationMatcher));

        Location location = new Location("TestClass.java", 10);
        invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList(), location);
        actualInvocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]), location);
        Assert.assertTrue("Same method and location", testInvocationMatcher.matches(invocationMatcher));
        Assert.assertTrue("Same method and location", testInvocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testInvocationMatcherDoesNotMatch() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = new Invocation(new Object() {}, new Method("testOtherMethod", new Class[0]));

        Location location = new Location("TestClass.java", 10);
        invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList(), location);
        Assert.assertFalse("Different methods", invocationMatcher.matches(actualInvocation));

        InvocationMatcher testInvocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        Assert.assertFalse("Different mocks", testInvocationMatcher.matches(new InvocationMatcher(new Invocation(new Object() {}, new Method("testMethod", new Class[0]), new Location("TestOtherClass.java", 20)))));
    }

    @Test(expected = RuntimeException.class)
    public void testArgumentsDontMatch() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[]{Integer.class}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(new org.hamcrest.Matcher<Integer>() {
            @Override
            public boolean matches(Object item) {
                return item.equals(2);
            }
        }));
        Object[] arguments = new Object[]{1};

        invocationMatcher.safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatchero(InvocationMatcher.class, invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue("Same method", invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodDifferentMethods() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod1", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod2", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse("Different methods", invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodSameMethods() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue("Same methods", invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodDifferentMethods() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod1", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod2", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse("Different methods", invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodSameMethodsWithDifferentArguments() {
        Invocation invocation1 = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object() {}, new Method("testMethod", new Class[]{Integer.class}));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse("Different methods with same name but different arguments", invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String toString = invocationMatcher.toString();

        Assert.assertNotNull(toString);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        Invocation invocation = new Invocation(new Object() {}, new Method("testMethod", new Class[0]));
        invocations.add(invocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher invocationMatcher = invocationMatchers.get(0);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(method.getName(), "testMethod");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFromEmptyList() {
        InvocationMatcher.createFrom(Collections.emptyList());
    }
}