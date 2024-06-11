import org.joda.time.DateTimeField;
import org.joda.time.MockDateTimeField;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.LenientChronology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;
    private DateTimeField baseField;

    @Before
    public void setUp() {
        baseField = new MockDateTimeField();
        lenientDateTimeField = new LenientDateTimeField(baseField, GregorianChronology.getInstance());
    }

    @Test
    public void testIsLenient() {
        assertEquals(true, lenientDateTimeField.isLenient());
    }

    @Test
    public void testSet_WithValidValue() {
        long instant = 1589764900000L; // May 18, 2020 10:15:00
        int value = 5;

        long result = lenientDateTimeField.set(instant, value);

        assertEquals(5, lenientDateTimeField.get(result));
    }

    @Test
    public void testSet_WithOutOfBoundsValue() {
        long instant = 1589764900000L; // May 18, 2020 10:15:00
        int value = 15;

        long result = lenientDateTimeField.set(instant, value);

        // Out of bounds value should perform an addition instead
        assertEquals(15, lenientDateTimeField.get(result));
    }

    @Test
    public void testSet_WithNegativeOutOfBoundsValue() {
        long instant = 1589764900000L; // May 18, 2020 10:15:00
        int value = -5;

        long result = lenientDateTimeField.set(instant, value);

        // Out of bounds value should perform an addition instead
        assertEquals(-5, lenientDateTimeField.get(result));
    }

    @Test
    public void testSet_WithEdgeValue() {
        long instant = 1589764900000L; // May 18, 2020 10:15:00
        int value = 59; // maximum value for minutes

        long result = lenientDateTimeField.set(instant, value);

        assertEquals(59, lenientDateTimeField.get(result));
    }

    @Test
    public void testSet_WithErrorScenario() {
        // Create a LenientDateTimeField with a null base field
        DateTimeField nullField = null;
        LenientDateTimeField nullLenientDateTimeField = new LenientDateTimeField(nullField, GregorianChronology.getInstance());

        long instant = 1589764900000L; // May 18, 2020 10:15:00
        int value = 10;

        // The set method should gracefully handle the null field and return the same instant
        long result = nullLenientDateTimeField.set(instant, value);

        assertEquals(instant, result);
    }
}