import static org.junit.Assert.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        assertNotNull(comparator);
    }

    @Test
    public void testGetScript() {
        List<String> list1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> list2 = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2);
        EditScript<String> script = comparator.getScript();
        assertNotNull(script);
    }

    @Test
    public void testBuildSnake() {
        List<Character> list1 = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
        List<Character> list2 = new ArrayList<>(Arrays.asList('a', 'c', 'd'));
        SequencesComparator<Character> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.buildSnake(0, 0, 3, 3);
        assertNotNull(snake);
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
        Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
        assertNotNull(snake);
    }

    @Test
    public void testBuildScript() {
        List<String> list1 = new ArrayList<>(Arrays.asList("apple", "banana", "carrot"));
        List<String> list2 = new ArrayList<>(Arrays.asList("banana", "apple", "mango"));
        SequencesComparator<String> comparator = new SequencesComparator<>(list1, list2);
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 3, script);
        assertNotNull(script);
    }

}