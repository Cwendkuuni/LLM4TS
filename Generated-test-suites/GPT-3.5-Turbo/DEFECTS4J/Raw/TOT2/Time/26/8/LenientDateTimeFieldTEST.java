
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;

public class LenientDateTimeFieldTest {
    
    @Test
    public void testGetInstance() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.year(), ISOChronology.getInstance());
        Chronology base = ISOChronology.getInstance();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertTrue(lenientField instanceof LenientDateTimeField);
    }

    @Test
    public void testSet_OutOfBounds() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.monthOfYear(), ISOChronology.getInstance());
        Chronology base = ISOChronology.getInstance();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        
        long instant = base.getDateTimeMillis(2022, 2, 28, 12, 0, 0, 0);
        int value = 13;
        long result = lenientField.set(instant, value);
        
        assertEquals(base.getDateTimeMillis(2023, 3, 28, 0, 0, 0, 0), result);
    }
}
