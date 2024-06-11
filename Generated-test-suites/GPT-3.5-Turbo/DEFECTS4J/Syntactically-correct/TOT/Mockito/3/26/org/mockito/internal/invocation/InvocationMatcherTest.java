import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {
    
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setup() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(getDummyMethod());
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(getDummyArguments());
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        invocationMatcher = new InvocationMatcher(invocation);
    }
    
    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocationMatcher.getInvocation(), matchers);
        
        assertThat(matcher.getMatchers(), is(equalTo(matchers)));
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher matcher = new InvocationMatcher(invocationMatcher.getInvocation());
        
        assertThat(matcher.getMatchers(), is(notNullValue()));
        assertThat(matcher.getMatchers().size(), is(equalTo(invocationMatcher.getInvocation().getArguments().length)));
    }
    
    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        
        assertThat(method, is(equalTo(getDummyMethod())));
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = invocationMatcher.getInvocation();
        
        assertThat(invocation, is(equalTo(invocationMatcher.getInvocation())));
    }

    @Test
    public void testToString() {
        String expectedString = "dummyPrintString";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(invocationMatcher.getMatchers(), invocationMatcher.getInvocation())).thenReturn(expectedString);
        
        String result = invocationMatcher.toString();
        
        assertThat(result, is(equalTo(expectedString)));
    }
    
    @Test
    public void testMatches_WithMatchingInvocation() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocationMatcher.getInvocation().getMock());
        when(actualInvocation.getMethod()).thenReturn(invocationMatcher.getInvocation().getMethod());
        when(actualInvocation.getArguments()).thenReturn(invocationMatcher.getInvocation().getArguments());
        
        boolean result = invocationMatcher.matches(actualInvocation);
        
        assertThat(result, is(true));
    }
    
    @Test
    public void testMatches_WithNonMatchingInvocation() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMethod()).thenReturn(getDummyMethod());
        when(actualInvocation.getArguments()).thenReturn(getDummyArguments());
        
        boolean result = invocationMatcher.matches(actualInvocation);
        
        assertThat(result, is(false));
    }
    
    @Test
    public void testHasSimilarMethod_WithMatchingMethodAndUnverifiedInvocation() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(getDummyMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocationMatcher.getInvocation().getMock());
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertThat(result, is(true));
    }
    
    @Test
    public void testHasSimilarMethod_WithNonMatchingMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocationMatcher.getInvocation().getMock());
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertThat(result, is(false));
    }
    
    @Test
    public void testHasSimilarMethod_WithVerifiedInvocation() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(getDummyMethod());
        when(candidate.isVerified()).thenReturn(true);
        when(candidate.getMock()).thenReturn(invocationMatcher.getInvocation().getMock());
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertThat(result, is(false));
    }
    
    @Test
    public void testHasSimilarMethod_WithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(getDummyMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertThat(result, is(false));
    }
    
    @Test
    public void testHasSimilarMethod_WithOverloadedMethodButSameArguments() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(getDummyMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocationMatcher.getInvocation().getMock());
        when(candidate.getArguments()).thenReturn(getDummyArguments());
        when(invocationMatcher.safelyArgumentsMatch(getDummyArguments())).thenReturn(true);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertThat(result, is(false));
    }
    
    @Test
    public void testHasSimilarMethod_WithOverloadedMethodAndDifferentArguments() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(getDummyMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocationMatcher.getInvocation().getMock());
        when(candidate.getArguments()).thenReturn(getDummyArguments());
        when(invocationMatcher.safelyArgumentsMatch(getDummyArguments())).thenReturn(false);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertThat(result, is(true));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(getDummyMethod());
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertThat(result, is(true));
    }
    
    @Test
    public void testHasSameMethod_WithDifferentName() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertThat(result, is(false));
    }
    
    @Test
    public void testHasSameMethod_WithDifferentArgumentTypes() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(getDummyMethodWithDifferentParameters());
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertThat(result, is(false));
    }
    
    @Test
    public void testGetLocation() {
        Location location = invocationMatcher.getLocation();
        
        assertThat(location, is(notNullValue()));
    }
    
    @Test
    public void testCaptureArgumentsFrom_WithVarArgs() {
        Invocation invocation = createVarArgsInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation argumentInvocation = mock(Invocation.class);
        Object[] rawArguments = getDummyArguments();
        when(argumentInvocation.getArgumentAt(0, Object.class)).thenReturn(rawArguments[0]);
        when(argumentInvocation.getRawArguments()).thenReturn(rawArguments);
        
        matcher.captureArgumentsFrom(argumentInvocation);
        
        // Verify that the argument was captured
        List<Matcher> matchers = matcher.getMatchers();
        assertThat(matchers.get(0), is(instanceOf(CapturesArguments.class)));
        CapturesArguments capturesArgumentsMatcher = (CapturesArguments) matchers.get(0);
        assertThat(capturesArgumentsMatcher.getArgument(), is(equalTo(rawArguments[0])));
        
        // Verify that the varargs arguments were captured
        for (int i = 1; i < rawArguments.length; i++) {
            assertThat(matchers.get(i), is(instanceOf(CapturesArguments.class)));
            capturesArgumentsMatcher = (CapturesArguments) matchers.get(i);
            assertThat(capturesArgumentsMatcher.getArgument(), is(equalTo(rawArguments[i])));
        }
    }
    
    @Test
    public void testCaptureArgumentsFrom_WithoutVarArgs() {
        Invocation invocation = createNonVarArgsInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation argumentInvocation = mock(Invocation.class);
        Object[] rawArguments = getDummyArguments();
        for (int i = 0; i < rawArguments.length; i++) {
            when(argumentInvocation.getArgumentAt(i, Object.class)).thenReturn(rawArguments[i]);
        }
        
        matcher.captureArgumentsFrom(argumentInvocation);
        
        // Verify that the arguments were captured
        List<Matcher> matchers = matcher.getMatchers();
        for (int i = 0; i < rawArguments.length; i++) {
            assertThat(matchers.get(i), is(instanceOf(CapturesArguments.class)));
            CapturesArguments capturesArgumentsMatcher = (CapturesArguments) matchers.get(i);
            assertThat(capturesArgumentsMatcher.getArgument(), is(equalTo(rawArguments[i])));
        }
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertThat(invocationMatchers, hasSize(invocations.size()));
        assertThat(invocationMatchers.get(0).getInvocation(), is(equalTo(invocation1)));
        assertThat(invocationMatchers.get(1).getInvocation(), is(equalTo(invocation2)));
    }
    
    private Method getDummyMethod() {
        try {
            return DummyClass.class.getMethod("dummyMethod", String.class, Integer.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    private Method getDummyMethodWithDifferentParameters() {
        try {
            return DummyClass.class.getMethod("dummyMethod", Integer.class, String.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    private Object[] getDummyArguments() {
        return new Object[] { "argument1", 2 };
    }
    
    private Invocation createVarArgsInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(getVarArgsMethod());
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] { "nonVarArg1", "nonVarArg2", "varArg1", "varArg2" });
        
        return invocation;
    }
    
    private Invocation createNonVarArgsInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(getDummyMethod());
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(getDummyArguments());
        
        return invocation;
    }
    
    private Method getVarArgsMethod() {
        try {
            return DummyClass.class.getMethod("dummyVarArgsMethod", String.class, Integer.class, String[].class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    private static class DummyClass {
        public void dummyMethod(String arg1, Integer arg2) {}
        public void dummyMethod(Integer arg1, String arg2) {}
        public void dummyVarArgsMethod(String arg1, Integer arg2, String... varArgs) {}
    }
}