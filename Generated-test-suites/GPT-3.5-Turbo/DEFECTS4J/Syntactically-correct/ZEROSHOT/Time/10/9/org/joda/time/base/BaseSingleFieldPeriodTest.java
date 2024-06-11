import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;


import org.joda.time.DateTimeUtils;
import org.joda.time.DaurationField;
import org.joda.time.DaurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod baseSingleFieldPeriod;

    @Before
    public void setUp() {
        baseSingleFieldPeriod = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
    }

    @Test
    public void testGetValue() {
        assertEquals(10, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testSetValue() {
        baseSingleFieldPeriod.setValue(20);
        assertEquals(20, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testSize() {
        assertEquals(1, baseSingleFieldPeriod.size());
    }

    @Test
    public void testGetFieldType() {
        assertEquals(null, baseSingleFieldPeriod.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(null, baseSingleFieldPeriod.getPeriodType());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        assertEquals(null, baseSingleFieldPeriod.getFieldType(0));
    }

    @Test
    public void testGetValueAtIndex() {
        assertEquals(10, baseSingleFieldPeriod.getValue(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeAtIndex_invalidIndex() {
        baseSingleFieldPeriod.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtIndex_invalidIndex() {
        baseSingleFieldPeriod.getValue(1);
    }

    @Test
    public void testGet() {
        assertEquals(10, baseSingleFieldPeriod.get(null));
        assertEquals(0, baseSingleFieldPeriod.get(DateTimeFieldType.dayOfMonth()));
    }

    @Test
    public void testIsSupported() {
        assertTrue(baseSingleFieldPeriod.isSupported(null));
        assertFalse(baseSingleFieldPeriod.isSupported(DateTimeFieldType.dayOfMonth()));
    }

    @Test
    public void testToPeriod() {
        Period period = baseSingleFieldPeriod.toPeriod();
        assertEquals(10, period.get(DurationFieldType.days()));
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        assertEquals(10, mutablePeriod.get(DurationFieldType.days()));
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod different = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertTrue(baseSingleFieldPeriod.equals(other));
        assertFalse(baseSingleFieldPeriod.equals(different));
    }

    @Test
    public void testHashCode() {
        int expected = 17;
        expected = 27 * expected + 10;
        expected = 27 * expected + baseSingleFieldPeriod.getFieldType().hashCode();
        assertEquals(expected, baseSingleFieldPeriod.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(20) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertEquals(-1, baseSingleFieldPeriod.compareTo(other));
        assertEquals(1, other.compareTo(baseSingleFieldPeriod));
        assertEquals(0, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod));
    }
}