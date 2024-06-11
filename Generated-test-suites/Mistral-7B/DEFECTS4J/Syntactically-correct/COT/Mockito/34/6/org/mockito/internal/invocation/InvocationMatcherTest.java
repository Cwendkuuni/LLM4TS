import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImpl;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatchers() {
        InvocationMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> emptyMatchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(invocationMock, method), emptyMatchers);

        assertTrue(invocationMatcher.matches(invocationMatcher.getInvocation()));
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher.getInvocation()));
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatchers() {
        InvocationMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(CapturesArguments.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(invocationMock, method), matchers);

        assertTrue(invocationMatcher.matches(invocationMatcher.getInvocation()));
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher.getInvocation()));
    }

    @Test
    public void testGetMethod() {
        InvocationMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(invocationMock, method), Collections.emptyList());

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        InvocationMock invocationMock = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(invocationMock, null), Collections.emptyList());

        assertEquals(invocationMock, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        InvocationMock invocationMock = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(CapturesArguments.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(invocationMock, null), matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        InvocationMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(CapturesArguments.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(invocationMock, method), matchers);

        String toString = invocationMatcher.toString();
        assertTrue(toString.startsWith(invocationMatcher.getInvocation().toString()));
        assertTrue(toString.contains("InvocationMatcher"));
        assertTrue(toString.contains("Matcher"));
    }

    @Test
    public void testMatches() {
        InvocationMock invocationMock1 = Mockito.mock(InvocationOnMock.class);
        InvocationMock invocationMock2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> emptyMatchers = Collections.emptyList();

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(invocationMock1, method), emptyMatchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(invocationMock2, method), emptyMatchers);

        assertTrue(invocationMatcher1.matches(invocationMatcher1.getInvocation()));
        assertTrue(invocationMatcher2.matches(invocationMatcher2.getInvocation()));
        assertTrue(invocationMatcher1.matches(invocationMatcher2.getInvocation()));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Object[] actualArgs = new Object[]{Mockito.mock(Object.class), Mockito.mock(Object.class)};
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(invocationMock, null), Collections.emptyList());

        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMock invocationMock1 = Mockito.mock(InvocationOnMock.class);
        InvocationMock invocationMock2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(invocationMock1, method1), Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(invocationMock2, method2), Collections.emptyList());

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher1));
        assertTrue(invocationMatcher2.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher2.hasSimilarMethod(invocationMatcher1));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMock invocationMock1 = Mockito.mock(InvocationOnMock.class);
        InvocationMock invocationMock2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(invocationMock1, method), Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(invocationMock2, method), Collections.emptyList());

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher1));
        assertTrue(invocationMatcher2.hasSameMethod(invocationMatcher2));
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testGetLocation() {
        InvocationMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Location location = Mockito.mock(Location.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(invocationMock, null), Collections.emptyList());

        Whitebox.setInternalState(invocationMatcher, "location", location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test(expected = AssertionError.class)
    public void testToStringWithNullPrintSettings() {
        InvocationMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(CapturesArguments.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(invocationMock, method), matchers);

        invocationMatcher.toString(null);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{Mockito.mock(Object.class), Mockito.mock(Object.class)};
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(invocationMock, null), Collections.emptyList());

        invocationMatcher.captureArgumentsFrom(new InvocationOnMockImpl(invocationMock, null).getArguments());

        for (Matcher matcher : invocationMatcher.getMatchers()) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matcher;
                Object capturedArgument = capturesArguments.getCapturedArgument();
                assertTrue(Arrays.asList(arguments).contains(capturedArgument));
            }
        }
    }
}