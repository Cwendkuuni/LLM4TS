
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
        assertEquals(0, script.getCommands().size());
        
        sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4, 3, 5));
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        assertNotNull(script);
        assertEquals(3, script.getCommands().size());
        assertTrue(script.getCommands().get(0) instanceof KeepCommand);
        assertTrue(script.getCommands().get(1) instanceof InsertCommand);
        assertTrue(script.getCommands().get(2) instanceof DeleteCommand);
    }
    
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        assertNotNull(snake);
        assertEquals(5, snake.getEnd());
        
        sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4, 3, 5));
        comparator = new SequencesComparator<>(sequence1, sequence2);
        snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        assertNotNull(snake);
        assertEquals(3, snake.getEnd());
    }
    
    @Test 
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNull(snake);
        
        sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4, 3, 5));
        comparator = new SequencesComparator<>(sequence1, sequence2);
        snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNotNull(snake);
        assertEquals(2, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(-1, snake.getDiag());
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        assertNotNull(script);
        assertEquals(0, script.getCommands().size());
        
        sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4, 3, 5));
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        assertNotNull(script);
        assertEquals(3, script.getCommands().size());
        assertTrue(script.getCommands().get(0) instanceof KeepCommand);
        assertTrue(script.getCommands().get(1) instanceof InsertCommand);
        assertTrue(script.getCommands().get(2) instanceof DeleteCommand);
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScriptRuntimeException() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4, 3, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, sequence2.size(), 0, sequence1.size(), script);
    }
}
