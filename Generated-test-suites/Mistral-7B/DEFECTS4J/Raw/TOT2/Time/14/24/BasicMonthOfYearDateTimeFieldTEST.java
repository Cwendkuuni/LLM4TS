import org.junit.Test;
import org.joda.time.BasicDateTime;
import org.joda.time.ChronologyUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.format.DateTimeFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicGetSet() {
        BasicChronology chronology = new BasicChronology("TestChronology", DateTimeZone.UTC);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        assertNotNull(field);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(0, field.get(0));

        field.set(1, 2);
        assertEquals(2, field.get(1));
        LocalDateTime dt = new LocalDateTime(0, 1, 1, 0, 0);
        field.set(dt.toMilliseconds());
        assertEquals(1, field.get(dt.toMilliseconds()));

        LocalDateTime maxDate = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(3000, 12, 31), new LocalTime());
        field.set(maxDate.toMilliseconds());
        assertEquals(12, field.get(maxDate.toMilliseconds()));
    }

    @Test
    public void testAdd() {
        BasicChronology chronology = new BasicChronology("TestChronology", DateTimeZone.UTC);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long initial = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2022, 3, 1), new LocalTime()).toMilliseconds();
        long result = field.add(initial, 1);

        LocalDateTime expected = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2022, 4, 1), new LocalTime());
        assertEquals(expected.toMilliseconds(), result);

        result = field.add(initial, -1);
        LocalDateTime expectedMinusOne = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2022, 2, 28), new LocalTime());
        assertEquals(expectedMinusOne.toMilliseconds(), result); // note: assuming non-leap year

        result = field.add(initial, 12);
        LocalDateTime expectedPlusTwelve = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2023, 3, 1), new LocalTime());
        assertEquals(expectedPlusTwelve.toMilliseconds(), result);

        result = field.add(initial, -12);
        LocalDateTime expectedMinusTwelve = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2021, 11, 30), new LocalTime());
        assertEquals(expectedMinusTwelve.toMilliseconds(), result); // note: assuming non-leap year
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology("TestChronology", DateTimeZone.UTC);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long initial = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2022, 12, 31), new LocalTime()).toMilliseconds();
        long result = field.addWrapField(initial, 1);

        LocalDateTime expected = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2023, 1, 1), new LocalTime());
        assertEquals(expected.toMilliseconds(), result);

        result = field.addWrapField(initial, 12);
        LocalDateTime expectedPlusTwelve = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2024, 2, 1), new LocalTime());
        assertEquals(expectedPlusTwelve.toMilliseconds(), result);

        result = field.addWrapField(initial, -1);
        LocalDateTime expectedMinusOne = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2022, 11, 30), new LocalTime());
        assertEquals(expectedMinusOne.toMilliseconds(), result);

        result = field.addWrapField(initial, -12);
        LocalDateTime expectedMinusTwelve = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2021, 10, 31), new LocalTime());
        assertEquals(expectedMinusTwelve.toMilliseconds(), result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology("TestChronology", DateTimeZone.UTC);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long initial = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2022, 12, 31), new LocalTime()).toMilliseconds();
        long subtrahend = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2022, 11, 30), new LocalTime()).toMilliseconds();

        long result = field.getDifferenceAsLong(initial, subtrahend);
        assertEquals(30, result);

        result = field.getDifferenceAsLong(subtrahend, initial);
        assertEquals(-30, result);
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology("TestChronology", DateTimeZone.UTC);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);

        long initial = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2023, 2, 28), new LocalTime()).toMilliseconds();
        int leapAmount = field.getLeapAmount(initial);
        assertEquals(0, leapAmount);

        initial = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2024, 2, 29), new LocalTime()).toMilliseconds();
        leapAmount = field.getLeapAmount(initial);
        assertEquals(1, leapAmount);
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology("TestChronology", DateTimeZone.UTC);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);

        long initial = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2023, 2, 28), new LocalTime()).toMilliseconds();
        assertFalse(field.isLeap(initial));

        initial = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2024, 2, 29), new LocalTime()).toMilliseconds();
        assertTrue(field.isLeap(initial));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMonthValue() {
        BasicChronology chronology = new BasicChronology("TestChronology", DateTimeZone.UTC);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear().getValues().length + 1);
        assertThrows(IllegalArgumentException.class, () -> field.set(-1L, 0));
        assertThrows(IllegalArgumentException.class, () -> field.set(0, DateTimeFieldType.monthOfYear().getValues().length + 1));
    }

    @Test
    public void testToString() {
        BasicChronology chronology = new BasicChronology("TestChronology", DateTimeZone.UTC);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        String first = "monthOfYear(TestChronology, 0)";
        assertEquals(first, field.toString());

        LocalDateTime dt = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2022, 11, 21), new LocalTime());
        field.set(dt.toMilliseconds());
        String second = "monthOfYear(TestChronology, 11)";
        assertEquals(second, field.toString());

        LocalDateTime dtMax = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(3000, 12, 31), new LocalTime());
        field.set(dtMax.toMilliseconds());
        String third = "monthOfYear(TestChronology, 12)";
        assertEquals(third, field.toString());
    }

    @Test
    public void testSetInvalidMonthValue() {
        BasicChronology chronology = new BasicChronology("TestChronology", DateTimeZone.UTC);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear().getValues().length + 1);

        long invalidValue = new LocalDateTime(ChronologyUtils.getUTC(), new LocalDate(2022, 12, 31), new LocalTime()).toMilliseconds();
        assertThrows(IllegalArgumentException.class, () -> field.set(invalidValue, 0));
        assertThrows(IllegalArgumentException.class, () -> field.set(0, DateTimeFieldType.monthOfYear().getValues().length + 1));
    }
}