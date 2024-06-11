import org.joda.time.DateTimeField;
import org.joda.time.testing.FakeDateTimeField;
import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.LenientChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private DateTimeField field;
    private Chronology base;

    @Before
    public void setUp() {
        base = new LenientChronology();
        field = new FakeDateTimeField();
    }

    @After
    public void tearDown() {
        field = null;
        base = null;
    }

    @Test
    public void testGetInstance_withNull() {
        DateTimeField result = LenientDateTimeField.getInstance(null, base);
        assertNull(result);
    }

    @Test
    public void testGetInstance_withStrictDateTimeField() {
        StrictDateTimeField strictField = new StrictDateTimeField(field);
        DateTimeField result = LenientDateTimeField.getInstance(strictField, base);
        assertSame(field, result);
    }

    @Test
    public void testGetInstance_withLenientDateTimeField() {
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        DateTimeField result = LenientDateTimeField.getInstance(lenientField, base);
        assertSame(lenientField, result);
    }

    @Test
    public void testGetInstance_withNonLenientDateTimeField() {
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        assertTrue(result instanceof LenientDateTimeField);
    }

    @Test
    public void testIsLenient() {
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeUtils.setCurrentMillisFixed(0);
        long instant = 0;
        int value = 10;
        long expected = 20;

        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        long result = lenientField.set(instant, value);

        assertEquals(expected, result);
        DateTimeUtils.setCurrentMillisSystem();
    }
}