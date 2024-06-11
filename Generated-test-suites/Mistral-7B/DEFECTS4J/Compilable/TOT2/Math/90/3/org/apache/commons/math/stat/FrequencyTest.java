package org.apache.commons.math.stat;
import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();

        // Basic usage test
        frequency.addValue("test");
        frequency.addValue("test");
        assertEquals(2, frequency.getCount("test"));

        // Edge case 1: Adding an empty object
        frequency.addValue(new Object());
        assertEquals(0, frequency.getCount(new Object()));

        // Edge case 2: Adding a null object
        frequency.addValue(null);
        assertEquals(0, frequency.getCount(null));
    }
}