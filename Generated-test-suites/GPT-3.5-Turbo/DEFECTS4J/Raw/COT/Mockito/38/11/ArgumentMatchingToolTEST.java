import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test case 1: matchers and arguments have the same size
        List<Matcher> matchers1 = new LinkedList<>();
        matchers1.add(new ContainsExtraTypeInformation());
        Integer[] arguments1 = {1};
        Integer[] expected1 = {};
        assertArrayEquals(expected1, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers1, arguments1));

        // Test case 2: matchers size is different from arguments size
        List<Matcher> matchers2 = new LinkedList<>();
        Integer[] arguments2 = {1, 2, 3};
        Integer[] expected2 = {};
        assertArrayEquals(expected2, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2));

        // Test case 3: ContainsExtraTypeInformation matches, safelyMatches returns false, toStringEquals returns true
        List<Matcher> matchers3 = new LinkedList<>();
        matchers3.add(new ContainsExtraTypeInformation());
        Integer[] arguments3 = {null};
        Integer[] expected3 = {0};
        assertArrayEquals(expected3, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers3, arguments3));
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test case 1: m.matches throws an exception
        Matcher m1 = (arg) -> {
            throw new RuntimeException("Mock exception");
        };
        assertFalse(tool.safelyMatches(m1, null));

        // Test case 2: m.matches returns true
        Matcher m2 = (arg) -> true;
        assertTrue(tool.safelyMatches(m2, null));

        // Test case 3: m.matches returns false
        Matcher m3 = (arg) -> false;
        assertFalse(tool.safelyMatches(m3, null));
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test case 1: StringDescription.toString(m) equals arg.toString()
        Matcher m1 = (arg) -> {
            return true;
        };
        assertTrue(tool.toStringEquals(m1, "test"));

        // Test case 2: StringDescription.toString(m) doesn't equal arg.toString()
        Matcher m2 = (arg) -> {
            return true;
        };
        assertFalse(tool.toStringEquals(m2, "test2"));
    }
}

