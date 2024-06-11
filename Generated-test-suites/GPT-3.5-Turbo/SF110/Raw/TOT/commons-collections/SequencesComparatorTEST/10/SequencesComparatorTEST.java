import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript_typicalCase() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(2, 4, 3));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertEquals(4, script.size());
        assertTrue(script.getCommand(0) instanceof DeleteCommand);
        assertTrue(script.getCommand(1) instanceof KeepCommand);
        assertTrue(script.getCommand(2) instanceof KeepCommand);
        assertTrue(script.getCommand(3) instanceof KeepCommand);
    }
    
    @Test
    public void testGetScript_emptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertEquals(0, script.size());
    }
    
    @Test
    public void testGetScript_sameSequences() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertEquals(5, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof KeepCommand);
        assertTrue(script.getCommand(2) instanceof KeepCommand);
        assertTrue(script.getCommand(3) instanceof KeepCommand);
        assertTrue(script.getCommand(4) instanceof KeepCommand);
    }
    
    @Test
    public void testGetScript_differentSequences() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(6, 7, 8, 9, 10));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertEquals(10, script.size());
        assertTrue(script.getCommand(0) instanceof InsertCommand);
        assertTrue(script.getCommand(1) instanceof InsertCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
        assertTrue(script.getCommand(3) instanceof InsertCommand);
        assertTrue(script.getCommand(4) instanceof InsertCommand);
        assertTrue(script.getCommand(5) instanceof DeleteCommand);
        assertTrue(script.getCommand(6) instanceof DeleteCommand);
        assertTrue(script.getCommand(7) instanceof DeleteCommand);
        assertTrue(script.getCommand(8) instanceof DeleteCommand);
        assertTrue(script.getCommand(9) instanceof DeleteCommand);
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetScript_internalError() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 3, 2, 1));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
    }
}