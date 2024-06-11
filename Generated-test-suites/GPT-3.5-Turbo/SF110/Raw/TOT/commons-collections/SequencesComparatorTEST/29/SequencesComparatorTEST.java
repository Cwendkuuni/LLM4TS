package org.apache.commons.collections4.sequence;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {
    
    @Test
    public void testConstructor() {
        List<String> sequence1 = new ArrayList<String>();
        List<String> sequence2 = new ArrayList<String>();
        SequencesComparator<String> sc = new SequencesComparator<String>(sequence1, sequence2);
        assertNotNull(sc);
    }
    
    @Test
    public void testConstructorWithEquator() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        List<Integer> sequence2 = new ArrayList<Integer>();
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> sc = new SequencesComparator<Integer>(sequence1, sequence2, equator);
        assertNotNull(sc);
    }
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        List<Integer> sequence2 = new ArrayList<Integer>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(3);
        SequencesComparator<Integer> sc = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = sc.getScript();
        assertEquals(3, script.size());
        assertTrue(script.getCommand(0) instanceof KeepCommand);
        assertTrue(script.getCommand(1) instanceof KeepCommand);
        assertTrue(script.getCommand(2) instanceof KeepCommand);
    }
    
    @Test
    public void testBuildSnake() {
        List<String> sequence1 = new ArrayList<String>();
        List<String> sequence2 = new ArrayList<String>();
        sequence1.add("a");
        sequence1.add("b");
        sequence2.add("a");
        sequence2.add("c");
        sequence2.add("b");
        sequence2.add("d");
        SequencesComparator<String> sc = new SequencesComparator<String>(sequence1, sequence2);
        SequencesComparator.Snake snake = sc.buildSnake(0, 0, 4, 2);
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        List<Integer> sequence2 = new ArrayList<Integer>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence2.add(2);
        SequencesComparator<Integer> sc = new SequencesComparator<Integer>(sequence1, sequence2);
        SequencesComparator.Snake snake = sc.getMiddleSnake(0, 3, 0, 1);
        assertNull(snake);
    }
    
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        List<Integer> sequence2 = new ArrayList<Integer>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);
        SequencesComparator<Integer> sc = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<Integer>();
        sc.buildScript(0, 3, 0, 3, script);
        assertEquals(4, script.size());
        assertTrue(script.getCommand(0) instanceof DeleteCommand);
        assertTrue(script.getCommand(1) instanceof KeepCommand);
        assertTrue(script.getCommand(2) instanceof KeepCommand);
        assertTrue(script.getCommand(3) instanceof InsertCommand);
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuildScriptWithInvalidIndices() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        List<Integer> sequence2 = new ArrayList<Integer>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);
        SequencesComparator<Integer> sc = new SequencesComparator<Integer>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<Integer>();
        sc.buildScript(1, 3, 0, 2, script);
    }
    
    @Test
    public void testSnakeGetStart() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 2, 0);
        assertEquals(1, snake.getStart());
    }
    
    @Test
    public void testSnakeGetEnd() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 2, 0);
        assertEquals(2, snake.getEnd());
    }
    
    @Test
    public void testSnakeGetDiag() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 2, 0);
        assertEquals(0, snake.getDiag());
    }
}