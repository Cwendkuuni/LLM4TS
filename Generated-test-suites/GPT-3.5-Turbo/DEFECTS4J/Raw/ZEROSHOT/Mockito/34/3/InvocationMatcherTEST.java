import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.hamcrest.Matchers;
import java.lang.reflect.Method;
import java.util.Arrays;

public class InvocationMatcherTest {

    @Test
    public void testConstructor1() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, im.getInvocation());
        assertEquals(matchers, im.getMatchers());
    }
    
    @Test
    public void testConstructor2() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher im = new InvocationMatcher(invocation);
        assertEquals(invocation, im.getInvocation());
        assertTrue(im.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher im = new InvocationMatcher(invocation);
        assertEquals(invocation.getMethod(), im.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        PrintSettings settings = Mockito.mock(PrintSettings.class);
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        Mockito.when(invocation.toString(matchers, settings)).thenReturn("test string");
        String result = im.toString();
        assertEquals("test string", result);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation actual = Mockito.mock(Invocation.class);
        ArgumentsComparator acMock = Mockito.mock(ArgumentsComparator.class);
        InvocationMatcher im = new InvocationMatcher(invocation);
        im.setArgumentsComparator(acMock);
        Mockito.when(invocation.getMock()).thenReturn("mockObj");
        Mockito.when(actual.getMock()).thenReturn("mockObj");
        Mockito.when(acMock.argumentsMatch(im, actual)).thenReturn(true);
        boolean result = im.matches(actual);
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        ArgumentsComparator acMock = Mockito.mock(ArgumentsComparator.class);
        InvocationMatcher im = new InvocationMatcher(invocation);
        im.setArgumentsComparator(acMock);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.getMethod().getName()).thenReturn("methodName");
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mockObj");
        Mockito.when(candidate.getMock()).thenReturn("mockObj");
        Mockito.when(im.hasSameMethod(candidate)).thenReturn(false);
        Mockito.when(acMock.argumentsMatch(im, candidate.getArguments()))
            .thenReturn(true);
        
        boolean result = im.hasSimilarMethod(candidate);
        assertTrue(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        InvocationMatcher im = new InvocationMatcher(invocation);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getMethod().equals(candidate.getMethod()))
            .thenReturn(true);
        
        boolean result = im.hasSameMethod(candidate);
        assertTrue(result);
    }
}