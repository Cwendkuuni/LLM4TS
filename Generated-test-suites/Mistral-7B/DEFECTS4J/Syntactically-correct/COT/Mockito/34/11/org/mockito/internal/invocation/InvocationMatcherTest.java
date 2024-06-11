import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocation;
    private List<Matcher> matchers;

    @Rule
    public TestRule mockitoRule = Mockito.rule();

    @Before
    public void setup() {
        Matcher mockMatcher = Mockito.any();
        matchers = Arrays.asList(mockMatcher);
        invocation = new InvocationOnMockImpl(new Object(), new Method(getClass(), "testMethod", new Class[0]), new Object[0], null);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(getClass(), "testMethod", new Class[0]), new Object[0], null), matchers);
        assertEquals(invocationMatcher.getMatchers(), matchers);
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(getClass(), "testMethod", new Class[0]), new Object[0], null));
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertEquals(method.getName(), "testMethod");
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = invocationMatcher.getInvocation();
        assertEquals(invocation.getMock().getClass(), Object.class);
        assertEquals(invocation.getMethod().getName(), "testMethod");
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertTrue(matchers.size() > 0);
        assertTrue(matchers.get(0) instanceof ArgumentMatcher);
    }

    @Test
    public void testToString() {
        String expected = String.format("%s %s(%s)", "InvocationMatcher", invocation.getMethod().getName(), Arrays.toString(invocation.getArguments()));
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatchesWithSameMethodAndSameMock() {
        InvocationOnMock sameInvocation = new InvocationOnMockImpl(new Object(), new Method(getClass(), "testMethod", new Class[0]), new Object[0], null);
        InvocationMatcher sameMatcher = new InvocationMatcher(sameInvocation, matchers);
        assertTrue(invocationMatcher.matches(sameMatcher));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentMock() {
        InvocationOnMock differentInvocation = new InvocationOnMockImpl(new Object(), new Method(getClass(), "testMethod", new Class[0]), new Object[0], null);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation, matchers);
        assertFalse(invocationMatcher.matches(differentMatcher));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Method differentMethod = new Method(getClass(), "testOtherMethod", new Class[0]);
        InvocationOnMock differentInvocation = new InvocationOnMockImpl(new Object(), differentMethod, new Object[0], null);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation, matchers);
        assertFalse(invocationMatcher.matches(differentMatcher));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMethodButSameArguments() {
        Method differentMethod = new Method(getClass(), "testOtherMethod", new Class[0]);
        Object[] differentArguments = new Object[0];
        InvocationOnMock differentInvocation = new InvocationOnMockImpl(new Object(), differentMethod, differentArguments, null);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation, matchers);
        assertTrue(invocationMatcher.matches(differentMatcher)); // Should fail with AssertionError
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] differentArguments = new Object[0];
        InvocationOnMock differentInvocation = new InvocationOnMockImpl(new Object(), invocation.getMethod(), differentArguments, null);
        assertTrue(invocationMatcher.safelyArgumentsMatch(differentInvocation.getArguments()));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationOnMock sameInvocation = new InvocationOnMockImpl(new Object(), new Method(getClass(), "testMethod", new Class[0]), new Object[0], null);
        InvocationMatcher sameMatcher = new InvocationMatcher(sameInvocation, matchers);
        assertTrue(invocationMatcher.hasSimilarMethod(sameMatcher));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Method differentMethod = new Method(getClass(), "testOtherMethod", new Class[0]);
        InvocationOnMock differentInvocation = new InvocationOnMockImpl(new Object(), differentMethod, new Object[0], null);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation, matchers);
        assertFalse(invocationMatcher.hasSimilarMethod(differentMatcher));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher sameMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.hasSameMethod(sameMatcher));
    }

    @Test
    public void testHasSameMethodWithDifferentMethod() {
        Method differentMethod = new Method(getClass(), "testOtherMethod", new Class[0]);
        InvocationOnMock differentInvocation = new InvocationOnMockImpl(new Object(), differentMethod, new Object[0], null);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation, matchers);
        assertFalse(invocationMatcher.hasSameMethod(differentMatcher));
    }
}