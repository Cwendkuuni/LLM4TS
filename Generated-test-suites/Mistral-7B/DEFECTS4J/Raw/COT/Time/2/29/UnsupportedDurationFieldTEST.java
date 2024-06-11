import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;

public class UnsupportedDurationFieldTest extends BaseTestCase {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupportedDurationField.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        long duration = 10;
        unsupportedDurationField.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        long duration = 10;
        unsupportedDurationField.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        long duration = 10;
        long instant = 12345;
        unsupportedDurationField.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        long duration = 10;
        long instant = 12345;
        unsupportedDurationField.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        int value = 10;
        unsupportedDurationField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        long value = 10;
        unsupportedDurationField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValueAndInstant() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        int value = 10;
        long instant = 12345;
        unsupportedDurationField.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        long value = 10;
        long instant = 12345;
        unsupportedDurationField.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        long instant = 12345;
        int value = 10;
        unsupportedDurationField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        long instant = 12345;
        long value = 10;
        unsupportedDurationField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        long minuendInstant = 12345;
        long subtrahendInstant = 5678;
        unsupportedDurationField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        long minuendInstant = 12345;
        long subtrahendInstant = 5678;
        unsupportedDurationField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedDurationField1 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupportedDurationField2 = new UnsupportedDurationField(DurationFieldType.MINUTES());
        int comparisonResult = unsupportedDurationField1.compareTo(unsupportedDurationField2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedDurationField1 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupportedDurationField2 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupportedDurationField3 = new UnsupportedDurationField(DurationFieldType.MINUTES());
        assertTrue(unsupportedDurationField1.equals(unsupportedDurationField1));
        assertTrue(unsupportedDurationField1.equals(unsupportedDurationField2));
        assertFalse(unsupportedDurationField1.equals(unsupportedDurationField3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        int hashCode = unsupportedDurationField.hashCode();
        UnsupportedDurationField unsupportedDurationField2 = new UnsupportedDurationField(DurationFieldType.HOURS());
        int hashCode2 = unsupportedDurationField2.hashCode();
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DurationFieldType.HOURS());
        String toString = unsupportedDurationField.toString();
        String expectedString = "UnsupportedDurationField[HOURS]";
        assertEquals(expectedString, toString);
    }

    @Test
    public void testSingletonSerialization() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedDurationField2 = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(unsupportedDurationField1);
            objectOutputStream.close();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            objectOutputStream = new ObjectInputStream(byteArrayInputStream);
            unsupportedDurationField2 = (UnsupportedDurationField) objectOutputStream.readObject();
            objectOutputStream.close();
        } catch (IOException e) {
            fail("Failed to serialize and deserialize: " + e.getMessage());
        }
        assertEquals(unsupportedDurationField1, unsupportedDurationField2);
    }
}