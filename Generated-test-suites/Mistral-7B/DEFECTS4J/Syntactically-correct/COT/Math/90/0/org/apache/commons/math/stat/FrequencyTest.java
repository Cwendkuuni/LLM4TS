package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Test class for Frequency class
 */
public class FrequencyTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);

        Frequency frequencyComparator = new Frequency(Comparator.naturalOrder());
        assertNotNull(frequencyComparator);
    }

    /**
     * Test toString method when no values have been added
     */
    @org.junit.Test
    public void testToStringEmpty() {
        Frequency frequency = new Frequency();
        String result = frequency.toString();
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n", result);
    }

    /**
     * Test addValue method with different value types
     */
    @org.junit.Test
    public void testAddValueDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("test");
        frequency.addValue('A');

        // check size of frequency table
        assertEquals(3, frequency.values().size());

        // check values and frequencies
        Iterator<Object> iterator = frequency.values().iterator();
        Object firstValue = iterator.next();
        assertEquals(1, frequency.getCount(firstValue));
        assertNotSame(firstValue, iterator.next());
        assertEquals("test", iterator.next());
        assertEquals(1, frequency.getCount(iterator.next()));
        assertSame(iterator.next(), iterator.next());
        assertEquals('A', iterator.next());
        assertEquals(1, frequency.getCount(iterator.next()));
    }

    /**
     * Test getSumFreq method
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        long sumFreq = frequency.getSumFreq();
        assertEquals(6, sumFreq);
    }

    /**
     * Test getCount method with different value types
     */
    @org.junit.Test
    public void testGetCountDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("test");
        frequency.addValue('A');

        assertEquals(1, frequency.getCount(1));
        assertEquals(0, frequency.getCount("test"));
        assertEquals(0, frequency.getCount('A'));
    }

    /**
     * Test getCount method with same value
     */
    @org.junit.Test
    public void testGetCountSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(2, frequency.getCount(1));
    }

    /**
     * Test getPct method with different value types
     */
    @org.junit.Test
    public void testGetPctDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("test");
        frequency.addValue('A');

        double pct1 = frequency.getPct(1);
        assertEquals(0.3333333333333333, pct1, 0.001);

        double pctTest = frequency.getPct("test");
        assertEquals(Double.NaN, pctTest, 0.0);

        double pctA = frequency.getPct('A');
        assertEquals(Double.NaN, pctA, 0.0);
    }

    /**
     * Test getPct method with same value
     */
    @org.junit.Test
    public void testGetPctSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        double pct = frequency.getPct(1);
        assertEquals(0.5, pct, 0.001);
    }

    /**
     * Test getCumFreq method with different value types
     */
    @org.junit.Test
    public void testGetCumFreqDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("test");
        frequency.addValue('A');

        long cumFreq1 = frequency.getCumFreq(1);
        assertEquals(1, cumFreq1);

        long cumFreqTest = frequency.getCumFreq("test");
        assertEquals(1, cumFreqTest);

        long cumFreqA = frequency.getCumFreq('A');
        assertEquals(1, cumFreqA);

        long cumFreqNone = frequency.getCumFreq(null);
        assertEquals(0, cumFreqNone);
    }

    /**
     * Test getCumFreq method with same value
     */
    @org.junit.Test
    public void testGetCumFreqSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        long cumFreq1 = frequency.getCumFreq(1);
        assertEquals(2, cumFreq1);

        long cumFreqNone = frequency.getCumFreq(0);
        assertEquals(0, cumFreqNone);
    }

    /**
     * Test getCumPct method with different value types
     */
    @org.junit.Test
    public void testGetCumPctDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("test");
        frequency.addValue('A');

        double cumPct1 = frequency.getCumPct(1);
        assertEquals(0.3333333333333333, cumPct1, 0.001);

        double cumPctTest = frequency.getCumPct("test");
        assertEquals(Double.NaN, cumPctTest, 0.0);

        double cumPctA = frequency.getCumPct('A');
        assertEquals(Double.NaN, cumPctA, 0.0);
    }

    /**
     * Test getCumPct method with same value
     */
    @org.junit.Test
    public void testGetCumPctSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        double cumPct1 = frequency.getCumPct(1);
        assertEquals(0.5, cumPct1, 0.001);

        double cumPctNone = frequency.getCumPct(0);
        assertEquals(0.0, cumPctNone, 0.0);
    }

    /**
     * Test addValue method with invalid value
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(null);
    }

    /**
     * Test addValue method with non-comparable value
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    /**
     * Test clear method
     */
    @org.junit.Test
     public void testClear() {
         Frequency frequency = new Frequency();
         frequency.addValue(1);
         frequency.clear();
         assertEquals(0, frequency.getSumFreq());
     }
}