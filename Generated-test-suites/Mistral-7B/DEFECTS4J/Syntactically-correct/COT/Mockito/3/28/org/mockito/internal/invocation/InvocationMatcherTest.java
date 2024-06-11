import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.MockitoStubs;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocation;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        invocation = MockitoStubs.mockInvocation();
        matchers = Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testMatcherConstructorWithMatchers() {
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatcherConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcherWithoutMatchers = new InvocationMatcher(invocation);
        assertTrue(invocationMatcherWithoutMatchers.matches(invocation));
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = new Method("testMethod", new Class[]{String.class, int.class});
        Method actualMethod = invocationMatcher.getMethod();
        assertTrue(expectedMethod.getName().equals(actualMethod.getName()));
        assertTrue(expectedMethod.getParameterTypes().length == actualMethod.getParameterTypes().length);
        Arrays.stream(expectedMethod.getParameterTypes()).forEach(expectedType -> {
            assertTrue(expectedType.equals(actualMethod.getParameterTypes()[0]));
        });
    }

    @Test
    public void testGetInvocation() {
        assertTrue(invocationMatcher.getInvocation().equals(invocation));
    }

    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        String expectedToString = "InvocationMatcher[invocation=InvocationOnMock[this, InvocationOnMock.class, testMethod, [this, 0], 1, null, InvocationOnMock.java:76], matchers=[ArgumentMatchers.<String> anyString(), ArgumentMatchers.<Integer> anyInt()]]";
        String actualToString = invocationMatcher.toString();
        assertTrue(expectedToString.equals(actualToString));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMocks() {
        InvocationOnMock differentMockInvocation = MockitoStubs.mockInvocation();
        invocationMatcher.matches(differentMockInvocation);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMethods() {
        InvocationOnMock differentMethodInvocation = MockitoStubs.mockInvocation(new Method("testDifferentMethod", new Class[]{String.class}));
        invocationMatcher.matches(differentMethodInvocation);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithUnverifiedInvocation() {
        InvocationOnMock unverifiedInvocation = MockitoStubs.mockVerificationOrder(invocation, new InvocationOnMock[0]);
        invocationMatcher.matches(unverifiedInvocation);
    }

    @Test
    public void testHasSimilarMethod_sameArguments() {
        InvocationOnMock similarInvocation = MockitoStubs.mockInvocation("", new Object[]{"testString", 1});
        assertTrue(invocationMatcher.hasSimilarMethod(similarInvocation));
    }

    @Test
    public void testHasSimilarMethod_differentMethods() {
        InvocationOnMock differentMethodInvocation = MockitoStubs.mockInvocation("", new Object[]{"testDifferentString", 1});
        assertFalse(invocationMatcher.hasSimilarMethod(differentMethodInvocation));
    }

    @Test
    public void testHasSameMethod_sameMethod() {
        InvocationOnMock sameMethodInvocation = MockitoStubs.mockInvocation("testMethod", new Object[]{"testString", 1});
        InvocationOnMock sameMethodInvocation2 = MockitoStubs.mockInvocation("testMethod", new Object[]{"testString2", 1});
        assertTrue(invocationMatcher.hasSameMethod(sameMethodInvocation2));
    }

    @Test
    public void testHasSameMethod_differentMethods() {
        InvocationOnMock differentMethodInvocation = MockitoStubs.mockInvocation("testMethod1", new Object[]{"testString", 1});
        InvocationOnMock differentMethodInvocation2 = MockitoStubs.mockInvocation("testMethod2", new Object[]{"testString2", 1});
        assertFalse(invocationMatcher.hasSameMethod(differentMethodInvocation2));
    }

    @Test
    public void testSafelyArgumentsMatch_sameArguments() {
        Object[] sameArguments = new Object[]{"testString", 1};
        assertTrue(invocationMatcher.safelyArgumentsMatch(sameArguments));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatch_differentArguments() {
        Object[] differentArguments = new Object[]{"testString", 2};
        assertFalse(invocationMatcher.safelyArgumentsMatch(differentArguments));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        String expectedString = "testString";
        int expectedInt = 1;
        Matcher<String> stringMatcher = ArgumentMatchers.argThat(s -> s.equals(expectedString));
        Matcher<Integer> intMatcher = ArgumentMatchers.argThat(i -> i.equals(expectedInt));

        List<Matcher> newMatchers = Arrays.asList(stringMatcher, intMatcher);

        invocationMatcher = new InvocationMatcher(invocation, newMatchers);
        invocationMatcher.captureArgumentsFrom(invocation);

        assertTrue(stringMatcher.matches(invocation.getArgumentAt(0, String.class)));
        assertTrue(intMatcher.matches(invocation.getArgumentAt(1, Integer.class)));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocation, invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertTrue(invocationMatchers.size() == 2);
    }
}