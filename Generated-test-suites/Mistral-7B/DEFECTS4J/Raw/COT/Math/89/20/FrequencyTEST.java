package org.apache.commons.math.stat.test;

import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertNotNull(frequency);
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        Assert.assertNotNull(frequency);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        String expected = "Value    Freq.     Pct.     Cum Pct.\n1       2       0.25       0.25\n2       2       0.25       0.5\n" +
                "Total    4       1.00       1.00";
        Assert.assertEquals(expected, frequency.toString());
    }

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue("test");
        long expectedSumFreq = 5L;
        long actualSumFreq = frequency.getSumFreq();
        Assert.assertEquals(expectedSumFreq, actualSumFreq);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        // Invoke the method that throws the exception
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableWithComparableObjects() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        // Invoke the method that throws the exception
        frequency.addValue(new Integer(2));
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long expectedSumFreq = 5L;
        long actualSumFreq = frequency.getSumFreq();
        Assert.assertEquals(expectedSumFreq, actualSumFreq);
    }

    @Test
    public void testGetCountObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long expectedCount = 2L;
        long actualCount = frequency.getCount(1);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long expectedCount = 2L;
        long actualCount = frequency.getCount(1);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long expectedCount = 2L;
        long actualCount = frequency.getCount(1L);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue((char) 1);
        frequency.addValue((char) 1);
        frequency.addValue((char) 2);
        frequency.addValue((char) 2);
        long expectedCount = 2L;
        long actualCount = frequency.getCount((char) 1);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetCountNonExistingValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long expectedCount = 0L;
        long actualCount = frequency.getCount("test");
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double expectedPct = 0.4;
        double actualPct = frequency.getPct(2);
        Assert.assertEquals(expectedPct, actualPct, 0.001);
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double expectedPct = 0.4;
        double actualPct = frequency.getPct(2);
        Assert.assertEquals(expectedPct, actualPct, 0.001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double expectedPct = 0.4;
        double actualPct = frequency.getPct(2L);
        Assert.assertEquals(expectedPct, actualPct, 0.001);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue((char) 1);
        frequency.addValue((char) 1);
        frequency.addValue((char) 2);
        frequency.addValue((char) 2);
        double expectedPct = 0.4;
        double actualPct = frequency.getPct((char) 2);
        Assert.assertEquals(expectedPct, actualPct, 0.001);
    }

    @Test
    public void testGetPctNonExistingValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double expectedPct = Double.NaN;
        double actualPct = frequency.getPct("test");
        Assert.assertEquals(expectedPct, actualPct, 0.001);
    }

    @Test
    public void testGetCumFreqObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long expectedCumFreq = 3L;
        long actualCumFreq = frequency.getCumFreq(1);
        Assert.assertEquals(expectedCumFreq, actualCumFreq);
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long expectedCumFreq = 3L;
        long actualCumFreq = frequency.getCumFreq(1);
        Assert.assertEquals(expectedCumFreq, actualCumFreq);
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long expectedCumFreq = 3L;
        long actualCumFreq = frequency.getCumFreq(1L);
        Assert.assertEquals(expectedCumFreq, actualCumFreq);
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue((char) 1);
        frequency.addValue((char) 1);
        frequency.addValue((char) 2);
        frequency.addValue((char) 2);
        long expectedCumFreq = 3L;
        long actualCumFreq = frequency.getCumFreq((char) 1);
        Assert.assertEquals(expectedCumFreq, actualCumFreq);
    }

    @Test
    public void testGetCumFreqNonExistingValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long expectedCumFreq = 0L;
        long actualCumFreq = frequency.getCumFreq("test");
        Assert.assertEquals(expectedCumFreq, actualCumFreq);
    }

    @Test
    public void testGetCumPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double expectedCumPct = 0.6;
        double actualCumPct = frequency.getCumPct(2);
        Assert.assertEquals(expectedCumPct, actualCumPct, 0.001);
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double expectedCumPct = 0.6;
        double actualCumPct = frequency.getCumPct(2);
        Assert.assertEquals(expectedCumPct, actualCumPct, 0.001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double expectedCumPct = 0.6;
        double actualCumPct = frequency.getCumPct(2L);
        Assert.assertEquals(expectedCumPct, actualCumPct, 0.001);
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue((char) 1);
        frequency.addValue((char) 1);
        frequency.addValue((char) 2);
        frequency.addValue((char) 2);
        double expectedCumPct = 0.6;
        double actualCumPct = frequency.getCumPct((char) 2);
        Assert.assertEquals(expectedCumPct, actualCumPct, 0.001);
    }

    @Test
    public void testGetCumPctNonExistingValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double expectedCumPct = Double.NaN;
        double actualCumPct = frequency.getCumPct("test");
        Assert.assertEquals(expectedCumPct, actualCumPct, 0.001);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        Iterator valuesIterator = frequency.valuesIterator();
        Object expectedValue = 1;
        Assert.assertTrue(valuesIterator.hasNext());
        Assert.assertEquals(expectedValue, valuesIterator.next());
        expectedValue = 1;
        Assert.assertTrue(valuesIterator.hasNext());
        Assert.assertEquals(expectedValue, valuesIterator.next());
        expectedValue = 2;
        Assert.assertTrue(valuesIterator.hasNext());
        Assert.assertEquals(expectedValue, valuesIterator.next());
        expectedValue = 2;
        Assert.assertTrue(valuesIterator.hasNext());
        Assert.assertEquals(expectedValue, valuesIterator.next());
        Assert.assertFalse(valuesIterator.hasNext());
    }

    
}