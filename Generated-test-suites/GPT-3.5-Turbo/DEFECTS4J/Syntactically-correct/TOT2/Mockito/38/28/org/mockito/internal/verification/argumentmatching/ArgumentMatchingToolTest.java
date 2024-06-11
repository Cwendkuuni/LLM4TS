import org.junit.Test;
import static org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_typicalCase() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        
        Object[] arguments = new Object[1];
        arguments[0] = "test";
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchers() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new LinkedList<>();
        
        Object[] arguments = new Object[0];
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspicious() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        
        Object[] arguments = new Object[2];
        arguments[0] = "test";
        arguments[1] = 123;
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(2, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
        assertEquals(Integer.valueOf(1), result[1]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_exceptionHandling() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        
        Object[] arguments = new Object[1];
        arguments[0] = new Object();
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
    }

}