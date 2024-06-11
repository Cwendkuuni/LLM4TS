
import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {
  
    @Test
    public void testConstructor_withTwoSequences() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
    }
    
    @Test
    public void testConstructor_withEquator() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);
        
        Equator<Integer> equator = new Equator<>();
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
    }
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        // Test the content of the script
        // ...
    }
}
