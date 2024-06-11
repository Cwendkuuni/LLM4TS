import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertEquals(invocation.getMethod().getName(), method.getName());
        Assert.assertEquals(invocation.getMock(), invocationMatcher.getInvocation().getMock());
        Assert.assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = createMockMatchers();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocationMatcher.getMethod();
        Assert.assertNotNull(method);
        Assert.assertEquals(invocation.getMethod().getName(), method.getName());
        Assert.assertEquals(invocation.getMock(), invocationMatcher.getInvocation().getMock());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testMatchesSameInvocation() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation sameInvocation = createMockInvocation();
        Assert.assertTrue(invocationMatcher.matches(sameInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationSameMethod() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation differentInvocation = createMockInvocationWithDifferentArguments(invocation);
        Assert.assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testMatchesDifferentInvocationDifferentMethod() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation differentInvocation = createMockInvocationWithDifferentMethod();
        Assert.assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargInvocation() {
        Invocation invocation = createMockVarargInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testHasSimilarMethodSameMethodName() {
        Invocation invocation = createMockInvocation();
        Invocation candidate = createMockInvocationWithSameMethodName();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodDifferentMethodName() {
        Invocation invocation = createMockInvocation();
        Invocation candidate = createMockInvocationWithDifferentMethodName();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethodSameMethod() {
        Invocation invocation = createMockInvocation();
        Invocation candidate = new Invocation(invocation.getMock(), invocation.getMethod(), new Object[0], createLocation(), true, true, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodDifferentMethods() {
        Invocation invocation = createMockInvocation();
        Invocation candidate = createMockInvocationWithDifferentMethod();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testToString() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, createMockMatchers());
        String toString = invocationMatcher.toString();
        Assert.assertNotNull(toString);
    }

    private Invocation createMockInvocation() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.spy(Object.class).getMethod("testMethod", Integer.class, String[].class);
        return new Invocation(mock, method, new Object[]{1, new String[]{"test"}}, createLocation(), true, true, null);
    }

    private Invocation createMockInvocationWithDifferentArguments(Invocation invocation) {
        Object mock = Mockito.mock(Object.class);
        Method method = invocation.getMethod();
        return new Invocation(mock, method, new Object[]{2, new String[]{"test2"}}, createLocation(), true, true, null);
    }

    private Invocation createMockInvocationWithDifferentMethod() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.spy(Object.class).getMethod("testOtherMethod", Float.class);
        return new Invocation(mock, method, new Object[0], createLocation(), true, true, null);
    }

    private Invocation createMockInvocationWithSameMethodName() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.spy(Object.class).getMethod("testMethod", Integer.class, String[].class);
        return new Invocation(mock, method, new Object[]{1, new String[]{"test"}}, createLocation(), true, true, null);
    }

    private Location createLocation() {
        return new Location("TestClass.java", 1, "TestClass.java:1");
    }

    private List<Matcher> createMockMatchers() {
        Matcher matcher = Mockito.mock(Matcher.class);
        return Collections.singletonList(matcher);
    }
}