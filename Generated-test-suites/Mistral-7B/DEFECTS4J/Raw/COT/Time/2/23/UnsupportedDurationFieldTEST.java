import org.joda.time.BaseTestCase;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

public class UnsupportedDurationFieldTest extends BaseTestCase {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();
 private static final DurationFieldType MILLISECONDS = DurationFieldType.millis();

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(unsupportedField1, unsupportedField2);
 }

 @org.junit.Test
 public void testGetType() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 assertEquals(MINUTES, unsupportedField.getType());
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 assertFalse(unsupportedField.isSupported());
 }

 @org.junit.Test
 public void testIsPrecise() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 assertTrue(unsupportedField.isPrecise());
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 long duration = 60000;
 unsupportedField.getValue(duration);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 long duration = 60000;
 unsupportedField.getValueAsLong(duration);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 long duration = 60000;
 long instant = 1234567890;
 unsupportedField.getValue(duration, instant);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 long duration = 60000;
 long instant = 1234567890;
 unsupportedField.getValueAsLong(duration, instant);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 int value = 60;
 unsupportedField.getMillis(value);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 long value = 60000;
 unsupportedField.getMillis(value);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 long value = 60000;
 long instant = 1234567890;
 unsupportedField.getMillis(value, instant);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 long instant = 1234567890;
 long value = 60;
 unsupportedField.add(instant, value);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testAddWithInstans() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 long instant1 = 1234567890;
 long instant2 = 234567890L;
 long value = 60;
 unsupportedField.add(instant1, instant2, value);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 long minuendInstant = 1234567890;
 long subtrahendInstant = 13567890;
 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 long minuendInstant = 1234567890;
 long subtrahendInstant = 13567890;
 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @org.junit.Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 assertEquals(0, unsupportedField.getUnitMillis());
 }

 @org.junit.Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(MINUTES);
 assertEquals(0, unsupportedField1.compareTo(unsupportedField2));
 }

 @org.junit.Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(MINUTES);
 assertTrue(unsupportedField1.equals(unsupportedField2));
 }

 @org.junit.Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 int expectedHashCode = MINUTES.hashCode();
 int actualHashCode = unsupportedField.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @org.junit.Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 String expectedString = "UnsupportedDurationField[minutes]";
 String actualString = unsupportedField.toString();
 assertEquals(expectedString, actualString);
 }

 @org.junit.Test
 public void testSingletonSerialization() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(MINUTES);
 byte[] serialized = serialize(unsupportedField);
 UnsupportedDurationField unsupportedFieldDeserialized = (UnsupportedDurationField) deserialize(serialized);
 assertEquals(unsupportedField, unsupportedFieldDeserialized);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithNullType() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(null);
 }

}