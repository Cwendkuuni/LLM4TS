import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.hamcrest.Matcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.internal.reporting.PrintSettings;

import static org.junit.Assert.*;
import org.junit.*;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher1() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcher2() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method1 = new Method();
        Invocation invocation = new Invocation(method1);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(method1, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        List<Matcher> matchers = new LinkedList<>();
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = new PrintSettings().print(matchers, invocation);
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        List<Matcher> matchers = new LinkedList<>();
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();

        assertTrue(invocation1.getMock().equals(invocation2.getMock())
                && invocation1.hasSameMethod(invocation2)
                && argumentsComparator.argumentsMatch(invocation1, invocation2));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        List<Matcher> matchers = new LinkedList<>();
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();

        invocation1.setVerified(false);
        invocation1.setMock(invocation2.getMock());

        invocation2.setVerified(false);
        invocation2.setMock(invocation1.getMock());

        assertTrue(invocation1.getMethod().getName().equals(invocation2.getMethod().getName()));

        captorMatchers.addAll(matchers);
        
        assertTrue(invocation1.hasSimilarMethod(invocation2));
        assertTrue(invocation2.hasSimilarMethod(invocation1));

        captorMatchers.clear();
        captorMatchers.addAll(matchers);

        assertFalse(invocation1.hasSimilarMethod(invocation2));
        assertFalse(invocation2.hasSimilarMethod(invocation1));
    }

    @Test
    public void testHasSameMethod() {
        List<Matcher> matchers = new LinkedList<>();
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();

        Method method1 = invocation1.getMethod();
        Method method2 = invocation2.getMethod();

        assertTrue(method1.getName() != null && method1.getName().equals(method2.getName()));

        Class[] params1 = method1.getParameterTypes();
        Class[] params2 = method2.getParameterTypes();

        assertFalse(params1.length == params2.length);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Matcher> matchers = new LinkedList<>();
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();

        invocation1.getMethod().setVarArgs(true);
        invocation2.getMethod().setVarArgs(true);

        Object[] rawArguments1 = invocation1.getRawArguments();
        Object[] rawArguments2 = invocation2.getRawArguments();

        int indexOfVararg = rawArguments1.length - 1;

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            invocation1.captureArgumentsFrom(invocation2);
        });

        assertNotNull(exception);

        captorMatchers.clear();
        for (int position = 0; position < matchers.size(); position++) {
            Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments && invocation2.getRawArguments().length > position) {
                ((CapturesArguments) m).captureFrom(invocation2.getRawArguments()[position]);
            }
        }
    }

    @Test
    public void testCreateFrom() {
        LinkedList<InvocationMatcher> out = new LinkedList<InvocationMatcher>();
        LinkedList<Invocation> invocations = new LinkedList<Invocation>();

        for (Invocation i : invocations) {
            out.add(new InvocationMatcher(i));
        }

        List<InvocationMatcher> expectedResult = InvocationMatcher.createFrom(invocations);

        assertEquals(expectedResult, out);
    }

}