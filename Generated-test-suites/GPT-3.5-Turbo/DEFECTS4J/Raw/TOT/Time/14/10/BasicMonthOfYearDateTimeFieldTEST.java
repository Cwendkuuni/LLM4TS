import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField monthField;

    @Before
    public void setUp() {
        BasicChronology chronology = new BasicChronology();
        monthField = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
    }

    @After
    public void tearDown() {
        monthField = null;
    }

    @Test
    public void testIsLenient() {
        boolean isLenient = monthField.isLenient();
        Assert.assertFalse(isLenient);
    }

    @Test
    public void testGet() {
        long instant = 1525737600;
        int month = monthField.get(instant);
        Assert.assertEquals(5, month);
    }

    @Test
    public void testAdd() {
        long instant = 1525737600;
        int months = 3;
        long updatedInstant = monthField.add(instant, months);
        int updatedMonth = monthField.get(updatedInstant);
        Assert.assertEquals(8, updatedMonth);
    }

    @Test
    public void testAddWrapField() {
        long instant = 1525737600;
        int months = 11;
        long updatedInstant = monthField.addWrapField(instant, months);
        int updatedMonth = monthField.get(updatedInstant);
        Assert.assertEquals(4, updatedMonth);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = 1525737600;
        long subtrahendInstant = 1525766400;
        long difference = monthField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        Assert.assertEquals(-1, difference);
    }

    @Test
    public void testSet() {
        long instant = 1525737600;
        int month = 11;
        long updatedInstant = monthField.set(instant, month);
        int updatedMonth = monthField.get(updatedInstant);
        Assert.assertEquals(11, updatedMonth);
    }

    @Test
    public void testIsLeap() {
        long instant = 1525737600;
        boolean isLeap = monthField.isLeap(instant);
        Assert.assertFalse(isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        long instant = 1525737600;
        int leapAmount = monthField.getLeapAmount(instant);
        Assert.assertEquals(0, leapAmount);
    }

    @Test
    public void testGetMinimumValue() {
        int minimumValue = monthField.getMinimumValue();
        Assert.assertEquals(DateTimeConstants.JANUARY, minimumValue);
    }

    @Test
    public void testGetMaximumValue() {
        int maximumValue = monthField.getMaximumValue();
        Assert.assertEquals(DateTimeConstants.DECEMBER, maximumValue);
    }

    @Test
    public void testRoundFloor() {
        long instant = 1525737600;
        long roundedFloorInstant = monthField.roundFloor(instant);
        Assert.assertEquals(1514764800, roundedFloorInstant);
    }

    @Test
    public void testRemainder() {
        long instant = 1525737600;
        long remainder = monthField.remainder(instant);
        Assert.assertEquals(10987200, remainder);
    }

}