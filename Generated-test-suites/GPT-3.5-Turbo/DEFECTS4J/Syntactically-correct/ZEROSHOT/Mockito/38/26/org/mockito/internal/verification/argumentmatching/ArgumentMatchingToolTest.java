import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.Matcher;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersSizeNotEqualToArgumentsLength_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(org.mockito.Matchers.anyString());

        Object[] arguments = { "arg1", "arg2" };

        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatcherNotContainsExtraTypeInformation_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(org.mockito.Matchers.anyString());

        Object[] arguments = { "arg1" };

        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatcherDoesNotSafelyMatches_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = { "arg1" };

        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatcherToStringNotEqualsArgumentToString_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = { "arg1" };

        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatcherTypeNotMatchesArgumentType_ReturnsIndexesArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = { 10 };

        Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, indexes.length);
        assertEquals(0, indexes[0].intValue());
    }
}