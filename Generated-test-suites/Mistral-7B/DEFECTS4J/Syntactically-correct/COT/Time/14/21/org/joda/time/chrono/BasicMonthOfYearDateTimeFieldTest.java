import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.JodaTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.chrono.BasicChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        long instant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-03-31T23:59:59.999"));

        int expected = 3;
        int actual = field.get(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        long instant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-03-31T23:59:59.999"));

        long expected = JodaTime.readMilliseconds(LocalDateTime.parse("2021-04-30T23:59:59.999"));
        long actual = field.add(instant, 1);
        assertEquals(expected, actual);

        long expected2 = JodaTime.readMilliseconds(LocalDateTime.parse("2021-02-28T23:59:59.999"));
        long actual2 = field.add(instant, -1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        long instant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-13-31T23:59:59.999"));

        long expected = JodaTime.readMilliseconds(LocalDateTime.parse("2021-01-31T23:59:59.999"));
        long actual = field.addWrapField(instant, 12);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        long minuendInstant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-03-31T23:59:59.999"));
        long subtrahendInstant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-04-30T23:59:59.999"));

        long expected = 1;
        long actual = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, actual);

        long minuendInstant2 = JodaTime.readMilliseconds(LocalDateTime.parse("2021-03-31T23:59:59.999"));
        long subtrahendInstant2 = JodaTime.readMilliseconds(LocalDateTime.parse("2021-02-28T23:59:59.999"));

        long expected2 = 1;
        long actual2 = field.getDifferenceAsLong(minuendInstant2, subtrahendInstant2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        long instant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-03-31T23:59:59.999"));

        long expected = JodaTime.readMilliseconds(LocalDateTime.parse("2021-04-30T23:59:59.999"));
        long actual = field.set(instant, 4);
        assertEquals(expected, actual);

        long expected2 = JodaTime.readMilliseconds(LocalDateTime.parse("2021-02-28T23:59:59.999"));
        long actual2 = field.set(instant, 2);
        assertEquals(expected2, actual2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        long instant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-03-31T23:59:59.999"));

        field.set(instant, 13);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        DurationField expected = field.getRangeDurationField();
        DurationField actual = new DurationField(DateTimeFieldType.years(), 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        long instant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-02-29T23:59:59.999"));

        boolean expected = true;
        boolean actual = field.isLeap(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        long instant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-02-29T23:59:59.999"));

        int expected = 1;
        int actual = field.getLeapAmount(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        DurationField expected = field.getLeapDurationField();
        DurationField actual = new DurationField(DateTimeFieldType.days(), 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        int expected = DateTimeConstants.JANUARY;
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        int expected = 12;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        long instant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-03-31T23:59:59.999"));

        long expected = JodaTime.readMilliseconds(LocalDateTime.parse("2021-03-31T00:00:00.000"));
        long actual = field.roundFloor(instant);
        assertEquals(expected, actual);

        long expected2 = JodaTime.readMilliseconds(LocalDateTime.parse("2021-03-31T00:00:00.001"));
        long actual2 = field.roundFloor(instant + 1);
        assertEquals(expected, actual2);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO"), 2);
        long instant = JodaTime.readMilliseconds(LocalDateTime.parse("2021-03-31T23:59:59.999"));

        long expected = 0;
        long actual = field.remainder(instant);
        assertEquals(expected, actual);

        long expected2 = 1;
        long actual2 = field.remainder(instant + 1);
        assertEquals(expected2, actual2);
    }
}