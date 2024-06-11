import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.*;

public class SequencesComparatorTest {
    @Test
    public void testConstructor() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
    }
    
    @Test
    public void testConstructorWithEquator() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer object1, Integer object2) {
                return Objects.equals(object1, object2);
            }
            @Override
            public int hash(Integer object) {
                return Objects.hashCode(object);
            }
        };
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
    }
    
    @Test
    public void testGetScript() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("A");
        sequence1.add("B");
        sequence1.add("C");
        
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("B");
        sequence2.add("D");
        sequence2.add("C");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<String> script = comparator.getScript();
        
        assertEquals(4, script.getCommands().size());
        assertEquals(new KeepCommand<String>("B"), script.getCommands().get(0));
        assertEquals(new DeleteCommand<String>("A"), script.getCommands().get(1));
        assertEquals(new InsertCommand<String>("D"), script.getCommands().get(2));
        assertEquals(new KeepCommand<String>("C"), script.getCommands().get(3));
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence1.add(4);
        
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(3);
        sequence2.add(4);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        
        assertEquals(1, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(1, snake.getDiag());
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetMiddleSnakeInternalError() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(3);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
    }
    
    @Test
    public void testBuildScript() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("A");
        sequence1.add("B");
        sequence1.add("C");
        
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("B");
        sequence2.add("D");
        sequence2.add("C");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<String> script = new EditScript<>();
        
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        
        assertEquals(4, script.getCommands().size());
        assertEquals(new KeepCommand<String>("B"), script.getCommands().get(0));
        assertEquals(new DeleteCommand<String>("A"), script.getCommands().get(1));
        assertEquals(new InsertCommand<String>("D"), script.getCommands().get(2));
        assertEquals(new KeepCommand<String>("C"), script.getCommands().get(3));
    }
}