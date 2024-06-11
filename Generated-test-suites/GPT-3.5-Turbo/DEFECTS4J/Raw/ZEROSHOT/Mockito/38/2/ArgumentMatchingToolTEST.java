import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeDifferentFromArgumentsLength() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add((Matcher) null);
        matchers.add((Matcher) null);
        matchers.add((Matcher) null);

        Object[] arguments = new Object[]{};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingStringDescriptionToString() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new StringDescription().appendText("test"));
        matchers.add(new StringDescription().appendText("test"));
        matchers.add(new StringDescription().appendText("test"));

        Object[] arguments = new Object[]{"test", "test", "test"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingContainsExtraTypeInformationButNotMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[]{"test", "test", "test"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[]{0, 1, 2}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingContainsExtraTypeInformationAndMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[]{1, 2, 3};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }
}