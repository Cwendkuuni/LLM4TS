import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(3, frequency.getSumFreq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("nonComparableValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValueOfDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("nonComparableValue");
    }
}