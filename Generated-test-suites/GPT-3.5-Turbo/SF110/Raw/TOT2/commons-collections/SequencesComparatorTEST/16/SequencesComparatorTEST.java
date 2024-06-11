import static org.junit.Assert.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        List<Integer> seq1 = Arrays.asList(1, 2, 3);
        List<Integer> seq2 = Arrays.asList(1, 2, 4);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
        
        assertNotNull(comparator);
    }
}