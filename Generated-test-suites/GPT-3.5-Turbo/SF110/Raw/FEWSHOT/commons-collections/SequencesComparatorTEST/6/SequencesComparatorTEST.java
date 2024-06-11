
package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;

public class SequencesComparatorTest {
    @Test
    public void testConstructor() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
        
        assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vDown.length);
        assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vUp.length);
    }
    
    @Test
    public void testConstructorWithEquator() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                return o1 % 2 == 0 && o2 % 2 == 0;
            }

            @Override
            public int hash(Integer object) {
                return object.hashCode();
            }
        };
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(equator, comparator.equator);
        
        assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vDown.length);
        assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vUp.length);
    }
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = comparator.getScript();
        
        // TODO: Assert the contents of the script
    }
    
    @Test
    public void testBuildSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        
        // TODO: Assert the values in the snake object
    }
    
    @Test
    public void testGetMiddleSnake() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        
        // TODO: Assert the values in the snake object
    }
    
    @Test
    public void testBuildScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> script = new EditScript<>();
        
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        
        // TODO: Assert the contents of the script
    }
    
}
