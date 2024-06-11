import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;

public class FrequencyTest {

    private Frequency freq;

    @Before
    public void setUp() {
        freq = new Frequency();
    }

    @Test
    public void testAddValue_Integer() {
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(1, freq.getCount(5));
        assertEquals(1, freq.getCount(10));
        assertEquals(0, freq.getCount(7));
    }

    @Test
    public void testAddValue_Long() {
        freq.addValue(100L);
        freq.addValue(200L);
        assertEquals(1, freq.getCount(100L));
        assertEquals(2, freq.getCount(200L));
    }

    @Test
    public void testAddValue_Character() {
        freq.addValue('a');
        freq.addValue('b');
        assertEquals(1, freq.getCount('a'));
        assertEquals(1, freq.getCount('b'));
    }

    @Test
    public void testAddValue_Comparable() {
        freq.addValue(5);
        freq.addValue(5);
        assertEquals(2, freq.getCount(5));
    }

    @Test
    public void testClear() {
        freq.addValue(5);
        freq.addValue(10);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
    }

    @Test
    public void testGetSumFreq() {
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(2, freq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        freq.addValue(5);
        freq.addValue(5);
        assertEquals(2, freq.getCount(5));
        assertEquals(0, freq.getCount(10));
    }

    @Test
    public void testGetPct() {
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(0.5, freq.getPct(5), 0.01);
    }

    @Test
    public void testGetCumFreq() {
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(1, freq.getCumFreq(5));
        assertEquals(2, freq.getCumFreq(10));
    }

    @Test
    public void testGetCumPct() {
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(0.5, freq.getCumPct(5), 0.01);
    }

}