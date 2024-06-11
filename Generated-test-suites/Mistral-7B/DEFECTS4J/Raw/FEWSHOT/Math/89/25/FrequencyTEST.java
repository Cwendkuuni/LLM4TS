package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

import junit.framework.JUnit4TestAdapter;

public class FrequencyTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FrequencyTest.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();
        // Test default constructor
        frequency.addValue("test1");
        frequency.addValue("test2");
        frequency.addValue("test1");
        frequency.addValue("test3");
        frequency.addValue("test2");

        // Test valuesIterator()
        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals("test1", iterator.next());
        assertEquals("test1", iterator.next());
        assertEquals("test2", iterator.next());
        assertEquals("test2", iterator.next());
        assertEquals("test3", iterator.next());
        assertNull(iterator.hasNext());

        // Test getSumFreq()
        long sumFreq = frequency.getSumFreq();
        assertEquals(5, sumFreq);

        // Test getCount()
        long countTest1 = frequency.getCount("test1");
        assertEquals(2, countTest1);
        long countTest2 = frequency.getCount("test2");
        assertEquals(2, countTest2);
        long countTest3 = frequency.getCount("test3");
        assertEquals(1, countTest3);

        // Test getPct()
        double pctTest1 = frequency.getPct("test1");
        assertEquals(0.4, pctTest1, 0.001);
        double pctTest2 = frequency.getPct("test2");
        assertEquals(0.4, pctTest2, 0.001);
        double pctTest3 = frequency.getPct("test3");
        assertEquals(0.2, pctTest3, 0.001);

        // Test getCumFreq()
        long cumFreqTest1 = frequency.getCumFreq("test1");
        assertEquals(2, cumFreqTest1);
        long cumFreqTest2 = frequency.getCumFreq("test2");
        assertEquals(4, cumFreqTest2);
        long cumFreqTest3 = frequency.getCumFreq("test3");
        assertEquals(5, cumFreqTest3);

        // Test getCumPct()
        double cumPctTest1 = frequency.getCumPct("test1");
        assertEquals(0.4, cumPctTest1, 0.001);
        double cumPctTest2 = frequency.getCumPct("test2");
        assertEquals(0.8, cumPctTest2, 0.001);
        double cumPctTest3 = frequency.getCumPct("test3");
        assertEquals(1.0, cumPctTest3, 0.001);

        // Test addValue() with different data types
        Frequency frequency2 = new Frequency();
        frequency2.addValue(1);
        frequency2.addValue(2);
        frequency2.addValue(1);
        frequency2.addValue(2);
        frequency2.addValue(3);
        frequency2.addValue(2);

        // Test valuesIterator()
        iterator = frequency2.valuesIterator();
        assertEquals(1, iterator.next().longValue());
        assertEquals(1, iterator.next().longValue());
        assertEquals(2, iterator.next().longValue());
        assertEquals(2, iterator.next().longValue());
        assertEquals(2, iterator.next().longValue());
        assertEquals(3, iterator.next().longValue());
        assertNull(iterator.hasNext());

        // Test getSumFreq()
        sumFreq = frequency2.getSumFreq();
        assertEquals(6, sumFreq);

        // Test getCount()
        countTest1 = frequency2.getCount(1);
        assertEquals(2, countTest1);
        countTest1 = frequency2.getCount(2);
        assertEquals(3, countTest1);
        countTest1 = frequency2.getCount(3);
        assertEquals(1, countTest1);

        // Test getPct()
        pctTest1 = frequency2.getPct(1);
        assertEquals(0.33, pctTest1, 0.001);
        pctTest1 = frequency2.getPct(2);
        assertEquals(0.5, pctTest1, 0.001);
        pctTest1 = frequency2.getPct(3);
        assertEquals(0.17, pctTest1, 0.001);

        // Test getCumFreq()
        cumFreqTest1 = frequency2.getCumFreq(1);
        assertEquals(2, cumFreqTest1);
        cumFreqTest1 = frequency2.getCumFreq(2);
        assertEquals(4, cumFreqTest1);
        cumFreqTest1 = frequency2.getCumFreq(3);
        assertEquals(5, cumFreqTest1);

        // Test getCumPct()
        cumPctTest1 = frequency2.getCumPct(1);
        assertEquals(0.33, cumPctTest1, 0.001);
        cumPctTest1 = frequency2.getCumPct(2);
        assertEquals(0.67, cumPctTest1, 0.001);
        cumPctTest1 = frequency2.getCumPct(3);
        assertEquals(1.0, cumPctTest1, 0.001);

        // Test addValue() with Character
        Frequency frequency3 = new Frequency();
        frequency3.addValue('a');
        frequency3.addValue('b');
        frequency3.addValue('a');
        frequency3.addValue('b');
        frequency3.addValue('c');
        frequency3.addValue('b');

        // Test valuesIterator()
        iterator = frequency3.valuesIterator();
        assertEquals('a', iterator.next());
        assertEquals('a', iterator.next());
        assertEquals('b', iterator.next());
        assertEquals('b', iterator.next());
        assertEquals('b', iterator.next());
        assertEquals('c', iterator.next());
        assertNull(iterator.hasNext());

        // Test getSumFreq()
        sumFreq = frequency3.getSumFreq();
        assertEquals(6, sumFreq);

        // Test getCount()
        countTest1 = frequency3.getCount('a');
        assertEquals(2, countTest1);
        countTest1 = frequency3.getCount('b');
        assertEquals(3, countTest1);
        countTest1 = frequency3.getCount('c');
        assertEquals(1, countTest1);

        // Test getPct()
        pctTest1 = frequency3.getPct('a');
        assertEquals(0.33, pctTest1, 0.001);
        pctTest1 = frequency3.getPct('b');
        assertEquals(0.5, pctTest1, 0.001);
        pctTest1 = frequency3.getPct('c');
        assertEquals(0.17, pctTest1, 0.001);

        // Test getCumFreq()
        cumFreqTest1 = frequency3.getCumFreq('a');
        assertEquals(2, cumFreqTest1);
        cumFreqTest1 = frequency3.getCumFreq('b');
        assertEquals(4, cumFreqTest1);
        cumFreqTest1 = frequency3.getCumFreq('c');
        assertEquals(5, cumFreqTest1);

        // Test getCumPct()
        cumPctTest1 = frequency3.getCumPct('a');
        assertEquals(0.33, cumPctTest1, 0.001);
        cumPctTest1 = frequency3.getCumPct('b');
        assertEquals(0.67, cumPctTest1, 0.001);
        cumPctTest1 = frequency3.getCumPct('c');
        assertEquals(1.0, cumPctTest1, 0.001);

        // Test addValue() with Comparator
        Frequency frequency4 = new Frequency(new NaturalComparator());
        frequency4.addValue("z");
        frequency4.addValue("a");
        frequency4.addValue("z");
        frequency4.addValue("b");
        frequency4.addValue("a");
        frequency4.addValue("c");
        frequency4.addValue("b");

        // Test valuesIterator()
        iterator = frequency4.valuesIterator();
        assertEquals("a", iterator.next());
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("z", iterator.next());
        assertEquals("z", iterator.next());
        assertNull(iterator.hasNext());

        // Test getSumFreq()
        sumFreq = frequency4.getSumFreq();
        assertEquals(8, sumFreq);

        // Test getCount()
        countTest1 = frequency4.getCount("a");
        assertEquals(2, countTest1);
        countTest1 = frequency4.getCount("b");
        assertEquals(2, countTest1);
        countTest1 = frequency4.getCount("c");
        assertEquals(1, countTest1);
        countTest1 = frequency4.getCount("z");
        assertEquals(2, countTest1);

        // Test getPct()
        pctTest1 = frequency4.getPct("a");
        assertEquals(0.25, pctTest1, 0.001);
        pctTest1 = frequency4.getPct("b");
        assertEquals(0.25, pctTest1, 0.001);
        pctTest1 = frequency4.getPct("c");
        assertEquals(0.125, pctTest1, 0.001);
        pctTest1 = frequency4.getPct("z");
        assertEquals(0.25, pctTest1, 0.001);

        // Test getCumFreq()
        cumFreqTest1 = frequency4.getCumFreq("a");
        assertEquals(2, cumFreqTest1);
        cumFreqTest1 = frequency4.getCumFreq("b");
        assertEquals(4, cumFreqTest1);
        cumFreqTest1 = frequency4.getCumFreq("c");
        assertEquals(5, cumFreqTest1);
        cumFreqTest1 = frequency4.getCumFreq("z");
        assertEquals(7, cumFreqTest1);

        // Test getCumPct()
        cumPctTest1 = frequency4.getCumPct("a");
        assertEquals(0.25, cumPctTest1, 0.001);
        cumPctTest1 = frequency4.getCumPct("b");
        assertEquals(0.5, cumPctTest1, 0.001);
        cumPctTest1 = frequency4.getCumPct("c");
        assertEquals(0.625, cumPctTest1, 0.001);
        cumPctTest1 = frequency4.getCumPct("z");
        assertEquals(1.0, cumPctTest1, 0.001);
    }
}