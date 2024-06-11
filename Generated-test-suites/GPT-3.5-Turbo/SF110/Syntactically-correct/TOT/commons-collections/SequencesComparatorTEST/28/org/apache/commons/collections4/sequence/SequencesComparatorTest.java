import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript_EmptySequences() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>();

        EditScript<Integer> script = comparator.getScript();

        assertEquals(0, script.size());
    }

    @Test
    public void testGetScript_OneEmptySequence() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>();
        List<Integer> sequence = Arrays.asList(1, 2, 3, 4, 5);

        EditScript<Integer> script = comparator.getScript();

        assertEquals(5, script.size());
        assertTrue(script.getCommand(0) instanceof DeleteCommand);
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
        assertTrue(script.getCommand(2) instanceof DeleteCommand);
        assertTrue(script.getCommand(3) instanceof DeleteCommand);
        assertTrue(script.getCommand(4) instanceof DeleteCommand);
    }

    @Test
    public void testGetScript_SameSequences() {
        List<Integer> sequence = Arrays.asList(1, 2, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence, sequence);

        EditScript<Integer> script = comparator.getScript();

        assertEquals(5, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof KeepCommand);
        assertTrue(script.getCommand(2) instanceof KeepCommand);
        assertTrue(script.getCommand(3) instanceof KeepCommand);
        assertTrue(script.getCommand(4) instanceof KeepCommand);
    }

    @Test
    public void testGetScript_DifferentSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(6, 7, 8, 9, 10);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();

        assertEquals(10, script.size());
        assertTrue(script.getCommand(0) instanceof InsertCommand);
        assertTrue(script.getCommand(1) instanceof InsertCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
        assertTrue(script.getCommand(3) instanceof InsertCommand);
        assertTrue(script.getCommand(4) instanceof InsertCommand);
        assertTrue(script.getCommand(5) instanceof InsertCommand);
        assertTrue(script.getCommand(6) instanceof InsertCommand);
        assertTrue(script.getCommand(7) instanceof InsertCommand);
        assertTrue(script.getCommand(8) instanceof InsertCommand);
        assertTrue(script.getCommand(9) instanceof InsertCommand);
    }

    @Test
    public void testGetScript_DifferentSizes() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();

        assertEquals(2, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
    }

    @Test(expected = RuntimeException.class)
    public void testGetScript_InternalError() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(6, 7, 8, 9, 10);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                throw new RuntimeException();
            }

            @Override
            public int hash(Integer o) {
                return 0;
            }
        });

        comparator.getScript(); // should throw RuntimeException
    }
}