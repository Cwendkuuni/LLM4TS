import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.junit.Assert;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1);
        Assert.assertEquals(1, period.getValue(0));
    }

    @Test
    public void testToDurationFrom() {
        BasePeriod period = new BasePeriod(1);
        DateTime dt = new DateTime();
        Duration duration = period.toDurationFrom(dt);
        Assert.assertEquals(dt.getMillis() + 1, duration.getMillis());
    }

    @Test
    public void testToDurationTo() {
        BasePeriod period = new BasePeriod(1);
        DateTime dt = new DateTime();
        Duration duration = period.toDurationTo(dt);
        Assert.assertEquals(dt.getMillis() - 1, duration.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAndUpdate() {
        BasePeriod period = new BasePeriod(1);
        period.checkAndUpdate(DurationFieldType.months(), new int[8], 1);
    }

    @Test
    public void testSetPeriod() {
        BasePeriod period = new BasePeriod(1);
        period.setPeriod((ReadablePeriod) null);
        Assert.assertEquals(0, period.getValue(0));
    }

    @Test
    public void testSetPeriodInternal() {
        BasePeriod period = new BasePeriod(1);
        MutablePeriod mutablePeriod = new MutablePeriod(2);
        period.setPeriodInternal(mutablePeriod);
        Assert.assertEquals(2, period.getValue(0));
    }

    @Test
    public void testSetPeriod2() {
        BasePeriod period = new BasePeriod(1);
        period.setPeriod(1, 2, 3, 4, 5, 6, 7, 8);
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
    public void testMergePeriod() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        Period other = new Period(1);
        period.mergePeriod(other);
        Assert.assertEquals(1, period.getValue(0));
    }

    @Test
    public void testMergePeriodInto() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        int[] values = new int[8];
        values[0] = 1;
        period.mergePeriodInto(values, null);
        Assert.assertEquals(1, period.getValue(0));
    }

    @Test
    public void testAddPeriod() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        Period other = new Period(1);
        period.addPeriod(other);
        Assert.assertEquals(1, period.getValue(0));
    }

    @Test
    public void testAddPeriodInto() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        int[] values = new int[8];
        values[0] = 1;
        period.addPeriodInto(values, null);
        Assert.assertEquals(1, period.getValue(0));
    }

    @Test
    public void testSetValue() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        period.setValue(0, 1);
        Assert.assertEquals(1, period.getValue(0));
    }

    @Test
    public void testSetValues() {
        BasePeriod period = new BasePeriod(new int[8], PeriodType.standard());
        int[] values = new int[8];
        values[0] = 1;
        period.setValues(values);
        Assert.assertEquals(1, period.getValue(0));
    }
}