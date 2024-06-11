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
    public void testAddValueEdgeCase() {
        Frequency freq = new Frequency();
        freq.addValue(-1); // Negative number edge case
        assertEquals(1, freq.getCount(-1));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        assertEquals(0, freq.getCount(5));
        assertEquals(1, freq.getCount(10));
    }

    @Test
    public void testGetValueIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        int count = 0;
        for (Object value : freq.valuesIterator()) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(3, freq.getCumFreq(3));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
    }
}