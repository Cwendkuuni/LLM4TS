package org.mockito.internal.invocation.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testDefaultConstructor() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertNotNull(matcher);
    }

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(eq("expectedValue"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(matcher);
    }

    @Test
    public void testGetters() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("expectedValue1"), eq("expectedValue2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
        assertEquals("InvocationMatcher [invocation=Invocation [mock=mock1, method=Method [name=testMethod, returnType=void, parameterTypes=[java.lang.String]], arguments=[], location=Location [line=1, methodName=testMethod, file=/test/Test.java]], matchers=[Matcher [is=org.hamcrest.Matcher@6d4e47e6 matcher=Matcher [is=org.hamcrest.Matchers$IsInstanceOf$1 matcher=Class [name=java.lang.String]], args=[], description=], Matcher [is=org.hamcrest.Matchers$IsInstanceOf$1 matcher=Class [name=java.util.List], args=[java.util.List], description=]]]", matcher.toString());
    }

    @Test
    public void testEqualsAndHashcode() {
        Invocation invocation1 = mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Invocation invocation3 = mock(Invocation.class);
        InvocationMatcher matcher3 = new InvocationMatcher(invocation3, Arrays.asList(eq("expectedValue")));

        assertNotEquals(matcher1, matcher2);
        assertNotEquals(matcher1, matcher3);
        assertNotEquals(matcher2, matcher3);

        InvocationMatcher matcher4 = new InvocationMatcher(matcher1);
        assertEquals(matcher1, matcher4);
        assertEquals(matcher1.hashCode(), matcher4.hashCode());
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher candidate1 = new InvocationMatcher(invocation);
        assertTrue(matcher1.hasSameMethod(candidate1));

        InvocationMatcher candidate2 = new InvocationMatcher(invocation, Arrays.asList(eq("expectedValue")));
        assertFalse(matcher1.hasSameMethod(candidate2));

        InvocationMatcher candidate3 = null;
        assertFalse(matcher1.hasSameMethod(candidate3));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher candidate4 = new InvocationMatcher(invocation, Arrays.asList(eq("expectedValue")));
        assertTrue(matcher1.hasSimilarMethod(candidate4));

        InvocationMatcher candidate5 = new InvocationMatcher(invocation, Arrays.asList(eq("expectedValue1"), eq("expectedValue2")));
        assertFalse(matcher1.hasSimilarMethod(candidate5));

        InvocationMatcher candidate6 = null;
        assertFalse(matcher1.hasSimilarMethod(candidate6));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("expectedValue1"), eq("expectedValue2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals("InvocationMatcher [invocation=Invocation [mock=mock1, method=Method [name=testMethod, returnType=void, parameterTypes=[java.lang.String]], arguments=[], location=Location [line=1, methodName=testMethod, file=/test/Test.java]], matchers=[Matcher [is=org.hamcrest.Matcher@6d4e47e6 matcher=Matcher [is=org.hamcrest.Matchers$IsInstanceOf$1 matcher=Class [name=java.lang.String]], args=[], description=], Matcher [is=org.hamcrest.Matchers$IsInstanceOf$1 matcher=Class [name=java.util.List], args=[java.util.List], description=]]]", matcher.toString());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{"argument1", "argument2"};
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.anyString()));
        matcher.captureArgumentsFrom(invocation);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(invocation).getArgumentAt(0, String.class, captor.capture());
        assertEquals("argument1", captor.getValue());
        verify(invocation).getArgumentAt(1, String.class, captor);
        assertEquals("argument2", captor.getValue());
    }

    @Test
    public void testExecute() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.argThat(arg -> arg.equals("expectedValue"))));

        try {
            matcher.execute();
            fail("Expected MatcherAssertionError");
        } catch (MatcherAssertionError e) {
            // Expected
        }

        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.argThat(arg -> arg.equals("expectedValue1")), ArgumentMatchers.argThat(arg -> arg.equals("expectedValue2"))));
        InvocationMatcher capturedMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.argThat(arg -> arg.equals("expectedValue"))));
        assertTrue(matcherWithMatchers.matches(capturedMatcher));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class), mock(Invocation.class));
        List<InvocationMatcher> expectedMatchers = Arrays.asList(new InvocationMatcher(invocations.get(0)), new InvocationMatcher(invocations.get(1)), new InvocationMatcher(invocations.get(2)));
        List<InvocationMatcher> actualMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(expectedMatchers, actualMatchers);
    }
}