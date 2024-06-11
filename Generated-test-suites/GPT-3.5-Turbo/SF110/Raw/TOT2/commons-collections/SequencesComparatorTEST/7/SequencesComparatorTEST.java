import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }

    @Test
    public void testBuildSnake() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, list1.size(), list2.size());
        assertNotNull(snake);
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());
        assertNull(snake);
    }

    @Test
    public void testBuildScript() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, list1.size(), 0, list2.size(), script);
        assertNotNull(script);
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScriptError() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 1, 0, 1, script); // Force internal error
    }

}