import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.CoreMatchers;
import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Matcher;

public class ArgumentMatchingToolTest {

    ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(CoreMatchers.is("test"));
        matchers.add(CoreMatchers.is(123));
        
        Object[] arguments = new Object[]{"test", 123};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
}