import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript_typicalCase() {
        // Typical case: comparing two sequences with some common elements
        
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(Arrays.asList(1, 2, 3, 4), Arrays.asList(2, 3, 4, 5));
        
        EditScript<Integer> expectedScript = new EditScript<Integer>();
        expectedScript.append(new KeepCommand<Integer>(1));
        expectedScript.append(new InsertCommand<Integer>(2));
        expectedScript.append(new KeepCommand<Integer>(3));
        expectedScript.append(new KeepCommand<Integer>(4));
        expectedScript.append(new KeepCommand<Integer>(5));
        
        EditScript<Integer> actualScript = comparator.getScript();
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test
    public void testGetScript_emptySequences() {
        // Edge case: comparing two empty sequences
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(new ArrayList<String>(), new ArrayList<String>());
        
        EditScript<String> expectedScript = new EditScript<String>();
        
        EditScript<String> actualScript = comparator.getScript();
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test
    public void testGetScript_sameSequences() {
        // Edge case: comparing two identical sequences
        
        List<Character> sequence = Arrays.asList('a', 'b', 'c');
        
        SequencesComparator<Character> comparator = new SequencesComparator<Character>(sequence, sequence);
        
        EditScript<Character> expectedScript = new EditScript<Character>();
        expectedScript.append(new KeepCommand<Character>('a'));
        expectedScript.append(new KeepCommand<Character>('b'));
        expectedScript.append(new KeepCommand<Character>('c'));
        
        EditScript<Character> actualScript = comparator.getScript();
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test
    public void testGetScript_reversedSequences() {
        // Edge case: comparing two sequences that are reversed versions of each other
        
        SequencesComparator<Character> comparator = new SequencesComparator<Character>(Arrays.asList('a', 'b', 'c'), Arrays.asList('c', 'b', 'a'));
        
        EditScript<Character> expectedScript = new EditScript<Character>();
        expectedScript.append(new KeepCommand<Character>('a'));
        expectedScript.append(new DeleteCommand<Character>('b'));
        expectedScript.append(new DeleteCommand<Character>('c'));
        expectedScript.append(new KeepCommand<Character>('b'));
        expectedScript.append(new InsertCommand<Character>('c'));
        expectedScript.append(new InsertCommand<Character>('a'));
        
        EditScript<Character> actualScript = comparator.getScript();
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test
    public void testGetScript_differentElementTypes() {
        // Typical case: comparing sequences with elements of different types
        
        SequencesComparator<Number> comparator = new SequencesComparator<Number>(Arrays.asList(1, 2, 3.5, 4.2), Arrays.asList(2, 3, 4, 5));
        
        EditScript<Number> expectedScript = new EditScript<Number>();
        expectedScript.append(new KeepCommand<Number>(1));
        expectedScript.append(new InsertCommand<Number>(2));
        expectedScript.append(new KeepCommand<Number>(3.5));
        expectedScript.append(new KeepCommand<Number>(4.2));
        expectedScript.append(new KeepCommand<Number>(4));
        expectedScript.append(new KeepCommand<Number>(5));
        
        EditScript<Number> actualScript = comparator.getScript();
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test
    public void testGetScript_errorScenarios() {
        // Error scenario: comparing two sequences with a null element
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(Arrays.asList("a", null, "c"), Arrays.asList("a", "b", "c"));
        
        EditScript<String> expectedScript = new EditScript<String>();
        expectedScript.append(new KeepCommand<String>("a"));
        expectedScript.append(new InsertCommand<String>("b"));
        expectedScript.append(new DeleteCommand<String>(null));
        expectedScript.append(new KeepCommand<String>("c"));
        
        EditScript<String> actualScript = comparator.getScript();
        
        assertEquals(expectedScript, actualScript);
    }
}