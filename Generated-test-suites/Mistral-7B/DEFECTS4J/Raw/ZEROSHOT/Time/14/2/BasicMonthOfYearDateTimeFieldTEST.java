import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.FieldUtils;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldLength;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        Chronology gregorianChronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField monthOfYearField = new BasicMonthOfYearDateTimeField(
                (BasicChronology) gregorianChronology, DateTimeConstants.JANUARY);

        // Test get method
        long instant = DateTimeUtils.currentMillis();
        int month = monthOfYearField.get(instant);
        assertEquals("Current month", DateTimeConstants.MARCH, month);

        // Test set method
        long setInstant = monthOfYearField.set(instant, DateTimeConstants.MAY);
        assertEquals("Set month to May", DateTimeConstants.MAY, monthOfYearField.get(setInstant));

        // Test add method
        long addInstant = monthOfYearField.add(instant, 3);
        assertEquals("Add 3 months", DateTimeConstants.JUNE, monthOfYearField.get(addInstant));

        // Test addWrapField method
        long wrapInstant = monthOfYearField.addWrapField(instant, 13);
        assertEquals("Add 13 months wrapping around", DateTimeConstants.APRIL, monthOfYearField.get(wrapInstant));

        // Test getMinimumValue and getMaximumValue methods
        assertEquals("Minimum value", DateTimeConstants.JANUARY, monthOfYearField.getMinimumValue());
        assertEquals("Maximum value", DateTimeConstants.DECEMBER, monthOfYearField.getMaximumValue());

        // Test getRangeDurationField method
        DurationField rangeDurationField = monthOfYearField.getRangeDurationField();
        assertEquals("Range duration field type", DateTimeFieldType.years(), rangeDurationField.getFieldType());
        assertEquals("Range duration length", new FieldLength(12), rangeDurationField.getLength());

        // Test isLeap method
        long leapInstant = new BaseDateTime(1500, 2, DateTimeConstants.FEBRUARY_29, 0, 0, 0, 0, gregorianChronology).getMillis();
        assertTrue("Is leap year for February 29, 1500", monthOfYearField.isLeap(leapInstant));

        // Test getLeapAmount method
        long leapAmountInstant = new BaseDateTime(1500, 2, DateTimeConstants.FEBRUARY_28, 0, 0, 0, 0, gregorianChronology).getMillis();
        assertEquals("Not leap year for February 28, 1500", 0, monthOfYearField.getLeapAmount(leapAmountInstant));

        // Test getLeapDurationField method
        DurationField leapDurationField = monthOfYearField.getLeapDurationField();
        assertEquals("Leap duration field type", DateTimeFieldType.days(), leapDurationField.getFieldType());
        assertEquals("Leap duration length", new FieldLength(1), leapDurationField.getLength());
    }
}