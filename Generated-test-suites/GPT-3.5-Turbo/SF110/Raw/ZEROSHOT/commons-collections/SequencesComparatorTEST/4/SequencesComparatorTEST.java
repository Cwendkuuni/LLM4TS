import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testSequencesComparator() {
        List<String> sequence1 = new ArrayList<String>();
        List<String> sequence2 = new ArrayList<String>();
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        
        assertNotNull(comparator);
    }
    
    @Test
    public void testSequencesComparatorWithEquator() {
        List<String> sequence1 = new ArrayList<String>();
        List<String> sequence2 = new ArrayList<String>();
        Equator<String> equator = new DefaultEquator<String>();
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2, equator);
        
        assertNotNull(comparator);
    }
    
    @Test
    public void testGetScript() {
        List<String> sequence1 = new ArrayList<String>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        List<String> sequence2 = new ArrayList<String>();
        sequence2.add("a");
        sequence2.add("d");
        sequence2.add("c");
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        
        EditScript<String> script = comparator.getScript();
        
        assertNotNull(script);
        assertEquals(3, script.getCommands().size());
    }
    
    @Test
    public void testBuildSnake() {
        List<String> sequence1 = new ArrayList<String>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        List<String> sequence2 = new ArrayList<String>();
        sequence2.add("a");
        sequence2.add("d");
        sequence2.add("c");
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 3);
        
        assertNotNull(snake);
        assertEquals(3, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<String> sequence1 = new ArrayList<String>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        List<String> sequence2 = new ArrayList<String>();
        sequence2.add("a");
        sequence2.add("d");
        sequence2.add("c");
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
        
        assertNotNull(snake);
        assertEquals(1, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testBuildScript() {
        List<String> sequence1 = new ArrayList<String>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        List<String> sequence2 = new ArrayList<String>();
        sequence2.add("a");
        sequence2.add("d");
        sequence2.add("c");
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        
        EditScript<String> script = new EditScript<String>();
        
        comparator.buildScript(0, 3, 0, 3, script);
        
        assertNotNull(script);
        assertEquals(3, script.getCommands().size());
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScriptWithInvalidInput() {
        List<String> sequence1 = new ArrayList<String>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        List<String> sequence2 = new ArrayList<String>();
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        
        EditScript<String> script = new EditScript<String>();
        
        comparator.buildScript(0, 3, 0, 0, script);
    }
    
    @Test
    public void testSnakeGetters() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 2, 0);
        
        assertEquals(1, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
}