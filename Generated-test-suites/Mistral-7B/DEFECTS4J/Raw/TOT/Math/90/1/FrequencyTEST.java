package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequencyAddValues() {
        Frequency frequency = new Frequency();
        // Adding integral values
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        // Adding char value
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');

        // Checking getCount method
        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(2));
        assertEquals(2, frequency.getCount('A'));
        assertEquals(1, frequency.getCount('B'));

        // Checking getCumFreq method
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(6, frequency.getCumFreq(2));
        assertEquals(8, frequency.getCumFreq('A'));
        assertEquals(8, frequency.getCumFreq('A')); // Cumulative frequency of same value should be the same
        assertEquals(9, frequency.getCumFreq('B'));

        // Checking getCumPct method
        assertEquals(0.375, frequency.getCumPct(1), 0.001);
        assertEquals(0.625, frequency.getCumPct(2), 0.001);
        assertEquals(0.25, frequency.getCumPct('A'), 0.001);
        assertEquals(0.25, frequency.getCumPct('B'), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('A');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddNonComparableValues2() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(new Integer(2));
    }

    @Test
    public void testFrequencyClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
    }
}