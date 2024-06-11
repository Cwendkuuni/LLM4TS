import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentsMatcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcherFactory;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_whenMatchersAreEmpty_thenArgumentsAreMatchedUsingDefaultMatchers() {
        Invocation mockInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        Method method = mock(Method.class);
        when(mockInvocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");

        Object[] arguments = new Object[]{1, "string"};
        when(mockInvocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertThat(matchers.size(), is(0));

        InvocationMatcher actualMatcher = InvocationMatcherFactory.createMatcher(mockInvocation);
        assertTrue(invocationMatcher.matches(actualMatcher));

        verify(mockInvocation).getMethod();
        verify(mockInvocation).getArguments();
    }

    @Test
    public void testInvocationMatcher_whenMatchersAreProvided_thenArgumentsAreMatchedUsingProvidedMatchers() {
        Invocation mockInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, Collections.singletonList(new CapturesArguments("string")));

        Method method = mock(Method.class);
        when(mockInvocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");

        Object[] arguments = new Object[]{1, "string"};
        when(mockInvocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Matcher matcher = matchers.get(0);
        assertThat(matcher, is(instanceOf(CapturesArguments.class)));
        ((CapturesArguments) matcher).assertArgumentCapturedIs("string", arguments[1]);

        InvocationMatcher actualMatcher = InvocationMatcherFactory.createMatcher(mockInvocation);
        assertFalse(invocationMatcher.matches(actualMatcher));

        verify(mockInvocation).getMethod();
        verify(mockInvocation).getArguments();
    }

    @Test
    public void testHasSimilarMethod_whenInvocationsHaveSameMethod_thenReturnsTrue() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        when(invocation1.getMock()).thenReturn(invocation1.getMock());
        when(invocation2.getMock()).thenReturn(invocation1.getMock());

        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);

        assertTrue(new InvocationMatcher(invocation1).hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethod_whenInvocationsHaveDifferentMethods_thenReturnsFalse() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        when(invocation1.getMock()).thenReturn(invocation1.getMock());
        when(invocation2.getMock()).thenReturn(invocation1.getMock());

        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);

        String methodName1 = "testMethod1";
        String methodName2 = "testMethod2";
        when(method1.getName()).thenReturn(methodName1);
        when(method2.getName()).thenReturn(methodName2);

        assertFalse(new InvocationMatcher(invocation1).hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod_whenInvocationsHaveSameMethod_thenReturnsTrue() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        when(invocation1.getMock()).thenReturn(invocation1.getMock());
        when(invocation2.getMock()).thenReturn(invocation1.getMock());

        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);

        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
    }

    @Test
    public void testHasSameMethod_whenInvocationsHaveDifferentMethods_thenReturnsFalse() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        when(invocation1.getMock()).thenReturn(invocation1.getMock());
        when(invocation2.getMock()).thenReturn(invocation1.getMock());

        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);

        String methodName1 = "testMethod1";
        String methodName2 = "testMethod2";
        when(method1.getName()).thenReturn(methodName1);
        when(method2.getName()).thenReturn(methodName2);

        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
    }

    @Test
    public void testMatches_whenInvocationsHaveSameMethodAndArguments_thenReturnsTrue() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        when(invocation1.getMock()).thenReturn(invocation1.getMock());
        when(invocation2.getMock()).thenReturn(invocation1.getMock());

        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);

        Object[] arguments1 = new Object[]{1, "string"};
        Object[] arguments2 = new Object[]{1, "string"};
        when(invocation1.getArguments()).thenReturn(arguments1);
        when(invocation2.getArguments()).thenReturn(arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        String expectedToString1 = "Invocation [method = testMethod(int,java.lang.String), arguments = [1, string], invocationSite = <not available>]";
        String expectedToString2 = "Invocation [method = testMethod(int,java.lang.String), arguments = [1, string], invocationSite = <not available>]";
        InvocationMatcherFactory invocationMatcherFactory = new InvocationMatcherFactory();

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintDetails(true);

        when(invocationMatcher1.toString(any(PrintSettings.class))).thenReturn(expectedToString1);
        when(invocationMatcher2.toString(any(PrintSettings.class))).thenReturn(expectedToString2);

        when(invocationMatcher1.matches(invocationMatcher2)).thenReturn(true);

        InvocationMatcher actualMatcher1 = invocationMatcherFactory.createMatcher(invocation1);
        InvocationMatcher actualMatcher2 = invocationMatcherFactory.createMatcher(invocation2);

        String actualToString1 = actualMatcher1.toString();
        String actualToString2 = actualMatcher2.toString();

        assertTrue(invocationMatcher1.matches(actualMatcher2));
        assertEquals(expectedToString1, actualToString1);
        assertEquals(expectedToString2, actualToString2);
    }

    @Test
    public void testMatches_whenInvocationsHaveDifferentMethods_thenReturnsFalse() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        when(invocation1.getMock()).thenReturn(invocation1.getMock());
        when(invocation2.getMock()).thenReturn(invocation1.getMock());

        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);

        Object[] arguments1 = new Object[]{1, "string"};
        Object[] arguments2 = new Object[]{1, "otherString"};
        when(invocation1.getArguments()).thenReturn(arguments1);
        when(invocation2.getArguments()).thenReturn(arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        String expectedToString1 = "Invocation [method = testMethod1(int,java.lang.String), arguments = [1, string], invocationSite = <not available>]";
        String expectedToString2 = "Invocation [method = testMethod2(int,java.lang.String), arguments = [1, otherString], invocationSite = <not available>]";

        InvocationMatcherFactory invocationMatcherFactory = new InvocationMatcherFactory();

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintDetails(true);

        when(invocationMatcher1.toString(any(PrintSettings.class))).thenReturn(expectedToString1);
        when(invocationMatcher2.toString(any(PrintSettings.class))).thenReturn(expectedToString2);

        when(invocationMatcher1.matches(invocationMatcher2)).thenReturn(false);

        InvocationMatcher actualMatcher1 = invocationMatcherFactory.createMatcher(invocation1);
        InvocationMatcher actualMatcher2 = invocationMatcherFactory.createMatcher(invocation2);

        String actualToString1 = actualMatcher1.toString();
        String actualToString2 = actualMatcher2.toString();

        assertFalse(invocationMatcher1.matches(actualMatcher2));
        assertEquals(expectedToString1, actualToString1);
        assertEquals(expectedToString2, actualToString2);
    }

    @Test
    public void testCreateFrom_whenInvocationsListIsEmpty_thenReturnsEmptyList() {
        List<Invocation> invocations = Collections.emptyList();
        List<InvocationMatcher> actualMatchers = InvocationMatcher.createFrom(invocations);

        assertTrue(actualMatchers.isEmpty());
    }

    @Test
    public void testCreateFrom_whenInvocationsListIsNotEmpty_thenReturnsListOfInvocationMatchers() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        when(invocation1.getMock()).thenReturn(invocation1.getMock());
        when(invocation2.getMock()).thenReturn(invocation1.getMock());

        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> actualMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, actualMatchers.size());
        assertTrue(actualMatchers.contains(invocationMatcher1));
        assertTrue(actualMatchers.contains(invocationMatcher2));
    }
}