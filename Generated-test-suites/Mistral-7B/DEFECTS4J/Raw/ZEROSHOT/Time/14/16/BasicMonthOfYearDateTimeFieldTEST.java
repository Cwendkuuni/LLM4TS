import org.joda.time.BaseTestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.JodaTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.Partial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearDateTimeField;

/**
 * Test class for {@code BasicMonthOfYearDateTimeField}
 */
public class BasicMonthOfYearDateTimeFieldTest extends BaseTestCase {

    /** The chronology to use for testing. */
    private static final Chronology CHRONOLOGY = new BasicChronology("Basic", DateTimeConstants.PROLEPTIC_GREGORIAN);

    /** The test field. */
    private MonthOfYearDateTimeField field;

    /**
     * Sets up the test.
     */
    @Override
    protected void setUp() {
        super.setUp();
        field = new MonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
    }

    /**
     * Tests the isLenient method.
     */
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    /**
     * Tests the get method.
     */
    public void testGet() {
        MutableDateTime dateTime = new MutableDateTime(JodaTime.readMilliseconds(0));
        assertEquals(1, field.get(dateTime.getMillis()));
        dateTime.setField(DateTimeFieldType.year(), 2022);
        dateTime.setField(DateTimeFieldType.monthOfYear(), 2);
        assertEquals(2, field.get(dateTime.getMillis()));
        dateTime.setField(DateTimeFieldType.monthOfYear(), 13);
        assertEquals(13, field.get(dateTime.getMillis()));
        dateTime.setField(DateTimeFieldType.millisOfDay(), DateTimeConstants.MILLIS_PER_DAY * 365);
        assertEquals(12, field.get(dateTime.getMillis()));
    }

    /**
     * Tests the add method.
     */
    public void testAdd() {
        MutableDateTime dateTime = new MutableDateTime(JodaTime.readMilliseconds(0));
        long result = field.add(dateTime.getMillis(), 1);
        assertEquals(JodaTime.readMilliseconds(result), JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY));
        result = field.add(dateTime.getMillis(), -1);
        assertEquals(JodaTime.readMilliseconds(result), JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY * -1));
        dateTime.setField(DateTimeFieldType.monthOfYear(), 12);
        result = field.add(dateTime.getMillis(), 1);
        assertEquals(JodaTime.readMilliseconds(result), JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY * 365 + DateTimeConstants.MILLIS_PER_DAY * 11));
        result = field.add(dateTime.getMillis(), -12);
        assertEquals(JodaTime.readMilliseconds(result), JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY * 365 * -1 + DateTimeConstants.MILLIS_PER_DAY * 11 * -1));
    }

    /**
     * Tests the addWrapField method.
     */
    public void testAddWrapField() {
        MutableDateTime dateTime = new MutableDateTime(JodaTime.readMilliseconds(0));
        LocalDate date = new LocalDate(CHRONOLOGY, dateTime);
        assertEquals(1, field.addWrapField(dateTime.getMillis(), 1));
        assertEquals(2, field.addWrapField(dateTime.getMillis(), 12));
        assertEquals(1, field.addWrapField(dateTime.getMillis(), -12));
        assertEquals(1, field.addWrapField(dateTime.getMillis(), -13));
        LocalDateTime datetime = new LocalDateTime(CHRONOLOGY, dateTime);
        assertEquals(1, field.addWrapField(datetime.getMillis(), 1));
        assertEquals(2, field.addWrapField(datetime.getMillis(), 12));
        assertEquals(1, field.addWrapField(datetime.getMillis(), -12));
        assertEquals(1, field.addWrapField(datetime.getMillis(), -13));
        assertEquals(1, field.addWrapField(datetime.getMillis(), Integer.MIN_VALUE));
        assertEquals(12, field.addWrapField(datetime.getMillis(), Integer.MAX_VALUE));
    }

    /**
     * Tests the getDifferenceAsLong method.
     */
    public void testGetDifferenceAsLong() {
        MutableDateTime dateTime1 = new MutableDateTime(JodaTime.readMilliseconds(0));
        MutableDateTime dateTime2 = new MutableDateTime(JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(1, field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis()));
        dateTime1.setField(DateTimeFieldType.year(), 2022);
        dateTime1.setField(DateTimeFieldType.monthOfYear(), 2);
        dateTime2.setField(DateTimeFieldType.year(), 2022);
        dateTime2.setField(DateTimeFieldType.monthOfYear(), 3);
        assertEquals(1, field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis()));
        dateTime1.setField(DateTimeFieldType.year(), 2022);
        dateTime1.setField(DateTimeFieldType.monthOfYear(), 13);
        dateTime2.setField(DateTimeFieldType.year(), 2022);
        dateTime2.setField(DateTimeFieldType.monthOfYear(), 1);
        assertEquals(11, field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis()));
        dateTime1.setField(DateTimeFieldType.year(), 2022);
        dateTime1.setField(DateTimeFieldType.monthOfYear(), DateTimeConstants.JANUARY);
        dateTime2.setField(DateTimeFieldType.year(), 2021);
        assertEquals(365, field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis()));
        dateTime1.setField(DateTimeFieldType.year(), 2100);
        dateTime1.setField(DateTimeFieldType.monthOfYear(), DateTimeConstants.DECEMBER);
        dateTime2.setField(DateTimeFieldType.year(), 1900);
        assertEquals(3153600000L, field.getDifferenceAsLong(dateTime1.getMillis(), dateTime2.getMillis()));
    }

    /**
     * Tests the set method.
     */
    public void testSet() {
        MutableDateTime dateTime = new MutableDateTime(JodaTime.readMilliseconds(0));
        field.set(dateTime.getMillis(), 13);
        assertEquals(13, iChronology.getMonthOfYear(dateTime.getMillis()));
        field.set(dateTime.getMillis(), 0);
        assertEquals(1, iChronology.getMonthOfYear(dateTime.getMillis()));
        field.set(dateTime.getMillis(), 13 + iChronology.getMaximumValue());
        assertEquals(1, iChronology.getMonthOfYear(dateTime.getMillis()));
        LocalDateTime datetime = new LocalDateTime(CHRONOLOGY, new LocalDate(CHRONOLOGY, new LocalDate(2022, 2, 29)));
        field.set(datetime.getMillis(), 13);
        assertEquals(13, iChronology.getMonthOfYear(datetime.getMillis()));
        field.set(datetime.getMillis(), 0);
        assertEquals(1, iChronology.getMonthOfYear(datetime.getMillis()));
        field.set(datetime.getMillis(), 13 + iChronology.getMaximumValue());
        assertEquals(1, iChronology.getMonthOfYear(datetime.getMillis()));
    }

    /**
     * Tests the getRangeDurationField method.
     */
    public void testGetRangeDurationField() {
        DurationField range = field.getRangeDurationField();
        assertEquals(DateTimeFieldType.YEARS, range.getFieldType());
        assertEquals(12, range.getValues()[0]);
        assertEquals(1, range.getValues()[1]);
    }

    /**
     * Tests the isLeap method.
     */
    public void testIsLeap() {
        MutableDateTime dateTime = new MutableDateTime(JodaTime.readMilliseconds(0));
        assertTrue(field.isLeap(dateTime.getMillis()));
        dateTime.setField(DateTimeFieldType.year(), 2000);
        assertFalse(field.isLeap(dateTime.getMillis()));
        dateTime.setField(DateTimeFieldType.year(), 2004);
        assertTrue(field.isLeap(dateTime.getMillis()));
    }

    /**
     * Tests the getLeapAmount method.
     */
    public void testGetLeapAmount() {
        MutableDateTime dateTime = new MutableDateTime(JodaTime.readMilliseconds(0));
        assertEquals(0, field.getLeapAmount(dateTime.getMillis()));
        dateTime.setField(DateTimeFieldType.year(), 2000);
        assertEquals(0, field.getLeapAmount(dateTime.getMillis()));
        dateTime.setField(DateTimeFieldType.year(), 2004);
        assertEquals(1, field.getLeapAmount(dateTime.getMillis()));
    }

    /**
     * Tests the getLeapDurationField method.
     */
    public void testGetLeapDurationField() {
        DurationField leap = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.DAYS, leap.getFieldType());
        assertEquals(1, leap.getValues()[0]);
    }

    /**
     * Tests the getMinimumValue method.
     */
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    /**
     * Tests the getMaximumValue method.
     */
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }

    /**
     * Tests the roundFloor method.
     */
    public void testRoundFloor() {
        MutableDateTime dateTime1 = new MutableDateTime(JodaTime.readMilliseconds(0));
        MutableDateTime dateTime2 = new MutableDateTime(JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY + 500));
        assertEquals(JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY), field.roundFloor(dateTime1.getMillis()));
        assertEquals(JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY), field.roundFloor(dateTime2.getMillis()));
    }

    /**
     * Tests the remainder method.
     */
    public void testRemainder() {
        MutableDateTime dateTime = new MutableDateTime(JodaTime.readMilliseconds(0));
        assertEquals(0, field.remainder(dateTime.getMillis()));
        dateTime.setField(DateTimeFieldType.monthOfYear(), 13);
        assertEquals(field.getMaximumValue(), field.remainder(dateTime.getMillis()));
        dateTime.setField(DateTimeFieldType.millisOfDay(), DateTimeConstants.MILLIS_PER_DAY + 500);
        assertEquals(500, field.remainder(dateTime.getMillis()));
    }

    /**
     * Tests the serialization.
     */
    public void testSerialization() {
        field = new MonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        byte[] bytes = new byte[0];
        try {
            bytes = JodaTime.serialize(field);
            MonthOfYearDateTimeField field2 = (MonthOfYearDateTimeField) JodaTime.parse(bytes);
            assertEquals(field, field2);
        } catch (Exception e) {
            fail("Failed to serialize: " + e.getMessage());
        }
    }

    /**
     * Tests the add method with a {@code ReadablePartial}.
     */
    public void testAddWithReadablePartial() {
        LocalDateTime dateTime = new LocalDateTime(JodaTime.readMilliseconds(0));
        Partial partial = new Partial(DateTimeFieldType.MONTH_OF_YEAR, 1);
        LocalDateTime result = new LocalDateTime(CHRONOLOGY, dateTime);
        field.add(partial, DateTimeFieldType.MONTH_OF_YEAR, new int[]{1}, 1);
        assertEquals(result.getMillis(), JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY));
        result = new LocalDateTime(CHRONOLOGY, dateTime.plusMonths(1));
        partial = new Partial(DateTimeFieldType.MONTH_OF_YEAR, -12);
        field.add(partial, DateTimeFieldType.MONTH_OF_YEAR, new int[]{1}, -12);
        assertEquals(result.getMillis(), JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY * 365 * -1 + DateTimeConstants.MILLIS_PER_DAY * 11 * -1));
        partial = new Partial(DateTimeFieldType.MONTH_OF_YEAR, Integer.MIN_VALUE);
        field.add(partial, DateTimeFieldType.MONTH_OF_YEAR, new int[]{1}, Integer.MIN_VALUE);
        assertEquals(result.getMillis(), JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY));
        partial = new Partial(DateTimeFieldType.MONTH_OF_YEAR, Integer.MAX_VALUE);
        field.add(partial, DateTimeFieldType.MONTH_OF_YEAR, new int[]{1}, Integer.MAX_VALUE);
        assertEquals(result.getMillis(), JodaTime.readMilliseconds(DateTimeConstants.MILLIS_PER_DAY));
    }

    /**
     * Tests the addWrapField method with a {@code ReadablePartial}.
     */
    public void testAddWrapFieldWithReadablePartial() {
        LocalDateTime dateTime = new LocalDateTime(JodaTime.readMilliseconds(0));
        Partial partial = new Partial(DateTimeFieldType.MONTH_OF_YEAR, 1);
        LocalDateTime result = new LocalDateTime(CHRONOLOGY, dateTime);
        assertEquals(result.getMillis(), FieldUtils.wrapValue(FieldUtils.getWrappedValue(1, 12, 1, 12), 1, 1));
        result = new LocalDateTime(CHRONOLOGY, dateTime.plusMonths(12));
        partial = new Partial(DateTimeFieldType.MONTH_OF_YEAR, -12);
        assertEquals(result.getMillis(), FieldUtils.wrapValue(FieldUtils.getWrappedValue(12, 1, 1, 12), 1, 1));
        partial = new Partial(DateTimeFieldType.MONTH_OF_YEAR, Integer.MIN_VALUE);
        assertEquals(result.getMillis(), FieldUtils.wrapValue(FieldUtils.getWrappedValue(1, 12, 1, 12), 1, 1));
        partial = new Partial(DateTimeFieldType.MONTH_OF_YEAR, Integer.MAX_VALUE);
        assertEquals(result.getMillis(), FieldUtils.wrapValue(FieldUtils.getWrappedValue(1, 12, 1, 12), 1, 1));
    }
}