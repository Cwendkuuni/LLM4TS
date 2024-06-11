package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.collections4.DefaultEquator;
import org.apache.commons.collections4.equators.Equator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SequencesComparatorTest {
    private List<String> sequence1;
    private List<String> sequence2;
    private Equator<String> equator;

    @Before
    public void setUp() {
        sequence1 = new ArrayList<>();
        sequence1.add("foo");
        sequence1.add("bar");
        sequence1.add("baz");

        sequence2 = new ArrayList<>();
        sequence2.add("foobar");
        sequence2.add("baz");

        equator = DefaultEquator.defaultEquator();
    }

    @Test
    public void testConstructor() {
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);

        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
    }

    @Test
    public void testGetScript() {
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();

        // Ensure sequence1 is transformed to sequence2
        assertEquals(5, script.getLength());
        assertEquals(new KeepCommand<>("foo"), script.getCommand(0));
        assertEquals(new DeleteCommand<>("bar"), script.getCommand(1));
        assertEquals(new InsertCommand<>("bar"), script.getCommand(2));
        assertEquals(new KeepCommand<>("baz"), script.getCommand(3));
        assertEquals(new KeepCommand<>("baz"), script.getCommand(4));
    }

    @Test
    public void testBuildSnake() {
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);

        Snake snake1 = comparator.buildSnake(0, 0, 3, 2);
        assertEquals(0, snake1.getStart());
        assertEquals(1, snake1.getEnd());
        assertEquals(-1, snake1.getDiag());

        Snake snake2 = comparator.buildSnake(1, 1, 3, 2);
        assertEquals(1, snake2.getStart());
        assertEquals(1, snake2.getEnd());
        assertEquals(0, snake2.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake middleSnake = comparator.getMiddleSnake(0, 3, 0, 2);

        assertEquals(1, middleSnake.getStart());
        assertEquals(1, middleSnake.getEnd());
        assertEquals(0, middleSnake.getDiag());
    }

    @Test
    public void testBuildScript() {
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();

        comparator.buildScript(0, 3, 0, 2, script);

        assertEquals(4, script.getLength());
        assertEquals(new KeepCommand<>("foo"), script.getCommand(0));
        assertEquals(new DeleteCommand<>("bar"), script.getCommand(1));
        assertEquals(new InsertCommand<>("bar"), script.getCommand(2));
        assertEquals(new KeepCommand<>("baz"), script.getCommand(3));
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScriptInternalError() {
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();

        comparator.buildScript(0, 0, 0, 2, script);
    }
}