import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.invocation.ArgumentsComparator;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Test
    public void testConstructorWithMatchersEmpty() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<Matcher>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.getMethod().equals(invocation.getMethod()));
        assertTrue(invocationMatcher.getInvocation().equals(invocation));
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithMatchersNotEmpty() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new Matcher());
        matchers.add(new Matcher());
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.getMethod().equals(invocation.getMethod()));
        assertTrue(invocationMatcher.getInvocation().equals(invocation));
        assertTrue(invocationMatcher.getMatchers().equals(matchers));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = new Invocation();
        invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.getMethod().equals(invocation.getMethod()));
        assertTrue(invocationMatcher.getInvocation().equals(invocation));
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.getMethod().equals(invocation.getMethod()));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation();
        invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.getInvocation().equals(invocation));
    }

    @Test
    public void testGetMatchersEmpty() {
        Invocation invocation = new Invocation();
        invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.toString().equals(new PrintSettings().print(invocationMatcher.getMatchers(), invocationMatcher.getInvocation())));
    }

    @Test
    public void testMatchesTrue() {
        Invocation invocation = new Invocation();
        Object[] args = new Object[1];
        invocation.setArguments(args);
        invocation.setMock("mock");
        Invocation actual = new Invocation();
        actual.setArguments(args);
        actual.setMock("mock");
        invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesFalse() {
        Invocation invocation = new Invocation();
        Object[] args = new Object[1];
        invocation.setArguments(args);
        invocation.setMock("mock1");
        Invocation actual = new Invocation();
        actual.setArguments(args);
        actual.setMock("mock2");
        invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new Invocation();
        Object[] args = new Object[1];
        invocation.setArguments(args);
        Invocation actual = new Invocation();
        actual.setArguments(args);
        invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.safelyArgumentsMatch(actual.getRawArguments()));
    }

    @Test
    public void testHasSimilarMethodTrue() {
        Invocation invocation = new Invocation();
        invocation.setMethod("methodName");
        invocation.setVerified(false);
        invocation.setMock("mock");
        Invocation candidate = new Invocation();
        candidate.setMethod("methodName");
        candidate.setVerified(false);
        candidate.setMock("mock");
        invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodMethodNameEqualsFalse() {
        Invocation invocation = new Invocation();
        invocation.setMethod("methodName1");
        invocation.setVerified(false);
        invocation.setMock("mock");
        Invocation candidate = new Invocation();
        candidate.setMethod("methodName2");
        candidate.setVerified(false);
        candidate.setMock("mock");
        invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodIsVerifiedFalse() {
        Invocation invocation = new Invocation();
        invocation.setMethod("methodName");
        invocation.setVerified(true);
        invocation.setMock("mock");
        Invocation candidate = new Invocation();
        candidate.setMethod("methodName");
        candidate.setVerified(false);
        candidate.setMock("mock");
        invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodMockIsTheSameFalse() {
        Invocation invocation = new Invocation();
        invocation.setMethod("methodName");
        invocation.setVerified(false);
        invocation.setMock("mock1");
        Invocation candidate = new Invocation();
        candidate.setMethod("methodName");
        candidate.setVerified(false);
        candidate.setMock("mock2");
        invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodMethodEqualsFalse() {
        Invocation invocation = new Invocation();
        invocation.setMethod("methodName");
        invocation.setVerified(false);
        invocation.setMock("mock");
        Invocation candidate = new Invocation();
        candidate.setMethod("methodName");
        candidate.setVerified(false);
        candidate.setMock("mock");
        invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodOverloadedButSameArgs() {
        Invocation invocation = new Invocation();
        Object[] args = new Object[1];
        invocation.setArguments(args);
        Invocation candidate = new Invocation();
        candidate.setArguments(args);
        invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethodTrue() throws NoSuchMethodException {
        Invocation invocation = new Invocation();
        MethodInvocation methodInvocation = new MethodInvocation();
        methodInvocation.setMethod("methodName");
        invocation.setMethod(methodInvocation);
        Invocation candidate = new Invocation();
        candidate.setMethod(methodInvocation);
        invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodNameNotEqual() throws NoSuchMethodException {
        Invocation invocation = new Invocation();
        MethodInvocation methodInvocation = new MethodInvocation();
        methodInvocation.setMethod("methodName1");
        invocation.setMethod(methodInvocation);
        Invocation candidate = new Invocation();
        candidate.setMethod(methodInvocation);
        invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodParamLengthNotEqual() throws NoSuchMethodException {
        Invocation invocation = new Invocation();
        MethodInvocation methodInvocation = new MethodInvocation();
        methodInvocation.setMethod("methodName");
        methodInvocation.setParameterTypes(new Class[2]);
        invocation.setMethod(methodInvocation);
        Invocation candidate = new Invocation();
        candidate.setMethod(methodInvocation);
        invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodParamsNotEqual() throws NoSuchMethodException {
        Invocation invocation = new Invocation();
        MethodInvocation methodInvocation = new MethodInvocation();
        methodInvocation.setMethod("methodName");
        methodInvocation.setParameterTypes(new Class[1]);
        methodInvocation.getParameterTypes()[0] = new Class();
        invocation.setMethod(methodInvocation);
        Invocation candidate = new Invocation();
        candidate.setMethod(methodInvocation);
        invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.getLocation().equals(invocation.getLocation()));
    }

    @Test
    public void testCaptureArgumentsFromVarargsNotSupported() {
        Invocation invocation = new Invocation();
        invocation.setMethod("methodName");
        invocation.setArguments(new Object[1]);
        invocation.getRawArguments()[0] = new Object[1];
        invocation.getMethod().setVarArgs(true);
        invocation.setParameterTypes(new Class[1]);
        invocation.getParameterTypes()[0] = new Class();
        invocationMatcher = new InvocationMatcher(invocation);

        try {
            invocationMatcher.captureArgumentsFrom(invocation);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        Object[] args = new Object[1];
        invocation.setArguments(args);
        InvocationMatcher capturerInvocationMatcher = new InvocationMatcher();
        capturerInvocationMatcher.getMatchers().add(new Matcher());
        invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMatchers().add(capturerInvocationMatcher);

        invocationMatcher.captureArgumentsFrom(invocation);

        assertTrue(((CapturesArguments)capturerInvocationMatcher.getMatchers().get(0)).captureFrom(args[0]));
    }

    @Test
    public void testIsVarargMatcherTrue() {
        InvocationMatcher invocationMatcher = new InvocationMatcher();
        Matcher matcher = new VarargMatcher();

        assertTrue(invocationMatcher.isVarargMatcher(matcher));
    }

    @Test
    public void testIsVarargMatcherFalse() {
        InvocationMatcher invocationMatcher = new InvocationMatcher();
        Matcher matcher = new Matcher();

        assertFalse(invocationMatcher.isVarargMatcher(matcher));

        MatcherDecorator matcherDecorator = new MatcherDecorator();
        matcherDecorator.setActualMatcher(matcher);
        assertFalse(invocationMatcher.isVarargMatcher(matcherDecorator));
    }

    @Test
    public void testIsVariableArgumentTrue() {
        InvocationMatcher invocationMatcher = new InvocationMatcher();
        Invocation invocation = new Invocation();
        invocation.setMethod("methodName");
        invocation.setArguments(new Object[2]);
        invocation.getRawArguments()[1] = new Object[1];
        invocation.getMethod().setVarArgs(true);
        invocation.setParameterTypes(new Class[2]);
        invocation.getParameterTypes()[1] = new Class();
        int position = 1;

        assertTrue(invocationMatcher.isVariableArgument(invocation, position));
    }

    @Test
    public void testIsVariableArgumentFalse() {
        InvocationMatcher invocationMatcher = new InvocationMatcher();
        Invocation invocation = new Invocation();
        invocation.setMethod("methodName");
        invocation.setArguments(new Object[1]);
        invocation.getMethod().setVarArgs(true);
        invocation.setParameterTypes(new Class[1]);
        invocation.getParameterTypes()[0] = new Class();
        int position = 0;

        assertFalse(invocationMatcher.isVariableArgument(invocation, position));
    }

    @Test
    public void testCreateFromEmpty() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        List<InvocationMatcher> expected = new LinkedList<InvocationMatcher>();
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);

        assertTrue(actual.equals(expected));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        MethodInvocation methodInvocation = new MethodInvocation();
        methodInvocation.setMethod("methodName");
        Invocation invocation = new Invocation();
        invocation.setMethod(methodInvocation);
        invocations.add(invocation);
        List<InvocationMatcher> expected = new LinkedList<InvocationMatcher>();
        InvocationMatcher invocationMatcher = new InvocationMatcher();
        MethodInvocation methodInvocation = new MethodInvocation();
        methodInvocation.setMethod("methodName");
        invocationMatcher.setMethod(methodInvocation);
        expected.add(invocationMatcher);
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);

        assertTrue(actual.equals(expected));
    }
}