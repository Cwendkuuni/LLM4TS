
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SequencesComparatorTest {
    
    @Test
    public void testGetScript() {
        List<String> sequence1 = new ArrayList<String>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        
        List<String> sequence2 = new ArrayList<String>();
        sequence2.add("a");
        sequence2.add("c");
        sequence2.add("d");
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        
        EditScript<String> expectedScript = new EditScript<String>();
        expectedScript.append(new KeepCommand<String>("a"));
        expectedScript.append(new DeleteCommand<String>("b"));
        expectedScript.append(new KeepCommand<String>("c"));
        expectedScript.append(new InsertCommand<String>("d"));
        
        EditScript<String> actualScript = comparator.getScript();
        
        assertEquals(expectedScript, actualScript);
    }
    
    // Test other public methods and their functionalities
    
}

