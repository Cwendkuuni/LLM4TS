import static org.junit.Assert.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstructorWithInts() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.yearMonthDayTime());
        assertNotNull(basePeriod);
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
    public void testSetField() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.yearMonthDayTime());
        basePeriod.setField(DurationFieldType.years(), 10);
        assertEquals(10, basePeriod.getValue(0));
    }

    @Test
    public void testMergePeriod() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.yearMonthDayTime());
        BasePeriod otherPeriod = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.yearMonthDayTime());
        basePeriod.mergePeriod(otherPeriod);
        assertEquals(3, basePeriod.getValue(0));
        assertEquals(5, basePeriod.getValue(1));
        assertEquals(7, basePeriod.getValue(2));
        assertEquals(9, basePeriod.getValue(3));
        assertEquals(11, basePeriod.getValue(4));
        assertEquals(13, basePeriod.getValue(5));
        assertEquals(15, basePeriod.getValue(6));
        assertEquals(17, basePeriod.getValue(7));
    }

    @Test
    public void testAddPeriod() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.yearMonthDayTime());
        BasePeriod otherPeriod = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.yearMonthDayTime());
        basePeriod.addPeriod(otherPeriod);
        assertEquals(3, basePeriod.getValue(0));
        assertEquals(5, basePeriod.getValue(1));
        assertEquals(7, basePeriod.getValue(2));
        assertEquals(9, basePeriod.getValue(3));
        assertEquals(11, basePeriod.getValue(4));
        assertEquals(13, basePeriod.getValue(5));
        assertEquals(15, basePeriod.getValue(6));
        assertEquals(17, basePeriod.getValue(7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFieldUnsupportedFieldType() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.yearMonthDayTime());
        basePeriod.setField(DurationFieldType.centuries(), 10);
    }

}