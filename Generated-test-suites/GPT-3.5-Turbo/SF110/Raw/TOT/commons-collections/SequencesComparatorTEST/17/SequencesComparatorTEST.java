import static org.junit.Assert.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructor_DefaultEquator() {
        // Typical case
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertNotNull(comparator.equator);
    }

    @Test
    public void testConstructor_CustomEquator() {
        // Typical case
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("A", "B", "C");
        Equator<String> equator = new Equator<String>() {
            @Override
            public boolean equate(String s1, String s2) {
                return s1.equalsIgnoreCase(s2);
            }

            @Override
            public int hash(String s) {
                return s.toUpperCase().hashCode();
            }
        };
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertNotNull(comparator);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertNotNull(comparator.equator);
        assertSame(equator, comparator.equator);
    }

    @Test
    public void testGetScript() {
        // Typical case
        List<Character> sequence1 = Arrays.asList('a', 'b', 'c');
        List<Character> sequence2 = Arrays.asList('a', 'b', 'd');
        SequencesComparator<Character> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Character> script = comparator.getScript();
        assertNotNull(script);
        List<Command<Character>> commands = script.getCommands();
        assertNotNull(commands);
        assertEquals(2, commands.size());
        assertTrue(commands.get(0) instanceof KeepCommand);
        assertTrue(commands.get(1) instanceof InsertCommand);
        assertEquals('c', commands.get(0).getElement());
        assertEquals('d', commands.get(1).getElement());

        // Edge case: Empty sequences
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>();
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        assertNotNull(script);
        commands = script.getCommands();
        assertNotNull(commands);
        assertEquals(0, commands.size());

        // Error scenario: Null sequences
        sequence1 = null;
        sequence2 = new ArrayList<>();
        comparator = new SequencesComparator<>(sequence1, sequence2);
        try {
            script = comparator.getScript();
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            //expected
        }
    }

    // Edge case: Snake with start == end and diag == 0
    @Test
    public void testBuildSnake_EdgeCase() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 3, 2, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        int start = 1;
        int diag = 0;
        int end1 = 2;
        int end2 = 1;
        SequencesComparator.Snake snake = comparator.buildSnake(start, diag, end1, end2);
        assertNotNull(snake);
        assertEquals(1, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    // Typical case
    @Test
    public void testGetMiddleSnake_TypicalCase() {
        List<String> sequence1 = Arrays.asList("a", "b", "c", "d");
        List<String> sequence2 = Arrays.asList("a", "c", "b", "d");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        int start1 = 0;
        int end1 = 4;
        int start2 = 0;
        int end2 = 4;
        SequencesComparator.Snake snake = comparator.getMiddleSnake(start1, end1, start2, end2);
        assertNotNull(snake);
        assertEquals(2, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());

        // Error scenario: Empty sequence
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>();
        comparator = new SequencesComparator<>(sequence1, sequence2);
        start1 = 0;
        end1 = 0;
        start2 = 0;
        end2 = 0;
        snake = comparator.getMiddleSnake(start1, end1, start2, end2);
        assertNull(snake);
    }

    @Test
    public void testBuildScript() {
        // Typical case
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("a", "b", "d");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        int start1 = 0;
        int end1 = sequence1.size();
        int start2 = 0;
        int end2 = sequence2.size();
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(start1, end1, start2, end2, script);
        assertNotNull(script);
        List<Command<String>> commands = script.getCommands();
        assertNotNull(commands);
        assertEquals(2, commands.size());
        assertTrue(commands.get(0) instanceof KeepCommand);
        assertTrue(commands.get(1) instanceof DeleteCommand);
        assertEquals("c", commands.get(0).getElement());
        assertEquals("d", commands.get(1).getElement());

        // Error scenario: Invalid indices
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>();
        comparator = new SequencesComparator<>(sequence1, sequence2);
        start1 = 1;
        end1 = 1;
        start2 = 2;
        end2 = 3;
        script = new EditScript<>();
        try {
            comparator.buildScript(start1, end1, start2, end2, script);
            fail("Expected IndexOutOfBoundsException was not thrown");
        } catch (IndexOutOfBoundsException e) {
            //expected
        }
    }
}