import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientField;
    private DateTimeField baseField;
    private Chronology baseChrono;

    @Before
    public void setUp() {
        baseField = new BasicDateTimeField();
        baseChrono = new LenientChronology();
        lenientField = new LenientDateTimeField(baseField, baseChrono);
    }

    @Test
    public void testGetInstance_NullField() {
        assertNull(LenientDateTimeField.getInstance(null, baseChrono));
    }

    @Test
    public void testGetInstance_LenientField() {
        DateTimeField field = new LenientDateTimeField(baseField, baseChrono);
        assertSame(field, LenientDateTimeField.getInstance(field, baseChrono));
    }

    @Test
    public void testGetInstance_StrictField() {
        DateTimeField strictField = new StrictDateTimeField(baseField);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, baseChrono);
        assertTrue(lenientField instanceof LenientDateTimeField);
        assertNotSame(strictField, lenientField);
        assertSame(baseField, ((DelegatedDateTimeField) lenientField).getWrappedField());
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet_PositiveDifference() {
        long instant = 500000;
        int value = 100;
        long expected = 600000;
        assertEquals(expected, lenientField.set(instant, value));
    }

    @Test
    public void testSet_NegativeDifference() {
        long instant = 500000;
        int value = -100;
        long expected = 400000;
        assertEquals(expected, lenientField.set(instant, value));
    }

    @Test
    public void testSet_ZeroDifference() {
        long instant = 500000;
        int value = 0;
        long expected = 500000;
        assertEquals(expected, lenientField.set(instant, value));
    }
}