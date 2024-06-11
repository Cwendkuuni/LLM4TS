import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.field.*;

public class BasePeriodTest {

    @Test
    public void testGetPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.dayTime());
        assertEquals(PeriodType.dayTime(), period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.dayTime());
        assertEquals(1, period.getValue(0));
        assertEquals(2, period.getValue(1));
        assertEquals(3, period.getValue(2));
        assertEquals(4, period.getValue(3));
        assertEquals(5, period.getValue(4));
        assertEquals(6, period.getValue(5));
        assertEquals(7, period.getValue(6));
        assertEquals(8, period.getValue(7));
    }

    @Test
    public void testToDurationFrom() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.dayTime());
        DateTime from = DateTime.parse("2022-01-01T00:00:00Z");
        Duration duration = period.toDurationFrom(from);
        assertEquals(1, duration.getStandardDays());
        assertEquals(2, duration.getStandardHours());
        assertEquals(3, duration.getStandardMinutes());
        assertEquals(4, duration.getStandardSeconds());
        assertEquals(5, duration.getMillisOfSecond());
        assertEquals(6, duration.getMillisOfSecond());
        assertEquals(7, duration.getMillisOfSecond());
        assertEquals(8, duration.getMillisOfSecond());
    }

    @Test
    public void testToDurationTo() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.dayTime());
        DateTime to = DateTime.parse("2022-01-01T00:00:00Z");
        Duration duration = period.toDurationTo(to);
        assertEquals(-1, duration.getStandardDays());
        assertEquals(-2, duration.getStandardHours());
        assertEquals(-3, duration.getStandardMinutes());
        assertEquals(-4, duration.getStandardSeconds());
        assertEquals(-5, duration.getMillisOfSecond());
        assertEquals(-6, duration.getMillisOfSecond());
        assertEquals(-7, duration.getMillisOfSecond());
        assertEquals(-8, duration.getMillisOfSecond());
    }
}