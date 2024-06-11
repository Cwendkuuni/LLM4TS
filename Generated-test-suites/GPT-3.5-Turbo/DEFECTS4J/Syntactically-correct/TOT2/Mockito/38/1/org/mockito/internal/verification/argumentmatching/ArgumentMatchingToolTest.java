import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.Matchers;
import java.util.List;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_basic() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = List.of(Matchers.any(), Matchers.any());
        Object[] arguments = {1, "test"};
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_edgeCase() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = List.of(Matchers.contains("test"), Matchers.any());
        Object[] arguments = {"test", "test"};
        
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_exceptionHandling() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = List.of(Matchers.any(), Matchers.any());
        Object[] arguments = {1, "test"};
        
        // Test an exception scenario by passing null instead of Matcher instances
        assertThrows(NullPointerException.class, () -> {
            tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
        });
    }
}