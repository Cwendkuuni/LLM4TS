import org.joda.time.BaseDateTime;
import org.joda.time.BaseDateTimeField;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ImmutableDateTime;
import org.joda.time.JodaTime;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicGet() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = JodaTime.getUTC().getMillis();
        int expectedMonth = DateTimeConstants.DECEMBER;
        int actualMonth = field.get(instant);
        assertEquals(expectedMonth, actualMonth);
    }

    @Test
    public void testBasicAdd() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = JodaTime.getUTC().getMillis();
        int monthsToAdd = 2;
        long expectedInstants = JodaTime.getUTC().getMillis() + (monthsToAdd * 30 * 24 * 60 * 60 * 1000);
        long actualInstants = field.add(instant, monthsToAdd).longValue();
        assertEquals(expectedInstants, actualInstants);
    }

    @Test
    public void testAddWrap() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = JodaTime.getUTC().getMillis();
        int monthsToAdd = 12;
        long expectedInstants = instant - (DateTimeConstants.DECEMBER * 31 * 24 * 60 * 60 * 1000);
        long actualInstants = field.addWrapField(instant, monthsToAdd).longValue();
        assertEquals(expectedInstants, actualInstants);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOutOfBounds() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = JodaTime.getUTC().getMillis();
        int monthsToAdd = Integer.MAX_VALUE + 1;
        field.add(instant, monthsToAdd);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = JodaTime.getUTC().getMillis();
        int month = Integer.MIN_VALUE;
        long expectedInstants = instant;
        long actualInstants = field.set(instant, month).longValue();
        assertEquals(expectedInstants, actualInstants);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth2() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = JodaTime.getUTC().getMillis();
        int month = Integer.MAX_VALUE + 1;
        long expectedInstants = instant;
        long actualInstants = field.set(instant, month).longValue();
        assertEquals(expectedInstants, actualInstants);
    }

    @Test
    public void testSet() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = JodaTime.getUTC().getMillis();
        int month = DateTimeConstants.APRIL;
        long expectedInstants = JodaTime.getUTC().getMillis() + (3 * 30 * 24 * 60 * 60 * 1000);
        long actualInstants = field.set(instant, month).longValue();
        assertEquals(expectedInstants, actualInstants);
    }

    @Test
    public void testGetDifference() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long minuendInstant = JodaTime.getUTC().getMillis();
        long subtrahendInstant = JodaTime.getUTC().plusDays(1).getMillis();
        int expectedMonths = 1;
        int actualMonths = field.getDifferenceAsInt(minuendInstant, subtrahendInstant);
        assertEquals(expectedMonths, actualMonths);
    }

    @Test
    public void testIsLeap() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = JodaTime.getUTC().getMillis();
        boolean expectedLeap = false;
        boolean actualLeap = field.isLeap(instant);
        assertEquals(expectedLeap, actualLeap);
    }

    @Test
    public void testIsLeapTrue() {
        Chronology chronology = new GregorianChronology();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);
        long instant = JodaTime.getUTC().getMillis();
        int year = JodaTime.getUTC().getYear();
        boolean expectedLeap = (year % 4 == 0 && year % 100 != 0 || year % 400 == 0);
        boolean actualLeap = field.isLeap(instant);
        assertEquals(expectedLeap, actualLeap);
    }

    @Test
    public void testGetLeapAmount() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = JodaTime.getUTC().getMillis();
        int expectedLeapAmount = 0;
        int actualLeapAmount = field.getLeapAmount(instant);
        assertEquals(expectedLeapAmount, actualLeapAmount);
    }

    @Test
    public void testGetLeapAmountTrue() {
        Chronology chronology = new GregorianChronology();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);
        long instant = JodaTime.getUTC().getMillis();
        int year = JodaTime.getUTC().getYear();
        int expectedLeapAmount = (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) ? 1 : 0;
        int actualLeapAmount = field.getLeapAmount(instant);
        assertEquals(expectedLeapAmount, actualLeapAmount);
    }

    @Test
    public void testGetMaxValue() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        int expectedMaxValue = DateTimeConstants.DECEMBER;
        int actualMaxValue = field.getMaximumValue();
        assertEquals(expectedMaxValue, actualMaxValue);
    }

    @Test
    public void testGetMinValue() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        int expectedMinValue = DateTimeConstants.JANUARY;
        int actualMinValue = field.getMinimumValue();
        assertEquals(expectedMinValue, actualMinValue);
    }

    @Test
    public void testGetRangeDurationField() {
        Chronology chronology = JodaTime.getUTC();
        BaseDateTimeField field = new MonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        DurationField expectedDurationField = new DurationField(DurationFieldType.YEARS, 1, false);
        DurationField actualDurationField = field.getRangeDurationField();
        assertEquals(expectedDurationField, actualDurationField);
    }
}