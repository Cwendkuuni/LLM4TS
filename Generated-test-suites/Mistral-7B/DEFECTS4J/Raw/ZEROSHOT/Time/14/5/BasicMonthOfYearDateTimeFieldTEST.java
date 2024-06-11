import org.joda.time.BaseTestCase;
import org.joda.time.BasicChronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.JodaTimeTestCase;
import org.joda.time.LocalDate;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearDateTimeField;

/**
 * Test class for BasicMonthOfYearDateTimeField.
 */
public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    /**
     * Test the BasicMonthOfYearDateTimeField class.
     */
    public void testBasicMonthOfYearDateTimeField() {
        // Create a test chronology.
        BasicChronology chrono = new ISOChronology();

        // Test a BasicMonthOfYearDateTimeField with no leap month.
        MonthOfYearDateTimeField fieldNoLeap = new MonthOfYearDateTimeField(chrono, 0);
        assertEquals(0, fieldNoLeap.getMaximumValue());

        // Test a BasicMonthOfYearDateTimeField with a leap month.
        MonthOfYearDateTimeField fieldLeap = new MonthOfYearDateTimeField(chrono, DateTimeConstants.FEBRUARY);
        assertEquals(12, fieldLeap.getMaximumValue());

        // Test get method.
        MutableDateTime date = new MutableDateTime();
        date.setYear(2010);
        date.setMonthOfYear(10, 3);
        date.setDayOfMonth(31);
        long instant = date.getMillis();

        // Test getting the month in the middle of February.
        int month = fieldLeap.get(instant);
        assertEquals(10, month);

        // Test setting the month.
        long setInstant = fieldLeap.set(instant, 1);
        MutableDateTime setDate = new MutableDateTime(setInstant);
        assertEquals(DateTimeConstants.JANUARY, setDate.getMonthOfYear());

        // Test add method.
        long addInstant = fieldLeap.add(instant, 5);
        MutableDateTime addDate = new MutableDateTime(addInstant);
        assertEquals(DateTimeConstants.MAY, addDate.getMonthOfYear());

        // Test add wrap field method.
        long addWrapFieldInstant = fieldLeap.addWrapField(instant, 13);
        MutableDateTime addWrapFieldDate = new MutableDateTime(addWrapFieldInstant);
        assertEquals(DateTimeConstants.APRIL, addWrapFieldDate.getMonthOfYear());

        // Test getDifference method.
        long differenceInstant = fieldLeap.getDifference(instant, instant + DurationField.ONE_MONTH.getMillis(1));
        assertEquals(DurationField.ONE_MONTH.getValue(), differenceInstant);

        // Test set method.
        long setInstant2 = fieldLeap.set(instant, 2);
        MutableDateTime setDate2 = new MutableDateTime(setInstant2);
        assertEquals(DateTimeConstants.FEBRUARY, setDate2.getMonthOfYear());

        // Test getMinimumValue and getMaximumValue methods.
        assertEquals(DateTimeConstants.JANUARY, fieldNoLeap.getMinimumValue());
        assertEquals(DateTimeConstants.DECEMBER, fieldLeap.getMinimumValue());
        assertEquals(0, fieldNoLeap.getMaximumValue());
        assertEquals(12, fieldLeap.getMaximumValue());

        // Test roundFloor and remainder methods.
        long roundFloorInstant = fieldLeap.roundFloor(instant);
        assertEquals(chrono.getYearMonthMillis(2010, 10), roundFloorInstant);
        long remainderInstant = fieldLeap.remainder(roundFloorInstant);
        assertEquals(3L, remainderInstant);

        // Test serialization
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        MonthOfYearDateTimeField field2 = (MonthOfYearDateTimeField) ((FieldUtils.copy(field)).readResolve());
        assertEquals(field, field2);
    }

    /**
     * Test the BasicMonthOfYearDateTimeField with a large leap month value.
     */
    public void testBasicMonthOfYearDateTimeFieldLargeLeapMonth() {
        // Create a test chronology.
        BasicChronology chrono = new ISOChronology();

        // Test a BasicMonthOfYearDateTimeField with a leap month value greater than the number of months.
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, DateTimeConstants.FEBRUARY + 2);
        assertEquals(12, field.getMaximumValue());

        // Test getting the month.
        MutableDateTime date = new MutableDateTime();
        date.setYear(2010);
        date.setMonthOfYear(10, 3);
        date.setDayOfMonth(31);
        long instant = date.getMillis();

        int month = field.get(instant);
        assertEquals(10, month);

        // Test setting the month.
        long setInstant = field.set(instant, 2);
        MutableDateTime setDate = new MutableDateTime(setInstant);
        assertEquals(DateTimeConstants.JANUARY, setDate.getMonthOfYear());

        // Test add method.
        long addInstant = field.add(instant, 5);
        MutableDateTime addDate = new MutableDateTime(addInstant);
        assertEquals(DateTimeConstants.MAY, addDate.getMonthOfYear());

        // Test add wrap field method.
        long addWrapFieldInstant = field.addWrapField(instant, 13);
        MutableDateTime addWrapFieldDate = new MutableDateTime(addWrapFieldInstant);
        assertEquals(DateTimeConstants.APRIL, addWrapFieldDate.getMonthOfYear());

        // Test getDifference method.
        long differenceInstant = field.getDifference(instant, instant + DurationField.ONE_MONTH.getMillis(1));
        assertEquals(DurationField.ONE_MONTH.getValue(), differenceInstant);

        // Test set method.
        long setInstant2 = field.set(instant, 2);
        MutableDateTime setDate2 = new MutableDateTime(setInstant2);
        assertEquals(DateTimeConstants.FEBRUARY, setDate2.getMonthOfYear());

        // Test getMinimumValue and getMaximumValue methods.
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
        assertEquals(DateTimeConstants.DECEMBER, field.getMinimumValue());
        assertEquals(0, field.getMaximumValue());
        assertEquals(12, field.getMaximumValue());

        // Test roundFloor and remainder methods.
        long roundFloorInstant = field.roundFloor(instant);
        assertEquals(chrono.getYearMonthMillis(2010, 10), roundFloorInstant);
        long remainderInstant = field.remainder(roundFloorInstant);
        assertEquals(3L, remainderInstant);

        // Test serialization
        MonthOfYearDateTimeField field3 = new MonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        MonthOfYearDateTimeField field4 = (MonthOfYearDateTimeField) ((FieldUtils.copy(field3)).readResolve());
        assertEquals(field3, field4);
    }

    /**
     * Test the BasicMonthOfYearDateTimeField with a negative leap month value.
     */
    public void testBasicMonthOfYearDateTimeFieldNegativeLeapMonth() {
        // Create a test chronology.
        BasicChronology chrono = new ISOChronology();

        // Test a BasicMonthOfYearDateTimeField with a negative leap month value.
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY - 1);
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());

        // Test getting the month.
        MutableDateTime date = new MutableDateTime();
        date.setYear(2010);
        date.setMonthOfYear(10, 3);
        date.setDayOfMonth(31);
        long instant = date.getMillis();

        int month = field.get(instant);
        assertEquals(10, month);

        // Test setting the month.
        long setInstant = field.set(instant, 2);
        MutableDateTime setDate = new MutableDateTime(setInstant);
        assertEquals(DateTimeConstants.NOVEMBER, setDate.getMonthOfYear());

        // Test add method.
        long addInstant = field.add(instant, 5);
        MutableDateTime addDate = new MutableDateTime(addInstant);
        assertEquals(DateTimeConstants.APRIL, addDate.getMonthOfYear());

        // Test add wrap field method.
        long addWrapFieldInstant = field.addWrapField(instant, 13);
        MutableDateTime addWrapFieldDate = new MutableDateTime(addWrapFieldInstant);
        assertEquals(DateTimeConstants.MARCH, addWrapFieldDate.getMonthOfYear());

        // Test getDifference method.
        long differenceInstant = field.getDifference(instant, instant + DurationField.ONE_MONTH.getMillis(1));
        assertEquals(DurationField.ONE_MONTH.getValue(), differenceInstant);

        // Test set method.
        long setInstant2 = field.set(instant, 2);
        MutableDateTime setDate2 = new MutableDateTime(setInstant2);
        assertEquals(DateTimeConstants.DECEMBER, setDate2.getMonthOfYear());

        // Test getMinimumValue and getMaximumValue methods.
        assertEquals(DateTimeConstants.DECEMBER, field.getMinimumValue());
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
        assertEquals(0, field.getMaximumValue());
        assertEquals(11, field.getMaximumValue());

        // Test roundFloor and remainder methods.
        long roundFloorInstant = field.roundFloor(instant);
        assertEquals(chrono.getYearMonthMillis(2010, 10), roundFloorInstant);
        long remainderInstant = field.remainder(roundFloorInstant);
        assertEquals(3L, remainderInstant);

        // Test serialization
        MonthOfYearDateTimeField field3 = new MonthOfYearDateTimeField(chrono, DateTimeConstants.DECEMBER);
        MonthOfYearDateTimeField field4 = (MonthOfYearDateTimeField) ((FieldUtils.copy(field3)).readResolve());
        assertEquals(field3, field4);
    }
}