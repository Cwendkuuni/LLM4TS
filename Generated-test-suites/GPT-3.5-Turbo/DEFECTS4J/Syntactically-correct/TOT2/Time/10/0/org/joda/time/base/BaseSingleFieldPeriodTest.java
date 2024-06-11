import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod baseSingleFieldPeriod;

    @Before
    public void setUp() {
        baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(5);
    }

    @Test
    public void testGetValue() {
        assertEquals(5, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testSetValue() {
        baseSingleFieldPeriod.setValue(10);
        assertEquals(10, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.days(), baseSingleFieldPeriod.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.days(), baseSingleFieldPeriod.getPeriodType());
    }

    @Test
    public void testSize() {
        assertEquals(1, baseSingleFieldPeriod.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        assertEquals(DurationFieldType.days(), baseSingleFieldPeriod.getFieldType(0));
    }

    @Test
    public void testGetValueAtIndex() {
        assertEquals(5, baseSingleFieldPeriod.getValue(0));
    }

    @Test
    public void testGetNonSupportedFieldType() {
        assertEquals(0, baseSingleFieldPeriod.get(DurationFieldType.hours()));
    }

    @Test
    public void testIsFieldTypeSupported() {
        assertEquals(true, baseSingleFieldPeriod.isSupported(DurationFieldType.days()));
    }

    @Test
    public void testToPeriod() {
        Period period = baseSingleFieldPeriod.toPeriod();
        assertEquals(Period.days(5), period);
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        assertEquals(Period.days(5), mutablePeriod.toPeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod otherPeriod = new BaseSingleFieldPeriodImpl(5);
        assertEquals(true, baseSingleFieldPeriod.equals(otherPeriod));
    }

    @Test
    public void testHashCode() {
        int expectedHash = 17;
        expectedHash = 27 * expectedHash + 5;
        expectedHash = 27 * expectedHash + DurationFieldType.days().hashCode();
        assertEquals(expectedHash, baseSingleFieldPeriod.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod otherPeriod = new BaseSingleFieldPeriodImpl(3);
        assertEquals(1, baseSingleFieldPeriod.compareTo(otherPeriod));
    }

    private static class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {
        protected BaseSingleFieldPeriodImpl(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.days();
        }
    }
}