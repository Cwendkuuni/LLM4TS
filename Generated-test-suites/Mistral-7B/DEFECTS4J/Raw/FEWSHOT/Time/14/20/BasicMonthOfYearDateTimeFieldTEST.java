package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalDateTimeField;
import org.joda.time.MutableLocalTime;
import org.joda.time.TestCase;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.JulianDay;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

/**
 * Test cases for {@link BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    /** The chronology to use. */
    private static final Chronology CHRONOLOGY = new BasicChronology("UTC", "UTC") {
        @Override
        public DateTimeFieldType getDateTimeFieldType(int field) {
            return DateTimeFieldType.monthOfYear();
        }

        @Override
        public long getMillisPerDay() {
            return 86400000L;
        }

        @Override
        public int getDaysInYearMonth(int year, int month) {
            return DateTimeConstants.getDaysInMonth(year, month);
        }

        @Override
        public int getMaxMonth() {
            return 12;
        }

        @Override
        public boolean isLeapYear(int year) {
            return DateTimeConstants.isLeapYear(year);
        }
    };

    /** The test field. */
    private static final BasicMonthOfYearDateTimeField TEST_FIELD =
            new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);

    /**
     * Test the isLenient method.
     */
    @Test
    public void testIsLenient() {
        assertEquals(false, TEST_FIELD.isLenient());
    }

    /**
     * Test the get method.
     */
    @Test
    public void testGet() {
        String[] testValues = new String[] {"1970-01-31", "1970-02-29", "1970-02-28",
                "1970-03-31", "1970-04-30", "1970-05-31", "1970-12-31", "1970-01-31"};
        for (String testValue : testValues) {
            MutableDateTime testDateTime = new MutableDateTime();
            DateTimeFormat formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            LocalDate localDate = formatter.parseLocalDate(testValue);
            testDateTime.setDate(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
            assertEquals(TEST_FIELD.get(testDateTime.getMillis()),
                         Integer.parseInt(testValue.substring(5, 7)));
        }
    }

    /**
     * Test the add method with an integer.
     */
    @Test
    public void testAdd() {
        MutableDateTime testDateTime = new MutableDateTime();
        testDateTime.setDate(1970, 1, 31);

        int[] testValues = new int[] {-1, 0, 1, 12, 13};
        for (int testValue : testValues) {
            MutableDateTime resultDateTime = new MutableDateTime(TEST_FIELD.add(testDateTime.getMillis(), testValue));
            assertEquals(TEST_FIELD.get(resultDateTime.getMillis()), TEST_FIELD.get(testDateTime.getMillis()) + testValue);
            testDateTime.setDate(resultDateTime.getYear(), resultDateTime.getMonthOfYear(), resultDateTime.getDayOfMonth());
        }
    }

    /**
     * Test the add method with a long.
     */
    @Test
    public void testAddLong() {
        MutableDateTime testDateTime = new MutableDateTime();
        testDateTime.setDate(1970, 1, 31);

        long[] testValues = new long[] {-1L, 0L, 1L, Integer.MAX_VALUE, Long.MAX_VALUE};
        for (long testValue : testValues) {
            MutableDateTime resultDateTime = new MutableDateTime(TEST_FIELD.add(testDateTime.getMillis(), testValue));
            assertEquals(TEST_FIELD.get(resultDateTime.getMillis()), TEST_FIELD.get(testDateTime.getMillis()) + testValue);
            testDateTime.setDate(resultDateTime.getYear(), resultDateTime.getMonthOfYear(), resultDateTime.getDayOfMonth());
        }
    }

    /**
     * Test the add method with a {@link ReadablePartial}.
     */
    @Test
    public void testAddReadablePartial() {
        MutableDateTime testDateTime = new MutableDateTime();
        testDateTime.setDate(1970, 1, 31);

        // Test with a partially mutable field.
        MutableLocalDateTimeField mutableField = new MutableLocalDateTimeField(DateTimeFieldType.dayOfMonth(), CHRONOLOGY);
        LocalDateTime localDateTime = new LocalDateTime(testDateTime.toLocalDateTime());
        MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime(localDateTime);
        mutableLocalDateTime.setFieldValue(mutableField, 1);
        MutableLocalDateTime resultDateTime = new MutableLocalDateTime(TEST_FIELD.add(testDateTime.getMillis(), mutableLocalDateTime));
        assertEquals(TEST_FIELD.get(resultDateTime.getMillis()), TEST_FIELD.get(testDateTime.getMillis()) + 1);
        assertEquals(mutableField.getValue(resultDateTime), mutableField.getValue(mutableLocalDateTime) + 1);

        // Test with a fully mutable field.
        MutableLocalDateTimeField mutableField2 = new MutableLocalDateTimeField(DateTimeFieldType.monthOfYear(), CHRONOLOGY);
        LocalDateTime localDateTime2 = new LocalDateTime(testDateTime.toLocalDateTime());
        MutableLocalDateTime mutableLocalDateTime2 = new MutableLocalDateTime(localDateTime2);
        mutableLocalDateTime2.setFieldValue(mutableField2, 1);
        MutableLocalDateTime resultDateTime2 = new MutableLocalDateTime(TEST_FIELD.add(testDateTime.getMillis(), mutableLocalDateTime2));
        assertEquals(TEST_FIELD.get(resultDateTime2.getMillis()), TEST_FIELD.get(testDateTime.getMillis()) + 1);
        assertEquals(mutableField2.getValue(resultDateTime2), mutableField2.getValue(mutableLocalDateTime2) + 1);
    }

    /**
     * Test the addWrapField method.
     */
    @Test
    public void testAddWrapField() {
        MutableDateTime testDateTime = new MutableDateTime();
        testDateTime.setDate(1970, 1, 31);

        int[] testValues = new int[] {13, 14, 0};
        for (int testValue : testValues) {
            MutableDateTime resultDateTime = new MutableDateTime(TEST_FIELD.addWrapField(testDateTime.getMillis(), testValue));
            assertEquals(TEST_FIELD.get(resultDateTime.getMillis()), (testValue % CHRONOLOGY.getMaxMonth()) + 1);
            testDateTime.setDate(resultDateTime.getYear(), resultDateTime.getMonthOfYear(), resultDateTime.getDayOfMonth());
        }
    }

    /**
     * Test the getDifferenceAsLong method.
     */
    @Test
    public void testGetDifferenceAsLong() {
        MutableDateTime testDateTime1 = new MutableDateTime();
        MutableDateTime testDateTime2 = new MutableDateTime();
        testDateTime1.setDate(1970, 1, 31);
        testDateTime2.setDate(1971, 1, 31);

        int[] testValues = new int[] {-1, 0, 1};
        for (int testValue : testValues) {
            long testInstant1 = testDateTime1.getMillis();
            long testInstant2 = testDateTime2.getMillis();
            long difference = TEST_FIELD.getDifferenceAsLong(testInstant1, testInstant2);
            assertEquals(TEST_FIELD.get(testInstant1) - TEST_FIELD.get(testInstant2), testValue);
            assertEquals(difference, testValue);
        }
    }

    /**
     * Test the set method.
     */
    @Test
    public void testSet() {
        MutableDateTime testDateTime = new MutableDateTime();
        testDateTime.setDate(1970, 1, 31);

        int[] testValues = new int[] {-1, 0, 1, 13};
        for (int testValue : testValues) {
            MutableDateTime resultDateTime = new MutableDateTime(TEST_FIELD.set(testDateTime.getMillis(), testValue));
            assertEquals(TEST_FIELD.get(resultDateTime.getMillis()), testValue);
            assertEquals(resultDateTime.getYear(), testDateTime.getYear());
            assertEquals(resultDateTime.getMonthOfYear(), testValue);
            assertEquals(resultDateTime.getDayOfMonth(), 1);
        }

        // Test with an out of range value.
        MutableDateTime testDateTime3 = new MutableDateTime();
        testDateTime3.setDate(1970, 1, 31);
        MutableDateTime resultDateTime3 = new MutableDateTime();
        try {
            resultDateTime3 = new MutableDateTime(TEST_FIELD.set(testDateTime3.getMillis(), -1));
        } catch (IllegalArgumentException e) {
            // Expected.
        }
        assertNull(resultDateTime3);

        // Test with the maximum value.
        MutableDateTime testDateTime4 = new MutableDateTime();
        testDateTime4.setDate(1970, 1, 31);
        MutableDateTime resultDateTime4 = new MutableDateTime(TEST_FIELD.set(testDateTime4.getMillis(), Integer.MAX_VALUE));
        assertEquals(TEST_FIELD.get(resultDateTime4.getMillis()), Integer.MAX_VALUE);
        assertEquals(resultDateTime4.getYear(), testDateTime4.getYear());
        assertEquals(resultDateTime4.getMonthOfYear(), Integer.MAX_VALUE);
        assertEquals(resultDateTime4.getDayOfMonth(), 1);
    }

    /**
     * Test the getRangeDurationField method.
     */
    @Test
    public void testGetRangeDurationField() {
        DurationField result = TEST_FIELD.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), result.getType());
    }

    /**
     * Test the isLeap method.
     */
    @Test
    public void testIsLeap() {
        MutableDateTime testDateTime = new MutableDateTime();
        testDateTime.setDate(1970, 1, 31);

        int[] testValues = new int[] {1970, 1972, 1900};
        for (int testValue : testValues) {
            assertEquals(TEST_FIELD.isLeap(testDateTime.getMillis()), TEST_FIELD.isLeap(testValue));
            assertEquals(TEST_FIELD.isLeap(testDateTime.getMillis()), CHRONOLOGY.isLeapYear(testValue) && TEST_FIELD.get(testDateTime.getMillis()) == DateTimeConstants.JANUARY);
        }
    }

    /**
     * Test the getLeapAmount method.
     */
    @Test
    public void testGetLeapAmount() {
        MutableDateTime testDateTime = new MutableDateTime();
        testDateTime.setDate(1970, 1, 31);

        int[] testValues = new int[] {1970, 1972, 1900};
        for (int testValue : testValues) {
            assertEquals(TEST_FIELD.getLeapAmount(testDateTime.getMillis()), TEST_FIELD.isLeap(testDateTime.getMillis()) ? 1 : 0);
            assertEquals(TEST_FIELD.getLeapAmount(testDateTime.getMillis()), CHRONOLOGY.isLeapYear(testValue) && TEST_FIELD.get(testDateTime.getMillis()) == DateTimeConstants.JANUARY ? 1 : 0);
        }
    }

    /**
     * Test the getLeapDurationField method.
     */
    @Test
    public void testGetLeapDurationField() {
        DurationField result = TEST_FIELD.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), result.getType());
    }

    /**
     * Test the getMinimumValue method.
     */
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, TEST_FIELD.getMinimumValue());
    }

    /**
     * Test the getMaximumValue method.
     */
    @Test
    public void testGetMaximumValue() {
        assertEquals(12, TEST_FIELD.getMaximumValue());
    }

    /**
     * Test the roundFloor method.
     */
    @Test
    public void testRoundFloor() {
        MutableDateTime testDateTime = new MutableDateTime();
        testDateTime.setDate(1970, 1, 31);

        int[] testValues = new int[] {1970, 1971, 1972};
        for (int testValue : testValues) {
            assertEquals(TEST_FIELD.roundFloor(testDateTime.getMillis()), new JulianDay(TEST_FIELD.get(testValue) - 1).getMillis());
        }
    }

    /**
     * Test the remainder method.
     */
    @Test
    public void testRemainder() {
        MutableDateTime testDateTime = new MutableDateTime();
        testDateTime.setDate(1970, 1, 31);

        int[] testValues = new int[] {1970, 1971, 1972};
        for (int testValue : testValues) {
            assertEquals(TEST_FIELD.remainder(testDateTime.getMillis()), TEST_FIELD.get(testDateTime.getMillis()) % TEST_FIELD.get(testValue));
        }
    }
}