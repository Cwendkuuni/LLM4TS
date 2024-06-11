import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = BaseDateTime.UTC.getChronology();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertEquals(field, lenientField.getDelegate());
        assertEquals(base, lenientField.getBase());
    }

    /**
     * Test constructor with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullArguments() {
        new LenientDateTimeField(null, null);
    }

    /**
     * Test constructor with null delegate.
     */
    @Test(expected = IllegalStateException.class)
    public void testConstructorNullDelegate() {
        new LenientDateTimeField(null, BaseDateTime.UTC.getChronology());
    }

    /**
     * Test constructor with null base.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBase() {
        new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), null);
    }

    /**
     * Test getInstance method with valid arguments.
     */
    @Test
    public void testGetInstanceValidArguments() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = BaseDateTime.UTC.getChronology();
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, lenientField);
    }

    /**
     * Test getInstance method with null field.
     */
    @Test(expected = NullPointerException.class)
    public void testGetInstanceNullField() {
        LenientDateTimeField.getInstance(null, BaseDateTime.UTC.getChronology());
    }

    /**
     * Test getInstance method with null base.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceNullBase() {
        LenientDateTimeField.getInstance(DateTimeFieldType.dayOfMonth(), null);
    }

    /**
     * Test getInstance method with lenient field.
     */
    @Test
    public void testGetInstanceLenientField() {
        DateTimeField field = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), BaseDateTime.UTC.getChronology());
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, BaseDateTime.UTC.getChronology());
        assertEquals(field, lenientField);
    }

    /**
     * Test isLenient method.
     */
    @Test
    public void testIsLenient() {
        DateTimeField field = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), BaseDateTime.UTC.getChronology());
        assertTrue(field.isLenient());
    }

    /**
     * Test set method with valid arguments.
     */
    @Test
    public void testSetValidArguments() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = BaseDateTime.UTC.getChronology();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(BaseDateTime.UTC.toInstant(0L), base);
        long instant = dateTime.toInstant().getMillis();

        int value = 1;
        long expected = instant + FieldUtils.getMillisecondsPerDay(field.getFieldType()) - 1;
        long result = lenientField.set(instant, value);

        assertEquals(expected, result);

        dateTime = new LocalDateTime(BaseDateTime.UTC.toInstant(expected), base);
        assertEquals(dateTime.getDayOfMonth(), value);
    }

    /**
     * Test set method with out of bounds value.
     */
    @Test
    public void testSetOutOfBoundsValue() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        Chronology base = BaseDateTime.UTC.getChronology();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(BaseDateTime.UTC.toInstant(0L), base);
        long instant = dateTime.toInstant().getMillis();

        int value = 32; // out of bounds for February
        long expected = instant + FieldUtils.getMillisecondsPerDay(field.getFieldType()) * 28 + FieldUtils.getMillisecondsPerDay(field.getFieldType()) * 2; // February 29 in a leap year
        long result = lenientField.set(instant, value);

        assertEquals(expected, result);

        dateTime = new LocalDateTime(BaseDateTime.UTC.toInstant(expected), base);
        assertEquals(dateTime.getDayOfMonth(), value);
    }

    /**
     * Test set method with null instant.
     */
    @Test(expected = NullPointerException.class)
    public void testSetNullInstant() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, BaseDateTime.UTC.getChronology());
        lenientField.set(null, 1);
    }

    /**
     * Test set method with null value.
     */
    @Test(expected = NullPointerException.class)
    public void testSetNullValue() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, BaseDateTime.UTC.getChronology());
        lenientField.set(1L, null);
    }
}