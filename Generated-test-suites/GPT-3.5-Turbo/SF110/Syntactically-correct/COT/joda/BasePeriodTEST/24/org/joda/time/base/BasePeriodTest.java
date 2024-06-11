import static org.junit.Assert.*;
import org.joda.time.*;
import org.junit.Test;

public class BasePeriodTest {
    
    @Test
    public void testGetValue() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(1, basePeriod.getValue(0));
        assertEquals(2, basePeriod.getValue(1));
        assertEquals(3, basePeriod.getValue(2));
        assertEquals(4, basePeriod.getValue(3));
        assertEquals(5, basePeriod.getValue(4));
        assertEquals(6, basePeriod.getValue(5));
        assertEquals(7, basePeriod.getValue(6));
        assertEquals(8, basePeriod.getValue(7));
    }
    
    @Test
    public void testToDurationFrom() {
        DateTime dateTime = new DateTime(2021, 1, 1, 0, 0, 0);
        BasePeriod basePeriod = new BasePeriod(1, 0, 0, 0, 0, 0, 0, 0, PeriodType.standard());
        Duration duration = basePeriod.toDurationFrom(dateTime);
        assertEquals(31536000000L, duration.getMillis());
    }
    
    @Test
    public void testToDurationTo() {
        DateTime dateTime = new DateTime(2022, 1, 1, 0, 0, 0);
        BasePeriod basePeriod = new BasePeriod(1, 0, 0, 0, 0, 0, 0, 0, PeriodType.standard());
        Duration duration = basePeriod.toDurationTo(dateTime);
        assertEquals(31536000000L, duration.getMillis());
    }
    
}