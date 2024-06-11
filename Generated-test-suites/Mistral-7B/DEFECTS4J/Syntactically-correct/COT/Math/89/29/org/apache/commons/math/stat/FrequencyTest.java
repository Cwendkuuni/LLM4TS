package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

/**
 * Test class for Frequency class
 */
public class FrequencyTest {
    /** Test data for testing addValue method */
    private Frequency frequency;
    static final int[] intArray = {1, 2, 3, 4};
    static final long[] longArray = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L};
    static final char[] charArray = {'a', 'b', 'c', 'd'};
    static final Object[] objectArray = {1, 2, 3, 4, 'a', 'b', 'c', 'd'};
    
    @Test
    public void testFrequency() {
        // Test constructor with default comparator
        frequency = new Frequency();
        
        // Test toString method
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 4 \t 0.4 \t 0.4\n2 \t 4 \t 0.4 \t 0.8\n3 \t 4 \t 0.4 \t 1.2\n4 \t 4 \t 0.4 \t 1.6\n" +
                     "a \t 1 \t 0.1 \t 1.7\nb \t 1 \t 0.1 \t 1.8\nc \t 1 \t 0.1 \t 2.0\nd \t 1 \t 0.1 \t 2.1\n", frequency.toString());

        // Test constructor with custom comparator
        Comparator<Comparable> comparator = new NaturalComparator();
        Frequency frequencyWithComparator = new Frequency(comparator);
        
        // Test addValue method with int values
        for (int i : intArray) {
            frequency.addValue(i);
        }
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 4 \t 0.4 \t 0.4\n2 \t 4 \t 0.4 \t 0.8\n3 \t 4 \t 0.4 \t 1.2\n4 \t 4 \t 0.4 \t 1.6\n",
                frequency.toString());
        
        // Test addValue method with long values
        for (long l : longArray) {
            frequency.addValue(l);
        }
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 10 \t 0.1 \t 0.1\n2 \t 10 \t 0.1 \t 0.2\n3 \t 10 \t 0.1 \t 0.3\n4 \t 10 \t 0.1 \t 0.4\n5 \t 10 \t 0.1 \t 0.5\n6 \t 10 \t 0.1 \t 0.6\n7 \t 10 \t 0.1 \t 0.7\n8 \t 10 \t 0.1 \t 0.8\n9 \t 10 \t 0.1 \t 0.9\n10 \t 10 \t 0.1 \t 1.0\n",
                frequency.toString());
        
        // Test addValue method with char values
        for (char c : charArray) {
            frequency.addValue(c);
        }
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\na \t 1 \t 0.1 \t 1.1\nb \t 1 \t 0.1 \t 1.2\nc \t 1 \t 0.1 \t 1.3\nd \t 1 \t 0.1 \t 1.4\n",
                frequency.toString());

        // Test addValue method with Object values
        for (Object o : objectArray) {
            frequency.addValue(o);
        }
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 10 \t 0.1 \t 0.1\n2 \t 10 \t 0.1 \t 0.2\n3 \t 10 \t 0.1 \t 0.3\n4 \t 10 \t 0.1 \t 0.4\na \t 1 \t 0.1 \t 0.5\nb \t 1 \t 0.1 \t 0.6\nc \t 1 \t 0.1 \t 0.7\nd \t 1 \t 0.1 \t 0.8\n",
                frequency.toString());

        // Test addValue method with non-comparable values
        assertThrows(IllegalArgumentException.class, () -> frequency.addValue(new String("abc")));

        // Test addValue method with null values
        assertThrows(NullPointerException.class, () -> frequency.addValue(null));

        // Test addValue method with large values
        try {
            frequency.addValue(Long.parseLong("9223372036854775808L", 10));
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        // Test getCount method
        for (int i : intArray) {
            assertEquals(1, frequency.getCount(i));
        }
        for (long l : longArray) {
            assertEquals(1, frequency.getCount(l));
        }
        for (char c : charArray) {
            assertEquals(1, frequency.getCount(c));
        }
        for (Object o : objectArray) {
            if (o instanceof Number) {
                assertEquals(1, frequency.getCount(o));
            } else {
                assertNull(frequency.getCount(o));
            }
        }
        
        // Test getCount method with non-comparable values
        assertNull(frequency.getCount(new String("abc")));

        // Test getSumFreq method
        assertEquals(30, frequency.getSumFreq());

        // Test clear method
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        
        // Test valuesIterator method
        TreeMap<Comparable<?>, Long> expectedMap = new TreeMap<>();
        for (int i : intArray) {
            expectedMap.put(Long.valueOf(i), Long.valueOf(1));
        }
        for (long l : longArray) {
            expectedMap.put(Long.valueOf(l), Long.valueOf(1));
        }
        for (char c : charArray) {
            expectedMap.put(Character.valueOf(c), Long.valueOf(1));
        }
        for (Object o : objectArray) {
            if (o instanceof Comparable) {
                expectedMap.put((Comparable<?>) o, Long.valueOf(1));
            }
        }
        Iterator<Comparable<?>> expectedIterator = expectedMap.keySet().iterator();
        Iterator<Object> actualIterator = frequency.valuesIterator();
        boolean equals = true;
        while (expectedIterator.hasNext() && actualIterator.hasNext()) {
            Comparable<?> expected = expectedIterator.next();
            Object actual = actualIterator.next();
            if (!expected.equals(actual)) {
                equals = false;
                break;
            }
        }
        assertTrue(equals);

        // Test getCount method with get method chaining
        assertEquals(1, frequency.getCount(1).longValue());
        assertEquals(1, frequency.getCount(2).longValue());
        assertEquals(1, frequency.getCount(3).longValue());
        assertEquals(1, frequency.getCount(4).longValue());
        assertEquals(1, frequency.getCount('a').longValue());
        assertEquals(1, frequency.getCount('b').longValue());
        assertEquals(1, frequency.getCount('c').longValue());
        assertEquals(1, frequency.getCount('d').longValue());

        // Test getPct method
        for (int i : intArray) {
            assertEquals(0.04, frequency.getPct(i), 0.001);
        }
        for (long l : longArray) {
            assertEquals(0.03333333333333333, frequency.getPct(l), 0.001);
        }
        for (char c : charArray) {
            assertEquals(0.03333333333333333, frequency.getPct(c), 0.001);
        }
        assertEquals(0.0, frequency.getPct(new String("abc")));

        // Test getCumFreq method
        for (int i : intArray) {
            assertEquals(0.04, frequency.getCumFreq(i) / 30.0, 0.001);
        }
        for (long l : longArray) {
            assertEquals(l / 10.0, frequency.getCumFreq(l) / 30.0, 0.001);
        }
        for (char c : charArray) {
            assertEquals(getPct(c), frequency.getCumFreq(c) / 30.0, 0.001);
        }
        assertEquals(0.0, frequency.getCumFreq(new String("abc")));

        // Test getCumPct method
        for (int i : intArray) {
            assertEquals(0.04, frequency.getCumPct(i), 0.001);
        }
        for (long l : longArray) {
            assertEquals(l / 10.0, frequency.getCumPct(l), 0.001);
        }
        for (char c : charArray) {
            assertEquals(getPct(c), frequency.getCumPct(c), 0.001);
        }
        assertEquals(1.0, frequency.getCumPct(new String("abc")));
    }
    
    private double getPct(Comparable<?> value) {
        long sumFreq = frequency.getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return (double) frequency.getCount(value) / (double) sumFreq;
    }
}