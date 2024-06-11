import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    
    private InvocationMatcher invocationMatcher;
    private Invocation invocationMock;
    private List<Matcher> matchersMock;
    private List<Invocation> invocationsMock;
    
    @Before
    public void setUp() {
        invocationMock = mock(Invocation.class);
        matchersMock = mock(List.class);
        invocationsMock = mock(List.class);
        
        invocationMatcher = new InvocationMatcher(invocationMock, matchersMock);
    }
    
    @Test
    public void testConstructor() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, matchersMock);
        
        assertEquals(invocationMock, invocationMatcher.getInvocation());
        assertEquals(matchersMock, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testDefaultConstructor() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        
        assertEquals(invocationMock, invocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Method methodMock = mock(Method.class);
        when(invocationMock.getMethod()).thenReturn(methodMock);
        
        Method result = invocationMatcher.getMethod();
        
        assertEquals(methodMock, result);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation result = invocationMatcher.getInvocation();
        
        assertEquals(invocationMock, result);
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> result = invocationMatcher.getMatchers();
        
        assertEquals(matchersMock, result);
    }
    
    @Test
    public void testToString() {
        PrintSettings printSettingsMock = mock(PrintSettings.class);
        whenNew(PrintSettings.class).withNoArguments().thenReturn(printSettingsMock);
        
        String result = invocationMatcher.toString();
        
        verify(printSettingsMock).print(matchersMock, invocationMock);
    }
    
    @Test
    public void testMatches() {
        Invocation actualInvocationMock = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn(mock(Object.class));
        when(invocationMock.getArguments()).thenReturn(new Object[]{});
        when(actualInvocationMock.getMock()).thenReturn(mock(Object.class));
        when(actualInvocationMock.getArguments()).thenReturn(new Object[]{});
        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparatorMock);
        when(argumentsComparatorMock.argumentsMatch(eq(invocationMatcher), eq(actualInvocationMock))).thenReturn(true);
        
        boolean result = invocationMatcher.matches(actualInvocationMock);
        
        assertTrue(result);
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{};
        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparatorMock);
        when(argumentsComparatorMock.argumentsMatch(eq(invocationMatcher), eq(actualArgs))).thenReturn(true);

        
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidateMock = mock(Invocation.class);
        Method methodMock = mock(Method.class);
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(candidateMock.getMethod()).thenReturn(methodMock);
        when(invocationMock.getName()).thenReturn("methodName");
        when(candidateMock.getName()).thenReturn("methodName");
        when(candidateMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn(mock(Object.class));
        when(candidateMock.getMock()).thenReturn(mock(Object.class));
        when(invocationMatcher.hasSameMethod(eq(candidateMock))).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparatorMock);
        when(argumentsComparatorMock.argumentsMatch(eq(invocationMatcher), eq(candidateMock.getArguments()))).thenReturn(true);

        
        boolean result = invocationMatcher.hasSimilarMethod(candidateMock);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Method methodMock1 = mock(Method.class);
        Method methodMock2 = mock(Method.class);
        when(invocationMock.getMethod()).thenReturn(methodMock1);
        when(candidateMock.getMethod()).thenReturn(methodMock2);
        when(methodMock1.getName()).thenReturn("methodName");
        when(methodMock2.getName()).thenReturn("methodName");
        when(methodMock1.getParameterTypes()).thenReturn(new Class[]{});
        when(methodMock2.getParameterTypes()).thenReturn(new Class[]{});
        
        boolean result = invocationMatcher.hasSameMethod(candidateMock);
        
        assertTrue(result);
    }
    
    @Test
    public void testGetLocation() {
        Location locationMock = mock(Location.class);
        when(invocationMock.getLocation()).thenReturn(locationMock);
        
        Location result = invocationMatcher.getLocation();
        
        assertEquals(locationMock, result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_varArgs() {
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(methodMock.isVarArgs()).thenReturn(true);
        when(invocationMock.getRawArguments()).thenReturn(new Object[]{});
        when(invocationMock.getArgumentAt(anyInt(), eq(Object.class))).thenReturn(mock(Object.class));
        
        invocationMatcher.captureArgumentsFrom(invocationMock);
        
        fail("Expected UnsupportedOperationException");
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Matcher matcherMock = mock(Matcher.class);
        CapturesArguments capturesArgumentsMock = mock(CapturesArguments.class);
        when(matchersMock.get(anyInt())).thenReturn(matcherMock);
        when(matcherMock instanceof CapturesArguments).thenReturn(true);
        when((CapturesArguments)matcherMock).thenReturn(capturesArgumentsMock);
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(methodMock.isVarArgs()).thenReturn(false);
        when(invocationMock.getRawArguments()).thenReturn(new Object[]{mock(Object.class)});
        when(invocationMock.getArgumentAt(anyInt(), eq(Object.class))).thenReturn(mock(Object.class));
        
        invocationMatcher.captureArgumentsFrom(invocationMock);
        
        verify(capturesArgumentsMock).captureFrom(any());
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        InvocationMatcher invocationMatcher1 = mock(InvocationMatcher.class);
        InvocationMatcher invocationMatcher2 = mock(InvocationMatcher.class);
        whenNew(InvocationMatcher.class).withArguments(eq(invocation1)).thenReturn(invocationMatcher1);
        whenNew(InvocationMatcher.class).withArguments(eq(invocation2)).thenReturn(invocationMatcher2);

        
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        
        assertEquals(Arrays.asList(invocationMatcher1, invocationMatcher2), result);
    }
}