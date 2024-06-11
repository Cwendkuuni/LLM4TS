import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Matcher;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        Object[] arguments = new Object[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        // Verify length of result
        assertEquals(0, result.length);

        matchers.add(is("test"));
        arguments = new Object[]{"test"};
        result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        // Verify length of result
        assertEquals(0, result.length);

        matchers.add(is("test"));
        arguments = new Object[]{"different"};
        result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        // Verify length of result
        assertEquals(1, result.length);
        // Verify values in result
        assertEquals(1, (int)result[0]);

        matchers.add(new ContainsExtraTypeInformation());
        arguments = new Object[]{"test"};
        result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        // Verify length of result
        assertEquals(1, result.length);
        // Verify values in result
        assertEquals(0, (int)result[0]);

        matchers.add(new ContainsExtraTypeInformation());
        arguments = new Object[]{"different"};
        result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        // Verify length of result
        assertEquals(2, result.length);
        // Verify values in result
        assertEquals(0, (int)result[0]);
        assertEquals(1, (int)result[1]);
    }

    @Test
    // Test safelyMatches() method with matching argument 
    public void testSafelyMatchesWithMatchingArgument() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        Object arg = "test";
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);
        // Verify result
        assertTrue(result);
    }

    @Test
    // Test safelyMatches() method with non-matching argument
    public void testSafelyMatchesWithNonMatchingArgument() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        Object arg = new Object();
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);
        // Verify result
        assertFalse(result);
    }

    @Test
    // Test safelyMatches() method with throwable
    public void testSafelyMatchesWithThrowable() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        Object arg = "test";
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);
        // Verify result
        assertTrue(result);
    }

    @Test
    // Test toStringEquals() method with equal strings
    public void testToStringEqualsWithEqualStrings() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        Object arg = "test";
        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);
        // Verify result
        assertTrue(result);
    }

    @Test
    // Test toStringEquals() method with unequal strings
    public void testToStringEqualsWithUnequalStrings() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        Object arg = "different";
        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);
        // Verify result
        assertFalse(result);
    }
}