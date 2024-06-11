import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.reporting.PrintSettings;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationMatcher invocationMatcherWithMatchers;
    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<Matcher>();
        invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod_ReturnsMethodFromInvocation() {
        Method expected = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expected);
        Method actual = invocationMatcher.getMethod();
        assertSame(expected, actual);
    }

    @Test
    public void testGetInvocation_ReturnsInvocation() {
        Invocation actual = invocationMatcher.getInvocation();
        assertSame(invocation, actual);
    }

    @Test
    public void testGetMatchers_ReturnsMatchers() {
        List<Matcher> actual = invocationMatcher.getMatchers();
        assertEquals(matchers, actual);
    }

    @Test
    public void testToString_CallsPrintSettingsPrintMethod() {
        PrintSettings printSettings = mock(PrintSettings.class);
        whenNew(PrintSettings.class).withNoArguments().thenReturn(printSettings);
        when(printSettings.print(matchers, invocation)).thenReturn("test");
        String actual = invocationMatcher.toString();
        assertEquals("test", actual);
    }

    @Test
    public void testMatches_ReturnsTrue_WhenInvocationMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(actual.getMock()).thenReturn("mock");
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(new Object[]{});
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));
        when(mockArgumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);
        boolean result = invocationMatcher.matches(actual);
        assertTrue(result);
    }

    @Test
    public void testSafelyArgumentsMatch_ReturnsTrue_WhenArgumentsMatch() {
        Object[] actualArgs = new Object[]{};
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));
        when(mockArgumentsComparator.argumentsMatch(invocationMatcher, actualArgs)).thenReturn(true);
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        assertTrue(result);
    }

    @Test
    public void testSafelyArgumentsMatch_ReturnsFalse_WhenArgumentsDoNotMatch() {
        Object[] actualArgs = new Object[]{};
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));
        when(mockArgumentsComparator.argumentsMatch(invocationMatcher, actualArgs)).thenThrow(new Throwable());
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_ReturnsTrue_WhenCandidateHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(actual.getArguments()).thenReturn(new Object[]{});
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_ReturnsFalse_WhenCandidateDoesNotHaveSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_ReturnsFalse_WhenMethodsAreNotEqual() {
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m2);
        when(m1.getName()).thenReturn("name1");
        when(m2.getName()).thenReturn("name2");
        boolean result = invocationMatcher.hasSameMethod(candidate);
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_ReturnsTrue_WhenMethodsAreEqual() {
        Method m1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m1);
        when(m1.getName()).thenReturn("name");
        boolean result = invocationMatcher.hasSameMethod(candidate);
        assertTrue(result);
    }

    @Test
    public void testGetLocation_ReturnsLocationFromInvocation() {
        Location expected = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expected);
        Location actual = invocationMatcher.getLocation();
        assertSame(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom_CallsCaptureFromMethodOnMatchers() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[]{});
        captureArgumentsFrom(invocation);
        verify(mockMatchers.get(0)).captureFrom(invocation.getArgumentAt(0, Object.class));
    }

    @Test
    public void testCreateFrom_ReturnsListWithInvocationMatchers() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        List<Invocation> invocations = Collections.singletonList(invocation);
        List<InvocationMatcher> result = createFrom(invocations);
        assertEquals(invocationMatcher.getMethod(), result.get(0).getMethod());
    }

}