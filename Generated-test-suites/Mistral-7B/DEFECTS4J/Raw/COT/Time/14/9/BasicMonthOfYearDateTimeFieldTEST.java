import org.joda.time.BaseTestCase;
import org.joda.time.BasicChronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.JodaTime;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest extends BaseTestCase {

    private BasicMonthOfYearDateTimeField field;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        field = new BasicMonthOfYearDateTimeField(new ISOChronology(), DateTimeConstants.JANUARY);
    }

    @Test
    public void testIsLenient() {
        assertEquals(false, field.isLenient());
    }

    @Test
    public void testGet() {
        LocalDateTime dt = new LocalDateTime(JodaTime.readMilliseconds(0L), new ISOChronology());
        int expected = ISOChronology.getInstance().getMonthOfYear(dt.toDateMilleris());
        int actual = field.get(dt.getMillis());
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        long millis = JodaTime.readMilliseconds(0L);
        LocalDateTime dt = new LocalDateTime(millis, new ISOChronology());

        long added = field.add(millis, 1);
        LocalDateTime expectedAdded = dt.plusMonths(1);
        LocalDateTime actualAdded = new LocalDateTime(JodaTime.readMilliseconds(added), new ISOChronology());
        assertEquals(expectedAdded.toDate(), actualAdded.toDate());

        long addedNegative = field.add(millis, -1);
        LocalDateTime expectedAddedNegative = dt.minusMonths(1);
        LocalDateTime actualAddedNegative = new LocalDateTime(JodaTime.readMilliseconds(addedNegative), new ISOChronology());
        assertEquals(expectedAddedNegative.toDate(), actualAddedNegative.toDate());

        long addedMax = field.add(millis, Integer.MAX_VALUE);
        LocalDateTime expectedAddedMax = dt.plusMonths(Integer.MAX_VALUE);
        LocalDateTime actualAddedMax = new LocalDateTime(JodaTime.readMilliseconds(addedMax), new ISOChronology());
        assertFalse(expectedAddedMax.isBefore(expectedAddedNegative.toDate()));
        assertEquals(expectedAddedMax.toDate(), actualAddedMax.toDate());

        long addedMin = field.add(millis, Integer.MIN_VALUE);
        LocalDateTime expectedAddedMin = dt.minusMonths(Integer.MIN_VALUE);
        LocalDateTime actualAddedMin = new LocalDateTime(JodaTime.readMilliseconds(addedMin), new ISOChronology());
        assertFalse(expectedAddedMin.isAfter(expectedAddedMax.toDate()));
        assertEquals(expectedAddedMin.toDate(), actualAddedMin.toDate());
    }

    @Test
    public void testAddWrapField() {
        long millis = JodaTime.readMilliseconds(0L);
        LocalDateTime dt = new LocalDateTime(millis, new ISOChronology());

        long added = field.addWrapField(millis, 12);
        LocalDateTime expectedAdded = dt.plusYears(1).plusMonths(1);
        LocalDateTime actualAdded = new LocalDateTime(JodaTime.readMilliseconds(added), new ISOChronology());
        assertEquals(expectedAdded.toDate(), actualAdded.toDate());

        long addedNegative = field.addWrapField(millis, -12);
        LocalDateTime expectedAddedNegative = dt.minusYears(1).minusMonths(1);
        LocalDateTime actualAddedNegative = new LocalDateTime(JodaTime.readMilliseconds(addedNegative), new ISOChronology());
        assertEquals(expectedAddedNegative.toDate(), actualAddedNegative.toDate());
    }

    @Test
    public void testGetDifferenceAsLong() {
        long millisMinuend = JodaTime.readMilliseconds(0L);
        long millisSubtrahend = JodaTime.readMilliseconds(1L);

        long difference = field.getDifferenceAsLong(millisMinuend, millisSubtrahend);
        assertEquals(1, difference);

        millisSubtrahend = JodaTime.readMilliseconds(-1L);
        difference = field.getDifferenceAsLong(millisMinuend, millisSubtrahend);
        assertEquals(-1, difference);

        millisMinuend = JodaTime.readMilliseconds(Integer.MIN_VALUE + 1000);
        millisSubtrahend = JodaTime.readMilliseconds(Integer.MAX_VALUE - 1000);
        difference = field.getDifferenceAsLong(millisMinuend, millisSubtrahend);
        assertEquals(Integer.MAX_VALUE - Integer.MIN_VALUE, difference);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        long millis = JodaTime.readMilliseconds(0L);
        field.set(millis, Integer.MIN_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaximumValue() {
        long millis = JodaTime.readMilliseconds(0L);
        field.set(millis, Integer.MAX_VALUE);
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField expected = field.getRangeDurationField();
        DurationField actual = field.getRangeDurationField();
        assertEquals(expected.getUnits(), actual.getUnits());
    }

    @Test
    public void testIsLeap() {
        long millis = JodaTime.readMilliseconds(0L);
        boolean expected = ISOChronology.getInstance().isLeapYear(new ISOChronology().getYear(millis));
        boolean actual = field.isLeap(millis);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount() {
        long millis = JodaTime.readMilliseconds(0L);
        int expected = (ISOChronology.getInstance().isLeapYear(new ISOChronology().getYear(millis)) ? 1 : 0);
        int actual = field.getLeapAmount(millis);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField expected = field.getLeapDurationField();
        DurationField actual = field.getLeapDurationField();
        assertEquals(expected.getUnits(), actual.getUnits());
    }

    @Test
    public void testGetMinimumValue() {
        int expected = DateTimeConstants.JANUARY;
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        int expected = DateTimeConstants.DECEMBER;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        long millis = JodaTime.readMilliseconds(0L);
        LocalDateTime dt = new LocalDateTime(millis, new ISOChronology());
        int expected = ISOChronology.getInstance().getYear(dt.toDateMilleris());
        int expectedMonth = ISOChronology.getInstance().getMonthOfYear(dt.toDateMilleris());
        long actual = field.roundFloor(millis);
        LocalDateTime actualLocalDateTime = new LocalDateTime(JodaTime.readMilliseconds(actual), new ISOChronology());
        assertEquals(expected, actualLocalDateTime.getYear());
        assertEquals(expectedMonth, actualLocalDateTime.getMonthOfYear());
    }

    @Test
    public void testRemainder() {
        long millis = JodaTime.readMilliseconds(0L);
        long millisRounded = field.roundFloor(millis);
        long actual = field.remainder(millis);
        long expected = millis - millisRounded;
        assertEquals(expected, actual);
    }

}