package org.apache.commons.collections4.sequence;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testConstructor1() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
        int size = sequence1.size() + sequence2.size() + 2;
        assertEquals(size, comparator.vDown.length);
        assertEquals(size, comparator.vUp.length);
    }

    @Test
    public void testConstructor2() {
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                return o1.equals(o2);
            }

            @Override
            public int hash(Integer o) {
                return o.hashCode();
            }
        };

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
        int size = sequence1.size() + sequence2.size() + 2;
        assertEquals(size, comparator.vDown.length);
        assertEquals(size, comparator.vUp.length);
    }

    @Test
    public void testGetScript() {
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        sequence1.add("b");
        
        List<String> sequence2 = new ArrayList<>();
        sequence2.add("a");
        sequence2.add("c");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<String> script = comparator.getScript();
        assertEquals(3, script.getCommands().size());
        
        assertEquals(KeepCommand.class, script.getCommands().get(0).getClass());
        assertEquals("a", ((KeepCommand) script.getCommands().get(0)).getObject());
        
        assertEquals(DeleteCommand.class, script.getCommands().get(1).getClass());
        assertEquals("b", ((DeleteCommand) script.getCommands().get(1)).getObject());
        
        assertEquals(InsertCommand.class, script.getCommands().get(2).getClass());
        assertEquals("c", ((InsertCommand) script.getCommands().get(2)).getObject());
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(3);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake snake = comparator.buildSnake(0, 0, 2, 2);
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(3);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake snake = comparator.getMiddleSnake(0, 2, 0, 2);
        assertNull(snake);
    }

    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        
        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(3);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 2, 0, 2, script);
        
        assertEquals(3, script.getCommands().size());
        
        assertEquals(KeepCommand.class, script.getCommands().get(0).getClass());
        assertEquals(1, ((KeepCommand) script.getCommands().get(0)).getObject().intValue());
        
        assertEquals(DeleteCommand.class, script.getCommands().get(1).getClass());
        assertEquals(2, ((DeleteCommand) script.getCommands().get(1)).getObject().intValue());
        
        assertEquals(InsertCommand.class, script.getCommands().get(2).getClass());
        assertEquals(3, ((InsertCommand) script.getCommands().get(2)).getObject().intValue());
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScriptWithRuntimeException() {
        List<Integer> sequence1 = new ArrayList<>();
        
        List<Integer> sequence2 = new ArrayList<>();
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 2, 0, 2, script);
    }
}