import org.joda.time.BasicDateTime;
import org.joda.time.BasicMonthOfYearDateTimeField;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.MockChronology;
import org.joda.time.MutableDateTime;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final Chronology ISO_8601 = DateTimeUtils.iso8601Utc();

    @Test
    public void testBasic() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, DateTimeConstants.JANUARY);
        MutableDateTime date = new MutableDateTime(0L, 0, 1, 0, 0, 0, 0);
        assertEquals(1, field.get(date.getMillis()));
        assertEquals(1, field.get(new BasicDateTime(2001, 1, 1, 0, 0, 0, 0).getMillis()));
        assertEquals(12, field.get(new BasicDateTime(2001, 12, 31, 0, 0, 0, 0).getMillis()));
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());

        date = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.SECOND_OF_MINUTE,
                0, DateTimeConstants.DAY_OF_MONTH, DateTimeConstants.MONTH_OF_YEAR, DateTimeConstants.YEAR, DateTimeConstants.DAY_OF_YEAR);
        assertEquals(DateTimeConstants.MONTH_OF_YEAR, field.get(date.getMillis()));

        assertEquals(1, field.get(new BasicDateTime(2001, 1, 1, 0, 0, 0, 1).getMillis()));
        assertEquals(12, field.get(new BasicDateTime(2001, 12, 31, 0, 0, 0, -1).getMillis()));
        assertEquals(1, field.get(new BasicDateTime(2001, 1, 31, 0, 0, 0, 1).getMillis()));
        assertEquals(11, field.get(new BasicDateTime(2001, 11, 30, 0, 0, 0, -1).getMillis()));
        assertEquals(12, field.get(new BasicDateTime(2001, 12, 31, 0, 0, 0, 1).getMillis()));
        assertEquals(1, field.get(new BasicDateTime(2001, 1, 31, 0, 0, 0, 1).getMillis()));

        assertEquals(new BasicDateTime(2001, 2, 28, 0, 0, 0, 0).getMillis(),
                field.add(new BasicDateTime(2001, 2, 29, 0, 0, 0, 0).getMillis(), -1).longValue());
        assertEquals(new BasicDateTime(2001, 2, 29, 0, 0, 0, 0).getMillis(),
                field.add(new BasicDateTime(2001, 2, 28, 0, 0, 0, 0).getMillis(), 1).longValue());

        MutableDateTime dateBeforeMin = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.SECOND_OF_MINUTE,
                0, DateTimeConstants.DAY_OF_MONTH - 1, DateTimeConstants.MONTH_OF_YEAR, DateTimeConstants.YEAR, DateTimeConstants.DAY_OF_YEAR);
        MutableDateTime dateMax = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.SECOND_OF_MINUTE,
                0, DateTimeConstants.DAY_OF_MONTH - 1, DateTimeConstants.MONTH_OF_YEAR + 1, DateTimeConstants.YEAR, DateTimeConstants.DAY_OF_YEAR);
        long min = field.getMinimumValue();
        long max = field.getMaximumValue();
        long expectedMin = dateBeforeMin.getMillis();
        long expectedMax = dateMax.getMillis();
        assertTrue(field.isValidValue(min));
        assertTrue(field.isValidValue(max));
        assertTrue(field.isValidValue(min - 1));
        assertTrue(field.isValidValue(max + 1));
        assertFalse(field.isValidValue(min - 2));
        assertFalse(field.isValidValue(max + 2));
        assertEquals(min, field.roundFloor(min));
        assertEquals(min, field.roundFloor(min - 1));
        assertEquals(max, field.roundFloor(max));
        assertEquals(max, field.roundFloor(max + 1));
        assertEquals(0, field.remainder(min));
        assertEquals(0, field.remainder(max));

        MockChronology chronology = new MockChronology() {
            @Override
            public int getMaxMonth() {
                return 13;
            }
        };
        BasicMonthOfYearDateTimeField fieldWithInvalidChronology = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        assertTrue(fieldWithInvalidChronology.isLenient());
        assertEquals(Integer.MIN_VALUE, fieldWithInvalidChronology.getMinimumValue());
        assertEquals(Integer.MAX_VALUE, fieldWithInvalidChronology.getMaximumValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLeapMonth() {
        new BasicMonthOfYearDateTimeField(ISO_8601, DateTimeConstants.FEBRUARY - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMinYear() {
        new BasicMonthOfYearDateTimeField(ISO_8601, 1970);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxYear() {
        new BasicMonthOfYearDateTimeField(ISO_8601, 2001 + 100);
    }

    @Test
    public void testGetters() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, DateTimeConstants.JANUARY);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(false, field.isLenient());
        assertEquals(1, field.getMinimumValue());
        assertEquals(12, field.getMaximumValue());
        assertEquals(1, field.getLeapAmount(new BasicDateTime(2000, DateTimeConstants.FEBRUARY, 29, 0, 0, 0, 0).getMillis()));
        assertEquals(1, field.getLeapAmount(new BasicDateTime(2001, DateTimeConstants.FEBRUARY, 28, 0, 0, 0, 0).getMillis()));
        assertEquals(30, field.getLeapDurationField().get(new BasicDateTime(2000, DateTimeConstants.FEBRUARY, 29, 0, 0, 0, 0).getMillis()));
        assertEquals(28, field.getLeapDurationField().get(new BasicDateTime(2001, DateTimeConstants.FEBRUARY, 28, 0, 0, 0, 0).getMillis()));
        assertEquals(DateTimeFieldType.monthOfYear(), field.getRangeDurationField());
        assertEquals(12, field.getDifferenceAsLong(new BasicDateTime(2001, 1, 1, 0, 0, 0, 0).getMillis(),
                new BasicDateTime(2001, 12, 31, 0, 0, 0, 0).getMillis()));
        assertSame(ISO_8601, field.getChronology());
        assertNotNull(field.serialVersionUID);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, DateTimeConstants.JANUARY);
        MutableDateTime date = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.SECOND_OF_MINUTE,
                0, DateTimeConstants.DAY_OF_MONTH, DateTimeConstants.MONTH_OF_YEAR, DateTimeConstants.YEAR, DateTimeConstants.DAY_OF_YEAR);
        long instant = date.getMillis();
        long monthToAdd = 5;
        long expectedMonth = ((DateTimeConstants.MONTH_OF_YEAR + monthToAdd) % DateTimeConstants.MONTHS_PER_YEAR) + 1;
        long expectedYear = DateTimeConstants.YEAR_2000 + (DateTimeConstants.MONTHS_PER_YEAR * (DateTimeConstants.MONTH_OF_YEAR / DateTimeConstants.MONTHS_PER_YEAR))
                + (DateTimeConstants.MONTH_OF_YEAR % DateTimeConstants.MONTHS_PER_YEAR > 0 ? 1 : 0);
        MutableDateTime expectedDate = new MutableDateTime(expectedYear, expectedMonth, 1, 0, 0, 0, 0);
        MutableDateTime result = new MutableDateTime(field.add(instant, monthToAdd));
        assertEquals(expectedDate.getMillis(), result.getMillis());
        assertEquals(expectedMonth, field.get(result.getMillis()));
        assertEquals(expectedYear, iChronology.getYear(result.getMillis()));

        long dateBeforeMin = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.SECOND_OF_MINUTE,
                0, DateTimeConstants.DAY_OF_MONTH - 1, DateTimeConstants.MONTH_OF_YEAR, DateTimeConstants.YEAR, DateTimeConstants.DAY_OF_YEAR).getMillis();
        long dateMax = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.SECOND_OF_MINUTE,
                0, DateTimeConstants.DAY_OF_MONTH - 1, DateTimeConstants.MONTH_OF_YEAR + 1, DateTimeConstants.YEAR, DateTimeConstants.DAY_OF_YEAR).getMillis();
        assertEquals(dateBeforeMin, field.add(dateMax, -6));
        assertEquals(dateMax, field.add(dateBeforeMin, 6));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, DateTimeConstants.JANUARY);
        MutableDateTime date = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.SECOND_OF_MINUTE,
                0, DateTimeConstants.DAY_OF_MONTH, DateTimeConstants.MONTH_OF_YEAR, DateTimeConstants.YEAR, DateTimeConstants.DAY_OF_YEAR);
        assertEquals(new BasicDateTime(DateTimeConstants.YEAR_2002, 1, 1, 0, 0, 0, 0).getMillis(),
                field.addWrapField(date.getMillis(), 13));
        assertEquals(new BasicDateTime(DateTimeConstants.YEAR_2001, 12, 31, 0, 0, 0, 0).getMillis(),
                field.addWrapField(date.getMillis(), -12));
    }
}