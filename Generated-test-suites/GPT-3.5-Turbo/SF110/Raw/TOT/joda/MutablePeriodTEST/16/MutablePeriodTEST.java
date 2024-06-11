import org.joda.time.*;
import org.junit.*;

public class MutablePeriodTest {

    @Test
    public void testParseString() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3W4DT5H6M7S");
        Assert.assertEquals(1, period.getYears());
        Assert.assertEquals(2, period.getMonths());
        Assert.assertEquals(3, period.getWeeks());
        Assert.assertEquals(4, period.getDays());
        Assert.assertEquals(5, period.getHours());
        Assert.assertEquals(6, period.getMinutes());
        Assert.assertEquals(7, period.getSeconds());
    }

    @Test
    public void testParseStringWithFormatter() {
        PeriodFormatter formatter = PeriodFormatterBuilder.forFields()
                .appendYears()
                .appendSeparatorIfFieldsBefore(",")
                .appendMonths()
                .toFormatter();
        MutablePeriod period = MutablePeriod.parse("1Y,2M", formatter);
        Assert.assertEquals(1, period.getYears());
        Assert.assertEquals(2, period.getMonths());
        Assert.assertEquals(0, period.getWeeks());
        Assert.assertEquals(0, period.getDays());
        Assert.assertEquals(0, period.getHours());
        Assert.assertEquals(0, period.getMinutes());
        Assert.assertEquals(0, period.getSeconds());
    }

    @Test
    public void testConstructionWithDefaultValues() {
        MutablePeriod period = new MutablePeriod();
        Assert.assertEquals(0, period.getYears());
        Assert.assertEquals(0, period.getMonths());
        Assert.assertEquals(0, period.getWeeks());
        Assert.assertEquals(0, period.getDays());
        Assert.assertEquals(0, period.getHours());
        Assert.assertEquals(0, period.getMinutes());
        Assert.assertEquals(0, period.getSeconds());
    }

    @Test
    public void testConstructionWithPeriodType() {
        PeriodType periodType = PeriodType.forFields(new DurationFieldType[]{ DurationFieldType.days(), DurationFieldType.millis() });
        MutablePeriod period = new MutablePeriod(periodType);
        Assert.assertEquals(0, period.getYears());
        Assert.assertEquals(0, period.getMonths());
        Assert.assertEquals(0, period.getWeeks());
        Assert.assertEquals(0, period.getDays());
        Assert.assertEquals(0, period.getHours());
        Assert.assertEquals(0, period.getMinutes());
        Assert.assertEquals(0, period.getSeconds());
    }

    @Test
    public void testConstructionWithYearsMonthsDaysHours() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        Assert.assertEquals(1, period.getYears());
        Assert.assertEquals(2, period.getMonths());
        Assert.assertEquals(3, period.getWeeks());
        Assert.assertEquals(4, period.getDays());
        Assert.assertEquals(0, period.getHours());
        Assert.assertEquals(0, period.getMinutes());
        Assert.assertEquals(0, period.getSeconds());
    }

    @Test
    public void testConstructionWithAllFields() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        Assert.assertEquals(1, period.getYears());
        Assert.assertEquals(2, period.getMonths());
        Assert.assertEquals(3, period.getWeeks());
        Assert.assertEquals(4, period.getDays());
        Assert.assertEquals(5, period.getHours());
        Assert.assertEquals(6, period.getMinutes());
        Assert.assertEquals(7, period.getSeconds());
        Assert.assertEquals(8, period.getMillis());
    }

    @Test
    public void testConstructionWithAllFieldsAndPeriodType() {
        PeriodType periodType = PeriodType.yearMonthDayTime().withMillisRemoved();
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8, periodType);
        Assert.assertEquals(1, period.getYears());
        Assert.assertEquals(2, period.getMonths());
        Assert.assertEquals(3, period.getWeeks());
        Assert.assertEquals(4, period.getDays());
        Assert.assertEquals(5, period.getHours());
        Assert.assertEquals(6, period.getMinutes());
        Assert.assertEquals(7, period.getSeconds());
        Assert.assertEquals(0, period.getMillis());
    }

    // More test cases for other methods...

}