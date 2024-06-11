import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 @Test
 public void testGetInstance() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 assertNotNull(field);
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 assertFalse(field.isSupported());
 }

 @Test
 public void testGetValue() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 long duration = 1L; // arbitrary value
 assertThrows(UnsupportedOperationException.class, () -> field.getValue(duration));
 }

 @Test
 public void testAdd() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 long instant = 1L; // arbitrary value
 int value = 2;
 assertThrows(UnsupportedOperationException.class, () -> field.add(instant, value));
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 assertZero(field1.compareTo(field2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 assertTrue(field1.equals(field2));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 assertEquals(field1.hashCode(), field2.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE_DAY_OF_YEAR);
 assertEquals("UnsupportedDurationField[dayOfYear]", field.toString());
 }

 private void assertNotNull(Object obj) {
 if (obj == null) {
 fail("Expected object to not be null");
 }
 }

 private void assertZero(int value) {
 assertEquals(0, value);
 }

}