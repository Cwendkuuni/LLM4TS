import org.joda.time.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    private MutablePeriod mutablePeriod;
    
    @Before
    public void setUp() {
        mutablePeriod = new MutablePeriod();
    }
    
    @Test
    public void testParse() {
        MutablePeriod period = MutablePeriod.parse("P1Y");
        assertEquals(1, period.getYears());
    }
    
    @Test
    public void testSetYears() {
        mutablePeriod.setYears(5);
        assertEquals(5, mutablePeriod.getYears());
    }
    
    @Test
    public void testAddYears() {
        mutablePeriod.addYears(3);
        assertEquals(3, mutablePeriod.getYears());
    }
    
    @Test
    public void testSetMonths() {
        mutablePeriod.setMonths(2);
        assertEquals(2, mutablePeriod.getMonths());
    }
    
    @Test
    public void testAddMonths() {
        mutablePeriod.addMonths(4);
        assertEquals(4, mutablePeriod.getMonths());
    }
    
    @Test
    public void testSetWeeks() {
        mutablePeriod.setWeeks(1);
        assertEquals(1, mutablePeriod.getWeeks());
    }
    
    @Test
    public void testAddWeeks() {
        mutablePeriod.addWeeks(2);
        assertEquals(2, mutablePeriod.getWeeks());
    }
    
    @Test
    public void testSetDays() {
        mutablePeriod.setDays(3);
        assertEquals(3, mutablePeriod.getDays());
    }
    
    @Test
    public void testAddDays() {
        mutablePeriod.addDays(7);
        assertEquals(7, mutablePeriod.getDays());
    }
    
    @Test
    public void testSetHours() {
        mutablePeriod.setHours(5);
        assertEquals(5, mutablePeriod.getHours());
    }
    
    @Test
    public void testAddHours() {
        mutablePeriod.addHours(2);
        assertEquals(2, mutablePeriod.getHours());
    }
    
    @Test
    public void testSetMinutes() {
        mutablePeriod.setMinutes(30);
        assertEquals(30, mutablePeriod.getMinutes());
    }
    
    @Test
    public void testAddMinutes() {
        mutablePeriod.addMinutes(15);
        assertEquals(15, mutablePeriod.getMinutes());
    }
    
    @Test
    public void testSetSeconds() {
        mutablePeriod.setSeconds(45);
        assertEquals(45, mutablePeriod.getSeconds());
    }
    
    @Test
    public void testAddSeconds() {
        mutablePeriod.addSeconds(30);
        assertEquals(30, mutablePeriod.getSeconds());
    }
    
    @Test
    public void testSetMillis() {
        mutablePeriod.setMillis(500);
        assertEquals(500, mutablePeriod.getMillis());
    }
    
    @Test
    public void testAddMillis() {
        mutablePeriod.addMillis(100);
        assertEquals(100, mutablePeriod.getMillis());
    }
    
    @Test
    public void testCopy() {
        MutablePeriod copy = mutablePeriod.copy();
        assertEquals(mutablePeriod.getYears(), copy.getYears());
        assertEquals(mutablePeriod.getMonths(), copy.getMonths());
        assertEquals(mutablePeriod.getWeeks(), copy.getWeeks());
        assertEquals(mutablePeriod.getDays(), copy.getDays());
        assertEquals(mutablePeriod.getHours(), copy.getHours());
        assertEquals(mutablePeriod.getMinutes(), copy.getMinutes());
        assertEquals(mutablePeriod.getSeconds(), copy.getSeconds());
        assertEquals(mutablePeriod.getMillis(), copy.getMillis());
    }
    
    @Test
    public void testClone() {
        MutablePeriod clone = (MutablePeriod) mutablePeriod.clone();
        assertEquals(mutablePeriod.getYears(), clone.getYears());
        assertEquals(mutablePeriod.getMonths(), clone.getMonths());
        assertEquals(mutablePeriod.getWeeks(), clone.getWeeks());
        assertEquals(mutablePeriod.getDays(), clone.getDays());
        assertEquals(mutablePeriod.getHours(), clone.getHours());
        assertEquals(mutablePeriod.getMinutes(), clone.getMinutes());
        assertEquals(mutablePeriod.getSeconds(), clone.getSeconds());
        assertEquals(mutablePeriod.getMillis(), clone.getMillis());
    }
}