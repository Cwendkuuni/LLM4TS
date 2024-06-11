import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private List<Invocation> invocations;
    private InvocationMatcher invocationMatcher;
    private InvocationMatcher emptyMatcher;

    @Before
    public void setUp() {
        invocations = Arrays.asList(
                Mockito.mock(InvocationOnMock.class),
                Mockito.mock(InvocationOnMock.class)
        );

        invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test"), new Object[0], new Object[0], new Location(1, 2)), Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.eq("hello")));
        emptyMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test"), new Object[0], new Object[0], new Location(1, 2)));
    }

    @Test
    public void testNewInstanceWithMatchers() {
        assertTrue(invocationMatcher.matches(Mockito.argThat(i -> i.getMethod().getName().equals("test"))));
        assertTrue(invocationMatcher.matches(Mockito.argThat(i -> i.getArguments()[0] instanceof String && i.getArguments()[1].equals("hello"))));
    }

    @Test
    public void testNewInstanceWithEmptyMatchersList() {
        assertTrue(emptyMatcher.matches(Mockito.argThat(i -> i.getMethod().getName().equals("test"))));
        assertTrue(emptyMatcher.matches(Mockito.anyInvocation()));
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new InvocationOnMockImpl(new Method(getClass(), "test"), new Object[0], new Object[0], new Location(1, 2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new InvocationOnMockImpl(new Method(getClass(), "test"), new Object[0], new Object[0], new Location(1, 2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test"), new Object[0], new Object[0], new Location(1, 2)), Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.eq("hello")));
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertEquals(2, matchers.size());
        assertTrue(matchers.get(0) instanceof ArgumentMatcher);
        assertTrue(matchers.get(1) instanceof ArgumentMatcher);
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test"), new Object[0], new Object[0], new Location(1, 2)), Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.eq("hello")));
        String toString = invocationMatcher.toString();
        assertTrue(toString.contains("test"));
        assertTrue(toString.contains("arg that"));
        assertTrue(toString.contains("anyString"));
        assertTrue(toString.contains("eq"));
        assertTrue(toString.contains("hello"));
    }

    @Test
    public void testMatches() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test"), new Object[0], new Object[0], new Location(1, 2)), Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.eq("hello")));
        Invocation invocation = new InvocationOnMockImpl(new Method(getClass(), "test"), new Object[0], new Object[0], new Location(1, 2));
        assertTrue(invocationMatcher.matches(invocation));

        invocation.getArguments()[0] = "world";
        assertFalse(invocationMatcher.matches(invocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test"), new Object[0], new Object[0], new Location(1, 2)), Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.eq("hello")));
        Object[] arguments = new Object[2];
        arguments[0] = "hello";
        arguments[1] = "world";
        assertFalse(invocationMatcher.safelyArgumentsMatch(arguments));

        arguments[1] = "hello";
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test1"), new Object[0], new Object[0], new Location(1, 2)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test2"), new Object[0], new Object[0], new Location(1, 2)));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        InvocationMatcher invocationMatcher3 = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test1"), new Object[0], new Object[0], new Location(1, 2)));
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test1"), new Object[0], new Object[0], new Location(1, 2)));
        assertTrue(invocationMatcher3.hasSimilarMethod(invocationMatcher4));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test1"), new Object[0], new Object[0], new Location(1, 2)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test2"), new Object[0], new Object[0], new Location(1, 2)));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));

        InvocationMatcher invocationMatcher3 = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test1"), new Object[0], new Object[0], new Location(1, 2)));
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test1"), new Object[0], new Object[0], new Location(1, 2)));
        assertTrue(invocationMatcher3.hasSameMethod(invocationMatcher4));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Method(getClass(), "test"), new Object[0], new Object[0], new Location(1, 2)), Arrays.asList(ArgumentMatchers.argThat(m -> m instanceof CapturesArguments), ArgumentMatchers.eq("hello")));
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Mockito.argThat(m -> m instanceof CapturesArguments));
        matchers.add(ArgumentMatchers.eq("hello"));
        invocationMatcher.captureArgumentsFrom(Mockito.mock(InvocationOnMock.class, matchers.toArray(new Matcher[0])));

        Object capturedArgument = invocationMatcher.getMatchers().get(0).getRealValue();
        assertTrue(capturedArgument instanceof InvocationOnMock);
    }

    @Test
    public void testCreateFrom() {
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected == null) {
            if (actual == null) {
                return;
            }
            fail("Expected " + getClass().getName() + " to be null, but was: " + actual);
        }
        if (!expected.getClass().equals(actual.getClass())) {
            fail("Expected " + getClass().getName() + " of type " + expected.getClass().getName() + ", but was: " + actual.getClass().getName());
        }
        if (!expected.equals(actual)) {
            fail("Expected " + getClass().getName() + " to be: " + expected + ", but was: " + actual);
        }
    }
}