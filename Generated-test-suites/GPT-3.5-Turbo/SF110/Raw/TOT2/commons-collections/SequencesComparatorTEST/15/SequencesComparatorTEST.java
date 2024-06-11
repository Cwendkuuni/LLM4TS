import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class SequencesComparatorTest {
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
        SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> result = sequencesComparator.getScript();
        
        assertNotNull(result);
    }
    
    @Test
    public void testBuildSnake() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b");
        SequencesComparator<String> sequencesComparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake result = sequencesComparator.buildSnake(0, 0, 2, 2);
        
        assertNull(result);
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Character> sequence1 = Arrays.asList('a', 'b', 'c');
        List<Character> sequence2 = Arrays.asList('a', 'b', 'd');
        SequencesComparator<Character> sequencesComparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake result = sequencesComparator.getMiddleSnake(0, 3, 0, 3);
        
        assertNotNull(result);
    }
    
    @Test
    public void testBuildScript() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "c");
        SequencesComparator<String> sequencesComparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<String> result = new EditScript<>();
        sequencesComparator.buildScript(0, 3, 0, 2, result);
        
        assertNotNull(result);
    }
    
}