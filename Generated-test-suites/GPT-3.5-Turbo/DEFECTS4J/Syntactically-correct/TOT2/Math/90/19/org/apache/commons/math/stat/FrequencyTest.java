import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddValueObject() {
        Frequency freq = new Frequency();
        freq.addValue((Object) 10);
        assertEquals(1, freq.getCount(10));
    }

    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(100L);
        assertEquals(1, freq.getCount(100L));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(new Frequency());
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        assertEquals(2, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(7);
        assertEquals(0.5, freq.getPct(5), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(7);
        assertEquals(1, freq.getCumFreq(5));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(7);
        assertEquals(0.5, freq.getCumPct(5), 0.001);
    }
}