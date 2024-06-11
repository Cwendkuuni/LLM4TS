import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.JodaTimeTestCase;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearField;
import org.joda.time.chrono.BasicChronology;

public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    public void testBasicMonthOfYearField() {
        BasicChronology gregorian = new BasicChronology("gregorian", Locale.ENGLISH);
        MonthOfYearField monthField = new MonthOfYearField(gregorian, DateTimeConstants.JANUARY);
        // test isLenient
        assertEquals(false, monthField.isLenient());
        // test get()
        long instant = BaseDateTime.INSTANCE.getMillis();
        assertEquals(12, monthField.get(instant));
        // test set()
        long newInstant = monthField.set(instant, 5);
        assertEquals(1, newInstant % (24 * 60 * 60 * 1000L)); // check the day part is reset
        assertEquals(5, monthField.get(newInstant));
        // test add()
        long newInstant2 = monthField.add(instant, 2);
        assertEquals(3, monthField.get(newInstant2));
        // test addWrapField()
        long newInstant3 = monthField.addWrapField(instant, 14);
        assertEquals(14, monthField.get(newInstant3));
        // test getDifferenceAsLong()
        long tooFar = newInstant3 + 365 * 24 * 60 * 60 * 1000;
        long diff = monthField.getDifferenceAsLong(instant, tooFar);
        assertEquals(14, diff);
        // test set() with invalid month
        try {
            monthField.set(instant, 13);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        // test add() with invalid amount
        try {
            monthField.add(instant, 13);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        // test addWrapField() with invalid amount
        try {
            monthField.addWrapField(instant, 13);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        // test getMinimumValue() and getMaximumValue()
        assertEquals(1, monthField.getMinimumValue());
        assertEquals(12, monthField.getMaximumValue());
        // test getRangeDurationField()
        DurationField range = monthField.getRangeDurationField();
        assertEquals(DurationFieldType.YEARS, range.getUnit());
        assertEquals(12, range.getAmount());
        // test getLeapAmount()
        assertEquals(0, monthField.getLeapAmount(instant));
        // test getLeapDurationField()
        DurationField leap = monthField.getLeapDurationField();
        assertEquals(DurationFieldType.DAYS, leap.getUnit());
        assertEquals(1, leap.getAmount());
        // test roundFloor()
        long round = monthField.roundFloor(instant);
        assertEquals(2019, iChronology.getYear(round));
        assertEquals(1, monthField.get(round));
        // test remainder()
        long remainder = monthField.remainder(instant);
        assertEquals(1, remainder % (24 * 60 * 60 * 1000L));
    }

    public void testMonthOfYearFieldWithLeapYear() {
        BasicChronology gregorian = new BasicChronology("gregorian", Locale.ENGLISH);
        MonthOfYearField monthField = new MonthOfYearField(gregorian, DateTimeConstants.FEBRUARY);
        // test isLeap()
        long leapYear = iChronology.getYear(2000);
        assertTrue(monthField.isLeap(leapYear * 365L + 61 * 24 * 60 * 60 * 1000L));
        // test getLeapAmount()
        assertEquals(1, monthField.getLeapAmount(leapYear * 365L + 61 * 24 * 60 * 60 * 1000L));
        // test getLeapDurationField()
        DurationField leap = monthField.getLeapDurationField();
        assertEquals(DurationFieldType.DAYS, leap.getUnit());
        assertEquals(29, leap.getAmount());
    }

    public void testMonthOfYearFieldWithDifferentChronology() {
        BasicChronology iso = new BasicChronology("iso-8601", Locale.ENGLISH);
        MonthOfYearField monthField = new MonthOfYearField(iso, DateTimeConstants.JANUARY);
        // test get()
        long instant = BaseDateTime.INSTANCE.getMillis();
        assertEquals(12, monthField.get(instant));
        // test set()
        long newInstant = monthField.set(instant, 5);
        assertEquals(1, newInstant % (24 * 60 * 60 * 1000L)); // check the day part is reset
        assertEquals(5, monthField.get(newInstant));
        // test add()
        long newInstant2 = monthField.add(instant, 2);
        assertEquals(3, monthField.get(newInstant2));
        // test addWrapField()
        long newInstant3 = monthField.addWrapField(instant, 14);
        assertEquals(14, monthField.get(newInstant3));
        // test getMinimumValue() and getMaximumValue()
        assertEquals(1, monthField.getMinimumValue());
        assertEquals(12, monthField.getMaximumValue());
        // test getRangeDurationField()
        DurationField range = monthField.getRangeDurationField();
        assertEquals(DurationFieldType.YEARS, range.getUnit());
        assertEquals(12, range.getAmount());
        // test getLeapAmount()
        assertEquals(0, monthField.getLeapAmount(instant));
        // test getLeapDurationField()
        DurationField leap = monthField.getLeapDurationField();
        assertEquals(DurationFieldType.DAYS, leap.getUnit());
        assertEquals(31, leap.getAmount());
    }

    @Override
    protected Chronology getChronology() {
        return new BasicChronology("test", Locale.ENGLISH);
    }

    @Override
    protected void setUp() {
        super.setUp();
        iChronology = getChronology();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        iChronology = null;
    }
}