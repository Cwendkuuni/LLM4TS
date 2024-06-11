import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;
import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.functors.DefaultEquator;

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
        
        assertEquals(6, script.size());
        assertEquals(new KeepCommand<>(1), script.getCommand(0));
        assertEquals(new DeleteCommand<>(2), script.getCommand(1));
        assertEquals(new DeleteCommand<>(3), script.getCommand(2));
        assertEquals(new InsertCommand<>(4), script.getCommand(3));
        assertEquals(new InsertCommand<>(5), script.getCommand(4));
        assertEquals(new InsertCommand<>(6), script.getCommand(5));
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
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence1.add(4);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        
        assertEquals(2, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
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
        
        assertEquals(6, script.size());
        assertEquals(new KeepCommand<>(1), script.getCommand(0));
        assertEquals(new DeleteCommand<>(2), script.getCommand(1));
        assertEquals(new DeleteCommand<>(3), script.getCommand(2));
        assertEquals(new InsertCommand<>(4), script.getCommand(3));
        assertEquals(new InsertCommand<>(5), script.getCommand(4));
        assertEquals(new InsertCommand<>(6), script.getCommand(5));
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScript_withException() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
    }
    
}