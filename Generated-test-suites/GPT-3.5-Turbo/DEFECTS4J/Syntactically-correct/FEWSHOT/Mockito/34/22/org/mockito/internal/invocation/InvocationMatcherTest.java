package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {
    
    @Mock
    private Invocation invocationMock;
    
    @Mock
    private Invocation otherInvocationMock;
    
    private InvocationMatcher invocationMatcher;
    
    private List<Matcher> matchers;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        matchers = new ArrayList<Matcher>();
        
        invocationMatcher = new InvocationMatcher(invocationMock, matchers);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(invocationMatcher);
        assertEquals(invocationMock, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithMatchers() {
        matchers.add(mock(Matcher.class));
        
        invocationMatcher = new InvocationMatcher(invocationMock, matchers);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocationMock, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithEmptyMatchers() {
        matchers.clear();
        
        when(invocationMock.argumentsToMatchers()).thenReturn(matchers);
        
        invocationMatcher = new InvocationMatcher(invocationMock, matchers);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocationMock, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method expectedMethod = InvocationMatcher.class.getDeclaredMethod("testGetMethod");
        
        when(invocationMock.getMethod()).thenReturn(expectedMethod);
        
        Method actualMethod = invocationMatcher.getMethod();
        
        assertNotNull(actualMethod);
        assertEquals(expectedMethod, actualMethod);
    }
    
    @Test
    public void testToString() {
        assertNotNull(invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        when(invocationMock.getMock()).thenReturn(new Object());
        when(invocationMock.isVerified()).thenReturn(false);
        when(otherInvocationMock.getMock()).thenReturn(new Object());
        when(otherInvocationMock.isVerified()).thenReturn(false);
        
        assertTrue(invocationMatcher.matches(invocationMock));
        assertFalse(invocationMatcher.matches(otherInvocationMock));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[0];
        
        assertFalse(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }
    
    @Test
    public void testHasSimilarMethod() {
        String wantedMethodName = "testHasSimilarMethod";
        String currentMethodName = "testHasSimilarMethod";
        
        Method wantedMethod = InvocationMatcher.class.getDeclaredMethod(wantedMethodName);
        Method currentMethod = InvocationMatcher.class.getDeclaredMethod(currentMethodName);
        
        when(invocationMock.getMethod()).thenReturn(wantedMethod);
        when(otherInvocationMock.getMethod()).thenReturn(currentMethod);
        when(invocationMock.isVerified()).thenReturn(false);
        when(invocationMock.getMock()).thenReturn(new Object());
        when(otherInvocationMock.getMock()).thenReturn(new Object());
        when(invocationMock.getArguments()).thenReturn(new Object[0]);
        when(otherInvocationMock.getArguments()).thenReturn(new Object[0]);
        
        assertTrue(invocationMatcher.hasSimilarMethod(otherInvocationMock));
        
        when(invocationMock.getMethod()).thenReturn(currentMethod);
        
        assertFalse(invocationMatcher.hasSimilarMethod(otherInvocationMock));
    }
    
    @Test
    public void testHasSameMethod() {
        String methodName = "testHasSameMethod";
        
        Method method = InvocationMatcher.class.getDeclaredMethod(methodName);
        
        when(invocationMock.getMethod()).thenReturn(method);
        when(otherInvocationMock.getMethod()).thenReturn(method);
        
        assertTrue(invocationMatcher.hasSameMethod(otherInvocationMock));
        
        when(invocationMock.getMethod()).thenReturn(mock(Method.class));
        
        assertFalse(invocationMatcher.hasSameMethod(otherInvocationMock));
    }
    
    @Test
    public void testGetLocation() {
        Location expectedLocation = new Location("", 0);
        
        when(invocationMock.getLocation()).thenReturn(expectedLocation);
        
        Location actualLocation = invocationMatcher.getLocation();
        
        assertNotNull(actualLocation);
        assertEquals(expectedLocation, actualLocation);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        
        assertNotNull(invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        CapturesArguments capturesArgumentsMock = mock(CapturesArguments.class);
        
        matchers.add(capturesArgumentsMock);
        
        Object[] arguments = new Object[1];
        
        when(invocationMock.getArguments()).thenReturn(arguments);
        
        invocationMatcher.captureArgumentsFrom(invocationMock);
        
        verify(capturesArgumentsMock).captureFrom(arguments[0]);
    }
}