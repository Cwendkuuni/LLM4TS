import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
    }

    @Test
    public void getType_ReturnsDurationFieldType() {
        assertEquals(DurationFieldType.MINUTE_TYPE, unsupportedDurationField.getType());
    }

    @Test
    public void getName_ReturnsFieldName() {
        assertEquals("minuteType", unsupportedDurationField.getName());
    }

    @Test
    public void isSupported_ReturnsFalse() {
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void isPrecise_ReturnsTrue() {
        assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValue_ThrowsUnsupportedOperationException() {
        unsupportedDurationField.getValue(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLong_ThrowsUnsupportedOperationException() {
        unsupportedDurationField.getValueAsLong(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueWithInstant_ThrowsUnsupportedOperationException() {
        unsupportedDurationField.getValue(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getValueAsLongWithInstant_ThrowsUnsupportedOperationException() {
        unsupportedDurationField.getValueAsLong(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithIntValue_ThrowsUnsupportedOperationException() {
        unsupportedDurationField.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithLongValue_ThrowsUnsupportedOperationException() {
        unsupportedDurationField.getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithIntValueAndInstant_ThrowsUnsupportedOperationException() {
        unsupportedDurationField.getMillis(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getMillisWithLongValueAndInstant_ThrowsUnsupportedOperationException() {
        unsupportedDurationField.getMillis(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add_ThrowsUnsupportedOperationException() {
        unsupportedDurationField.add(100L, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addWithLongValue_ThrowsUnsupportedOperationException() {
        unsupportedDurationField.add(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDifference_ReturnsUnsupportedOperationException() {
        unsupportedDurationField.getDifference(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDifferenceAsLong_ReturnsUnsupportedOperationException() {
        unsupportedDurationField.getDifferenceAsLong(100L, 200L);
    }

    @Test
    public void getUnitMillis_ReturnsZero() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void compareTo_ReturnsOneIfComparisonFieldIsSupported() {
        UnsupportedDurationField comparisonField = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        assertTrue(unsupportedDurationField.compareTo(comparisonField) > 0);
    }

    @Test
    public void compareTo_ReturnsZeroIfComparisonFieldIsNotSupported() {
        UnsupportedDurationField comparisonField = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_TYPE);
        assertEquals(0, unsupportedDurationField.compareTo(comparisonField));
    }

    @Test
    public void equals_ReturnsTrueIfComparisonIsSameObject() {
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
    }

    @Test
    public void equals_ReturnsTrueIfComparisonHasSameFieldName() {
        UnsupportedDurationField comparisonField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        assertTrue(unsupportedDurationField.equals(comparisonField));
    }

    @Test
    public void equals_ReturnsFalseIfComparisonIsDifferentClass() {
        Object comparison = new Object();
        assertFalse(unsupportedDurationField.equals(comparison));
    }

    @Test
    public void equals_ReturnsFalseIfComparisonHasDifferentFieldName() {
        UnsupportedDurationField comparisonField = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_TYPE);
        assertFalse(unsupportedDurationField.equals(comparisonField));
    }

    @Test
    public void hashCode_ReturnsHashCodeOfFieldName() {
        assertEquals("minuteType".hashCode(), unsupportedDurationField.hashCode());
    }

    @Test
    public void toString_ReturnsDebugString() {
        assertEquals("UnsupportedDurationField[minuteType]", unsupportedDurationField.toString());
    }

    @Test
    public void readResolve_ReturnsExistingInstance() {
        UnsupportedDurationField.readResolve();
        UnsupportedDurationField newField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_TYPE);
        assertSame(unsupportedDurationField, newField);
    }

}