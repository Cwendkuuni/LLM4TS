import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructorWithDefaultEquator() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator);
    }

    @Test
    public void testConstructorWithCustomEquator() {
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();
        Equator<String> equator = new Equator<String>() {
            @Override
            public boolean equate(String o1, String o2) {
                return o1.contains(o2);
            }

            @Override
            public int hash(String o) {
                return o.hashCode();
            }
        };
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertNotNull(comparator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
        assertEquals(3, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
    }

    @Test
    public void testBuildSnake() {
        List<String> sequence1 = Arrays.asList("a", "b", "c", "d");
        List<String> sequence2 = Arrays.asList("a", "c", "d", "e");
        Equator<String> equator = new Equator<String>() {
            @Override
            public boolean equate(String o1, String o2) {
                return o1.equals(o2);
            }

            @Override
            public int hash(String o) {
                return o.hashCode();
            }
        };
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        List<String> sequence1 = Arrays.asList("a", "b", "c", "d");
        List<String> sequence2 = Arrays.asList("a", "c", "d", "e");
        Equator<String> equator = new Equator<String>() {
            @Override
            public boolean equate(String o1, String o2) {
                return o1.equals(o2);
            }

            @Override
            public int hash(String o) {
                return o.hashCode();
            }
        };
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        assertNotNull(script);
        assertEquals(3, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScriptWithInvalidInput() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence1.size(), script);
    }

    @Test
    public void testSnakeClass() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 2, 0);
        assertEquals(0, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

}