import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DURATION_FIELD_TYPE = DurationFieldType.MINUTES;
 private static final UnsupportedDurationField UNSUPPORTED_FIELD = UnsupportedDurationField.getInstance(DURATION_FIELD_TYPE);

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DURATION_FIELD_TYPE);
 Assert.assertSame(UNSUPPORTED_FIELD, unsupportedField);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 boolean isSupported = UNSUPPORTED_FIELD.isSupported();
 Assert.assertFalse(isSupported);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 long duration = 100;
 int value = UNSUPPORTED_FIELD.getValue(duration);
 Assert.fail("Unexpected value: " + value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 long duration = 100;
 long value = UNSUPPORTED_FIELD.getValueAsLong(duration);
 Assert.fail("Unexpected value: " + value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstants() {
 long minuendInstant = 0;
 long subtrahendInstant = 100;
 int difference = UNSUPPORTED_FIELD.getDifference(minuendInstant, subtrahendInstant);
 Assert.fail("Unexpected value: " + difference);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstants() {
 long minuendInstant = 0;
 long subtrahendInstant = 100;
 long differenceAsLong = UNSUPPORTED_FIELD.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 Assert.fail("Unexpected value: " + differenceAsLong);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 long instant = 0;
 long value = 10;
 long result = UNSUPPORTED_FIELD.add(instant, value);
 Assert.fail("Unexpected value: " + result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLongValue() {
 long instant = 0;
 long value = 10;
 long result = UNSUPPORTED_FIELD.add(instant, value);
 Assert.fail("Unexpected value: " + result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 int value = 10;
 long millis = UNSUPPORTED_FIELD.getMillis(value);
 Assert.fail("Unexpected value: " + millis);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValue() {
 long value = 10;
 long millis = UNSUPPORTED_FIELD.getMillis(value);
 Assert.fail("Unexpected value: " + millis);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithInstantAndValue() {
 long instant = 0;
 int value = 10;
 long millis = UNSUPPORTED_FIELD.getMillis(instant, value);
 Assert.fail("Unexpected value: " + millis);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithInstantAndLongValue() {
 long instant = 0;
 long value = 10;
 long millis = UNSUPPORTED_FIELD.getMillis(instant, value);
 Assert.fail("Unexpected value: " + millis);
 }

 @Test
 public void testCompareTo() {
 DurationField supportedField = new DurationField(1, DurationFieldType.SECONDS);
 int comparisonResult = UNSUPPORTED_FIELD.compareTo(supportedField);
 Assert.assertTrue(comparisonResult > 0);
 }

 @Test
 public void testEquals() {
 Object other = new Object();
 Assert.assertFalse(UNSUPPORTED_FIELD.equals(other));

 UnsupportedDurationField otherUnsupportedField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 Assert.assertTrue(UNSUPPORTED_FIELD.equals(otherUnsupportedField));
 }

 @Test
 public void testHashCode() {
 int hashcode = UNSUPPORTED_FIELD.hashCode();
 Integer hashcodeAsInt = Integer.valueOf(hashcode);
 String name = UNSUPPORTED_FIELD.getName();
 String nameAsString = name.intern();
 UnsupportedDurationField otherUnsupportedField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 Assert.assertEquals(hashcodeAsInt.intValue(), otherUnsupportedField.hashCode());
 }

 @Test
 public void testToString() {
 String toString = UNSUPPORTED_FIELD.toString();
 Assert.assertEquals("UnsupportedDurationField[MINUTES]", toString);
 }

 @Test
 public void testReadResolve() {
 UnsupportedDurationField resolvedField = (UnsupportedDurationField) UNSUPPORTED_FIELD.readResolve();
 Assert.assertSame(UNSUPPORTED_FIELD, resolvedField);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupported() {
 UNSUPPORTED_FIELD.getValue(0);
 }
}