import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.DefaultEquator;

public class SequencesComparatorTest {
    @Test
    public void testConstructor() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
        
        assertEquals(8, comparator.vDown.length);
        assertEquals(8, comparator.vUp.length);
    }
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(3, 4, 5));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        
        // Assert the contents of the script
        assertEquals(4, script.size());
        assertTrue(script.getCommand(0) instanceof InsertCommand);
        assertEquals(3, script.getCommand(0).getObject());
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
        assertEquals(1, script.getCommand(1).getObject());
        assertTrue(script.getCommand(2) instanceof KeepCommand);
        assertEquals(2, script.getCommand(2).getObject());
        assertTrue(script.getCommand(3) instanceof InsertCommand);
        assertEquals(4, script.getCommand(3).getObject());
    }
    
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(3, 4, 5));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.buildSnake(0, 0, 3, 3);
        
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(3, 4, 5));

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake middle = comparator.getMiddleSnake(0, 3, 0, 3);
        
        assertEquals(1, middle.getStart());
        assertEquals(2, middle.getEnd());
        assertEquals(1, middle.getDiag());
    }
    
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(3, 4, 5));
        EditScript<Integer> script = new EditScript<>();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.buildScript(0, 3, 0, 3, script);
        
        // Assert the contents of the script
        assertEquals(4, script.size());
        assertTrue(script.getCommand(0) instanceof InsertCommand);
        assertEquals(3, script.getCommand(0).getObject());
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
        assertEquals(1, script.getCommand(1).getObject());
        assertTrue(script.getCommand(2) instanceof KeepCommand);
        assertEquals(2, script.getCommand(2).getObject());
        assertTrue(script.getCommand(3) instanceof InsertCommand);
        assertEquals(4, script.getCommand(3).getObject());
    }
}