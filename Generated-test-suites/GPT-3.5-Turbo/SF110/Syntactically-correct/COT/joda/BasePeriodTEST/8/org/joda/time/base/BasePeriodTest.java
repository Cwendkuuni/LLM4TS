import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.convert.*;
import org.joda.time.field.*;
import org.junit.Assert;
import org.junit.Test;

public class BasePeriodTest {
    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1);
        Assert.assertEquals(0, period.getValue(0));
        Assert.assertEquals(0, period.getValue(1));
        Assert.assertEquals(0, period.getValue(2));
        Assert.assertEquals(0, period.getValue(3));
        Assert.assertEquals(1, period.getValue(4));
        Assert.assertEquals(0, period.getValue(5));
        Assert.assertEquals(0, period.getValue(6));
        Assert.assertEquals(0, period.getValue(7));
    }

    @Test
    public void testToDurationFrom() {
        ReadableInstant instant = new DateTime();
        BasePeriod period = new BasePeriod(1);
        Duration duration = period.toDurationFrom(instant);
        Assert.assertNotNull(duration);
        Assert.assertEquals(instant.getMillis(), duration.getStartMillis());
        Assert.assertEquals(instant.getMillis() + 1, duration.getEndMillis());
    }

    @Test
    public void testToDurationTo() {
        ReadableInstant instant = new DateTime();
        BasePeriod period = new BasePeriod(1);
        Duration duration = period.toDurationTo(instant);
        Assert.assertNotNull(duration);
        Assert.assertEquals(instant.getMillis() - 1, duration.getStartMillis());
        Assert.assertEquals(instant.getMillis(), duration.getEndMillis());
    }

    @Test
    public void testSetField() {
        BasePeriod period = new BasePeriod(1);
        period.setField(DurationFieldType.hours(), 2);
        Assert.assertEquals(0, period.getValue(0));
        Assert.assertEquals(0, period.getValue(1));
        Assert.assertEquals(0, period.getValue(2));
        Assert.assertEquals(0, period.getValue(3));
        Assert.assertEquals(1, period.getValue(4));
        Assert.assertEquals(2, period.getValue(5));
        Assert.assertEquals(0, period.getValue(6));
        Assert.assertEquals(0, period.getValue(7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFieldException() {
        BasePeriod period = new BasePeriod(1);
        period.setField(DurationFieldType.years(), 2);
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(1);
        period.setField(DurationFieldType.hours(), 2);
        period.addField(DurationFieldType.hours(), 3);
        Assert.assertEquals(0, period.getValue(0));
        Assert.assertEquals(0, period.getValue(1));
        Assert.assertEquals(0, period.getValue(2));
        Assert.assertEquals(0, period.getValue(3));
        Assert.assertEquals(1, period.getValue(4));
        Assert.assertEquals(5, period.getValue(5));
        Assert.assertEquals(0, period.getValue(6));
        Assert.assertEquals(0, period.getValue(7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFieldException() {
        BasePeriod period = new BasePeriod(1);
        period.addField(DurationFieldType.years(), 2);
    }

    @Test
    public void testMergePeriod() {
        BasePeriod period = new BasePeriod(1);
        BasePeriod otherPeriod = new BasePeriod(2);
        period.mergePeriod(otherPeriod);
        Assert.assertEquals(0, period.getValue(0));
        Assert.assertEquals(0, period.getValue(1));
        Assert.assertEquals(0, period.getValue(2));
        Assert.assertEquals(0, period.getValue(3));
        Assert.assertEquals(3, period.getValue(4));
        Assert.assertEquals(0, period.getValue(5));
        Assert.assertEquals(0, period.getValue(6));
        Assert.assertEquals(0, period.getValue(7));
    }

    @Test
    public void testAddPeriod() {
        BasePeriod period = new BasePeriod(1);
        BasePeriod otherPeriod = new BasePeriod(2);
        period.addPeriod(otherPeriod);
        Assert.assertEquals(0, period.getValue(0));
        Assert.assertEquals(0, period.getValue(1));
        Assert.assertEquals(0, period.getValue(2));
        Assert.assertEquals(0, period.getValue(3));
        Assert.assertEquals(3, period.getValue(4));
        Assert.assertEquals(0, period.getValue(5));
        Assert.assertEquals(0, period.getValue(6));
        Assert.assertEquals(0, period.getValue(7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPeriodException() {
        BasePeriod period = new BasePeriod(1);
        period.addPeriod(null);
    }

    @Test
    public void testSetValue() {
        BasePeriod period = new BasePeriod(1);
        period.setValue(2, 3);
        Assert.assertEquals(3, period.getValue(2));
    }

    @Test
    public void testSetValues() {
        BasePeriod period = new BasePeriod(1);
        period.setValues(new int[]{2, 3, 4, 5, 6, 7, 8, 9});
        Assert.assertEquals(9, period.getValue(7));
    }
}

