import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {
    
    @Test
    public void testConstructor() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        
        assertNotNull(comparator);
        assertEquals(Arrays.asList(1, 2, 3), comparator.sequence1);
        assertEquals(Arrays.asList(4, 5, 6), comparator.sequence2);
    }
    
    @Test
    public void testGetScript() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("a", "b", "c"), Arrays.asList("a", "d", "e", "b", "c"));
        EditScript<String> script = comparator.getScript();
        
        assertNotNull(script);
    }
    
    @Test
    public void testBuildSnake() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 0, 0);
        
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(0, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        SequencesComparator<Character> comparator = new SequencesComparator<>(Arrays.asList('a', 'b', 'c'), Arrays.asList('a', 'd', 'e', 'b', 'c'));
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 5);
        
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testBuildScript() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("a", "b", "c"), Arrays.asList("a", "d", "e", "b", "c"));
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 5, script);

        assertNotNull(script);
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScriptException() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 2, script);
    }
    
    @Test
    public void testSnakeClass() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 3, 0);
        
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
}