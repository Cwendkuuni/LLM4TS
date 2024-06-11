import static org.junit.Assert.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructorWithoutEquator() {
        List<Integer> seq1 = Arrays.asList(1, 2, 3);
        List<Integer> seq2 = Arrays.asList(1, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
        assertNotNull(comparator);
    }

    @Test
    public void testConstructorWithEquator() {
        List<String> seq1 = Arrays.asList("a", "b", "c");
        List<String> seq2 = Arrays.asList("a", "c", "d");
        SequencesComparator<String> comparator = new SequencesComparator<>(seq1, seq2, new Equator<String>() {
            @Override
            public boolean equate(String o1, String o2) {
                return o1.equals(o2);
            }
        });
        assertNotNull(comparator);
    }

    @Test
    public void testGetScript() {
        List<Integer> seq1 = Arrays.asList(1, 2, 3);
        List<Integer> seq2 = Arrays.asList(1, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }
}