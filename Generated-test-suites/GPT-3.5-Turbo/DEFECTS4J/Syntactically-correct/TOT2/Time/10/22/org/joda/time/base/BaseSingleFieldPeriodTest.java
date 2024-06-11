import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    // Initialize the BaseSingleFieldPeriod object for testing
    BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(5) {
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.days();
        }
    };

    // Test case for getValue method
    @Test
    public void testGetValue() {
        assertEquals(5, baseSingleFieldPeriod.getValue());
    }

    // Test case for setValue method
    @Test
    public void testSetValue() {
        baseSingleFieldPeriod.setValue(10);
        assertEquals(10, baseSingleFieldPeriod.getValue());
    }

    // Test case for getFieldType method
    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.days(), baseSingleFieldPeriod.getFieldType());
    }

    // Test case for getPeriodType method
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.days(), baseSingleFieldPeriod.getPeriodType());
    }

    // Test case for size method
    @Test
    public void testSize() {
        assertEquals(1, baseSingleFieldPeriod.size());
    }

    // Test case for getFieldType(index) method
    @Test
    public void testGetFieldTypeAtIndex() {
        assertEquals(DurationFieldType.days(), baseSingleFieldPeriod.getFieldType(0));
        assertThrows(IndexOutOfBoundsException.class, () -> baseSingleFieldPeriod.getFieldType(1));
    }

    // Test case for getValue(index) method
    @Test
    public void testGetValueAtIndex() {
        assertEquals(5, baseSingleFieldPeriod.getValue(0));
        assertThrows(IndexOutOfBoundsException.class, () -> baseSingleFieldPeriod.getValue(1));
    }

    // Test case for get(type) method
    @Test
    public void testGet() {
        assertEquals(5, baseSingleFieldPeriod.get(DurationFieldType.days()));
        assertEquals(0, baseSingleFieldPeriod.get(DurationFieldType.months())); // Non-matching type should return 0
    }

    // Test case for isSupported method
    @Test
    public void testIsSupported() {
        assertTrue(baseSingleFieldPeriod.isSupported(DurationFieldType.days()));
        assertFalse(baseSingleFieldPeriod.isSupported(DurationFieldType.months())); // Non-matching type should return false
    }

    // Test case for equals method
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };
        assertTrue(baseSingleFieldPeriod.equals(other));
    }

    // Test case for hashCode method
    @Test
    public void testHashCode() {
        int expected = 17 * 27 + 5; // Expected hash code based on field value
        expected = expected * 27 + DurationFieldType.days().hashCode(); // Include field type hash code
        assertEquals(expected, baseSingleFieldPeriod.hashCode());
    }

    // Test case for compareTo method
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        BaseSingleFieldPeriod largerPeriod = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }
        };

        assertEquals(1, baseSingleFieldPeriod.compareTo(smallerPeriod));
        assertEquals(-1, baseSingleFieldPeriod.compareTo(largerPeriod));
        assertEquals(0, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod));
    }

}