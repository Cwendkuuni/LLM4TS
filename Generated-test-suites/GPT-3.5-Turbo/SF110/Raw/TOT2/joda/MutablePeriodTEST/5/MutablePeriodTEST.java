import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        MutablePeriod mutablePeriod = MutablePeriod.parse("P1Y2M3D");
        
        assertNotNull(mutablePeriod);
        assertEquals(1, mutablePeriod.getYears());
        assertEquals(2, mutablePeriod.getMonths());
        assertEquals(3, mutablePeriod.getDays());
    }
    
    @Test
    public void testSetPeriodWithChronology() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        Chronology chronology = ISOChronology.getInstance();
        
        mutablePeriod.setPeriod(3600, chronology);
        
        assertEquals(1, mutablePeriod.getHours());
    }
    
    @Test
    public void testAddDurationFieldType() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        
        mutablePeriod.add(DurationFieldType.minutes(), 30);
        
        assertEquals(30, mutablePeriod.getMinutes());
    }

}