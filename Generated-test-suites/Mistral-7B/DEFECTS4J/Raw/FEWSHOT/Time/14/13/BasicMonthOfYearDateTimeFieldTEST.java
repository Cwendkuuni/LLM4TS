package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

/**
 * Test cases for {@link BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    /** The chronology used for testing. */
    private static final BasicChronology ISO_8601 = DateTimeUtils.getISO8601LocalDate();

    /** A test instance. */
    private static final BasicMonthOfYearDateTimeField MONTH_OF_YEAR = ISO_8601.monthOfYear();

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(
                chronology, DateTimeConstants.JANUARY);
        assertEquals(DateTimeFieldType.monthOfYear(), monthOfYear.getFieldType());
        assertEquals(chronology.getAverageMillisPerMonth(), monthOfYear.getMillisPerValue());
    }

    /**
     * Tests the get method.
     */
    @Test
    public void testGet() {
        int month = MONTH_OF_YEAR.get(ISO_8601.millis("2008-02-29T12:00:00.000Z"));
        assertEquals(2, month);
    }

    /**
     * Tests the add method.
     */
    @Test
    public void testAdd() {
        long instant = ISO_8601.millis("2008-02-29T12:00:00.000Z");
        int months = 1;
        long result = MONTH_OF_YEAR.add(instant, months);
        LocalDateTime expected = new LocalDateTime(ISO_8601, LocalDate.of(2008, 3, 29), new LocalTime(12, 0, 0, 0));
        assertEquals(expected.getMillis(), result);
    }

    /**
     * Tests the add method with negative months.
     */
    @Test
    public void testAddNegativeMonths() {
        long instant = ISO_8601.millis("2008-03-01T12:00:00.000Z");
        int months = -1;
        long result = MONTH_OF_YEAR.add(instant, months);
        LocalDateTime expected = new LocalDateTime(ISO_8601, LocalDate.of(2008, 2, 29), new LocalTime(12, 0, 0, 0));
        assertEquals(expected.getMillis(), result);
    }

    /**
     * Tests the add method with a large number of months.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddLargeNumberOfMonths() {
        long instant = ISO_8601.millis("2008-02-29T12:00:00.000Z");
        int months = 1234;
        MONTH_OF_YEAR.add(instant, months);
    }

    /**
     * Tests the add method with a {@link ReadablePartial} object.
     */
    @Test
    public void testAddReadablePartial() {
        long instant = ISO_8601.millis("2008-02-29T12:00:00.000Z");
        ReadablePartial partial = DateTimeUtils.localDate(ISO_8601, 2008, 3, 30);
        int[] values = new int[1];
        int[] result = MONTH_OF_YEAR.add(partial, 0, values, 1);
        assertEquals(3, result[0]);
        LocalDateTime expected = new LocalDateTime(ISO_8601, LocalDate.of(2008, 3, 30), new LocalTime(12, 0, 0, 0));
        assertEquals(expected.getMillis(), result[1]);
    }

    /**
     * Tests the addWrapField method.
     */
    @Test
    public void testAddWrapField() {
        long instant = ISO_8601.millis("2008-02-29T12:00:00.000Z");
        int months = 13;
        long result = MONTH_OF_YEAR.addWrapField(instant, months);
        LocalDateTime expected = new LocalDateTime(ISO_8601, LocalDate.of(2009, 2, 28), new LocalTime(12, 0, 0, 0));
        assertEquals(expected.getMillis(), result);
    }

    /**
     * Tests the getDifferenceAsLong method.
     */
    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = ISO_8601.millis("2008-02-29T12:00:00.000Z");
        long instant2 = ISO_8601.millis("2008-03-31T12:00:00.000Z");
        long difference = MONTH_OF_YEAR.getDifferenceAsLong(instant1, instant2);
        assertEquals(1, difference);
    }

    /**
     * Tests the set method.
     */
    @Test
    public void testSet() {
        long instant = ISO_8601.millis("2008-02-29T12:00:00.000Z");
        int month = 3;
        long result = MONTH_OF_YEAR.set(instant, month);
        LocalDateTime expected = new LocalDateTime(ISO_8601, LocalDate.of(2008, 3, 29), new LocalTime(12, 0, 0, 0));
        assertEquals(expected.getMillis(), result);
    }

    /**
     * Tests the set method with an invalid month value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        long instant = ISO_8601.millis("2008-02-29T12:00:00.000Z");
        int month = 13;
        MONTH_OF_YEAR.set(instant, month);
    }

    /**
     * Tests the getRangeDurationField method.
     */
    @Test
    public void testGetRangeDurationField() {
        DurationField rangeField = MONTH_OF_YEAR.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), rangeField.getFieldType());
        assertEquals(1, rangeField.getMillisPerValue());
    }

    /**
     * Tests the isLeap method.
     */
    @Test
    public void testIsLeap() {
        long instant = ISO_8601.millis("2008-02-29T12:00:00.000Z");
        assertTrue(MONTH_OF_YEAR.isLeap(instant));
    }

    /**
     * Tests the getLeapAmount method.
     */
    @Test
    public void testGetLeapAmount() {
        long instant = ISO_8601.millis("2008-02-29T12:00:00.000Z");
        assertEquals(1, MONTH_OF_YEAR.getLeapAmount(instant));
    }

    /**
     * Tests the getLeapDurationField method.
     */
    @Test
    public void testGetLeapDurationField() {
        DurationField leapField = MONTH_OF_YEAR.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), leapField.getFieldType());
        assertEquals(1, leapField.getMillisPerValue());
    }

    /**
     * Tests the getMinimumValue method.
     */
    @Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, MONTH_OF_YEAR.getMinimumValue());
    }

    /**
     * Tests the getMaximumValue method.
     */
    @Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.DECEMBER, MONTH_OF_YEAR.getMaximumValue());
    }

    /**
     * Tests the roundFloor method.
     */
    @Test
    public void testRoundFloor() {
        long instant = ISO_8601.millis("2008-03-01T12:00:00.000Z");
        long result = MONTH_OF_YEAR.roundFloor(instant);
        assertEquals(ISO_8601.getMonthOfYearMillis(2008, 3), result);
    }

    /**
     * Tests the remainder method.
     */
    @Test
    public void testRemainder() {
        long instant = ISO_8601.millis("2008-03-01T12:00:00.000Z");
        long result = MONTH_OF_YEAR.remainder(instant);
        assertEquals(0, result);
    }

    /**
     * Tests the serialization.
     */
    @Test
    public void testSerialization() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, DateTimeConstants.JANUARY);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(output);
        stream.writeObject(field);
        stream.close();
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        Object object = input.readObject();
        assertEquals(field, object);
    }
}