import org.hamcrest.ArgumentMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("expectedValue"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        // assert statements here
    }

    @Test
    public void testEdgeCases() {
        // Test with empty matchers list
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        // assert statements for empty matchers list

        // Test with null invocation
        InvocationMatcher invocationMatcherNullInvocation = new InvocationMatcher(null, Arrays.asList(ArgumentMatchers.eq("expectedValue")));

        // assert statements for null invocation

        // Test with null matchers list
        InvocationMatcher invocationMatcherNullMatchers = new InvocationMatcher(Mockito.mock(Invocation.class), null);

        // assert statements for null matchers list

        // Test with mismatched invocation and matchers types
        Invocation invocationMismatched = Mockito.mock(Invocation.class);
        List<Matcher> matchersMismatched = Arrays.asList(new CapturesArgumentsMatcher("expectedValue"));
        InvocationMatcher invocationMatcherMismatched = new InvocationMatcher(invocationMismatched, matchersMismatched);

        // assert statements for mismatched invocation and matchers types
    }

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testInvocationMatcherWithNullInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(null, Arrays.asList(ArgumentMatchers.eq("expectedValue")));
        assertNull(invocationMatcher.getInvocation());
    }

    @Test
    public void testInvocationMatcherWithNullMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), null);
        assertNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherWithMismatchedInvocationAndMatchersTypes() {
        Invocation invocationMismatched = Mockito.mock(Invocation.class);
        List<Matcher> matchersMismatched = Arrays.asList(new CapturesArgumentsMatcher("expectedValue"));
        InvocationMatcher invocationMatcherMismatched = new InvocationMatcher(invocationMismatched, matchersMismatched);
        assertFalse(invocationMatcherMismatched.matches(Mockito.mock(Invocation.class)));
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("expectedValue1"), ArgumentMatchers.eq("expectedValue2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals("InvocationMatcher:\n"
                + "[Method]\n"
                + "  Invocation: Invocation:\n"
                + "    invocation: Invocation:\n"
                + "      mock: <MockitoMock$1>MockitoMock:invocation\n"
                + "      method: public void testMethod(java.lang.String arg1, java.lang.String arg2)\n"
                + "      arguments: []\n"
                + "    location: InvocationLocation:\n"
                + "      line: 0\n"
                + "      file: <not available>\n"
                + "      class: <not available>\n"
                + "      method: <not available>\n"
                + "      lineNumber: 0\n"
                + "      fileName: <not available>\n"
                + "\n"
                + "[Matchers]\n"
                + "  Matcher:\n"
                + "    Matcher: org.hamcrest.Matcher:\n"
                + "      description: Equal to: expectedValue1\n"
                + "  Matcher:\n"
                + "    Matcher: org.hamcrest.Matcher:\n"
                + "      description: Equal to: expectedValue2\n"
                + "\n"
                + "]", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("expectedValue1"), ArgumentMatchers.eq("expectedValue2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation candidate = Mockito.mock(Invocation.class);
        assertTrue(invocationMatcher.matches(candidate));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(ArgumentMatchers.eq("expectedValue")));
        Object[] actualArgs = new Object[]{"actualValue1", "actualValue2"};
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithException() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(ArgumentMatchers.eq("expectedValue")));
        Object[] actualArgs = new Object[]{"actualValue1", new RuntimeException("Exception")};
        assertFalse(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodNames() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentCaptor.forClass(String.class), ArgumentCaptor.forClass(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(Mockito.mock(Invocation.class));
        ArgumentCaptor<String> argCaptor1 = matchers.get(0);
        ArgumentCaptor<String> argCaptor2 = matchers.get(1);
        assertEquals("expectedValue1", argCaptor1.getValue());
        assertEquals("expectedValue2", argCaptor2.getValue());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
    }
}