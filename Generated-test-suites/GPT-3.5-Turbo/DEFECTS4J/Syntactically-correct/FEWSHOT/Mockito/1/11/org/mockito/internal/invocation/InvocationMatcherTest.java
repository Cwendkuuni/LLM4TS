import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method expected = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expected);
        
        Method actual = invocationMatcher.getMethod();
        
        assertSame(expected, actual);
    }

    @Test
    public void testGetInvocation() {
        Invocation actual = invocationMatcher.getInvocation();
        
        assertSame(invocation, actual);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        List<Matcher> actual = invocationMatcher.getMatchers();
        
        assertSame(matchers, actual);
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(any(List.class), any(Invocation.class))).thenReturn("toString");
        invocationMatcher = spy(new InvocationMatcher(invocation));
        doReturn(printSettings).when(invocationMatcher).getPrintSettings();
        
        String actual = invocationMatcher.toString();
        
        assertEquals("toString", actual);
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actual)).thenReturn(true);
        
        boolean result = invocationMatcher.matches(actual);
        
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m2);
        when(m1.getName()).thenReturn("method");
        when(m2.getName()).thenReturn("method");
        when(m1.getParameterTypes()).thenReturn(new Class[] {String.class});
        when(m2.getParameterTypes()).thenReturn(new Class[] {String.class});
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testGetLocation() {
        Location expected = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expected);
        
        Location actual = invocationMatcher.getLocation();
        
        assertSame(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(new Object[] {1, 2, 3});
        when(invocation.getArgumentAt(anyInt(), eq(Object.class))).thenReturn(1, 2, 3);
        Matcher matcher = mock(Matcher.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(invocationMatcher.getMatchers()).thenReturn(Arrays.asList(matcher, matcher, matcher));
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(invocationMatcher, times(3)).captureFrom(any());
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertSame(invocation1, actual.get(0).getInvocation());
        assertSame(invocation2, actual.get(1).getInvocation());
    }
}