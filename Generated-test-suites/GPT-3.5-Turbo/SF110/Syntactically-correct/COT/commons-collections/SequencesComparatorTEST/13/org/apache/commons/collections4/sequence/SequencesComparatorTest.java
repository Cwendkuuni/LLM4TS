
import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor2() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator);
    }

    @Test
    public void testConstructor3() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        Equator<Integer> equator = new DefaultEquator<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertNotNull(comparator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }
    
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 3, 4));
        Equator<Integer> equator = new DefaultEquator<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        assertNotNull(snake);
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 3, 4));
        Equator<Integer> equator = new DefaultEquator<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNotNull(snake);
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 3, 4));
        Equator<Integer> equator = new DefaultEquator<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        assertNotNull(script);
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScriptException() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 3, 4));
        Equator<Integer> equator = new DefaultEquator<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = new EditScript<>();
        try {
            comparator.buildScript(-1, sequence1.size(), -1, sequence2.size(), script);
        } catch (RuntimeException e) {
            assertEquals("Internal Error", e.getMessage());
            throw e;
        }
    }
}
