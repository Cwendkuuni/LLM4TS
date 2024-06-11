import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(any(String.class));
        matchers.add(any(Integer.class));
        
        Object[] arguments = new Object[]{"test", 10};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Integer[] expected = new Integer[0];
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesMismatch() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(any(String.class));
        
        Object[] arguments = new Object[]{10};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        Integer[] expected = new Integer[]{0};
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        
        Matcher matcher = mock(Matcher.class);
        
        Object argument = "test";
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, argument);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        
        Matcher matcher = mock(Matcher.class);
        
        Object argument = "test";
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, argument);
        
        assertEquals(false, result);
    }
}