import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {
    
    @Test
    public void testGetScript() {
        // Typical Use Case
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(3, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
        
        // Edge Case: Empty sequences
        sequence1 = new ArrayList<Integer>();
        sequence2 = new ArrayList<Integer>();
        comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        script = comparator.getScript();
        assertEquals(0, script.size());
        
        // Edge Case: Same sequences
        sequence1 = Arrays.asList(1, 2, 3);
        sequence2 = Arrays.asList(1, 2, 3);
        comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        script = comparator.getScript();
        assertEquals(3, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof KeepCommand);
        assertTrue(script.getCommand(2) instanceof KeepCommand);
        
        // Error Scenario: Null sequence
        sequence1 = null;
        sequence2 = Arrays.asList(1, 2, 3);
        try {
            comparator = new SequencesComparator<Integer>(sequence1, sequence2);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Sequence1 cannot be null", e.getMessage());
        }
    }
    
    @Test
    public void testBuildSnake() {
        // Typical Use Case
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        assertEquals(0, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
        
        // Edge Case: Empty sequences
        sequence1 = new ArrayList<Integer>();
        sequence2 = new ArrayList<Integer>();
        comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        assertNull(snake);
        
        // Error Scenario: Negative indices
        sequence1 = Arrays.asList(1, 2, 3);
        sequence2 = Arrays.asList(1, 2, 3);
        comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        try {
            snake = comparator.buildSnake(-1, -1, sequence1.size(), sequence2.size());
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index out of range: -1", e.getMessage());
        }
    }
    
    @Test
    public void testGetMiddleSnake() {
        // Typical Use Case
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNotNull(snake);
        
        // Edge Case: Empty sequences
        sequence1 = new ArrayList<Integer>();
        sequence2 = new ArrayList<Integer>();
        comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNull(snake);
        
        // Error Scenario: Null equator
        sequence1 = Arrays.asList(1, 2, 3);
        sequence2 = Arrays.asList(1, 2, 3);
        Equator<Integer> equator = null;
        try {
            comparator = new SequencesComparator<Integer>(sequence1, sequence2, equator);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Equator cannot be null", e.getMessage());
        }
    }
    
    @Test
    public void testBuildScript() {
        // Typical Use Case
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<Integer>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        assertEquals(3, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
        
        // Edge Case: Empty sequences
        sequence1 = new ArrayList<Integer>();
        sequence2 = new ArrayList<Integer>();
        comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        script = new EditScript<Integer>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        assertEquals(0, script.size());
        
        // Error Scenario: Invalid indices
        sequence1 = Arrays.asList(1, 2, 3);
        sequence2 = Arrays.asList(1, 2, 3);
        comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        script = new EditScript<Integer>();
        try {
            comparator.buildScript(-1, sequence1.size(), 0, sequence2.size(), script);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index out of range: -1", e.getMessage());
        }
    }
}