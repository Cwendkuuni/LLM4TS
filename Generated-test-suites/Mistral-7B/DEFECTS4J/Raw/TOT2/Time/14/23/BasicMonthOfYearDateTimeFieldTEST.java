import org.joda.time.BasicDateTime;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.YearMonth;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final Chronology CHRONOLOGY = DateTimeConstants.ISO_8601_DATE_TIME;

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.parse("2015-01-31T12:00:00.000Z").getMillis();
        int expected = DateTimeConstants.JANUARY;
        int result = field.get(instant);
        assertEquals(expected, result);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.parse("2015-01-31T12:00:00.000Z").getMillis();
        long expected = BaseDateTime.parse("2016-02-29T12:00:00.000Z").getMillis();
        long result = field.add(instant, 12);
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.parse("2015-13-31T12:00:00.000Z").getMillis();
        long expected = BaseDateTime.parse("2015-01-31T12:00:00.000Z").getMillis();
        long result = field.addWrapField(instant, 12);
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long minuendInstant = BaseDateTime.parse("2015-01-31T12:00:00.000Z").getMillis();
        long subtrahendInstant = BaseDateTime.parse("2014-12-31T12:00:00.000Z").getMillis();
        long expected = 12;
        long result = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, result);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.parse("2015-01-31T12:00:00.000Z").getMillis();
        int expectedMonth = DateTimeConstants.FEBRUARY;
        long result = field.set(instant, expectedMonth);
        assertEquals(expectedMonth, field.get(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.parse("2015-01-31T12:00:00.000Z").getMillis();
        int invalidMonth = 13;
        field.set(instant, invalidMonth);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.FEBRUARY);
        long instant = BaseDateTime.parse("2016-02-29T12:00:00.000Z").getMillis();
        int expected = 1;
        int result = field.getLeapAmount(instant);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.FEBRUARY);
        long instant = BaseDateTime.parse("2016-02-29T12:00:00.000Z").getMillis();
        boolean expected = true;
        boolean result = field.isLeap(instant);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        int expected = 12;
        int result = field.getMaximumValue();
        assertEquals(expected, result);
    }
}