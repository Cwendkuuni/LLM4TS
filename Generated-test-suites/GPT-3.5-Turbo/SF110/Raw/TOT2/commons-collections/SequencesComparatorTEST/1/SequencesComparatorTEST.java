import static org.junit.Assert.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructor1() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        assertNotNull(comparator);
    }

    @Test
    public void testConstructor2() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        Equator<? super Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);

        assertNotNull(comparator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();

        assertNotNull(script);
    }

    // Add more test cases for other methods as needed

}