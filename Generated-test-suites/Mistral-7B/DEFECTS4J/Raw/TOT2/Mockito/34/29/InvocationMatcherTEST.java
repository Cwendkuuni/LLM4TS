import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doReturn;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class InvocationMatcherTest {

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1, Collections.emptyList());

        assertTrue(matcher.matches(invocation1));
    }

    @Test
    public void testDifferentMocksAndMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1, Collections.emptyList());

        doReturn(false).when(invocation1).getMock().equals(invocation2.getMock());
        doReturn(new Method("method1", new Class[]{}, new Class[0])).when(invocation1).getMethod();
        doReturn(new Method("method2", new Class[]{}, new Class[0])).when(invocation2).getMethod();

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testNullActual() {
        InvocationMatcher matcher = new InvocationMatcher(null, Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void testSameMockButDifferentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1, Collections.emptyList());

        doReturn(true).when(invocation1).getMock();
        doReturn(true).when(invocation2).getMock();
        doReturn(new Method("method1", new Class[]{}, new Class[0])).when(invocation1).getMethod();
        doReturn(new Method("method2", new Class[]{}, new Class[0])).when(invocation2).getMethod();

        assertFalse(matcher.matches(invocation2));
    }

    // Test cases 6 to 11

    @Test
    public void testMatchesWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        Matcher mockMatcher = mock(Matcher.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(mockMatcher));

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatchesWithNullMatcher() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, null);

        assertFalse(matcher.matches(invocation));
    }

    @Test
    public void testMatchesWithEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertFalse(matcher.matches(invocation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesWithMatcherThatThrowsException() {
        Invocation invocation = mock(Invocation.class);
        Throwable exception = new RuntimeException("Test exception");
        Matcher mockMatcher = mock(Matcher.class);
        doThrow(exception).when(mockMatcher).matches(any());

        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(mockMatcher));
        Invocation otherInvocation = mock(Invocation.class);

        matcher.matches(otherInvocation);
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        String expected = "InvocationMatcher: " + invocation;
        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        String expected = invocation.toString();
        assertEquals(expected, matcher.toString(new PrintSettings()));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        List<Object> capturedArguments = new ArrayList<>();
        Matcher mockMatcher = mock(Matcher.class);
        doReturn(capturedArguments).when(mockMatcher).arguments();

        matcher.captureArgumentsFrom(invocation);

        assertEquals(1, capturedArguments.size());
        assertEquals(invocation.getArguments()[0], capturedArguments.get(0));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        Location expected = mock(Location.class);
        doReturn(expected).when(invocation).getLocation();

        Location actual = matcher.getLocation();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        Method expected = mock(Method.class);
        doReturn(expected).when(invocation).getMethod();

        Method actual = matcher.getMethod();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        Invocation actual = matcher.getInvocation();

        assertEquals(invocation, actual);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.singletonList(mock(Matcher.class)));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.singletonList(mock(Matcher.class)));

        List<Matcher> matchers1 = matcher1.getMatchers();
        List<Matcher> matchers2 = matcher2.getMatchers();

        assertEquals(1, matchers1.size());
        assertEquals(1, matchers2.size());
        assertEquals(matchers1.get(0), matchers2.get(0));
    }
}