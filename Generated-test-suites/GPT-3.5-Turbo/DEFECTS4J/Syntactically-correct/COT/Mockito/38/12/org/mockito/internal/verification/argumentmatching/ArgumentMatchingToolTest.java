
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;

import static org.junit.Assert.*;
import org.junit.*;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // Test when matchers and arguments sizes are different
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[]{1, 2, 3};
        Integer[] expected = new Integer[0];
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, result);

        // Test when matchers and arguments sizes are the same, but there are no suspicious arguments
        matchers.add(Matchers.any());
        matchers.add(Matchers.any());
        matchers.add(Matchers.any());
        arguments = new Object[]{1, 2, 3};
        expected = new Integer[0];
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, result);

        // Test when matchers and arguments sizes are the same, and there is one suspicious argument
        matchers.add(Matchers.equalTo(2));
        arguments = new Object[]{1, "2", 3};
        expected = new Integer[]{1};
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, result);

        // Test when matchers and arguments sizes are the same, and there are multiple suspicious arguments
        matchers.add(Matchers.equalTo(2));
        matchers.add(Matchers.containsString("test"));
        arguments = new Object[]{1, "2", "test"};
        expected = new Integer[]{1, 2};
        result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, result);
    }
}
