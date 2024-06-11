import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
    }

    @Test
    public void testAddValue_Integer() {
        Frequency freq = new Frequency();
        freq.addValue(Integer.valueOf(2));
        assertEquals(1, freq.getCount(2));
    }

    @Test
    public void testAddValue_Long() {
        Frequency freq = new Frequency();
        freq.addValue(3L);
        assertEquals(1, freq.getCount(3));
    }

    @Test
    public void testAddValue_Char() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testAddValue_NonComparable() {
        Frequency freq = new Frequency();
        try {
            freq.addValue(new Object()); // Adding non-comparable object should throw IllegalArgumentException
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getCount(1));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(3, freq.getSumFreq());
    }

    // Add more test cases for other methods as needed

}