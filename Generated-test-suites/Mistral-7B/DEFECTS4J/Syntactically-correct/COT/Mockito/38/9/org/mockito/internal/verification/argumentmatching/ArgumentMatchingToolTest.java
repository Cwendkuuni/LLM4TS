package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.internal.matchers.TypeByType.isA;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;

/**
 * Tests for ArgumentMatchingTool class
 */
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList_returnsEmptyArray() {
        List<Matcher> matchers = Collections.emptyList();
        Object[] arguments = new Object[0];

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(indexes, is(new Integer[0]));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSizes_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(isA(String.class));
        Object[] arguments = new Object[2];

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(indexes, is(new Integer[0]));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatchers_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(is(Integer.class), is("String"));
        Object[] arguments = new Object[]{1, "String"};

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(indexes, is(new Integer[0]));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatcher_returnsIndex() {
        List<Matcher> matchers = Arrays.asList(
                containsString("String").named("stringMatcher"),
                isA(String.class).named("stringMatcherWithType")
        );
        Object[] arguments = new Object[]{1, "Argument1", "String"};

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(indexes, arrayContaining(equalTo(1)));
        assertThat(indexes[0], is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nullList_throwsIllegalArgumentException() {
        Object[] arguments = new Object[]{1, "Argument1", "String"};

        ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArguments_throwsIllegalArgumentException() {
        List<Matcher> matchers = Arrays.asList(isA(String.class));

        ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
    }

    private Matcher<String> containsString(String expected) {
        return new ContainsString(expected);
    }

    private static class ContainsString extends org.hamcrest.Matcher<String> {

        private final String expected;

        public ContainsString(String expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object argument) {
            return argument.toString().contains(expected);
        }

        @Override
        public String describeTo(StringDescription description) {
            description.appendText("contains \"").appendValue(expected).appendText("\"");
            return description.toString();
        }
    }
}