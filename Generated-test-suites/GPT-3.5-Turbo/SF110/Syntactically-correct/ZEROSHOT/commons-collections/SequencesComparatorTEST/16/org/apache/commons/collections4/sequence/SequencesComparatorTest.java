import org.junit.Test;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        // Assert statements
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.buildSnake(0, 0, 0, 0);
        // Assert statements
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake middleSnake = comparator.getMiddleSnake(0, 0, 0, 0);
        // Assert statements
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        EditScript<Integer> script = new EditScript<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.buildScript(0, 0, 0, 0, script);
        // Assert statements
    }

    @Test
    public void testSnakeGetStart() {
        Snake snake = new Snake(0, 0, 0);
        int start = snake.getStart();
        // Assert statements
    }

    @Test
    public void testSnakeGetEnd() {
        Snake snake = new Snake(0, 0, 0);
        int end = snake.getEnd();
        // Assert statements
    }

    @Test
    public void testSnakeGetDiag() {
        Snake snake = new Snake(0, 0, 0);
        int diag = snake.getDiag();
        // Assert statements
    }
}