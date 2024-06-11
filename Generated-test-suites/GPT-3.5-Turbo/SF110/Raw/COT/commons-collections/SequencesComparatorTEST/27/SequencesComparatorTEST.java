
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections4.equators.DefaultEquator;
import java.util.*;

public class SequencesComparatorTest {
    
    @Test
    public void testConstructor() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator);
    }
    
    @Test
    public void testConstructorWithEquator() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        assertNotNull(comparator);
    }
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }
    
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 3);
        assertNotNull(snake);
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
        assertNotNull(snake);
    }
    
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 3, script);
        assertNotNull(script);
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScriptWithException() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 2, script);
    }
    
    @Test
    public void testSnakeGetStart() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 3, 0);
        assertEquals(0, snake.getStart());
    }
    
    @Test
    public void testSnakeGetEnd() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 3, 0);
        assertEquals(3, snake.getEnd());
    }
    
    @Test
    public void testSnakeGetDiag() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 3, 0);
        assertEquals(0, snake.getDiag());
    }
    
}
