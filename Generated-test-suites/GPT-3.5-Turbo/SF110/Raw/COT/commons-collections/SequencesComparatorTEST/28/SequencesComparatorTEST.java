import org.junit.Test;
import java.util.List;
import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.equator.DefaultEquator;
import org.apache.commons.collections4.equator.Equator;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructorWithDefaultEquator() {
        List<Integer> sequence1 = List.of(1, 2, 3);
        List<Integer> sequence2 = List.of(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator);
    }

    @Test
    public void testConstructorWithCustomEquator() {
        List<String> sequence1 = List.of("a", "b", "c");
        List<String> sequence2 = List.of("a", "b", "c");
        Equator<String> equator = new Equator<String>() {
            @Override
            public boolean equate(String o1, String o2) {
                return o1.equalsIgnoreCase(o2);
            }
        };
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertNotNull(comparator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = List.of(1, 2, 3);
        List<Integer> sequence2 = List.of(1, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScriptThrowsRuntimeException() {
        List<Integer> sequence1 = List.of(1, 2, 3);
        List<Integer> sequence2 = List.of(1, 2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = List.of(1, 2, 3);
        List<Integer> sequence2 = List.of(1, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 3);
        assertNotNull(snake);
    }

    @Test
    public void testGetMiddleSnakeWithNullResult() {
        List<Integer> sequence1 = List.of(1, 2, 3);
        List<Integer> sequence2 = List.of(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 0, 3, 0);
        assertNull(snake);
    }

    @Test
    public void testGetMiddleSnakeWithNonNullResult() {
        List<Integer> sequence1 = List.of(1, 2, 3);
        List<Integer> sequence2 = List.of(1, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
        assertNotNull(snake);
    }

    @Test
    public void testBuildScriptWithEmptySequences() {
        List<Integer> sequence1 = List.of();
        List<Integer> sequence2 = List.of();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertTrue(script.isEmpty());
    }

    @Test
    public void testBuildScriptWithEqualSequences() {
        List<Integer> sequence1 = List.of(1, 2, 3);
        List<Integer> sequence2 = List.of(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertTrue(script.isEmpty());
    }

    @Test
    public void testBuildScriptWithDifferentSequences() {
        List<Integer> sequence1 = List.of(1, 2, 3);
        List<Integer> sequence2 = List.of(1, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertFalse(script.isEmpty());
    }
}