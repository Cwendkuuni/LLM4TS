import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(contains("abc"));
        matchers.add(contains("def"));
        matchers.add(contains("ghi"));

        Object[] arguments = new Object[]{1, "abc", 3, "def"};

        Integer[] expectedIndexes = new Integer[]{1, 3};
        Integer[] actualIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expectedIndexes, actualIndexes);

        matchers = new ArrayList<>();
        matchers.add(contains("abc"));
        matchers.add(contains("def"));
        matchers.add(contains("def"));
        arguments = new Object[]{1, "abc", 3, "def"};

        actualIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], actualIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EdgeCases() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(contains(""));
        matchers.add(contains(""));
        matchers.add(contains(""));
        Object[] arguments = new Object[]{null, null, null};

        Integer[] expectedIndexes = new Integer[]{0, 1, 2};
        Integer[] actualIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expectedIndexes, actualIndexes);

        matchers = new ArrayList<>();
        matchers.add(contains(""));
        matchers.add(contains(""));
        matchers.add(contains(""));
        matchers.add(contains(""));
        arguments = new Object[]{null, null, null, null};

        actualIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], actualIndexes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_Exception() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(contains(""));
        Object[] arguments = new Object[]{1};

        argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }
}