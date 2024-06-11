import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructorWithDefaultEquator() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        assertNotNull(comparator);
    }

    @Test
    public void testConstructorWithCustomEquator() {
        Equator<Integer> customEquator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                return o1 % 2 == o2 % 2;
            }

            @Override
            public int hash(Integer o) {
                return o.hashCode() % 2;
            }
        };

        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3), customEquator);
        assertNotNull(comparator);
    }

    @Test
    public void testGetScriptWithEqualSequences() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        EditScript<Integer> script = comparator.getScript();

        assertTrue(script.isEmpty());
    }

    @Test
    public void testGetScriptWithDifferentSequences() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        EditScript<Integer> script = comparator.getScript();

        assertEquals(script.getCommands().size(), 6);
        assertTrue(script.getCommands().get(0) instanceof InsertCommand);
        assertTrue(script.getCommands().get(1) instanceof InsertCommand);
        assertTrue(script.getCommands().get(2) instanceof InsertCommand);
        assertTrue(script.getCommands().get(3) instanceof DeleteCommand);
        assertTrue(script.getCommands().get(4) instanceof DeleteCommand);
        assertTrue(script.getCommands().get(5) instanceof DeleteCommand);
    }

    @Test
    public void testBuildSnakeWithEqualSequences() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        Snake snake = comparator.buildSnake(0, 0, 3, 3);

        assertEquals(snake.getStart(), 3);
        assertEquals(snake.getEnd(), 3);
        assertEquals(snake.getDiag(), 0);
    }

    @Test
    public void testBuildSnakeWithDifferentSequences() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        Snake snake = comparator.buildSnake(0, 0, 3, 3);

        assertEquals(snake.getStart(), 1);
        assertEquals(snake.getEnd(), 1);
        assertEquals(snake.getDiag(), 0);
    }

    @Test
    public void testGetMiddleSnakeWithEqualSequences() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);

        assertNull(snake);
    }

    @Test
    public void testGetMiddleSnakeWithDifferentSequences() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);

        assertEquals(snake.getStart(), 1);
        assertEquals(snake.getEnd(), 1);
        assertEquals(snake.getDiag(), 0);
    }

    @Test
    public void testBuildScriptWithEqualSequences() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        EditScript<Integer> script = new EditScript<>();

        comparator.buildScript(0, 3, 0, 3, script);
        assertTrue(script.isEmpty());
    }

    @Test
    public void testBuildScriptWithDifferentSequences() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        EditScript<Integer> script = new EditScript<>();

        comparator.buildScript(0, 3, 0, 3, script);
        assertEquals(script.getCommands().size(), 6);
        assertTrue(script.getCommands().get(0) instanceof InsertCommand);
        assertTrue(script.getCommands().get(1) instanceof InsertCommand);
        assertTrue(script.getCommands().get(2) instanceof InsertCommand);
        assertTrue(script.getCommands().get(3) instanceof DeleteCommand);
        assertTrue(script.getCommands().get(4) instanceof DeleteCommand);
        assertTrue(script.getCommands().get(5) instanceof DeleteCommand);
    }
}