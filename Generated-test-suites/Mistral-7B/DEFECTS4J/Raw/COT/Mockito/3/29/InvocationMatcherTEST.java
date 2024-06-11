import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.invocation.DescribedInvocation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new MockitoTestUtils.MockInvocation("testMethod", "testClass", new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());
        Assert.assertNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Object[] arguments = new Object[]{new Integer(1), new Integer(2)};
        Invocation invocation = new MockitoTestUtils.MockInvocation("testMethod", "testClass", arguments, new Location("TestClass.java", 1));
        List<Matcher> matchers = Arrays.asList(new Equals(new Integer(1)), new Equals(new Integer(2)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals("testMethod", method.getName());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testMatchesSameInvocation() {
        Object[] arguments = new Object[]{new Integer(1), new Integer(2)};
        Invocation invocation = new MockitoTestUtils.MockInvocation("testMethod", "testClass", arguments, new Location("TestClass.java", 1));
        List<Matcher> matchers = Arrays.asList(new Equals(new Integer(1)), new Equals(new Integer(2)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation sameInvocation = new MockitoTestUtils.MockInvocation("testMethod", "testClass", arguments, new Location("TestClass.java", 1));
        InvocationMatcher sameInvocationMatcher = new InvocationMatcher(sameInvocation, matchers);

        Assert.assertTrue(invocationMatcher.matches(sameInvocationMatcher));
    }

    @Test
    public void testMatchesDifferentInvocation() {
        Object[] arguments1 = new Object[]{new Integer(1), new Integer(2)};
        Object[] arguments2 = new Object[]{new Integer(1), new Integer(3)};
        Invocation invocation1 = new MockitoTestUtils.MockInvocation("testMethod1", "testClass", arguments1, new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(new Equals(new Integer(1)), new Equals(new Integer(2))));

        Invocation invocation2 = new MockitoTestUtils.MockInvocation("testMethod2", "testClass", arguments2, new Location("TestClass.java", 2));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(new Equals(new Integer(1)), new Equals(new Integer(3))));

        Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = new MockitoTestUtils.TestMethodWithTwoArguments();
        Method method2 = new MockitoTestUtils.TestMethodWithTwoArguments();
        Invocation invocation1 = new MockitoTestUtils.MockInvocation("testMethod1", "testClass", new Object[0], new Location("TestClass.java", 1), method1);
        Invocation invocation2 = new MockitoTestUtils.MockInvocation("testMethod2", "testClass", new Object[0], new Location("TestClass.java", 2), method2);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasDifferentMethods() {
        Method method1 = new MockitoTestUtils.TestMethodWithTwoArguments();
        Method method2 = new MockitoTestUtils.TestMethodWithOneArgument();
        Invocation invocation1 = new MockitoTestUtils.MockInvocation("testMethod1", "testClass", new Object[0], new Location("TestClass.java", 1), method1);
        Invocation invocation2 = new MockitoTestUtils.MockInvocation("testMethod2", "testClass", new Object[0], new Location("TestClass.java", 2), method2);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFromVarargsInvocation() {
        Object[] arguments = new Object[]{new Integer(1), new Integer[]{new Integer(3), new Integer(4)}};
        Invocation invocation = new MockitoTestUtils.MockInvocation("testMethod", "testClass", arguments, new Location("TestClass.java", 1));
        List<Matcher> matchers = Arrays.asList(new IsInstanceOf(Integer.class), new IsInstanceOf(Integer[].class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        Matcher argMatcher1 = matchers.get(0);
        Assert.assertTrue(argMatcher1.matches(invocation.getArguments()[0]));

        Matcher argMatcher2 = matchers.get(1);
        MockitoTestUtils.AssertCaptured(argMatcher2, new Integer[]{new Integer(3), new Integer(4)});
    }

    @Test(expected = RuntimeException.class)
    public void testCaptureArgumentsFromNonVarargsInvocation() {
        Object[] arguments = new Object[]{new Integer(1), new Integer(2)};
        Invocation invocation = new MockitoTestUtils.MockInvocation("testMethod", "testClass", arguments, new Location("TestClass.java", 1));
        List<Matcher> matchers = Arrays.asList(new IsInstanceOf(Integer.class), new IsInstanceOf(Integer[].class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        Matcher argMatcher1 = matchers.get(0);
        Assert.assertTrue(argMatcher1.matches(invocation.getArguments()[0]));

        Matcher argMatcher2 = matchers.get(1);
        try {
            argMatcher2.captureFrom(invocation.getArguments()[1]);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Test
    public void testCreateFromInvocations() {
        List<Invocation> invocations = Arrays.asList(new MockitoTestUtils.MockInvocation("testMethod1", "testClass1", new Object[0], new Location("TestClass1.java", 1)),
                new MockitoTestUtils.MockInvocation("testMethod2", "testClass2", new Object[]{new Integer(1)}, new Location("TestClass2.java", 2)),
                new MockitoTestUtils.MockInvocation("testMethod3", "testClass3", new Object[]{new Integer(1), new Integer(2)}, new Location("TestClass3.java", 3)));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        Invocation invocation1 = invocationMatcher1.getInvocation();
        Assert.assertEquals("testMethod1", invocation1.getMethod().getName());
        Assert.assertNull(invocationMatcher1.getMatchers());

        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        Invocation invocation2 = invocationMatcher2.getInvocation();
        Assert.assertEquals("testMethod2", invocation2.getMethod().getName());
        Matcher matcher1 = invocationMatcher2.getMatchers().get(0);
        Assert.assertEquals(new Integer(1), matcher1.getValue());
    }
}

class MockitoTestUtils {

    public static Invocation newMockInvocation(String methodName, String className, Object[] arguments, Location location) {
        return new MockInvocation(methodName, className, arguments, location, new MockitoTestUtils.TestMethodWithNoArguments());
    }

    public static Invocation newMockInvocation(String methodName, String className, Object[] arguments, Location location, Method method) {
        return new MockInvocation(methodName, className, arguments, location, method);
    }

    public static Invocation newMockInvocation(String methodName, String className, Object[] arguments, Location location, Class<?>[] parameterTypes) {
        try {
            Method method = Class.forName(className).getMethod(methodName, parameterTypes);
            return new MockInvocation(methodName, className, arguments, location, method);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static class MockInvocation implements Invocation, DescribedInvocation {
        private static final long serialVersionUID = 3232653441252558261L;
        private final String methodName;
        private final String className;
        private final Object[] arguments;
        private final Location location;
        private final Method method;

        public MockInvocation(String methodName, String className, Object[] arguments, Location location, Method method) {
            this.methodName = methodName;
            this.className = className;
            this.arguments = arguments;
            this.location = location;
            this.method = method;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getClassName() {
            return className;
        }

        public Object[] getArguments() {
            return arguments;
        }

        public Object getArgumentAt(int index, Class<?> type) {
            return arguments[index];
        }

        public Location getLocation() {
            return location;
        }

        public Method getMethod() {
            return method;
        }

        public Object[] getRawArguments() {
            if (method.isVarArgs()) {
                Object[] rawArguments = new Object[arguments.length + 1];
                System.arraycopy(arguments, 0, rawArguments, 0, arguments.length);
                return rawArguments;
            } else {
                return arguments;
            }
        }

        public String toString() {
            return "Invocation : " + methodName + "(" + Arrays.toString(arguments) + ")";
        }
    }

    public static class TestMethodWithNoArguments {
    }

    public static class TestMethodWithTwoArguments {
    }
}

class MockitoTestUtilsAssert {

    public static void AssertCaptured(Matcher matcher, Object expected) {
        Assert.assertEquals(expected, matcher.getValue());
    }
}