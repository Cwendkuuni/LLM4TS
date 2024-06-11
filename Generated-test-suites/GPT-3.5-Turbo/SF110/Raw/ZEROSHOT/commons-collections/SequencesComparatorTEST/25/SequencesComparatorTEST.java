import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        
        List<Command<Integer>> expected = Arrays.asList(
            new KeepCommand<>(1),
            new KeepCommand<>(2),
            new InsertCommand<>(4),
            new InsertCommand<>(5)
        );
        
        assertEquals(expected, script.getCommands());
    }

    @Test
    public void testGetScript_sameSequences() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "c");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();
        
        assertTrue(script.getCommands().isEmpty());
    }

    @Test
    public void testGetScript_emptySequence1() {
        List<Double> sequence1 = Collections.emptyList();
        List<Double> sequence2 = Arrays.asList(1.0, 2.0, 3.0);
        SequencesComparator<Double> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Double> script = comparator.getScript();
        
        List<Command<Double>> expected = Arrays.asList(
            new InsertCommand<>(1.0),
            new InsertCommand<>(2.0),
            new InsertCommand<>(3.0)
        );
        
        assertEquals(expected, script.getCommands());
    }

    @Test
    public void testGetScript_emptySequence2() {
        List<Character> sequence1 = Arrays.asList('a', 'b', 'c');
        List<Character> sequence2 = Collections.emptyList();
        SequencesComparator<Character> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Character> script = comparator.getScript();
        
        List<Command<Character>> expected = Arrays.asList(
            new DeleteCommand<>('a'),
            new DeleteCommand<>('b'),
            new DeleteCommand<>('c')
        );
        
        assertEquals(expected, script.getCommands());
    }
    
    // Add more test methods here
    
}

