import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SequencesComparatorTest {

    private SequencesComparator<Integer> comparator;

    @Before
    public void setUp() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(4);
        sequence2.add(3);

        comparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test
    public void testGetScript() {
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
        // Additional assertions for script correctness
    }

    @Test
    public void testBuildSnake() {
        int start = 0;
        int diag = 0;
        int end1 = 3;
        int end2 = 3;

        Snake snake = comparator.buildSnake(start, diag, end1, end2);

        assertNotNull(snake);
        assertEquals(start, snake.getStart());
        // Additional assertions for snake correctness
    }

    @Test
    public void testGetMiddleSnake() {
        int start1 = 0;
        int end1 = 3;
        int start2 = 0;
        int end2 = 3;

        Snake middleSnake = comparator.getMiddleSnake(start1, end1, start2, end2);

        assertNull(middleSnake);
        // Additional assertions for middleSnake correctness
    }

    @Test
    public void testBuildScript() {
        int start1 = 0;
        int end1 = 3;
        int start2 = 0;
        int end2 = 3;

        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(start1, end1, start2, end2, script);

        // Assertions for script correctness
    }
    
    // Additional test cases for edge cases and exception handling
    // ...
}

