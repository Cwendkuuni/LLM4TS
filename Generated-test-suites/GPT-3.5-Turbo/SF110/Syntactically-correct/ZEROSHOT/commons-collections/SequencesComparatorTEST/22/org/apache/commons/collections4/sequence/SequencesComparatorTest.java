import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<String> sequence1 = Arrays.asList("A", "B", "C", "D");
        List<String> sequence2 = Arrays.asList("A", "B", "C", "D");
        List<String> sequence3 = Arrays.asList("A", "B", "C");

        SequencesComparator<String> comparator1 = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script1 = comparator1.getScript();
        assertEquals(0, script1.size());

        SequencesComparator<String> comparator2 = new SequencesComparator<>(sequence1, sequence3);
        EditScript<String> script2 = comparator2.getScript();
        assertEquals(1, script2.size());
        assertTrue(script2.getCommand(0) instanceof InsertCommand);
        assertEquals("D", script2.getCommand(0).getObject());

        SequencesComparator<String> comparator3 = new SequencesComparator<>(sequence3, sequence1);
        EditScript<String> script3 = comparator3.getScript();
        assertEquals(1, script3.size());
        assertTrue(script3.getCommand(0) instanceof DeleteCommand);
        assertEquals("D", script3.getCommand(0).getObject());
    }
    
    @Test
    public void testBuildSnake() {
        List<String> sequence1 = Arrays.asList("A", "B", "C", "D");
        List<String> sequence2 = Arrays.asList("A", "B", "C", "D");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        assertEquals(0, snake.getStart());
        assertEquals(4, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<String> sequence1 = Arrays.asList("A", "B", "C", "D");
        List<String> sequence2 = Arrays.asList("A", "B", "C", "D");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNull(snake);
    }
    
    @Test
    public void testBuildScript() {
        List<String> sequence1 = Arrays.asList("A", "B", "C", "D");
        List<String> sequence2 = Arrays.asList("A", "B", "C", "D");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        assertEquals(0, script.size());
    }
    
    @Test
    public void testSnakeStart() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(5, 10, 2);
        assertEquals(5, snake.getStart());
    }
    
    @Test
    public void testSnakeEnd() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(5, 10, 2);
        assertEquals(10, snake.getEnd());
    }
    
    @Test
    public void testSnakeDiag() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(5, 10, 2);
        assertEquals(2, snake.getDiag());
    }
}