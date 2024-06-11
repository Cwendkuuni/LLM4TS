import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.*; // for assert(equality) and assertThrows

public class UnsupportedDurationFieldTest {

 private DurationFieldType type = DurationFieldType.MILLISECONDS;
 private UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

 @Test
 public void testGetInstance() {
 UnsupportedDurationField instance1 = UnsupportedDurationField.getInstance(type);
 UnsupportedDurationField instance2 = UnsupportedDurationField.getInstance(type);
 assertEquals(instance1, instance2);
 }

 @Test
 public void testGetType() {
 assertEquals(type, unsupportedField.getType());
 }

 @Test
 public void testGetName() {
 assertEquals(type.getName(), unsupportedField.getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 assertFalse(unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsPrecise() {
 assertTrue(unsupportedField.isPrecise());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 long duration = 123456789;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(duration));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 long duration = 123456789;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(duration));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue_long_long() {
 long minuendInstant = 123456789;
 long subtrahendInstant = 987654321;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(minuendInstant, subtrahendInstant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong_long_long() {
 long minuendInstant = 123456789;
 long subtrahendInstant = 987654321;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(minuendInstant, subtrahendInstant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 int value = 123;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long() {
 long value = 123456789;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_int_long() {
 int value = 123;
 long instant = 123456789;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(value, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long_long() {
 long value = 123456789;
 long instant = 123456789;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(value, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_int() {
 long instant = 123456789;
 int value = 123;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(instant, value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_long() {
 long instant = 123456789;
 long value = 123456789;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(instant, value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference_long_long() {
 long minuendInstant = 123456789;
 long subtrahendInstant = 987654321;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(minuendInstant, subtrahendInstant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong_long_long() {
 long minuendInstant = 123456789;
 long subtrahendInstant = 987654321;
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testGetUnitMillis() {
 long unitMillis = unsupportedField.getUnitMillis();
 assertEquals(0, unitMillis);
 }

 @Test
 public void testCompareTo() {
 DurationField durationField = DurationField.standardHours(1);
 int comparisonResult = unsupportedField.compareTo(durationField);
 assertEquals(0, comparisonResult);
 }

 @Test
 public void testHashCode() {
 int hashCode = unsupportedField.hashCode();
 assertEquals(type.hashCode(), hashCode);
 }

 @Test
 public void testToString() {
 String toString = unsupportedField.toString();
 assertEquals("UnsupportedDurationField[MILLISECONDS]", toString);
 }

 @Test
 public void testSingletonSerialization() {
 UnsupportedDurationField unsupportedFieldClone = (UnsupportedDurationField) unsupportedField.readResolve();
 assertEquals(unsupportedField, unsupportedFieldClone);
 }

}