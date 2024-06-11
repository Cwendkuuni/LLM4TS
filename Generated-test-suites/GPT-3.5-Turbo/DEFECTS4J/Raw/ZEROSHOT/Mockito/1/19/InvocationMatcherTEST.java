import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import org.hamcrest.Matcher;


public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocation.getMethod();
        Assert.assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = new PrintSettings().print(matchers, invocation);
        Assert.assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation actual = new Invocation();
        boolean result = invocationMatcher.matches(actual);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] actualArgs = new Object[10];
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation candidate = new Invocation();
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation candidate = new Invocation();
        boolean result = invocationMatcher.hasSameMethod(candidate);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Location expected = invocation.getLocation();
        Assert.assertEquals(expected, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation invocation2 = new Invocation();
        invocationMatcher.captureArgumentsFrom(invocation2);
        // Add assertions here
    }
    
    @Test
    public void testIsVarargMatcher() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Matcher matcher = new Matcher();
        boolean result = invocationMatcher.isVarargMatcher(matcher);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsVariableArgument() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        int position = 0;
        boolean result = invocationMatcher.isVariableArgument(invocation, position);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        LinkedList<InvocationMatcher> expected = new LinkedList<>();
        for (Invocation i : invocations) {
            expected.add(new InvocationMatcher(i));
        }
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        Assert.assertEquals(expected, result);
    }

}