package org.apache.commons.math.stat;
import org.apache.commons.math.stat.Frequency;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @Test
    public void testAddValueInteger() {
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueLong() {
        frequency.addValue(2L);
        long count = frequency.getCount(2L);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueString() {
        frequency.addValue("hello");
        long count = frequency.getCount("hello");
        assertEquals(1, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueSameValueMultipleTimes() {
        for (int i = 0; i < 10; i++) {
            frequency.addValue(1);
        }
        long count = frequency.getCount(1);
        assertEquals(10, count);
    }

    @Test
    public void testClear() {
        frequency.addValue(1);
        frequency.clear();
        long count = frequency.getCount(1);
        assertEquals(0, count);
    }

    @Test
    public void testGetSumFreq() {
        for (int i = 0; i < 10; i++) {
            frequency.addValue(1);
        }
        long sumFreq = frequency.getSumFreq();
        assertEquals(10, sumFreq);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCountNonComparable() {
        frequency.addValue(1);
        frequency.addValue(new Object());
        long count = frequency.getCount(new Object());
    }

    @Test
    public void testGetCountValueNotPresent() {
        long count = frequency.getCount(2L);
        assertEquals(0, count);
    }

    @Test
    public void testGetPct() {
        for (int i = 0; i < 10; i++) {
            frequency.addValue(1);
        }
        double pct = frequency.getPct(1);
        assertEquals(0.1, pct, 0.01);
    }

    @Test
    public void testGetPctZeroValues() {
        double pct = frequency.getPct(1);
        assertEquals(Double.NaN, pct);
    }

    @Test
    public void testGetCumFreq() {
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);

        frequency.addValue(2);
        cumFreq = frequency.getCumFreq(1);
        assertEquals(2, cumFreq);

        cumFreq = frequency.getCumFreq(2);
        assertEquals(2, cumFreq);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCumFreqNonComparable() {
        frequency.addValue(1);
        frequency.addValue(new Object());
        long cumFreq = frequency.getCumFreq(new Object());
    }

    @Test
    public void testGetCumFreqValueNotPresent() {
        long cumFreq = frequency.getCumFreq(2);
        assertEquals(0, cumFreq);
    }

    @Test
    public void testGetCumPct() {
        for (int i = 0; i < 10; i++) {
            frequency.addValue(1);
        }
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.1, cumPct, 0.01);
    }

    @Test
    public void testGetCumPctZeroValues() {
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.0, cumPct, 0.01);
    }

    @Test
    public void testGetCumPctAllValues() {
        for (int i = 0; i < 100; i++) {
            frequency.addValue(1);
        }
        double cumPct = frequency.getCumPct(1);
        assertEquals(1.0, cumPct, 0.01);
    }

    @After
    public void tearDown() {
        frequency = null;
    }
}