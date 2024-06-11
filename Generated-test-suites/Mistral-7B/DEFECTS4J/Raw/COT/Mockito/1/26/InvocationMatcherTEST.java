import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.DescribedInvocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Location;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = createMockInvocation();
        Matcher matcher1 = createArgumentMatcher(1);
        Matcher matcher2 = createArgumentMatcher(2);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = createMockInvocation();
        Method method = new MockMethod("testMethod", int.class, String.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method result = invocationMatcher.getMethod();
        Assert.assertSame(method, result);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation result = invocationMatcher.getInvocation();
        Assert.assertSame(invocation, result);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = createMockInvocation();
        Matcher matcher1 = createArgumentMatcher(1);
        Matcher matcher2 = createArgumentMatcher(2);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> result = invocationMatcher.getMatchers();
        Assert.assertSame(matchers, result);
    }

    @Test
    public void testToString() {
        Invocation invocation = createMockInvocation();
        Matcher matcher1 = createArgumentMatcher(1);
        Matcher matcher2 = createArgumentMatcher(2);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String result = invocationMatcher.toString();
        String expected = "InvocationMatcher [method = testMethod(int, String), invocation = Invocation [mocked object = MockObject@e02b24, method = testMethod(int, String), arguments = [1, mock], invocations = 0]]";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();
        Matcher matcher1 = createArgumentMatcher(1);
        Matcher matcher2 = createArgumentMatcher(2);
        List<Matcher> matchers1 = Arrays.asList(matcher1, matcher2);
        List<Matcher> matchers2 = Arrays.asList(matcher2, matcher1);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);
        Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
        Assert.assertFalse(invocationMatcher1.matches(new MockInvocation()));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = createMockInvocation();
        Object[] arguments = new Object[] { 1, 2, 3 };
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.safelyArgumentsMatch(arguments);
        Assert.assertTrue(result);
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.safelyArgumentsMatch(new Object[] {});
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();
        Method method1 = new MockMethod("testMethod", int.class);
        Method method2 = new MockMethod("testMethod", String.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
        Assert.assertFalse(invocationMatcher1.hasSimilarMethod(new MockInvocation()));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();
        Method method1 = new MockMethod("testMethod", int.class, int.class);
        Method method2 = new MockMethod("testMethod", int.class, int.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        Assert.assertFalse(invocationMatcher1.hasSameMethod(new MockInvocation()));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = createMockInvocation();
        Location location = new MockLocation("TestClass.java", 10);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location result = invocationMatcher.getLocation();
        Assert.assertSame(location, result);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = createMockInvocation();
        Matcher matcher = createArgumentMatcher(1);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(1);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
        CapturesArguments capturedMatcher = (CapturesArguments) matcher;
        Assert.assertNotNull(capturedMatcher.getCapturedValue());
        Assert.assertEquals(1, capturedMatcher.getCapturedValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromInvocationVarargs() {
        Invocation invocation = createMockInvocation();
        Matcher matcher = createVarargMatcher();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();
        List<InvocationMatcher> result = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));
        Assert.assertEquals(2, result.size());
        InvocationMatcher invocationMatcher1 = result.get(0);
        InvocationMatcher invocationMatcher2 = result.get(1);
        Assert.assertNotSame(invocationMatcher1, invocationMatcher2);
        Invocation invocation = invocationMatcher1.getInvocation();
        Assert.assertSame(invocation1, invocation);
        invocation = invocationMatcher2.getInvocation();
        Assert.assertSame(invocation2, invocation);
    }

    private Invocation createMockInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(new MockObject());
        return invocation;
    }

    private Method newMockMethod(String name, Class... parameterTypes) {
        return mock(Method.class);
    }

    private Location newMockLocation(String file, int line) {
        Location location = mock(Location.class);
        when(location.getFile()).thenReturn(file);
        when(location.getLineNumber()).thenReturn(line);
        return location;
    }

    private Matcher createArgumentMatcher(int index) {
        Matcher matcher = mock(Matcher.class);
        when(matcher.matches(anyObject())).thenReturn(true);
        return matcher;
    }

    private Matcher createVarargMatcher() {
        VarargMatcher varargMatcher = mock(VarargMatcher.class);
        return varargMatcher;
    }
}

class MockInvocation implements Invocation {
    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public Object getRawArguments() {
        return new Object[0];
    }

    @Override
    public Invocation getCallingInvocation() {
        return null;
    }

    @Override
    public Object[] getArgumentsAt(int index, Class[] types) {
        return new Object[0];
    }

    @Override
    public Object getArgumentAt(int index, Class type) {
        return null;
    }

    @Override
    public List<Invocation> getInvocations() {
        return Collections.emptyList();
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public boolean isVerified() {
        return false;
    }
}

class MockObject {
}