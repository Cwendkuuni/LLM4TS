import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = new Invocation();
        matchers = new LinkedList<Matcher>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testConstructor() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Method method = new Method();
        invocation.setMethod(method);
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
        String expectedResult = invocation.toString(matchers, new PrintSettings());
        assertEquals(expectedResult, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actual = new Invocation();
        actual.setMock(invocation.getMock());
        actual.setArguments(invocation.getArguments());
        actual.setVerified(invocation.isVerified());
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod_sameMethodName() {
        Invocation candidate = new Invocation();
        Method method = new Method();
        method.setName(invocation.getMethod().getName());
        candidate.setMethod(method);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_differentMethodName() {
        Invocation candidate = new Invocation();
        Method method = new Method();
        method.setName("differentMethodName");
        candidate.setMethod(method);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_isVerified() {
        Invocation candidate = new Invocation();
        candidate.setVerified(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_differentMock() {
        Invocation candidate = new Invocation();
        candidate.setMock(new Object());
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_sameMethod_sameArgs() {
        Invocation candidate = new Invocation();
        Method method = new Method();
        method.setName(invocation.getMethod().getName());
        candidate.setMethod(method);
        candidate.setArguments(invocation.getArguments());
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_sameMethod_overloadedArgs() {
        Invocation candidate = new Invocation();
        Method method = new Method();
        method.setName(invocation.getMethod().getName());
        candidate.setMethod(method);
        candidate.setArguments(new Object[]{});
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod_sameMethod() {
        Invocation candidate = new Invocation();
        Method method = new Method();
        method.setName(invocation.getMethod().getName());
        candidate.setMethod(method);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod_differentMethod() {
        Invocation candidate = new Invocation();
        Method method = new Method();
        method.setName("differentMethodName");
        candidate.setMethod(method);
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testgetLocation() {
        Location location = new Location();
        invocation.setLocation(location);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expectedResult = invocation.toString(matchers, printSettings);
        
        assertEquals(expectedResult, invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = new Invocation();
        Object[] arguments = new Object[]{};
        i.setArguments(arguments);
        CapturesArguments capturesArgumentsMock = mock(CapturesArguments.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(capturesArgumentsMock));
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArgumentsMock).captureFrom(arguments[0]);
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        Invocation i = new Invocation();
        invocations.add(i);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher invocationMatcher = invocationMatchers.get(0);
        
        assertNotNull(invocationMatchers);
        assertEquals(1, invocationMatchers.size());
        assertNotNull(invocationMatcher);
        assertEquals(i, invocationMatcher.getInvocation());
    }
    
}