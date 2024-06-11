import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsSizeMismatch_returnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = {1, 2, 3};

        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsSizeMatch_returnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = {};

        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersContainContainsExtraTypeInformation_matcherDoesNotSafelyMatchAndToStringEqualsAndTypeDoesNotMatch() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {1};

        Integer[] expected = {0};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersContainContainsExtraTypeInformation_matcherSafelyMatchesAndToStringEqualsAndTypeDoesNotMatch() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {1};

        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersContainContainsExtraTypeInformation_matcherDoesNotSafelyMatchAndToStringEqualsAndTypeMatches() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {1};

        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersContainContainsExtraTypeInformation_matcherDoesNotSafelyMatchAndToStringNotEqualsAndTypeDoesNotMatch() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {new Object()};

        Integer[] expected = {0};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersContainContainsExtraTypeInformation_matcherDoesNotSafelyMatchAndToStringNotEqualsAndTypeMatches() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {1};

        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersContainContainsExtraTypeInformation_matcherDoesNotSafelyMatchAndToStringNotEqualsAndTypeMatchesAndTypeDoesNotMatch() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = {new Object()};

        Integer[] expected = {0};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }
}
