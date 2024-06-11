import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.JodaTimeTestCase;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthAdjustable;
import org.joda.time.field.MonthAdjustableField;
import org.joda.time.field.MonthLength;
import org.joda.time.local.LocalDateTime;

public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    public void testBasic() {
        BasicChronology chrono = new BasicChronology("UTC");
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, DateTimeConstants.JANUARY);

        // isLenient
        assertFalse(field.isLenient());

        // get
        long instant = DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY / 4;
        assertEquals(12, field.get(instant));

        // add
        long result = field.add(instant, 11);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 * 2 + DateTimeConstants.MILLIS_PER_DAY / 4, result);

        // addWrapField
        result = field.addWrapField(instant, -12);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 * 2 - DateTimeConstants.MILLIS_PER_DAY, result);

        // getDifferenceAsLong
        long minuend = DateTimeConstants.MILLIS_PER_DAY * 365 * 2 + DateTimeConstants.MILLIS_PER_DAY / 4;
        long subtrahend = DateTimeConstants.MILLIS_PER_DAY * 365 * 2 - DateTimeConstants.MILLIS_PER_DAY;
        assertEquals(2 * DateTimeConstants.MILLIS_PER_DAY, field.getDifferenceAsLong(minuend, subtrahend));

        // set
        result = field.set(instant, DateTimeConstants.MAY);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 * 2 + DateTimeConstants.MILLIS_PER_DAY / 4 +
                DateTimeConstants.MILLIS_PER_DAY * 2 - DateTimeConstants.MILLIS_PER_DAY / 4, result);

        // getRangeDurationField
        assertEquals(DateTimeFieldType.years(), field.getRangeDurationField().getFieldType());

        // isLeap, getLeapAmount, getLeapDurationField
        LocalDateTime dt = new LocalDateTime(2000, 2, 29, 0, 0);
        assertEquals(true, field.isLeap(dt.getMillis()));
        assertEquals(1, field.getLeapAmount(dt.getMillis()));
        assertEquals(DateTimeFieldType.days(), field.getLeapDurationField().getFieldType());

        // getMinimumValue, getMaximumValue
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());

        // roundFloor, remainder
        LocalDateTime dt1 = new LocalDateTime(2000, 12, 31, 0, 0);
        assertEquals(dt1.getMillis(), field.roundFloor(dt1.getMillis()));
        assertEquals(0, field.remainder(dt1.getMillis()));

        // serialization
        field = (BasicMonthOfYearDateTimeField) super.copy(field);
        assertEquals(instant, field.get(instant));
    }

    public void testMonthAdjustable() {
        BasicChronology chrono = new BasicChronology("UTC");
        MonthAdjustableField monthField = new MonthAdjustableField(DateTimeFieldType.monthOfYear(), chrono.getAverageMillisPerMonth(), chrono);
        MonthAdjustable monthAdjustable = new MonthAdjustable(monthField, DateTimeConstants.JANUARY);

        // isLenient
        assertFalse(monthAdjustable.isLenient());

        // get
        long instant = DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY / 4;
        assertEquals(12, monthAdjustable.get(instant));

        // add
        long result = monthAdjustable.add(instant, 11);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 * 2 + DateTimeConstants.MILLIS_PER_DAY / 4, result);

        // addWrapField
        result = monthAdjustable.addWrapField(instant, -12);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 * 2 - DateTimeConstants.MILLIS_PER_DAY, result);

        // getDifferenceAsLong
        long minuend = DateTimeConstants.MILLIS_PER_DAY * 365 * 2 + DateTimeConstants.MILLIS_PER_DAY / 4;
        long subtrahend = DateTimeConstants.MILLIS_PER_DAY * 365 * 2 - DateTimeConstants.MILLIS_PER_DAY;
        assertEquals(2 * DateTimeConstants.MILLIS_PER_DAY, monthField.getDifferenceAsLong(minuend, subtrahend));

        // set
        result = monthAdjustable.set(instant, DateTimeConstants.MAY);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY * 365 * 2 + DateTimeConstants.MILLIS_PER_DAY / 4 +
                DateTimeConstants.MILLIS_PER_DAY * 2 - DateTimeConstants.MILLIS_PER_DAY / 4, result);

        // getRangeDurationField
        assertEquals(DateTimeFieldType.years(), monthField.getRangeDurationField().getFieldType());

        // isLeap, getLeapAmount, getLeapDurationField
        LocalDateTime dt = new LocalDateTime(2000, 2, 29, 0, 0);
        assertEquals(true, monthAdjustable.isLeap(dt.getMillis()));
        assertEquals(1, monthAdjustable.getLeapAmount(dt.getMillis()));
        assertEquals(DateTimeFieldType.days(), monthField.getLeapDurationField().getFieldType());

        // getMinimumValue, getMaximumValue
        assertEquals(DateTimeConstants.JANUARY, monthAdjustable.getMinimumValue());
        assertEquals(DateTimeConstants.DECEMBER, monthAdjustable.getMaximumValue());

        // roundFloor, remainder
        LocalDateTime dt1 = new LocalDateTime(2000, 12, 31, 0, 0);
        assertEquals(dt1.getMillis(), monthAdjustable.roundFloor(dt1.getMillis()));
        assertEquals(0, monthAdjustable.remainder(dt1.getMillis()));
    }

    public void testMonthLength() {
        BasicChronology chrono = new BasicChronology("UTC");
        MonthLength monthLength = new MonthLength(DateTimeFieldType.monthOfYear(), chrono);

        // get
        assertEquals(DateTimeConstants.JANUARY, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365));
        assertEquals(DateTimeConstants.FEBRUARY, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(DateTimeConstants.MARCH, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 2));
        assertEquals(DateTimeConstants.APRIL, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 3));
        assertEquals(DateTimeConstants.MAY, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 4));
        assertEquals(DateTimeConstants.JUNE, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 5));
        assertEquals(DateTimeConstants.JULY, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 6));
        assertEquals(DateTimeConstants.AUGUST, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 7));
        assertEquals(DateTimeConstants.SEPTEMBER, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 8));
        assertEquals(DateTimeConstants.OCTOBER, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 9));
        assertEquals(DateTimeConstants.NOVEMBER, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 10));
        assertEquals(DateTimeConstants.DECEMBER, monthLength.get(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 11));

        // add
        assertEquals(DateTimeConstants.JANUARY, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365, 0));
        assertEquals(DateTimeConstants.FEBRUARY, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY, 1));
        assertEquals(DateTimeConstants.MARCH, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 2, 2));
        assertEquals(DateTimeConstants.APRIL, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 3, 3));
        assertEquals(DateTimeConstants.MAY, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 4, 4));
        assertEquals(DateTimeConstants.JUNE, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 5, 5));
        assertEquals(DateTimeConstants.JULY, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 6, 6));
        assertEquals(DateTimeConstants.AUGUST, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 7, 7));
        assertEquals(DateTimeConstants.SEPTEMBER, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 8, 8));
        assertEquals(DateTimeConstants.OCTOBER, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 9, 9));
        assertEquals(DateTimeConstants.NOVEMBER, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 10, 10));
        assertEquals(DateTimeConstants.DECEMBER, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 11, 11));

        // add wrap
        assertEquals(DateTimeConstants.JANUARY, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365, 12));
        assertEquals(DateTimeConstants.FEBRUARY, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY, 11));
        assertEquals(DateTimeConstants.MARCH, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 2, 10));
        assertEquals(DateTimeConstants.APRIL, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 3, 9));
        assertEquals(DateTimeConstants.MAY, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 4, 8));
        assertEquals(DateTimeConstants.JUNE, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 5, 7));
        assertEquals(DateTimeConstants.JULY, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 6, 6));
        assertEquals(DateTimeConstants.AUGUST, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 7, 5));
        assertEquals(DateTimeConstants.SEPTEMBER, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 8, 4));
        assertEquals(DateTimeConstants.OCTOBER, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 9, 3));
        assertEquals(DateTimeConstants.NOVEMBER, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 10, 2));
        assertEquals(DateTimeConstants.DECEMBER, monthLength.add(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 11, 1));

        // getMinimumValue, getMaximumValue
        assertEquals(DateTimeConstants.JANUARY, monthLength.getMinimumValue());
        assertEquals(DateTimeConstants.DECEMBER, monthLength.getMaximumValue());
    }

    @Override
    protected void setUp() {
        super.setUp();
    }
}