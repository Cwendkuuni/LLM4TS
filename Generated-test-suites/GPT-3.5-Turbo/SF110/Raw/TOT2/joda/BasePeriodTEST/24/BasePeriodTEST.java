import org.joda.time.base.*;
import org.joda.time.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testGetValue() {
        BasePeriod basePeriod = new BasePeriod(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, PeriodType.standard());
        
        assertEquals(1, basePeriod.getValue(0));
        assertEquals(2, basePeriod.getValue(1));
        assertEquals(3, basePeriod.getValue(2));
        assertEquals(4, basePeriod.getValue(3));
        assertEquals(5, basePeriod.getValue(4));
        assertEquals(6, basePeriod.getValue(5));
        assertEquals(7, basePeriod.getValue(6));
        assertEquals(8, basePeriod.getValue(7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFieldValueUnsupported() {
        BasePeriod basePeriod = new BasePeriod(new int[8], PeriodType.standard());
        basePeriod.setField(DurationFieldType.centuries(), 1);
    }
    
    // Add additional test cases for other public methods as needed

}