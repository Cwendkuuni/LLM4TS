package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructor1() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator);
    }

    @Test
    public void testConstructor2() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
        assertNotNull(comparator);
    }

    @Test
    public void testGetScript() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();
        assertNotNull(script);
    }

    @Test
    public void testBuildSnake() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.buildSnake(0, 0, 1, 1);
        assertNotNull(snake);
    }

    @Test
    public void testGetMiddleSnake() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        Snake snake = comparator.getMiddleSnake(0, 1, 0, 1);
        assertNotNull(snake);
    }

    @Test
    public void testBuildScript1() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, 1, 0, 1, script);
        assertNotNull(script);
    }

    @Test
    public void testBuildScript2() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, 0, 0, 0, script);
        assertNotNull(script);
    }

    @Test
    public void testSnakeGetStart() {
        Snake snake = new Snake(0, 1, 0);
        assertEquals(0, snake.getStart());
    }

    @Test
    public void testSnakeGetEnd() {
        Snake snake = new Snake(0, 1, 0);
        assertEquals(1, snake.getEnd());
    }

    @Test
    public void testSnakeGetDiag() {
        Snake snake = new Snake(0, 1, 0);
        assertEquals(0, snake.getDiag());
    }
    
}