import org.junit.Test;
import org.apache.commons.collections4.sequence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructorWithDefaultEquator() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        assertNotNull(comparator);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
        assertTrue(Arrays.equals(new int[8], comparator.vDown));
        assertTrue(Arrays.equals(new int[8], comparator.vUp));
    }

    @Test
    public void testConstructorWithCustomEquator() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "c");

        Equator<String> equator = new Equator<String>() {
            @Override
            public boolean equate(String s1, String s2) {
                return s1.equalsIgnoreCase(s2);
            }

            @Override
            public int hash(String s) {
                return s.toLowerCase().hashCode();
            }
        };

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);

        assertNotNull(comparator);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
        assertTrue(Arrays.equals(new int[8], comparator.vDown));
        assertTrue(Arrays.equals(new int[8], comparator.vUp));
    }

    @Test
    public void testGetScript() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "d");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();

        assertNotNull(script);
        assertEquals(3, script.getCommands().size());
        assertTrue(script.getCommands().get(0) instanceof KeepCommand<?>);
        assertTrue(script.getCommands().get(1) instanceof KeepCommand<?>);
        assertTrue(script.getCommands().get(2) instanceof UpdateCommand<?>);
        assertEquals("a", ((KeepCommand<?>) script.getCommands().get(0)).getObject());
        assertEquals("b", ((KeepCommand<?>) script.getCommands().get(1)).getObject());
        assertEquals("c", ((UpdateCommand<?>) script.getCommands().get(2)).getObject());
        assertEquals("d", ((UpdateCommand<?>) script.getCommands().get(2)).getTo());
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(6, 2, 3, 7, 5);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 1, 5, 4);

        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(1, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(6, 2, 3, 7, 5);

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 5, 0, 5);

        assertNotNull(snake);
        assertEquals(2, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testBuildScript() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "d");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();

        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);

        assertNotNull(script);
        assertEquals(3, script.getCommands().size());
        assertTrue(script.getCommands().get(0) instanceof KeepCommand<?>);
        assertTrue(script.getCommands().get(1) instanceof KeepCommand<?>);
        assertTrue(script.getCommands().get(2) instanceof UpdateCommand<?>);
        assertEquals("a", ((KeepCommand<?>) script.getCommands().get(0)).getObject());
        assertEquals("b", ((KeepCommand<?>) script.getCommands().get(1)).getObject());
        assertEquals("c", ((UpdateCommand<?>) script.getCommands().get(2)).getObject());
        assertEquals("d", ((UpdateCommand<?>) script.getCommands().get(2)).getTo());
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScriptInternalError() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = new ArrayList<>();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            sequence2.add("a");
        }

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();

        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
    }

    @Test
    public void testCompleteSequencesComparator() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "d");

        // Test constructor with default equator
        SequencesComparator<String> comparator1 = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator1);

        // Test constructor with custom equator
        Equator<String> equator = new Equator<String>() {
            @Override
            public boolean equate(String s1, String s2) {
                return s1.equalsIgnoreCase(s2);
            }

            @Override
            public int hash(String s) {
                return s.toLowerCase().hashCode();
            }
        };

        SequencesComparator<String> comparator2 = new SequencesComparator<>(sequence1, sequence2, equator);
        assertNotNull(comparator2);

        // Test getScript
        EditScript<String> script1 = comparator1.getScript();
        assertNotNull(script1);
        assertEquals(3, script1.getCommands().size());

        EditScript<String> script2 = comparator2.getScript();
        assertNotNull(script2);
        assertEquals(3, script2.getCommands().size());
    }
}