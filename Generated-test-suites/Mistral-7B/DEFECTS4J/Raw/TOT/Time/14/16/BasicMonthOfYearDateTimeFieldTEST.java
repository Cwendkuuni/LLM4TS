import org.joda.time.BasicChronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.junit.Test;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        final int leapYear = 2000;
        final int leapMonth = DateTimeConstants.FEBRUARY;
        BasicChronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(
                gregorianChronology, leapMonth);

        // Test getter methods
        assertEquals("Incorrect minimum value.", DateTimeConstants.JANUARY, basicMonthOfYearDateTimeField.getMinimumValue());
        assertEquals("Incorrect maximum value.", DateTimeConstants.DECEMBER, basicMonthOfYearDateTimeField.getMaximumValue());
        assertEquals("Incorrect field type.", DateTimeFieldType.monthOfYear(), basicMonthOfYearDateTimeField.getFieldType());
        assertEquals("Incorrect average milliseconds per month.", DateTimeConstants.MONTHS_PER_YEAR * DateTimeConstants.MILLIS_PER_DAY,
                basicMonthOfYearDateTimeField.getPeriodForValue(1).getMilliseconds());

        // Test isLeap method
        assertFalse("Expected false for non-leap year.", basicMonthOfYearDateTimeField.isLeap(new Instant(0)));
        assertTrue("Expected true for leap year.", basicMonthOfYearDateTimeField.isLeap(new Instant(DateTimeUtils.toMillis(new LocalDate(leapYear, 2, 29)))));

        // Test get method
        assertEquals("Incorrect month value.", DateTimeConstants.JANUARY, basicMonthOfYearDateTimeField.get(new Instant(DateTimeUtils.toMillis(new LocalDate(2000, 1, 1)))));
        assertEquals("Incorrect month value.", DateTimeConstants.DECEMBER, basicMonthOfYearDateTimeField.get(new Instant(DateTimeUtils.toMillis(new LocalDate(2000, 12, 31)))));

        // Test add method
        Instant initialInstant = new Instant(DateTimeUtils.toMillis(new LocalDate(2000, 1, 1)));
        Instant addedInstant = basicMonthOfYearDateTimeField.add(initialInstant, 1);
        assertEquals("Incorrect month value after addition.", DateTimeConstants.FEBRUARY, basicMonthOfYearDateTimeField.get(addedInstant));
        addedInstant = basicMonthOfYearDateTimeField.add(addedInstant, 1);
        assertEquals("Incorrect month value after addition.", DateTimeConstants.MARCH, basicMonthOfYearDateTimeField.get(addedInstant));

        // Test addWrapField method
        Instant wrappedInstant = basicMonthOfYearDateTimeField.addWrapField(initialInstant, 14);
        assertEquals("Incorrect month value after wrapping.", DateTimeConstants.APRIL, basicMonthOfYearDateTimeField.get(wrappedInstant));

        // Test getDifferenceAsLong method
        Instant subtrahendInstant = new Instant(DateTimeUtils.toMillis(new LocalDate(2000, 7, 31)));
        Instant minuendInstant = new Instant(DateTimeUtils.toMillis(new LocalDate(2001, 6, 1)));
        long difference = basicMonthOfYearDateTimeField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals("Incorrect difference value.", 1, difference);

        // Test set method
        Instant setInstant = new Instant(DateTimeUtils.toMillis(new LocalDate(2000, 1, 1)));
        Instant setResultInstant = basicMonthOfYearDateTimeField.set(setInstant, DateTimeConstants.MAY);
        assertEquals("Incorrect month value after setting.", DateTimeConstants.MAY, basicMonthOfYearDateTimeField.get(setResultInstant));

        // Test getRangeDurationField method
        assertEquals("Incorrect range duration field.", DurationField.fields(Months.MONTHS), basicMonthOfYearDateTimeField.getRangeDurationField());

        // Test getLeapDurationField method
        assertEquals("Incorrect leap duration field.", DurationField.fields(1), basicMonthOfYearDateTimeField.getLeapDurationField());

        // Test getLeapAmount method
        Instant leapDayInstant = new Instant(DateTimeUtils.toMillis(new LocalDate(leapYear, leapMonth, 29)));
        assertEquals("Incorrect leap amount value.", 1, basicMonthOfYearDateTimeField.getLeapAmount(leapDayInstant));

        // Test roundFloor method
        Instant roundFloorInstant = new Instant(DateTimeUtils.toMillis(new LocalDate(2000, 1, 31)));
        Instant floorResultInstant = basicMonthOfYearDateTimeField.roundFloor(roundFloorInstant);
        assertEquals("Incorrect month value after rounding floor.", DateTimeConstants.JANUARY, basicMonthOfYearDateTimeField.get(floorResultInstant));

        // Test remainder method
        Instant remainderInstant = new Instant(DateTimeUtils.toMillis(new LocalDate(2000, 2, 29)));
        Instant remainderResultInstant = basicMonthOfYearDateTimeField.roundFloor(remainderInstant);
        Instant remainderRemainderInstant = new Instant(remainderInstant.getMillis() % DateTimeConstants.MILLIS_PER_DAY);
        assertEquals("Incorrect month value after remainder.", DateTimeConstants.FEBRUARY, basicMonthOfYearDateTimeField.get(remainderResultInstant));
        assertEquals("Incorrect remainder value.", remainderRemainderInstant.getMillis(), basicMonthOfYearDateTimeField.remainder(remainderInstant).getMillis());

        // Test add method with negative values
        Instant initialInstantNegative = new Instant(DateTimeUtils.toMillis(new LocalDate(2000, 12, 31)));
        Instant addedInstantNegative = basicMonthOfYearDateTimeField.add(initialInstantNegative, -1);
        assertEquals("Incorrect month value after subtraction.", DateTimeConstants.NOVEMBER, basicMonthOfYearDateTimeField.get(addedInstantNegative));

        // Test addWrapField method with negative values
        Instant wrappedInstantNegative = basicMonthOfYearDateTimeField.addWrapField(initialInstantNegative, -14);
        assertEquals("Incorrect month value after wrapping with negative values.", DateTimeConstants.NOVEMBER, basicMonthOfYearDateTimeField.get(wrappedInstantNegative));

        // Test set method with negative values
        Instant setInstantNegative = new Instant(DateTimeUtils.toMillis(new LocalDate(2000, 12, 31)));
        Instant setResultInstantNegative = basicMonthOfYearDateTimeField.set(setInstantNegative, DateTimeConstants.NOVEMBER);
        assertEquals("Incorrect month value after setting with negative values.", DateTimeConstants.NOVEMBER, basicMonthOfYearDateTimeField.get(setResultInstantNegative));
    }
}