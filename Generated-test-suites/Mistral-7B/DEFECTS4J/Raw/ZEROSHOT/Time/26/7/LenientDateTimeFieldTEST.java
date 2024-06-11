import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.TestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link LenientDateTimeField}.
 */
public class LenientDateTimeFieldTest extends TestBase {

    private DateTimeField field;
    private Chronology chronology;
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        field = DateTimeFieldType.dayOfMonth();
        chronology = ISOChronology.getInstance();
        lenientField = LenientDateTimeField.getInstance(field, chronology);
    }

    @Test
    public void testInstance() {
        assertSame(field, lenientField.getAsDelegate());
        assertFalse(lenientField.isLenient());
        lenientField = LenientDateTimeField.getInstance(lenientField, chronology);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, chronology));
        LocalDateTime localDateTime = new MutableLocalDateTime(new BaseLocalDateTime(0L, chronology), chronology);

        // test valid values
        dateTime.setField(field, 3);
        localDateTime.setField(lenientField, 3);
        assertEquals(3, dateTime.getFieldValue(field));
        assertEquals(3, localDateTime.getFieldValue(lenientField));

        // test out of bounds values
        dateTime.setField(field, 32);
        localDateTime.setField(lenientField, 32);
        assertEquals(33, dateTime.getFieldValue(field));
        assertEquals(32, localDateTime.getFieldValue(lenientField));

        // test out of bounds values with offset
        dateTime.setField(field, -3);
        localDateTime.setField(lenientField, -3);
        assertEquals(-3, dateTime.getFieldValue(field));
        assertEquals(-3, localDateTime.getFieldValue(lenientField));

        // test set with LocalDateTimeFieldVerifier
        new LocalDateTimeFieldVerifier(lenientField).assertIsLenient();
        new LocalDateTimeFieldVerifier(lenientField).assertSetsCorrectly(localDateTime, 32);
        new LocalDateTimeFieldVerifier(lenientField).assertSetsCorrectly(localDateTime, -3);
    }

    @Test
    public void testGet() {
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, chronology));
        dateTime.setField(field, 3);

        assertEquals(3, lenientField.get(dateTime.getMillis()));
        assertEquals(3, lenientField.get(new BaseDateTime(0L, chronology).getMillis()));
    }

    @Test
    public void testAddRemove() {
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, chronology));
        MutableLocalDateTime localDateTime = new MutableLocalDateTime(new BaseLocalDateTime(0L, chronology), chronology);

        // test add
        assertEquals(3, lenientField.get(dateTime.getMillis()));
        lenientField.add(dateTime, 2);
        assertEquals(5, lenientField.get(dateTime.getMillis()));

        // test remove
        lenientField.set(dateTime, 5);
        assertEquals(5, lenientField.get(dateTime.getMillis()));
        lenientField.remove(dateTime, 2);
        assertEquals(3, lenientField.get(dateTime.getMillis()));

        // test add with LocalDateTime
        assertEquals(3, lenientField.get(localDateTime.getMillis()));
        lenientField.add(localDateTime, 2);
        assertEquals(5, lenientField.get(localDateTime.getMillis()));

        // test remove with LocalDateTime
        lenientField.set(localDateTime, 5);
        assertEquals(5, lenientField.get(localDateTime.getMillis()));
        lenientField.remove(localDateTime, 2);
        assertEquals(3, lenientField.get(localDateTime.getMillis()));
    }

    @Test
    public void testGetAsInt() {
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, chronology));
        dateTime.setFieldValueExact(field, 3);

        assertEquals(3, lenientField.getAsInt(dateTime.getMillis()));
        assertEquals(3, lenientField.getAsInt(new BaseDateTime(0L, chronology).getMillis()));
    }

    @Test
    public void testGetAsLong() {
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, chronology));
        dateTime.setFieldValueExact(field, 3);

        assertEquals(3, lenientField.getAsLong(dateTime.getMillis()));
        assertEquals(3, lenientField.getAsLong(new BaseDateTime(0L, chronology).getMillis()));
    }

    @Test
    public void testGetType() {
        assertEquals(field.getType(), lenientField.getType());
    }

    @Test
    public void testGetWrappedField() {
        assertEquals(field, lenientField.getWrappedField());
    }

    @Test
    public void testGetBase() {
        assertEquals(chronology, lenientField.getBase());
    }

    @Test
    public void testAddRemoveWithFieldUtils() {
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, chronology));
        MutableLocalDateTime localDateTime = new MutableLocalDateTime(new BaseLocalDateTime(0L, chronology), chronology);

        // test add
        assertEquals(3, lenientField.get(dateTime.getMillis()));
        FieldUtils.addToField(dateTime, lenientField, 2);
        assertEquals(5, lenientField.get(dateTime.getMillis()));

        // test remove
        FieldUtils.setToField(dateTime, lenientField, 5);
        assertEquals(5, lenientField.get(dateTime.getMillis()));
        FieldUtils.addToField(dateTime, lenientField, -2);
        assertEquals(3, lenientField.get(dateTime.getMillis()));

        // test add with LocalDateTime
        assertEquals(3, lenientField.get(localDateTime.getMillis()));
        FieldUtils.addToField(localDateTime, lenientField, 2);
        assertEquals(5, lenientField.get(localDateTime.getMillis()));

        // test remove with LocalDateTime
        FieldUtils.setToField(localDateTime, lenientField, 5);
        assertEquals(5, lenientField.get(localDateTime.getMillis()));
        FieldUtils.addToField(localDateTime, lenientField, -2);
        assertEquals(3, lenientField.get(localDateTime.getMillis()));
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testToString() {
        assertEquals("LenientDateTimeField[" + field + "]", lenientField.toString());
    }
}