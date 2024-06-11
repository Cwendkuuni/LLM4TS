import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.hamcrest.Matcher;
import org.mockito.internal.invocation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import static org.mockito.Mockito.*;
import org.mockito.internal.debugging.Location;

public class InvocationMatcherTest {

    @Test
    public void testConstructorInvokesArgumentsToMatchersIfEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        verify(invocation, times(1)).argumentsToMatchers();
    }
    
    @Test
    public void testConstructorDoesNotInvokeArgumentsToMatchersIfMatchegersNotEmpty() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        verify(invocation, times(0)).argumentsToMatchers();
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assert(invocationMatcher.getMethod() == method);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assert(invocationMatcher.getInvocation() == invocation);
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assert(invocationMatcher.getMatchers() == matchers);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);
        invocationMatcher.toString(printSettings);
        verify(invocation, times(1)).toString(matchers, printSettings);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] {});
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assert(invocationMatcher.matches(actual));
        
        verify(invocation, times(1)).getMock();
        verify(actual, times(1)).getMock();
        verify(invocation, times(1)).getArguments();
        verify(argumentsComparator, times(1)).argumentsMatch(any(), any());
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assert(invocationMatcher.safelyArgumentsMatch(new Object[] {}));
        
        verify(invocation, times(1)).getArguments();
        verify(argumentsComparator, times(1)).argumentsMatch(any(), any());
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assert(invocationMatcher.hasSimilarMethod(candidate));
        
        verify(invocation, times(1)).getMethod();
        verify(candidate, times(1)).getMethod();
        verify(invocation, times(1)).getMock();
        verify(argumentsComparator, times(1)).argumentsMatch(any(), any());
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assert(invocationMatcher.hasSameMethod(candidate));
        
        verify(invocation, times(1)).getMethod();
        verify(candidate, times(1)).getMethod();
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assert(invocationMatcher.getLocation() == location);
        
        verify(invocation, times(1)).getLocation();
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);
        
        invocationMatcher.toString(printSettings);
        
        verify(invocation, times(1)).toString(matchers, printSettings);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(invocation.getArguments()).thenReturn(new Object[] { matcher });
        when(capturesArguments.captureFrom(any())).thenReturn(null);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(invocation, times(1)).getArguments();
        verify(capturesArguments, times(1)).captureFrom(any());
    }
}