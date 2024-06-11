
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {


    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noMatchers() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        Integer[] arguments = new Integer[]{1, 2, 3};

        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, indexes.length);
    }
    
    // Other test cases...
    
}