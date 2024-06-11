import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest extends BaseTestCase {

    @org.junit.Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedField1, unsupportedField2);
    }

    @org.junit.Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupportedField.getType());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        assertFalse(unsupportedField.isSupported());
    }

    @org.junit.Test
    public void testIsPrecise() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        assertTrue(unsupportedField.isPrecise());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        long duration = 1;
        unsupportedField.getValue(duration);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        long duration = 1;
        unsupportedField.getValueAsLong(duration);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValueAt() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        long duration = 1;
        long instant = 0;
        unsupportedField.getValue(duration, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAt() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        long duration = 1;
        long instant = 0;
        unsupportedField.getValueAsLong(duration, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        int value = 1;
        unsupportedField.getMillis(value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAt() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        long value = 1;
        unsupportedField.getMillis(value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        long instant = 0;
        int value = 1;
        unsupportedField.add(instant, value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        long instant = 0;
        long value = 1;
        unsupportedField.add(instant, value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        long minuendInstant = 0;
        long subtrahendInstant = 0;
        unsupportedField.getDifference(minuendInstant, subtrahendInstant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        long minuendInstant = 0;
        long subtrahendInstant = 0;
        unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @org.junit.Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @org.junit.Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        DurationField supportedField = DurationField.standardHours();
        assertEquals(1, unsupportedField1.compareTo(supportedField));
        assertEquals(0, unsupportedField1.compareTo(unsupportedField2));
    }

    @org.junit.Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        assertTrue(unsupportedField1.equals(unsupportedField2));
    }

    @org.junit.Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        String name = DurationFieldType.INSTANCE.getName();
        int hashCode = name.hashCode();
        assertEquals(hashCode, unsupportedField.hashCode());
    }

    @org.junit.Test
    public void testToString() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        String expected = "UnsupportedDurationField[null]";
        String result = unsupportedField.toString();
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testSingletonSerialization() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(unsupportedField1);
        objOut.close();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objIn = new ObjectInputStream(in);
        UnsupportedDurationField unsupportedField2 = (UnsupportedDurationField) objIn.readObject();
        objIn.close();
        assertEquals(unsupportedField1, unsupportedField2);
    }
}