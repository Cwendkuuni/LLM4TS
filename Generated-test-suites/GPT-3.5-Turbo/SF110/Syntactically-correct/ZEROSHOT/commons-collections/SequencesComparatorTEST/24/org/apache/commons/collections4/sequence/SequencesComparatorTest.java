import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.equator.DefaultEquator;
import org.junit.Before;
import org.junit.Test;

public class SequencesComparatorTest {
    private List<Integer> sequence1;
    private List<Integer> sequence2;
    private SequencesComparator<Integer> comparator;
    
    @Before
    public void setup() {
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>();
        comparator = new SequencesComparator<>(sequence1, sequence2);
    }
    
    @Test
    public void testGetScript_emptySequences() {
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
        assertEquals(0, script.size());
    }
    
    @Test
    public void testGetScript_sameSequences() {
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(3);
        
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
        assertEquals(3, script.size());
        
        assertEquals(new KeepCommand<>(1), script.getCommand(0));
        assertEquals(new KeepCommand<>(2), script.getCommand(1));
        assertEquals(new KeepCommand<>(3), script.getCommand(2));
    }
    
    @Test
    public void testGetScript_differentSequences() {
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        sequence2.add(4);
        sequence2.add(5);
        sequence2.add(6);
        
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
        assertEquals(6, script.size());
        
        assertEquals(new DeleteCommand<>(1), script.getCommand(0));
        assertEquals(new DeleteCommand<>(2), script.getCommand(1));
        assertEquals(new DeleteCommand<>(3), script.getCommand(2));
        assertEquals(new InsertCommand<>(4), script.getCommand(3));
        assertEquals(new InsertCommand<>(5), script.getCommand(4));
        assertEquals(new InsertCommand<>(6), script.getCommand(5));
    }
}