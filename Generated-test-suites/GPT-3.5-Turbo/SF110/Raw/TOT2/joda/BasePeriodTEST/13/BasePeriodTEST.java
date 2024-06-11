import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {
    // Test getValue method with normal case
    @Test
    public void testGetValue_NormalCase() {
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

    // Test getValue method with out of bounds index
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetValue_OutOfBoundsIndex() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        basePeriod.getValue(8);
    }

    // Test toDurationFrom method with normal case
    @Test
    public void testToDurationFrom_NormalCase() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = basePeriod.toDurationFrom(new DateTime(2022, 1, 1, 0, 0));
        assertEquals(365*24*60*60*1000, duration.getMillis()); // 1 year in milliseconds
    }

    // Test toDurationTo method with normal case
    @Test
    public void testToDurationTo_NormalCase() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Duration duration = basePeriod.toDurationTo(new DateTime(2023, 1, 1, 0, 0));
        assertEquals(365*24*60*60*1000, duration.getMillis()); // 1 year in milliseconds
    }

    // Additional edge cases and error scenarios should be added here

}