import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertEquals(6, script.size());
        assertTrue(script.getCommand(0) instanceof InsertCommand);
        assertTrue(script.getCommand(1) instanceof InsertCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
        assertTrue(script.getCommand(3) instanceof DeleteCommand);
        assertTrue(script.getCommand(4) instanceof DeleteCommand);
        assertTrue(script.getCommand(5) instanceof DeleteCommand);
    }
    
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 3);
        
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
        
        assertEquals(2, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(1, snake.getDiag());
    }
    
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        
        comparator.buildScript(0, 3, 0, 3, script);
        
        assertEquals(3, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof KeepCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
    }
}