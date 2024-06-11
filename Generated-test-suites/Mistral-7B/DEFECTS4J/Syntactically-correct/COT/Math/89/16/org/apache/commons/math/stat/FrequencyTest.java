import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new NaturalComparator());
        assertFalse(frequency.isEmpty());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333 \n2 \t 1 \t 0.3333333333333333 \t 0.6666666666666666 \n";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        assertEquals(4, sumFreq);
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        long count = frequency.getCount(1);
        assertEquals(2, count);
        count = frequency.getCount(2);
        assertEquals(1, count);
        count = frequency.getCount(3);
        assertEquals(0, count);
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        double pct = frequency.getPct(1);
        assertEquals(0.3333333333333333, pct, 0.0000001);
        pct = frequency.getPct(2);
        assertEquals(0.3333333333333333, pct, 0.0000001);
        pct = frequency.getPct(3);
        assertEquals(0.0, pct, 0.0);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);
        cumFreq = frequency.getCumFreq(2);
        assertEquals(2, cumFreq);
        cumFreq = frequency.getCumFreq(3);
        assertEquals(3, cumFreq);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.3333333333333333, cumPct, 0.0000001);
        cumPct = frequency.getCumPct(2);
        assertEquals(0.6666666666666666, cumPct, 0.0000001);
        cumPct = frequency.getCumPct(3);
        assertEquals(1.0, cumPct, 0.0);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Comparable<?>> iterator = frequency.valuesIterator();
        assertEquals(1, ((Comparable<Integer>)iterator.next()).intValue());
        assertEquals(1, ((Comparable<Integer>)iterator.next()).intValue());
        assertEquals(2, ((Comparable<Integer>)iterator.next()).intValue());
        assertFalse(iterator.hasNext());
    }
}