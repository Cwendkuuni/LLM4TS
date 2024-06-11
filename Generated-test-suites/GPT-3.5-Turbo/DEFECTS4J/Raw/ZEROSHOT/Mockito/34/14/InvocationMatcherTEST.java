import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() throws Exception {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }
    
    @Test
    public void testGetMethod() {
        Method expected = invocation.getMethod();
        Method actual = invocationMatcher.getMethod();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation expected = invocation;
        Invocation actual = invocationMatcher.getInvocation();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> expected = invocation.argumentsToMatchers();
        List<Matcher> actual = invocationMatcher.getMatchers();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        String expected = invocation.toString(invocation.argumentsToMatchers(), new PrintSettings());
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn(invocation.getMock());
        when(invocationMock.getMethod()).thenReturn(invocation.getMethod());
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getArguments()).thenReturn(invocation.getArguments());
        
        boolean expected = true;
        boolean actual = invocationMatcher.matches(actual);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches_False() {
        Invocation actual = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn(mock(Object.class));
        when(invocationMock.getMethod()).thenReturn(invocation.getMethod());
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getArguments()).thenReturn(invocation.getArguments());
        
        boolean expected = false;
        boolean actual = invocationMatcher.matches(actual);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        
        boolean expected = true;
        boolean actual = invocationMatcher.hasSimilarMethod(candidate);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHasSimilarMethod_False() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        
        boolean expected = false;
        boolean actual = invocationMatcher.hasSimilarMethod(candidate);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        
        boolean expected = true;
        boolean actual = invocationMatcher.hasSameMethod(candidate);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHasSameMethod_False() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        boolean expected = false;
        boolean actual = invocationMatcher.hasSameMethod(candidate);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLocation() {
        Location expected = invocation.getLocation();
        Location actual = invocationMatcher.getLocation();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString_PrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expected = invocation.toString(invocation.argumentsToMatchers(), printSettings);
        String actual = invocationMatcher.toString(printSettings);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[invocation.getArguments().length];
        for (int k = 0; k < invocation.getArguments().length; k++) {
            arguments[k] = new Object();
        }
        when(i.getArguments()).thenReturn(arguments);
        
        invocationMatcher.captureArgumentsFrom(i);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        for (int k = 0; k < matchers.size(); k++) {
            Matcher matcher = matchers.get(k);
            if (matcher instanceof CapturesArguments) {
                Object expected = arguments[k];
                Object actual = ((CapturesArguments) matcher).getCapturedArguments();
                assertEquals(expected, actual);
            }
        }
    }
}