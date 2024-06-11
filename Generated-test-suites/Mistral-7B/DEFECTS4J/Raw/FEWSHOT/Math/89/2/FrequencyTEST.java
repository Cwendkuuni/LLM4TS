package org.apache.commons.math.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
        
        Frequency comparatorFreq = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<?>) o1).compareTo(o2);
            }
        });
        assertNotNull(comparatorFreq);
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue("one");
        freq.addValue("one");
        freq.addValue("two");
        freq.addValue("two");
        freq.addValue("three");

        String result = freq.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n"
                + "one \t 2 \t 0.4 \t 0.4 \t\n"
                + "two \t 2 \t 0.4 \t 0.8 \t\n"
                + "three \t 1 \t 0.2 \t 1.0 \t\n";
        assertEquals(expected, result);
    }

    /**
     * Test addValue method
     */
    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        
        // Test Integer values
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        freq.addValue(1);
        assertEquals(2, freq.getCount(1));
        
        freq.addValue(2);
        assertEquals(1, freq.getCount(2));
        freq.addValue(2);
        assertEquals(2, freq.getCount(2));

        // Test Long values
        freq.addValue(Long.valueOf(3));
        assertEquals(1, freq.getCount(3));
        freq.addValue(Long.valueOf(3));
        assertEquals(2, freq.getCount(3));

        // Test String values
        freq.addValue("one");
        assertEquals(1, freq.getCount("one"));
        freq.addValue("one");
        assertEquals(2, freq.getCount("one"));

        // Test null value
        freq.addValue(null);
        assertNull(freq.getCount(null));

        // Test non-Comparable value
        try {
            freq.addValue(new Object() {});
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test adding same value with different types
        freq.addValue(Character.valueOf('a'));
        assertEquals(1, freq.getCount('a'));
        freq.addValue(Character.valueOf('a'));
        assertEquals(2, freq.getCount('a'));
        freq.addValue(Integer.valueOf(1));
        assertEquals(3, freq.getCount(1));
        
        // Test adding different values
        freq.addValue(Character.valueOf('b'));
        assertEquals(1, freq.getCount('b'));
        freq.addValue(Integer.valueOf(2));
        assertEquals(1, freq.getCount(2));
        
        // Test clearing values
        freq.clear();
        assertEquals(0, freq.getSumFreq());
    }

    /**
     * Test valuesIterator method
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);

        Iterator values = freq.valuesIterator();
        assertEquals(1, values.next().longValue());
        assertEquals(2, values.next().longValue());
        assertEquals(3, values.next().longValue());
        assertNull(values.next());
    }

    /**
     * Test getSumFreq method
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);

        long result = freq.getSumFreq();
        assertEquals(6, result);
    }

    /**
     * Test getCount method
     */
    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);

        long result = freq.getCount(1);
        assertEquals(2, result);
        
        result = freq.getCount(2);
        assertEquals(2, result);
        
        result = freq.getCount(3);
        assertEquals(1, result);
        
        result = freq.getCount(4);
        assertEquals(0, result);
    }

    /**
     * Test getPct method
     */
    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);

        double result = freq.getPct(1);
        assertEquals(0.33, result, 0.01);
        
        result = freq.getPct(2);
        assertEquals(0.33, result, 0.01);
        
        result = freq.getPct(3);
        assertEquals(0.17, result, 0.01);
        
        result = freq.getPct(4);
        assertEquals(0.0, result, 0.01);
    }

    /**
     * Test getCumFreq method
     */
    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);

        long result = freq.getCumFreq(1);
        assertEquals(2, result);
        
        result = freq.getCumFreq(2);
        assertEquals(4, result);
        
        result = freq.getCumFreq(3);
        assertEquals(5, result);
        
        result = freq.getCumFreq(4);
        assertEquals(5, result);
        
        result = freq.getCumFreq(5);
        assertEquals(5, result);
    }

    /**
     * Test getCumPct method
     */
    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);

        double result = freq.getCumPct(1);
        assertEquals(0.33, result, 0.01);
        
        result = freq.getCumPct(2);
        assertEquals(0.67, result, 0.01);
        
        result = freq.getCumPct(3);
        assertEquals(1.0, result, 0.01);
        
        result = freq.getCumPct(4);
        assertEquals(1.0, result, 0.01);
        
        result = freq.getCumPct(5);
        assertEquals(1.0, result, 0.01);
    }

    /**
     * Test clear method
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);

        freq.clear();

        long result = freq.getSumFreq();
        assertEquals(0, result);
        
        result = freq.getCount(1);
        assertEquals(0, result);
        
        result = freq.getCount(2);
        assertEquals(0, result);
        
        result = freq.getCount(3);
        assertEquals(0, result);
    }
}