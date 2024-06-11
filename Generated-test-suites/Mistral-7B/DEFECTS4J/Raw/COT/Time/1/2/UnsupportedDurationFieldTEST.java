import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private static UnsupportedDurationField getUnsupportedField(DurationFieldType type) {
 return UnsupportedDurationField.getInstance(type);
 }

 @Test
 public void testGetInstanceWithValidType() {
 DurationFieldType type = DurationFieldType.YEARS;
 UnsupportedDurationField unsupportedField = getUnsupportedField(type);
 assertEquals(type, unsupportedField.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 assertFalse(unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long duration = 1234567890L;
 unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long duration = 1234567890L;
 unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long duration = 1234567890L;
 long instant = 1234567890L;
 unsupportedField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long duration = 1234567890L;
 long instant = 1234567890L;
 unsupportedField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 int value = 123;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValue() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long value = 123L;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 int value = 123;
 long instant = 1234567890L;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndLongInstant() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long value = 123L;
 long instant = 1234567890L;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long instant = 1234567890L;
 int value = 123;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLongValue() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long instant = 1234567890L;
 long value = 123L;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1234567880L;
 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1234567880L;
 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 long result = unsupportedField.getUnitMillis();
 assertEquals(0, result);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = getUnsupportedField(DurationFieldType.YEARS);
 UnsupportedDurationField unsupportedField2 = getUnsupportedField(DurationFieldType.MONTHS);
 int result = unsupportedField1.compareTo(unsupportedField2);
 assertTrue(result > 0);
 }

 @Test
 public void testEqualsWithSameInstance() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 assertTrue(unsupportedField.equals(unsupportedField));
 }

 @Test
 public void testEqualsWithDifferentType() {
 UnsupportedDurationField unsupportedField1 = getUnsupportedField(DurationFieldType.YEARS);
 UnsupportedDurationField unsupportedField2 = getUnsupportedField(DurationFieldType.MONTHS);
 assertFalse(unsupportedField1.equals(unsupportedField2));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 String name = unsupportedField.getName();
 int expectedHashCode = name.hashCode();
 int actualHashCode = unsupportedField.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = getUnsupportedField(DurationFieldType.YEARS);
 String expectedString = "UnsupportedDurationField[YEARS]";
 String actualString = unsupportedField.toString();
 assertEquals(expectedString, actualString);
 }
}