
import org.junit.Test;

import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<String> seq1 = Arrays.asList("A", "B", "C");
        List<String> seq2 = Arrays.asList("A", "C", "D");

        SequencesComparator<String> comparator = new SequencesComparator<>(seq1, seq2);

        EditScript<String> script = comparator.getScript();

        assertNotNull(script);
        assertEquals(3, script.size());
    }

    @Test
    public void testBuildSnake() {
        List<Integer> seq1 = Arrays.asList(1, 2, 3);
        List<Integer> seq2 = Arrays.asList(1, 2, 4);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);

        Snake snake = comparator.buildSnake(0, 0, 3, 3);

        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testBuildScript() {
        List<Character> seq1 = Arrays.asList('A', 'B', 'C');
        List<Character> seq2 = Arrays.asList('A', 'C', 'D');

        SequencesComparator<Character> comparator = new SequencesComparator<>(seq1, seq2);

        EditScript<Character> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 3, script);

        assertNotNull(script);
        assertEquals(3, script.size());
    }

}