import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor_WithEmptySequences_ShouldInitializeCorrectly() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(new ArrayList<>(), new ArrayList<>());

        assertEquals(0, comparator.getScript().size());
    }

    @Test
    public void testConstructor_WithNonEmptySequences_ShouldInitializeCorrectly() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        assertEquals(3, comparator.getScript().size());
    }

    @Test
    public void testGetScript_WithEqualSequences_ShouldReturnEmptyScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        assertEquals(0, comparator.getScript().size());
    }

    @Test
    public void testGetScript_WithDifferentSequences_ShouldReturnCorrectScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(2, script.size());
        assertTrue(script.get(0) instanceof KeepCommand);
        assertTrue(script.get(1) instanceof InsertCommand);
        assertEquals(3, (int) script.get(0).getObject());
        assertEquals(4, (int) script.get(1).getObject());
    }

    @Test
    public void testBuildSnake_WithValidIndices_ShouldReturnCorrectSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());

        assertEquals(0, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake_WithValidIndices_ShouldReturnCorrectSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());

        assertEquals(0, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test(expected = RuntimeException.class)
    public void testGetMiddleSnake_WithInvalidIndices_ShouldThrowException() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getMiddleSnake(1, sequence1.size(), 0, sequence2.size());
    }

    @Test
    public void testBuildScript_WithEqualSequences_ShouldReturnEmptyScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();

        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);

        assertEquals(0, script.size());
    }

    @Test
    public void testBuildScript_WithDifferentSequences_ShouldReturnCorrectScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();

        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);

        assertEquals(2, script.size());
        assertTrue(script.get(0) instanceof KeepCommand);
        assertTrue(script.get(1) instanceof InsertCommand);
        assertEquals(3, (int) script.get(0).getObject());
        assertEquals(4, (int) script.get(1).getObject());
    }

    @Test
    public void testCompleteFunctionalFlow_WithValidSequences_ShouldReturnCorrectScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(2, script.size());
        assertTrue(script.get(0) instanceof KeepCommand);
        assertTrue(script.get(1) instanceof InsertCommand);
        assertEquals(3, (int) script.get(0).getObject());
        assertEquals(4, (int) script.get(1).getObject());
    }
}