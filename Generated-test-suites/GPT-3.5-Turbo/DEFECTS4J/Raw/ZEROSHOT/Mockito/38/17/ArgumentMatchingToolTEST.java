import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test case 1
        List<Matcher> matchers1 = new LinkedList<>();
        matchers1.add(mock(Matcher.class));
        Object[] arguments1 = new Object[1];
        Integer[] expected1 = new Integer[0];
        Integer[] result1 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers1, arguments1);
        assertArrayEquals(expected1, result1);

        // Test case 2
        List<Matcher> matchers2 = new LinkedList<>();
        matchers2.add(mock(Matcher.class));
        matchers2.add(mock(ContainsExtraTypeInformation.class));
        Object[] arguments2 = new Object[2];
        Integer[] expected2 = new Integer[0];
        Integer[] result2 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);
        assertArrayEquals(expected2, result2);

        // Test case 3
        List<Matcher> matchers3 = new LinkedList<>();
        matchers3.add(mock(ContainsExtraTypeInformation.class));
        Object[] arguments3 = new Object[1];
        Integer[] expected3 = new Integer[] {0};
        Integer[] result3 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers3, arguments3);
        assertArrayEquals(expected3, result3);

        // Test case 4
        List<Matcher> matchers4 = new LinkedList<>();
        matchers4.add(mock(ContainsExtraTypeInformation.class));
        matchers4.add(mock(ContainsExtraTypeInformation.class));
        Object[] arguments4 = new Object[2];
        Integer[] expected4 = new Integer[] {0, 1};
        Integer[] result4 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers4, arguments4);
        assertArrayEquals(expected4, result4);
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test case 1
        Matcher matcher1 = mock(Matcher.class);
        Object arg1 = new Object();
        boolean expected1 = false;
        boolean result1 = tool.safelyMatches(matcher1, arg1);
        assertEquals(expected1, result1);

        // Test case 2
        Matcher matcher2 = mock(Matcher.class);
        Object arg2 = "test";
        boolean expected2 = false;
        boolean result2 = tool.safelyMatches(matcher2, arg2);
        assertEquals(expected2, result2);

        // Test case 3
        Matcher matcher3 = mock(Matcher.class);
        Object arg3 = null;
        boolean expected3 = false;
        boolean result3 = tool.safelyMatches(matcher3, arg3);
        assertEquals(expected3, result3);
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test case 1
        Matcher matcher1 = mock(Matcher.class);
        Object arg1 = new Object();
        boolean expected1 = false;
        boolean result1 = tool.toStringEquals(matcher1, arg1);
        assertEquals(expected1, result1);

        // Test case 2
        Matcher matcher2 = mock(Matcher.class);
        Object arg2 = "test";
        boolean expected2 = false;
        boolean result2 = tool.toStringEquals(matcher2, arg2);
        assertEquals(expected2, result2);

        // Test case 3
        Matcher matcher3 = mock(Matcher.class);
        Object arg3 = null;
        boolean expected3 = false;
        boolean result3 = tool.toStringEquals(matcher3, arg3);
        assertEquals(expected3, result3);
    }
}