import static org.junit.Assert.*;
import org.apache.commons.collections4.sequence.*;
import org.apache.commons.collections4.functors.*;
import java.util.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        List<Integer> sequence2 = new ArrayList<Integer>();
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        assertNotNull(comparator);
    }

    @Test
    public void testConstructorWithEquator() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        List<Integer> sequence2 = new ArrayList<Integer>();
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2, equator);
        assertNotNull(comparator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(6, 7, 8, 9, 10));
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(6, 7, 8, 9, 10));
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        assertNotNull(snake);
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(6, 7, 8, 9, 10));
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        Snake middleSnake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertNotNull(middleSnake);
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(6, 7, 8, 9, 10));
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<Integer>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        assertNotNull(script);
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScriptInternalError() {
        List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(6, 7, 8, 9, 10));
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<Integer>();
        comparator.buildScript(0, sequence1.size(), 0, sequence1.size(), script);
    }

    @Test
    public void testKeepCommand() {
        Integer obj = 5;
        KeepCommand<Integer> command = new KeepCommand<Integer>(obj);
        assertNotNull(command);
    }

    @Test
    public void testDeleteCommand() {
        Integer obj = 5;
        DeleteCommand<Integer> command = new DeleteCommand<Integer>(obj);
        assertNotNull(command);
    }

    @Test
    public void testInsertCommand() {
        Integer obj = 5;
        InsertCommand<Integer> command = new InsertCommand<Integer>(obj);
        assertNotNull(command);
    }
}