import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        LenientDateTimeField field = new LenientDateTimeField(null, null);
        assertEquals(true, field.isLenient());
    }

    @Test
    public void testSet() {
        Chronology base = null;
        DateTimeField dateTimeField = new StrictDateTimeField(null, null);
        LenientDateTimeField field = new LenientDateTimeField(dateTimeField, base);
        long instant = 1000;
        int value = 10;
        long result = field.set(instant, value);
        assertEquals(1010, result);
    }

    @Test
    public void testGetInstance() {
        Chronology base = null;
        DateTimeField dateTimeField = new StrictDateTimeField(null, null);
        DateTimeField result = LenientDateTimeField.getInstance(dateTimeField, base);
        assertEquals(null, result);
    }
}