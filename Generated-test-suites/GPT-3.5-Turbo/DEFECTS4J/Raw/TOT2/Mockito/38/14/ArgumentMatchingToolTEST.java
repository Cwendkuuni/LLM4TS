import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(CoreMatchers.any(String.class));
        
        Object[] arguments = { "Test" };
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        Matcher matcher = CoreMatchers.any(String.class);
        
        boolean result = tool.safelyMatches(matcher, "Test");
        
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        Matcher matcher = CoreMatchers.equalTo("Test");
        
        boolean result = tool.toStringEquals(matcher, "Test");
        
        assertTrue(result);
    }
}