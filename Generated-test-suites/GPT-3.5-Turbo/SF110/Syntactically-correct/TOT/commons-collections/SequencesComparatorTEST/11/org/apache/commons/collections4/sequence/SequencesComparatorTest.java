import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {
    
    @Test
    public void testSequencesComparatorConstructor() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertNotNull(comparator);
    }
    
    @Test
    public void testSequencesComparatorConstructorWithEquator() {
        List<String> sequence1 = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("aple", "banana", "cherry"));
        Equator<String> equator = new Equator<String>() {
            @Override
            public boolean equate(String o1, String o2) {
                return o1.equals(o2);
            }
        
            @Override
            public int hash(Object o) {
                return 0;
            }
        };
    
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertNotNull(comparator);
    }
    
    @Test
    public void testGetScript_emptySequences() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
        assertTrue(script.isEmpty());
    }
    
    @Test
    public void testGetScript_equalSequences() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
        assertEquals(3, script.size());
        assertTrue(script.get(0) instanceof KeepCommand);
        assertTrue(script.get(1) instanceof KeepCommand);
        assertTrue(script.get(2) instanceof KeepCommand);
    }
    
    @Test
    public void testGetScript_differentSequences() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
        assertEquals(6, script.size());
        assertTrue(script.get(0) instanceof InsertCommand);
        assertTrue(script.get(1) instanceof InsertCommand);
        assertTrue(script.get(2) instanceof InsertCommand);
        assertTrue(script.get(3) instanceof DeleteCommand);
        assertTrue(script.get(4) instanceof DeleteCommand);
        assertTrue(script.get(5) instanceof DeleteCommand);
    }
    
    @Test
    public void testGetScript_edgeCases() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
        assertEquals(0, script.size());
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScript_InternalError() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        comparator.buildScript(0, 1, 0, 1, new EditScript<>());
    }
}