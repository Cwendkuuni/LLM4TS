import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = Arrays.asList(new Equals("test"));
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
        String expected = invocation.toString(matchers, new PrintSettings());
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", 2, true});
        
        when(actual.getMethod()).thenReturn(method);
        when(actual.getMock()).thenReturn("mock");
        when(actual.getArguments()).thenReturn(new Object[]{"arg1", 2, true});
        
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", 2, true});
        
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getMock()).thenReturn("mock");
        when(candidate.getArguments()).thenReturn(new Object[]{"arg1", 2, true});
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        
        when(candidate.getMethod()).thenReturn(method);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = invocation.toString(matchers, printSettings);
        
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }

    // Add more test cases for edge cases and exception handling scenarios

}