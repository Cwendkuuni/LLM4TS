import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(field1, field2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 assertFalse(unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 10;
 unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 10;
 unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 10;
 long instant = 1;
 unsupportedField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 10;
 long instant = 1;
 unsupportedField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 int value = 1;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long value = 1;
 long instant = 1;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long instant = 1;
 long value = 1;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAtInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long instant = 1;
 long value = 1;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long minuendInstant = 1;
 long subtrahendInstant = 1;
 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long minuendInstant = 1;
 long subtrahendInstant = 1;
 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long unitMillis = unsupportedField.getUnitMillis();
 assertEquals(0, unitMillis);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(HOURS);
 int result = unsupportedField1.compareTo(unsupportedField2);
 assertEquals(0, result);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedField3 = new UnsupportedDurationField(HOURS);
 assertTrue(unsupportedField1.equals(unsupportedField1));
 assertTrue(unsupportedField1.equals(unsupportedField2));
 assertFalse(unsupportedField1.equals(unsupportedField3));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 int hashCode1 = unsupportedField.hashCode();
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DAYS);
 int hashCode2 = unsupportedField2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 String toString = unsupportedField.toString();
 assertEquals("UnsupportedDurationField[days]", toString);
 }

 @Test
 public void testSingletonSerialization() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedFieldSerialized = (UnsupportedDurationField) SerializationUtils.deserialize(SerializationUtils.serialize(unsupportedField));
 assertEquals(unsupportedField, unsupportedFieldSerialized);
 }

}