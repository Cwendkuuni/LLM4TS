package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Before;
import org.junit.Test;

public class SequencesComparatorTest {

    private List<Integer> sequence1;
    private List<Integer> sequence2;

    @Before
    public void setUp() {
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>();

        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);
    }

    @Test
    public void testConstructorWithDefaultEquator() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        assertEquals(sequence1, comparator.sequence1);
        assertEquals(sequence2, comparator.sequence2);
        assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
    }

    @Test
    public void testConstructorWithEquator() {
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
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        // TODO: Test the contents of the edit script
    }
}