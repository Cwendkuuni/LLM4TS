package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.collections4.equators.DefaultEquator;
import org.apache.commons.collections4.equators.Equator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testConstructor1() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        assertEquals(sequence1, comparator.getSequence1());
        assertEquals(sequence2, comparator.getSequence2());
        assertEquals(DefaultEquator.defaultEquator(), comparator.getEquator());
    }

    @Test
    public void testConstructor2() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                return false;
            }

            @Override
            public int hash(Integer o) {
                return 0;
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);

        assertEquals(sequence1, comparator.getSequence1());
        assertEquals(sequence2, comparator.getSequence2());
        assertEquals(equator, comparator.getEquator());
    }

    @Test
    public void testGetScript_Null() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        assertNull(comparator.getScript());
    }

    @Test
    public void testGetScript_NotNull() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        assertNotNull(comparator.getScript());
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();

        comparator.buildScript(0, 0, 0, 0, script);

        assertEquals(0, script.size());
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        int start = 0;
        int diag = 1;
        int end1 = 0;
        int end2 = 0;

        Snake snake = comparator.buildSnake(start, diag, end1, end2);

        assertNotNull(snake);
        assertEquals(start, snake.getStart());
        assertEquals(start, snake.getEnd());
        assertEquals(diag, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake_Null() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        int start1 = 0;
        int end1 = 0;
        int start2 = 0;
        int end2 = 0;

        Snake middleSnake = comparator.getMiddleSnake(start1, end1, start2, end2);

        assertNull(middleSnake);
    }

    @Test
    public void testGetMiddleSnake_NotNull() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        int start1 = 0;
        int end1 = 1;
        int start2 = 1;
        int end2 = 2;

        Snake middleSnake = comparator.getMiddleSnake(start1, end1, start2, end2);

        assertNotNull(middleSnake);
    }

    @Test
    public void testSnake_Getters() {
        int start = 0;
        int end = 1;
        int diag = 2;
        SequencesComparator.Snake snake = new SequencesComparator.Snake(start, end, diag);

        assertEquals(start, snake.getStart());
        assertEquals(end, snake.getEnd());
        assertEquals(diag, snake.getDiag());
    }
}