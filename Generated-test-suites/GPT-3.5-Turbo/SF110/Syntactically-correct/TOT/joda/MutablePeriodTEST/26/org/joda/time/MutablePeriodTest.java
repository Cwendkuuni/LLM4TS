import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.*;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.ISOPeriodFormat;

public class MutablePeriodTest {
    
    private MutablePeriod period;
    
    @Before
    public void setUp() {
        period = new MutablePeriod();
    }
    
    @Test
    public void testParse() {
        String input = "P1Y2M3D";
        MutablePeriod parsedPeriod = MutablePeriod.parse(input);
        
        assertEquals(1, parsedPeriod.getYears());
        assertEquals(2, parsedPeriod.getMonths());
        assertEquals(3, parsedPeriod.getDays());
    }
    
    @Test
    public void testParseWithFormatter() {
        String input = "P1Y2M3D";
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        MutablePeriod parsedPeriod = MutablePeriod.parse(input, formatter);
        
        assertEquals(1, parsedPeriod.getYears());
        assertEquals(2, parsedPeriod.getMonths());
        assertEquals(3, parsedPeriod.getDays());
    }
    
    @Test
    public void testConstructorNoArgs() {
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getDays());
    }
    
    @Test
    public void testConstructorWithPeriodType() {
        PeriodType periodType = PeriodType.standard();
        MutablePeriod period = new MutablePeriod(periodType);
        
        assertEquals(periodType, period.getPeriodType());
    }
    
    @Test
    public void testConstructorWithInts() {
        MutablePeriod period = new MutablePeriod(1, 2, 3, 4);
        
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(3, period.getWeeks());
        assertEquals(4, period.getDays());
    }
  
    // ... Other test cases for all other methods ...

    @Test
    public void testSetYears() {
        period.setYears(5);
        
        assertEquals(5, period.getYears());
    }
    
    @Test
    public void testAddYears() {
        period.setYears(5);
        period.addYears(2);
        
        assertEquals(7, period.getYears());
    }
    
    @Test
    public void testSetMonths() {
        period.setMonths(10);
        
        assertEquals(10, period.getMonths());
    }
    
    @Test
    public void testAddMonths() {
        period.setMonths(10);
        period.addMonths(5);
        
        assertEquals(15, period.getMonths());
    }
    
    // ... Tests for all other setters and adders ...
    
    @Test
    public void testCopy() {
        period.setYears(2);
        period.setMonths(3);
        period.setDays(4);
        
        MutablePeriod copiedPeriod = period.copy();
        
        assertEquals(period.getYears(), copiedPeriod.getYears());
        assertEquals(period.getMonths(), copiedPeriod.getMonths());
        assertEquals(period.getDays(), copiedPeriod.getDays());
    }
    
    @Test
    public void testClone() throws CloneNotSupportedException {
        period.setYears(2);
        period.setMonths(3);
        period.setDays(4);
        
        MutablePeriod clonedPeriod = (MutablePeriod) period.clone();
        
        assertEquals(period.getYears(), clonedPeriod.getYears());
        assertEquals(period.getMonths(), clonedPeriod.getMonths());
        assertEquals(period.getDays(), clonedPeriod.getDays());
    }
}