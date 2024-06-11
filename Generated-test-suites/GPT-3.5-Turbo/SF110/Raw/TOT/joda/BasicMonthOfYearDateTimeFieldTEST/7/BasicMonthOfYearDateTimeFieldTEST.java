import org.junit.Test;
import org.junit.Assert;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.DateTimeFieldType;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        boolean isLenient = field.isLenient();

        Assert.assertEquals(false, isLenient);
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        int month = field.get(1599246000000L); // 2020-09-05

        Assert.assertEquals(9, month);
    }

    @Test
    public void testAddInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        long newTime = field.add(1599246000000L, 1); // 2020-10-05

        Assert.assertEquals(1601838000000L, newTime); // 2020-10-05
    }

    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        long newTime = field.add(1599246000000L, 30); // 2022-03-05

        Assert.assertEquals(1646502000000L, newTime); // 2022-03-05
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        long newTime = field.addWrapField(1599246000000L, 13); // 2021-10-05

        Assert.assertEquals(1633412400000L, newTime); // 2021-10-05
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        long newTime = field.set(1599246000000L, 12); // 2020-12-05

        Assert.assertEquals(1607113200000L, newTime); // 2020-12-05
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        boolean isLeap = field.isLeap(1607113200000L); // 2020-12-05

        Assert.assertEquals(false, isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        int leapAmount = field.getLeapAmount(1607113200000L); // 2020-12-05

        Assert.assertEquals(0, leapAmount);
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        int minimumValue = field.getMinimumValue();

        Assert.assertEquals(1, minimumValue);
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        int maximumValue = field.getMaximumValue();

        Assert.assertEquals(12, maximumValue);
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        long roundedTime = field.roundFloor(1607113200000L); // 2020-12-05

        Assert.assertEquals(1606758000000L, roundedTime); // 2020-12-01
    }

    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        long remainder = field.remainder(1607113200000L); // 2020-12-05

        Assert.assertEquals(366000000L, remainder); // 06:10:00
    }

    @Test
    public void testGetRangeDurationField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        DurationField rangeDurationField = field.getRangeDurationField();

        Assert.assertEquals(chronology.years(), rangeDurationField);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        DurationField leapDurationField = field.getLeapDurationField();

        Assert.assertEquals(chronology.days(), leapDurationField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDifferenceAsLong_Exception() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);

        long difference = field.getDifferenceAsLong(1607113200000L, 1599246000000L); // 2020-12-05, 2020-09-05
    }
}