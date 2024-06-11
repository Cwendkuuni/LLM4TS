package org.apache.commons.collections4.sequence;

import java.util.*;
import org.junit.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testConstructor1() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        List<Integer> sequence2 = new ArrayList<Integer>();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertNotNull(comparator);
    }
    
    @Test
    public void testConstructor2() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        List<Integer> sequence2 = new ArrayList<Integer>();
        Equator<Integer> equator = DefaultEquator.defaultEquator();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        assertNotNull(comparator);
    }
    
    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<Integer>();
        sequence1.add(1);
        sequence1.add(2);
        List<Integer> sequence2 = new ArrayList<Integer>();
        sequence2.add(2);
        sequence2.add(3);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        assertNotNull(script);
    }
}