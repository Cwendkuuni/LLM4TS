import org.joda.time.*;
import org.junit.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y2M3D");
        Assert.assertEquals(1, period.getYears());
        Assert.assertEquals(2, period.getMonths());
        Assert.assertEquals(3, period.getDays());
    }
    
    @Test
    public void testSetYears() {
        MutablePeriod period = new MutablePeriod();
        period.setYears(1);
        Assert.assertEquals(1, period.getYears());
    }
    
    @Test
    public void testAddYears() {
        MutablePeriod period = new MutablePeriod();
        period.addYears(1);
        Assert.assertEquals(1, period.getYears());
    }
    
    @Test
    public void testSetMonths() {
        MutablePeriod period = new MutablePeriod();
        period.setMonths(2);
        Assert.assertEquals(2, period.getMonths());
    }
    
    @Test
    public void testAddMonths() {
        MutablePeriod period = new MutablePeriod();
        period.addMonths(2);
        Assert.assertEquals(2, period.getMonths());
    }
    
    @Test
    public void testSetWeeks() {
        MutablePeriod period = new MutablePeriod();
        period.setWeeks(3);
        Assert.assertEquals(3, period.getWeeks());
    }
    
    @Test
    public void testAddWeeks() {
        MutablePeriod period = new MutablePeriod();
        period.addWeeks(3);
        Assert.assertEquals(3, period.getWeeks());
    }
    
    @Test
    public void testSetDays() {
        MutablePeriod period = new MutablePeriod();
        period.setDays(4);
        Assert.assertEquals(4, period.getDays());
    }
    
    @Test
    public void testAddDays() {
        MutablePeriod period = new MutablePeriod();
        period.addDays(4);
        Assert.assertEquals(4, period.getDays());
    }
    
    @Test
    public void testSetHours() {
        MutablePeriod period = new MutablePeriod();
        period.setHours(5);
        Assert.assertEquals(5, period.getHours());
    }
    
    @Test
    public void testAddHours() {
        MutablePeriod period = new MutablePeriod();
        period.addHours(5);
        Assert.assertEquals(5, period.getHours());
    }
    
    @Test
    public void testSetMinutes() {
        MutablePeriod period = new MutablePeriod();
        period.setMinutes(6);
        Assert.assertEquals(6, period.getMinutes());
    }
    
    @Test
    public void testAddMinutes() {
        MutablePeriod period = new MutablePeriod();
        period.addMinutes(6);
        Assert.assertEquals(6, period.getMinutes());
    }
    
    @Test
    public void testSetSeconds() {
        MutablePeriod period = new MutablePeriod();
        period.setSeconds(7);
        Assert.assertEquals(7, period.getSeconds());
    }
    
    @Test
    public void testAddSeconds() {
        MutablePeriod period = new MutablePeriod();
        period.addSeconds(7);
        Assert.assertEquals(7, period.getSeconds());
    }
    
    @Test
    public void testSetMillis() {
        MutablePeriod period = new MutablePeriod();
        period.setMillis(8);
        Assert.assertEquals(8, period.getMillis());
    }
    
    @Test
    public void testAddMillis() {
        MutablePeriod period = new MutablePeriod();
        period.addMillis(8);
        Assert.assertEquals(8, period.getMillis());
    }
    
    @Test
    public void testCopy() {
        MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4);
        MutablePeriod period2 = period1.copy();
        Assert.assertEquals(period1.getYears(), period2.getYears());
        Assert.assertEquals(period1.getMonths(), period2.getMonths());
        Assert.assertEquals(period1.getWeeks(), period2.getWeeks());
        Assert.assertEquals(period1.getDays(), period2.getDays());
        Assert.assertEquals(period1.getHours(), period2.getHours());
        Assert.assertEquals(period1.getMinutes(), period2.getMinutes());
        Assert.assertEquals(period1.getSeconds(), period2.getSeconds());
        Assert.assertEquals(period1.getMillis(), period2.getMillis());
    }
}