import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator);
    }

    @Test
    public void testConstructorWithEquator() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                return o1.equals(o2);
            }

            @Override
            public int hash(Integer o) {
                return o.hashCode();
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertNotNull(comparator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(2, script.getCommands().size());
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 2);
        assertEquals(3, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 2);
        assertNotNull(snake);
    }

    @Test(expected = RuntimeException.class)
    public void testGetMiddleSnakeException() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getMiddleSnake(0, 3, 0, 0);
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 2, script);
        assertEquals(2, script.getCommands().size());
    }

    @Test
    public void testSnakeGetStart() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 3, 2);
        assertEquals(1, snake.getStart());
    }

    @Test
    public void testSnakeGetEnd() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 3, 2);
        assertEquals(3, snake.getEnd());
    }

    @Test
    public void testSnakeGetDiag() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 3, 2);
        assertEquals(2, snake.getDiag());
    }
}