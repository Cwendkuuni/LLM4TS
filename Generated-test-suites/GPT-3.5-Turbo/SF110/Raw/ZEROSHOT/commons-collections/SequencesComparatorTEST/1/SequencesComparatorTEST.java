import org.apache.commons.collections4.equator.DefaultEquator;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(4);
        sequence2.add(5);
        sequence2.add(6);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();

        assertEquals(3, script.getCommands().size());
        assertEquals(new KeepCommand<>(1), script.getCommands().get(0));
        assertEquals(new KeepCommand<>(2), script.getCommands().get(1));
        assertEquals(new KeepCommand<>(3), script.getCommands().get(2));
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(4);
        sequence2.add(5);
        sequence2.add(6);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());

        assertEquals(0, snake.getStart());
        assertEquals(sequence1.size(), snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(4);
        sequence2.add(5);
        sequence2.add(6);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        Snake middleSnake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());

        assertEquals(null, middleSnake);
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(4);
        sequence2.add(5);
        sequence2.add(6);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);

        assertEquals(6, script.getCommands().size());
        assertEquals(new DeleteCommand<>(1), script.getCommands().get(0));
        assertEquals(new DeleteCommand<>(2), script.getCommands().get(1));
        assertEquals(new InsertCommand<>(4), script.getCommands().get(2));
        assertEquals(new InsertCommand<>(5), script.getCommands().get(3));
        assertEquals(new InsertCommand<>(6), script.getCommands().get(4));
        assertEquals(new KeepCommand<>(3), script.getCommands().get(5));
    }
}