import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcherBuilders;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.invocation.MatchersBinderForRegularMethod;
import org.mockito.internal.invocation.MockAwareVerificationMode;
import org.mockito.internal.invocation.mockref.MockHandlerReference;
import org.mockito.internal.invocation.mockref.MockReference;
import org.mockito.internal.matchers.AllInvocationsFinder;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.CapturesArgumentsFromInvocation;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Find;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.ReflectiveToStringMatcher;
import org.mockito.internal.matchers.Same;
import org.mockito.internal.matchers.TextMatcher;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.stubbing.defaultanswers.LenientStubber;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationFactory;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = Mockito.mock(Invocation.class);
        matchers = Arrays.asList(
                Mockito.mock(Matcher.class),
                Mockito.mock(Matcher.class),
                Mockito.mock(Matcher.class)
        );
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(expectedMethod);
        Assert.assertEquals(expectedMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        String expectedToString = "InvocationMatcherToString";
        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        Mockito.when(printSettings.print(Mockito.any(List.class), Mockito.any(Invocation.class))).thenReturn(expectedToString);
        Whitebox.setInternalState(invocationMatcher, "printSettings", printSettings);
        Assert.assertEquals(expectedToString, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(actual.getMock()).thenReturn("mock");
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(actual.getMethod()).thenReturn(Mockito.mock(Method.class));
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(Mockito.any(InvocationMatcher.class), Mockito.any(Invocation.class))).thenReturn(true);
        Whitebox.setInternalState(invocationMatcher, "argumentsComparator", argumentsComparator);
        Assert.assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(Mockito.any(InvocationMatcher.class), Mockito.any(Object[].class))).thenReturn(true);
        Whitebox.setInternalState(invocationMatcher, "argumentsComparator", argumentsComparator);
        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(null));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(candidate.getMock()).thenReturn("mock");
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(Mockito.any(InvocationMatcher.class), Mockito.any(Object[].class))).thenReturn(true);
        Whitebox.setInternalState(invocationMatcher, "argumentsComparator", argumentsComparator);
        Assert.assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(method1.getName()).thenReturn("MethodName");
        Mockito.when(method2.getName()).thenReturn("MethodName");
        Mockito.when(method1.getParameterTypes()).thenReturn(new Class<?>[]{});
        Mockito.when(method2.getParameterTypes()).thenReturn(new Class<?>[]{});
        Assert.assertTrue(invocationMatcher.hasSameMethod(Mockito.mock(Invocation.class)));
    }

    @Test
    public void testCaptureArgumentsFromVararg() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments = {1, 2, 3, "vararg"};
        Object[] varargs = {4, 5};
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getRawArguments()).thenReturn(arguments);
        Mockito.when(method.isVarArgs()).thenReturn(true);
        Mockito.when(invocation.getArgumentAt(Mockito.eq(3), Mockito.eq(Object.class))).thenReturn(varargs);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        Whitebox.setInternalState(invocationMatcher, "matchers", Collections.singletonList(capturesArguments));
        Whitebox.setInternalState(invocationMatcher, "matchersBinder", matchersBinder);
        invocationMatcher.captureArgumentsFrom(invocation);
        Mockito.verify(capturesArguments, Mockito.times(2)).captureFrom(Mockito.any());
    }

    @Test
    public void testCaptureArgumentsFromRegular() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments = {1, "regular"};
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getRawArguments()).thenReturn(arguments);
        int position = 0;
        Object argument = 1;
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        MatchersBinderForRegularMethod matchersBinder = Mockito.mock(MatchersBinderForRegularMethod.class);
        Whitebox.setInternalState(invocationMatcher, "matchers", Collections.singletonList(capturesArguments));
        Whitebox.setInternalState(invocationMatcher, "matchersBinder", matchersBinder);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(Method.class), Mockito.anyList())).thenReturn(new Object[]{argument});
        invocationMatcher.captureArgumentsFrom(invocation);
        Mockito.verify(capturesArguments, Mockito.times(1)).captureFrom(Mockito.eq(argument));
    }

    @Test
    public void testCaptureArgumentsFromRegularWithVarargs() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments = {1, 2, 3, "regular", 4, 5};
        Object[] varargs = {4, 5};
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getRawArguments()).thenReturn(arguments);
        Mockito.when(method.isVarArgs()).thenReturn(true);
        Mockito.when(invocation.getRawArguments()).thenReturn(arguments);
        Mockito.when(invocation.getArgumentAt(Mockito.eq(3), Mockito.eq(Object.class))).thenReturn(varargs);
        int position = 0;
        Object argument = 1;
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        MatchersBinderForRegularMethod matchersBinder = Mockito.mock(MatchersBinderForRegularMethod.class);
        Whitebox.setInternalState(invocationMatcher, "matchers", Collections.singletonList(capturesArguments));
        Whitebox.setInternalState(invocationMatcher, "matchersBinder", matchersBinder);
        Mockito.when(matchersBinder.bindMatchers(Mockito.any(Method.class), Mockito.anyList())).thenReturn(new Object[]{argument});
        invocationMatcher.captureArgumentsFrom(invocation);
        Mockito.verify(capturesArguments, Mockito.times(1)).captureFrom(Mockito.eq(argument));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Collections.singletonList(Mockito.mock(Invocation.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        Assert.assertEquals(invocations.size(), invocationMatchers.size());
        Assert.assertEquals(invocations.get(0), invocationMatchers.get(0).getInvocation());
    }
}

