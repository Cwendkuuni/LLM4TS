import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.CoreMatchers.*;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.List;
import org.hamcrest.Matcher;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool tool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        // Test when matchers list size is different from arguments list size
        List<Matcher> matchers = List.of(CoreMatchers.is("hello"), CoreMatchers.is(10));
        Object[] arguments = new Object[]{"hello"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_typical() {
        // Typical use cases
        List<Matcher> matchers = List.of(CoreMatchers.is("hello"), CoreMatchers.is(10));
        Object[] arguments = new Object[]{"hello", 20};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(1), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_edge() {
        // Edge case: empty matchers and empty arguments
        List<Matcher> matchers = List.of();
        Object[] arguments = new Object[]{};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_error() {
        // Error scenario: matcher throws an exception while matching
        List<Matcher> matchers = List.of(CoreMatchers.is("hello"));
        Object[] arguments = new Object[]{"hello"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformation() {
        // ContainsExtraTypeInformation case
        List<Matcher> matchers = List.of(new ContainsExtraTypeInformation(), new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{"hello", 10};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

}