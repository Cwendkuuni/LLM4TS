import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;

import org.hamcrest.Matcher;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        // Creating mock objects
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(ContainsExtraTypeInformation.class));

        Object[] arguments = new Object[2];
        arguments[0] = "test";
        arguments[1] = 123;

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        Integer[] expected = {};
        assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
    }
}