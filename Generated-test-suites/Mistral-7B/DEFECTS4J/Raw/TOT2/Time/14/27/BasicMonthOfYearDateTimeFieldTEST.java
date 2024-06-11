import org.joda.time.BaseDateTime;
import org.joda.time.ChronologyUtils;
import org.joda.time.DateTime;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicFunctions() {
        BasicChronology testChronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(testChronology, 2); // February

        // Test isLenient()
        assertFalse(testField.isLenient());

        // Test get() method
        DateTime testDate = new DateTime(2022, 1, 31);
        long instant = testDate.getMillis();
        int expectedMonth = 1;
        int actualMonth = testField.get(instant);
        assertEquals("The month extracted from the input is incorrect.", expectedMonth, actualMonth);

        // Test set() method
        expectedMonth = 3;
        long newInstant = testField.set(instant, expectedMonth).getMillis();
        LocalDate newDate = new LocalDate(newInstant);
        int newYear = newDate.getYear();
        int newMonth = newDate.getMonthOfYear();
        assertEquals("The month set to the time instant is incorrect.", expectedMonth, newMonth);
        assertEquals("The year set to the time instant is incorrect.", newYear, 2022);

        // Test add() method
        int monthsToAdd = 2;
        long newInstant2 = testField.add(instant, monthsToAdd).getMillis();
        LocalDate newDate2 = new LocalDate(newInstant2);
        int newYear2 = newDate2.getYear();
        int newMonth2 = newDate2.getMonthOfYear();
        assertEquals("The month added to the time instant is incorrect.", expectedMonth + monthsToAdd, newMonth2);
        assertEquals("The year added to the time instant is incorrect.", newYear2, 2023);

        // Test addWrapField() method
        monthsToAdd = -1;
        long newInstant3 = testField.addWrapField(instant, monthsToAdd).getMillis();
        LocalDate newDate3 = new LocalDate(newInstant3);
        assertEquals("The month wrapped around is incorrect.", 12, newMonth2);
        assertEquals("The year wrapped around is incorrect.", newYear2, 2022);
    }

    @Test
    public void testEdgeCases() {
        BasicChronology testChronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(testChronology, 2); // February

        // Test negative month
        long instant = testField.getMillis(0);
        int expectedMonth = 12;
        long newInstant = testField.addWrapField(instant, -1).getMillis();
        LocalDate newDate = new LocalDate(newInstant);
        int newMonth = newDate.getMonthOfYear();
        assertEquals("The month after the last month is incorrect.", expectedMonth, newMonth);

        // Test negative day
        int dayToSubtract = 31;
        long newInstant2 = testField.add(instant, -dayToSubtract).getMillis();
        LocalDate newDate2 = new LocalDate(newInstant2);
        int newMonth2 = newDate2.getMonthOfYear();
        int newDay = newDate2.getDayOfMonth();
        assertEquals("The day subtracted is incorrect.", dayToSubtract, newDay);
        assertEquals("The month after the last day is incorrect.", expectedMonth, newMonth2);

        // Test maximum month
        int maxMonth = 12;
        long newInstant3 = testField.addWrapField(instant, maxMonth + 1).getMillis();
        LocalDate newDate3 = new LocalDate(newInstant3);
        int newMonth3 = newDate3.getMonthOfYear();
        int newDay3 = newDate3.getDayOfMonth();
        assertEquals("The month after the maximum month is incorrect.", 1, newMonth3);
        assertEquals("The day of the first month is incorrect.", 1, newDay3);

        // Test minimum month
        long newInstant4 = testField.addWrapField(instant, -(maxMonth + 1)).getMillis();
        LocalDate newDate4 = new LocalDate(newInstant4);
        int newMonth4 = newDate4.getMonthOfYear();
        assertEquals("The month before the minimum month is incorrect.", maxMonth, newMonth4);
        assertEquals("The day of the last month is incorrect.", 31, DateTimeConstants.getDaysInMonth(2022, maxMonth));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        BasicChronology testChronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(testChronology, 2); // February

        // Test negative chronology
        BasicChronology invalidChronology = new JulianChronology();
        try {
            new BasicMonthOfYearDateTimeField(invalidChronology, 2);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The chronology argument is invalid.", e.getMessage());
        }

        // Test non-existent month
        int nonExistentMonth = 13;
        try {
            new BasicMonthOfYearDateTimeField(testChronology, nonExistentMonth);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The month argument is invalid.", e.getMessage());
        }
    }

    @Test
    public void testAddDifferentUnits() {
        BasicChronology testChronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(testChronology, 2); // February
        DurationField daysField = DurationFieldType.dayOfYear();

        // Test add(long, int) with dayOfYear
        long instant = testField.getMillis(0);
        long newInstant = testField.add(instant, 15);
        LocalDate newDate = new LocalDate(newInstant);
        int newMonth = newDate.getMonthOfYear();
        int newDay = newDate.getDayOfMonth();
        assertEquals("The month after 15 days is incorrect.", 3, newMonth);
        assertEquals("The day of the third month is incorrect.", 15, newDay);

        // Test add(ReadablePartial, int, int[], int) with dayOfYear
        ReadablePartial testPartial = FieldUtils.localDate(2022, 1, 31);
        int[] values = {0};
        newInstan
        t = testField.add(testPartial, 0, values, 15);
        newDate = new LocalDate(newInstan);
        newMonth = newDate.getMonthOfYear();
        newDay = newDate.getDayOfMonth();
        assertEquals("The month after 15 days is incorrect.", 3, newMonth);
        assertEquals("The day of the third month is incorrect.", 15, newDay);

        // Test addWrapField(long, int) with dayOfYear
        int daysToAdd = 30;
        long newInstant2 = testField.addWrapField(instant, daysToAdd);
        newDate = new LocalDate(newInstant2);
        newMonth = newDate.getMonthOfYear();
        newDay = newDate.getDayOfMonth();
        assertEquals("The month after 30 days is incorrect.", 4, newMonth);
        assertEquals("The day of the fourth month is incorrect.", 30, newDay);

        // Test add(ReadablePartial, int, int[], int) with dayOfYear
        newInstan = testField.add(testPartial, 0, values, daysToAdd);
        newDate = new LocalDate(newInstan);
        newMonth = newDate.getMonthOfYear();
        newDay = newDate.getDayOfMonth();
        assertEquals("The month after 30 days is incorrect.", 4, newMonth);
        assertEquals("The day of the fourth month is incorrect.", 30, newDay);

        // Test add(long, DurationField) with days
        DurationField daysToAddField = daysField.withValue(daysToAdd);
        newInstant = testField.add(instant, daysToAddField);
        newDate = new LocalDate(newInstant);
        newMonth = newDate.getMonthOfYear();
        newDay = newDate.getDayOfMonth();
        assertEquals("The month after 30 days is incorrect.", 4, newMonth);
        assertEquals("The day of the fourth month is incorrect.", 30, newDay);

        // Test add(ReadablePartial, int, int[], DurationField) with days
        newInstan = testField.add(testPartial, 0, values, daysToAddField);
        newDate = new LocalDate(newInstan);
        newMonth = newDate.getMonthOfYear();
        newDay = newDate.getDayOfMonth();
        assertEquals("The month after 30 days is incorrect.", 4, newMonth);
        assertEquals("The day of the fourth month is incorrect.", 30, newDay);

        // Test addWrapField(long, DurationField) with days
        daysToAdd = 12 * 31;
        newInstant2 = testField.addWrapField(instant, daysToAddField);
        newDate = new LocalDate(newInstant2);
        newMonth = newDate.getMonthOfYear();
        newDay = newDate.getDayOfMonth();
        assertEquals("The month after 365 days is incorrect.", 13, newMonth);
        assertEquals("The day of the thirteenth month is incorrect.", 31, newDay);
    }

    @Test
    public void testAddDifferentChronologies() {
        BasicChronology testChronology1 = new GregorianChronology();
        BasicMonthOfYearDateTimeField testField1 = new BasicMonthOfYearDateTimeField(testChronology1, 2); // February

        BasicChronology testChronology2 = new JulianChronology();
        BasicMonthOfYearDateTimeField testField2 = new BasicMonthOfYearDateTimeField(testChronology2, 2); // February

        // Test add(long, int)
        long instant = testField1.getMillis(0);
        long newInstant = testField1.add(instant, 1);
        long newInstantWithChronology2 = testField2.add(instant, 1);
        assertEquals("The result of adding one month with two different chronologies is incorrect.", newInstant, newInstantWithChronology2);

        // Test addWrapField(long, int)
        int monthsToAdd = 12;
        long newInstant3 = testField1.addWrapField(instant, monthsToAdd);
        long newInstantWithChronology2Wrap = testField2.addWrapField(instant, monthsToAdd);
        assertEquals("The result of adding 12 months with two different chronologies is incorrect.", newInstant3, newInstantWithChronology2Wrap);

        // Test add(ReadablePartial, int, int[], int)
        ReadablePartial testPartial = FieldUtils.localDate(2022, 1, 31);
        int[] values = {0};
        long newInstant4 = testField1.add(testPartial, 0, values, 1);
        long newInstantWithChronology2 = testField2.add(testPartial, 0, values, 1);
        assertEquals("The result of adding one month with two different chronologies is incorrect.", newInstant4, newInstantWithChronology2);

        // Test addWrapField(ReadablePartial, int, int[], int)
        int monthsToAddWrap = -1;
        long newInstant5 = testField1.addWrapField(testPartial, 0, values, monthsToAddWrap);
        long newInstantWithChronology2Wrap2 = testField2.addWrapField(testPartial, 0, values, monthsToAddWrap);
        assertEquals("The result of wrapping one month with two different chronologies is incorrect.", newInstant5, newInstantWithChronology2Wrap2);
    }
}