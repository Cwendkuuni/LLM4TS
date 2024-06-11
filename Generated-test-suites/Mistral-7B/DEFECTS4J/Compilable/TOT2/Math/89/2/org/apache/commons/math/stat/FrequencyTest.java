package org.apache.commons.math.stat;
import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequencyAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        assertEquals(4, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(0, frequency.getCount(3));

        assertEquals(0.75, frequency.getPct(1), 0.01);
        assertEquals(0.25, frequency.getPct(2), 0.01);

        assertEquals(0.5, frequency.getCumPct(1), 0.01);
        assertEquals(0.625, frequency.getCumPct(2), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyAddNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }
}