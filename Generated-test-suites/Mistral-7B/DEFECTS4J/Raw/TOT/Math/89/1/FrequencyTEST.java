package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequencyWithComparableValues() {
        Frequency frequency = new Frequency();
        // Adding comparable values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        // Checking frequencies, percentages, and cumulative percentages
        assertEquals(4, frequency.getCount(1));
        assertEquals(4, frequency.getCount(1), 0); // Asserting double equality
        assertEquals(50.0, frequency.getPct(1), 0.001);
        assertEquals(50.0, frequency.getPct(1), 0.01); // Asserting relative equality
        assertEquals(50.0, frequency.getCumPct(1), 0.0);
        assertEquals(100.0, frequency.getCumPct(2), 0.0);
        // Checking frequencies for non-comparable values (int and String)
        try {
            frequency.addValue("test");
            fail("Adding non-comparable values should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        // Testing clear method
        frequency.clear();
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getSumFreq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyWithNonComparableValues() {
        Frequency frequency = new Frequency();
        // Adding non-comparable values
        frequency.addValue(1);
        try {
            frequency.addValue('a');
        } catch (IllegalArgumentException e) {
            // Expected exception
            throw e;
        }
    }

    @Test
    public void testFrequencyWithComparator() {
        Frequency frequency = new Frequency(new Comparator<Comparable<?>>() {
            @Override
            public int compare(Comparable o1, Comparable o2) {
                return o1.compareTo(o2) * -1;
            }
        });
        // Adding values in reverse order
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        // Checking frequencies, percentages, and cumulative percentages
        assertEquals(2, frequency.getCount(1));
        assertEquals(100.0, frequency.getCumPct(2), 0.0);
        assertEquals(50.0, frequency.getCumPct(1), 0.0);
    }
}