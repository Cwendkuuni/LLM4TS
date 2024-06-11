import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.field.LenientDateTimeField;

public class LenientDateTimeFieldTest {
    
    private LenientDateTimeField lenientDateTimeField;
    private DateTime dateTime;

    @Before
    public void setup() {
        lenientDateTimeField = LenientDateTimeField.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeZone.UTC);
        dateTime = new DateTime(2022, 10, 15, 0, 0, 0, 0, DateTimeZone.UTC);
    }

    @Test
    public void testSetTypical() {
        long updatedInstant = lenientDateTimeField.set(dateTime.getMillis(), 20);
        DateTime updatedDateTime = new DateTime(updatedInstant, DateTimeZone.UTC);
        assertEquals(20, updatedDateTime.getDayOfMonth());
    }

    @Test
    public void testSetEdgeCase() {
        // Setting a value that is out of bounds should increment the value instead
        long updatedInstant = lenientDateTimeField.set(dateTime.getMillis(), 35);
        DateTime updatedDateTime = new DateTime(updatedInstant, DateTimeZone.UTC);
        assertEquals(5, updatedDateTime.getDayOfMonth());
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void testGetInstance() {
        // Testing getInstance method for null field
        DateTimeField field = null;
        DateTimeField instance = LenientDateTimeField.getInstance(field, DateTimeZone.UTC);
        assertEquals(null, instance);
    }

    @Test
    public void testGetInstanceNonLenient() {
        // Testing getInstance method when the field is not already lenient
        DateTimeField field = new StrictDateTimeField(BaseDateTimeField.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeZone.UTC));
        DateTimeField instance = LenientDateTimeField.getInstance(field, DateTimeZone.UTC);
        assertTrue(instance instanceof LenientDateTimeField);
    }

    @Test
    public void testGetInstanceAlreadyLenient() {
        // Testing getInstance method when the field is already lenient
        DateTimeField field = new LenientDateTimeField(BaseDateTimeField.getInstance(DateTimeFieldType.dayOfMonth(), DateTimeZone.UTC));
        DateTimeField instance = LenientDateTimeField.getInstance(field, DateTimeZone.UTC);
        assertTrue(instance instanceof LenientDateTimeField);
    }

}