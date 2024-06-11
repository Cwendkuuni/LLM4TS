import org.joda.time.*;
import org.junit.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        String s = "P1Y2M3W4DT5H6M7S";
        MutablePeriod expected = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 0, PeriodType.standard());
        MutablePeriod actual = MutablePeriod.parse(s);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDefaultValueConstructor() {
        MutablePeriod period = new MutablePeriod();
        Assert.assertEquals(0, period.getYears());
        Assert.assertEquals(0, period.getMonths());
        Assert.assertEquals(0, period.getWeeks());
        Assert.assertEquals(0, period.getDays());
        Assert.assertEquals(0, period.getHours());
        Assert.assertEquals(0, period.getMinutes());
        Assert.assertEquals(0, period.getSeconds());
        Assert.assertEquals(0, period.getMillis());
    }

    @Test
    public void testPeriodTypeConstructor() {
        PeriodType periodType = PeriodType.yearMonthDayTime();
        MutablePeriod period = new MutablePeriod(periodType);
        Assert.assertEquals(periodType, period.getPeriodType());
        Assert.assertEquals(0, period.getYears());
        Assert.assertEquals(0, period.getMonths());
        Assert.assertEquals(0, period.getWeeks());
        Assert.assertEquals(0, period.getDays());
        Assert.assertEquals(0, period.getHours());
        Assert.assertEquals(0, period.getMinutes());
        Assert.assertEquals(0, period.getSeconds());
        Assert.assertEquals(0, period.getMillis());
    }

    // TODO: Add more test methods to comprehensively test all methods in MutablePeriod class

}