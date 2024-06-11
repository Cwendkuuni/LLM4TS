import org.junit.Test;

public class BasePeriodTest {
    
    @Test
    public void testGetValue() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        
        int value = basePeriod.getValue(0);
        
        assertEquals(1, value);
    }
    
    @Test
    public void testToDurationFrom() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        ReadableInstant readableInstant = new ReadableInstant();
        
        Duration duration = basePeriod.toDurationFrom(readableInstant);
        
        //TODO: Assert the result
    }
    
    @Test
    public void testToDurationTo() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        ReadableInstant readableInstant = new ReadableInstant();
        
        Duration duration = basePeriod.toDurationTo(readableInstant);
        
        //TODO: Assert the result
    }
    
    //TODO: Add more test cases for other public methods
    
    //TODO: Add edge cases and exception handling scenarios test cases
    
}