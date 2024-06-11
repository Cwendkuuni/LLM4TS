import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        List<Integer> sequence1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<Integer>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
        
    }

    @Test
    public void testConstructorWithEquator() {
        List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("x", "y", "z"));
        Equator<String> equator = new Equator<String>() {

            @Override
            public boolean equate(String o1, String o2) {
               return o1.toLowerCase().equals(o2.toLowerCase());
            }

            @Override
            public int hash(String o) {
               return o.toLowerCase().hashCode();
            }
        };

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);

        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);

    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertNotNull(script);
        assertEquals(5, script.getLength());

        List<EditCommand<Integer>> commands = script.getCommands();
        assertTrue(commands.get(0) instanceof DeleteCommand);
        assertEquals(sequence1.get(0), commands.get(0).getObject());

        assertTrue(commands.get(1) instanceof KeepCommand);
        assertEquals(sequence1.get(1), commands.get(1).getObject());

        assertTrue(commands.get(2) instanceof KeepCommand);
        assertEquals(sequence1.get(2), commands.get(2).getObject());

        assertTrue(commands.get(3) instanceof KeepCommand);
        assertEquals(sequence1.get(3), commands.get(3).getObject());

        assertTrue(commands.get(4) instanceof InsertCommand);
        assertEquals(sequence2.get(4), commands.get(4).getObject());
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        int start1 = 0;
        int end1 = sequence1.size();
        int start2 = 0;
        int end2 = sequence2.size();

        SequencesComparator.Snake snake = comparator.getMiddleSnake(start1, end1, start2, end2);

        assertNotNull(snake);
        assertEquals(2, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test(expected = RuntimeException.class)
    public void testGetMiddleSnakeInvalidInputs() {
        List<String> sequence1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> sequence2 = new ArrayList<>(Arrays.asList("d", "e", "f"));
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        int start1 = 1;
        int end1 = sequence1.size();
        int start2 = 0;
        int end2 = sequence2.size();

        comparator.getMiddleSnake(start1, end1, start2, end2);

        fail("Expected RuntimeException");
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(2, 3, 4, 5));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        int start1 = 0;
        int end1 = sequence1.size();
        int start2 = 0;
        int end2 = sequence2.size();

        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(start1, end1, start2, end2, script);

        assertNotNull(script);
        assertEquals(4, script.getLength());

        List<EditCommand<Integer>> commands = script.getCommands();
        assertTrue(commands.get(0) instanceof DeleteCommand);
        assertEquals(sequence1.get(0), commands.get(0).getObject());

        assertTrue(commands.get(1) instanceof KeepCommand);
        assertEquals(sequence1.get(1), commands.get(1).getObject());

        assertTrue(commands.get(2) instanceof KeepCommand);
        assertEquals(sequence1.get(2), commands.get(2).getObject());

        assertTrue(commands.get(3) instanceof InsertCommand);
        assertEquals(sequence2.get(3), commands.get(3).getObject());

    }

}