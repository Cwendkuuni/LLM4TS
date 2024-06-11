import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.*;

public class BasePeriodTest {

    @Test
    public void testGetValue() {
        BasePeriod basePeriod = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
        assertEquals(2, basePeriod.getValue(0));
        assertEquals(3, basePeriod.getValue(1));
        assertEquals(4, basePeriod.getValue(2));
        assertEquals(5, basePeriod.getValue(3));
        assertEquals(6, basePeriod.getValue(4));
        assertEquals(7, basePeriod.getValue(5));
        assertEquals(8, basePeriod.getValue(6));
        assertEquals(9, basePeriod.getValue(7));
    }

    @Test
    public void testToDurationFrom() {
        DateTime dateTime = new DateTime(2020, 1, 1, 0, 0);
        BasePeriod basePeriod = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
        Duration result = basePeriod.toDurationFrom(dateTime);
        assertEquals(new Duration(dateTime.getMillis(), dateTime.getChronology().add(basePeriod, dateTime.getMillis(), 1)).getMillis(), result.getMillis());
    }

    @Test
    public void testToDurationTo() {
        DateTime dateTime = new DateTime(2020, 1, 1, 0, 0);
        BasePeriod basePeriod = new BasePeriod(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard());
        Duration result = basePeriod.toDurationTo(dateTime);
        assertEquals(new Duration(dateTime.getChronology().add(basePeriod, dateTime.getMillis(), -1), dateTime.getMillis()).getMillis(), result.getMillis());
    }
}