import org.junit.Assert;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImpl;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import java.lang.reflect.Method;
import java.util.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[0]), null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        List<Matcher> matchers = Arrays.asList(Mockito.eq(42));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertEquals(method.getName(), "method");
        Assert.assertEquals(method.getParameterTypes()[0].getTypeName(), "int");
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertSame(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        List<Matcher> matchers = Arrays.asList(Mockito.eq(42));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        List<Matcher> matchers = Arrays.asList(Mockito.eq(42));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String printSettings = new PrintSettings().withValuePrefixes(new String[]{"invocation:", "matchers:"}).withValueSuffixes(new String[]{}).withLineSeparator("\n");
        String expected = String.format("invocation:%s%nmatchers:%s%n", invocation.toString(), matchers.toString());
        String actual = new PrintSettings().print(invocationMatcher.getMatchers(), invocationMatcher);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMocks() {
        Invocation invocation1 = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        Invocation invocation2 = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        Mockito.mock(Object.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        invocationMatcher1.matches(invocationMatcher2);
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        Invocation invocation1 = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        Invocation invocation2 = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocation1 = new InvocationOnMockImpl(new Method(Object.class, "method1", new Class[]{String.class, Integer.TYPE}), null, new Object[]{"test", 42});
        Invocation invocation2 = new InvocationOnMockImpl(new Method(Object.class, "method2", new Class[]{Integer.TYPE}), null, new Object[]{42});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMethods() {
        Invocation invocation1 = new InvocationOnMockImpl(new Method(Object.class, "method1", new Class[]{String.class, Integer.TYPE}), null, new Object[]{"test", 42});
        Invocation invocation2 = new InvocationOnMockImpl(new Method(Object.class, "method2", new Class[]{Integer.TYPE}), null, new Object[]{42});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        invocationMatcher1.matches(invocationMatcher2);
    }

    @Test
    public void testSafelyArgumentsMatchWithSameArguments() {
        Invocation invocation = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] actualArgs = new Object[]{42};
        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchWithDifferentArguments() {
        Invocation invocation = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] actualArgs = new Object[]{43};
        invocationMatcher.safelyArgumentsMatch(actualArgs);
    }

    @Test
    public void testHasSimilarMethodWithSameMethods() {
        Invocation invocation1 = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        Invocation invocation2 = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodsButSameArguments() {
        Invocation invocation1 = new InvocationOnMockImpl(new Method(Object.class, "method1", new Class[]{String.class, Integer.TYPE}), null, new Object[]{"test", 42});
        Invocation invocation2 = new InvocationOnMockImpl(new Method(Object.class, "method2", new Class[]{Integer.TYPE}), null, new Object[]{42});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testCreateFromWithEmptyList() {
        List<Invocation> invocations = new ArrayList<>();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        Assert.assertTrue(invocationMatchers.isEmpty());
    }

    @Test
    public void testCreateFromWithOneInvocation() {
        Invocation invocation = new InvocationOnMockImpl(new Method(Object.class, "method", new Class[]{Integer.TYPE}), null, new Object[]{42});
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation));
        InvocationMatcher invocationMatcher = invocationMatchers.get(0);
        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(invocation);
        Assert.assertEquals(expectedInvocationMatcher, invocationMatcher);
    }

    @Test
    public void testCreateFromWithMultipleInvocations() {
        Invocation invocation1 = new InvocationOnMockImpl(new Method(Object.class, "method1", new Class[]{Integer.TYPE, String.class}), null, new Object[]{1, "test"});
        Invocation invocation2 = new InvocationOnMockImpl(new Method(Object.class, "method2", new Class[]{String.class, Integer.TYPE}), null, new Object[]{"test", 2});
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));
        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        InvocationMatcher expectedInvocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher expectedInvocationMatcher2 = new InvocationMatcher(invocation2);
        Assert.assertEquals(expectedInvocationMatcher1, invocationMatcher1);
        Assert.assertEquals(expectedInvocationMatcher2, invocationMatcher2);
    }
}