
import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        
        assertNotNull(comparator);
        assertEquals(Arrays.asList(1, 2, 3), comparator.sequence1);
        assertEquals(Arrays.asList(1, 2, 3), comparator.sequence2);
        assertNotNull(comparator.equator);
        assertNotNull(comparator.vDown);
        assertNotNull(comparator.vUp);
    }

    @Test
    public void testGetScript() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
        assertEquals(3, script.size());
        
        List<Command<Integer>> commands = script.getCommands();
        assertTrue(commands.get(0) instanceof KeepCommand);
        assertTrue(commands.get(1) instanceof KeepCommand);
        assertTrue(commands.get(2) instanceof KeepCommand);
        
        assertEquals(1, (int) commands.get(0).getObject());
        assertEquals(2, (int) commands.get(1).getObject());
        assertEquals(3, (int) commands.get(2).getObject());
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetScript_withInvalidEquator() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "c");
        Equator<String> equator = (Object o1, Object o2) -> false; // Always return false
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        EditScript<String> script = comparator.getScript();
    }

}
