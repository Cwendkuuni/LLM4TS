
import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {
    
    @Test
    public void testGetMethod() {
        Method expected = Invocation.class.getMethod("getMethod");
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
        assertEquals(expected, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        Invocation expected = null;
        InvocationMatcher invocationMatcher = new InvocationMatcher(expected);
        assertEquals(expected, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> expected = new ArrayList<Matcher>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(null, expected);
        assertEquals(expected, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        String expected = "PrintSettings().print(matchers, invocation)";
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actual = null;
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = null;
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = null;
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Location expected = null;
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
        assertEquals(expected, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = null;
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
        invocationMatcher.captureArgumentsFrom(invocation);
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<Invocation>();
        List<InvocationMatcher> expected = new ArrayList<InvocationMatcher>();
        assertEquals(expected, InvocationMatcher.createFrom(invocations));
    }
}
