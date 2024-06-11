import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new NaturalComparator());
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.25 \t 0.5833333333333333\n", frequency.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueFailedComparison() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(1);
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(1L);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1L));
    }

    @Test
    public void testAddValueDouble() {
        Frequency frequency = new Frequency();
        frequency.addValue(1.0);
        frequency.addValue(1.0);
        frequency.addValue(1.0);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1.0));
    }

    @Test
    public void testAddValueCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('a');
        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(1L);
        assertEquals(1, frequency.getCount(1L));
    }

    @Test
    public void testGetCountCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void testGetCountNull() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCount(null));
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.3333333333333333, frequency.getPct(1), 0.0001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        assertEquals(0.3333333333333333, frequency.getPct(1L), 0.0001);
    }

    @Test
    public void testGetPctCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('a');
        assertEquals(0.3333333333333333, frequency.getPct('a'), 0.0001);
    }

    @Test
    public void testGetPctNull() {
        Frequency frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getPct(null));
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        assertEquals(1, frequency.getCumFreq(1L));
        assertEquals(2, frequency.getCumFreq(2L));
        assertEquals(3, frequency.getCumFreq(3L));
    }

    @Test
    public void testGetCumFreqCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('a');
        assertEquals(1, frequency.getCumFreq('a'));
        assertEquals(2, frequency.getCumFreq('b'));
        assertEquals(3, frequency.getCumFreq('c'));
    }

    @Test
    public void testGetCumFreqNull() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getCumFreq(null));
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(0.3333333333333333, frequency.getPct(1), 0.0001);
        assertEquals(0.6666666666666667, frequency.getPct(2), 0.0001);
        assertEquals(1.0, frequency.getPct(3), 0.0001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        assertEquals(0.3333333333333333, frequency.getPct(1L), 0.0001);
        assertEquals(0.6666666666666667, frequency.getPct(2L), 0.0001);
        assertEquals(1.0, frequency.getPct(3L), 0.0001);
    }

    @Test
    public void testGetCumPctCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('a');
        assertEquals(0.3333333333333333, frequency.getPct('a'), 0.0001);
        assertEquals(0.6666666666666667, frequency.getPct('b'), 0.0001);
        assertEquals(1.0, frequency.getPct('c'), 0.0001);
    }

    @Test
    public void testGetCumPctNull() {
        Frequency frequency = new Frequency();
        assertEquals(Double.NaN, frequency.getPct(null));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator values = frequency.valuesIterator();
        assertTrue(values.hasNext());
        assertEquals(1, values.next().longValue());
        assertTrue(values.hasNext());
        assertEquals(1, values.next().longValue());
        assertTrue(values.hasNext());
        assertEquals(2, values.next().longValue());
        assertFalse(values.hasNext());
    }
}