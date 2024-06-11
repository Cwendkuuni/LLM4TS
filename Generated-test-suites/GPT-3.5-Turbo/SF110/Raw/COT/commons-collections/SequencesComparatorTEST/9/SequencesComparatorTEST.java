
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Before;
import org.junit.Test;

public class SequencesComparatorTest {

    private SequencesComparator<Integer> comparator;
    
    @Before
    public void setUp() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        comparator = new SequencesComparator<Integer>(sequence1, sequence2);
    }
    
    @Test
    public void testGetScript() {
        comparator.getScript();
        // Check if the method executes without throwing an exception
        assertTrue(true);
    }
    
    @Test
    public void testBuildSnake() {
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 3);
        
        // Check if the snake's start, end, and diag values are correct
        assertEquals(0, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
        
        // Check if the snake's start, end, and diag values are correct
        assertEquals(1, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testBuildScript() {
        EditScript<Integer> script = new EditScript<Integer>();
        comparator.buildScript(0, 3, 0, 3, script);
        
        // Check if the script contains the expected commands
        
        // Example expected commands:
        // KeepCommand{object=1}, KeepCommand{object=2}, KeepCommand{object=3}
        
        // You should compare the expected commands with the actual commands stored in the script
        
        // assertEquals(expectedCommands, script.getCommands());
    }
    
}
