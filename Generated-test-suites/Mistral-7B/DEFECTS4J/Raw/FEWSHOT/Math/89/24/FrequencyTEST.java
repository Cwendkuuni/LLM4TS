package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        assertEquals(6, frequency.getSumFreq());

        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(2));
        assertEquals(3, frequency.getCount(3));

        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.5, frequency.getPct(3), 0.001);

        assertEquals(0.16666666666666664, frequency.getCumPct(1), 0.0001);
        assertEquals(0.3333333333333333, frequency.getCumPct(2), 0.0001);
        assertEquals(0.5, frequency.getCumPct(3), 0.0001);

        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
        assertEquals(3, frequency.getCumFreq(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue('a');
    }
}