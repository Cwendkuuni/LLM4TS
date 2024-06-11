package org.mockito.internal.invocation;

import org.junit.Test;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.Mockito;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.hamcrest.Matcher;

import static org.junit.Assert.assertEquals;

public class InvocationMatcherTest {
    @Test
    public void testInvocationMatcherWithMatchers() {
        // setup
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // test
        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();
        assertEquals(expectedMethod, actualMethod);
        
        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(expectedInvocation, actualInvocation);
        
        List<Matcher> expectedMatchers = matchers;
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertEquals(expectedMatchers, actualMatchers);
        
        String expectedString = invocation.toString(matchers, new PrintSettings());
        String actualString = invocationMatcher.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testInvocationMatcherWithoutMatchers() {
        // setup
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // test
        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();
        assertEquals(expectedMethod, actualMethod);
        
        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(expectedInvocation, actualInvocation);
        
        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertEquals(expectedMatchers, actualMatchers);
        
        String expectedString = invocation.toString(expectedMatchers, new PrintSettings());
        String actualString = invocationMatcher.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatches() {
        // setup
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        Mockito.when(invocation1.getMock()).thenReturn("mock1");
        Mockito.when(invocation2.getMock()).thenReturn("mock2");
        Mockito.when(invocation1.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation2.getMethod()).thenReturn(Mockito.mock(Method.class));
        
        // test
        boolean expectedMatch1 = true;
        boolean actualMatch1 = invocationMatcher.matches(invocation1);
        assertEquals(expectedMatch1, actualMatch1);
        
        boolean expectedMatch2 = false;
        boolean actualMatch2 = invocationMatcher.matches(invocation2);
        assertEquals(expectedMatch2, actualMatch2);
    }

    @Test
    public void testHasSimilarMethod() {
        // setup
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        Mockito.when(invocation1.getMethod()).thenReturn(Mockito.mock(Method.class, "method1"));
        Mockito.when(invocation2.getMethod()).thenReturn(Mockito.mock(Method.class, "method2"));
        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class, "mock1"));
        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class, "mock2"));
        Mockito.when(invocation1.isVerified()).thenReturn(false);
        Mockito.when(invocation2.isVerified()).thenReturn(true);
        
        // test
        boolean expectedSimilar1 = false;
        boolean actualSimilar1 = invocationMatcher.hasSimilarMethod(invocation2);
        assertEquals(expectedSimilar1, actualSimilar1);
        
        boolean expectedSimilar2 = true;
        boolean actualSimilar2 = invocationMatcher.hasSimilarMethod(invocation1);
        assertEquals(expectedSimilar2, actualSimilar2);
    }

    @Test
    public void testHasSameMethod() {
        // setup
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        Method method1 = Mockito.mock(Method.class, "method1");
        Method method2 = Mockito.mock(Method.class, "method2");
        
        // test
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        boolean expectedMethod1 = false;
        boolean actualMethod1 = invocationMatcher.hasSameMethod(invocation2);
        assertEquals(expectedMethod1, actualMethod1);
        
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method1);
        boolean expectedMethod2 = true;
        boolean actualMethod2 = invocationMatcher.hasSameMethod(invocation2);
        assertEquals(expectedMethod2, actualMethod2);
    }

    @Test
    public void testToString() {
        // setup
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = new ArrayList<>();
        PrintSettings printSettings = new PrintSettings();
        
        // test
        Mockito.when(invocation.toString(matchers, printSettings)).thenReturn("mock invocation");
        String expectedString = "mock invocation";
        String actualString = invocationMatcher.toString(printSettings);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // setup
        InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        Mockito.when(invocationMatcher.getMatchers()).thenReturn(matchers);
        
        // test
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{});
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        // setup
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class));
        
        // test
        List<InvocationMatcher> expectedList = new ArrayList<>();
        expectedList.add(new InvocationMatcher(Mockito.mock(Invocation.class)));
        List<InvocationMatcher> actualList = InvocationMatcher.createFrom(invocations);
        assertEquals(expectedList, actualList);
    }
}