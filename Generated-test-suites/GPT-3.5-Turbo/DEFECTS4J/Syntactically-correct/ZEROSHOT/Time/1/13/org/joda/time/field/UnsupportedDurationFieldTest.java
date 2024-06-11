import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedField;

    @Before
    public void setUp() {
        unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.DAY_OF_MONTH, unsupportedField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("dayOfMonth", unsupportedField.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_Long() {
        unsupportedField.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_Long() {
        unsupportedField.getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_Long_Long() {
        unsupportedField.getValue(1000L, new DateTime().getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_Long_Long() {
        unsupportedField.getValueAsLong(1000L, new DateTime().getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Int() {
        unsupportedField.getMillis(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Long() {
        unsupportedField.getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Int_Long() {
        unsupportedField.getMillis(10, new DateTime().getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_Long_Long() {
        unsupportedField.getMillis(100L, new DateTime().getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_Long_Int() {
        unsupportedField.add(new DateTime().getMillis(), 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_Long_Long() {
        unsupportedField.add(new DateTime().getMillis(), 100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_Long_Long() {
        unsupportedField.getDifference(new DateTime().getMillis(), new DateTime().getMillis());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_Long_Long() {
        unsupportedField.getDifferenceAsLong(new DateTime().getMillis(), new DateTime().getMillis());
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo_SupportedField() {
        DurationField supportedField = DurationFieldType.year().getField();
        assertEquals(1, unsupportedField.compareTo(supportedField));
    }

    @Test
    public void testCompareTo_UnsupportedField() {
        DurationField otherUnsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.hourOfHalfday());
        assertEquals(0, unsupportedField.compareTo(otherUnsupportedField));
    }

    @Test
    public void testEquals_SameInstance() {
        assertTrue(unsupportedField.equals(unsupportedField));
    }

    @Test
    public void testEquals_SameField() {
        UnsupportedDurationField sameField = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        assertTrue(unsupportedField.equals(sameField));
    }

    @Test
    public void testEquals_NullName() {
        UnsupportedDurationField nullNameField = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertFalse(unsupportedField.equals(nullNameField));
    }

    @Test
    public void testEquals_DifferentField() {
        UnsupportedDurationField differentField = UnsupportedDurationField.getInstance(DurationFieldType.monthOfYear());
        assertFalse(unsupportedField.equals(differentField));
    }

    @Test
    public void testHashCode_SameField() {
        UnsupportedDurationField sameField = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        assertEquals(unsupportedField.hashCode(), sameField.hashCode());
    }

    @Test
    public void testHashCode_DifferentField() {
        UnsupportedDurationField differentField = UnsupportedDurationField.getInstance(DurationFieldType.monthOfYear());
        assertNotEquals(unsupportedField.hashCode(), differentField.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[dayOfMonth]", unsupportedField.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) unsupportedField.readResolve();
        assertEquals(unsupportedField.getName(), resolvedField.getName());
    }

    @Test
    public void testUnsupported() {
        UnsupportedOperationException exception = unsupportedField.unsupported();
        assertNotNull(exception);
        assertEquals("dayOfMonth field is unsupported", exception.getMessage());
    }
}