package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Before;
import org.junit.Test;

public class SequencesComparatorTest {
    
    private SequencesComparator<Integer> comparator;
    
    @Before
    public void setUp() {
        List<Integer> seq1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> seq2 = new ArrayList<>(Arrays.asList(2, 3, 4));
        comparator = new SequencesComparator<>(seq1, seq2);
    }

    @Test
    public void testConstructor() {
        assertNotNull(comparator);
    }
    
    @Test
    public void testGetScript() {
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }
    
    @Test
    public void testBuildSnake() {
        Snake snake = comparator.buildSnake(0, 0, 1, 1);
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
        assertNull(snake);
    }
    
    @Test
    public void testBuildScript() {
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 3, script);
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