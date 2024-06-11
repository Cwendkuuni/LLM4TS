package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.Arguments.not;
import static org.mockito.internal.matchers.Arguments.same;
import static org.mockito.internal.matchers.type.IsA.instanceOf;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyListAndArguments() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[0];
        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatches() {
        List<Matcher> matchers = Arrays.asList(instanceOf(String.class), same(123));
        Object[] arguments = new Object[]{123, "test"};
        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SingleSuspiciousMatch() {
        List<Matcher> matchers = Arrays.asList(instanceOf(String.class), not(instanceOf(Integer.class)));
        Object[] arguments = new Object[]{123, "test"};
        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[]{1}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MultipleSuspiciousMatches() {
        List<Matcher> matchers = Arrays.asList(
                instanceOf(String.class),
                not(instanceOf(String.class)),
                same(123),
                not(same(123))
        );
        Object[] arguments = new Object[]{123, "test", 321, "test"};
        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[]{1, 3}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments() {
        List<Matcher> matchers = Arrays.asList(instanceOf(String.class), same(123));
        Object[] arguments = null;
        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherWithCustomToString() {
        List<Matcher> matchers = Arrays.asList(
                new TypeSafeMatcher<List<String>>() {
                    @Override
                    public boolean matchesSafely(List<String> item) {
                        return item.size() > 1;
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("a list with more than one element");
                    }
                },
                same(Arrays.asList("test1", "test2"))
        );
        Object[] arguments = new Object[]{Arrays.asList("test1"), Arrays.asList("test1", "test2")};
        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], result);
    }
}