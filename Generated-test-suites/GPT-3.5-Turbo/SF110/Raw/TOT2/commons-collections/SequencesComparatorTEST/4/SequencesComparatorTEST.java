
import org.junit.Test;
import static org.junit.Assert.*;

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
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();
        Equator<String> equator = new DefaultEquator<>();
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertNotNull(comparator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }

    @Test
    public void testConstructorEmptySequences() {
        List<Character> sequence1 = new ArrayList<>();
        List<Character> sequence2 = new ArrayList<>();
        SequencesComparator<Character> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator);
    }

    @Test
    public void testConstructorDifferentLengths() {
        List<Double> sequence1 = Arrays.asList(1.0, 2.0);
        List<Double> sequence2 = Arrays.asList(1.0, 2.0, 3.0);
        Equator<Double> equator = new CustomEquator<>();
        SequencesComparator<Double> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertNotNull(comparator);
    }

    @Test(expected = CustomEquatorException.class)
    public void testConstructorCustomEquatorException() {
        List<String> sequence1 = Arrays.asList("a", "b");
        List<String> sequence2 = Arrays.asList("b", "c");
        Equator<String> equator = new CustomEquator<>();
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
    }
}
