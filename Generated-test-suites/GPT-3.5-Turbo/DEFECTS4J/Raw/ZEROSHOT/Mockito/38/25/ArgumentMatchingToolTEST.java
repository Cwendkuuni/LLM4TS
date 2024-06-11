import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        Object[] arguments = new Object[0];
        Integer[] expected = new Integer[0];

        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithMismatchedSizes() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        Integer[] expected = new Integer[0];

        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithSuspiciousMatch() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = "test";
        Integer[] expected = new Integer[]{0};

        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSafelyMatches() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        Object arg = "test";
        boolean expected = false;

        boolean actual = argumentMatchingTool.safelyMatches(matcher, arg);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringEquals() {
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        Object arg = "test";
        boolean expected = false;

        boolean actual = argumentMatchingTool.toStringEquals(matcher, arg);

        Assert.assertEquals(expected, actual);
    }
}