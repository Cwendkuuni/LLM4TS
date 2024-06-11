package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.collections4.Equator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        Equator<Integer> equator = Equator.defaultEquator();

        SequencesComparator<Integer> comparator1 = new SequencesComparator<>(sequence1, sequence2);
        assertEquals(sequence1, comparator1.sequence1);
        assertEquals(sequence2, comparator1.sequence2);
        assertEquals(equator, comparator1.equator);

        SequencesComparator<Integer> comparator2 = new SequencesComparator<>(sequence1, sequence2, equator);
        assertEquals(sequence1, comparator2.sequence1);
        assertEquals(sequence2, comparator2.sequence2);
        assertEquals(equator, comparator2.equator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();
        // Perform asserts on the script here
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        Snake snake = comparator.buildSnake(0, 1, 2, 3);
        // Perform asserts on the snake here
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        Snake middleSnake = comparator.getMiddleSnake(0, 1, 2, 3);
        // Perform asserts on the middle snake here
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 1, 2, 3, script);
        // Perform asserts on the script here
    }

    @Test
    public void testSnakeStart() {
        Snake snake = new Snake(0, 1, 2);
        assertEquals(0, snake.getStart());
    }

    @Test
    public void testSnakeEnd() {
        Snake snake = new Snake(0, 1, 2);
        assertEquals(1, snake.getEnd());
    }

    @Test
    public void testSnakeDiag() {
        Snake snake = new Snake(0, 1, 2);
        assertEquals(2, snake.getDiag());
    }
}