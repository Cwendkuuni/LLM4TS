package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        // test methods that do not require a comparator
        assertEquals(6, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(1L));
        assertEquals(3, frequency.getCount(1.0));
        assertEquals(3, frequency.getCount(1.0f));
        assertEquals(3, frequency.getCount(Character.valueOf('1')));
        assertEquals(3, frequency.getCount(Character.valueOf('1')));
        assertEquals(3, frequency.getCount('1'));

        assertEquals(1.0 / 6.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 6.0, frequency.getPct(1L), 0.001);
        assertEquals(1.0 / 6.0, frequency.getPct(1.0), 0.001);
        assertEquals(1.0 / 6.0, frequency.getPct(1.0f), 0.001);
        assertEquals(1.0 / 6.0, frequency.getPct(Character.valueOf('1')), 0.001);
        assertEquals(1.0 / 6.0, frequency.getPct(Character.valueOf('1')), 0.001);
        assertEquals(1.0 / 6.0, frequency.getPct('1'), 0.001);

        assertEquals(1.0 / 6.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(1L), 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(1.0), 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(1.0f), 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(Character.valueOf('1')), 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(Character.valueOf('1')), 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct('1'), 0.001);

        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(1, frequency.getCumFreq(Character.valueOf('1')));
        assertEquals(1, frequency.getCumFreq('1'));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(2, frequency.getCumFreq(2L));
        assertEquals(2, frequency.getCumFreq(Character.valueOf('2')));
        assertEquals(2, frequency.getCumFreq('2'));
        assertEquals(3, frequency.getCumFreq(3));
        assertEquals(3, frequency.getCumFreq(3L));
        assertEquals(3, frequency.getCumFreq(Character.valueOf('3')));
        assertEquals(3, frequency.getCumFreq('3'));
        assertEquals(5, frequency.getCumFreq(4));
        assertEquals(5, frequency.getCumFreq(4L));
        assertEquals(5, frequency.getCumFreq(Character.valueOf('4')));
        assertEquals(5, frequency.getCumFreq('4'));
        assertEquals(6, frequency.getCumFreq(5));
        assertEquals(6, frequency.getCumFreq(5L));
        assertEquals(6, frequency.getCumFreq(Character.valueOf('5')));
        assertEquals(6, frequency.getCumFreq('5'));

        // test methods that require a comparator
        Frequency frequencyComparator = new Frequency(new Comparator<Comparable<?>>() {
            @Override
            public int compare(Comparable<?> o1, Comparable<?> o2) {
                return o1.compareTo(o2);
            }
        });
        frequencyComparator.addValue(1);
        frequencyComparator.addValue(1);
        frequencyComparator.addValue(2);
        frequencyComparator.addValue(2);
        frequencyComparator.addValue(3);
        frequencyComparator.addValue(3);

        assertEquals(6, frequencyComparator.getSumFreq());
        assertEquals(3, frequencyComparator.getCount(1));
        assertEquals(3, frequencyComparator.getCount(1L));
        assertEquals(3, frequencyComparator.getCount(1.0));
        assertEquals(3, frequencyComparator.getCount(1.0f));
        assertEquals(3, frequencyComparator.getCount(Character.valueOf('1')));
        assertEquals(3, frequencyComparator.getCount(Character.valueOf('1')));
        assertEquals(3, frequencyComparator.getCount('1'));

        assertEquals(1.0 / 6.0, frequencyComparator.getPct(1), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getPct(1L), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getPct(1.0), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getPct(1.0f), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getPct(Character.valueOf('1')), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getPct(Character.valueOf('1')), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getPct('1'), 0.001);

        assertEquals(1.0 / 6.0, frequencyComparator.getCumPct(1), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getCumPct(1L), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getCumPct(1.0), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getCumPct(1.0f), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getCumPct(Character.valueOf('1')), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getCumPct(Character.valueOf('1')), 0.001);
        assertEquals(1.0 / 6.0, frequencyComparator.getCumPct('1'), 0.001);

        assertEquals(1, frequencyComparator.getCumFreq(1));
        assertEquals(1, frequencyComparator.getCumFreq(1L));
        assertEquals(1, frequencyComparator.getCumFreq(Character.valueOf('1')));
        assertEquals(1, frequencyComparator.getCumFreq('1'));
        assertEquals(2, frequencyComparator.getCumFreq(2));
        assertEquals(2, frequencyComparator.getCumFreq(2L));
        assertEquals(2, frequencyComparator.getCumFreq(Character.valueOf('2')));
        assertEquals(2, frequencyComparator.getCumFreq('2'));
        assertEquals(3, frequencyComparator.getCumFreq(3));
        assertEquals(3, frequencyComparator.getCumFreq(3L));
        assertEquals(3, frequencyComparator.getCumFreq(Character.valueOf('3')));
        assertEquals(3, frequencyComparator.getCumFreq('3'));
        assertEquals(5, frequencyComparator.getCumFreq(4));
        assertEquals(5, frequencyComparator.getCumFreq(4L));
        assertEquals(5, frequencyComparator.getCumFreq(Character.valueOf('4')));
        assertEquals(5, frequencyComparator.getCumFreq('4'));
        assertEquals(6, frequencyComparator.getCumFreq(5));
        assertEquals(6, frequencyComparator.getCumFreq(5L));
        assertEquals(6, frequencyComparator.getCumFreq(Character.valueOf('5')));
        assertEquals(6, frequencyComparator.getCumFreq('5'));
    }
}