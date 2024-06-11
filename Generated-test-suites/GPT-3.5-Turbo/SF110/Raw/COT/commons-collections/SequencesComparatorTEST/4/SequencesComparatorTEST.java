
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor_DefaultEquator() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertNotNull(comparator);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
        assertNotNull(comparator.vDown);
        assertNotNull(comparator.vUp);
    }
    
    @Test
    public void testConstructor_CustomEquator() {
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();
        Equator<String> customEquator = (obj1, obj2) -> obj1.equals(obj2);
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, customEquator);
        
        assertNotNull(comparator);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(customEquator, comparator.equator);
        assertNotNull(comparator.vDown);
        assertNotNull(comparator.vUp);
    }
    
    @Test
    public void testGetScript() {
        List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("a", "c", "d"));
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<String> script = comparator.getScript();
        
        assertNotNull(script);
        assertEquals(3, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertEquals("a", script.getCommand(0).getObject());
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
        assertEquals("b", script.getCommand(1).getObject());
        assertTrue(script.getCommand(2) instanceof InsertCommand);
        assertEquals("c", script.getCommand(2).getObject());
    }
}
