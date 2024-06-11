import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = new Invocation();
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructor_WithInvocationAndMatchers_ShouldSetValues() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_WithInvocation_ShouldSetValues() {
        invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod_ShouldReturnMethodFromInvocation() {
        Method method = new Method();
        invocation.setMethod(method);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString_ShouldReturnStringRepresentationOfInvocationMatcher() {
        String methodName = "methodName";
        String mockName = "mockName";
        invocation.getMethod().setName(methodName);
        invocation.getMock().setName(mockName);
        assertEquals(methodName + " from " + mockName, invocationMatcher.toString());
    }

    @Test
    public void testMatches_WithMatchingInvocation_ShouldReturnTrue() {
        Invocation actual = new Invocation();
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_WithNonMatchingInvocation_ShouldReturnFalse() {
        Invocation actual = new Invocation();
        actual.getMock().setName("differentMock");
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod_WithSameMethodName_ShouldReturnTrue() {
        Invocation candidate = new Invocation();
        candidate.setMethod(invocation.getMethod());
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMethodName_ShouldReturnFalse() {
        Invocation candidate = new Invocation();
        candidate.getMethod().setName("differentMethodName");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithVerifiedCandidate_ShouldReturnFalse() {
        Invocation candidate = new Invocation();
        candidate.setVerified(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMock_ShouldReturnFalse() {
        Invocation candidate = new Invocation();
        candidate.getMock().setName("differentMock");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithOverloadedButSameArgs_ShouldReturnFalse() {
        Invocation candidate = new Invocation();
        candidate.setMethod(invocation.getMethod());
        candidate.setArguments(invocation.getArguments());
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithSameMethodAndDifferentArgs_ShouldReturnTrue() {
        Invocation candidate = new Invocation();
        candidate.setMethod(invocation.getMethod());
        candidate.setArguments(new Object[] {1, 2, 3});
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WithSameMethod_ShouldReturnTrue() {
        Invocation candidate = new Invocation();
        candidate.setMethod(invocation.getMethod());
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WithDifferentMethod_ShouldReturnFalse() {
        Invocation candidate = new Invocation();
        candidate.getMethod().setName("differentMethod");
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation_ShouldReturnLocationFromInvocation() {
        Location location = new Location();
        invocation.setLocation(location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom_WithVarargsMethod_ShouldCaptureVarargsArguments() {
        Method method = new Method();
        method.setVarArgs(true);
        invocation.setMethod(method);
        Object[] rawArguments = new Object[] {1, 2, 3};
        invocation.setRawArguments(rawArguments);
        Matcher matcher = Mockito.mock(Matcher.class);
        matchers.add(matcher);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        matchers.add(capturesArguments);

        invocationMatcher.captureArgumentsFrom(invocation);

        Mockito.verify(capturesArguments).captureFrom(rawArguments[2]);
    }

    @Test
    public void testCaptureArgumentsFrom_WithNonVarargsMethod_ShouldCaptureArguments() {
        Object[] arguments = new Object[] {1, 2, 3, 4};
        invocation.setArguments(arguments);
        Matcher matcher = Mockito.mock(Matcher.class);
        matchers.add(matcher);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        matchers.add(capturesArguments);

        invocationMatcher.captureArgumentsFrom(invocation);

        for (int i = 0; i < arguments.length; i++) {
            Mockito.verify(capturesArguments).captureFrom(arguments[i]);
        }
    }

    @Test
    public void testCreateFrom_WithInvocationList_ShouldCreateInvocationMatchersList() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

}