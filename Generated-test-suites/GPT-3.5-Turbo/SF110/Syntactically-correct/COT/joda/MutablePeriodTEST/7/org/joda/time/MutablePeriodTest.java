import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.*;

public class MutablePeriodTest {

    private MutablePeriod mutablePeriod;

    @Before
    public void setUp() {
        mutablePeriod = new MutablePeriod();
    }

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3D");
        Assert.assertEquals(1, period.getYears());
        Assert.assertEquals(2, period.getMonths());
        Assert.assertEquals(3, period.getDays());
    }

    @Test
    public void testParseWithFormatter() {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendYears()
                .appendSeparator(";")
                .appendMonths()
                .appendSeparator(";")
                .appendDays()
                .toFormatter();
        MutablePeriod period = MutablePeriod.parse("1;2;3", formatter);
        Assert.assertEquals(1, period.getYears());
        Assert.assertEquals(2, period.getMonths());
        Assert.assertEquals(3, period.getDays());
    }

    @Test
    public void testDefaultConstructor() {
        Assert.assertEquals(0, mutablePeriod.getYears());
        Assert.assertEquals(0, mutablePeriod.getMonths());
        Assert.assertEquals(0, mutablePeriod.getWeeks());
        Assert.assertEquals(0, mutablePeriod.getDays());
    }

    @Test
    public void testPeriodTypeConstructor() {
        PeriodType periodType = PeriodType.standard().withYears();
        mutablePeriod = new MutablePeriod(periodType);
        Assert.assertEquals(0, mutablePeriod.getYears());
        Assert.assertEquals(0, mutablePeriod.getMonths());
        Assert.assertEquals(0, mutablePeriod.getWeeks());
        Assert.assertEquals(0, mutablePeriod.getDays());
    }

    @Test
    public void testIntConstructor() {
        mutablePeriod = new MutablePeriod(1, 2, 3, 4);
        Assert.assertEquals(1, mutablePeriod.getYears());
        Assert.assertEquals(2, mutablePeriod.getMonths());
        Assert.assertEquals(3, mutablePeriod.getWeeks());
        Assert.assertEquals(4, mutablePeriod.getDays());
    }

    @Test
    public void testLongConstructor() {
        mutablePeriod = new MutablePeriod(1000L);
        Assert.assertEquals(1000L, mutablePeriod.getMillis());
    }

    // Add the rest of the test cases for the other methods in the MutablePeriod class

}