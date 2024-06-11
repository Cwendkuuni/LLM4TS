import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections4.sequence.SequencesComparator;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.functors.DefaultEquator;

import java.util.ArrayList;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Character> sequence1 = new ArrayList<>();
        sequence1.add('a');
        sequence1.add('b');
        List<Character> sequence2 = new ArrayList<>();
        sequence2.add('a');
        sequence2.add('c');

        SequencesComparator<Character> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Character> script = comparator.getScript();

        assertNotNull(script);
        // Add more assertions based on the expected result
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 2, 2);

        assertNotNull(snake);
        // Add more assertions based on the expected result
    }

    @Test
    public void testGetMiddleSnake() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("hello");
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("world");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake middleSnake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());

        assertNull(middleSnake);
        // Add more assertions based on the expected result
    }

    @Test
    public void testBuildScript() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("apple");
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("banana");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);

        // Add assertions based on the expected result
    }
}