
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchersAndArguments_returnsEmptyArray() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchersAndNonEmptyArguments_returnsEmptyArray() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{1, 2, 3};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nonEmptyMatchersAndEmptyArguments_returnsEmptyArray() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingMatchersAndArguments_returnsEmptyArray() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{"hello"};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nonMatchingMatchersAndArguments_returnsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{1};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(0, result[0].intValue());
    }
}