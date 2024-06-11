import static org.junit.Assert.assertEquals;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsSizeMismatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(org.mockito.Matchers.anyString());
        Object[] arguments = new Object[] { "arg1", "arg2" };
        Integer[] expected = new Integer[0];

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndNotMatching() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[] { "arg1" };
        Integer[] expected = new Integer[]{0};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }


    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndTypeMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{5};
        Integer[] expected = new Integer[0];

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }


    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndTypeDoesNotMatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{"arg1"};
        Integer[] expected = new Integer[]{0};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

}