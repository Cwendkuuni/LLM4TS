import junit.framework.TestCase;

import org.joda.time.DurationFieldType;
import org.junit.*;

public class UnsupportedDurationFieldTest extends TestCase {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertNotNull(field);
        Assert.assertEquals(type, field.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String expected = type.getName();
        String actual = field.getName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        Assert.assertTrue(field.isPrecise());
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueLong() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(0, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long expected = 0;
        long actual = field.getUnitMillis();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        DurationField other = UnsupportedDurationField.getInstance(type);
        int expected = 0;
        int actual = field.compareTo(other);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        Assert.assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        Assert.assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String expected = "UnsupportedDurationField[" + type.getName() + ']';
        String actual = field.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.minute();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) field.readResolve();
        Assert.assertSame(field, resolvedField);
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(iType + " field is unsupported");
    }
}