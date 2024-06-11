import org.joda.time.BaseDateTime;
import org.joda.time.BasicDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("test"), 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("test"), 0);

        long instant = new BasicDateTime(1234567890L, new BasicChronology("test")).getMillis();
        int expectedValue = 3;
        int actualValue = field.get(instant);

        assertEquals("Unexpected month value", expectedValue, actualValue);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("test"), 0);

        long instant = new BasicDateTime(1234567890L, new BasicChronology("test")).getMillis();
        long expectedResult = 1420070681000L;// (3 months after initial instant)
        long actualResult = field.add(instant, 3);

        assertEquals("Unexpected result of add", expectedResult, actualResult);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("test"), 0);

        long instant = new BasicDateTime(1234567890L, new BasicChronology("test")).getMillis();
        long expectedResult = 567890000L;// (10 months after initial instant, wrapping around)
        long actualResult = field.addWrapField(instant, 10);

        assertEquals("Unexpected result of addWrapField", expectedResult, actualResult);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("test"), 0);

        long minuendInstant = new BasicDateTime(1234567890L, new BasicChronology("test")).getMillis();
        long subtrahendInstant = new BasicDateTime(1357946360L, new BasicChronology("test")).getMillis();
        long expectedDifference = 10;
        long actualDifference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);

        assertEquals("Unexpected difference", expectedDifference, actualDifference);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("test"), 0);

        long instant = new BasicDateTime(1234567890L, new BasicChronology("test")).getMillis();
        int expectedMonth = 5;
        long expectedResult = 1262304000000L;// (5 months after initial instant)
        long actualResult = field.set(instant, expectedMonth);

        assertEquals("Unexpected result of set", expectedResult, actualResult);
        assertEquals("Unexpected month value in result", expectedMonth,
                FieldUtils.getWrappedValue(
                        FieldUtils.getWrappedFieldValue(new BasicDateTime(actualResult, new BasicChronology("test")), DateTimeFieldType.monthOfYear()), 5, 1, 12));
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 2);

        long instant = new ImmutableDateTime(2004, 2, 29, 0, 0, 0, 0, new ISOChronology()).getMillis();
        assertTrue("Unexpected leap result", field.isLeap(instant));

        instant = new ImmutableDateTime(2004, 3, 1, 0, 0, 0, 0, new ISOChronology()).getMillis();
        assertFalse("Unexpected non-leap result", field.isLeap(instant));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("test"), 0);

        long instant = new BasicDateTime(1234567890L, new BasicChronology("test")).getMillis();
        int invalidMonth = 14;
        field.set(instant, invalidMonth);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("test"), 0);

        long instant = new BasicDateTime(1234567890L, new BasicChronology("test")).getMillis();
        int invalidMonths = 25;
        field.add(instant, invalidMonths);
    }
}