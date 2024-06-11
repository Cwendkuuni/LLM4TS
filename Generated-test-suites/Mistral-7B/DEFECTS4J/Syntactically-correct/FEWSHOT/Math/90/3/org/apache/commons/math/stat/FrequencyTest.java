package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertNotNull(frequency.freqTable);
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertEquals(0, frequency.getSumFreq());
        assertNotNull(frequency.freqTable);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        String expectedOutput = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 3 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 3 \t 0.3333333333333333 \t 0.6666666666666667\n";
        assertEquals(expectedOutput, frequency.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        long sumFreq = frequency.getSumFreq();
        assertEquals(9, sumFreq);
        Iterator valuesIterator = frequency.valuesIterator();
        List<Object> values = new ArrayList<>();
        while (valuesIterator.hasNext()) {
            values.add(valuesIterator.next());
        }
        List<Long> expectedCounts = Arrays.asList(new Long[]{3L, 3L, 3L, 1L, 1L, 1L, 1L, 1L, 1L});
        List<Object> expectedValues = Arrays.asList(new Object[]{1, 1, 1, 2, 2, 3, 3, 3, 3});
        assertEquals(expectedCounts, frequency.getValuesAndTheirFrequencies(values));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        long sumFreq = frequency.getSumFreq();
        assertEquals(9, sumFreq);
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(2));
        assertEquals(3, frequency.getCount(3));
        assertEquals(0, frequency.getCount('a'));
    }

    @Test
    public void testGetCountInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(2));
        assertEquals(3, frequency.getCount(3));
        assertEquals(0, frequency.getCount(4));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        assertEquals(3, frequency.getCount((long) 1));
        assertEquals(3, frequency.getCount((long) 2));
        assertEquals(3, frequency.getCount((long) 3));
        assertEquals(0, frequency.getCount((long) 4));
    }

    @Test
    public void testGetCountCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');
        assertEquals(2, frequency.getCount('a'));
        assertEquals(2, frequency.getCount('b'));
        assertEquals(0, frequency.getCount('c'));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        double pct1 = frequency.getPct((double) 1);
        double pct2 = frequency.getPct((double) 2);
        double pct3 = frequency.getPct((double) 3);
        assertEquals(0.3333333333333333, pct1, 0.0000001);
        assertEquals(0.3333333333333333, pct2, 0.0000001);
        assertEquals(0.3333333333333333, pct3, 0.0000001);
    }

    @Test
    public void testGetPctInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        double pct1 = frequency.getPct(1);
        double pct2 = frequency.getPct(2);
        double pct3 = frequency.getPct(3);
        assertEquals(0.3333333333333333, pct1, 0.0000001);
        assertEquals(0.3333333333333333, pct2, 0.0000001);
        assertEquals(0.3333333333333333, pct3, 0.0000001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        double pct1 = frequency.getPct((long) 1);
        double pct2 = frequency.getPct((long) 2);
        double pct3 = frequency.getPct((long) 3);
        assertEquals(0.3333333333333333, pct1, 0.0000001);
        assertEquals(0.3333333333333333, pct2, 0.0000001);
        assertEquals(0.3333333333333333, pct3, 0.0000001);
    }

    @Test
    public void testGetPctCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');
        double pctA = frequency.getPct('a');
        double pctB = frequency.getPct('b');
        assertEquals(0.5, pctA, 0.0000001);
        assertEquals(0.5, pctB, 0.0000001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        long cumFreq1 = frequency.getCumFreq((double) 1);
        long cumFreq2 = frequency.getCumFreq((double) 2);
        long cumFreq3 = frequency.getCumFreq((double) 3);
        assertEquals(1, cumFreq1);
        assertEquals(2, cumFreq2);
        assertEquals(5, cumFreq3);
    }

    @Test
    public void testGetCumFreqInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        long cumFreq1 = frequency.getCumFreq(1);
        long cumFreq2 = frequency.getCumFreq(2);
        long cumFreq3 = frequency.getCumFreq(3);
        assertEquals(1, cumFreq1);
        assertEquals(2, cumFreq2);
        assertEquals(5, cumFreq3);
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        long cumFreq1 = frequency.getCumFreq((long) 1);
        long cumFreq2 = frequency.getCumFreq((long) 2);
        long cumFreq3 = frequency.getCumFreq((long) 3);
        assertEquals(1, cumFreq1);
        assertEquals(2, cumFreq2);
        assertEquals(5, cumFreq3);
    }

    @Test
    public void testGetCumFreqCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');
        long cumFreqA = frequency.getCumFreq('a');
        long cumFreqB = frequency.getCumFreq('b');
        assertEquals(1, cumFreqA);
        assertEquals(2, cumFreqB);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        double cumPct1 = frequency.getCumPct((double) 1);
        double cumPct2 = frequency.getCumPct((double) 2);
        double cumPct3 = frequency.getCumPct((double) 3);
        assertEquals(0.3333333333333333, cumPct1, 0.0000001);
        assertEquals(0.6666666666666667, cumPct2, 0.0000001);
        assertEquals(1, cumPct3, 0.0000001);
    }

    @Test
    public void testGetCumPctInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        double cumPct1 = frequency.getCumPct(1);
        double cumPct2 = frequency.getCumPct(2);
        double cumPct3 = frequency.getCumPct(3);
        assertEquals(0.3333333333333333, cumPct1, 0.0000001);
        assertEquals(0.6666666666666667, cumPct2, 0.0000001);
        assertEquals(1, cumPct3, 0.0000001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        double cumPct1 = frequency.getCumPct((long) 1);
        double cumPct2 = frequency.getCumPct((long) 2);
        double cumPct3 = frequency.getCumPct((long) 3);
        assertEquals(0.3333333333333333, cumPct1, 0.0000001);
        assertEquals(0.6666666666666667, cumPct2, 0.0000001);
        assertEquals(1, cumPct3, 0.0000001);
    }

    @Test
    public void testGetCumPctCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');
        double cumPctA = frequency.getCumPct('a');
        double cumPctB = frequency.getCumPct('b');
        assertEquals(0.5, cumPctA, 0.0000001);
        assertEquals(1, cumPctB, 0.0000001);
    }
}