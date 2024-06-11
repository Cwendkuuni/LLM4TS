import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.*;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(IsEqual.equalTo(1));
        matchers.add(IsEqual.equalTo("test"));
        
        Object[] arguments = new Object[]{1, "test"};
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithOneMismatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(IsEqual.equalTo(1));
        matchers.add(IsEqual.equalTo("test"));
        
        Object[] arguments = new Object[]{1, 2};
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(new Integer[]{1}, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithDifferentTypes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(IsEqual.equalTo(1));
        matchers.add(IsEqual.equalTo("test"));
        
        Object[] arguments = new Object[]{1, 1};
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithException() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(IsEqual.equalTo(1));
        
        Object[] arguments = new Object[]{1};
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
}