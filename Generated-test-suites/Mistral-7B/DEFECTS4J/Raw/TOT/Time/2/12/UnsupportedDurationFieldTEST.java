import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFormat;
import org.joda.time.LocalDateTime;
import org.joda.time.base.AbstractBaseTestCase;

import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest extends AbstractBaseTestCase {

    @Test
    public void testGetInstance() {
        DurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        DurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertSame(unsupportedField1, unsupportedField2);
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("days", unsupportedField.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(unsupportedField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValueAsLong(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValue(1, new LocalDateTime().getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValueAsLong(1, new LocalDateTime().getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getMillis(1000, new LocalDateTime().getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.add(new LocalDateTime().getMillis(), 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.add(new LocalDateTime().getMillis(), 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getDifference(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getDifferenceAsLong(1, 1);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, unsupportedField1.compareTo(unsupportedField2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(unsupportedField1.equals(unsupportedField2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(unsupportedField1.hashCode(), unsupportedField2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        String format = DurationFormat.forPattern("u 'unsupported' d").format(new DurationField(DurationFieldType.DAYS, 1));
        assertEquals(format, unsupportedField.toString());
    }

    @Test
    public void testSingletonSerialization() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertNotSame(unsupportedField, UnsupportedDurationField.getInstance(DurationFieldType.DAYS).readResolve());
    }

    @Test
    public void testThrowsUnsupportedOperationException() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, unsupportedField::getValue);
    }
}