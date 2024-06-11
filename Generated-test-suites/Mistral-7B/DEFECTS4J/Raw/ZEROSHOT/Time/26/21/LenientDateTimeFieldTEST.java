import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.ZoneOffset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class LenientDateTimeFieldTest {

    private static final long SERIALIZATION_UID = 8714085824173290599L;
    private static final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
    private static final DateTimeFieldType HOUR_OF_DAY = DateTimeFieldType.hourOfDay();
    private static final DateTimeFieldType MINUTE_OF_HOUR = DateTimeFieldType.minuteOfHour();
    private static final DateTimeFieldType SECOND_OF_MINUTE = DateTimeFieldType.secondOfMinute();

    @Test
    public void testLenientDateTimeFieldIsLenient() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(new BasicDateTimeField(DAY_OF_MONTH, 30), BaseDateTime.UTC);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testLenientDateTimeFieldSetsValueOutOfBounds() {
        Chronology base = BaseDateTime.UTC;
        DateTimeField strictField = new BasicDateTimeField(DAY_OF_MONTH, 30, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, base);

        long instant = LocalDateTime.now().toDate().getTime();
        int newValue = 32;

        long result = lenientField.set(instant, newValue);

        DateTimeField expectedField = new BasicDateTimeField(DAY_OF_MONTH, 32, base);
        assertEquals(expectedField.get(instant), result);
    }

    @Test
    public void testLenientDateTimeFieldSetsValueWithinBounds() {
        Chronology base = BaseDateTime.UTC;
        DateTimeField strictField = new BasicDateTimeField(DAY_OF_MONTH, 30, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, base);

        long instant = LocalDateTime.now().toDate().getTime();
        int newValue = 30;

        long result = lenientField.set(instant, newValue);

        assertEquals(newValue, lenientField.get(instant));
        assertEquals(result, instant);
    }

    @Test
    public void testLenientDateTimeFieldDoesNotModifyValueWhenWithinBounds() {
        Chronology base = BaseDateTime.UTC;
        DateTimeField strictField = new BasicDateTimeField(DAY_OF_MONTH, 30, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, base);

        long instant = LocalDateTime.now().toDate().getTime();
        int newValue = 30;

        lenientField.set(instant, newValue);

        assertEquals(newValue, lenientField.get(instant));
        assertEquals(instant, lenientField.get(instant));
    }

    @Test
    public void testLenientDateTimeFieldGetValueWithinBounds() {
        Chronology base = BaseDateTime.UTC;
        DateTimeField field = new BasicDateTimeField(HOUR_OF_DAY, 12, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        long instant = LocalDateTime.now().toDate().getTime();

        int value = lenientField.get(instant);

        assertEquals(12, value);
    }

    @Test
    public void testLenientDateTimeFieldGetValueOutOfBounds() {
        Chronology base = BaseDateTime.UTC;
        DateTimeField field = new BasicDateTimeField(HOUR_OF_DAY, 23, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        long instant = LocalDateTime.now().toDate().getTime();

        int value = lenientField.get(instant);

        assertEquals(23, value);
    }

    @Test
    public void testLenientDateTimeFieldAddsValue() {
        Chronology base = BaseDateTime.UTC;
        DateTimeField field = new BasicDateTimeField(MINUTE_OF_HOUR, 30, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        long instant = LocalDateTime.now().toDate().getTime();
        int value = 10;

        long result = lenientField.set(instant, value);

        DateTimeField expectedField = new BasicDateTimeField(MINUTE_OF_HOUR, 40, base);
        assertEquals(expectedField.get(instant), result);
    }

    @Test
    public void testLenientDateTimeFieldAddsValueWithNegativeDifference() {
        Chronology base = BaseDateTime.UTC;
        DateTimeField field = new BasicDateTimeField(MINUTE_OF_HOUR, 30, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        long instant = LocalDateTime.now().toDate().getTime();
        int value = -10;

        long result = lenientField.set(instant, value);

        DateTimeField expectedField = new BasicDateTimeField(MINUTE_OF_HOUR, 20, base);
        assertEquals(expectedField.get(instant), result);
    }

    @Test
    public void testLenientDateTimeFieldAddsValueWithZeroDifference() {
        Chronology base = BaseDateTime.UTC;
        DateTimeField field = new BasicDateTimeField(MINUTE_OF_HOUR, 30, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        long instant = LocalDateTime.now().toDate().getTime();
        int value = 0;

        long result = lenientField.set(instant, value);

        assertEquals(instant, result);
    }

    @Test
    public void testLenientDateTimeFieldAddsValueWithMaximumDifference() {
        Chronology base = BaseDateTime.UTC;
        DateTimeField field = new BasicDateTimeField(SECOND_OF_MINUTE, 59, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        long instant = LocalDateTime.now().toDate().getTime();
        int value = 60;

        long result = lenientField.set(instant, value);

        DateTimeField expectedField = new BasicDateTimeField(SECOND_OF_MINUTE, 0, base);
        assertEquals(expectedField.get(instant + 1000), result);
    }

    @Test
    public void testLenientDateTimeFieldAddsValueWithMinimumDifference() {
        Chronology base = BaseDateTime.UTC;
        DateTimeField field = new BasicDateTimeField(SECOND_OF_MINUTE, 59, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        long instant = LocalDateTime.now().toDate().getTime();
        int value = -60;

        long result = lenientField.set(instant, value);

        DateTimeField expectedField = new BasicDateTimeField(SECOND_OF_MINUTE, 60, base);
        assertEquals(expectedField.get(instant - 1000), result);
    }

    @Test
    public void testLenientDateTimeFieldWithOffset() {
        Chronology base = BaseDateTime.UTC.withZone(ZoneOffset.UTC);
        DateTimeField field = new BasicDateTimeField(HOUR_OF_DAY, 12, base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        long instant = LocalDateTime.now().toDate().getTime();
        int newValue = 13;

        long result = lenientField.set(instant, newValue);

        DateTimeField expectedField = new BasicDateTimeField(HOUR_OF_DAY, 13, base);
        assertEquals(expectedField.get(instant), result);
    }
}