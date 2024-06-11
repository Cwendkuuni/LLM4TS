import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.collections4.sequence.*;

public class SequencesComparatorTest {
    
    // Test the constructor with two sequences
    @Test
    public void testConstructor() {
        List<String> sequence1 = new ArrayList<String>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        
        List<String> sequence2 = new ArrayList<String>();
        sequence2.add("a");
        sequence2.add("b");
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
    }
    
    // Test the constructor with two sequences and an equator
    @Test
    public void testConstructorWithEquator() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        List<Integer> sequence2 = new ArrayList<Integer>();
        sequence2.add(10);
        sequence2.add(20);
        sequence2.add(30);
        
        Equator<Integer> equator = new Equator<Integer>() {
            public boolean equate(Integer obj1, Integer obj2) {
                return obj1 % 10 == obj2 % 10;
            }
        };
        
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2, equator);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
    }
    
    // Test the getScript method
    @Test
    public void testGetScript() {
        List<Character> sequence1 = new ArrayList<Character>();
        sequence1.add('a');
        sequence1.add('b');
        sequence1.add('c');
        sequence1.add('d');
        
        List<Character> sequence2 = new ArrayList<Character>();
        sequence2.add('a');
        sequence2.add('c');
        sequence2.add('d');
        
        SequencesComparator<Character> comparator = new SequencesComparator<Character>(sequence1, sequence2);
        
        EditScript<Character> script = comparator.getScript();
        
        assertEquals(4, script.size());
        
        assertSame(sequence1.get(0), script.getCommand(0).getObject());
        assertSame(sequence1.get(1), script.getCommand(1).getObject());
        assertSame(sequence1.get(2), script.getCommand(2).getObject());
        assertSame(sequence1.get(3), script.getCommand(3).getObject());
    }
    
    // Test the buildSnake method
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        
        List<Integer> sequence2 = new ArrayList<Integer>();
        sequence2.add(10);
        sequence2.add(20);
        sequence2.add(30);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 3);
        
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    // Test the getMiddleSnake method
    @Test
    public void testGetMiddleSnake() {
        List<String> sequence1 = new ArrayList<String>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        
        List<String> sequence2 = new ArrayList<String>();
        sequence2.add("a");
        sequence2.add("b");
        
        SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 2);
        
        assertNull(snake);
    }
    
    // Test the buildScript method
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        sequence1.add(1);
        sequence1.add(2);
        
        List<Integer> sequence2 = new ArrayList<Integer>();
        sequence2.add(10);
        sequence2.add(20);
        sequence2.add(30);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(sequence1, sequence2);
        
        EditScript<Integer> script = new EditScript<Integer>();
        
        comparator.buildScript(0, 2, 0, 3, script);
        
        assertEquals(3, script.size());
        
        assertSame(sequence1.get(0), script.getCommand(0).getObject());
        assertSame(sequence1.get(1), script.getCommand(1).getObject());
        assertSame(sequence2.get(2), script.getCommand(2).getObject());
    }
    
    // Test the Snake class
    @Test
    public void testSnake() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(0, 1, 0);
        
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
}