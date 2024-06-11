import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Captor
    private ArgumentCaptor<Matcher> matcherCaptor;

    @Test
    public void testGetMethod() {
        Invocation invocationMock = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        Method expectedMethod = mock(Method.class);

        when(invocationMatcher.getMethod()).thenReturn(expectedMethod);
        verify(invocationMatcher).getMethod();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullInvocation() {
        new InvocationMatcher(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMatchers() {
        Invocation invocationMock = mock(Invocation.class);
        new InvocationMatcher(invocationMock, null);
    }

    @Test
    public void testMatchesWithNullInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class));
        assertFalse(invocationMatcher.matches(null));
    }

    @Test
    public void testMatchesWithDifferentInvocations() {
        Invocation invocation1Mock = mock(Invocation.class);
        Invocation invocation2Mock = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1Mock);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2Mock);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocationMock = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocationMock);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocationMock);

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test(expected = NullPointerException.class)
    public void testGetInvocationWithNullInvocation() {
        new InvocationMatcher(null);
    }

    @Test
    public void testGetMatchersWithEmptyList() {
        Invocation invocationMock = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, Collections.emptyList());

        List<Matcher> expectedMatchers = invocationMatcher.getMatchers();
        assertTrue(expectedMatchers.isEmpty());
    }

    @Test
    public void testGetMatchersWithListOfMatchers() {
        Invocation invocationMock = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, Arrays.asList(mock(Matcher.class)));

        List<Matcher> expectedMatchers = invocationMatcher.getMatchers();
        assertTrue(expectedMatchers.size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullPrintSettings() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), Arrays.asList(mock(Matcher.class)));
        invocationMatcher.toString(null);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocationMock = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, Arrays.asList(mock(CapturesArguments.class)));

        invocationMatcher.captureArgumentsFrom(invocationMock);

        verify(invocationMatcher, times(invocationMatcher.getMatchers().size())).captureArgumentsFrom(anyArray());
    }
}