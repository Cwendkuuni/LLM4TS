import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Assert;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testConstructor1() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Assert.assertEquals(1, basePeriod.getValue(0));
    }

    @Test
    public void testConstructor2() {
        DateTime dateTime = new DateTime();
        BasePeriod basePeriod = new BasePeriod(dateTime, dateTime.plusDays(1), PeriodType.standard());
        Assert.assertEquals(0, basePeriod.getValue(0));
    }

    @Test
    public void testConstructor3() {
        DateTime dateTime1 = new DateTime();
        DateTime dateTime2 = dateTime1.plusDays(2);
        BasePeriod basePeriod = new BasePeriod(dateTime1, dateTime2, PeriodType.standard());
        Assert.assertEquals(2, basePeriod.getValue(3));
    }

    @Test
    public void testConstructor4() {
        DateTime dateTime = new DateTime();
        Period period = new Period(dateTime, dateTime.plusDays(2));
        BasePeriod basePeriod = new BasePeriod(period, PeriodType.standard(), null);
        Assert.assertEquals(0, basePeriod.getValue(1));
    }

    @Test
    public void testGetPeriodType() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Assert.assertEquals(PeriodType.standard(), basePeriod.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Assert.assertEquals(1, basePeriod.getValue(0));
    }

    @Test
    public void testToDurationFrom() {
        DateTime dateTime1 = new DateTime();
        DateTime dateTime2 = dateTime1.plusDays(1);
        BasePeriod basePeriod = new BasePeriod(dateTime1, dateTime2, PeriodType.standard());
        Assert.assertEquals(86400000, basePeriod.toDurationFrom(dateTime1).getMillis());
    }

    @Test
    public void testToDurationTo() {
        DateTime dateTime1 = new DateTime();
        DateTime dateTime2 = dateTime1.plusDays(1);
        BasePeriod basePeriod = new BasePeriod(dateTime1, dateTime2, PeriodType.standard());
        Assert.assertEquals(86400000, basePeriod.toDurationTo(dateTime2).getMillis());
    }

    @Test
    public void testSetField() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        basePeriod.setField(PeriodType.standard().years(), 2);
        Assert.assertEquals(2, basePeriod.getValue(0));
    }

    @Test
    public void testAddField() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        basePeriod.addField(PeriodType.standard().years(), 3);
        Assert.assertEquals(4, basePeriod.getValue(0));
    }

    @Test
    public void testMergePeriod() {
        BasePeriod basePeriod1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod basePeriod2 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
        basePeriod1.mergePeriod(basePeriod2);
        Assert.assertEquals(4, basePeriod1.getValue(0));
    }

    @Test
    public void testAddPeriod() {
        BasePeriod basePeriod1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod basePeriod2 = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
        basePeriod1.addPeriod(basePeriod2);
        Assert.assertEquals(3, basePeriod1.getValue(7));
    }

    @Test
    public void testSetValue() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        basePeriod.setValue(0, 5);
        Assert.assertEquals(5, basePeriod.getValue(0));
    }

    @Test
    public void testSetValues() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        int[] values = {5, 6, 7, 8, 9, 10, 11, 12};
        basePeriod.setValues(values);
        Assert.assertEquals(5, basePeriod.getValue(0));
    }

}