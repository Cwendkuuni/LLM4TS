import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class FrequencyTest {

    @Test
    public void testAddValue_Object() {
        Frequency freq = new Frequency();

        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddValue_int() {
        Frequency freq = new Frequency();

        freq.addValue(10);
        assertEquals(1, freq.getCount(10));
    }

    @Test
    public void testAddValue_Integer() {
        Frequency freq = new Frequency();

        freq.addValue(Integer.valueOf(15));
        assertEquals(1, freq.getCount(15));
    }

    @Test
    public void testAddValue_long() {
        Frequency freq = new Frequency();

        freq.addValue(20L);
        assertEquals(1, freq.getCount(20L));
    }

    @Test
    public void testAddValue_char() {
        Frequency freq = new Frequency();

        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testToString() {
        Frequency freq = new Frequency();

        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n" +
                          "5\t2\t66%\t66%\n" +
                          "10\t1\t33%\t100%\n";
        assertEquals(expected, freq.toString());
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();

        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);

        assertEquals(3, freq.getSumFreq());
    }

    @Test
    public void testGetCount_Object() {
        Frequency freq = new Frequency();

        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(10);

        assertEquals(2, freq.getCount(5));
    }

    @Test
    public void testGetPct_Object() {
        Frequency freq = new Frequency();

        freq.addValue(5);
        freq.addValue(5);

        assertEquals(0.6666666666666666, freq.getPct(5), 0.001);
    }

    @Test
    public void testCumFreq_Object() {
        Frequency freq = new Frequency();
        
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(4);
        
        assertEquals(2, freq.getCumFreq(2));
    }

    @Test
    public void testCumPct_Object() {
        Frequency freq = new Frequency();
        
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(4);
        
        assertEquals(0.6666666666666666, freq.getCumPct(2), 0.001);
    }

}