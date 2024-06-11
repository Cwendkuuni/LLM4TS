

package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.InsertCommand;
import org.apache.commons.collections4.sequence.KeepCommand;
import org.apache.commons.collections4.sequence.SequencesComparator;
import org.apache.commons.collections4.sequence.DeleteCommand;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequencesComparatorTest {
    @Test
    public void testConstructor1() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
    }
    
    @Test
    public void testConstructor2() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
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
    }
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        assertEquals(3, script.size());
        assertEquals(new InsertCommand<>(4), script.get(0));
        assertEquals(new InsertCommand<>(5), script.get(1));
        assertEquals(new InsertCommand<>(6), script.get(2));
    }

    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, 3, 3);
        
        assertEquals(3, snake.getStart());
        assertEquals(3, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, 3, 0, 3);
        
        assertNull(snake);
    }
    
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, 3, 0, 3, script);
        
        assertEquals(3, script.size());
        assertEquals(new InsertCommand<>(4), script.get(0));
        assertEquals(new InsertCommand<>(5), script.get(1));
        assertEquals(new InsertCommand<>(6), script.get(2));
    }
}

