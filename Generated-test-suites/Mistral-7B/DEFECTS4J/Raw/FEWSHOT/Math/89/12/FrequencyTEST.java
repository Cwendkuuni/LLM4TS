package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyAddValues() {
        Frequency frequency = new Frequency();

        // Test adding integers
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);

        long count1 = frequency.getCount(1);
        long count2 = frequency.getCount(2);
        long count3 = frequency.getCount(3);

        assertEquals(2, count1);
        assertEquals(2, count2);
        assertEquals(1, count3);

        // Test adding longs
        Frequency frequencyLong = new Frequency();

        frequencyLong.addValue(1L);
        frequencyLong.addValue(1L);
        frequencyLong.addValue(2L);
        frequencyLong.addValue(2L);
        frequencyLong.addValue(3L);

        long count1Long = frequencyLong.getCount(1L);
        long count2Long = frequencyLong.getCount(2L);
        long count3Long = frequencyLong.getCount(3L);

        assertEquals(2, count1Long);
        assertEquals(2, count2Long);
        assertEquals(1, count3Long);

        // Test adding characters
        Frequency frequencyCharacter = new Frequency();

        frequencyCharacter.addValue('a');
        frequencyCharacter.addValue('a');
        frequencyCharacter.addValue('b');
        frequencyCharacter.addValue('b');
        frequencyCharacter.addValue('c');

        long countA = frequencyCharacter.getCount('a');
        long countB = frequencyCharacter.getCount('b');
        long countC = frequencyCharacter.getCount('c');

        assertEquals(2, countA);
        assertEquals(2, countB);
        assertEquals(1, countC);

        // Test adding objects
        Frequency frequencyObject = new Frequency();

        frequencyObject.addValue(1);
        frequencyObject.addValue(1);
        frequencyObject.addValue(2);
        frequencyObject.addValue(2);
        frequencyObject.addValue(3);

        Object key1 = 1;
        Object key2 = 2;
        Object key3 = 3;

        int compareResult1 = frequencyObject.compare(key1, key1);
        int compareResult12 = frequencyObject.compare(key1, key2);
        int compareResult21 = frequencyObject.compare(key2, key1);
        int compareResult22 = frequencyObject.compare(key2, key2);
        int compareResult13 = frequencyObject.compare(key1, key3);
        int compareResult31 = frequencyObject.compare(key3, key1);

        assertEquals(0, compareResult1);
        assertEquals(-1, compareResult12);
        assertEquals(1, compareResult12);
        assertEquals(0, compareResult22);
        assertEquals(-1, compareResult13);
        assertEquals(1, compareResult31);

        long count1Object = frequencyObject.getCount(key1);
        long count2Object = frequencyObject.getCount(key2);
        long count3Object = frequencyObject.getCount(key3);

        assertEquals(2, count1Object);
        assertEquals(2, count2Object);
        assertEquals(1, count3Object);
    }

    @Test
    public void testFrequencyComparator() {
        Comparator<Object> comparator = new Frequency.NaturalComparator();

        int compareResult12 = comparator.compare(1, 2);
        int compareResult21 = comparator.compare(2, 1);

        assertEquals(-1, compareResult12);
        assertEquals(1, compareResult21);
    }

    @Test
    public void testFrequencyToString() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);

        String toString = frequency.toString();

        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333 \n2 \t 2 \t 0.3333333333333333 \t 0.6666666666666666 \n3 \t 1 \t 0.1666666666666667 \t 0.7333333333333333 \n";

        assertEquals(expected, toString);
    }
}