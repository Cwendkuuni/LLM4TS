
import static org.junit.Assert.*;

import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;

public class SequencesComparatorTest {
    
    @Test
    public void testConstructor() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(new ArrayList<>(), new ArrayList<>());
        assertNotNull(comparator);
    }
    
    @Test
    public void testConstructorWithEquator() {
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(new ArrayList<>(), new ArrayList<>(), equator);
        assertNotNull(comparator);
    }
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }
    
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        assertNotNull(snake);
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        SequencesComparator.Snake middleSnake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNull(middleSnake);
    }
    
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        assertNotNull(script);
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScriptInvalidInput() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 0, 0, 0, script);
    }
    
    @Test
    public void testSnakeStart() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 0, 0);
        assertEquals(0, snake.getStart());
    }
    
    @Test
    public void testSnakeEnd() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 1, 0);
        assertEquals(1, snake.getEnd());
    }
    
    @Test
    public void testSnakeDiag() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 0, 1);
        assertEquals(1, snake.getDiag());
    }
}
