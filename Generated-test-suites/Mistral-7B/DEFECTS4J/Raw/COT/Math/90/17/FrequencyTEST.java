import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertNotNull(frequency);
    }

    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new NaturalComparator());
        Assert.assertNotNull(frequency);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.3333333333333333 \t 0.6666666666666667\n";
        String actual = frequency.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        Object key = new Integer(1);
        frequency.addValue(key);
        long expectedCount = 1;
        long actualCount = frequency.getCount(key);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency frequency = new Frequency();
        Object key1 = new Integer(1);
        Object key2 = new String("hello");
        frequency.addValue(key1);
        try {
            frequency.addValue(key2);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Value not comparable to existing values.", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        int key = 1;
        frequency.addValue(key);
        long expectedCount = 1;
        long actualCount = frequency.getCount(key);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        long key = 1;
        frequency.addValue(key);
        long expectedCount = 1;
        long actualCount = frequency.getCount(key);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        char key = 'a';
        frequency.addValue(key);
        long expectedCount = 1;
        long actualCount = frequency.getCount(key);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        long expectedCount = 0;
        long actualCount = frequency.getCount(1);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        Object expected = new Integer(1);
        Object actual = iterator.next();
        Assert.assertEquals(expected, actual);
        expected = new Integer(2);
        actual = iterator.next();
        Assert.assertEquals(expected, actual);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long expected = 3;
        long actual = frequency.getSumFreq();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCountObject() {
        Frequency frequency = new Frequency();
        Object key = new Integer(1);
        frequency.addValue(key);
        frequency.addValue(key);
        long expectedCount = 2;
        long actualCount = frequency.getCount(key);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        int key = 1;
        frequency.addValue(key);
        frequency.addValue(key);
        long expectedCount = 2;
        long actualCount = frequency.getCount(key);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        long key = 1;
        frequency.addValue(key);
        frequency.addValue(key);
        long expectedCount = 2;
        long actualCount = frequency.getCount(key);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        char key = 'a';
        frequency.addValue(key);
        frequency.addValue(key);
        long expectedCount = 2;
        long actualCount = frequency.getCount(key);
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetPctObject() {
        Frequency frequency = new Frequency();
        Object key = new Integer(1);
        frequency.addValue(key);
        frequency.addValue(new Integer(2));
        double expected = 0.3333333333333333;
        double actual = frequency.getPct(key);
        Assert.assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        int key = 1;
        frequency.addValue(key);
        frequency.addValue(2);
        double expected = 0.3333333333333333;
        double actual = frequency.getPct(key);
        Assert.assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        long key = 1;
        frequency.addValue(key);
        frequency.addValue(2);
        double expected = 0.3333333333333333;
        double actual = frequency.getPct(key);
        Assert.assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        char key = 'a';
        frequency.addValue(key);
        frequency.addValue('a');
        double expected = 0.3333333333333333;
        double actual = frequency.getPct(key);
        Assert.assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testGetCumFreqObject() {
        Frequency frequency = new Frequency();
        Object key = new Integer(1);
        frequency.addValue(key);
        frequency.addValue(new Integer(2));
        long expected = 1;
        long actual = frequency.getCumFreq(key);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        int key = 1;
        frequency.addValue(key);
        frequency.addValue(2);
        long expected = 1;
        long actual = frequency.getCumFreq(key);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        long key = 1;
        frequency.addValue(key);
        frequency.addValue(2);
        long expected = 1;
        long actual = frequency.getCumFreq(key);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        char key = 'a';
        frequency.addValue(key);
        frequency.addValue('a');
        long expected = 1;
        long actual = frequency.getCumFreq(key);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCumPctObject() {
        Frequency frequency = new Frequency();
        Object key = new Integer(1);
        frequency.addValue(key);
        frequency.addValue(new Integer(2));
        double expected = 0.3333333333333333;
        double actual = frequency.getCumPct(key);
        Assert.assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        int key = 1;
        frequency.addValue(key);
        frequency.addValue(2);
        double expected = 0.3333333333333333;
        double actual = frequency.getCumPct(key);
        Assert.assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        long key = 1;
        frequency.addValue(key);
        frequency.addValue(2);
        double expected = 0.3333333333333333;
        double actual = frequency.getCumPct(key);
        Assert.assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        char key = 'a';
        frequency.addValue(key);
        frequency.addValue('a');
        double expected = 0.3333333333333333;
        double actual = frequency.getCumPct(key);
        Assert.assertEquals(expected, actual, 0.0000001);
    }
}