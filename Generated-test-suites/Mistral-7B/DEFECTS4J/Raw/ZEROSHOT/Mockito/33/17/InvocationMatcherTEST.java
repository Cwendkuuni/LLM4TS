import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocationOnMock;

    @Before
    public void setup() {
        Method method = Mockito.method(String.class, "length");
        invocationOnMock = Mockito.mock(String.class);
        invocationMatcher = new InvocationMatcher(new Invocation(invocationOnMock, method), Arrays.asList(ArgumentMatchers.anyString()));
    }

    @Test
    public void testMatches_sameInvocation() {
        InvocationMatcher matchingInvocation = new InvocationMatcher(new Invocation(invocationOnMock, method), Arrays.asList(ArgumentMatchers.anyString()));
        assertTrue(invocationMatcher.matches(matchingInvocation));
    }

    @Test
    public void testMatches_differentInvocation() {
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(ArgumentMatchers.anyString()));
        assertFalse(invocationMatcher.matches(otherInvocationMatcher));
    }

    @Test
    public void testMatches_differentMethod() {
        Method otherMethod = Mockito.method(Integer.class, "intValue");
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(new Invocation(invocationOnMock, otherMethod), Arrays.asList(ArgumentMatchers.anyString()));
        assertFalse(invocationMatcher.matches(otherInvocationMatcher));
    }

    @Test
    public void testMatches_unverified_invocation() {
        InvocationOnMock unverifiedInvocationMock = Mockito.mock(String.class, Mockito.CALLS_REAL_METHODS);
        InvocationMatcher unverifiedInvocationMatcher = new InvocationMatcher(new Invocation(unverifiedInvocationMock, method), Arrays.asList(ArgumentMatchers.anyString()));
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(new Invocation(invocationOnMock, method), Arrays.asList(ArgumentMatchers.anyString()));
        assertTrue(unverifiedInvocationMatcher.matches(otherInvocationMatcher));
    }

    @Test
    public void testHasSimilarMethod_sameInvocation() {
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(new Invocation(invocationOnMock, method), Arrays.asList(ArgumentMatchers.anyString()));
        assertTrue(invocationMatcher.hasSimilarMethod(otherInvocationMatcher));
    }

    @Test
    public void testHasSimilarMethod_differentInvocation() {
        Method otherMethod = Mockito.method(Integer.class, "intValue");
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(new Invocation(invocationOnMock, otherMethod), Arrays.asList(ArgumentMatchers.anyString()));
        assertFalse(invocationMatcher.hasSimilarMethod(otherInvocationMatcher));
    }

    @Test
    public void testHasSameMethod_sameInvocation() {
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(new Invocation(invocationOnMock, method), Arrays.asList(ArgumentMatchers.anyString()));
        assertTrue(invocationMatcher.hasSameMethod(otherInvocationMatcher));
    }

    @Test
    public void testHasSameMethod_differentMethod() {
        Method otherMethod = Mockito.method(Integer.class, "intValue");
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(new Invocation(invocationOnMock, otherMethod), Arrays.asList(ArgumentMatchers.anyString()));
        assertFalse(invocationMatcher.hasSameMethod(otherInvocationMatcher));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        String arg = "test";
        when(invocationOnMock.toString()).thenReturn(arg);

        invocationMatcher.captureArgumentsFrom(invocationOnMock);
        assertEquals(arg, invocationMatcher.getMatchers().get(0).getValue());
    }

    @Test
    public void testCreateFromInvocations() {
        List<Invocation> invocations = Arrays.asList(new Invocation(invocationOnMock, method), new Invocation(Mockito.mock(String.class), method));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertTrue(invocationMatchers.size() == 2);
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }
}