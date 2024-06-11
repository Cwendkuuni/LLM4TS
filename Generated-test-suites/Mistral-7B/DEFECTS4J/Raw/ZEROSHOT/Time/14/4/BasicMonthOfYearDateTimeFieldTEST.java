import org.joda.time.BaseTestCase;
import org.joda.time. Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.field.MonthOfYearDateTimeField;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.test.TestUtils;

public class BasicMonthOfYearDateTimeFieldTest extends BaseTestCase {

    private static final DateTimeFormat DF = DateTimeFormat.forPattern("yyyy-MM-dd");

    @org.junit.Before
    public void setUp() {
        DateTimeUtils.initVM();
    }

    @org.junit.Test
    public void testBasic() {
        Chronology chrono = DateTimeUtils.getUTC();
        MonthOfYearDateTimeField testField = new MonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);

        // Get
        int month = testField.get(0L);
        assertEquals(1, month);

        long instant = DF.parseMillis("2000-01-31");
        month = testField.get(instant);
        assertEquals(1, month);

        // Set
        long newInstant = testField.set(instant, 2);
        assertEquals("2000-02-28", DF.print(newInstant));

        // Add
        newInstant = testField.add(instant, 1);
        assertEquals("2000-02-29", DF.print(newInstant));

        // Add wrap
        newInstant = testField.addWrapField(instant, 12);
        assertEquals("2000-03-01", DF.print(newInstant));

        // Add with length
        newInstant = testField.add(instant, new DurationField(12, DateTimeFieldType.months()), false);
        assertEquals("2000-03-01", DF.print(newInstant));

        // Add with amount
        newInstant = testField.add(instant, 48);
        assertEquals("2004-02-29", DF.print(newInstant));

        // Add with partial
        ReadablePartial partial = new DateTimeFieldTestPartial();
        partial.set(DateTimeFieldType.monthOfYear(), 1);
        newInstant = testField.add(instant, partial, 48, DateTimeFieldType.months());
        assertEquals("2008-02-29", DF.print(newInstant));

        // Add with invalid amount
        try {
            testField.add(instant, 366);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Difference
        long diff = testField.getDifferenceAsLong(instant, newInstant);
        assertEquals(48, diff);

        // Leap
        newInstant = DF.parseMillis("2000-02-29");
        assertTrue(testField.isLeap(newInstant));
        assertEquals(1, testField.getLeapAmount(newInstant));
    }

    @org.junit.Test
    public void testGregorian() {
        Chronology chrono = DateTimeUtils.gregorianUTC();
        MonthOfYearDateTimeField testField = new MonthOfYearDateTimeField(chrono, DateTimeConstants.APRIL);

        // Get
        int month = testField.get(0L);
        assertEquals(4, month);

        long instant = DF.parseMillis("2000-04-30");
        month = testField.get(instant);
        assertEquals(4, month);

        // Set
        long newInstant = testField.set(instant, 6);
        assertEquals("2000-06-30", DF.print(newInstant));

        // Add
        newInstant = testField.add(instant, 1);
        assertEquals("2000-05-31", DF.print(newInstant));

        // Add wrap
        newInstant = testField.addWrapField(instant, 12);
        assertEquals("2001-01-01", DF.print(newInstant));

        // Add with length
        newInstant = testField.add(instant, new DurationField(12, DateTimeFieldType.months()), false);
        assertEquals("2001-01-01", DF.print(newInstant));

        // Add with amount
        newInstant = testField.add(instant, 48);
        assertEquals("2004-04-30", DF.print(newInstant));

        // Add with partial
        ReadablePartial partial = new DateTimeFieldTestPartial();
        partial.set(DateTimeFieldType.monthOfYear(), 4);
        newInstant = testField.add(instant, partial, 48, DateTimeFieldType.months());
        assertEquals("2008-04-30", DF.print(newInstant));

        // Add with invalid amount
        try {
            testField.add(instant, 366);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Difference
        long diff = testField.getDifferenceAsLong(instant, newInstant);
        assertEquals(48, diff);

        // Leap
        newInstant = DF.parseMillis("2000-04-30");
        assertFalse(testField.isLeap(newInstant));
        assertEquals(0, testField.getLeapAmount(newInstant));

        newInstant = DF.parseMillis("2004-04-30");
        assertTrue(testField.isLeap(newInstant));
        assertEquals(1, testField.getLeapAmount(newInstant));
    }

    @org.junit.Test
    public void testJulian() {
        Chronology chrono = DateTimeUtils.julianDay();
        MonthOfYearDateTimeField testField = new MonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);

        // Get
        int month = testField.get(0L);
        assertEquals(1, month);

        long instant = DF.parseMillis("4712-01-01");
        month = testField.get(instant);
        assertEquals(1, month);

        // Set
        long newInstant = testField.set(instant, 2);
        assertEquals("4712-02-28", DF.print(newInstant));

        // Add
        newInstant = testField.add(instant, 1);
        assertEquals("4712-02-29", DF.print(newInstant));

        // Add wrap
        newInstant = testField.addWrapField(instant, 12);
        assertEquals("4713-01-01", DF.print(newInstant));

        // Add with length
        newInstant = testField.add(instant, new DurationField(12, DateTimeFieldType.months()), false);
        assertEquals("4713-01-01", DF.print(newInstant));

        // Add with amount
        newInstant = testField.add(instant, 48);
        assertEquals("5184-01-30", DF.print(newInstant));

        // Add with partial
        ReadablePartial partial = new DateTimeFieldTestPartial();
        partial.set(DateTimeFieldType.monthOfYear(), 1);
        newInstant = testField.add(instant, partial, 48, DateTimeFieldType.months());
        assertEquals("5821-01-30", DF.print(newInstant));

        // Add with invalid amount
        try {
            testField.add(instant, 13655);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Difference
        long diff = testField.getDifferenceAsLong(instant, newInstan);
        assertEquals(48 * 36524 + 28 + 1, diff);

        // Leap
        newInstant = DF.parseMillis("4712-01-31");
        assertTrue(testField.isLeap(newInstant));
        assertEquals(1, testField.getLeapAmount(newInstant));
    }

    @org.junit.Test
    public void testLeapYear() {
        Chronology chrono = DateTimeUtils.getUTC();
        MonthOfYearDateTimeField testField = new MonthOfYearDateTimeField(chrono, DateTimeConstants.FEBRUARY);

        // Get
        int month = testField.get(0L);
        assertEquals(2, month);

        long instant = DF.parseMillis("2000-02-29");
        month = testField.get(instant);
        assertEquals(2, month);

        // Set
        long newInstant = testField.set(instant, 7);
        assertEquals("2000-07-31", DF.print(newInstant));

        // Add
        newInstant = testField.add(instant, 1);
        assertEquals("2000-03-01", DF.print(newInstant));

        // Add wrap
        newInstant = testField.addWrapField(instant, 12);
        assertEquals("2001-02-29", DF.print(newInstant));

        // Add with length
        newInstant = testField.add(instant, new DurationField(12, DateTimeFieldType.months()), false);
        assertEquals("2001-02-29", DF.print(newInstant));

        // Add with amount
        newInstant = testField.add(instant, 47);
        assertEquals("2001-03-31", DF.print(newInstant));

        // Add with partial
        ReadablePartial partial = new DateTimeFieldTestPartial();
        partial.set(DateTimeFieldType.monthOfYear(), 2);
        newInstant = testField.add(instant, partial, 47, DateTimeFieldType.months());
        assertEquals("2006-03-31", DF.print(newInstant));

        // Add with invalid amount
        try {
            testField.add(instant, 1366);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Difference
        long diff = testField.getDifferenceAsLong(instant, newInstant);
        assertEquals(47, diff);

        // Leap
        assertFalse(testField.isLeap(instant + 1));
        assertEquals(0, testField.getLeapAmount(instant + 1));

        assertTrue(testField.isLeap(instant));
        assertEquals(1, testField.getLeapAmount(instant));
    }

    @org.junit.Test
    public void testSetMax() {
        Chronology chrono = DateTimeUtils.getUTC();
        MonthOfYearDateTimeField testField = new MonthOfYearDateTimeField(chrono, DateTimeConstants.DECEMBER);

        // Get
        int month = testField.get(0L);
        assertEquals(12, month);

        // Set
        long newInstant = testField.set(0L, 13);
        assertEquals(0, newInstant);
        month = testField.get(0L);
        assertEquals(1, month);

        newInstant = testField.set(0L, 0);
        assertEquals(0, newInstant);
        month = testField.get(0L);
        assertEquals(1, month);

        newInstant = testField.set(0L, 13);
        assertEquals(0, newInstant);
        month = testField.get(0L);
        assertEquals(1, month);
    }

    @org.junit.Test
    public void testSetMin() {
        Chronology chrono = DateTimeUtils.getUTC();
        MonthOfYearDateTimeField testField = new MonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);

        // Get
        int month = testField.get(0L);
        assertEquals(1, month);

        // Set
        long newInstant = testField.set(Long.MIN_VALUE, 13);
        assertEquals(0, newInstant);
        month = testField.get(newInstant);
        assertEquals(1, month);

        newInstant = testField.set(Long.MIN_VALUE, 0);
        assertEquals(0, newInstant);
        month = testField.get(newInstant);
        assertEquals(1, month);

        newInstant = testField.set(Long.MIN_VALUE, 13);
        assertEquals(0, newInstant);
        month = testField.get(newInstan);
        assertEquals(1, month);
    }

    //-----------------------------------------------------------------------
    private static class DateTimeFieldTestPartial implements ReadablePartial {
        private int fieldIndex = DateTimeFieldType.monthOfYear().getIndex();

        public void add(int value) {
            fail();
        }

        public void add(long value, DurationField field, boolean wrap) {
            fail();
        }

        public void add(long value, int lengthField, boolean wrap) {
            fail();
        }

        public int getFieldType() {
            return DateTimeFieldType.monthOfYear();
        }

        public int getValue() {
            return get(DateTimeFieldType.monthOfYear());
        }

        public void setValue(int value) {
            set(DateTimeFieldType.monthOfYear(), value);
        }

        public int getSize() {
            return 1;
        }

        public int getIndex() {
            return fieldIndex;
        }

        public void set(int field, int value) {
            if (field != getFieldType().getIndex()) {
                fail();
            }
            set(value);
        }

        private int value;

        public int get(int field) {
            if (field != getFieldType().getIndex()) {
                fail();
            }
            return value;
        }

        public void set(int value) {
            this.value = value;
        }
    }
}