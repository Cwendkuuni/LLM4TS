import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(2);

        SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = sequencesComparator.getScript();

        // Test that the script is not null
        assertNotNull(script);

        // Add more asserts based on specific use cases
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(3);

        SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = sequencesComparator.buildSnake(0, 0, sequence1.size(), sequence2.size());

        // Test that the snake is not null
        assertNotNull(snake);

        // Add more asserts based on specific use cases
    }

    // Add more test methods for other public methods and edge cases

}