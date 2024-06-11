import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

 private DurationFieldType type = DurationFieldType.HOURS;
 private UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);

 @Test
 public void testGetInstance() {
 DurationField instance1 = UnsupportedDurationField.getInstance(type);
 DurationField instance2 = UnsupportedDurationField.getInstance(type);
 Assert.assertEquals(instance1, instance2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 Assert.assertFalse(unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 long duration = 10L;
 unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 long duration = 10L;
 unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueLongInt() {
 long duration = 10L;
 long instant = 1234567890123L;
 unsupportedField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongLong() {
 long duration = 10L;
 long instant = 1234567890123L;
 unsupportedField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisInt() {
 int value = 10;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLong() {
 long value = 10L;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisIntLong() {
 int value = 10;
 long instant = 1234567890123L;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLongLong() {
 long value = 10L;
 long instant = 1234567890123L;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 long instant = 1234567890123L;
 unsupportedField.add(instant, 10L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInt() {
 long instant = 1234567890123L;
 unsupportedField.add(instant, 10);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceLongLong() {
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1337133713371L;
 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLongLong() {
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1337133713371L;
 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 long unitMillis = unsupportedField.getUnitMillis();
 Assert.assertEquals(0, unitMillis);
 }

 @Test
 public void testCompareTo() {
 DurationField other = DurationField.milliseconds(10);
 int comparison = unsupportedField.compareTo(other);
 Assert.assertEquals(0, comparison);
 }

 @Test
 public void testEquals() {
 Object other = new Object();
 Assert.assertFalse(unsupportedField.equals(other));
 UnsupportedDurationField otherUnsupportedField = UnsupportedDurationField.getInstance(type);
 Assert.assertEquals(unsupportedField, otherUnsupportedField);
 }

 @Test
 public void testHashCode() {
 int hashCode = unsupportedField.hashCode();
 // Different hashcodes for different objects should be unequal.
 Assert.assertNotEquals(hashCode, 1);
 }

 @Test
 public void testToString() {
 String toString = unsupportedField.toString();
 Assert.assertEquals("UnsupportedDurationField[HOURS]", toString);
 }

}