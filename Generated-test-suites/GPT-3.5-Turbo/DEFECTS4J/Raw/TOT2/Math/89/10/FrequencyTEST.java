import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testAddValueObject() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
        
        // Edge case: adding null value
        try {
            freq.addValue(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
        
        // Edge case: negative value
        freq.addValue(-2);
        assertEquals(1, freq.getCount(-2));
    }

    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(5000000000L);
        assertEquals(1, freq.getCount(5000000000L));
        
        // Edge case: maximum long value
        freq.addValue(Long.MAX_VALUE);
        assertEquals(1, freq.getCount(Long.MAX_VALUE));
    }

    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
        
        // Edge case: non-comparable char value
        try {
            freq.addValue('\u9999'); // Chinese character
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testAddValueComparable() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
        
        // Edge case: adding a non-comparable object
        try {
            freq.addValue(new Object());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(3, freq.getSumFreq());
        
        // Edge case: empty frequency table
        Frequency emptyFreq = new Frequency();
        assertEquals(0, emptyFreq.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(2, freq.getCount(5));
        
        // Edge case: value not present in frequency table
        assertEquals(0, freq.getCount(20));
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(0.6666666666666666, freq.getPct(5), 0.0001);
        
        // Edge case: no values added
        Double nan = Double.NaN;
        assertEquals(nan, freq.getPct(10));
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(2, freq.getCumFreq(2));
        
        // Edge case: value not comparable to present values
        assertEquals(0, freq.getCumFreq(5));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(0.3333333333333333, freq.getCumPct(2), 0.0001);
        
        // Edge case: no values added
        Double nan = Double.NaN;
        assertEquals(nan, freq.getCumPct(3));
    }
}