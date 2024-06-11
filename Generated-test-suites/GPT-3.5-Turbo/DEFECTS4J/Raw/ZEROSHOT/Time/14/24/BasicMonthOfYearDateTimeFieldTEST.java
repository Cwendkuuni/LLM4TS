import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField;

    public BasicMonthOfYearDateTimeFieldTest() {

        BasicChronology chronology = new BasicChronology();
        int leapMonth = 2;
        basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    }

    public void testIsLenient() {
        assertEquals(false, basicMonthOfYearDateTimeField.isLenient());
    }

    public void testGet() {
        assertEquals(1, basicMonthOfYearDateTimeField.get(1000000000));
        assertEquals(7, basicMonthOfYearDateTimeField.get(2000000000));
    }

    public void testAdd() {
        assertEquals(2000000000, basicMonthOfYearDateTimeField.add(1000000000, 12));
        assertEquals(1000000000, basicMonthOfYearDateTimeField.add(2000000000, -12));
    }

    public void testAddWrapField() {
        assertEquals(2000000000, basicMonthOfYearDateTimeField.addWrapField(1000000000, 12));
        assertEquals(1000000000, basicMonthOfYearDateTimeField.addWrapField(2000000000, -12));
    }

    public void testGetDifferenceAsLong() {
        assertEquals(12, basicMonthOfYearDateTimeField.getDifferenceAsLong(2000000000, 1000000000));
        assertEquals(-12, basicMonthOfYearDateTimeField.getDifferenceAsLong(1000000000, 2000000000));
    }

    public void testSet() {
        assertEquals(1000000000, basicMonthOfYearDateTimeField.set(2000000000, 1));
        assertEquals(1000000000, basicMonthOfYearDateTimeField.set(2000000000, 2));
    }

    public void testIsLeap() {
        assertEquals(true, basicMonthOfYearDateTimeField.isLeap(2000000000));
        assertEquals(false, basicMonthOfYearDateTimeField.isLeap(1000000000));
    }

    public void testGetLeapAmount() {
        assertEquals(1, basicMonthOfYearDateTimeField.getLeapAmount(2000000000));
        assertEquals(0, basicMonthOfYearDateTimeField.getLeapAmount(1000000000));
    }

    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, basicMonthOfYearDateTimeField.getMinimumValue());
    }

    public void testGetMaximumValue() {
        assertEquals(12, basicMonthOfYearDateTimeField.getMaximumValue());
    }

    public void testRoundFloor() {
        assertEquals(1000000000, basicMonthOfYearDateTimeField.roundFloor(1234567890));
        assertEquals(2000000000, basicMonthOfYearDateTimeField.roundFloor(2000000000));
    }

    public void testRemainder() {
        assertEquals(1234567890, basicMonthOfYearDateTimeField.remainder(1234567890));
        assertEquals(0, basicMonthOfYearDateTimeField.remainder(2000000000));
    }
}