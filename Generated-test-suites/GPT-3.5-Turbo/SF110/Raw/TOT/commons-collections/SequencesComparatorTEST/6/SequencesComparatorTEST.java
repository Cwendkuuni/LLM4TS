import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections4.equator.EqualsBuilder;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;

public class SequencesComparatorTest {

    @Test
    public void testConstructor_DefaultEquator() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        assertNotNull(comparator);
        assertTrue(new EqualsBuilder().append(comparator.sequence1, Arrays.asList(1, 2, 3))
                .append(comparator.sequence2, Arrays.asList(1, 2, 3)).isEquals());
    }

    @Test
    public void testConstructor_CustomEquator() {
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                return (o1 == o2);
            }
            @Override
            public int hash(Integer o) {
                return o.hashCode();
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3), equator);
        assertNotNull(comparator);
        assertTrue(new EqualsBuilder().append(comparator.sequence1, Arrays.asList(1, 2, 3))
                .append(comparator.sequence2, Arrays.asList(1, 2, 3)).isEquals());
    }

    @Test
    public void testGetScript_sameSequences() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("A", "B", "C"), Arrays.asList("A", "B", "C"));
        EditScript<String> script = comparator.getScript();
        assertTrue(script.isEmpty());
    }

    @Test
    public void testGetScript_differentSequences() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("A", "B", "C"), Arrays.asList("X", "Y", "Z"));
        EditScript<String> script = comparator.getScript();
        assertEquals(6, script.getLength());
        assertTrue(script.getCommand(0) instanceof DeleteCommand);
        assertTrue(script.getCommand(1) instanceof DeleteCommand);
        assertTrue(script.getCommand(2) instanceof InsertCommand);
        assertTrue(script.getCommand(3) instanceof InsertCommand);
        assertTrue(script.getCommand(4) instanceof InsertCommand);
        assertTrue(script.getCommand(5) instanceof InsertCommand);
    }

    @Test
    public void testBuildSnake_sameItemsInSequences() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("A", "B", "C"), Arrays.asList("A", "B", "C"));
        assertEquals(3, comparator.buildSnake(0, 0, 3, 3).getEnd());
    }

    @Test
    public void testBuildSnake_noSameItemsInSequences() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("A", "B", "C"), Arrays.asList("X", "Y", "Z"));
        assertEquals(0, comparator.buildSnake(0, 0, 3, 3).getEnd());
    }

    @Test
    public void testGetMiddleSnake_equalSizes() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("A", "B", "C"), Arrays.asList("X", "Y", "Z"));
        assertNull(comparator.getMiddleSnake(0, 3, 0, 3));
    }

    @Test
    public void testGetMiddleSnake_noEqualSizes() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("A", "B", "C"), Arrays.asList("X", "Y"));
        assertNotNull(comparator.getMiddleSnake(0, 3, 0, 2));
    }

    @Test
    public void testBuildScript_noMiddleSnake() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("A", "B", "C"), Arrays.asList("X", "Y"));
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 2, script);
        assertEquals(5, script.getLength());
    }

    @Test
    public void testBuildScript_withMiddleSnake() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("A", "B", "C"), Arrays.asList("A", "D"));
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 2, script);
        assertEquals(3, script.getLength());
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScript_internalError() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Arrays.asList("A", "B", "C"), Arrays.asList("X", "Y"));
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, 1, 0, 2, script);
    }

    @Test
    public void testSnake_getStart() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 2, 3);
        assertEquals(1, snake.getStart());
    }

    @Test
    public void testSnake_getEnd() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 2, 3);
        assertEquals(2, snake.getEnd());
    }

    @Test
    public void testSnake_getDiag() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 2, 3);
        assertEquals(3, snake.getDiag());
    }
}