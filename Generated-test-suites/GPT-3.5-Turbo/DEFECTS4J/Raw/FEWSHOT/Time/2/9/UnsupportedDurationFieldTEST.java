import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    private UnsupportedDurationField field;

    @Before
    public void setUp() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.months());
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.months(), field.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("months", field.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue() {
        try {
            field.getValue(100);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetValueAsLong() {
        try {
            field.getValueAsLong(100);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetValueWithDuration() {
        try {
            field.getValue(100, 123456789L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetValueAsLongWithDuration() {
        try {
            field.getValueAsLong(100, 123456789L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillisWithValue() {
        try {
            field.getMillis(100);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillisWithValueAsLong() {
        try {
            field.getMillis(100L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillisWithDuration() {
        try {
            field.getMillis(100, 123456789L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillisWithValueWithDurationAsLong() {
        try {
            field.getMillis(100L, 123456789L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testAdd() {
        try {
            field.add(123456789L, 100);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testAddWithValueAsLong() {
        try {
            field.add(123456789L, 100L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetDifference() {
        try {
            field.getDifference(123456789L, 987654321L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetDifferenceAsLong() {
        try {
            field.getDifferenceAsLong(123456789L, 987654321L);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("months field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, field.compareTo(field));
        assertTrue(field.compareTo(DurationFieldType.years()) < 0);
        assertTrue(field.compareTo(DurationFieldType.days()) > 0);
    }

    @Test
    public void testEquals() {
        assertTrue(field.equals(field));
        assertFalse(field.equals(null));
        assertFalse(field.equals(DurationFieldType.years()));
        assertFalse(field.equals(DurationFieldType.days()));
        assertTrue(field.equals(UnsupportedDurationField.getInstance(DurationFieldType.months())));
    }

    @Test
    public void testHashCode() {
        assertEquals(DurationFieldType.months().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[months]", field.toString());
    }
}