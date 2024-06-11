
import static org.junit.Assert.*;
import org.junit.Test;

public class SequencesComparatorTest {
    
    @Test
    public void testConstructor() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        sequence2.add("b");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertNotNull(comparator.equator);
        assertNotNull(comparator.vDown);
        assertNotNull(comparator.vUp);
    }
    
    @Test
    public void testConstructorWithEquator() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        sequence2.add("b");
        
        Equator<String> equator = DefaultEquator.defaultEquator();
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
        assertNotNull(comparator.vDown);
        assertNotNull(comparator.vUp);
    }
    
    @Test
    public void testGetScript() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        sequence2.add("b");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<String> script = comparator.getScript();
        
        // assert something about the script
    }
    
    @Test
    public void testBuildSnake() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        sequence2.add("b");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        
        // assert something about the snake
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        sequence2.add("b");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        
        // assert something about the snake
    }
    
    @Test
    public void testBuildScript() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        sequence2.add("b");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<String> script = new EditScript<>();
        
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        
        // assert something about the script
    }

}

