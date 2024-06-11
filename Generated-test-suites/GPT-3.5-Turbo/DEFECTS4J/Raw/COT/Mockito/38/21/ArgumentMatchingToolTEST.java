import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {
    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    /*  */

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add((Matcher) new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{"test"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[]{0}, result);
    }

    /*  */
}