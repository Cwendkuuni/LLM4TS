import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript_EmptySequences_ReturnsEmptyScript() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertNotNull(script);
        assertTrue(script.isEmpty());
    }

    @Test
    public void testGetScript_IdenticalSequences_ReturnsKeepCommands() {
        List<String> sequence1 = Arrays.asList("A", "B", "C");
        List<String> sequence2 = Arrays.asList("A", "B", "C");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();

        assertNotNull(script);
        assertEquals(3, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof KeepCommand);
        assertTrue(script.getCommand(2) instanceof KeepCommand);
    }

    @Test
    public void testGetScript_InsertionAndDeletion_ReturnsDeleteAndInsertCommands() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 4, 2, 5, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertNotNull(script);
        assertEquals(4, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
        assertTrue(script.getCommand(3) instanceof KeepCommand);
    }

    @Test(expected = RuntimeException.class)
    public void testGetScript_InvalidSequences_ThrowsException() {
        List<Integer> sequence1 = null;
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getScript();
    }

    // ... Add more test cases for other methods if necessary ...

    // 
    // ... Add more test cases for other methods ...
    // 
}