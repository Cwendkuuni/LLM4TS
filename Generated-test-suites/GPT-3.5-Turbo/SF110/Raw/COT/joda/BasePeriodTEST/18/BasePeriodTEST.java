import org.joda.time.field.FieldUtils;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.*;

public class BasePeriodTest {

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
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
        DateTime dateTime = new DateTime(2022, 1, 1, 0, 0);
        BasePeriod period = new BasePeriod(1, 0, 0, 0, 0, 0, 0, 0);
        Duration duration = period.toDurationFrom(dateTime);
        assertEquals(31536000000L, duration.getMillis());
    }

    @Test
    public void testToDurationTo() {
        DateTime dateTime = new DateTime(2023, 1, 1, 0, 0);
        BasePeriod period = new BasePeriod(1, 0, 0, 0, 0, 0, 0, 0);
        Duration duration = period.toDurationTo(dateTime);
        assertEquals(-31536000000L, duration.getMillis());
    }

    @Test
    public void testSetPeriod() {
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        BasePeriod basePeriod = new BasePeriod(0);
        basePeriod.setPeriod(period);
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
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period.setField(DurationFieldType.years(), 10);
        assertEquals(10, period.getValue(0));
        assertEquals(2, period.getValue(1));
        assertEquals(3, period.getValue(2));
        assertEquals(4, period.getValue(3));
        assertEquals(5, period.getValue(4));
        assertEquals(6, period.getValue(5));
        assertEquals(7, period.getValue(6));
        assertEquals(8, period.getValue(7));
    }

    @Test
    public void testAddField() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        period.addField(DurationFieldType.years(), 10);
        assertEquals(11, period.getValue(0));
        assertEquals(2, period.getValue(1));
        assertEquals(3, period.getValue(2));
        assertEquals(4, period.getValue(3));
        assertEquals(5, period.getValue(4));
        assertEquals(6, period.getValue(5));
        assertEquals(7, period.getValue(6));
        assertEquals(8, period.getValue(7));
    }

    @Test
    public void testMergePeriod() {
        Period period1 = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        Period period2 = new Period(10, 20, 30, 40, 50, 60, 70, 80);
        BasePeriod basePeriod1 = new BasePeriod(0);
        BasePeriod basePeriod2 = new BasePeriod(0);
        basePeriod1.mergePeriod(period1);
        basePeriod2.mergePeriod(period2);
        basePeriod1.mergePeriod(basePeriod2);
        assertEquals(11, basePeriod1.getValue(0));
        assertEquals(22, basePeriod1.getValue(1));
        assertEquals(33, basePeriod1.getValue(2));
        assertEquals(44, basePeriod1.getValue(3));
        assertEquals(55, basePeriod1.getValue(4));
        assertEquals(66, basePeriod1.getValue(5));
        assertEquals(77, basePeriod1.getValue(6));
        assertEquals(88, basePeriod1.getValue(7));
    }

    @Test
    public void testAddPeriod() {
        Period period1 = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        Period period2 = new Period(10, 20, 30, 40, 50, 60, 70, 80);
        BasePeriod basePeriod1 = new BasePeriod(0);
        basePeriod1.addPeriod(period1);
        basePeriod1.addPeriod(period2);
        assertEquals(11, basePeriod1.getValue(0));
        assertEquals(22, basePeriod1.getValue(1));
        assertEquals(33, basePeriod1.getValue(2));
        assertEquals(44, basePeriod1.getValue(3));
        assertEquals(55, basePeriod1.getValue(4));
        assertEquals(66, basePeriod1.getValue(5));
        assertEquals(77, basePeriod1.getValue(6));
        assertEquals(88, basePeriod1.getValue(7));
    }
}