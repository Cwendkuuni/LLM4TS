import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.List;
import java.util.LinkedList;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_sameSize() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[2];
        arguments[0] = "argument1";
        arguments[1] = "argument2";
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSize() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[2];
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformation() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = 5;
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, indexes.length);
        assertEquals(0, indexes[0].intValue());
    }

    @Test
    public void testSafelyMatches_exception() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = new Object();
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.safelyMatches(matcher, arg);
        
        assertFalse(result);
    }

    @Test
    public void testToStringEquals_true() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = "argument";
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(matcher, arg);
        
        assertTrue(result);
    }

    @Test
    public void testToStringEquals_false() {
        Matcher matcher = new ContainsExtraTypeInformation();
        Object arg = 5;
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(matcher, arg);
        
        assertFalse(result);
    }
}


