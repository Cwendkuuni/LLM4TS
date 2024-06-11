import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.collections4.sequence.*;
import org.junit.Test;

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
        List<String> sequence1 = new ArrayList<>();
        List<String> sequence2 = new ArrayList<>();
        Equator<String> equator = new Equator<String>() {
            @Override
            public boolean equate(String obj1, String obj2) {
                return obj1.equalsIgnoreCase(obj2);
            }
            @Override
            public int hash(String obj) {
                return obj.toLowerCase().hashCode();
            }
        };
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
    }
    
    @Test
    public void testGetScript() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("x", "y", "z");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<String> script = comparator.getScript();
        
        assertEquals(5, script.size());
        assertEquals(new InsertCommand<>("x"), script.get(0));
        assertEquals(new InsertCommand<>("y"), script.get(1));
        assertEquals(new InsertCommand<>("z"), script.get(2));
        assertEquals(new KeepCommand<>("c"), script.get(3));
        assertEquals(new KeepCommand<>("b"), script.get(4));
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4, 5, 6);
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer obj1, Integer obj2) {
                return obj1.equals(obj2);
            }
            @Override
            public int hash(Integer obj) {
                return obj.hashCode();
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        
        assertEquals(2, snake.getStart());
        assertEquals(4, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        
        assertEquals(5, script.size());
        assertEquals(new KeepCommand<>(1), script.get(0));
        assertEquals(new KeepCommand<>(2), script.get(1));
        assertEquals(new InsertCommand<>(4), script.get(2));
        assertEquals(new KeepCommand<>(5), script.get(3));
        assertEquals(new InsertCommand<>(6), script.get(4));
    }
}