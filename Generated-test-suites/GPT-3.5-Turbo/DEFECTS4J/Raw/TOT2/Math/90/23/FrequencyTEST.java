import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @Test
    public void addValue_Object_Integer() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCount(3));
    }

    @Test
    public void addValue_long() {
        frequency.addValue(10L);
        assertEquals(1, frequency.getCount(10L));
    }

    @Test
    public void addValue_char() {
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
    }

    @Test
    public void clear() {
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void valuesIterator() {
        frequency.addValue(2);
        frequency.addValue(3);
        Iterator iter = frequency.valuesIterator();
        List<Object> values = new ArrayList<>();
        while (iter.hasNext()) {
            values.add(iter.next());
        }
        assertEquals(2, values.size());
        assertTrue(values.contains(2));
        assertTrue(values.contains(3));
    }

    @Test
    public void getSumFreq() {
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(2, frequency.getSumFreq());
    }

    @Test
    public void getCount_Integer() {
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(2));
    }

    @Test
    public void getPct_Integer() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.5, frequency.getPct(2), 0.001);
    }

    @Test
    public void getCumFreq() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(1, frequency.getCumFreq(2));
        assertEquals(2, frequency.getCumFreq(3));
    }

    @Test
    public void getCumPct() {
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addValue_NonComparable() {
        frequency.addValue(new Object()); // Non-comparable object
    }

    @Test(expected = IllegalArgumentException.class)
    public void addValue_IncompatibleChar() {
        frequency.addValue('a'); // Char is not comparable with Long
        frequency.addValue(1L);
    }

}