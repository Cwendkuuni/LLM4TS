
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    //Extract and list all the public methods including their signatures
    //Method 1
    public void InvocationMatcher_constructor_withList() {}

    //Method 2
    public void InvocationMatcher_constructor_withoutList() {}

    //Method 3
    public void getMethod() {}

    //Method 4
    public void getInvocation() {}

    //Method 5
    public void getMatchers() {}

    //Method 6
    public void toString_method() {}

    //Method 7
    public void matches() {}

    //Method 8
    public void safelyArgumentsMatch() {}

    //Method 9
    public void hasSimilarMethod() {}

    //Method 10
    public void hasSameMethod() {}

    //Method 11
    public void getLocation() {}

    //Method 12
    public void toString_withPrintSettings() {}

    //Method 13
    public void captureArgumentsFrom() {}
    
    //Basic test case for InvocationMatcher_constructor_withList method
    @Test
    public void testInvocationMatcherConstructorWithList() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
    }

    //Basic test case for InvocationMatcher_constructor_withoutList method
    @Test
    public void testInvocationMatcherConstructorWithoutList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
    }

    //Basic test case for getMethod method
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(method, invocationMatcher.getMethod());
    }

    //Basic test case for getInvocation method
    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    //Basic test case for getMatchers method
    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    //Basic test case for toString method
    @Test
    public void testToStringMethod() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher.toString());
    }

    //Basic test case for matches method
    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getArguments()).thenReturn(new Object[]{});
        when(invocation2.getArguments()).thenReturn(new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        assertTrue(invocationMatcher.matches(invocation2));
    }

    //Basic test case for safelyArgumentsMatch method
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] args = new Object[]{};
        assertTrue(invocationMatcher.safelyArgumentsMatch(args));
    }

    //Basic test case for hasSimilarMethod method
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getArguments()).thenReturn(new Object[]{});
        when(invocation2.getArguments()).thenReturn(new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    //Basic test case for hasSameMethod method
    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    //Basic test case for getLocation method
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(location,invocationMatcher.getLocation());
    }

    //Basic test case for toString method with PrintSettings
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        PrintSettings printSettings = mock(PrintSettings.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher.toString(printSettings));
    }

    //Basic test case for captureArgumentsFrom method
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[]{};
        when(i.getArguments()).thenReturn(arguments);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matchers.get(0)).thenReturn(capturesArguments);
        invocationMatcher.captureArgumentsFrom(i);
        verify(capturesArguments, times(1)).captureFrom(arguments[0]);
    }
}
