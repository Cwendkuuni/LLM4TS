package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito. Mockito;
import org.mockito.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argument;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersSizeIsNotEqualToArgumentsLength_thenReturnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));
        Object[] arguments = new Object[]{new Integer(1), new String("test")};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNull(result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatcherMatchesArgument_thenReturnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));
        Object[] arguments = new Object[]{1, "test"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNull(result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenToStringRepresentationMatchesButTypesAreDifferent_thenReturnsIndex() {
        List<Matcher> matchers = Arrays.asList(
                Mockito.mock(ContainsExtraTypeInformation.class),
                Mockito.mock(Matcher.class)
        );
        Object[] arguments = new Object[]{1, "1"};
        when(((ContainsExtraTypeInformation) matchers.get(0)).typeMatches(arguments[0]))
                .thenReturn(false);

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(new Integer[]{0}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenToStringRepresentationMatchesButTypesAreSame_thenReturnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(
                Mockito.mock(ContainsExtraTypeInformation.class),
                Mockito.mock(Matcher.class)
        );
        Object[] arguments = new Object[]{1, "1"};
        when(((ContainsExtraTypeInformation) matchers.get(0)).typeMatches(arguments[0]))
                .thenReturn(true);

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testSafelyMatches_whenMatcherThrowsException_thenThrowsRuntimeException() {
        Matcher m = Mockito.mock(Matcher.class);
        Object arg = new Integer(1);

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.safelyMatches(m, arg);
    }

    @Test
    public void testToStringEquals_whenToStringRepresentationAreEqual_thenReturnsTrue() {
        Matcher m = Mockito.mock(Matcher.class);
        Object arg = new Integer(1);

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(m, arg);

        verify(m).toString();
        verify(arg).toString();
        assertEquals("", result);
    }

    @Test
    public void testToStringEquals_whenToStringRepresentationAreNotEqual_thenReturnsFalse() {
        Matcher m = mock(Matcher.class);
        when(m.toString()).thenReturn("mockito test");
        Object arg = new Integer(1);

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(m, arg);

        verify(m).toString();
        verify(arg).toString();
        assertEquals(false, result);
    }

    @Test
    public void testToStringEquals_whenMatcherIsStringDescription_thenToStringRepresentationAreEqual() {
        Matcher m = new StringDescription();
        Object arg = new Integer(1);

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(m, arg);

        assertEquals("", result);
    }

    @Test
    public void testToStringEquals_whenMatcherIsStringDescription_andArgumentIsStringDescription_thenToStringRepresentationAreEqual() {
        Matcher m = new StringDescription();
        Object arg = new StringDescription();

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(m, arg);

        assertEquals("", result);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringEquals_whenArgumentIsNull_thenThrowsNullPointerException() {
        Matcher m = new StringDescription();
        Object arg = null;

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.toStringEquals(m, arg);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringEquals_whenMatcherIsNull_thenThrowsNullPointerException() {
        Object arg = new Integer(1);

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.toStringEquals(null, arg);
    }

    @Test
    public void testArgumentMatchingToolSpy_whenCalledWithMatcherAndArgument_thenCallsMatchesMethodOfMatcher() {
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));
        Object[] arguments = new Object[]{1, "test"};
        ArgumentMatchingTool tool = spy(new ArgumentMatchingTool());

        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        verify(matchers.get(0)).matches(arguments[0]);
        verify(matchers.get(1)).matches(arguments[1]);
    }

    @Test
    public void testArgumentMatchingToolSpy_whenCalledWithMatcherAndArgument_andMatcherThrowsException_thenDoesNotCallMatchesMethodAgain() {
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));
        Object[] arguments = new Object[]{1, "test"};
        Matcher m = Mockito.mock(Matcher.class);
        when(m.matches(Mockito.anyObject()))
                .thenThrow(new RuntimeException("test"));
        ArgumentMatchingTool tool = spy(new ArgumentMatchingTool());

        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        verify(matchers.get(0), Mockito.atLeast(1)).matches(Mockito.anyObject());
        verify(matchers.get(1), Mockito.never()).matches(Mockito.anyObject());
    }

    @Test
    public void testArgumentMatchingToolAnswer_whenCalledWithMatcherAndArgument_thenReturnsSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class));
        Object[] arguments = new Object[]{1, "test"};
        ArgumentMatchingTool tool = Mockito.spy(new ArgumentMatchingTool());
        when(tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments))
                .thenAnswer(new Answer<Integer[]>() {
                    @Override
                    public Integer[] answer(InvocationOnMock invocation) throws Throwable {
                        return new Integer[]{0};
                    }
                });

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(new Integer[]{0}, result);
    }
}