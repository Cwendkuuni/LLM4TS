import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.hamcrest.Matcher;
import org.mockito.internal.reporting.PrintSettings;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        String expected = new PrintSettings().print(matchers, invocation);
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(actual.getMock());
        when(invocation.getMethod()).thenReturn(actual.getMethod());
        when(invocation.getArguments()).thenReturn(actual.getArguments());
        when(actual.isVerified()).thenReturn(false);
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        when(actual.getArguments()).thenReturn(invocation.getArguments());
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actual)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[10];
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("methodName");
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m2);
        when(m1.getName()).thenReturn("methodName");
        when(m2.getName()).thenReturn("methodName");
        when(m1.getParameterTypes()).thenReturn(new Class[0]);
        when(m2.getParameterTypes()).thenReturn(new Class[0]);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(new Object[10]);
        Matcher m = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        matchers.add(m);
        matchers.add(capturesArguments);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments, times(1)).captureFrom(any());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation);

        List<InvocationMatcher> expected = new LinkedList<>();
        expected.add(invocationMatcher);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        
        assertEquals(expected, result);
    }
}