import org.joda.time.*;

public class MutablePeriodTest {

    @org.junit.Test
    public void testParse() {
        String s = "P1Y2M3DT4H5M6S";
        MutablePeriod result = MutablePeriod.parse(s);
        assertEquals(1, result.getYears());
        assertEquals(2, result.getMonths());
        assertEquals(3, result.getDays());
        assertEquals(4, result.getHours());
        assertEquals(5, result.getMinutes());
        assertEquals(6, result.getSeconds());
    }

    @org.junit.Test
    public void testParseWithFormatter() {
        String s = "01-02-03";
        PeriodFormatter periodFormatter = DateTimeFormat.forPattern("yy-MM-dd");
        MutablePeriod result = MutablePeriod.parse(s, periodFormatter);
        assertEquals(1, result.getYears());
        assertEquals(2, result.getMonths());
        assertEquals(3, result.getDays());
    }

    @org.junit.Test
    public void testDefaultConstructor() {
        MutablePeriod mutablePeriod = new MutablePeriod();
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testPeriodTypeConstructor() {
        PeriodType periodType = PeriodType.yearWeekDayTime();
        MutablePeriod mutablePeriod = new MutablePeriod(periodType);
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @org.junit.Test
    public void testIntConstructor() {
        MutablePeriod mutablePeriod = new MutablePeriod(1, 2, 3, 4);
        assertEquals(1, mutablePeriod.getYears());
        assertEquals(2, mutablePeriod.getMonths());
        assertEquals(3, mutablePeriod.getDays());
        assertEquals(4, mutablePeriod.getHours());
    }

    // Add more test cases...

}