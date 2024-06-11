package org.apache.commons.math.stat;
import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FrequencyTest {

    @Test
    public void testFrequencyAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        
        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(0, frequency.getCount(3));
    }

    @Test
    public void testFrequencyAddValueException() {
        Frequency frequency = new Frequency();
        frequency.addValue('a'); // should throw an IllegalArgumentException
        
        // code to check for the exception
    }
}