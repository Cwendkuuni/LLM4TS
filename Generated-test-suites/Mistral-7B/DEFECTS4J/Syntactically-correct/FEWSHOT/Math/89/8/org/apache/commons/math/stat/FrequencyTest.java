package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);

        assertEquals(4, freq.getSumFreq());

        assertEquals(2, freq.getCount(1));
        assertEquals(2, freq.getCount(1l));
        assertEquals(2, freq.getCount(1.0));
        assertEquals(2, freq.getCount(1.0f));

        assertEquals(2, freq.getCount(2));
        assertEquals(2, freq.getCount(2l));
        assertEquals(2, freq.getCount(2.0));
        assertEquals(2, freq.getCount(2.0f));

        assertEquals(0, freq.getCount('a'));

        assertEquals(0.5, freq.getPct(1), 0.001);
        assertEquals(0.5, freq.getPct(1.0), 0.001);
        assertEquals(0.5, freq.getPct(1.0f), 0.001);

        assertEquals(0.5, freq.getPct(2), 0.001);
        assertEquals(0.5, freq.getPct(2.0), 0.001);
        assertEquals(0.5, freq.getPct(2.0f), 0.001);

        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(1.0), 0.001);
        assertEquals(1.0, freq.getCumPct(1.0f), 0.001);

        assertEquals(2.0 / 4.0, freq.getCumPct(2), 0.001);
        assertEquals(2.0 / 4.0, freq.getCumPct(2.0), 0.001);
        assertEquals(2.0 / 4.0, freq.getCumPct(2.0f), 0.001);

        assertEquals(3.0 / 4.0, freq.getCumPct(3), 0.001);
        assertEquals(3.0 / 4.0, freq.getCumPct(3.0), 0.001);
        assertEquals(3.0 / 4.0, freq.getCumPct(3.0f), 0.001);

        assertEquals(4.0 / 4.0, freq.getCumPct(4), 0.001);
        assertEquals(4.0 / 4.0, freq.getCumPct(4.0), 0.001);
        assertEquals(4.0 / 4.0, freq.getCumPct(4.0f), 0.001);

        Frequency freqComparator = new Frequency(new NaturalComparator());
        freqComparator.addValue(2);
        freqComparator.addValue(1);

        assertEquals(2, freqComparator.getCount(1));
        assertEquals(1, freqComparator.getCount(2));

        assertEquals(1.0, freqComparator.getCumPct(1), 0.001);
        assertEquals(0.5, freqComparator.getCumPct(2), 0.001);
    }
}