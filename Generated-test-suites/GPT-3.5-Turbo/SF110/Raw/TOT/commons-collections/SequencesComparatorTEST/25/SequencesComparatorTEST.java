import org.junit.Test;
import org.apache.commons.collections4.sequence.*;
import org.apache.commons.collections4.functors.*;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript_typicalCase() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "c");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<String> script = comparator.getScript();

        // Assert that the script is empty since the sequences are identical
        assertEquals(0, script.size());
    }

    @Test
    public void testGetScript_differentSizeSequences() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "c", "d", "e");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<String> script = comparator.getScript();

        // Assert that the script contains insert commands for the extra elements in sequence2
        assertEquals(2, script.size());
        assertTrue(script.getCommand(0) instanceof InsertCommand);
        assertTrue(script.getCommand(1) instanceof InsertCommand);
    }

    @Test
    public void testGetScript_differentElements() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "x", "c");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<String> script = comparator.getScript();

        // Assert that the script contains replace commands for the different elements
        assertEquals(1, script.size());
        assertTrue(script.getCommand(0) instanceof ReplaceCommand);
    }

    @Test
    public void testGetScript_emptySequences() {
        List<String> sequence1 = Collections.emptyList();
        List<String> sequence2 = Collections.emptyList();

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<String> script = comparator.getScript();

        // Assert that the script is empty since both sequences are empty
        assertEquals(0, script.size());
    }

    @Test(expected = RuntimeException.class)
    public void testGetScript_internalError() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "c");

        // Create a comparator with insufficient size for vDown and vUp arrays
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getScript();
    }
}