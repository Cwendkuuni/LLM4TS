import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    @Test
    public void testGetInstance() {
        DateTimeField field = null;
        Chronology base = null;
        assertNull(LenientDateTimeField.getInstance(field, base));
        field = new StrictDateTimeField();
        assertNull(LenientDateTimeField.getInstance(field, base));
        field = new LenientDateTimeField(new StrictDateTimeField(), new LenientChronology());
        assertEquals(field, LenientDateTimeField.getInstance(field, base));
    }
    
    @Test
    public void testIsLenient() {
        DateTimeField field = new LenientDateTimeField(new StrictDateTimeField(), new LenientChronology());
        assertEquals(true, field.isLenient());
    }
    
    @Test
    public void testSet() {
        DateTimeField field = new LenientDateTimeField(new StrictDateTimeField(), new LenientChronology());
        assertEquals(0L, field.set(0L, 1));
        assertEquals(1L, field.set(0L, 0));
        assertEquals(3L, field.set(0L, 4));
    }
}