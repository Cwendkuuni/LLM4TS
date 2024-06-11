import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void constructor_withList_shouldSetInvocationAndMatchers() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void constructor_withoutList_shouldSetInvocationAndEmptyMatchers() {
        InvocationMatcher invocationMatcherWithoutList = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcherWithoutList.getInvocation());
        assertEquals(0, invocationMatcherWithoutList.getMatchers().size());
    }

    @Test
    public void getMethod_shouldReturnMethodFromInvocation() {
        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);

        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void toString_shouldReturnStringRepresentationOfMatchersAndInvocation() {
        String expectedString = "Matchers: [matcher1, matcher2], Invocation: [invocation]";
        PrintSettings printSettingsMock = mock(PrintSettings.class);
        whenNew(PrintSettings.class).withNoArguments().thenReturn(printSettingsMock);
        when(printSettingsMock.print(matchers, invocation)).thenReturn(expectedString);

        String actualString = invocationMatcher.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void matches_withMatchingInvocation_shouldReturnTrue() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2});
        when(actualInvocation.getMock()).thenReturn("mock");
        when(actualInvocation.getArguments()).thenReturn(new Object[]{1, 2});
        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparatorMock);
        when(argumentsComparatorMock.argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertTrue(matches);
    }

    @Test
    public void matches_withNonMatchingInvocation_shouldReturnFalse() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2});
        when(actualInvocation.getMock()).thenReturn("mock2");
        when(actualInvocation.getArguments()).thenReturn(new Object[]{1, 2});
        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparatorMock);
        when(argumentsComparatorMock.argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(false);

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertFalse(matches);
    }

    @Test
    public void hasSimilarMethod_withSameMethodAndUnverifiedInvocation_shouldReturnTrue() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
    }

    @Test
    public void hasSimilarMethod_withDifferentMethodName_shouldReturnFalse() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName1");
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod().getName()).thenReturn("methodName2");

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void hasSimilarMethod_withVerifiedInvocation_shouldReturnFalse() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(true);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void hasSimilarMethod_withDifferentMock_shouldReturnFalse() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(invocation.getMock()).thenReturn("mock1");
        when(candidate.getMock()).thenReturn("mock2");

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void hasSimilarMethod_withSameMethodAndOverloadedArguments_shouldReturnFalse() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(invocation.getMethod().getParameterTypes()).thenReturn(new Class[]{int.class});
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getParameterTypes()).thenReturn(new Class[]{String.class});

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(hasSimilarMethod);
    }

    @Test
    public void hasSameMethod_withSameMethod_shouldReturnTrue() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        when(method1.getParameterTypes()).thenReturn(new Class[]{int.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{int.class});

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertTrue(hasSameMethod);
    }

    @Test
    public void hasSameMethod_withDifferentMethodName_shouldReturnFalse() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodName1");
        when(method2.getName()).thenReturn("methodName2");

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertFalse(hasSameMethod);
    }

    @Test
    public void hasSameMethod_withDifferentParameterTypes_shouldReturnFalse() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        when(method1.getParameterTypes()).thenReturn(new Class[]{int.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{String.class});

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertFalse(hasSameMethod);
    }

    @Test
    public void getLocation_shouldReturnLocationFromInvocation() {
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);

        Location actualLocation = invocationMatcher.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void captureArgumentsFrom_withVarArgs_shouldThrowUnsupportedOperationException() {
        Invocation invocationWithVarArgs = mock(Invocation.class);
        when(invocationWithVarArgs.getMethod()).thenReturn(mock(Method.class));
        when(invocationWithVarArgs.getMethod().isVarArgs()).thenReturn(true);
        when(invocationWithVarArgs.getRawArguments()).thenReturn(new Object[]{1, 2});

        invocationMatcher.captureArgumentsFrom(invocationWithVarArgs);
    }

    @Test
    public void captureArgumentsFrom_withMatchedArguments_shouldCaptureArgumentsForMatchers() {
        MethodInvocation invocationWithArgs = new MethodInvocation();
        invocationWithArgs.setArguments(1, null, "test", Arrays.asList(1, 2));
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(invocationWithArgs.getArguments().toArray());
        Matcher matcher1Mock = mock(Matcher.class);
        Matcher matcher2Mock = mock(Matcher.class);
        matchers.add(matcher1Mock);
        matchers.add(matcher2Mock);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(matcher1Mock).captureFrom(1);
        verify(matcher2Mock).captureFrom(null);
    }

    private static class MethodInvocation {
        private List<Object> arguments = new ArrayList<>();

        public List<Object> getArguments() {
            return arguments;
        }

        public void setArguments(Object... args) {
            this.arguments = Arrays.asList(args);
        }
    }
}