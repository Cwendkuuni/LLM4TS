import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import java.util.List;
import java.util.LinkedList;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(isA(String.class));
        matchers.add(isA(Integer.class));
        
        Object[] arguments = new Object[]{"test", 10};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(new Integer(1), result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_Empty() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ExceptionHandling() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(isA(String.class));
        
        Object[] arguments = new Object[]{"test"};
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
}