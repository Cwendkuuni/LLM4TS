package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.statistics.Statistics;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequencyAddValues() {
        Frequency frequency = new Frequency();
        // Adding integral values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        // Adding char value
        frequency.addValue('A');
        frequency.addValue('A');
        // Adding object value
        frequency.addValue(Integer.valueOf(3));
        frequency.addValue(Integer.valueOf(3));
        // Testing getCount method with valid values
        assertEquals(4, frequency.getCount(1));
        assertEquals(4, frequency.getCount(1L));
        assertEquals(4, frequency.getCount(1l));
        assertEquals(4, frequency.getCount(1));
        assertEquals(4, frequency.getCount(Integer.valueOf(1)));
        assertEquals(4, frequency.getCount(Integer.valueOf(1L)));
        assertEquals(4, frequency.getCount(Integer.valueOf(1l)));
        assertEquals(2, frequency.getCount(2));
        assertEquals(2, frequency.getCount(2L));
        assertEquals(2, frequency.getCount(2l));
        assertEquals(2, frequency.getCount(2));
        assertEquals(2, frequency.getCount(2));
        assertEquals(2, frequency.getCount(Long.valueOf(2)));
        assertEquals(2, frequency.getCount(Long.valueOf(2L)));
        assertEquals(2, frequency.getCount(Long.valueOf(2l)));
        assertEquals(2, frequency.getCount(Character.valueOf('A')));
        assertEquals(2, frequency.getCount(Character.valueOf('A')));
        assertEquals(2, frequency.getCount('A'));
        assertEquals(2, frequency.getCount('A'));
        assertEquals(2, frequency.getCount(Character.valueOf('A')));
        assertEquals(2, frequency.getCount(Character.valueOf('A')));
        assertEquals(2, frequency.getCount(Character.valueOf('A')));
        assertEquals(2, frequency.getCount(Character.valueOf('A')));
        assertEquals(4, frequency.getCount(Integer.valueOf(3)));
        assertEquals(4, frequency.getCount(Integer.valueOf(3L)));
        assertEquals(4, frequency.getCount(Integer.valueOf(3l)));
        assertEquals(4, frequency.getCount(Integer.valueOf(3)));
        assertEquals(4, frequency.getCount(Integer.valueOf(3)));
        assertEquals(4, frequency.getCount(Integer.valueOf(3)));
        assertEquals(4, frequency.getCount(Integer.valueOf(3)));
        // Testing getCount method with invalid value
        assertNull(frequency.getCount('B'));
        assertNull(frequency.getCount(Character.valueOf(3)));
        assertNull(frequency.getCount('3'));
        assertNull(frequency.getCount(Character.valueOf(3L)));
        assertNull(frequency.getCount(Character.valueOf(3l)));
        // Testing getSumFreq method
        assertEquals(12, frequency.getSumFreq());
        // Testing clear method
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testFrequencyGetPercentages() {
        Frequency frequency = new Frequency();
        // Adding integral values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        // Adding char value
        frequency.addValue('A');
        frequency.addValue('A');
        // Adding object value
        frequency.addValue(Integer.valueOf(3));
        frequency.addValue(Integer.valueOf(3));
        // Testing getPct method with valid values
        assertEquals(0.3333, frequency.getPct(1), 0.001);
        assertEquals(0.3333, frequency.getPct(1L), 0.001);
        assertEquals(0.3333, frequency.getPct(1l), 0.001);
        assertEquals(0.3333, frequency.getPct(1), 0.001);
        assertEquals(0.3333, frequency.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(0.3333, frequency.getPct(Integer.valueOf(1L)), 0.001);
        assertEquals(0.3333, frequency.getPct(Integer.valueOf(1l)), 0.001);
        assertEquals(0.3333, frequency.getPct(2), 0.001);
        assertEquals(0.3333, frequency.getPct(2L), 0.001);
        assertEquals(0.3333, frequency.getPct(2l), 0.001);
        assertEquals(0.3333, frequency.getPct(2), 0.001);
        assertEquals(0.3333, frequency.getPct(Character.valueOf('A')), 0.001);
        assertEquals(0.3333, frequency.getPct(Character.valueOf('A')), 0.001);
        assertEquals(0.3333, frequency.getPct('A'), 0.001);
        assertEquals(0.3333, frequency.getPct('A'), 0.001);
        assertEquals(0.3333, frequency.getPct(Character.valueOf('A')), 0.001);
        assertEquals(0.3333, frequency.getPct(Character.valueOf('A')), 0.001);
        assertEquals(0.3333, frequency.getPct(Character.valueOf('A')), 0.001);
        assertEquals(0.3333, frequency.getPct(Character.valueOf('A')), 0.001);
        assertEquals(0.3333, frequency.getPct(Character.valueOf('A')), 0.001);
        assertEquals(0.3333, frequency.getPct(Integer.valueOf(3)), 0.001);
        assertEquals(0.3333, frequency.getPct(Integer.valueOf(3L)), 0.001);
        assertEquals(0.3333, frequency.getPct(Integer.valueOf(3l)), 0.001);
        assertEquals(0.3333, frequency.getPct(Integer.valueOf(3)), 0.001);
        assertEquals(0.3333, frequency.getPct(Integer.valueOf(3)), 0.001);
        assertEquals(0.3333, frequency.getPct(Integer.valueOf(3)), 0.001);
        assertEquals(0.3333, frequency.getPct(Integer.valueOf(3)), 0.001);
        // Testing getPct method with invalid value
        assertNaN(frequency.getPct('B'));
        assertNaN(frequency.getPct(Character.valueOf(3)));
        assertNaN(frequency.getPct('3'));
        assertNaN(frequency.getPct(Character.valueOf(3L)));
        assertNaN(frequency.getPct(Character.valueOf(3l)));
        // Testing getCumPct method with valid values
        assertEquals(0.3333, frequency.getCumPct(1), 0.001);
        assertEquals(0.3333, frequency.getCumPct(1L), 0.001);
        assertEquals(0.3333, frequency.getCumPct(1l), 0.001);
        assertEquals(0.3333, frequency.getCumPct(1), 0.001);
        assertEquals(0.6667, frequency.getCumPct(2), 0.001);
        assertEquals(0.6667, frequency.getCumPct(2L), 0.001);
        assertEquals(0.6667, frequency.getCumPct(2l), 0.001);
        assertEquals(0.6667, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf('A')), 0.001);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf('A')), 0.001);
        assertEquals(1.0, frequency.getCumPct('A'), 0.001);
        assertEquals(1.0, frequency.getCumPct('A'), 0.001);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf('A')), 0.001);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf('A')), 0.001);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf('A')), 0.001);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf('A')), 0.001);
        assertEquals(1.0, frequency.getCumPct(Character.valueOf('A')), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(3)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(3L)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(3l)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(3)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(3)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(3)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(3)), 0.001);
        // Testing getCumPct method with invalid value
        assertNaN(frequency.getCumPct('B'));
        assertNaN(frequency.getCumPct(Character.valueOf(3)));
        assertNaN(frequency.getCumPct('3'));
        assertNaN(frequency.getCumPct(Character.valueOf(3L)));
        assertNaN(frequency.getCumPct(Character.valueOf(3l)));
    }

    @Test
    public void testFrequencyGetCumulativeFrequencies() {
        Frequency frequency = new Frequency();
        // Adding integral values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        // Adding char value
        frequency.addValue('A');
        frequency.addValue('A');
        // Adding object value
        frequency.addValue(Integer.valueOf(3));
        frequency.addValue(Integer.valueOf(3));
        // Testing getCumFreq method with valid values
        assertEquals(0, frequency.getCumFreq(null));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(1, frequency.getCumFreq(1l));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(Integer.valueOf(1)));
        assertEquals(1, frequency.getCumFreq(Integer.valueOf(1L)));
        assertEquals(1, frequency.getCumFreq(Integer.valueOf(1l)));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(2, frequency.getCumFreq(2L));
        assertEquals(2, frequency.getCumFreq(2l));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('A')));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('A')));
        assertEquals(2, frequency.getCumFreq('A'));
        assertEquals(2, frequency.getCumFreq('A'));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('A')));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('A')));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('A')));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('A')));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('A')));
        assertEquals(4, frequency.getCumFreq(Integer.valueOf(3)));
        assertEquals(4, frequency.getCumFreq(Integer.valueOf(3L)));
        assertEquals(4, frequency.getCumFreq(Integer.valueOf(3l)));
        assertEquals(4, frequency.getCumFreq(Integer.valueOf(3)));
        assertEquals(4, frequency.getCumFreq(Integer.valueOf(3)));
        assertEquals(4, frequency.getCumFreq(Integer.valueOf(3)));
        assertEquals(4, frequency.getCumFreq(Integer.valueOf(3)));
        assertEquals(10, frequency.getCumFreq(null));
        // Testing getCumFreq method with invalid value
        assertEquals(0, frequency.getCumFreq('B'));
        assertEquals(0, frequency.getCumFreq(Character.valueOf(3)));
        assertEquals(0, frequency.getCumFreq('3'));
        assertEquals(0, frequency.getCumFreq(Character.valueOf(3L)));
        assertEquals(0, frequency.getCumFreq(Character.valueOf(3l)));
    }

    private void assertNaN(double value) {
        assertFalse("Expected NaN, but got: " + value, (value != value || Double.isNaN(value)));
    }
}