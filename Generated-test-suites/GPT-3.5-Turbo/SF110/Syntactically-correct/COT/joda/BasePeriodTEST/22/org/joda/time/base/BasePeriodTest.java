import org.joda.time.*;
import org.junit.Assert;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        Assert.assertEquals(1, period.getValue(0));
        Assert.assertEquals(2, period.getValue(1));
        Assert.assertEquals(3, period.getValue(2));
        Assert.assertEquals(4, period.getValue(3));
        Assert.assertEquals(5, period.getValue(4));
        Assert.assertEquals(6, period.getValue(5));
        Assert.assertEquals(7, period.getValue(6));
        Assert.assertEquals(8, period.getValue(7));
    }

    @Test
    public void testToDurationFrom() {
        DateTime start = new DateTime(2022, 1, 1, 0, 0, 0);
        DateTime end = new DateTime(2022, 1, 1, 1, 0, 0);
        BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
        Duration duration = period.toDurationFrom(start);
        Assert.assertEquals(3600000L, duration.getMillis());
    }

    @Test
    public void testToDurationTo() {
        DateTime start = new DateTime(2022, 1, 1, 0, 0, 0);
        DateTime end = new DateTime(2022, 1, 1, 1, 0, 0);
        BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
        Duration duration = period.toDurationTo(end);
        Assert.assertEquals(3600000L, duration.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFieldInvalid() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period.setField(DurationFieldType.years(), 10);
    }

    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period.setField(DurationFieldType.months(), 10);
        Assert.assertEquals(10, period.getValue(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFieldInvalid() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period.addField(DurationFieldType.years(), 10);
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period.addField(DurationFieldType.months(), 10);
        Assert.assertEquals(12, period.getValue(1));
    }

    @Test
    public void testSetPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period.setPeriod(10, 20, 30, 40, 50, 60, 70, 80);
        Assert.assertEquals(10, period.getValue(0));
        Assert.assertEquals(20, period.getValue(1));
        Assert.assertEquals(30, period.getValue(2));
        Assert.assertEquals(40, period.getValue(3));
        Assert.assertEquals(50, period.getValue(4));
        Assert.assertEquals(60, period.getValue(5));
        Assert.assertEquals(70, period.getValue(6));
        Assert.assertEquals(80, period.getValue(7));
    }

    @Test
    public void testMergePeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        BasePeriod mergePeriod = new BasePeriod(10, 20, 30, 40, 50, 60, 70, 80);
        period.mergePeriod(mergePeriod);
        Assert.assertEquals(11, period.getValue(0));
        Assert.assertEquals(22, period.getValue(1));
        Assert.assertEquals(33, period.getValue(2));
        Assert.assertEquals(44, period.getValue(3));
        Assert.assertEquals(55, period.getValue(4));
        Assert.assertEquals(66, period.getValue(5));
        Assert.assertEquals(77, period.getValue(6));
        Assert.assertEquals(88, period.getValue(7));
    }

    @Test
    public void testAddPeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        BasePeriod addPeriod = new BasePeriod(10, 20, 30, 40, 50, 60, 70, 80);
        period.addPeriod(addPeriod);
        Assert.assertEquals(11, period.getValue(0));
        Assert.assertEquals(22, period.getValue(1));
        Assert.assertEquals(33, period.getValue(2));
        Assert.assertEquals(44, period.getValue(3));
        Assert.assertEquals(55, period.getValue(4));
        Assert.assertEquals(66, period.getValue(5));
        Assert.assertEquals(77, period.getValue(6));
        Assert.assertEquals(88, period.getValue(7));
    }

}