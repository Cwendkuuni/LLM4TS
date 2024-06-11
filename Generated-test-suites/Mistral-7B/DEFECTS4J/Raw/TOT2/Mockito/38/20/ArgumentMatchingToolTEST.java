package org.mockito.internal.verification.argumentmatching;

import org.junit.Test;
import org.mockito.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[]{1, "Two"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expectedIndexes = new Integer[]{0};
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertThat(actualIndexes, is(expectedIndexes));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedTypes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        Object[] arguments = new Object[]{1, "Two"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expectedIndexes = new Integer[]{0};
        matchers.set(0, mock(Matcher.class)); // Set the first matcher to a different type
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertThat(actualIndexes, is(expectedIndexes));
    }

    @Test(expected = Throwable.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherThrowsException() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[]{1, "Two"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        matchers.set(0, mock(Matcher.class, new Throwable())); // Set the first matcher to throw an exception
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        // This test should fail with the expected exception
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringEqualsButTypesDifferent() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        Object[] arguments = new Object[]{1, "One"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expectedIndexes = new Integer[]{1};
        matchers.set(1, mock(Matcher.class)); // Set the second matcher to have the same toString() representation but a different type
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertThat(actualIndexes, is(expectedIndexes));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersLessThanArguments() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{1, "Two"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expectedIndexes = new Integer[0];
        List<Matcher> matchersWithFewerElements = new LinkedList<>(matchers);
        matchersWithFewerElements.remove(0); // Remove the first matcher
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchersWithFewerElements, arguments);
        assertThat(actualIndexes, is(expectedIndexes));
    }
}