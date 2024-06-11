import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.junit.BaseConversionTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest extends BaseConversionTestCase {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        // Create a test chronology
        BasicChronology chrono = new ISOChronology();
        // Create a test field
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);
        // Basic checks
        assertEquals("Wrong serialVersionUID.", -8258715387168736L, field.serialVersionUID);
        assertEquals("Wrong chronology.", chrono, field.getChronology());
        assertEquals("Wrong maximum value.", 12, field.getMaximumValue());
        assertFalse("Should not be lenient.", field.isLenient());
        // Get and set values
        long instant = DateTimeConstants.MINUTE_OF_DAY;
        int month = field.get(instant);
        assertSame("Wrong month.", DateTimeConstants.JANUARY, month);
        instant = field.set(instant, DateTimeConstants.JUNE);
        month = field.get(instant);
        assertSame("Wrong month after set.", DateTimeConstants.JUNE, month);
        // Adding months
        instant = 0;
        instant = field.add(instant, 12);
        month = field.get(instant);
        assertSame("Wrong month after 12 months.", DateTimeConstants.DECEMBER, month);
        // Adding a large number of months
        instant = 0;
        instant = field.add(instant, Integer.MAX_VALUE * 12);
        month = field.get(instant);
        assertSame("Wrong month after overflow.", DateTimeConstants.JANUARY, month);
        // Adding negative months
        instant = DateTimeConstants.MILLIS_PER_DAY * 365;
        instant = field.add(instant, -12);
        month = field.get(instant);
        assertSame("Wrong month after 12 negative months.", DateTimeConstants.DECEMBER, month);
        // Largest and smallest possible values
        instant = field.getMinimumValue();
        month = field.get(instant);
        assertSame("Wrong month at minimum value.", DateTimeConstants.JANUARY, month);
        instant = field.getMaximumValue();
        month = field.get(instant);
        assertSame("Wrong month at maximum value.", DateTimeConstants.DECEMBER, month);
        // Test the isLeap() method
        instant = DateTimeConstants.MILLIS_PER_DAY * 365;
        assertFalse("January should not be a leap month.", field.isLeap(instant));
        instant += DateTimeConstants.MILLIS_PER_DAY;
        assertTrue("February should be a leap month.", field.isLeap(instant));
        // Test the getLeapAmount() method
        instant = DateTimeConstants.MILLIS_PER_DAY * 365;
        assertEquals("Wrong number of leaps in January.", 0, field.getLeapAmount(instant));
        instant += DateTimeConstants.MILLIS_PER_DAY;
        assertEquals("Wrong number of leaps in February.", 1, field.getLeapAmount(instant));
        // Test the getLeapDurationField() method
        DurationField df = field.getLeapDurationField();
        assertEquals("Wrong duration field.", BasicChronology.DAYS, df);
        // Test the add(ReadablePartial, int[], int, int) method
        BaseDateTime testDate = new BaseDateTime();
        int[] values = {DateTimeConstants.JANUARY, 31, 2012};
        int[] result
            = field.add(testDate.getReadablePartial(), 1, values, DateTimeConstants.JANUARY);
        assertEquals("Wrong number of values.", 3, result.length);
        assertEquals("Wrong year.", 2012, result[0]);
        assertEquals("Wrong month.", DateTimeConstants.FEBRUARY, result[1]);
        assertEquals("Wrong day of month.", 1, result[2]);
        // Test the addWrapField method
        long instantWrap
            = DateTimeConstants.MILLIS_PER_DAY * (DateTimeConstants.JANUARY + 12);
        instantWrap = field.addWrapField(instantWrap, -12);
        int monthWrap = field.get(instantWrap);
        assertSame("Wrong month after wrapping.", DateTimeConstants.DECEMBER, monthWrap);
        // Test the getDifferenceAsLong method
        long minuendInstant = DateTimeConstants.MILLIS_PER_DAY * 365;
        long subtrahendInstant = DateTimeConstants.MILLIS_PER_DAY * 366;
        long resultDifference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals("Wrong difference.", 1, resultDifference);
        // Test the set method
        instant = 0;
        instant = field.set(instant, DateTimeConstants.JANUARY);
        month = field.get(instant);
        assertSame("Wrong month after setting.", DateTimeConstants.JANUARY, month);
        // Test the getRangeDurationField method
        DurationField dfRange = field.getRangeDurationField();
        assertEquals("Wrong range duration field.", BasicChronology.YEARS, dfRange);
        // Test the roundFloor method
        instant = 0;
        instant = field.roundFloor(instant);
        month = field.get(instant);
        assertEquals("Wrong month after rounding.", DateTimeConstants.JANUARY, month);
        // Test the remainder method
        instant = DateTimeConstants.MILLIS_PER_DAY * 366;
        long remainder = field.remainder(instant);
        assertEquals("Wrong remainder.", DateTimeConstants.MILLIS_PER_DAY, remainder);
        // Test serialization
        field = null;
        byte[] bytes = serialize(field);
        field = (BasicMonthOfYearDateTimeField) deserialize(bytes);
        assertNotNull("Deserialized object was null.", field);
        assertEquals("Deserialized object had wrong values.", field, field);
    }
}