import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientYear;
    private LenientDateTimeField lenientMonthOfYear;
    private LenientDateTimeField lenientDayOfMonth;
    private LenientDateTimeField lenientHourOfDay;
    private LenientDateTimeField lenientMinuteOfHour;

    @Before
    public void setup() {
        Chronology utc = DateTimeUtils.UTC;
        lenientYear = new LenientDateTimeField(DateTimeFieldType.year(), utc);
        lenientMonthOfYear = new LenientDateTimeField(DateTimeFieldType.monthOfYear(), utc);
        lenientDayOfMonth = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), utc);
        lenientHourOfDay = new LenientDateTimeField(DateTimeFieldType.hourOfDay(), utc);
        lenientMinuteOfHour = new LenientDateTimeField(DateTimeFieldType.minuteOfHour(), utc);
    }

    @Test
    public void testIsLenient() {
        assertEquals(true, lenientYear.isLenient());
        assertEquals(true, lenientMonthOfYear.isLenient());
        assertEquals(true, lenientDayOfMonth.isLenient());
        assertEquals(true, lenientHourOfDay.isLenient());
        assertEquals(true, lenientMinuteOfHour.isLenient());
    }

    @Test
    public void testGetInstance() {
        Chronology gregorian = DateTimeUtils.GREGORIAN;
        DateTimeField strictYear = DateTimeFieldType.year();
        DateTimeField strictMonthOfYear = DateTimeFieldType.monthOfYear();
        DateTimeField strictDayOfMonth = DateTimeFieldType.dayOfMonth();
        DateTimeField strictHourOfDay = DateTimeFieldType.hourOfDay();
        DateTimeField strictMinuteOfHour = DateTimeFieldType.minuteOfHour();

        // Test with strict fields
        DateTimeField lenientYearFromStrict = LenientDateTimeField.getInstance(strictYear, gregorian);
        assertEquals(lenientYear, lenientYearFromStrict);

        DateTimeField lenientMonthOfYearFromStrict = LenientDateTimeField.getInstance(strictMonthOfYear, gregorian);
        assertEquals(lenientMonthOfYear, lenientMonthOfYearFromStrict);

        DateTimeField lenientDayOfMonthFromStrict = LenientDateTimeField.getInstance(strictDayOfMonth, gregorian);
        assertEquals(lenientDayOfMonth, lenientDayOfMonthFromStrict);

        DateTimeField lenientHourOfDayFromStrict = LenientDateTimeField.getInstance(strictHourOfDay, gregorian);
        assertEquals(lenientHourOfDay, lenientHourOfDayFromStrict);

        DateTimeField lenientMinuteOfHourFromStrict = LenientDateTimeField.getInstance(strictMinuteOfHour, gregorian);
        assertEquals(lenientMinuteOfHour, lenientMinuteOfHourFromStrict);

        // Test with existing lenient fields
        DateTimeField lenientYearFromLenient = LenientDateTimeField.getInstance(lenientYear, gregorian);
        assertEquals(lenientYear, lenientYearFromLenient);

        DateTimeField lenientMonthOfYearFromLenient = LenientDateTimeField.getInstance(lenientMonthOfYear, gregorian);
        assertEquals(lenientMonthOfYear, lenientMonthOfYearFromLenient);

        DateTimeField lenientDayOfMonthFromLenient = LenientDateTimeField.getInstance(lenientDayOfMonth, gregorian);
        assertEquals(lenientDayOfMonth, lenientDayOfMonthFromLenient);

        DateTimeField lenientHourOfDayFromLenient = LenientDateTimeField.getInstance(lenientHourOfDay, gregorian);
        assertEquals(lenientHourOfDay, lenientHourOfDayFromLenient);

        DateTimeField lenientMinuteOfHourFromLenient = LenientDateTimeField.getInstance(lenientMinuteOfHour, gregorian);
        assertEquals(lenientMinuteOfHour, lenientMinuteOfHourFromLenient);

        // Test with null
        assertNull(LenientDateTimeField.getInstance(null, gregorian));
    }

    @Test
    public void testSet() {
        long invalidDateInUtc = -1; // Invalid date
        long validDateInUtc = 1609459200000L; // 2021-01-01 12:00:00 UTC
        long validDateInLocal = 1609459600000L; // 2021-01-01 13:00:00 CET

        MutableDateTime invalidDateTime = new MutableDateTime(invalidDateInUtc);
        MutableDateTime validDateTime = new MutableDateTime(validDateInLocal);

        MutableLocalDateTime invalidLocalDateTime = new MutableLocalDateTime(invalidDateTime);
        MutableLocalDateTime validLocalDateTime = new MutableLocalDateTime(validDateTime);

        try {
            long lenientValue = lenientYear.set(validDateInUtc, 2);
            assertEquals(validDateInUtc + 2, lenientValue);
            assertEquals(validDateTime.getYear(), lenientYear.get(lenientValue));
            assertEquals(validDateTime.getYear(), lenientYear.get(lenientValue));

            lenientValue = lenientYear.set(invalidDateInUtc, 2);
            assertEquals(validDateInUtc + 2, lenientValue); // Addition occurs instead of setting
            assertEquals(validDateTime.getYear() + 1, lenientYear.get(lenientValue));

            lenientValue = lenientMonthOfYear.set(validDateInUtc, 13);
            assertEquals(validDateInUtc, lenientValue); // Valid value, no change
            assertEquals(validDateTime.getMonthOfYear(), lenientMonthOfYear.get(lenientValue));

            lenientValue = lenientMonthOfYear.set(validDateInUtc, 0);
            assertEquals(validDateInUtc, lenientValue); // Valid value, no change

            lenientValue = lenientDayOfMonth.set(validDateInUtc, 32);
            assertEquals(validDateInUtc, lenientValue); // Valid value, no change

            lenientValue = lenientDayOfMonth.set(validDateInUtc, 0);
            assertEquals(validDateInUtc, lenientValue); // Valid value, no change

            lenientValue = lenientHourOfDay.set(validDateInUtc, 25);
            assertEquals(validDateInUtc, lenientValue); // Valid value, no change

            lenientValue = lenientHourOfDay.set(validDateInUtc, 24);
            assertEquals(validDateInUtc, lenientValue); // Valid value, no change

            lenientValue = lenientMinuteOfHour.set(validDateInUtc, 60);
            assertEquals(validDateInUtc, lenientValue); // Valid value, no change

            lenientValue = lenientMinuteOfHour.set(validDateInUtc, 61);
            assertEquals(validDateInUtc + 1, lenientValue); // Addition occurs instead of setting
            assertEquals(validDateTime.getMinuteOfHour() + 1, lenientMinuteOfHour.get(lenientValue));

            lenientValue = lenientMinuteOfHour.set(invalidDateInUtc, 60);
            assertEquals(validDateInUtc + 1, lenientValue); // Addition occurs instead of setting
            assertEquals(validDateTime.getMinuteOfHour() + 1, lenientMinuteOfHour.get(lenientValue));

            lenientValue = lenientMinuteOfHour.set(invalidDateInUtc, -1);
            assertEquals(validDateInUtc + 1, lenientValue); // Addition occurs instead of setting
            assertEquals(validDateTime.getMinuteOfHour() + 1, lenientMinuteOfHour.get(lenientValue));

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOutOfBoundsNegative() {
        long invalidDateInUtc = 1609459200000L; // 2021-01-01 12:00:00 UTC

        MutableDateTime invalidDateTime = new MutableDateTime(invalidDateInUtc);
        MutableLocalDateTime invalidLocalDateTime = new MutableLocalDateTime(invalidDateTime);

        long lenientValue = lenientMonthOfYear.set(invalidDateInUtc, -1);
        assertThrows(IllegalArgumentException.class, () -> lenientValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOutOfBoundsPositive() {
        long invalidDateInUtc = 1609459200000L; // 2021-01-01 12:00:00 UTC

        MutableDateTime invalidDateTime = new MutableDateTime(invalidDateInUtc);
        MutableLocalDateTime invalidLocalDateTime = new MutableLocalDateTime(invalidDateTime);

        long lenientValue = lenientMinuteOfHour.set(invalidDateInUtc, 121);
        assertThrows(IllegalArgumentException.class, () -> lenientValue);
    }
}