import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 @Test
 public void testUnsupportedDurationField() {
 // Get an instance of UnsupportedDurationField
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.FIELD_HOURS);

 // Test methods that always throw UnsupportedOperationException
 assertEquals(0, unsupportedField.getValue(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1, 1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1, 1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1, 1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1, 1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1, 1L));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(1, 1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(1, 1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getUnitMillis());
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.compareTo(null));

 // Test equals and hashcode
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.FIELD_HOURS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.FIELD_HOURS);
 UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(DurationFieldType.FIELD_MINUTES);
 assertEquals(unsupportedField1, unsupportedField1);
 assertEquals(unsupportedField1, unsupportedField2);
 assertNotEquals(unsupportedField1, unsupportedField3);
 }
}