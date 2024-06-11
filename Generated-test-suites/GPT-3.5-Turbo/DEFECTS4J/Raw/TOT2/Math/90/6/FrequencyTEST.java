import static org.junit.Assert.*;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValue_Object() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        assertEquals(1, freq.getCount(10));
    }

    @Test
    public void testAddValue_int() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals(1, freq.getCount(5));
    }

    @Test
    public void testAddValue_long() {
        Frequency freq = new Frequency();
        freq.addValue(100L);
        assertEquals(1, freq.getCount(100L));
    }

    @Test
    public void testAddValue_char() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
    }

    @Test
    public void testGetValueIterator() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(20);
        Iterator iterator = freq.valuesIterator();
        ArrayList expectedList = Arrays.asList(10L, 20L);
        while(iterator.hasNext()) {
            assertTrue(expectedList.contains(iterator.next()));
        }
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.clear();
        assertEquals(0, freq.getCount(5));
    }

    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(2);
        freq.addValue(3);
        assertEquals(2, freq.getSumFreq());
    }

    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(10);
        freq.addValue(20);
        assertEquals(0.5, freq.getPct(10), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(1, freq.getCumFreq(5));
    }

    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(10);
        assertEquals(0.5, freq.getCumPct(5), 0.001);
    }

}