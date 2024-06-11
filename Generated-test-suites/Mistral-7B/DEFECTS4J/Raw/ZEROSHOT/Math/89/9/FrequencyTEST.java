import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequencyInstance() {
        Frequency frequency = new Frequency();
        Assert.assertNotNull(frequency);
    }

    @Test
    public void testFrequencyComparatorInstance() {
        Comparator<Comparable> comparator = new Comparator<Comparable>() {
            @Override
            public int compare(Comparable o1, Comparable o2) {
                return o1.compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        Assert.assertNotNull(frequency);
    }

    @Test
    public void testFrequencyToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333 \n2 \t 2 \t 0.3333333333333333 \t 0.6666666666666666 \n3 \t 2 \t 0.3333333333333333 \t 1 \n";
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue("not a number");
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);
        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals(10, sumFreq);
        long count = frequency.getCount(1);
        Assert.assertEquals(2, count);
        count = frequency.getCount(2);
        Assert.assertEquals(2, count);
        count = frequency.getCount(3);
        Assert.assertEquals(3, count);
        long cumFreq = frequency.getCumFreq(1);
        Assert.assertEquals(2, cumFreq);
        cumFreq = frequency.getCumFreq(2);
        Assert.assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(3);
        Assert.assertEquals(6, cumFreq);
        double pct = frequency.getPct(1);
        Assert.assertEquals(0.4, pct, 0.001);
        pct = frequency.getPct(2);
        Assert.assertEquals(0.4, pct, 0.001);
        pct = frequency.getPct(3);
        Assert.assertEquals(0.2, pct, 0.001);
        double cumPct = frequency.getCumPct(1);
        Assert.assertEquals(0.3333333333333333, cumPct, 0.001);
        cumPct = frequency.getCumPct(2);
        Assert.assertEquals(0.6666666666666666, cumPct, 0.001);
        cumPct = frequency.getCumPct(3);
        Assert.assertEquals(1, cumPct, 0.001);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals(0, sumFreq);
    }
}