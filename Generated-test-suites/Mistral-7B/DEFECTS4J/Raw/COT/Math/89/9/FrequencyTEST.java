package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math.stat.Frequency;

/**
 * Test case for Frequency class
 */
public class FrequencyTest {

    

    @org.junit.Before
    public void setUp() {
        // Initialize test objects
        Frequency frequency = new Frequency();
        Frequency frequencyWithComparator = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<?>) o1).compareTo(o2);
            }
        });
    }

    @org.junit.Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(null));
        assertTrue(frequency.isEmpty());
    }

    @org.junit.Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(1, ((Comparable<?>) iterator.next()).compareTo(1));
        assertEquals(1, ((Comparable<?>) iterator.next()).compareTo(1));
        assertEquals(2, ((Comparable<?>) iterator.next()).compareTo(1));
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(1.0, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('A');
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueWithExistingNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('A');
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueWithExistingDifferentType() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2L);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueWithExistingDifferentTypeChar() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('2');
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueWithExistingDifferentTypeInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(new Integer(2));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueWithExistingDifferentTypeLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(new Long(2));
    }

    @org.junit.Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertFalse(frequency.valuesIterator().hasNext());
    }

    @org.junit.Test
    public void testCustomComparator() {
        Frequency frequency = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<?>) o2).compareTo(o1);
            }
        });
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq(2));
        assertEquals(1.0, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
    }

    @org.junit.Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n"
                + "1 \t 2 \t 0.5 \t 0.5 \n"
                + "2 \t 1 \t 0.25 \t 0.75 \n";

        assertEquals(expected, frequency.toString());
    }

    
}