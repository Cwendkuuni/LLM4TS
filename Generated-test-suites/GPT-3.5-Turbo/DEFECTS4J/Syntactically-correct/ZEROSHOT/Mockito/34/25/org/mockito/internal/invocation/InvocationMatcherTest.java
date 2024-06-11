import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {
    
    @Test
    public void testConstructor() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithMatchetersEmpty() {
        Invocation invocation = new Invocation();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Assert.assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Assert.assertEquals(invocation.toString(matchers, new PrintSettings()), invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Assert.assertTrue(invocationMatcher.matches(invocation));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(invocation.getArguments()));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Assert.assertTrue(invocationMatcher.hasSimilarMethod(invocation));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Assert.assertTrue(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Assert.assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        PrintSettings printSettings = new PrintSettings();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Assert.assertEquals(invocation.toString(matchers, printSettings), invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matcher;
                Assert.assertEquals(invocation.getArguments()[i], capturesArguments.getCapturedArguments());
            }
        }
    }
}