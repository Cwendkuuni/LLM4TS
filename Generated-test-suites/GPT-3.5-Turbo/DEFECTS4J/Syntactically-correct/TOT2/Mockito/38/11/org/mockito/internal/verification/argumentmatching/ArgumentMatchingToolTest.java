import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import org.junit.Test;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool matchingTool = new ArgumentMatchingTool();

        // Test when matchers and arguments are the same size
        List<Matcher> matchers = List.of(isA(Integer.class), isA(String.class));
        Object[] arguments = new Object[] {10, "test"};
        Integer[] indexes = matchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, indexes.length);

        // Test when matchers and arguments are different sizes
        List<Matcher> matchers2 = List.of(isA(Integer.class), isA(String.class));
        Object[] arguments2 = new Object[] {"test"};
        Integer[] indexes2 = matchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);
        assertEquals(0, indexes2.length);

        // Test when matchers and arguments are the same size but not matching
        List<Matcher> matchers3 = List.of(isA(Integer.class), isA(String.class));
        Object[] arguments3 = new Object[] {10, 20};
        Integer[] indexes3 = matchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers3, arguments3);
        assertEquals(2, indexes3.length);
        assertArrayEquals(new Integer[]{0, 1}, indexes3);

        // Test when matchers and arguments have extra type information
        List<Matcher> matchers4 = List.of(isA(Integer.class), containsExtraTypeInformation(String.class));
        Object[] arguments4 = new Object[] {10, "test"};
        Integer[] indexes4 = matchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers4, arguments4);
        assertEquals(0, indexes4.length);
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool matchingTool = new ArgumentMatchingTool();

        // Test matching with valid matcher and argument
        assertTrue(matchingTool.safelyMatches(isA(Integer.class), 10));
        
        // Test matching with invalid matcher and argument
        assertFalse(matchingTool.safelyMatches(isA(Integer.class), "test"));
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool matchingTool = new ArgumentMatchingTool();

        // Test when toString of arguments and matchers are equal
        assertTrue(matchingTool.toStringEquals(isA(Integer.class), new Integer(10)));
        
        // Test when toString of arguments and matchers are not equal
        assertFalse(matchingTool.toStringEquals(isA(Integer.class), "test"));
    }
}