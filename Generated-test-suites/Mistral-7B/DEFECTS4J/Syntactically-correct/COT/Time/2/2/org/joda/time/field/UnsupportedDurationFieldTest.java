import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private DurationFieldType type = DurationFieldType.instant();
 private UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

 @Test
 public void testGetType() {
  assertEquals(type, unsupportedField.getType());
 }

 @Test
 public void testGetName() {
  assertEquals(type.getName(), unsupportedField.getName());
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
 public void testGetValue() {
  unsupportedField.getValue(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
  unsupportedField.getValueAsLong(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue_long_long() {
  unsupportedField.getValue(1L, 2L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong_long_long() {
  unsupportedField.getValueAsLong(1L, 2L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_int() {
  unsupportedField.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long() {
  unsupportedField.getMillis(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_int_long() {
  unsupportedField.getMillis(1, 2L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long_long() {
  unsupportedField.getMillis(1L, 2L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_int() {
  unsupportedField.add(1L, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_long() {
  unsupportedField.add(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference_long_long() {
  unsupportedField.getDifference(1L, 2L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong_long_long() {
  unsupportedField.getDifferenceAsLong(1L, 2L);
 }

 @Test
 public void testGetUnitMillis() {
  assertEquals(0, unsupportedField.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
  assertEquals(0, unsupportedField.compareTo(unsupportedField));
 }

 @Test
 public void testEquals() {
  UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
  assertTrue(unsupportedField.equals(unsupportedField2));
 }

 @Test
 public void testHashCode() {
  int hashCode1 = unsupportedField.hashCode();
  UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
  int hashCode2 = unsupportedField2.hashCode();
  assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
  assertEquals("UnsupportedDurationField[instant]", unsupportedField.toString());
 }

 @Test
 public void testSingletonSerialization() {
  UnsupportedDurationField unsupportedFieldSerialized = (UnsupportedDurationField) unsupportedField.readResolve();
  assertEquals(unsupportedField, unsupportedFieldSerialized);
 }

}