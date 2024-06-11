package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test default constructor and basic functionality.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals("Incorrect frequency count for value 1.", 2, frequency.getCount(1));
        assertEquals("Incorrect frequency count for value 2.", 2, frequency.getCount(2));
        assertEquals("Incorrect total frequency count.", 6, frequency.getSumFreq());
        assertEquals("Incorrect percentage of values equal to 1.", 1.0 / 3.0, frequency.getPct(1), 0.001);
        assertEquals("Incorrect percentage of values equal to 2.", 1.0 / 3.0, frequency.getPct(2), 0.001);
        assertEquals("Incorrect cumulative frequency for value 1.", 2, frequency.getCumFreq(1));
        assertEquals("Incorrect cumulative frequency for value 2.", 4, frequency.getCumFreq(2));
        assertEquals("Incorrect cumulative percentage of values less than or equal to 1.", 1.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals("Incorrect cumulative percentage of values less than or equal to 2.", 2.0 / 3.0, frequency.getCumPct(2), 0.001);
    }

    /**
     * Test constructor with Comparator.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals("Incorrect frequency count for value 1.", 3, frequency.getCount(1));
        assertEquals("Incorrect frequency count for value 2.", 1, frequency.getCount(2));
        assertEquals("Incorrect total frequency count.", 4, frequency.getSumFreq());
        assertEquals("Incorrect percentage of values equal to 1.", 3.0 / 4.0, frequency.getPct(1), 0.001);
        assertEquals("Incorrect percentage of values equal to 2.", 1.0 / 4.0, frequency.getPct(2), 0.001);
        assertEquals("Incorrect cumulative frequency for value 1.", 3, frequency.getCumFreq(1));
        assertEquals("Incorrect cumulative frequency for value 2.", 1, frequency.getCumFreq(2));
        assertEquals("Incorrect cumulative percentage of values less than or equal to 1.", 3.0 / 4.0, frequency.getCumPct(1), 0.001);
        assertEquals("Incorrect cumulative percentage of values less than or equal to 2.", 1.0 / 4.0, frequency.getCumPct(2), 0.001);
    }

    /**
     * Test addValue method with IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgument() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('a');
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.clear();
        assertEquals("Incorrect total frequency count after clear.", 0, frequency.getSumFreq());
    }
}