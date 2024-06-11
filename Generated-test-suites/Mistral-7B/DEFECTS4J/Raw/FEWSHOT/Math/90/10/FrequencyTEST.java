package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        TreeMap freqTable = frequency.freqTable;
        assertTrue(freqTable.isEmpty());
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        TreeMap freqTable = frequency.freqTable;
        assertTrue(freqTable.isEmpty());
    }

    @Test
    public void testAddValueInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        long count = frequency.getCount(5);
        assertEquals(1, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        long count = frequency.getCount(1L);
        assertEquals(1, count);
    }

    @Test
    public void testAddValueString() {
        Frequency frequency = new Frequency();
        frequency.addValue("five");
        long count = frequency.getCount("five");
        assertEquals(0, count);
    }

    @Test
    public void testAddValueCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('e');
        long count = frequency.getCount('e');
        assertEquals(1, count);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(5);
        frequency.clear();
        long count = frequency.getCount(5);
        assertEquals(0, count);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        Iterator iterator = frequency.valuesIterator();
        long expectedSumFreq = 4;
        long sumFreq = 0;
        while (iterator.hasNext()) {
            long value = (Long) iterator.next();
            assertEquals(value, sumFreq++);
        }
        assertEquals(expectedSumFreq, sumFreq);
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        Iterator iterator = frequency.valuesIterator();
        while (iterator.hasNext()) {
            frequency.addValue((Long) iterator.next());
        }
        long expectedSumFreq = 4;
        long sumFreq = frequency.getSumFreq();
        assertEquals(expectedSumFreq, sumFreq);
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(1, count);

        frequency.addValue(1);
        count = frequency.getCount(1);
        assertEquals(2, count);
    }

    @Test
    public void testGetCountNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(new Object());
        long count = frequency.getCount(new Object());
        assertEquals(0, count);
    }

    @Test
    public void testGetCountNonExistentValue() {
        Frequency frequency = new Frequency();
        long count = frequency.getCount(5);
        assertEquals(0, count);
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        double pct = frequency.getPct(1);
        double expectedPct = 0.45;
        assertEquals(expectedPct, pct, 0.001);
    }

    @Test
    public void testGetPctNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(new Object());

        double pct = frequency.getPct(new Object());
        double expectedPct = 0.0;
        assertEquals(expectedPct, pct, 0.001);
    }

    @Test
    public void testGetPctNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        double pct = frequency.getPct(5);
        double expectedPct = 0.0;
        assertEquals(expectedPct, pct, 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        long cumFreq = frequency.getCumFreq(1);
        long expectedCumFreq = 1;
        assertEquals(expectedCumFreq, cumFreq);

        cumFreq = frequency.getCumFreq(2);
        expectedCumFreq = 3;
        assertEquals(expectedCumFreq, cumFreq);

        cumFreq = frequency.getCumFreq(5);
        expectedCumFreq = 4;
        assertEquals(expectedCumFreq, cumFreq);
    }

    @Test
    public void testGetCumFreqNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(new Object());

        long cumFreq = frequency.getCumFreq(new Object());
        long expectedCumFreq = 0;
        assertEquals(expectedCumFreq, cumFreq);
    }

    @Test
    public void testGetCumFreqNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        long cumFreq = frequency.getCumFreq(5);
        long expectedCumFreq = 0;
        assertEquals(expectedCumFreq, cumFreq);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        double cumPct = frequency.getCumPct(1);
        double expectedCumPct = 0.45;
        assertEquals(expectedCumPct, cumPct, 0.001);

        cumPct = frequency.getCumPct(2);
        expectedCumPct = 0.6;
        assertEquals(expectedCumPct, cumPct, 0.001);

        cumPct = frequency.getCumPct(5);
        expectedCumPct = 1.0;
        assertEquals(expectedCumPct, cumPct, 0.001);

        cumPct = frequency.getCumPct(new Object());
        double expectedCumPct = 0.0;
        assertEquals(expectedCumPct, cumPct, 0.001);
    }

    @Test
    public void testGetCumPctNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(new Object());

        double cumPct = frequency.getCumPct(new Object());
        double expectedCumPct = 0.0;
        assertEquals(expectedCumPct, cumPct, 0.001);
    }

    @Test
    public void testGetCumPctNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        double cumPct = frequency.getCumPct(5);
        double expectedCumPct = 0.0;
        assertEquals(expectedCumPct, cumPct, 0.001);
    }
}