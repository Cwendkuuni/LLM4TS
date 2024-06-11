import static org.junit.Assert.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.getCommands().size());
        
        sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        sequence2 = Arrays.asList(5, 4, 3, 2, 1);
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        assertTrue(script.getCommands().contains(new DeleteCommand<Integer>(1)));
        assertTrue(script.getCommands().contains(new DeleteCommand<Integer>(2)));
        assertTrue(script.getCommands().contains(new DeleteCommand<Integer>(3)));
        assertTrue(script.getCommands().contains(new DeleteCommand<Integer>(4)));
        assertTrue(script.getCommands().contains(new DeleteCommand<Integer>(5)));
        assertTrue(script.getCommands().contains(new InsertCommand<Integer>(5)));
        assertTrue(script.getCommands().contains(new InsertCommand<Integer>(4)));
        assertTrue(script.getCommands().contains(new InsertCommand<Integer>(3)));
        assertTrue(script.getCommands().contains(new InsertCommand<Integer>(2)));
        assertTrue(script.getCommands().contains(new InsertCommand<Integer>(1)));
    }
    
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.buildSnake(0, 0, 5, 5);
        assertEquals(0, snake.getStart());
        assertEquals(5, snake.getEnd());
        assertEquals(0, snake.getDiag());
        
        sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        sequence2 = Arrays.asList(5, 4, 3, 2, 1);
        comparator = new SequencesComparator<>(sequence1, sequence2);
        snake = comparator.buildSnake(0, 0, 5, 5);
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake middleSnake = comparator.getMiddleSnake(0, 5, 0, 5);
        assertNull(middleSnake);
        
        sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        sequence2 = Arrays.asList(5, 4, 3, 2, 1);
        comparator = new SequencesComparator<>(sequence1, sequence2);
        middleSnake = comparator.getMiddleSnake(0, 5, 0, 5);
        assertNull(middleSnake);
    }
    
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 5, 0, 5, script);
        assertEquals(0, script.getCommands().size());
        
        sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        sequence2 = Arrays.asList(5, 4, 3, 2, 1);
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = new EditScript<>();
        comparator.buildScript(0, 5, 0, 5, script);
        assertTrue(script.getCommands().contains(new DeleteCommand<Integer>(1)));
        assertTrue(script.getCommands().contains(new DeleteCommand<Integer>(2)));
        assertTrue(script.getCommands().contains(new DeleteCommand<Integer>(3)));
        assertTrue(script.getCommands().contains(new DeleteCommand<Integer>(4)));
        assertTrue(script.getCommands().contains(new DeleteCommand<Integer>(5)));
        assertTrue(script.getCommands().contains(new InsertCommand<Integer>(5)));
        assertTrue(script.getCommands().contains(new InsertCommand<Integer>(4)));
        assertTrue(script.getCommands().contains(new InsertCommand<Integer>(3)));
        assertTrue(script.getCommands().contains(new InsertCommand<Integer>(2)));
        assertTrue(script.getCommands().contains(new InsertCommand<Integer>(1)));
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScript_InternalError() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 5, 0, 5, script);
    }
}