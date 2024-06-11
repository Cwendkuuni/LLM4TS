import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.equator.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor1() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertNotNull(comparator);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
        assertNotNull(comparator.vDown);
        assertNotNull(comparator.vUp);
        assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vDown.length);
        assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vUp.length);
    }
    
    @Test
    public void testConstructor2() {
        List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("x", "y", "z"));
        Equator<String> equator = new IdentityEquator<>();
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertNotNull(comparator);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
        assertNotNull(comparator.vDown);
        assertNotNull(comparator.vUp);
        assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vDown.length);
        assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vUp.length);
    }
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
        assertTrue(script.isEmpty());
    }
    
    @Test
    public void testBuildSnake() {
        List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("x", "y", "z"));
        Equator<String> equator = new IdentityEquator<>();
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(0, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        
        assertNull(snake);
    }
    
    @Test
    public void testBuildScript() {
        List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("x", "y", "z"));
        Equator<String> equator = new IdentityEquator<>();
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        
        assertFalse(script.isEmpty());
        assertEquals(6, script.size());
        
        assertTrue(script.getCommand(0) instanceof InsertCommand);
        assertEquals("x", script.getCommand(0).getObject());
        
        assertTrue(script.getCommand(1) instanceof InsertCommand);
        assertEquals("y", script.getCommand(1).getObject());
        
        assertTrue(script.getCommand(2) instanceof InsertCommand);
        assertEquals("z", script.getCommand(2).getObject());
        
        assertTrue(script.getCommand(3) instanceof DeleteCommand);
        assertEquals("a", script.getCommand(3).getObject());
        
        assertTrue(script.getCommand(4) instanceof DeleteCommand);
        assertEquals("b", script.getCommand(4).getObject());
        
        assertTrue(script.getCommand(5) instanceof DeleteCommand);
        assertEquals("c", script.getCommand(5).getObject());
    }
    
    private static class Snake {
        private final int start;
        private final int end;
        private final int diag;

        public Snake(final int start, final int end, final int diag) {
            this.start = start;
            this.end = end;
            this.diag = diag;
        }

        public int getStart() {
            return this.start;
        }

        public int getEnd() {
            return this.end;
        }

        public int getDiag() {
            return this.diag;
        }
    }
}