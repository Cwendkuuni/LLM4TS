import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_ConstructorWithInvocationAndEmptyMatchers_ShouldCreateInstance() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Assert.assertNotNull(matcher);
    }

    @Test
    public void testInvocationMatcher_ConstructorWithInvocationAndMatchers_ShouldCreateInstance() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = createMockMatchers();
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Assert.assertNotNull(matcher);
    }

    @Test
    public void testGetMethod_ShouldReturnMethod() {
        InvocationMatcher matcher = createInvocationMatcher();
        Method method = matcher.getMethod();
        Assert.assertNotNull(method);
    }

    @Test
    public void testGetInvocation_ShouldReturnInvocation() {
        InvocationMatcher matcher = createInvocationMatcher();
        Invocation invocation = matcher.getInvocation();
        Assert.assertNotNull(invocation);
    }

    @Test
    public void testGetMatchers_ShouldReturnMatchers() {
        InvocationMatcher matcher = createInvocationMatcher();
        List<Matcher> matchers = matcher.getMatchers();
        Assert.assertNotNull(matchers);
        Assert.assertEquals(0, matchers.size());
    }

    @Test
    public void testToString_ShouldReturnStringRepresentation() {
        InvocationMatcher matcher = createInvocationMatcher();
        String result = matcher.toString();
        Assert.assertNotNull(result);
    }

    @Test
    public void testMatches_WithSameInvocation_ShouldReturnTrue() {
        InvocationMatcher matcher = createInvocationMatcher();
        Invocation invocation = createMockInvocation();
        matcher.setInvocation(invocation);
        boolean result = matcher.matches(invocation);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_WithDifferentInvocation_ShouldReturnFalse() {
        InvocationMatcher matcher1 = createInvocationMatcher();
        Invocation invocation1 = createMockInvocation();
        matcher1.setInvocation(invocation1);

        InvocationMatcher matcher2 = createInvocationMatcher();
        Invocation invocation2 = createMockInvocationWithDifferentMethod();
        matcher2.setInvocation(invocation2);

        boolean result = matcher1.matches(matcher2.getInvocation());
        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithSameInvocation_ShouldReturnTrue() {
        InvocationMatcher matcher1 = createInvocationMatcher();
        Invocation invocation1 = createMockInvocation();
        matcher1.setInvocation(invocation1);

        InvocationMatcher matcher2 = createInvocationMatcher();
        Invocation invocation2 = invocation1;
        matcher2.setInvocation(invocation2);

        boolean result = matcher1.hasSimilarMethod(matcher2.getInvocation());
        Assert.assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_WithDifferentInvocation_ShouldReturnFalse() {
        InvocationMatcher matcher1 = createInvocationMatcher();
        Invocation invocation1 = createMockInvocation();
        matcher1.setInvocation(invocation1);

        InvocationMatcher matcher2 = createInvocationMatcher();
        Invocation invocation2 = createMockInvocationWithDifferentMethod();
        matcher2.setInvocation(invocation2);

        boolean result = matcher1.hasSimilarMethod(matcher2.getInvocation());
        Assert.assertFalse(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_WithVarargs_ShouldThrowException() {
        InvocationMatcher matcher = createInvocationMatcher();
        Invocation invocation = createMockInvocationWithVarargs();
        matcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom_ShouldCreateListOfMatchers() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocationWithDifferentMethod();
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
        Assert.assertNotNull(matchers);
        Assert.assertEquals(2, matchers.size());
    }

    private Invocation createMockInvocation() {
        Method method = getSampleMethod();
        return createMock(Invocation.class, method.getParameterTypes());
    }

    private Invocation createMockInvocationWithDifferentMethod() {
        Method method = getSampleMethodWithDifferentName();
        return createMock(Invocation.class, method.getParameterTypes());
    }

    private Invocation createMockInvocationWithVarargs() {
        Method method = getSampleMethodWithVarargs();
        return createMock(Invocation.class, method.getParameterTypes());
    }

    private Method getSampleMethod() {
        return new Method("sampleMethod", new Class[]{}, void.class);
    }

    private Method getSampleMethodWithDifferentName() {
        return new Method("differentMethod", new Class[]{}, void.class);
    }

    private Method getSampleMethodWithVarargs() {
        return new Method("sampleMethodWithVarargs", new Class[]{String[].class}, void.class);
    }

    private Object createMock(Class clazz, Class[] parameterTypes) {
        return Mockito.mock(clazz, withSettings().serializable());
    }

    private List<Matcher> createMockMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(createMock(Matcher.class));
        matchers.add(createMock(Matcher.class));
        return matchers;
    }

    private InvocationMatcher createInvocationMatcher() {
        InvocationMatcher matcher = new InvocationMatcher(null, null);
        return matcher;
    }
}