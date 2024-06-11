import org.joda.time.*;
import org.joda.time.field.*;
import org.joda.time.chrono.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testConstructor1() {
        BasePeriod basePeriod = new BasePeriod(2022, 10, 31, 0, 0, 0, 0, 0, PeriodType.standard());

        assertEquals(1, basePeriod.getValue(0));
        assertEquals(10, basePeriod.getValue(1));
        assertEquals(31, basePeriod.getValue(2));
        assertEquals(0, basePeriod.getValue(3));
        assertEquals(0, basePeriod.getValue(4));
        assertEquals(0, basePeriod.getValue(5));
        assertEquals(0, basePeriod.getValue(6));
        assertEquals(0, basePeriod.getValue(7));
        assertEquals(PeriodType.standard(), basePeriod.getPeriodType());
    }

    @Test
    public void testConstructor2() {
        BasePeriod basePeriod = new BasePeriod(100000L);

        assertEquals(0, basePeriod.getValue(0));
        assertEquals(0, basePeriod.getValue(1));
        assertEquals(0, basePeriod.getValue(2));
        assertEquals(1, basePeriod.getValue(3));
        assertEquals(0, basePeriod.getValue(4));
        assertEquals(0, basePeriod.getValue(5));
        assertEquals(0, basePeriod.getValue(6));
        assertEquals(0, basePeriod.getValue(7));
        assertEquals(PeriodType.standard(), basePeriod.getPeriodType());
    }

    @Test
    public void testConstructor3() {
        DateTime dateTime1 = new DateTime(2022, 10, 31, 0, 0);
        DateTime dateTime2 = new DateTime(2023, 11, 1, 0, 0);

        BasePeriod basePeriod = new BasePeriod(dateTime1, dateTime2, PeriodType.standard());

        assertEquals(1, basePeriod.getValue(0));
        assertEquals(10, basePeriod.getValue(1));
        assertEquals(0, basePeriod.getValue(2));
        assertEquals(1, basePeriod.getValue(3));
        assertEquals(0, basePeriod.getValue(4));
        assertEquals(0, basePeriod.getValue(5));
        assertEquals(0, basePeriod.getValue(6));
        assertEquals(0, basePeriod.getValue(7));
        assertEquals(PeriodType.standard(), basePeriod.getPeriodType());
    }

    @Test
    public void testConstructor4() {
        DateTime dateTime = new DateTime(2022, 10, 31, 0, 0);
        Duration duration = new Duration(86400000L);

        BasePeriod basePeriod = new BasePeriod(dateTime, duration, PeriodType.standard());

        assertEquals(0, basePeriod.getValue(0));
        assertEquals(0, basePeriod.getValue(1));
        assertEquals(0, basePeriod.getValue(2));
        assertEquals(1, basePeriod.getValue(3));
        assertEquals(0, basePeriod.getValue(4));
        assertEquals(0, basePeriod.getValue(5));
        assertEquals(0, basePeriod.getValue(6));
        assertEquals(0, basePeriod.getValue(7));
        assertEquals(PeriodType.standard(), basePeriod.getPeriodType());
    }

    @Test
    public void testSetValue() {
        BasePeriod basePeriod = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        basePeriod.setValue(0, 10);

        assertEquals(10, basePeriod.getValue(0));
    }

    @Test
    public void testSetValues() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        BasePeriod basePeriod = new BasePeriod(values, PeriodType.standard());

        int[] newValues = {10, 20, 30, 40, 50, 60, 70, 80};
        basePeriod.setValues(newValues);

        for (int i = 0; i < newValues.length; i++) {
            assertEquals(newValues[i], basePeriod.getValue(i));
        }
    }

}