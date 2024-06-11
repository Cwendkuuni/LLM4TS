import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;

    @Before
    public void setup() {
        lenientDateTimeField = new LenientDateTimeField(
                                  ISOChronology.getInstance().minuteOfHour(),
                                  ISOChronology.getInstance()
                               );
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void testGetInstance_withLenientField() {
        DateTimeField field = lenientDateTimeField;
        assertEquals(field, LenientDateTimeField.getInstance(field, ISOChronology.getInstance()));
    }

    @Test
    public void testGetInstance_withStrictField() {
        DateTimeField field = ISOChronology.getInstance().secondOfMinute();
        assertEquals(field, LenientDateTimeField.getInstance(field, ISOChronology.getInstance()));
    }

    @Test
    public void testGetInstance_withNullField() {
        assertNull(LenientDateTimeField.getInstance(null, ISOChronology.getInstance()));
    }

    @Test
    public void testSet_withinBounds() {
        DateTime dt = new DateTime(2022, 12, 31, 23, 59, ISOChronology.getInstance());
        long instant = dt.getMillis();
        int newValue = 29;

        long updatedInstant = lenientDateTimeField.set(instant, newValue);
        DateTime updatedDt = new DateTime(updatedInstant, ISOChronology.getInstance());

        assertEquals(newValue, updatedDt.getMinuteOfHour());
    }

    @Test
    public void testSet_withValueLowerThanBounds() {
        DateTime dt = new DateTime(2022, 12, 31, 23, 59, ISOChronology.getInstance());
        long instant = dt.getMillis();
        int newValue = -10;

        long updatedInstant = lenientDateTimeField.set(instant, newValue);
        DateTime updatedDt = new DateTime(updatedInstant, ISOChronology.getInstance());

        assertEquals(newValue, updatedDt.getMinuteOfHour());
    }

    @Test
    public void testSet_withValueHigherThanBounds() {
        DateTime dt = new DateTime(2022, 12, 31, 23, 59, ISOChronology.getInstance());
        long instant = dt.getMillis();
        int newValue = 60;

        long updatedInstant = lenientDateTimeField.set(instant, newValue);
        DateTime updatedDt = new DateTime(updatedInstant, ISOChronology.getInstance());

        assertEquals(newValue, updatedDt.getMinuteOfHour());
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testSet_withInvalidFieldValue() {
        DateTime dt = new DateTime(2022, 12, 31, 23, 59, ISOChronology.getInstance());
        long instant = dt.getMillis();
        int newValue = 61;

        lenientDateTimeField.set(instant, newValue);
    }

    @Test
    public void testSet_withPositiveDifference() {
        DateTime dt = new DateTime(2022, 12, 31, 23, 59, ISOChronology.getInstance());
        long instant = dt.getMillis();
        int oldValue = dt.getSecondOfMinute();
        int difference = 10;
        int newValue = oldValue + difference;

        long updatedInstant = lenientDateTimeField.set(instant, newValue);
        DateTime updatedDt = new DateTime(updatedInstant, ISOChronology.getInstance());

        assertEquals(newValue, updatedDt.getSecondOfMinute());
    }

    @Test
    public void testSet_withNegativeDifference() {
        DateTime dt = new DateTime(2022, 12, 31, 23, 59, ISOChronology.getInstance());
        long instant = dt.getMillis();
        int oldValue = dt.getSecondOfMinute();
        int difference = -10;
        int newValue = oldValue + difference;

        long updatedInstant = lenientDateTimeField.set(instant, newValue);
        DateTime updatedDt = new DateTime(updatedInstant, ISOChronology.getInstance());

        assertEquals(newValue, updatedDt.getSecondOfMinute());
    }

}