import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor1() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertNotNull(comparator.equator);
        assertEquals(7, comparator.vDown.length);
        assertEquals(7, comparator.vUp.length);
    }

    @Test
    public void testConstructor2() {
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = Arrays.asList("a", "b", "c");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, new CaseInsensitiveEquator<>());
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertNotNull(comparator.equator);
        assertEquals(7, comparator.vDown.length);
        assertEquals(7, comparator.vUp.length);
    }

    @Test
    public void testGetScript_emptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
        assertTrue(script.isEmpty());
    }

    @Test
    public void testGetScript_equalSequences() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "c");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();
        assertNotNull(script);
        assertTrue(script.isEmpty());
    }

    @Test
    public void testGetScript_differentSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
        assertEquals(6, script.size());
        assertTrue(script.getCommand(0) instanceof InsertCommand);
        assertTrue(script.getCommand(1) instanceof InsertCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
        assertTrue(script.getCommand(3) instanceof DeleteCommand);
        assertTrue(script.getCommand(4) instanceof DeleteCommand);
        assertTrue(script.getCommand(5) instanceof DeleteCommand);
    }

    @Test(expected = RuntimeException.class)
    public void testGetScript_internalError() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getScript();
    }

}