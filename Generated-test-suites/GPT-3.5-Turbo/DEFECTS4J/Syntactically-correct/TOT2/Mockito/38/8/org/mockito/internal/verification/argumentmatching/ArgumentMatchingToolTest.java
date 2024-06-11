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
                mock(Matcher.class),
                mock(Matcher.class)
        );
        
        Object[] arguments = new Object[] { "test", 123 };
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert that no suspicious arguments found in this case
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesMismatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = Arrays.asList(
                mock(Matcher.class),
                mock(Matcher.class)
        );
        
        Object[] arguments = new Object[] { 123, 123 };
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert that both arguments are suspicious in this case
        assertEquals(2, result.length);
        assertTrue(Arrays.asList(result).contains(0));
        assertTrue(Arrays.asList(result).contains(1));
    }

    @Test
    public void testSafelyMatchesWithException() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        Matcher matcher = mock(Matcher.class);
        
        // Mocking the matcher to throw an exception when matches method is called
        when(matcher.matches(any())).thenThrow(new RuntimeException());
        
        boolean result = tool.safelyMatches(matcher, "test");
        
        // Assert that exception handling works correctly
        assertFalse(result);
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        Matcher matcher = mock(Matcher.class);
        when(matcher.toString()).thenReturn("test");
        
        boolean result = tool.toStringEquals(matcher, "test");
        
        // Assert that toString comparison works correctly
        assertTrue(result);
    }
}