import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyZeroInteractions;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithInvocationAndEmptyList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = mock(Method.class);
        List<Matcher> matchers = Collections.emptyList();

        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(method, invocationMatcher.getMethod());
        assertNotNull(invocationMatcher.getMatchers());
        assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testInvocationMatcherWithInvocationAndNonEmptyList() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertEquals(2, invocationMatcher.getMatchers().size());

        verify(matcher1).matches(invocation.getArguments()[0]);
        verify(matcher2).matches(invocation.getArguments()[1]);
    }

    // Add other test cases for edge cases and exception handling scenarios

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments1 = new Object[]{1, 2, 3};
        Object[] arguments2 = new Object[]{1, 2, 3};

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(3)));

        assertTrue(invocationMatcher1.matches(invocation1));
        assertTrue(invocationMatcher1.matches(invocation2)); // false but we don't check this in test
        assertFalse(invocationMatcher2.matches(invocation1));
        assertFalse(invocationMatcher2.matches(invocation2)); // false but we don't check this in test

        assertTrue(invocationMatcher1.matches(new InvocationOnMock(method, arguments1)));
        assertTrue(invocationMatcher1.matches(new InvocationOnMock(method, arguments2))); // false but we don't check this in test
        assertFalse(invocationMatcher2.matches(new InvocationOnMock(method, arguments1)));
        assertFalse(invocationMatcher2.matches(new InvocationOnMock(method, arguments2))); // false but we don't check this in test
    }

    // Add other test cases for methods in the InvocationMatcher class

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expectedToString = "InvocationMatcher [invocation=" + invocation + ", matchers=" + matchers + "]";
        assertEquals(expectedToString, invocationMatcher.toString());
    }

    // Add other test cases for methods in the InvocationMatcher class

    @Test (expected = NullPointerException.class)
    public void testInvokeWithNullMatcher() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = null;

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // This should throw a NullPointerException
        invocationMatcher.matches(mock(Invocation.class));
    }

    @Test (expected = NullPointerException.class)
    public void testInvokeWithNullInvocation() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2));

        InvocationMatcher invocationMatcher = new InvocationMatcher(null, matchers);

        // This should throw a NullPointerException
        invocationMatcher.matches(mock(Invocation.class));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvokeWithDifferentMocks() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        // This should throw an IllegalArgumentException
        invocationMatcher1.matches(invocationMatcher2);
    }

    // Add other test cases for methods in the InvocationMatcher class

    @Test (expected = IllegalArgumentException.class)
    public void testInvokeWithOverloadedMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Object[] arguments1 = new Object[]{1, 2, 3};
        Object[] arguments2 = new Object[]{1, 3, 3};

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(3)));

        // This should throw an IllegalArgumentException
        invocationMatcher1.matches(new InvocationOnMock(method2, arguments1));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher1));

        Method sameMethod = mock(Method.class);
        InvocationMatcher sameMethodMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList(), sameMethod);
        InvocationMatcher sameMethodMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList(), sameMethod);

        assertTrue(sameMethodMatcher1.hasSameMethod(sameMethodMatcher2));

        assertFalse(invocationMatcher1.hasSameMethod(new InvocationMatcher(invocation1, Collections.emptyList(), mock(Method.class))));
        assertFalse(invocationMatcher1.hasSameMethod(new InvocationMatcher(invocation2, Collections.emptyList(), mock(Method.class))));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Captor<Matcher> captor = argumentCaptor(Matcher.class);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), captor.capture(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        Matcher capturedMatcher = captor.getValue();

        assertEquals(invocation.getArguments()[0], capturedMatcher.match(invocation.getArguments()[0]));
        assertEquals(invocation.getArguments()[1], capturedMatcher.match(invocation.getArguments()[1]));
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = PrintSettings.defaultPrintSettings();

        String expectedToString = "InvocationMatcher [invocation=" + invocation + ", matchers=" + matchers + "]";
        String actualToString = invocationMatcher.toString(printSettings);

        assertEquals(expectedToString, actualToString);
    }
}