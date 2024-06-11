import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class SequencesComparatorTest {
    
    private List<Integer> sequence1;
    private List<Integer> sequence2;
    private SequencesComparator<Integer> comparator;
    
    @Before
    public void setUp() {
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>();
        comparator = new SequencesComparator<>(sequence1, sequence2);
    }
    
    @Test
    public void testGetScript() {
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        sequence2.add(1);
        sequence2.add(3);
        sequence2.add(4);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
        assertEquals(3, script.size());
        assertEquals(new KeepCommand<>(1), script.get(0));
        assertEquals(new DeleteCommand<>(2), script.get(1));
        assertEquals(new InsertCommand<>(4), script.get(2));
    }
    
    @Test
    public void testBuildScript_withEqualSequences() {
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(3);
        
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        
        assertNotNull(script);
        assertEquals(3, script.size());
        assertEquals(new KeepCommand<>(1), script.get(0));
        assertEquals(new KeepCommand<>(2), script.get(1));
        assertEquals(new KeepCommand<>(3), script.get(2));
    }
}