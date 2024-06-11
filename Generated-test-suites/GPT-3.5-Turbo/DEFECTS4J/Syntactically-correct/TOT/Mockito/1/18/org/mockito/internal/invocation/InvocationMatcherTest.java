import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        Method actualMethod = invocationMatcher.getMethod();

        Assert.assertEquals(method, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();

        Assert.assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        Assert.assertEquals(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        Assert.assertNotNull(invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(actualInvocation.getArguments()).thenReturn(new Object[]{});

        boolean matches = invocationMatcher.matches(actualInvocation);

        Assert.assertTrue(matches);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(invocation.getMethod());

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        Assert.assertTrue(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        Location actualLocation = invocationMatcher.getLocation();

        Assert.assertEquals(location, actualLocation);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_VarArgs() {
        Method method = mock(Method.class);
        when(method.isVarArgs()).thenReturn(true);
        when(invocation.getMethod()).thenReturn(method);

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object argument = new Object();
        when(invocation.getArgumentAt(anyInt(), eq(Object.class))).thenReturn(argument);
        when(matcher instanceof CapturesArguments).thenReturn(true);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify((CapturesArguments) matcher, times(1)).captureFrom(argument);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        invocations.add(invocation1);
        invocations.add(invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        Assert.assertEquals(invocations.size(), invocationMatchers.size());
        Assert.assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        Assert.assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}