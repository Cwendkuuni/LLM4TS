import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.invocation.MethodType;
import org.mockito.stub.Stubber;
import org.powermock.api.support.annotation.MockStatic;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.common.PowerMockRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@PrepareForTest(InvocationMatcher.class)
@MockStatic(ArgumentsComparator.class)
public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Stubber stubber;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        stubber = mock(Stubber.class);

        List<Matcher> matchers = Arrays.asList(eq("expected"), any(Integer.class));
        matcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatchers() {
        assertTrue(matcher instanceof InvocationMatcher);
        assertTrue(matcher.getMatchers().size() == 2);
        assertTrue(matcher.getMatchers().get(0) instanceof org.mockito.Matchers$ArgThat);
    }

    @Test
    public void testConstructorWithoutMatchers() {
        assertTrue(matcher instanceof InvocationMatcher);
        assertTrue(matcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = new Method("testMethod", new Class[0]);
        when(invocation.getMethod()).thenReturn(method);

        Method actualMethod = matcher.getMethod();
        assertEquals(method, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = spy(invocation);
        when(matcher.getInvocation()).thenReturn(actualInvocation);

        Invocation returnedInvocation = matcher.getInvocation();
        assertEquals(actualInvocation, returnedInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> actualMatchers = matcher.getMatchers();
        List<Matcher> expectedMatchers = Arrays.asList(eq("expected"), any(Integer.class));

        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        String printSettings = "";
        String actualToString = matcher.toString(new PrintSettings(printSettings));

        String expectedToString = invocation.toString(matchers, new PrintSettings(printSettings));
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());

        assertTrue(matcher.matches(invocation));
        assertTrue(matcher.matches(actualInvocation));

        Invocation differentInvocation = mock(Invocation.class);
        when(differentInvocation.getMethod()).thenReturn(new Method("differentMethod", new Class[0]));

        assertFalse(matcher.matches(differentInvocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[2];
        when(invocation.getArguments()).thenReturn(actualArgs);

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(ArgumentsComparator.argumentsMatch(matcher, any(Object[].class))).thenReturn(true);

        assertTrue(matcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn(invocation.getMock());

        assertTrue(matcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(new Method("testMethod", new Class[0]));

        assertFalse(matcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        Location actualLocation = matcher.getLocation();
        assertEquals(location, actualLocation);
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchesWithDifferentMock() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getMock()).thenReturn(new Object());

        matcher.matches(actualInvocation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesWithDifferentMethods() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(new Method("differentMethod", new Class[0]));

        matcher.matches(actualInvocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] invocationArgs = new Object[2];

        when(invocation.getArguments()).thenReturn(invocationArgs);

        matcher.captureArgumentsFrom(invocation);

        for (Matcher matcher : matcher.getMatchers()) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturedArguments = (CapturesArguments) matcher;
                Object capturedArgument = capturedArguments.getCapturedArgument();
                assertEquals(invocationArgs[0], capturedArgument);
            }
        }
    }
}