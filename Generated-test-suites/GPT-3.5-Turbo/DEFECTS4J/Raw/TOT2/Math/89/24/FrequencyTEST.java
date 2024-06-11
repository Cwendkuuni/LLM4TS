import static org.junit.Assert.*;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        assertEquals(1, freq.getCount(2));
    }

    @Test
    public void testAddValue_Comparable() {
        Frequency freq = new Frequency();
        freq.addValue(Long.valueOf(3));
        assertEquals(1, freq.getCount(3));
    }

    @Test
    public void testAddValue_Int() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddValue_Long() {
        Frequency freq = new Frequency();
        freq.addValue(10L);
        assertEquals(1, freq.getCount(10L));
    }

    @Test
    public void testAddValue_Char() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        assertEquals(1, freq.getCount('A'));
    }

    @Test
    public void testAddValue_Exception() {
        Frequency freq = new Frequency();
        try {
            freq.addValue("Invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.clear();
        assertEquals(0, freq.getCount(2));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(0.5, freq.getPct(1), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(2, freq.getCumFreq(1));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0.666, freq.getCumPct(1), 0.001);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}