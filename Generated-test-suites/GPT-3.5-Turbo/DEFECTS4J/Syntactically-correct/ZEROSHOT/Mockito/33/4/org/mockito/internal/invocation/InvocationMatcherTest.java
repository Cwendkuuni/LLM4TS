import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.hamcrest.Matcher;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() {
        invocation = new Invocation();
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
        String expected = invocation.toString(matchers, new PrintSettings());
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches() {
        Invocation actual = new Invocation();
        boolean expected = invocation.getMock().equals(actual.getMock())
                && invocationMatcher.hasSameMethod(actual)
                && new ArgumentsComparator().argumentsMatch(invocationMatcher, actual);
        boolean result = invocationMatcher.matches(actual);
        assertEquals(expected, result);
    }
    
    @Test
    public void testMatches_safelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{};
        boolean expected = false;
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        assertEquals(expected, result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation();
        boolean expectedResult = methodNameEquals(candidate) && isUnverified(candidate) && mockIsTheSame(candidate) && methodEquals(candidate);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        assertEquals(expectedResult, result);
    }
    
    private boolean methodNameEquals(Invocation candidate) {
        String wantedMethodName = invocation.getMethod().getName();
        String currentMethodName = candidate.getMethod().getName();
        return wantedMethodName.equals(currentMethodName);
    }
    
    private boolean isUnverified(Invocation candidate) {
        return !candidate.isVerified();
    }
    
    private boolean mockIsTheSame(Invocation candidate) {
        return invocation.getMock() == candidate.getMock();
    }
    
    private boolean methodEquals(Invocation candidate) {
        Method m1 = invocation.getMethod();
        Method m2 = candidate.getMethod();
        return m1.equals(m2);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation();
        boolean expected = invocation.getMethod().equals(candidate.getMethod());
        boolean result = invocationMatcher.hasSameMethod(candidate);
        assertEquals(expected, result);
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
        String expected = invocation.toString(matchers, printSettings);
        String actual = invocationMatcher.toString(printSettings);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = new Invocation();
        int k = 0;
        
        for (Matcher m : matchers) {
            if (m instanceof CapturesArguments && i.getArguments().length > k) {
                CapturesArguments capturesArguments = (CapturesArguments) m;
                Object expected = i.getArguments()[k];
                capturesArguments.captureFrom(expected);
                Object actual = capturesArguments.getArgument();
                assertEquals(expected, actual);
            }
            k++;
        }
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        invocations.add(invocation);
        List<InvocationMatcher> expected = new LinkedList<InvocationMatcher>();
        expected.add(new InvocationMatcher(invocation));
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        assertEquals(expected, actual);
    }

}