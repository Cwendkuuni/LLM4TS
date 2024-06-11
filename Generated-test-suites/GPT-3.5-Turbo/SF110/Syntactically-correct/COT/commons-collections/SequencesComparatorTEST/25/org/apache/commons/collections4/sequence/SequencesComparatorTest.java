
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor1() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertNotNull(comparator.equator);
        assertNotNull(comparator.vDown);
        assertNotNull(comparator.vUp);
    }

    @Test
    public void testConstructor2() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        Equator<Integer> equator = new Equator<>();
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
        assertNotNull(comparator.vDown);
        assertNotNull(comparator.vUp);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
    }
    
    @Test
    public void testGetMiddleSnake_nullCase() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake snake = comparator.getMiddleSnake(1, 1, 1, 1);
        
        assertNull(snake);
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake snake = comparator.buildSnake(0, 0, 3, 3);
        
        assertEquals(0, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testBuildScript_noMiddleSnakeCase() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        
        comparator.buildScript(0, 3, 0, 3, script);
        
        assertEquals(6, script.size());
        assertTrue(script.get(0) instanceof DeleteCommand);
        assertEquals(1, script.get(0).getObject());
        assertTrue(script.get(1) instanceof DeleteCommand);
        assertEquals(2, script.get(1).getObject());
        assertTrue(script.get(2) instanceof DeleteCommand);
        assertEquals(3, script.get(2).getObject());
        assertTrue(script.get(3) instanceof InsertCommand);
        assertEquals(4, script.get(3).getObject());
        assertTrue(script.get(4) instanceof InsertCommand);
        assertEquals(5, script.get(4).getObject());
        assertTrue(script.get(5) instanceof InsertCommand);
        assertEquals(6, script.get(5).getObject());
    }
    
    @Test
    public void testBuildScript_withMiddleSnakeCase() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 2, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        
        comparator.buildScript(0, 3, 0, 4, script);
        
        assertEquals(5, script.size());
        assertTrue(script.get(0) instanceof DeleteCommand);
        assertEquals(1, script.get(0).getObject());
        assertTrue(script.get(1) instanceof KeepCommand);
        assertEquals(2, script.get(1).getObject());
        assertTrue(script.get(2) instanceof DeleteCommand);
        assertEquals(3, script.get(2).getObject());
        assertTrue(script.get(3) instanceof InsertCommand);
        assertEquals(5, script.get(3).getObject());
        assertTrue(script.get(4) instanceof InsertCommand);
        assertEquals(6, script.get(4).getObject());
    }
    
    @Test
    public void testBuildScript_withMiddleSnakeCase2() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(4, 2, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        
        comparator.buildScript(0, 4, 0, 4, script);
        
        assertEquals(3, script.size());
        assertTrue(script.get(0) instanceof KeepCommand);
        assertEquals(1, script.get(0).getObject());
        assertTrue(script.get(1) instanceof KeepCommand);
        assertEquals(2, script.get(1).getObject());
        assertTrue(script.get(2) instanceof DeleteCommand);
        assertEquals(3, script.get(2).getObject());
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScript_internalErrorCase() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        
        comparator.buildScript(3, 2, 2, 1, script);
    }
    
}
