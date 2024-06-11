import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("a", "c", "d"));

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();

        assertNotNull(script);
        // Add more assertions if needed
    }

    @Test
    public void testBuildMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4, 5));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());

        assertNotNull(snake);
        assertEquals(2, snake.getStart());
        assertEquals(4, snake.getEnd());
        assertEquals(1, snake.getDiag());
        // Add more assertions if needed
    }

    // Add more test cases for other methods as needed

}