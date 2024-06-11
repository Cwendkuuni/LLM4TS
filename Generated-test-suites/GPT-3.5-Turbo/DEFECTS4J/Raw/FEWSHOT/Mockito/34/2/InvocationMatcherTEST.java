package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        Invocation invocationMock = mock(Invocation.class);
        List<Matcher> matchersMock = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, matchersMock);
        
        assertSame(invocationMock, invocationMatcher.getInvocation());
        assertSame(matchersMock, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        Invocation invocationMock = mock(Invocation.class);
        List<Matcher> matchersMock = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        
        assertSame(invocationMock, invocationMatcher.getInvocation());
        assertSame(matchersMock, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocationMock = mock(Invocation.class);
        Method methodMock = mock(Method.class);
        when(invocationMock.getMethod()).thenReturn(methodMock);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        
        assertSame(methodMock, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocationMock = mock(Invocation.class);
        List<Matcher> matchersMock = mock(List.class);
        PrintSettings printSettingsMock = mock(PrintSettings.class);
        String expectedToString = "InvocationMatcher Mock";
        when(invocationMock.toString(matchersMock, printSettingsMock)).thenReturn(expectedToString);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, matchersMock);
        
        assertEquals(expectedToString, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocationMock = mock(Invocation.class);
        Invocation actualMock = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn("Mock");
        when(actualMock.getMock()).thenReturn("Mock");
        when(invocationMock.getMethod()).thenReturn(mock(Method.class));
        when(actualMock.getMethod()).thenReturn(mock(Method.class));
        when(invocationMock.argumentsToMatchers()).thenReturn(Collections.emptyList());
        when(actualMock.getArguments()).thenReturn(new Object[] {});
        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        when(argumentsComparatorMock.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        boolean matches = invocationMatcher.matches(actualMock);
        
        assertTrue(matches);
        verify(argumentsComparatorMock).argumentsMatch(invocationMatcher, actualMock.getArguments());
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocationMock = mock(Invocation.class);
        Invocation candidateMock = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        
        when(invocationMock.getMethod().getName()).thenReturn("methodA");
        when(candidateMock.getMethod().getName()).thenReturn("methodA");
        when(candidateMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn("Mock");
        when(candidateMock.getMock()).thenReturn("Mock");
        when(candidateMock.getMethod()).thenReturn(mock(Method.class));
        when(invocationMock.getMethod()).thenReturn(mock(Method.class));
        when(candidateMock.getArguments()).thenReturn(new Object[] {});
        when(invocationMock.argumentsMatch(candidateMock.getArguments())).thenReturn(true);
        
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateMock);
        
        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocationMock = mock(Invocation.class);
        Invocation candidateMock = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        
        when(invocationMock.getMethod()).thenReturn(mock(Method.class));
        when(candidateMock.getMethod()).thenReturn(mock(Method.class));
        
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidateMock);
        
        assertTrue(hasSameMethod);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocationMock = mock(Invocation.class);
        Location locationMock = mock(Location.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        
        when(invocationMock.getLocation()).thenReturn(locationMock);
        
        assertSame(locationMock, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocationMock = mock(Invocation.class);
        List<Matcher> matchersMock = mock(List.class);
        PrintSettings printSettingsMock = mock(PrintSettings.class);
        String expectedToString = "InvocationMatcher Mock";
        when(invocationMock.toString(matchersMock, printSettingsMock)).thenReturn(expectedToString);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, matchersMock);
        
        assertEquals(expectedToString, invocationMatcher.toString(printSettingsMock));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocationMock = mock(Invocation.class);
        Invocation iMock = mock(Invocation.class);
        Matcher matcherMock = mock(Matcher.class);
        CapturesArguments capturesArgumentsMock = mock(CapturesArguments.class);
        Object[] argumentsMock = new Object[] {};
        when(invocationMock.getArgumentsToMatchers()).thenReturn(Collections.singletonList(matcherMock));
        when(matcherMock instanceof CapturesArguments).thenReturn(true);
        when(matcherMock.captureFrom(argumentsMock[0])).thenAnswer(invocation -> {
            return capturesArgumentsMock;
        });
        when(iMock.getArguments()).thenReturn(argumentsMock);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        invocationMatcher.captureArgumentsFrom(iMock);
        
        verify(capturesArgumentsMock).captureFrom(argumentsMock[0]);
    }
}