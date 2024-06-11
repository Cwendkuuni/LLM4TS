import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {
    private SequencesComparator<Integer> comparator;
    
    @Before
    public void setUp() {
        // Sample test data
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 6, 7);
        
        comparator = new SequencesComparator<>(sequence1, sequence2);
    }
    
    @Test
    public void testGetScript() {
        EditScript<Integer> script = comparator.getScript();
        
        // Test script commands
        assertEquals(script.getCommands().size(), 6);
        assertTrue(script.getCommands().get(0) instanceof KeepCommand);
        assertTrue(script.getCommands().get(1) instanceof KeepCommand);
        assertTrue(script.getCommands().get(2) instanceof KeepCommand);
        assertTrue(script.getCommands().get(3) instanceof DeleteCommand);
        assertTrue(script.getCommands().get(4) instanceof InsertCommand);
        assertTrue(script.getCommands().get(5) instanceof InsertCommand);
        
        // Test script command values
        assertEquals(script.getCommands().get(0).getObject(), Integer.valueOf(1));
        assertEquals(script.getCommands().get(1).getObject(), Integer.valueOf(2));
        assertEquals(script.getCommands().get(2).getObject(), Integer.valueOf(3));
        assertEquals(script.getCommands().get(3).getObject(), Integer.valueOf(4));
        assertEquals(script.getCommands().get(4).getObject(), Integer.valueOf(6));
        assertEquals(script.getCommands().get(5).getObject(), Integer.valueOf(7));
    }
    
    @Test
    public void testBuildSnake() {
        // Sample test data
        int start = 0;
        int diag = 0;
        int end1 = 3;
        int end2 = 4;
        
        SequencesComparator.Snake snake = comparator.buildSnake(start, diag, end1, end2);
        
        assertEquals(snake.getStart(), 0);
        assertEquals(snake.getEnd(), 3);
        assertEquals(snake.getDiag(), 0);
    }
    
    @Test
    public void testGetMiddleSnake() {
        // Sample test data
        int start1 = 0;
        int end1 = 3;
        int start2 = 0;
        int end2 = 4;
        
        SequencesComparator.Snake middleSnake = comparator.getMiddleSnake(start1, end1, start2, end2);
        
        assertNull(middleSnake);
    }
    
    @Test
    public void testBuildScript() {
        // Sample test data
        int start1 = 0;
        int end1 = 3;
        int start2 = 0;
        int end2 = 4;
        EditScript<Integer> script = new EditScript<>();
        
        comparator.buildScript(start1, end1, start2, end2, script);
        
        // Test script commands
        assertEquals(script.getCommands().size(), 4);
        assertTrue(script.getCommands().get(0) instanceof KeepCommand);
        assertTrue(script.getCommands().get(1) instanceof KeepCommand);
        assertTrue(script.getCommands().get(2) instanceof KeepCommand);
        assertTrue(script.getCommands().get(3) instanceof InsertCommand);
        
        // Test script command values
        assertEquals(script.getCommands().get(0).getObject(), Integer.valueOf(1));
        assertEquals(script.getCommands().get(1).getObject(), Integer.valueOf(2));
        assertEquals(script.getCommands().get(2).getObject(), Integer.valueOf(3));
        assertEquals(script.getCommands().get(3).getObject(), Integer.valueOf(6));
    }
}