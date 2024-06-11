import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = Arrays.asList(
            isA(String.class),
            isA(Integer.class),
            isA(Double.class)
        );
        
        Object[] arguments = new Object[] {"hello", 5, 3.14};
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesMismatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = Arrays.asList(
            isA(String.class),
            isA(Integer.class),
            isA(Double.class)
        );
        
        Object[] arguments = new Object[] {5, "world", 3.14};
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(1), result[0]);
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        Matcher m = isA(String.class);
        Object arg = "hello";
        
        boolean result = tool.safelyMatches(m, arg);
        
        assertTrue(result);
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        Matcher m = contains("hello");
        Object arg = "hello";
        
        boolean result = tool.toStringEquals(m, arg);
        
        assertTrue(result);
    }
}