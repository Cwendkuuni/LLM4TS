import org.junit.Test;
import static org.junit.Assert.assertSame;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType durationFieldType = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(durationFieldType);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(durationFieldType);
        assertSame(unsupportedField1, unsupportedField2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);
        unsupportedField.getValue(0);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertSame(unsupportedField1, unsupportedField2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        int hashCode1 = unsupportedField1.hashCode();
        int hashCode2 = unsupportedField2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);
        String toString = unsupportedField.toString();
        String expected = "UnsupportedDurationField[DAYS]";
        assertEquals(expected, toString);
    }
}