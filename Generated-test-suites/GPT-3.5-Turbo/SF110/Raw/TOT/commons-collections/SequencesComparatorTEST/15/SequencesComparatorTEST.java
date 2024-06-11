import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class SequencesComparatorTest {

    private SequencesComparator<Integer> comparator;
    
    @Before
    public void setUp() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);
        
        comparator = new SequencesComparator<>(sequence1, sequence2);
    }
    
    @Test
    public void testGetScript() {
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }
    
    @Test
    public void testBuildSnake() {
        Snake snake = comparator.buildSnake(0, 0, 2, 2);
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
        assertNull(snake);
    }
    
    @Test
    public void testBuildScript() {
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 3, script);
        assertEquals(2, script.getCommands().size());
        assertTrue(script.getCommands().get(0) instanceof DeleteCommand);
        assertTrue(script.getCommands().get(1) instanceof InsertCommand);
        assertEquals(1, (int) script.getCommands().get(0).getObject());
        assertEquals(4, (int) script.getCommands().get(1).getObject());
    }
}