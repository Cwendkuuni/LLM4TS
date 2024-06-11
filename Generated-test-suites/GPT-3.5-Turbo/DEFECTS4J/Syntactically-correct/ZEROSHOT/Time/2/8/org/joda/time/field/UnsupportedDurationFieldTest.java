import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        assertNull(field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        assertNull(field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLongLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.getMillis(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.getMillis(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.getMillis(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongInt() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.add(0L, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.add(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        field.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(null);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(null);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(null);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(null);
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        assertNotNull(field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        assertNotNull(field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(null);
        UnsupportedDurationField field2 = (UnsupportedDurationField) field1.readResolve();
        assertEquals(field1, field2);
    }

    @Test
    public void testUnsupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(null);
        try {
            field.unsupported();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e.getMessage());
        }
    }
}