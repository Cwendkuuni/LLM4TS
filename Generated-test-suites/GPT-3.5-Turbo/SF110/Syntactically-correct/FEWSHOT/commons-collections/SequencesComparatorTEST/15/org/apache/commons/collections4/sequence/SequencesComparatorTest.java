import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;

public class SequencesComparatorTest {
    @Test
    public void testConstructor_OneArg() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
        assertEquals(8, comparator.vDown.length);
        assertEquals(8, comparator.vUp.length);
    }
    
    @Test
    public void testConstructor_TwoArgs() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                return o1.equals(o2);
            }
            
            @Override
            public int hash(Integer o) {
                return o.hashCode();
            }
        };
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
        assertEquals(8, comparator.vDown.length);
        assertEquals(8, comparator.vUp.length);
    }
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertNull(script);
    }
    
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 4);
        
        assertEquals(0, snake.getStart());
        assertEquals(4, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 4);
        
        assertEquals(0, snake.getStart());
        assertEquals(4, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        
        comparator.buildScript(0, 3, 0, 4, script);
        
        assertNull(script);
    }
    
    @Test
    public void testSnakeGetters() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 4, 0);
        
        assertEquals(0, snake.getStart());
        assertEquals(4, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
}