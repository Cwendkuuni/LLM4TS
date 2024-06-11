import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matcher;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Method;
import java.util.*;

public class InvocationMatcherTest {
    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Test
    public void testConstructorWithMatchersNotEmpty() {
        invocation = createMock(Invocation.class);
        matchers = Arrays.asList(createMock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithMatchersEmpty() {
        invocation = createMock(Invocation.class);
        matchers = Collections.emptyList();
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertEquals(ArgumentsProcessor.argumentsToMatchers(invocation.getArguments())
                , invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        invocation = createMock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        invocation = createMock(Invocation.class);
        Method method = createMock(Method.class);
        expect(invocation.getMethod()).andReturn(method);

        replay(invocation, method);

        invocationMatcher = new InvocationMatcher(invocation);
        assertSame(method, invocationMatcher.getMethod());

        verify(invocation, method);
    }

    @Test
    public void testToString() {
        Invocation invocation = createMock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(createMock(Matcher.class));
        PrintSettings printSettings = createMock(PrintSettings.class);

        expect(invocation.getMock()).andReturn("mock");
        expect(printSettings.print(matchers, invocation)).andReturn("toString");

        replay(invocation, printSettings);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.toString();

        verify(invocation, printSettings);
    }

    @Test
    public void testMatches() {
        Invocation actual = createMock(Invocation.class);
        Object[] arguments = createMock(Object[].class);

        expect(invocation.getMock()).andReturn("mock");
        expect(actual.getMock()).andReturn("mock");

        expect(invocation.getMethod()).andReturn(createMock(Method.class));
        expect(actual.getMethod()).andReturn(createMock(Method.class));

        ArgumentsComparator argumentsComparator = createMock(ArgumentsComparator.class);
        expect(argumentsComparator.argumentsMatch(invocation, actual)).andReturn(true);

        replay(invocation, actual, argumentsComparator);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);

        verify(invocation, actual, argumentsComparator);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = createMock(Object[].class);
        ArgumentsComparator argumentsComparator = createMock(ArgumentsComparator.class);

        expect(argumentsComparator.argumentsMatch(invocation, actualArgs)).andReturn(true);

        replay(argumentsComparator);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertTrue(result);

        verify(argumentsComparator);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = createMock(Invocation.class);

        expect(invocation.getMethod().getName()).andReturn("method");
        expect(candidate.getMethod().getName()).andReturn("method");
        expect(candidate.isVerified()).andReturn(false);
        expect(getInvocation().getMock()).andReturn(getInvocation().getMock());
        expect(hasSameMethod(candidate)).andReturn(true);
        expect(safelyArgumentsMatch(candidate.getArguments())).andReturn(false);

        replay(invocation, candidate);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);

        verify(invocation, candidate);
    }

    @Test
    public void testHasSimilarMethod_methodNameNotEqual() {
        Invocation candidate = createMock(Invocation.class);

        expect(invocation.getMethod().getName()).andReturn("method1");
        expect(candidate.getMethod().getName()).andReturn("method2");

        replay(invocation, candidate);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);

        verify(invocation, candidate);
    }

    @Test
    public void testHasSimilarMethod_isVerified() {
        Invocation candidate = createMock(Invocation.class);

        expect(invocation.getMethod().getName()).andReturn("method");
        expect(candidate.getMethod().getName()).andReturn("method");
        expect(candidate.isVerified()).andReturn(true);

        replay(invocation, candidate);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);

        verify(invocation, candidate);
    }

    @Test
    public void testHasSimilarMethod_mockNotTheSame() {
        Invocation candidate = createMock(Invocation.class);

        expect(invocation.getMethod().getName()).andReturn("method");
        expect(candidate.getMethod().getName()).andReturn("method");
        expect(candidate.isVerified()).andReturn(false);
        expect(getInvocation().getMock()).andReturn("mock1");
        expect(candidate.getMock()).andReturn("mock2");

        replay(invocation, candidate);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);

        verify(invocation, candidate);
    }

    @Test
    public void testHasSimilarMethod_notOverloadedButSameArgs() {
        Invocation candidate = createMock(Invocation.class);

        expect(invocation.getMethod().getName()).andReturn("method");
        expect(candidate.getMethod().getName()).andReturn("method");
        expect(candidate.isVerified()).andReturn(false);
        expect(getInvocation().getMock()).andReturn(getInvocation().getMock());
        expect(hasSameMethod(candidate)).andReturn(false);
        expect(safelyArgumentsMatch(candidate.getArguments())).andReturn(true);

        replay(invocation, candidate);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);

        verify(invocation, candidate);
    }

    @Test
    public void testHasSameMethod_methodNameNotEqual() {
        Method m1 = createMock(Method.class);
        Method m2 = createMock(Method.class);

        expect(invocation.getMethod()).andReturn(m1);
        expect(candidate.getMethod()).andReturn(m2);
        expect(m1.getName()).andReturn("method1");
        expect(m2.getName()).andReturn("method2");

        replay(invocation, candidate, m1, m2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);

        verify(invocation, candidate, m1, m2);
    }

    @Test
    public void testHasSameMethod_parameterLengthNotEqual() {
        Method m1 = createMock(Method.class);
        Method m2 = createMock(Method.class);

        expect(invocation.getMethod()).andReturn(m1);
        expect(candidate.getMethod()).andReturn(m2);
        expect(m1.getName()).andReturn("method");
        expect(m2.getName()).andReturn("method");
        expect(m1.getParameterTypes()).andReturn(new Class[0]);
        expect(m2.getParameterTypes()).andReturn(new Class[1]);

        replay(invocation, candidate, m1, m2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);

        verify(invocation, candidate, m1, m2);
    }

    @Test
    public void testHasSameMethod_differentParameterTypes() {
        Method m1 = createMock(Method.class);
        Method m2 = createMock(Method.class);
        Class[] param1 = new Class[]{String.class};
        Class[] param2 = new Class[]{Integer.class};

        expect(invocation.getMethod()).andReturn(m1);
        expect(candidate.getMethod()).andReturn(m2);
        expect(m1.getName()).andReturn("method");
        expect(m2.getName()).andReturn("method");
        expect(m1.getParameterTypes()).andReturn(param1);
        expect(m2.getParameterTypes()).andReturn(param2);

        replay(invocation, candidate, m1, m2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);

        verify(invocation, candidate, m1, m2);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = createMock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(createMock(Matcher.class));
        CapturesArguments capturesArguments = createMock(CapturesArguments.class);
        Method method = createMock(Method.class);
        Object expectedArgument = new Object();

        expect(invocation.getMethod()).andReturn(method);
        expect(invocation.getArguments()).andReturn(new Object[]{expectedArgument});
        expect(method.isVarArgs()).andReturn(false);
        expect(matchers.get(0)).andReturn(capturesArguments);
        capturesArguments.captureFrom(expectedArgument);

        replay(invocation, matchers, capturesArguments, method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation, matchers, capturesArguments, method);
    }

    @Test
    public void testCaptureArgumentsFrom_VarArgsMethod() {
        Invocation invocation = createMock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(createMock(Matcher.class));
        CapturesArguments capturesArguments = createMock(CapturesArguments.class);
        Method method = createMock(Method.class);
        Object[] rawArguments = new Object[]{new Object()};
        int indexOfVararg = rawArguments.length - 1;

        expect(invocation.getMethod()).andReturn(method);
        expect(invocation.getRawArguments()).andReturn(rawArguments);
        expect(method.isVarArgs()).andReturn(true);

        for (int position = 0; position < indexOfVararg; position++) {
          expect(matchers.get(position)).andReturn(capturesArguments);
          capturesArguments.captureFrom(invocation.getArgumentAt(position, Object.class));
        }

        for (int position = indexOfVararg; position < matchers.size(); position++) {
          expect(matchers.get(position)).andReturn(capturesArguments);
          capturesArguments.captureFrom(invocation.getRawArguments()[position - indexOfVararg]);
        }

        replay(invocation, matchers, capturesArguments, method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation, matchers, capturesArguments, method);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = createMock(Invocation.class);
        Invocation invocation2 = createMock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        InvocationMatcher invocationMatcher1 = createMock(InvocationMatcher.class);
        InvocationMatcher invocationMatcher2 = createMock(InvocationMatcher.class);

        expectNew(InvocationMatcher.class, invocation1).andReturn(invocationMatcher1);
        expectNew(InvocationMatcher.class, invocation2).andReturn(invocationMatcher2);

        replayNew(InvocationMatcher.class, invocation1, invocation2);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertEquals(Arrays.asList(invocationMatcher1, invocationMatcher2), result);

        verifyNew(InvocationMatcher.class, invocation1, invocation2);
    }
}