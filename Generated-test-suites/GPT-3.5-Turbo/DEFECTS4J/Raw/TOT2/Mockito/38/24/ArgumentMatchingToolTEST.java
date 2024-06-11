import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.verify;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(CoreMatchers.instanceOf(String.class));
        matchers.add(CoreMatchers.is("test"));
        
        Object[] arguments = new Object[]{"test", 123};
        
        Integer[] expectedIndexes = new Integer[]{1};
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expectedIndexes, result);
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher matcher = CoreMatchers.is("test");
        Object arg = "test";
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);
        
        assertTrue(result);
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher matcher = CoreMatchers.is("test");
        Object arg = "test";
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);
        
        assertTrue(result);
    }
}