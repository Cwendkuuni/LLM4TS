import org.joda.time.DateTimeUtils;
import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.base.BaseSingleFieldPeriodTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {

    @Before
    public void setup() {
        DateTimeUtils.setCurrentMillisFixed(System.currentTimeMillis());
    }

    @Test
    public void testBetween_Instants() {
        Instant start = new Instant(0L);
        Instant end = new Instant(1000L);
        int result = BaseSingleFieldPeriod.between(start, end, Days.days());
        assertEquals(1, result);
    }

    @Test
    public void testBetween_PartialDates() {
        Days days = Days.days();
        BaseSingleFieldPeriod zeroPeriod = new BaseSingleFieldPeriod(0);
        int result = BaseSingleFieldPeriod.between(days, days, zeroPeriod);
        assertEquals(0, result);
    }

    @Test
    public void testStandardPeriodIn() {
        Days days = Days.days();
        int result = BaseSingleFieldPeriod.standardPeriodIn(days, 24L * 3600 * 1000); // 24 hours in milliseconds
        assertEquals(1, result);
    }
}