import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private UnsupportedDurationField getUnsupportedDurationField(DurationFieldType type) {
 return UnsupportedDurationField.getInstance(type);
 }

 @Test
 public void testGetInstance() {
 DurationFieldType type = DurationFieldType.MINUTES;
 UnsupportedDurationField unsupported = getUnsupportedDurationField(type);
 UnsupportedDurationField another = getUnsupportedDurationField(type);
 assertEquals(unsupported, another);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 assertFalse(unsupported.isSupported());
 }

 @Test
 public void testGetName() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 assertEquals("Minutes", unsupported.getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 long duration = 1000;
 unsupported.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 long duration = 1000;
 unsupported.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 long duration = 1000;
 long instant = 123456789;
 unsupported.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 long duration = 1000;
 long instant = 123456789;
 unsupported.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 int value = 1;
 unsupported.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValue() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 long value = 1;
 unsupported.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 int value = 1;
 long instant = 123456789;
 unsupported.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValueAndInstant() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 long value = 1;
 long instant = 123456789;
 unsupported.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 long instant = 123456789;
 long value = 1;
 unsupported.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLongValue() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 long instant = 123456789;
 long value = 1;
 unsupported.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 long minuendInstant = 123456789;
 long subtrahendInstant = 123456788;
 unsupported.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 long minuendInstant = 123456789;
 long subtrahendInstant = 123456788;
 unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 assertEquals(0, unsupported.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupported1 = getUnsupportedDurationField(DurationFieldType.MINUTES);
 UnsupportedDurationField unsupported2 = getUnsupportedDurationField(DurationFieldType.HOURS);
 int comparisonResult = unsupported1.compareTo(unsupported2);
 assertTrue(comparisonResult > 0);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupported1 = getUnsupportedDurationField(DurationFieldType.MINUTES);
 UnsupportedDurationField unsupported2 = getUnsupportedDurationField(DurationFieldType.MINUTES);
 UnsupportedDurationField unsupported3 = getUnsupportedDurationField(DurationFieldType.HOURS);
 assertTrue(unsupported1.equals(unsupported1));
 assertTrue(unsupported1.equals(unsupported2));
 assertFalse(unsupported1.equals(unsupported3));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupported1 = getUnsupportedDurationField(DurationFieldType.MINUTES);
 UnsupportedDurationField unsupported2 = getUnsupportedDurationField(DurationFieldType.MINUTES);
 int hashCode1 = unsupported1.hashCode();
 int hashCode2 = unsupported2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 assertEquals("UnsupportedDurationField[Minutes]", unsupported.toString());
 }

 @Test
 public void testSingletonSerialization() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.MINUTES);
 byte[] serialized = new Gson().toJsonByteArray(unsupported);
 UnsupportedDurationField deserialized = new Gson().fromJson(new String(serialized), UnsupportedDurationField.class);
 assertEquals(unsupported, deserialized);
 }
}