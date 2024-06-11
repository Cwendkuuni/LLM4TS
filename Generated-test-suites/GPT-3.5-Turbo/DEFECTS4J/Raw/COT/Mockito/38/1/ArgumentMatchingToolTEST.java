// The complete JUnit test file starts here

// Generated test cases for the ArgumentMatchingTool class

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Test case: matchers size is not equal to arguments length
        // Expected result: No suspicious arguments
        List<Matcher> matchers1 = Arrays.asList(
                CoreMatchers.isA(Integer.class),
                CoreMatchers.isA(String.class),
                CoreMatchers.isA(Boolean.class)
        );

        Object[] arguments1 = new Object[]{1, "test", true, false};
        Integer[] result1 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers1, arguments1);
        Assert.assertEquals(0, result1.length);
        
        // Test case: matchers and arguments size match, no suspicious arguments
        // Expected result: No suspicious arguments
        List<Matcher> matchers2 = Arrays.asList(
                CoreMatchers.isA(Integer.class),
                CoreMatchers.isA(String.class),
                CoreMatchers.isA(Boolean.class)
        );
        Object[] arguments2 = new Object[]{1, "test", true};

        Integer[] result2 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);
        Assert.assertEquals(0, result2.length);

        // Test case: matchers and arguments size match, one suspicious argument
        // Expected result: One suspicious argument at index 1
        List<Matcher> matchers3 = Arrays.asList(
                CoreMatchers.isA(Integer.class),
                new ContainsExtraTypeInformation(Integer.class),
                CoreMatchers.isA(Boolean.class)
        );
        Object[] arguments3 = new Object[]{1, "1", true};

        Integer[] result3 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers3, arguments3);
        Assert.assertEquals(1, result3.length);
        Assert.assertEquals(1, (int) result3[0]);

        // Test case: matchers and arguments size match, multiple suspicious arguments
        // Expected result: Two suspicious arguments at index 1 and 2
        List<Matcher> matchers4 = Arrays.asList(
                CoreMatchers.isA(Integer.class),
                new ContainsExtraTypeInformation(Integer.class),
                new ContainsExtraTypeInformation(String.class)
        );
        Object[] arguments4 = new Object[]{1, "1", 1};

        Integer[] result4 = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers4, arguments4);
        Assert.assertEquals(2, result4.length);
        Assert.assertTrue(Arrays.asList(result4).contains(1));
        Assert.assertTrue(Arrays.asList(result4).contains(2));
    }
  
    // The complete JUnit test file ends here
}