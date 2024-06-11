import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private UnsupportedDurationField getUnsupportedField(DurationFieldType type) {
 return UnsupportedDurationField.getInstance(type);
 }

 @Test
 public void testGetInstance() {
 DurationFieldType type = DurationFieldType.DAYS;
 UnsupportedDurationField unsupportedField = getUnsupportedField(type);
 UnsupportedDurationField anotherUnsupportedField = getUnsupportedField(type);
 assertEquals(unsupportedField, anotherUnsupportedField);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.DAYS);
 assertFalse(unsupportedField.isSupported());
 }

 @Test
 public void testGetName() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 assertEquals("YEARS", unsupportedField.getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLISECONDS);
 long duration = 12345L;
 unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.SECONDS);
 long duration = 12345L;
 unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue_long_long() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MINUTES);
 long duration = 12345L;
 long instant = 1234567890123L;
 unsupportedField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong_long_long() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.HOURS);
 long duration = 12345L;
 long instant = 1234567890123L;
 unsupportedField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_int() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 int value = 12345;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 long value = 12345L;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_int_long() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 int value = 12345;
 long instant = 1234567890123L;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long_long() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 long value = 12345L;
 long instant = 1234567890123L;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_int() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 long instant = 1234567890123L;
 int value = 12345;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_long() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 long instant = 1234567890123L;
 long value = 12345L;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference_long_long() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1234567889123L;
 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong_long_long() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1234567889123L;
 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 long unitMillis = unsupportedField.getUnitMillis();
 assertEquals(0, unitMillis);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = getUnsupportedField(DurationFieldType.MILLIS);
 UnsupportedDurationField unsupportedField2 = getUnsupportedField(DurationFieldType.MILLIS);
 int comparison = unsupportedField1.compareTo(unsupportedField2);
 assertEquals(0, comparison);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = getUnsupportedField(DurationFieldType.MILLIS);
 UnsupportedDurationField unsupportedField2 = getUnsupportedField(DurationFieldType.MILLIS);
 UnsupportedDurationField unsupportedField3 = getUnsupportedField(DurationFieldType.SECONDS);
 assertEquals(unsupportedField1, unsupportedField1);
 assertEquals(unsupportedField1, unsupportedField2);
 assertNotEquals(unsupportedField1, unsupportedField3);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 int hashCode = unsupportedField.hashCode();
 assertEquals(1, hashCode); // Expected to be different for each test run
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.MILLIS);
 String toString = unsupportedField.toString();
 assertTrue(toString.contains("UnsupportedDurationField"));
 assertTrue(toString.contains("MILLIS"));
 }
}