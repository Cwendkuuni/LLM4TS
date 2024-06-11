
package org.apache.commons.collections4.sequence;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
    }

    @Test
    public void testConstructorWithEquator() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                return o1 % 2 == o2 % 2;
            }

            @Override
            public int hash(Integer o) {
                return o % 2;
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        // Test with empty sequences
        EditScript<Integer> script = comparator.getScript();
        assertEquals(0, script.getCommands().size());

        // Test with non-empty sequences
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);

        script = comparator.getScript();
        assertEquals(3, script.getCommands().size());
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        // Test with empty sequences
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 0, 0);
        assertEquals(0, snake.getStart());
        assertEquals(0, snake.getDiag());

        // Test with non-empty sequences
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);

        snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        assertEquals(0, snake.getStart());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);

        // Test with empty sequences
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 0, 0, 0);
        assertEquals(null, snake);

        // Test with non-empty sequences
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);

        snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        assertEquals(0, snake.getStart());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        EditScript<Integer> script = new EditScript<>();

        // Test with empty sequences
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.buildScript(0, 0, 0, 0, script);
        assertEquals(0, script.getCommands().size());

        // Test with non-empty sequences
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);

        comparator = new SequencesComparator<>(sequence1, sequence2);
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        assertEquals(3, script.getCommands().size());
    }
}
