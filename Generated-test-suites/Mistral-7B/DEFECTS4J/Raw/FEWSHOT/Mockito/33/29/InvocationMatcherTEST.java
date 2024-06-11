package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgThat.argThat;
import static org.mockito.internal.reporting.PrintSettings.PRETTY_PRINT;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsSame;
import org.junit.Test;
import org.mockito.exceptions.InvocationNotRecordedException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

public class InvocationMatcherTest {

    private static final Class<?> CLASS_GERANIUM = Object.class;
    private static final Class<?> CLASS_ROSE = Object.class;
    private static final Class<?> CLASS_BUTTERFLY = Object.class;
    private static final Class<?> CLASS_FLOWER = Object.class;

    private static final String METHOD_NAME_GET_COLOR = "getColor";
    private static final String METHOD_NAME_FOLD = "fold";
    private static final String METHOD_NAME_EAT = "eat";

    private static final Object[] EMPTY_ARGUMENTS = new Object[0];

    @Test
    public void testToStringWithEmptyMatcherList() {
        // given
        Invocation invocation = new InvocationOnMock(new Object(), new Method(getClass(), METHOD_NAME_GET_COLOR), EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        String printSettingsString = PRETTY_PRINT.toString();
        String expectedToString = String.format("InvocationMatcher [invocation=%s, matchers=[]]", invocation);

        // then
        String toString = invocationMatcher.toString(new PrintSettings(printSettingsString));
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testToStringWithCaptureArgumentsMatcher() {
        // given
        Invocation invocation = new InvocationOnMock(new Object(), new Method(getClass(), METHOD_NAME_GET_COLOR), new Object[]{"geranium"});
        CapturesArguments capturesArguments = new CapturesArguments<Object>(Object.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argThat(new IsInstanceOf<CapturesArguments>(CapturesArguments.class)), capturesArguments));

        // when
        String printSettingsString = PRETTY_PRINT.toString();
        String expectedToString = String.format("InvocationMatcher [invocation=%s, matchers=[%s]]", invocation, capturesArguments);

        // then
        String toString = invocationMatcher.toString(new PrintSettings(printSettingsString));
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        // given
        Method method = new Method(getClass(), METHOD_NAME_GET_COLOR);
        Invocation invocation1 = new InvocationOnMock(new Object(), method, EMPTY_ARGUMENTS);
        Invocation invocation2 = new InvocationOnMock(new Object(), method, EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // when
        boolean hasSameMethod = invocationMatcher.hasSameMethod(new InvocationMatcher(invocation2));

        // then
        assertTrue(hasSameMethod);
    }

    @Test
    public void testHasSameMethodWithDifferentMethod() {
        // given
        Method methodGeraniumGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_GERANIUM});
        Method methodRoseGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_ROSE});
        Invocation invocation1 = new InvocationOnMock(new Object(), methodGeraniumGetColor, new Object[]{"geranium"});
        Invocation invocation2 = new InvocationOnMock(new Object(), methodRoseGetColor, EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // when
        boolean hasSameMethod = invocationMatcher.hasSameMethod(new InvocationMatcher(invocation2));

        // then
        assertFalse(hasSameMethod);
    }

    @Test
    public void testHasSameMethodWithUnverifiedDifferentMethod() {
        // given
        Method methodGeraniumGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_GERANIUM});
        Method methodRoseGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_ROSE});
        Invocation invocation1 = new InvocationOnMock(new Object(), methodGeraniumGetColor, new Object[]{"geranium"});
        Invocation invocation2 = new InvocationOnMock(new Object(), methodRoseGetColor, EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        boolean hasSameMethod = invocationMatcher1.hasSimilarMethod(new InvocationMatcher(invocation2));

        // then
        assertTrue(hasSameMethod);
    }

    @Test
    public void testMatchesWithSameInvocation() {
        // given
        Method method = new Method(getClass(), METHOD_NAME_GET_COLOR);
        Invocation invocation = new InvocationOnMock(new Object(), method, EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        boolean matches = invocationMatcher.matches(invocation);

        // then
        assertTrue(matches);
    }

    @Test
    public void testMatchesWithDifferentInvocationSameMethod() {
        // given
        Method method = new Method(getClass(), METHOD_NAME_GET_COLOR);
        Invocation invocation1 = new InvocationOnMock(new Object(), method, new Object[]{"geranium"});
        Invocation invocation2 = new InvocationOnMock(new Object(), method, EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // when
        boolean matches = invocationMatcher.matches(invocation2);

        // then
        assertFalse(matches);
    }

    @Test
    public void testMatchesWithDifferentInvocationDifferentMethod() {
        // given
        Method methodGeraniumGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_GERANIUM});
        Method methodRoseGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_ROSE});
        Invocation invocation1 = new InvocationOnMock(new Object(), methodGeraniumGetColor, new Object[]{"geranium"});
        Invocation invocation2 = new InvocationOnMock(new Object(), methodRoseGetColor, EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // when
        boolean matches = invocationMatcher.matches(invocation2);

        // then
        assertFalse(matches);
    }

    @Test
    public void testMatchesWithDifferentInvocationOverloadedMethod() {
        // given
        Method methodGeraniumGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_GERANIUM});
        Method methodRoseGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_ROSE});
        Invocation invocation1 = new InvocationOnMock(new Object(), methodGeraniumGetColor, new Object[]{"geranium"});
        Invocation invocation2 = new InvocationOnMock(new Object(), methodRoseGetColor, EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // when
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        boolean matches = invocationMatcher.matches(invocationMatcher2);

        // then
        assertFalse(matches);
    }

    @Test
    public void testMatchesWithDifferentInvocationOverloadedMethodAndSameArguments() {
        // given
        Method methodGeraniumGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_GERANIUM});
        Method methodRoseGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_ROSE});
        Invocation invocation1 = new InvocationOnMock(new Object(), methodGeraniumGetColor, new Object[]{"geranium"});
        Invocation invocation2 = new InvocationOnMock(new Object(), methodRoseGetColor, new Object[]{"geranium"});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        // when
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        boolean matches = invocationMatcher1.matches(invocationMatcher2);

        // then
        assertTrue(matches);
    }

    @Test(expected = InvocationNotRecordedException.class)
    public void testMatchesWithUnverifiedDifferentInvocation() {
        // given
        Method methodGeraniumGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_GERANIUM});
        Method methodRoseGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_ROSE});
        Invocation invocation1 = new InvocationOnMock(new Object(), methodGeraniumGetColor, EMPTY_ARGUMENTS);
        Invocation invocation2 = new InvocationOnMock(new Object(), methodRoseGetColor, EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        // when
        boolean matches = invocationMatcher1.matches(new InvocationMatcher(invocation2));

        // then
        throw new InvocationNotRecordedException("Invocation not recorded");
    }

    @Test
    public void testCreateFrom() {
        // given
        List<Invocation> invocations = new LinkedList<>();
        Invocation invocation1 = new InvocationOnMock(new Object(), new Method(getClass(), METHOD_NAME_GET_COLOR), new Object[]{"geranium"});
        Invocation invocation2 = new InvocationOnMock(new Object(), new Method(getClass(), METHOD_NAME_GET_COLOR), new Object[]{"rose"});
        invocations.add(invocation1);
        invocations.add(invocation2);

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        assertEquals(2, invocationMatchers.size());

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertTrue(invocationMatcher1.matches(invocation1));
        assertTrue(invocationMatcher2.matches(invocation2));
        assertFalse(invocationMatcher1.matches(invocation2));
        assertFalse(invocationMatcher2.matches(invocation1));
    }

    @Test
    public void testCreateFromWithEmptyInvocationList() {
        // given
        List<Invocation> invocations = Collections.emptyList();

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        assertTrue(invocationMatchers.isEmpty());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Invocation invocation = new InvocationOnMock(new Object(), new Method(getClass(), METHOD_NAME_GET_COLOR), new Object[]{"geranium"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object capturedArgument = new Object();

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        CapturesArguments capturesArguments = (CapturesArguments) invocationMatcher.getMatchers().get(0);
        assertEquals(capturedArgument, capturesArguments.getValue());
    }

    @Test
    public void testCaptureArgumentsFromWithMultipleMatchers() {
        // given
        Invocation invocation = new InvocationOnMock(new Object(), new Method(getClass(), METHOD_NAME_GET_COLOR), new Object[]{"geranium", "butterfly"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argThat(new IsInstanceOf(InvocationMatcher.class)), argThat(IsSame.instance(this))));

        // when
        Object capturedArgument = new Object();
        invocationMatcher.getMatchers().get(1).captureArgumentsFrom(invocation);

        // then
        CapturesArguments capturesArguments = (CapturesArguments) invocationMatcher.getMatchers().get(1);
        assertEquals(capturedArgument, capturesArguments.getValue());
    }

    @Test
    public void testCaptureArgumentsFromWithNullInvocation() {
        // given
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), (Method)null, EMPTY_ARGUMENTS));

        // when
        invocationMatcher.captureArgumentsFrom(null);

        // then
        // empty
    }

    @Test
    public void testCreateMethodStubWithSameMethod() {
        // given
        Method method = new Method(getClass(), METHOD_NAME_GET_COLOR);
        Invocation invocation = new InvocationOnMock(new Object(), method, EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation);
        Stubber stubber = mock(Stubber.class);

        // when
        Answer answer = invocationMatcher.createAnswer(stubber);

        // then
        assertEquals(answer, invocationMatcher2.createAnswer(stubber));
    }

    @Test
    public void testCreateMethodStubWithDifferentMethod() {
        // given
        Method methodGeraniumGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_GERANIUM});
        Method methodRoseGetColor = new Method(getClass(), METHOD_NAME_GET_COLOR, new Class[]{CLASS_ROSE});
        Invocation invocation1 = new InvocationOnMock(new Object(), methodGeraniumGetColor, new Object[]{"geranium"});
        Invocation invocation2 = new InvocationOnMock(new Object(), methodRoseGetColor, EMPTY_ARGUMENTS);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        Stubber stubber = mock(Stubber.class);

        // when
        Answer answer1 = invocationMatcher1.createAnswer(stubber);
        Answer answer2 = invocationMatcher2.createAnswer(stubber);

        // then
        assertNotSame(answer1, answer2);
    }

    private void assertEquals(String expected, String actual) {
        assertEquals(expected, actual);
    }
}