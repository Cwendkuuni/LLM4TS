import org.joda.time.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BasePeriodTest {

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(2019, 12, 1, 0, 0, 0, 0, 0, PeriodType.standard());
        assertEquals(2019, period.getValue(0));
        assertEquals(12, period.getValue(1));
        assertEquals(1, period.getValue(2));
        assertEquals(0, period.getValue(3));
        assertEquals(0, period.getValue(4));
        assertEquals(0, period.getValue(5));
        assertEquals(0, period.getValue(6));
        assertEquals(0, period.getValue(7));
    }

    @Test
    public void testToDurationFrom() {
        DateTime startDate = new DateTime(2019, 12, 1, 0, 0, 0);
        DateTime endDate = new DateTime(2019, 12, 2, 0, 0, 0);
        BasePeriod period = new BasePeriod(startDate, endDate, PeriodType.standard());
        Duration duration = period.toDurationFrom(startDate);
        assertEquals(24 * 60 * 60 * 1000, duration.getMillis());
    }

    @Test
    public void testToDurationTo() {
        DateTime startDate = new DateTime(2019, 12, 1, 0, 0, 0);
        DateTime endDate = new DateTime(2019, 12, 2, 0, 0, 0);
        BasePeriod period = new BasePeriod(startDate, endDate, PeriodType.standard());
        Duration duration = period.toDurationTo(endDate);
        assertEquals(24 * 60 * 60 * 1000, duration.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetField_invalidFieldType() {
        BasePeriod period = new BasePeriod(2019, 12, 1, 0, 0, 0, 0, 0, PeriodType.standard());
        period.setField(DurationFieldType.eras(), 1);
    }

    @Test
    public void testSetField_validFieldType() {
        BasePeriod period = new BasePeriod(2019, 12, 1, 0, 0, 0, 0, 0, PeriodType.standard());
        period.setField(DurationFieldType.years(), 2020);
        assertEquals(2020, period.getValue(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddField_invalidFieldType() {
        BasePeriod period = new BasePeriod(2019, 12, 1, 0, 0, 0, 0, 0, PeriodType.standard());
        period.addField(DurationFieldType.eras(), 1);
    }

    @Test
    public void testAddField_validFieldType() {
        BasePeriod period = new BasePeriod(2019, 12, 1, 0, 0, 0, 0, 0, PeriodType.standard());
        period.addField(DurationFieldType.years(), 1);
        assertEquals(2020, period.getValue(0));
    }

    @Test
    public void testMergePeriod() {
        BasePeriod period = new BasePeriod(2019, 12, 1, 0, 0, 0, 0, 0, PeriodType.standard());
        BasePeriod anotherPeriod = new BasePeriod(2020, 1, 1, 0, 0, 0, 0, 0, PeriodType.standard());
        period.mergePeriod(anotherPeriod);
        assertEquals(2020, period.getValue(0));
        assertEquals(1, period.getValue(1));
    }

    @Test
    public void testAddPeriod() {
        BasePeriod period = new BasePeriod(2019, 12, 1, 0, 0, 0, 0, 0, PeriodType.standard());
        BasePeriod anotherPeriod = new BasePeriod(1, 0, 0, 0, 0, 0, 0, 0, PeriodType.standard());
        period.addPeriod(anotherPeriod);
        assertEquals(2020, period.getValue(0));
    }

    @Test
    public void testSetValue() {
        BasePeriod period = new BasePeriod(2019, 12, 1, 0, 0, 0, 0, 0, PeriodType.standard());
        period.setValue(0, 2020);
        assertEquals(2020, period.getValue(0));
    }

    @Test
    public void testSetValues() {
        BasePeriod period = new BasePeriod(2019, 12, 1, 0, 0, 0, 0, 0, PeriodType.standard());
        int[] values = {2020, 1, 1, 0, 0, 0, 0, 0};
        period.setValues(values);
        assertEquals(2020, period.getValue(0));
        assertEquals(1, period.getValue(1));
        assertEquals(1, period.getValue(2));
    }

}