import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FrequencyTest {

    @Test
    public void testAddValueObject() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
    }

    @Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        assertEquals(1, freq.getCount(2));
    }

    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(100000000000L);
        assertEquals(1, freq.getCount(100000000000L));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(2, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(1, freq.getCount(2));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(0.3333333333333333, freq.getPct(1), 0.0001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(2, freq.getCumFreq(2));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(0.6666666666666666, freq.getCumPct(2), 0.0001);
    }
}