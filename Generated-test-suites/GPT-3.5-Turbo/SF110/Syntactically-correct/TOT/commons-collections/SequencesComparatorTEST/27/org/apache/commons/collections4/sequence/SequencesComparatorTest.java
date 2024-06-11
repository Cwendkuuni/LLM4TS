import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.sequence.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructorWithDefaultEquator() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "c");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator);
    }
    
    @Test
    public void testConstructorWithCustomEquator() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer object1, Integer object2) {
                return object1 % 2 == object2 % 2;
            }
            @Override
            public int hash(Integer object) {
                return object.hashCode();
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertNotNull(comparator);
    }
    
    @Test
    public void testGetScriptWithEqualSequences() {
        List<Double> sequence1 = Arrays.asList(1.5, 2.5, 3.5);
        List<Double> sequence2 = Arrays.asList(1.5, 2.5, 3.5);
        SequencesComparator<Double> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Double> script = comparator.getScript();
        assertNotNull(script);
        assertTrue(script.getCommands().isEmpty());
    }
    
    @Test
    public void testGetScriptWithDifferentSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
        assertEquals(4, script.getCommands().size());
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetScriptWithInternalError() {
        List<Character> sequence1 = new ArrayList<>();
        List<Character> sequence2 = new ArrayList<>();
        SequencesComparator<Character> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.getScript();
    }
}