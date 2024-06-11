import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.PeriodType;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class LenientDateTimeFieldTest {

    @Test
    public void testLenientDateTimeField_Basic() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setChronology(chronology);
        dateTime.set(2022, 1, 31); // Should be out of bounds, but lenient

        lenientField.set(dateTime.getLong(), 1);
        assertEquals(new LocalDate(2022, 2, 1), dateTime.toLocalDate()); // Should be February 1, not March 1

        lenientField.set(dateTime.getLong(), 33);
        assertEquals(new LocalDate(2022, 3, 3), dateTime.toLocalDate()); // Should be March 3, not February 29
    }

    @Test
    public void testLenientDateTimeField_GetInstanceWithNullField() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = null;
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);

        assertNotSame(field, lenientField);
    }

    @Test
    public void testLenientDateTimeField_GetInstanceWithSameField() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), chronology);
        LenientDateTimeField lenientField1 = LenientDateTimeField.getInstance(field, chronology);
        LenientDateTimeField lenientField2 = LenientDateTimeField.getInstance(field, chronology);

        assertNotSame(field, lenientField1);
        assertSame(lenientField1, lenientField2);
    }

    @Test
    public void testLenientDateTimeField_GetInstanceWithSameWrappedField() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField wrappedField = StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), chronology);
        LenientDateTimeField lenientField1 = new LenientDateTimeField(wrappedField, chronology);
        LenientDateTimeField lenientField2 = new LenientDateTimeField(wrappedField, chronology);

        assertNotSame(wrappedField, lenientField1);
        assertSame(lenientField1, lenientField2);
    }

    @Test
    public void testLenientDateTimeField_GetInstanceWithWrappedFieldThatIsLenient() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        LenientDateTimeField wrappedField = new LenientDateTimeField(StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), chronology), chronology);
        LenientDateTimeField lenientField = new LenientDateTimeField(wrappedField, chronology);

        assertSame(wrappedField, lenientField.getWrappedField());
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testLenientDateTimeField_SetValueOutOfBounds() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.minuteOfHour(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setChronology(chronology);
        dateTime.set(2022, 1, 31, 1, 55);

        long originalValue = lenientField.get(dateTime.getLong());

        lenientField.set(dateTime.getLong(), 61); // Should be out of bounds, but lenient
        assertEquals(new LocalTime(2022, 2, 31, 1, 1), new LocalTime(dateTime.getLocalDateTime())); // Should be 1:01:00, not 1:61:00

        assertEquals(originalValue + 11, lenientField.get(dateTime.getLong()));
    }

    @Test
    public void testLenientDateTimeField_SetValueWithinBounds() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.minuteOfHour(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setChronology(chronology);
        dateTime.set(2022, 1, 31, 1, 55);

        lenientField.set(dateTime.getLong(), 56);
        assertEquals(new LocalTime(2022, 1, 31, 1, 56), new LocalTime(dateTime.getLocalDateTime()));

        assertEquals(56, lenientField.get(dateTime.getLong()));
    }

    @Test
    public void testLenientDateTimeField_SetValueWithinBounds_AddingMoreThanADay() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.minuteOfHour(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setChronology(chronology);
        dateTime.set(2022, 1, 31, 1, 55);

        lenientField.set(dateTime.getLong(), 1440); // Should be 22 hours, but we add days as well

        assertEquals(new LocalDateTime(2022, 2, 1, 11, 55), new LocalDateTime(dateTime.toLocalDateTime()));

        assertEquals(1440, lenientField.get(dateTime.getLong()));
    }

    @Test
    public void testLenientDateTimeField_SetValueWithinBounds_AddingMoreThanADay_PeriodType() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.minuteOfHour(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setChronology(chronology);
        dateTime.set(2022, 1, 31, 1, 55);

        lenientField.set(dateTime.getLong(), new DurationFieldType(PeriodType.days(), 2).get(2)); // Should be 2 days, but we add hours as well

        assertEquals(new LocalDateTime(2022, 3, 2, 1, 55), new LocalDateTime(dateTime.toLocalDateTime()));

        assertEquals(2, lenientField.get(dateTime.getLong()));
    }

    @Test
    public void testLenientDateTimeField_SetValueWithinBounds_AddingLessThanADay() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.minuteOfHour(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setChronology(chronology);
        dateTime.set(2022, 1, 31, 1, 55);

        lenientField.set(dateTime.getLong(), -120); // Should be -2 hours, but we subtract days as well

        assertEquals(new LocalDateTime(2022, 1, 30, 19, 35), new LocalDateTime(dateTime.toLocalDateTime()));

        assertEquals(-120, lenientField.get(dateTime.getLong()));
    }

    @Test
    public void testLenientDateTimeField_SetValueWithinBounds_AddingLessThanADay_PeriodType() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.minuteOfHour(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setChronology(chronology);
        dateTime.set(2022, 1, 31, 1, 55);

        lenientField.set(dateTime.getLong(), new DurationFieldType(PeriodType.hours(), -2).get(2)); // Should be -2 hours, but we subtract days as well

        assertEquals(new LocalDateTime(2022, 1, 30, 19, 35), new LocalDateTime(dateTime.toLocalDateTime()));

        assertEquals(-2, lenientField.get(dateTime.getLong()));
    }

    @Test
    public void testLenientDateTimeField_SetValueOutOfBounds_AddingMoreThanMaxValue() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setChronology(chronology);
        dateTime.set(2022, 1, 31);

        long originalValue = lenientField.get(dateTime.getLong());

        lenientField.set(dateTime.getLong(), Integer.MAX_VALUE + 1); // Should be out of bounds, but lenient

        assertEquals(new LocalDate(2023, 1, 1), new LocalDate(dateTime.toLocalDateTime())); // Should be January 1, 2023, not February date

        assertEquals(originalValue + 31, lenientField.get(dateTime.getLong()));
    }

    @Test
    public void testLenientDateTimeField_SetValueOutOfBounds_AddingLessThanMinValue() {
        Chronology chronology = org.joda.time.LocalDateTime.now().getChronology();
        DateTimeField field = StrictDateTimeField.instance(DateTimeFieldType.dayOfMonth(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setChronology(chronology);
        dateTime.set(2022, 1, 31);

        lenientField.set(dateTime.getLong(), Integer.MIN_VALUE - 1); // Should be out of bounds, but lenient

        assertEquals(new LocalDate(2021, 12, 31), new LocalDate(dateTime.toLocalDateTime())); // Should be December 31, 2021, not November date

        assertEquals(dateTime.getMillis() - 31 * 24 * 60 * 60 * 1000, lenientField.get(dateTime.getLong()) * 24 * 60 * 60 * 1000);
    }
}