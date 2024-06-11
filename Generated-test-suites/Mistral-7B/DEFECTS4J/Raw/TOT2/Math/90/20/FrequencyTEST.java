import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        long count = freq.getCount(1);
        assertEquals(2, count);
    }

    @Test
    public void testAddValueWithMinIntegerValue() {
        Frequency freq = new Frequency();
        Integer minValue = Integer.MIN_VALUE;
        freq.addValue(minValue);
        long count = freq.getCount(minValue);
        assertEquals(1, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithIncomparableValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNegativeValue() {
        Frequency freq = new Frequency();
        freq.addValue(-1);
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.clear();
        long count = freq.getCount(1);
        assertEquals(0, count);
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        long sumFreq = freq.getSumFreq();
        assertEquals(3, sumFreq);
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        long count = freq.getCount(1);
        assertEquals(2, count);
        count = freq.getCount(2);
        assertEquals(1, count);
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        double pct = freq.getPct(1);
        assertEquals(0.6666666666666666, pct, 0.0000001);
        pct = freq.getPct(2);
        assertEquals(0.3333333333333333, pct, 0.0000001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        long cumFreq = freq.getCumFreq(1);
        assertEquals(2, cumFreq);
        cumFreq = freq.getCumFreq(2);
        assertEquals(3, cumFreq);
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        double cumPct = freq.getCumPct(1);
        assertEquals(0.6666666666666666, cumPct, 0.0000001);
        cumPct = freq.getCumPct(2);
        assertEquals(0.8333333333333333, cumPct, 0.0000001);
    }

    @Test
    public void testAddValueWithMaxIntegerValue() {
        Frequency freq = new Frequency();
        Integer maxValue = Integer.MAX_VALUE;
        freq.addValue(maxValue);
        long count = freq.getCount(maxValue);
        assertEquals(1, count);
    }
}