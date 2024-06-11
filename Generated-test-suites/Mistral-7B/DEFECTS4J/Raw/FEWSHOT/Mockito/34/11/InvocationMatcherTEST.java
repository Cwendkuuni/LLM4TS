package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithSizes;
import static org.mockito.internal.matchers.ArgumentsMatcher.instanceOf;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.value;
import static org.mockito.internal.matchers.ArgumentsMatcher.variable;
import static org.mockito.internal.reporting.PrintSettings.printEmpty;
import static org.mockito.internal.reporting.PrintSettings.printJavaType;
import static org.mockito.internal.reporting.PrintSettings.printLocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation.InvocationToStringBuilder;
import org.mockito.internal.util.CollectionsUtil;
import org.mockito.internal.util.reflection.FieldAccess;
import org.mockito.internal.util.reflection.ReflectionUtil;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatchers;
import org.mockito.internal.invocation.InvocationType;
import org.mockito.internal.invocation.InvocationWithArgs;
import org.mockito.internal.reporting.PrintSettings.PrintMode;
import org.mockito.internal.util.args.Arguments;
import org.mockito.internal.util.args.ArgumentsMatcher;
import org.mockito.internal.util.args.ArgumentsMatcher.ArgumentsMatcherWithSizes;
import org.mockito.internal.util.args.ArgumentsMatcher.ArgumentsMatcherWithVariable;
import org.mockito.internal.util.args.ArgumentsMatcher.ArgumentsMatcherWithValue;
import org.mockito.internal.util.args.CapturingInvocation;
import org.mockito.internal.util.args.CapturingInvocation.CapturingInvocationWithArgs;
import org.mockito.internal.util.args.Invocation;
import org.mockito.internal.util.args.MethodMatcher;
import org.mockito.internal.util.args.MethodParameterMatcher;
import org.mockito.internal.util.args.ObjectArgumentMatcher;
import org.mockito.internal.util.args.StringArgumentMatcher;
import org.mockito.internal.util.reflection.FieldAccess.PrivateFieldAccess;

public class InvocationMatcherTest {

    private static final String CLASS_NAME = "InvocationMatcherTest";
    private static final String METHOD_NAME_1 = "testHasSimilarMethod";
    private static final String METHOD_NAME_2 = "testToString";
    private static final String METHOD_NAME_3 = "testMatches";
    private static final String METHOD_NAME_4 = "testHasSameMethod";
    private static final String METHOD_NAME_5 = "testCaptureArgumentsFrom";
    private static final String METHOD_NAME_6 = "testSafelyArgumentsMatch";
    private static final String METHOD_NAME_7 = "testHasSimilarMethodWithNullMethods";

    private InvocationMatcher invocationMatcher;
    private CapturingInvocation invocation;
    private CapturingInvocation candidateInvocation;
    private InvocationType invocationType;
    private Method method;
    private Location location;
    private List<Matcher> matchers;
    private PrintSettings printSettings;

    @Before
    public void setUp() {
        invocation = CapturingInvocationWithArgs.create(Mockito.mock(Object.class), method("method1", String.class, int.class), new String[]{"arg1", "arg2"}, new int[]{1, 2});
        candidateInvocation = CapturingInvocationWithArgs.create(Mockito.mock(Object.class), method("method1", String.class, int.class), new String[]{"arg1", "arg3"}, new int[]{1, 3});
        invocationType = InvocationType.INVOCATION_WITH_ARGS;
        method = ReflectionUtil.getMethod(InvocationMatcher.class, "hasSameMethod", Invocation.class);
        location = new Location(new Object(){}.getClass().getName(), METHOD_NAME_1, 0);
        matchers = Collections.singletonList(value(1));
        printSettings = new PrintSettings(PrintMode.PRETTY_PRINT, printEmpty(), printJavaType(), printLocation());
    }

    @Test
    public void testHasSimilarMethod() {
        assertTrue(invocationMatcher(invocation, candidateInvocation, invocationType, matchers, location).hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testToString() {
        String expectedToString = "InvocationMatcher{invocation=Invocation{method=public java.lang.String org.mockito.internal.invocation.InvocationMatcherTest.method1(java.lang.String, int)}, matchers=[[java.lang.reflect.Matchers.is(1)]]}";
        String actualToString = invocationMatcher(invocation, null, invocationType, matchers, printSettings).toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testMatches() {
        assertTrue(invocationMatcher(invocation, invocation, invocationType, matchers, location).matches(invocation));
    }

    @Test
    public void testHasSameMethod() {
        assertTrue(invocationMatcher(invocation, invocation, invocationType, matchers, location).hasSameMethod(invocation));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        CapturingInvocation capturedInvocation = new CapturingInvocation(Mockito.mock(Object.class));

        invocationMatcher(invocation, capturedInvocation, invocationType, matchers, location).captureArgumentsFrom(capturedInvocation);

        assertArrayEquals(invocation.getArguments(), capturedInvocation.getArguments());
    }

    @Test
    public void testSafelyArgumentsMatch() {
        assertTrue(invocationMatcher(invocation, invocation, invocationType, matchers, location).safelyArgumentsMatch(invocation.getArguments()));
    }

    @Test
    public void testHasSimilarMethodWithNullMethods() {
        Invocation nullInvocation = Mockito.mock(Invocation.class);
        assertFalse(invocationMatcher(invocation, nullInvocation, invocationType, matchers, location).hasSimilarMethod(nullInvocation));
    }

    private InvocationMatcher invocationMatcher(Invocation invocation, Invocation candidateInvocation, InvocationType invocationType, List<Matcher> matchers, PrintSettings printSettings) {
        return new InvocationMatcher(ReflectionUtil.getProxyAccess(invocation), matchers);
    }
}

class MethodMatcherTest {

    @Test
    public void testIsInstanceOf() {
        MethodParameterMatcher methodParameterMatcher = new MethodParameterMatcher(method("method1", String.class, int.class), 0);
        assertTrue(methodParameterMatcher.matches(String.class));
    }

    @Test
    public void testIsVariable() {
        MethodParameterMatcher methodParameterMatcher = new MethodParameterMatcher(method("method1", String.class, int.class), 1);
        assertTrue(methodParameterMatcher.isVariable());
    }

    @Test
    public void testIsValue() {
        MethodParameterMatcher methodParameterMatcher = new MethodParameterMatcher(method("method1", String.class, int.class), 0);
        assertFalse(methodParameterMatcher.isValue());
    }

    @Test
    public void testIsCaptured() {
        MethodParameterMatcher methodParameterMatcher = new MethodParameterMatcher(method("method1", String.class, int.class), 1);
        assertTrue(methodParameterMatcher.isCaptured());
    }

    @Test
    public void testMatches() {
        MethodParameterMatcher methodParameterMatcher = new MethodParameterMatcher(method("method1", String.class, int.class), 0);
        assertTrue(methodParameterMatcher.matches(new String("foo")));
    }

    @Test
    public void testMatchesWithVariable() {
        MethodParameterMatcher methodParameterMatcher = new MethodParameterMatcher(method("method1", String.class, int.class), 1);
        ObjectArgumentMatcher objectArgumentMatcher = new ObjectArgumentMatcher("arg");
        ArgumentMatcherWithValue argumentMatcherWithValue = new ArgumentMatcherWithValue(objectArgumentMatcher);
        MethodMatcher methodMatcher = new MethodMatcher(method("method1", String.class, int.class), 1);
        ArgumentMatcherWithVariable argumentMatcherWithVariable = new ArgumentMatcherWithVariable("arg");
        methodMatcher.addMatcher(argumentMatcherWithValue);
        methodMatcher.addMatcher(argumentMatcherWithVariable);
        assertTrue(methodMatcher.matches(new String("foo"), new String("bar")));
    }

    @Test
    public void testMatchesWithCaptured() {
        MethodParameterMatcher methodParameterMatcher = new MethodParameterMatcher(method("method1", String.class, int.class), 1);
        Whitebox whitebox = Whitebox.getInternalState(CapturingInvocation.class, "capturedArguments");
        PrivateFieldAccess capturedArgumentsFieldAccess = new PrivateFieldAccess(whitebox, "capturedArguments");
        ObjectArgumentMatcher objectArgumentMatcher = new ObjectArgumentMatcher("arg");
        ArgumentMatcherWithValue argumentMatcherWithValue = new ArgumentMatcherWithValue(objectArgumentMatcher);
        MethodMatcher methodMatcher = new MethodMatcher(method("method1", String.class, int.class), 1);
        ArgumentMatcherWithCaptures argumentMatcherWithCaptures = new ArgumentMatcherWithCaptures("arg");
        methodMatcher.addMatcher(argumentMatcherWithValue);
        methodMatcher.addMatcher(argumentMatcherWithCaptures);
        Invocation invocation = new InvocationWithArgs(Mockito.mock(Object.class), method("method1", String.class, int.class), new Object[]{new String("foo"), 1});
        Object capturedArgument = capturedArgumentsFieldAccess.get(invocation);
        assertTrue(methodMatcher.matches(capturedArgument, new String("bar")));
    }

    @Test
    public void testMatchesWithArgumentsMatcherWithSizes() {
        ArgumentMatcherWithSizes argumentsMatcherWithSizes = new ArgumentMatcherWithSizes(argumentsWithSizes(String.class, 2));
        MethodMatcher methodMatcher = new MethodMatcher(method("method1", String.class, int.class), 0);
        methodMatcher.addMatcher(argumentsMatcherWithSizes);
        assertTrue(methodMatcher.matches(new String[]{"foo", "bar"}, new String[]{"foo", "bar"}));
    }

    private Method method(String name, Class<?>... parameterTypes) {
        return ReflectionUtil.getMethod(Object.class, name, parameterTypes);
    }
}

class ArgumentsMatcherTest {

    @Test
    public void testArgumentsWithSizes() {
        String[] expected = new String[]{"foo", "bar"};
        String[] actual = new String[]{"foo", "bar"};
        ArgumentsMatcherWithSizes argumentsMatcherWithSizes = new ArgumentsMatcherWithSizes(argumentsWithSizes(String.class, 2));
        assertTrue(argumentsMatcherWithSizes.matches(expected, actual));
    }

    @Test
    public void testInstanceOf() {
        Class<?> expectedType = String.class;
        Object expected = new String("foo");
        Object actual = new String("bar");
        ArgumentMatcherWithValue argumentMatcherWithValue = new ArgumentMatcherWithValue(value(expected));
        assertTrue(argumentMatcherWithValue.matches(expectedType, actual));
    }

    @Test
    public void testVariable() {
        String variable = "arg";
        Object expected = new String("foo");
        Object actual = new String("bar");
        ArgumentMatcherWithVariable argumentMatcherWithVariable = new ArgumentMatcherWithVariable(variable);
        assertTrue(argumentMatcherWithVariable.matches(expected, actual));
    }

    @Test
    public void testValue() {
        Object expected = new String("foo");
        Object actual = new String("foo");
        ArgumentMatcherWithValue argumentMatcherWithValue = new ArgumentMatcherWithValue(value(expected));
        assertTrue(argumentMatcherWithValue.matches(expected, actual));
    }

    @Test
    public void testCaptures() {
        String variable = "arg";
        Object expected = new String("foo");
        Object actual = new String("bar");
        ArgumentMatcherWithCaptures argumentMatcherWithCaptures = new ArgumentMatcherWithCaptures(variable);
        assertTrue(argumentMatcherWithCaptures.matches(expected, actual));
    }

    @Test
    public void testInvocationToStringBuilder() {
        String expectedToString = "ArgumentsMatcher{matcher=[java.lang.reflect.Matchers.is(java.lang.String)]}";
        String actualToString = new InvocationToStringBuilder(ReflectionUtil.getProxyAccess(MockitoJUnitRunner.class), "matchers", new Object[]{new ArgumentsMatcherWithValue(value(new String("foo")))}, printSettings).toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testEquals() {
        ArgumentMatcherWithValue argumentMatcherWithValue1 = new ArgumentMatcherWithValue(value(new String("foo")));
        ArgumentMatcherWithValue argumentMatcherWithValue2 = new ArgumentMatcherWithValue(value(new String("foo")));
        assertTrue(argumentMatcherWithValue1.equals(argumentMatcherWithValue2));
    }

    @Test
    public void testHashCode() {
        ArgumentMatcherWithValue argumentMatcherWithValue = new ArgumentMatcherWithValue(value(new String("foo")));
        int expectedHashCode = 31 * 1 + "foo".hashCode();
        int actualHashCode = argumentMatcherWithValue.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    private Arguments argumentsWithSizes(Class<?>... parameterTypes) {
        return new Arguments(ReflectionUtil.getMethod(Object.class, "toString", new Class[0]), new ArgumentsMatcher[]{CollectionsUtil.toArray(new ArgumentsMatcher[0], new ArgumentsMatcher[]{new ArgumentsMatcherWithSizes(value(new String[0]))})}, parameterTypes);
    }
}

class PrintSettingsTest {

    @Test
    public void testPrintEmpty() {
        String expected = "";
        String actual = printSettings(PrintSettings.printEmpty()).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintJavaType() {
        String expected = "java.lang.String";
        String actual = printSettings(PrintSettings.printJavaType(new PrintSettings()), "java.lang.String").toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintLocation() {
        String expected = "InvocationMatcherTest.method1(java.lang.String, int) @ org.mockito.internal.invocation.InvocationMatcherTest:22";
        String actual = printSettings(PrintSettings.printLocation(new PrintSettings(), CLASS_NAME, METHOD_NAME_1, 22)).toString();
        assertEquals(expected, actual);
    }

    private PrintSettings printSettings(PrintSettings printSettings) {
        return new PrintSettings(PrintSettings.printEmpty(), PrintSettings.printJavaType(printSettings, "java.lang.String"), PrintSettings.printLocation(printSettings, CLASS_NAME, METHOD_NAME_1, 22));
    }
}