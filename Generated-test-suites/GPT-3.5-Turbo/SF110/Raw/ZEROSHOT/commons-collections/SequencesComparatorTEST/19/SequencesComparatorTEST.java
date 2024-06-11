import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        // Test case 1
        SequencesComparator<Integer> comparator1 = new SequencesComparator<>(List.of(1, 2, 3), List.of(1, 2, 3));
        EditScript<Integer> script1 = comparator1.getScript();
        assertEquals(3, script1.getLength());

        // Test case 2
        SequencesComparator<String> comparator2 = new SequencesComparator<>(List.of("a", "b", "c"), List.of("a", "b", "c"));
        EditScript<String> script2 = comparator2.getScript();
        assertEquals(3, script2.getLength());

        // Test case 3
        SequencesComparator<Integer> comparator3 = new SequencesComparator<>(List.of(1, 2, 3), List.of(1, 2, 4));
        EditScript<Integer> script3 = comparator3.getScript();
        assertEquals(1, script3.getLength());

        // Test case 4
        SequencesComparator<String> comparator4 = new SequencesComparator<>(List.of("a", "b", "c"), List.of("a", "b", "d"));
        EditScript<String> script4 = comparator4.getScript();
        assertEquals(1, script4.getLength());
    }

    @Test
    public void testBuildScript() {
        // Test case 1
        SequencesComparator<Integer> comparator1 = new SequencesComparator<>(List.of(1, 2, 3), List.of(1, 2, 3));
        EditScript<Integer> script1 = new EditScript<>();
        comparator1.buildScript(0, 3, 0, 3, script1);
        assertEquals(3, script1.getLength());

        // Test case 2
        SequencesComparator<String> comparator2 = new SequencesComparator<>(List.of("a", "b", "c"), List.of("a", "b", "c"));
        EditScript<String> script2 = new EditScript<>();
        comparator2.buildScript(0, 3, 0, 3, script2);
        assertEquals(3, script2.getLength());

        // Test case 3
        SequencesComparator<Integer> comparator3 = new SequencesComparator<>(List.of(1, 2, 3), List.of(1, 2, 4));
        EditScript<Integer> script3 = new EditScript<>();
        comparator3.buildScript(0, 3, 0, 3, script3);
        assertEquals(1, script3.getLength());

        // Test case 4
        SequencesComparator<String> comparator4 = new SequencesComparator<>(List.of("a", "b", "c"), List.of("a", "b", "d"));
        EditScript<String> script4 = new EditScript<>();
        comparator4.buildScript(0, 3, 0, 3, script4);
        assertEquals(1, script4.getLength());
    }

    @Test
    public void testBuildSnake() {
        // Test case 1
        SequencesComparator<Integer> comparator1 = new SequencesComparator<>();
        SequencesComparator.Snake snake1 = comparator1.buildSnake(0, 0, 3, 3);
        assertEquals(0, snake1.getStart());
        assertEquals(3, snake1.getEnd());
        assertEquals(0, snake1.getDiag());

        // Test case 2
        SequencesComparator<String> comparator2 = new SequencesComparator<>();
        SequencesComparator.Snake snake2 = comparator2.buildSnake(0, 0, 3, 3);
        assertEquals(0, snake2.getStart());
        assertEquals(3, snake2.getEnd());
        assertEquals(0, snake2.getDiag());

        // Test case 3
        SequencesComparator<Integer> comparator3 = new SequencesComparator<>();
        SequencesComparator.Snake snake3 = comparator3.buildSnake(1, 1, 3, 3);
        assertEquals(1, snake3.getStart());
        assertEquals(2, snake3.getEnd());
        assertEquals(1, snake3.getDiag());

        // Test case 4
        SequencesComparator<String> comparator4 = new SequencesComparator<>();
        SequencesComparator.Snake snake4 = comparator4.buildSnake(1, 1, 3, 3);
        assertEquals(1, snake4.getStart());
        assertEquals(2, snake4.getEnd());
        assertEquals(1, snake4.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        // Test case 1
        SequencesComparator<Integer> comparator1 = new SequencesComparator<>();
        SequencesComparator.Snake middleSnake1 = comparator1.getMiddleSnake(0, 3, 0, 3);
        assertNull(middleSnake1);

        // Test case 2
        SequencesComparator<String> comparator2 = new SequencesComparator<>();
        SequencesComparator.Snake middleSnake2 = comparator2.getMiddleSnake(0, 3, 0, 3);
        assertNull(middleSnake2);

        // Test case 3
        SequencesComparator<Integer> comparator3 = new SequencesComparator<>();
        SequencesComparator.Snake middleSnake3 = comparator3.getMiddleSnake(0, 3, 0, 2);
        assertNotNull(middleSnake3);

        // Test case 4
        SequencesComparator<String> comparator4 = new SequencesComparator<>();
        SequencesComparator.Snake middleSnake4 = comparator4.getMiddleSnake(0, 3, 0, 2);
        assertNotNull(middleSnake4);
    }

    @Test
    public void testSnakeGetters() {
        // Test case 1
        SequencesComparator.Snake snake1 = new SequencesComparator.Snake(0, 3, 0);
        assertEquals(0, snake1.getStart());
        assertEquals(3, snake1.getEnd());
        assertEquals(0, snake1.getDiag());

        // Test case 2
        SequencesComparator.Snake snake2 = new SequencesComparator.Snake(1, 2, 1);
        assertEquals(1, snake2.getStart());
        assertEquals(2, snake2.getEnd());
        assertEquals(1, snake2.getDiag());

        // Test case 3
        SequencesComparator.Snake snake3 = new SequencesComparator.Snake(2, 2, 3);
        assertEquals(2, snake3.getStart());
        assertEquals(2, snake3.getEnd());
        assertEquals(3, snake3.getDiag());
    }
}