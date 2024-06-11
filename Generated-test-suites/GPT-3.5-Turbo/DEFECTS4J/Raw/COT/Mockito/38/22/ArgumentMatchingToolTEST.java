import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_basic() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};
        Integer[] expected = new Integer[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, result);
    }
    
    // Edge case: matchers and arguments have different lengths
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentLengths() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(null);
        Object[] arguments = new Object[]{};
        Integer[] expected = new Integer[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, result);
    }

    // Edge case: ContainsExtraTypeInformation and objects don't match
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformation() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{"test"};
        Integer[] expected = new Integer[]{0};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, result);
    }

    // Edge case: ContainsExtraTypeInformation and objects match
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformation_match() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[]{matcher};
        Integer[] expected = new Integer[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testSafelyMatches() {
        // Test the safelyMatches() method with a Matcher and an argument that matches
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = new ContainsExtraTypeInformation();
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);
        assertTrue(result);
        
        // Test the safelyMatches() method with a Matcher and an argument that does not match
        matcher = new ContainsExtraTypeInformation();
        arg = "test";
        result = argumentMatchingTool.safelyMatches(matcher, arg);
        assertFalse(result);
    }
    
    @Test
    public void testToStringEquals() {
        // Test the toStringEquals() method with a Matcher and an argument that have equal toString() representations
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = new ContainsExtraTypeInformation();
        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);
        assertTrue(result);
        
        // Test the toStringEquals() method with a Matcher and an argument that have different toString() representations
        matcher = new ContainsExtraTypeInformation();
        arg = "test";
        result = argumentMatchingTool.toStringEquals(matcher, arg);
        assertFalse(result);
    }
}