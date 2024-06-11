import org.joda.time.*;
import org.junit.Before;
import org.junit.Test;

public class MutablePeriodTest {
    
    private MutablePeriod mutablePeriod;
    
    @Before
    public void setup() {
        mutablePeriod = new MutablePeriod();
    }
    
    @Test
    public void testParse() {
        MutablePeriod parsedPeriod = MutablePeriod.parse("P1Y2M3D");
        // Add assertion for parsedPeriod
    }
    
    @Test
    public void testClear() {
        mutablePeriod.setValue(PeriodType.YEAR, 1);
        mutablePeriod.clear();
        // Add assertion for cleared values
    }
    
    // More test cases for other methods
    
}