import static org.apache.commons.math3.stat.Frequency.NaturalComparator;
import static org.apache.commons.math3.stat.Frequency.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        Iterator values = frequency.valuesIterator();
        assertFalse(values.hasNext());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertEquals(0, frequency.getSumFreq());
        Iterator values = frequency.valuesIterator();
        assertFalse(values.hasNext());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333 \n" +
                "2 \t 1 \t 0.1666666666666667 \t 0.5 \n";
        assertEquals(expected, result);
    }

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        Object value = new Integer(1);
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
        assertEquals(1, frequency.getSumFreq());
        Iterator values = frequency.valuesIterator();
        assertEquals(value, values.next());

        frequency.addValue(value);
        assertEquals(2, frequency.getCount(value));
        assertEquals(2, frequency.getSumFreq());

        frequency.addValue(new Double(2.5));
        assertEquals(2, frequency.getCount(value));
        assertEquals(3, frequency.getSumFreq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCount(1));
        assertEquals(3, frequency.getSumFreq());
        Iterator values = frequency.valuesIterator();
        assertEquals(1, values.next().longValue());
        assertEquals(1, values.next().longValue());
        assertEquals(2, values.next().longValue());
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
        assertEquals(2, frequency.getSumFreq());
        Iterator values = frequency.valuesIterator();
        assertEquals(Character.valueOf('a'), values.next());
        assertEquals(Character.valueOf('a'), values.next());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        Iterator values = frequency.valuesIterator();
        assertFalse(values.hasNext());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator values = frequency.valuesIterator();
        assertEquals(1, values.next().longValue());
        assertEquals(1, values.next().longValue());
        assertEquals(2, values.next().longValue());
        assertFalse(values.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(6, frequency.getSumFreq());
    }

    @Test
    public void testGetCountObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertNull(frequency.getCount(3));
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertNull(frequency.getCount(3));
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(2, frequency.getCount('a'));
        assertEquals(1, frequency.getCount('b'));
        assertNull(frequency.getCount('c'));
    }

    @Test
    public void testGetPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.3333333333333333, frequency.getPct(1), 0.00001);
        assertEquals(0.1666666666666667, frequency.getPct(2), 0.00001);
        assertNull(frequency.getPct(3));
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.3333333333333333, frequency.getPct(1), 0.00001);
        assertEquals(0.1666666666666667, frequency.getPct(2), 0.00001);
        assertNull(frequency.getPct(3));
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0.4, frequency.getPct('a'), 0.00001);
        assertEquals(0.1666666666666667, frequency.getPct('b'), 0.00001);
        assertNull(frequency.getPct('c'));
    }

    @Test
    public void testGetCumFreqObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0, frequency.getCumFreq(null));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
        assertFalse(frequency.getCumFreq(4) > 3);
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0, frequency.getCumFreq(null));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
        assertFalse(frequency.getCumFreq(4) > 3);
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0, frequency.getCumFreq(null));
        assertEquals(1, frequency.getCumFreq('a'));
        assertEquals(2, frequency.getCumFreq('a'));
        assertEquals(3, frequency.getCumFreq('b'));
        assertFalse(frequency.getCumFreq('c') > 3);
    }

    @Test
    public void testGetCumPctObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0, frequency.getCumPct(null), 0.00001);
        assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.00001);
        assertEquals(0.5, frequency.getCumPct(2), 0.00001);
        assertNull(frequency.getCumPct(3));
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0, frequency.getCumPct(null), 0.00001);
        assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.00001);
        assertEquals(0.5, frequency.getCumPct(2), 0.00001);
        assertNull(frequency.getCumPct(3));
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(0, frequency.getCumPct(null), 0.00001);
        assertEquals(0.4, frequency.getCumPct('a'), 0.00001);
        assertEquals(0.5, frequency.getCumPct('b'), 0.00001);
        assertNull(frequency.getCumPct('c'));
    }
}