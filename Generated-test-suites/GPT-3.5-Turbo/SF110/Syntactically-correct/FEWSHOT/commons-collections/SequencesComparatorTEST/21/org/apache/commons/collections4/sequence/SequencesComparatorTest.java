package org.apache.commons.collections4.sequence;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class SequencesComparatorTest {

    @Test
    public void testConstructor1() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
    }

    @Test
    public void testConstructor2() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        Equator<Integer> equator = new DefaultEquator<Integer>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(3);
        sequence2.add(4);
        Equator<Integer> equator = new DefaultEquator<Integer>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = comparator.getScript();
        assertEquals(4, script.size());
        assertEquals(new KeepCommand<>(1), script.getCommand(0));
        assertEquals(new DeleteCommand<>(2), script.getCommand(1));
        assertEquals(new KeepCommand<>(3), script.getCommand(2));
        assertEquals(new InsertCommand<>(4), script.getCommand(3));
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence1.add(4);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(5);
        Equator<Integer> equator = new DefaultEquator<Integer>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 4, 3);
        assertEquals(1, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(1, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence1.add(4);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(5);
        Equator<Integer> equator = new DefaultEquator<Integer>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 4, 0, 3);
        assertEquals(1, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(1, snake.getDiag());
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence1.add(4);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(5);
        Equator<Integer> equator = new DefaultEquator<Integer>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 4, 0, 3, script);
        assertEquals(4, script.size());
        assertEquals(new DeleteCommand<>(1), script.getCommand(0));
        assertEquals(new KeepCommand<>(2), script.getCommand(1));
        assertEquals(new KeepCommand<>(3), script.getCommand(2));
        assertEquals(new InsertCommand<>(4), script.getCommand(3));
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScriptThrowsException() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        List<Integer> sequence2 = new ArrayList<>();
        Equator<Integer> equator = new DefaultEquator<Integer>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 1, 0, 0, script);
    }
}