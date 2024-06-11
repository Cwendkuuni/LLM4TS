import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.KeepCommand;
import org.apache.commons.collections4.sequence.DeleteCommand;
import org.apache.commons.collections4.sequence.InsertCommand;
import org.apache.commons.collections4.sequence.SequencesComparator;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();

        assertTrue(script.isEmpty());
    }

    @Test
    public void testEqualSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();

        assertTrue(script.isEmpty());
    }

    @Test
    public void testDifferentSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();

        assertEquals(6, script.getCommands().size());
        assertTrue(script.getCommands().get(0) instanceof DeleteCommand);
        assertTrue(script.getCommands().get(1) instanceof DeleteCommand);
        assertTrue(script.getCommands().get(2) instanceof DeleteCommand);
        assertTrue(script.getCommands().get(3) instanceof InsertCommand);
        assertTrue(script.getCommands().get(4) instanceof InsertCommand);
        assertTrue(script.getCommands().get(5) instanceof InsertCommand);
    }

    @Test
    public void testEmptySequence1() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();

        assertEquals(sequence2.size(), script.getCommands().size());
        for (int i = 0; i < sequence2.size(); i++) {
            assertTrue(script.getCommands().get(i) instanceof InsertCommand);
        }
    }

    @Test
    public void testEmptySequence2() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();

        assertEquals(sequence1.size(), script.getCommands().size());
        for (int i = 0; i < sequence1.size(); i++) {
            assertTrue(script.getCommands().get(i) instanceof DeleteCommand);
        }
    }

}