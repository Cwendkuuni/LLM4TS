import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.PeriodType;

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
        // Test case for null ReadableInstant
        BasePeriod basePeriod1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration1 = basePeriod1.toDurationFrom(null);
        assertEquals(0, duration1.getMillis());

        // Test case for non-null ReadableInstant
        BasePeriod basePeriod2 = new BasePeriod(0, 0, 0, 0, 1, 0, 0, 0, PeriodType.standard());
        DateTime instant = new DateTime(2022, 1, 1, 0, 0, 0, 0);
        Duration duration2 = basePeriod2.toDurationFrom(instant);
        assertEquals(3600000, duration2.getMillis());
    }

    // Add more test cases for other methods...

}