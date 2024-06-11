import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocationMock;

    @Before
    public void setUp() {
        invocationMock = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocationMock);
    }

    @Test
    public void testGetMethod() {
        Method expected = invocationMock.getMethod();
        Method actual = invocationMatcher.getMethod();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testGetInvocation() {
        Invocation expected = invocationMock;
        Invocation actual = invocationMatcher.getInvocation();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMatchers_withoutCustomMatchers() {
        List<Matcher> expected = invocationMock.argumentsToMatchers();
        List<Matcher> actual = invocationMatcher.getMatchers();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMatchers_withCustomMatchers() {
        List<Matcher> customMatchers = Collections.singletonList(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocationMock, customMatchers);
        
        List<Matcher> expected = customMatchers;
        List<Matcher> actual = invocationMatcher.getMatchers();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = invocationMock.toString(invocationMatcher.getMatchers(), new PrintSettings());
        String actual = invocationMatcher.toString();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches_withMatchingInvocation() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn(actualInvocation.getMock());
        when(invocationMock.getMethod()).thenReturn(actualInvocation.getMethod());
        when(invocationMock.argumentsMatch(actualInvocation)).thenReturn(true);
        
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_withNonMatchingInvocation() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn(actualInvocation.getMock());
        when(invocationMock.getMethod()).thenReturn(actualInvocation.getMethod());
        when(invocationMock.argumentsMatch(actualInvocation)).thenReturn(false);
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod_withOverloadedMethods() {
        Invocation candidate = mock(Invocation.class);
        Method method = invocationMock.getMethod();
        Method overloadedMethod = mock(Method.class);
        when(candidate.getMethod()).thenReturn(overloadedMethod);
        when(invocationMock.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");
        when(overloadedMethod.getName()).thenReturn("testMethod");
        when(candidate.isVerified()).thenReturn(false);
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn(candidate.getMock());
        when(candidate.getMock()).thenReturn(mock(Object.class));

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_withSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = invocationMock.getMethod();
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocationMock.getMethod()).thenReturn(method);
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn(candidate.getMock());
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_differentMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method method = invocationMock.getMethod();
        Method differentMethod = mock(Method.class);
        when(candidate.getMethod()).thenReturn(differentMethod);
        when(invocationMock.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");
        when(differentMethod.getName()).thenReturn("differentMethod");
        when(candidate.isVerified()).thenReturn(false);
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn(candidate.getMock());
        when(candidate.getMock()).thenReturn(mock(Object.class));
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_sameMethodName_butDifferentArguments() {
        Invocation candidate = mock(Invocation.class);
        Method method = invocationMock.getMethod();
        when(candidate.getMethod()).thenReturn(method);
        when(invocationMock.getMethod()).thenReturn(method);
        when(candidate.getArguments()).thenReturn(new Object[] {1, 2});
        when(invocationMock.getArguments()).thenReturn(new Object[] {3, 4});
        when(candidate.isVerified()).thenReturn(false);
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn(candidate.getMock());
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod_withSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = invocationMock.getMethod();
        when(candidate.getMethod()).thenReturn(method);
        when(invocationMock.getMethod()).thenReturn(method);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_withDifferentMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocationMock.getMethod()).thenReturn(method);
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location expected = invocationMock.getLocation();
        Location actual = invocationMatcher.getLocation();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_withCustomPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        
        String expected = invocationMock.toString(invocationMatcher.getMatchers(), printSettings);
        String actual = invocationMatcher.toString(printSettings);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[] {1, 2, 3};
        when(i.getArguments()).thenReturn(arguments);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(invocationMatcher.getMatchers()).thenReturn(Collections.singletonList(capturesArguments));
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArguments, times(1)).captureFrom(arguments[0]);
        verify(capturesArguments, times(1)).captureFrom(arguments[1]);
        verify(capturesArguments, times(1)).captureFrom(arguments[2]);
    }
}