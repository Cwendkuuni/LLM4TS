import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import org.apache.commons.collections4.equators.DefaultEquator;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        // Test Case 1: Empty Sequences
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();

        SequencesComparator<Integer> seqComp1 = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script1 = seqComp1.getScript();
        assertEquals(0, script1.getCommands().size());

        // Test Case 2: Sequences with same elements
        List<Integer> sequence3 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence4 = new ArrayList<>(Arrays.asList(1, 2, 3));

        SequencesComparator<Integer> seqComp2 = new SequencesComparator<>(sequence3, sequence4);
        EditScript<Integer> script2 = seqComp2.getScript();
        List<Command<Integer>> commands2 = script2.getCommands();
        assertEquals(3, commands2.size());

        assertTrue(commands2.get(0) instanceof KeepCommand);
        assertTrue(commands2.get(1) instanceof KeepCommand);
        assertTrue(commands2.get(2) instanceof KeepCommand);

        assertEquals(1, (int)commands2.get(0).getElement());
        assertEquals(2, (int)commands2.get(1).getElement());
        assertEquals(3, (int)commands2.get(2).getElement());

        // Test Case 3: Sequences with different elements
        List<Integer> sequence5 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence6 = new ArrayList<>(Arrays.asList(4, 5, 6));

        SequencesComparator<Integer> seqComp3 = new SequencesComparator<>(sequence5, sequence6);
        EditScript<Integer> script3 = seqComp3.getScript();
        List<Command<Integer>> commands3 = script3.getCommands();
        assertEquals(6, commands3.size());

        assertTrue(commands3.get(0) instanceof InsertCommand);
        assertTrue(commands3.get(1) instanceof KeepCommand);
        assertTrue(commands3.get(2) instanceof KeepCommand);
        assertTrue(commands3.get(3) instanceof KeepCommand);
        assertTrue(commands3.get(4) instanceof InsertCommand);
        assertTrue(commands3.get(5) instanceof InsertCommand);

        assertEquals(4, (int)commands3.get(0).getElement());
        assertEquals(1, (int)commands3.get(1).getElement());
        assertEquals(2, (int)commands3.get(2).getElement());
        assertEquals(3, (int)commands3.get(3).getElement());
        assertEquals(5, (int)commands3.get(4).getElement());
        assertEquals(6, (int)commands3.get(5).getElement());

        // Test Case 4: Sequences with different sizes
        List<Integer> sequence7 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> sequence8 = new ArrayList<>(Arrays.asList(1, 2, 3));

        SequencesComparator<Integer> seqComp4 = new SequencesComparator<>(sequence7, sequence8);
        EditScript<Integer> script4 = seqComp4.getScript();
        List<Command<Integer>> commands4 = script4.getCommands();
        assertEquals(2, commands4.size());

        assertTrue(commands4.get(0) instanceof KeepCommand);
        assertTrue(commands4.get(1) instanceof DeleteCommand);

        assertEquals(1, (int)commands4.get(0).getElement());
        assertEquals(2, (int)commands4.get(1).getElement());
    }

    @Test
    public void testBuildSnake() {
        // Test Case 1: Same start and end indices
        SequencesComparator<Integer> seqComp = new SequencesComparator<>(Arrays.asList(1), Arrays.asList(1));
        SequencesComparator.Snake snake = seqComp.buildSnake(0, 0, 0, 0);
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());

        // Test Case 2: Sequence1 and sequence2 elements are equal
        SequencesComparator<Integer> seqComp2 = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        SequencesComparator.Snake snake2 = seqComp2.buildSnake(0, 0, 3, 3);
        assertEquals(3, snake2.getStart());
        assertEquals(3, snake2.getEnd());
        assertEquals(0, snake2.getDiag());

        // Test Case 3: Sequence1 and sequence2 elements are not equal
        SequencesComparator<Integer> seqComp3 = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 4));
        SequencesComparator.Snake snake3 = seqComp3.buildSnake(0, 0, 3, 3);
        assertEquals(1, snake3.getStart());
        assertEquals(1, snake3.getEnd());
        assertEquals(0, snake3.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        // Test Case 1: No matching elements
        SequencesComparator<Integer> seqComp1 = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        SequencesComparator.Snake snake1 = seqComp1.getMiddleSnake(0, 3, 0, 3);
        assertNull(snake1);

        // Test Case 2: m = 0, n = 3, offset = 3, delta = -3
        SequencesComparator<Integer> seqComp2 = new SequencesComparator<>(Collections.emptyList(), Arrays.asList(4, 5, 6));
        SequencesComparator.Snake snake2 = seqComp2.getMiddleSnake(0, 0, 0, 3);
        assertNull(snake2);

        // Test Case 3: n = 0, m = 3, offset = 3, delta = 3
        SequencesComparator<Integer> seqComp3 = new SequencesComparator<>(Arrays.asList(1, 2, 3), Collections.emptyList());
        SequencesComparator.Snake snake3 = seqComp3.getMiddleSnake(0, 3, 0, 0);
        assertNull(snake3);

        // Test Case 4: Equal m and n
        SequencesComparator<Integer> seqComp4 = new SequencesComparator<>(Arrays.asList(1, 2), Arrays.asList(3, 4));
        SequencesComparator.Snake snake4 = seqComp4.getMiddleSnake(0, 2, 0, 2);
        assertNull(snake4);

        // Test Case 5: m > n
        SequencesComparator<Integer> seqComp5 = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2));
        SequencesComparator.Snake snake5 = seqComp5.getMiddleSnake(0, 3, 0, 2);
        assertNull(snake5);

        // Test Case 6: Positive scenario
        SequencesComparator<Integer> seqComp6 = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        SequencesComparator.Snake snake6 = seqComp6.getMiddleSnake(0, 3, 0, 3);
        assertEquals(3, snake6.getStart());
        assertEquals(3, snake6.getEnd());
        assertEquals(0, snake6.getDiag());
    }

    @Test
    public void testBuildScript() {
        // Test Case 1: No changes required
        SequencesComparator<Integer> seqComp1 = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        EditScript<Integer> script1 = new EditScript<>();
        seqComp1.buildScript(0, 3, 0, 3, script1);
        List<Command<Integer>> commands1 = script1.getCommands();
        assertEquals(3, commands1.size());

        assertTrue(commands1.get(0) instanceof KeepCommand);
        assertTrue(commands1.get(1) instanceof KeepCommand);
        assertTrue(commands1.get(2) instanceof KeepCommand);

        assertEquals(1, (int)commands1.get(0).getElement());
        assertEquals(2, (int)commands1.get(1).getElement());
        assertEquals(3, (int)commands1.get(2).getElement());

        // Test Case 2: Equal m and n
        SequencesComparator<Integer> seqComp2 = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(3, 4, 5));
        EditScript<Integer> script2 = new EditScript<>();
        seqComp2.buildScript(0, 3, 0, 3, script2);
        List<Command<Integer>> commands2 = script2.getCommands();
        assertEquals(2, commands2.size());

        assertTrue(commands2.get(0) instanceof InsertCommand);
        assertTrue(commands2.get(1) instanceof InsertCommand);

        assertEquals(3, (int)commands2.get(0).getElement());
        assertEquals(4, (int)commands2.get(1).getElement());

        // Test Case 3: Positive scenario
        SequencesComparator<Integer> seqComp3 = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 4));
        EditScript<Integer> script3 = new EditScript<>();
        seqComp3.buildScript(0, 3, 0, 3, script3);
        List<Command<Integer>> commands3 = script3.getCommands();
        assertEquals(3, commands3.size());

        assertTrue(commands3.get(0) instanceof DeleteCommand);
        assertTrue(commands3.get(1) instanceof KeepCommand);
        assertTrue(commands3.get(2) instanceof InsertCommand);

        assertEquals(1, (int)commands3.get(0).getElement());
        assertEquals(3, (int)commands3.get(1).getElement());
        assertEquals(4, (int)commands3.get(2).getElement());
    }
}