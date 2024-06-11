import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor_sequence1Sequence2Equator() {
        // Test case to check if the sequence1, sequence2, and equator are initialized correctly
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();
        Equator<String> equator = DefaultEquator.defaultEquator();
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
    }

    @Test
    public void testConstructor_sequence1Sequence2() {
        // Test case to check if the sequence1, sequence2, and default equator are initialized correctly
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
    }

    @Test
    public void testGetScript() {
        // Test case to check if getScript() returns a valid EditScript object
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();
        sequence1.add("A");
        sequence1.add("B");
        sequence1.add("C");
        sequence2.add("A");
        sequence2.add("D");
        sequence2.add("B");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<String> script = comparator.getScript();
        
        assertNotNull(script);
    }

    @Test
    public void testBuildSnake() {
        // Test case to check if buildSnake returns a valid Snake object
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();
        sequence1.add("A");
        sequence1.add("B");
        sequence2.add("A");
        sequence2.add("D");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake snake = comparator.buildSnake(0, 0, 2, 2);
        
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        // Test case to check if getMiddleSnake returns a valid Snake object
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();
        sequence1.add("A");
        sequence1.add("B");
        sequence2.add("A");
        sequence2.add("D");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake snake = comparator.getMiddleSnake(0, 2, 0, 2);
        
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testBuildScript() {
        // Test case to check if buildScript returns a valid EditScript object
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();
        sequence1.add("A");
        sequence2.add("B");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<String> script = new EditScript<>();
        
        comparator.buildScript(0, 1, 0, 1, script);
        
        assertNotNull(script);
    }
}