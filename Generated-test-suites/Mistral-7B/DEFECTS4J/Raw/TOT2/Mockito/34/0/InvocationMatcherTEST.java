import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {
    private Invocation invocation;
    private InvocationMatcher matcher;

    @Before
    public void setUp() {
        invocation = Mockito.spy(new Invocation(new Object() {}.getClass().getMethod("test", String.class), "test", "test", new Object[]{"test"}, new Location(1, 1), new Object[0]));
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(Collections.emptyList(), matcher.getMatchers());
        assertEquals(invocation.getMethod().getName(), matcher.getMethod().getName());
        assertEquals(invocation.getLocation(), matcher.getLocation());
    }

    @Test
    public void testToStringWithDefaultPrintSettings() {
        String expected = "InvocationMatcher: test(java.lang.String)";
        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Matcher matcher1 = Mockito.mock(Matcher.class);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.singletonList(matcher1));
        verify(matcher1).matches(invocation.getArguments()[0]);
        assertTrue(matcher.matches(matcher2));
    }

    @Test
    public void testMatchesWithDifferentInvocation() {
        Invocation differentInvocation = Mockito.spy(new Invocation(new Object() {}.getClass().getMethod("test2", int.class), "test2", "test2", new Object[]{1}, new Location(1, 1), new Object[0]));
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation);
        assertFalse(matcher.matches(differentMatcher));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{new Object()};
        InvocationMatcher otherMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Collections.singletonList(Mockito.mock(ArgumentsMatcher.class)));
       assertTrue(matcher.safelyArgumentsMatch(arguments));
        verify(otherMatcher.getMatchers().get(0)).matches(arguments[0]);
    }

    @Test
    public void testHasSameMethodWithSameInvocation() {
        InvocationMatcher otherMatcher = new InvocationMatcher(invocation);
        assertTrue(matcher.hasSameMethod(otherMatcher));
    }

    @Test
    public void testHasSameMethodWithDifferentInvocation() {
        Invocation differentInvocation = Mockito.spy(new Invocation(new Object() {}.getClass().getMethod("test2", int.class), "test2", "test2", new Object[]{1}, new Location(1, 1), new Object[0]));
        InvocationMatcher otherMatcher = new InvocationMatcher(differentInvocation);
        assertFalse(matcher.hasSameMethod(otherMatcher));
    }
}