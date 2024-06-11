import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
    }
    
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.buildSnake(0, 0, 3, 3);
        
        assertNotNull(snake);
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake middleSnake = comparator.getMiddleSnake(0, 3, 0, 3);
        
        assertNull(middleSnake);
    }
    
    @Test
    public void testBuildScript() {
        List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("a", "c", "d"));
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 3, script);
        
        assertNotNull(script);
    }
}