import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private DurationFieldType testType = DurationFieldType.milliseconds();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(testType);
 assertEquals(unsupportedDurationField, unsupportedDurationField2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 assertFalse(unsupportedDurationField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 long duration = 100;
 unsupportedDurationField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 long duration = 100;
 unsupportedDurationField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 long instant = 100;
 unsupportedDurationField.getValue(instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 long instant = 100;
 unsupportedDurationField.getValueAsLong(instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 int value = 1;
 unsupportedDurationField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValue() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 long value = 1;
 unsupportedDurationField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 int value = 1;
 long instant = 1;
 unsupportedDurationField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndLongInstant() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 long value = 1;
 long instant = 1;
 unsupportedDurationField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 long instant = 1;
 unsupportedDurationField.add(instant, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLong() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 long instant = 1;
 unsupportedDurationField.add(instant, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 long minuendInstant = 1;
 long subtrahendInstant = 1;
 unsupportedDurationField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 long minuendInstant = 1;
 long subtrahendInstant = 1;
 unsupportedDurationField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 DurationField supportedDurationField = DurationField.standardHours();
 assertFalse(unsupportedDurationField.compareTo(supportedDurationField) > 0);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(testType);
 assertEquals(unsupportedDurationField, unsupportedDurationField2);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(testType);
 assertEquals(unsupportedDurationField.hashCode(), unsupportedDurationField2.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 assertEquals("UnsupportedDurationField[milliseconds]", unsupportedDurationField.toString());
 }

 @Test
 public void testSingletonSerialization() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(testType);
 byte[] bytes = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(unsupportedDurationField));
 UnsupportedDurationField unsupportedDurationFieldDeserialized = (UnsupportedDurationField) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
 assertEquals(unsupportedDurationField, unsupportedDurationFieldDeserialized);
 }

}