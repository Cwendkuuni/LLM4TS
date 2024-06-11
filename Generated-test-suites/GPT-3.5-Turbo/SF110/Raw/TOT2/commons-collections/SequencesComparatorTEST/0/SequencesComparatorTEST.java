
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 3, 2, 4, 5);
        
        SequencesComparator<Integer> sequencesComparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> editScript = sequencesComparator.getScript();
        
        assertNotNull(editScript);
    }

    // Additional test cases can be added here as needed
}
