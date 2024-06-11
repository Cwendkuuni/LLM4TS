import org.joda.time.BaseDateTime;
import org.joda.time.BasicDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.JodaTimeTestCase;
import org.joda.time.LocalDate;
import org.joda.time.chrono.BasicGregorianChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    private static final Chronology CHRONOLOGY = new BasicGregorianChronology();

    @Test
    public void test_newInstance() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        assertNotNull(field);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getDateTimeFieldType());
        assertEquals(DateTimeFieldType.monthOfYear(), field.getBaseDateTimeFieldType());
        assertEquals(CHRONOLOGY, field.getChronology());
        assertEquals(-8258715387168736L, field.serialVersionUID);
        assertEquals(DateTimeConstants.JANUARY, field.iLeapMonth);
    }

    @Test
    public void test_isLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
    }

    @Test
    public void test_get() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        int expected = DateTimeConstants.JANUARY;
        int result = field.get(instant);
        assertEquals(expected, result);
    }

    @Test
    public void test_get_future() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2);
        int expected = DateTimeConstants.JANUARY;
        int result = field.get(instant);
        assertEquals(expected, result);
    }

    @Test
    public void test_add() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();

        long expected1 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 30L);
        long result1 = field.add(instant, 1);
        assertEquals(expected1, result1);

        long expected2 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 366L);
        long result2 = field.add(instant, 12);
        assertEquals(expected2, result2);

        long expected3 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2 + 1);
        long result3 = field.add(instant, 720);
        assertEquals(expected3, result3);
    }

    @Test
    public void test_add_negative() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2);

        long expected1 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 30L);
        long result1 = field.add(instant, -11);
        assertEquals(expected1, result1);

        long expected2 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2 - 1);
        long result2 = field.add(instant, -719);
        assertEquals(expected2, result2);
    }

    @Test
    public void test_addWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2);

        long expected1 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 30L);
        long result1 = field.addWrapField(instant, 11);
        assertEquals(expected1, result1);

        long expected2 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2 - 1);
        long result2 = field.addWrapField(instant, -719);
        assertEquals(expected2, result2);
    }

    @Test
    public void test_getDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant1 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2);
        long instant2 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2 + 30L);

        long expected = 1;
        long result = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(expected, result);

        instant2 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2 - 365L);

        result = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(-1, result);
    }

    @Test
    public void test_set() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();

        LocalDate expected1 = new LocalDate(2000, 1, 31);
        long result1 = field.set(instant, 1);
        assertEquals(expected1.getMillis(), result1);

        LocalDate expected2 = new LocalDate(1999, 2, 29);
        long result2 = field.set(instant, 2);
        assertEquals(expected2.getMillis(), result2);

        long expected3 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2);
        long result3 = field.set(instant, 12);
        assertEquals(expected3, result3);

        long expected4 = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 2 - 1);
        long result4 = field.set(instant, 11);
        assertEquals(expected4, result4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_invalid() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        field.set(instant, 13);
    }

    @Test
    public void test_getRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        assertSame(field.getChronology().years(), field.getRangeDurationField());
    }

    @Test
    public void test_isLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();

        assertFalse(field.isLeap(instant));

        instant = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 4);
        assertTrue(field.isLeap(instant));

        instant = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 4 + 1);
        assertFalse(field.isLeap(instant));
    }

    @Test
    public void test_getLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();

        assertEquals(0, field.getLeapAmount(instant));

        instant = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 4);
        assertEquals(1, field.getLeapAmount(instant));

        instant = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 365L * 4 + 1);
        assertEquals(0, field.getLeapAmount(instant));
    }

    @Test
    public void test_getLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        assertSame(field.getChronology().days(), field.getLeapDurationField());
    }

    @Test
    public void test_getMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
    }

    @Test
    public void test_getMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
    }

    @Test
    public void test_roundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        long expected = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 31L);
        long result = field.roundFloor(instant);
        assertEquals(expected, result);
    }

    @Test
    public void test_remainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 31L);
        long expected = 0;
        long result = field.remainder(instant);
        assertEquals(expected, result);

        instant = BaseDateTime.UTC.getMillis() + (1000L * 60L * 60L * 24L * 31L + 1);
        expected = 1;
        result = field.remainder(instant);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void test_serialization() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        field.writeObject(new ByteArrayOutputStream());
    }
}