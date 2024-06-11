import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddValueForChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testAddValueInvalid() {
        Frequency freq = new Frequency();
        try {
            freq.addValue("invalid");
        } catch (IllegalArgumentException e) {
            // Expected exception
            return;
        }
        // If no exception was thrown, fail the test
        fail("Expected IllegalArgumentException was not thrown");
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.clear();
        assertEquals(0, freq.getCount(10));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(3);
        freq.addValue(3);
        freq.addValue(5);
        assertEquals(3, freq.getSumFreq());
    }

    // Add more test cases here for other methods in the Frequency class

}