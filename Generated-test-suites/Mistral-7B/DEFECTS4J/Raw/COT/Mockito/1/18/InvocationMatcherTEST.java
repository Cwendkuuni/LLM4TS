import org.junit.Assert;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchersList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();
        Assert.assertNotNull(invocationMatcher.getMethod());
        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testConstructorWithInvocationAndMatchersList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(Mockito.anyInt(), Mockito.anyString());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocation.getMethod();
        Assert.assertNotNull(invocationMatcher.getMethod());
        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testMatchesMethodWithSameNameAndArguments() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidateInvocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method);
        Assert.assertTrue(invocationMatcher.matches(candidateInvocation));
    }

    @Test
    public void testMatchesMethodWithDifferentName() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidateInvocation = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method2);
        Assert.assertFalse(invocationMatcher.matches(candidateInvocation));
    }

    @Test
    public void testMatchesMethodWithSameNameButDifferentArguments() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidateInvocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments = new Object[0];
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Assert.assertFalse(invocationMatcher.matches(candidateInvocation));
    }

    @Test
    public void testMatchesMethodWithSameNameButDifferentMock() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidateInvocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(candidateInvocation.getMock()).thenReturn(Mockito.mock(Object.class, Mockito.withSettings().serializable()));
        Assert.assertFalse(invocationMatcher.matches(candidateInvocation));
    }

    @Test
    public void testMatchesMethodWithSameNameButUnverifiedCandidate() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidateInvocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method);
        Mockito.when(candidateInvocation.isVerified()).thenReturn(false);
        Assert.assertTrue(invocationMatcher.matches(candidateInvocation));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargMethod() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        if (method.isVarArgs()) {
            throw new UnsupportedOperationException();
        }
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Invocation candidateInvocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method);
        Assert.assertTrue(new InvocationMatcher(invocation).hasSameMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Invocation candidateInvocation = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method2);
        Assert.assertFalse(new InvocationMatcher(invocation).hasSameMethod(candidateInvocation));
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(Mockito.anyInt(), Mockito.anyString());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = String.format("Invocation with method %s and arguments %s", invocation.getMethod().toGenericString(), Arrays.toString(invocation.getArguments()));
        String actual = invocationMatcher.toString();
        Assert.assertEquals(expected, actual);
    }
}