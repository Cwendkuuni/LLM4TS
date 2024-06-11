import org.joda.time.BaseDateTime;
import org.joda.time.BasicDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.YearMonth;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Chronology.getDefault(), 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);

        // Test valid cases
        long instant = BaseDateTime.UTC.getMillis();
        int expectedMonth = DateTimeConstants.MARCH;
        int actualMonth = field.get(instant);
        assertEquals(expectedMonth, actualMonth);

        // Test minimum value
        instant -= (2 * DateTimeConstants.MILLIS_PER_DAY);
        expectedMonth = DateTimeConstants.JANUARY;
        actualMonth = field.get(instant);
        assertEquals(expectedMonth, actualMonth);

        // Test maximum value
        instant += (12 * DateTimeConstants.MILLIS_PER_MONTH);
        expectedMonth = DateTimeConstants.DECEMBER;
        actualMonth = field.get(instant);
        assertEquals(expectedMonth, actualMonth);
    }

    @Test
    public void testAdd() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);

        // Test valid cases
        long instant = BaseDateTime.UTC.getMillis();
        int monthsToAdd = 2;
        long expectedResult = instant + (monthsToAdd * DateTimeConstants.MILLIS_PER_MONTH);
        long actualResult = field.add(instant, monthsToAdd);
        assertEquals(expectedResult, actualResult);

        // Test minimum value
        instant -= (2 * DateTimeConstants.MILLIS_PER_DAY);
        monthsToAdd = 1;
        expectedResult = instant + (monthsToAdd * DateTimeConstants.MILLIS_PER_MONTH);
        actualResult = field.add(instant, monthsToAdd);
        assertEquals(expectedResult, actualResult);

        // Test maximum value
        instant += (12 * DateTimeConstants.MILLIS_PER_MONTH);
        monthsToAdd = -1;
        expectedResult = instant + (monthsToAdd * DateTimeConstants.MILLIS_PER_MONTH);
        actualResult = field.add(instant, monthsToAdd);
        assertEquals(expectedResult, actualResult);

        // Test negative months
        instant += (DateTimeConstants.MILLIS_PER_MONTH * 2);
        monthsToAdd = -3;
        expectedResult = instant + (monthsToAdd * DateTimeConstants.MILLIS_PER_MONTH);
        actualResult = field.add(instant, monthsToAdd);
        assertEquals(expectedResult, actualResult);

        // Test adding months in a leap year
        Chronology leapChrono = new GregorianChronology();
        BasicMonthOfYearDateTimeField leapField = new BasicMonthOfYearDateTimeField(leapChrono, 2);
        instant = BaseDateTime.UTC.getMillis();
        int leapYearMonthToAdd = 2;
        expectedResult = instant + (leapYearMonthToAdd * DateTimeConstants.MILLIS_PER_MONTH);
        actualResult = leapField.add(instant, leapYearMonthToAdd);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAddWrapField() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);

        long instant = BaseDateTime.UTC.getMillis();
        int monthsToAdd = 13;
        long expectedResult = instant + (monthsToAdd * DateTimeConstants.MILLIS_PER_MONTH);
        long actualResult = field.addWrapField(instant, monthsToAdd);
        assertEquals(expectedResult, actualResult);

        // Test negative months
        instant += (DateTimeConstants.MILLIS_PER_MONTH * 3);
        monthsToAdd = -10;
        expectedResult = instant + (monthsToAdd * DateTimeConstants.MILLIS_PER_MONTH);
        actualResult = field.addWrapField(instant, monthsToAdd);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetDifference() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);

        long minuendInstant = BaseDateTime.UTC.getMillis() - (2 * DateTimeConstants.MILLIS_PER_MONTH);
        long subtrahendInstant = BaseDateTime.UTC.getMillis() - (3 * DateTimeConstants.MILLIS_PER_MONTH);
        long expectedResult = 1;
        long actualResult = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expectedResult, actualResult);

        minuendInstant = BaseDateTime.UTC.getMillis() + (2 * DateTimeConstants.MILLIS_PER_MONTH);
        subtrahendInstant = BaseDateTime.UTC.getMillis() + (3 * DateTimeConstants.MILLIS_PER_MONTH);
        expectedResult = -1;
        actualResult = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expectedResult, actualResult);

        minuendInstant = BaseDateTime.UTC.getMillis();
        subtrahendInstant = BaseDateTime.UTC.getMillis() - (DateTimeConstants.MILLIS_PER_MONTH);
        expectedResult = 1;
        actualResult = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expectedResult, actualResult);

        minuendInstant = BaseDateTime.UTC.getMillis();
        subtrahendInstant = BaseDateTime.UTC.getMillis() + (DateTimeConstants.MILLIS_PER_MONTH);
        expectedResult = -1;
        actualResult = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSet() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);

        long instant = BaseDateTime.UTC.getMillis();
        int monthToSet = DateTimeConstants.JANUARY;
        long expectedResult = iChronology.getYearMonthMillis(2021, monthToSet);
        long actualResult = field.set(instant, monthToSet);
        assertEquals(expectedResult, actualResult);

        // Test minimum value
        instant -= (2 * DateTimeConstants.MILLIS_PER_DAY);
        monthToSet = DateTimeConstants.JANUARY;
        expectedResult = iChronology.getYearMonthMillis(2020, monthToSet);
        actualResult = field.set(instant, monthToSet);
        assertEquals(expectedResult, actualResult);

        // Test maximum value
        instant += (12 * DateTimeConstants.MILLIS_PER_MONTH);
        monthToSet = DateTimeConstants.DECEMBER;
        expectedResult = iChronology.getYearMonthMillis(2021, monthToSet);
        actualResult = field.set(instant, monthToSet);
        assertEquals(expectedResult, actualResult);

        // Test invalid month
        monthToSet = -1;
        expectedResult = iChronology.getYearMonthMillis(2021, 0);
        actualResult = field.set(instant, monthToSet);
        assertEquals(expectedResult, actualResult);

        // Test invalid month (maximum value)
        monthToSet = 13;
        expectedResult = iChronology.getYearMonthMillis(2021, 0);
        actualResult = field.set(instant, monthToSet);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetRangeDurationField() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);

        DurationField expectedField = DurationFieldType.years();
        DurationField actualField = field.getRangeDurationField();
        assertEquals(expectedField, actualField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsLeap() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);

        long instant = BaseDateTime.UTC.getMillis();
        int leapMonth = DateTimeConstants.FEBRUARY;
        assertFalse(field.isLeap(instant));

        instant = new BasicDateTime(2024, 2, 29, 0, 0, 0).getMillis();
        assertTrue(field.isLeap(instant));

        instant = new BasicDateTime(1900, 2, 29, 0, 0, 0).getMillis();
        assertFalse(field.isLeap(instant));

        instant = new BasicDateTime(2019, 2, 29, 0, 0, 0).getMillis();
        assertFalse(field.isLeap(instant));

        instant = new BasicDateTime(2020, 2, 29, 0, 0, 0).getMillis();
        assertTrue(field.isLeap(instant));

        instant = new BasicDateTime(2021, 2, 28, 0, 0, 0).getMillis();
        assertFalse(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 2);

        long instant = BaseDateTime.UTC.getMillis();
        int leapMonth = DateTimeConstants.FEBRUARY;
        int expectedLeapAmount = 1;
        int actualLeapAmount = field.getLeapAmount(instant);
        assertEquals(expectedLeapAmount, actualLeapAmount);

        instant = new BasicDateTime(2024, 2, 29, 0, 0, 0).getMillis();
        expectedLeapAmount = 1;
        actualLeapAmount = field.getLeapAmount(instant);
        assertEquals(expectedLeapAmount, actualLeapAmount);

        instant = new BasicDateTime(1900, 2, 29, 0, 0, 0).getMillis();
        expectedLeapAmount = 0;
        actualLeapAmount = field.getLeapAmount(instant);
        assertEquals(expectedLeapAmount, actualLeapAmount);

        instant = new BasicDateTime(2019, 2, 28, 0, 0, 0).getMillis();
        expectedLeapAmount = 0;
        actualLeapAmount = field.getLeapAmount(instant);
        assertEquals(expectedLeapAmount, actualLeapAmount);

        instant = new BasicDateTime(2020, 2, 29, 0, 0, 0).getMillis();
        expectedLeapAmount = 1;
        actualLeapAmount = field.getLeapAmount(instant);
        assertEquals(expectedLeapAmount, actualLeapAmount);
    }

    @Test
    public void testGetLeapDurationField() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);

        DurationField expectedField = DurationFieldType.days();
        DurationField actualField = field.getLeapDurationField();
        assertEquals(expectedField, actualField);
    }

    @Test
    public void testGetMinimumValue() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);
        int expectedMinimumValue = DateTimeConstants.JANUARY;
        int actualMinimumValue = field.getMinimumValue();
        assertEquals(expectedMinimumValue, actualMinimumValue);
    }

    @Test
    public void testGetMaximumValue() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);
        int expectedMaximumValue = DateTimeConstants.DECEMBER;
        int actualMaximumValue = field.getMaximumValue();
        assertEquals(expectedMaximumValue, actualMaximumValue);
    }

    @Test
    public void testRoundFloor() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);

        long instant = BaseDateTime.UTC.getMillis();
        long expectedResult = iChronology.getYearMonthMillis(2021, 3);
        long actualResult = field.roundFloor(instant);
        assertEquals(expectedResult, actualResult);

        instant -= (DateTimeConstants.MILLIS_PER_MONTH * 2);
        expectedResult = iChronology.getYearMonthMillis(2021, 1);
        actualResult = field.roundFloor(instant);
        assertEquals(expectedResult, actualResult);

        instant += (DateTimeConstants.MILLIS_PER_MONTH * 2);
        expectedResult = iChronology.getYearMonthMillis(2021, 5);
        actualResult = field.roundFloor(instant);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testRemainder() {
        Chronology chrono = Chronology.getDefault();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 0);

        long instant = BaseDateTime.UTC.getMillis();
        int monthsToAdd = 2;
        long expectedResult = instant % (DateTimeConstants.MILLIS_PER_MONTH * monthsToAdd);
        long actualResult = field.remainder(instant + (DateTimeConstants.MILLIS_PER_MONTH * monthsToAdd));
        assertEquals(expectedResult, actualResult);

        instant -= (DateTimeConstants.MILLIS_PER_MONTH * 2);
        monthsToAdd = -2;
        expectedResult = instant % (DateTimeConstants.MILLIS_PER_MONTH * monthsToAdd);
        actualResult = field.remainder(instant + (DateTimeConstants.MILLIS_PER_MONTH * monthsToAdd));
        assertEquals(expectedResult, actualResult);

        instant = BaseDateTime.UTC.getMillis() + (DateTimeConstants.MILLIS_PER_MONTH * 3);
        expectedResult = instant % (DateTimeConstants.MILLIS_PER_MONTH);
        actualResult = field.remainder(instant);
        assertEquals(expectedResult, actualResult);
    }
}