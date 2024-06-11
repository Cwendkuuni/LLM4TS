import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;
import java.util.List;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(CoreMatchers.is("test"));
        matchers.add(CoreMatchers.instanceOf(Integer.class));
        
        Object[] arguments = { "test", 123 };
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(1), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NotMatching() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(CoreMatchers.is("test"));
        matchers.add(CoreMatchers.instanceOf(Integer.class));
        
        Object[] arguments = { "test", "123" };
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(2, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
        assertEquals(Integer.valueOf(1), result[1]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_Empty() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = {};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSizes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(CoreMatchers.is("test"));
        
        Object[] arguments = { "test", 123 };

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testSafelyMatches() {
        Matcher matcher = CoreMatchers.is("test");
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, "test");
        
        assertTrue(result);
    }

    @Test
    public void testToStringEquals() {
        Matcher matcher = CoreMatchers.is("test");
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, "test");
        
        assertTrue(result);
    }

    @Test
    public void testToStringEquals_NotEqual() {
        Matcher matcher = CoreMatchers.is("test");
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, "not test");
        
        assertFalse(result);
    }
}