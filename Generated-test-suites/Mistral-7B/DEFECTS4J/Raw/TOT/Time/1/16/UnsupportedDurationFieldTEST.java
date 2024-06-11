import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 @Test
 public void testInstance() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INSTANT_SECONDS);
 assertNotNull(field);
 assertEquals(DurationFieldType.INSTANT_SECONDS, field.getType());
 }

 @Test(expected = AssertionError.class)
 public void testGetValueUnsupported() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 long duration = 10;
 try {
 field.getValue(duration);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testGetValueAsLongUnsupported() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 long duration = 10;
 try {
 field.getValueAsLong(duration);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testGetValueUnsupportedInstant() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 long duration = 10;
 long instant = 1234567890L;
 try {
 field.getValue(duration, instant);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testGetValueAsLongUnsupportedInstant() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 long duration = 10;
 long instant = 1234567890L;
 try {
 field.getValueAsLong(duration, instant);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testGetMillisUnsupported() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 int value = 10;
 try {
 field.getMillis(value);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testGetMillisUnsupportedLong() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 long value = 10L;
 try {
 field.getMillis(value);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testGetMillisUnsupportedValueInstant() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 int value = 10;
 long instant = 1234567890L;
 try {
 field.getMillis(value, instant);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testGetMillisUnsupportedValueLongInstant() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 long value = 10L;
 long instant = 1234567890L;
 try {
 field.getMillis(value, instant);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testAddUnsupported() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 long instant = 1234567890L;
 try {
 field.add(instant, 10);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testGetDifferenceUnsupported() {
 UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 UnsupportedDurationField fieldB = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1000L;
 try {
 fieldA.getDifference(minuendInstant, subtrahendInstant);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testGetDifferenceAsLongUnsupported() {
 UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 UnsupportedDurationField fieldB = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1000L;
 try {
 fieldA.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testCompareToNull() {
 UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 DurationField fieldB = null;
 int comparison = fieldA.compareTo(fieldB);
 assertEquals(1, comparison);
 }

 @Test
 public void testCompareToSupported() {
 UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 DurationFieldType supportedType = DurationFieldType.INSTANT_SECONDS;
 DurationField fieldB = DurationField.standardInstances().get(supportedType);
 int comparison = fieldA.compareTo(fieldB);
 assertEquals(1, comparison);
 }

 @Test
 public void testEqualsSameInstance() {
 UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 assertEquals(fieldA, fieldA);
 }

 @Test
 public void testEqualsDifferentType() {
 UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 UnsupportedDurationField fieldB = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 assertNotEquals(fieldA, fieldB);
 }

 @Test
 public void testEqualsDifferentName() {
 UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 UnsupportedDurationField fieldB = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
 assertNotEquals(fieldA, fieldB);
 }

 @Test
 public void testHashCodeSameName() {
 UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 UnsupportedDurationField fieldB = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 assertEquals(fieldA.hashCode(), fieldB.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.INVALID);
 String expected = "UnsupportedDurationField[INVALID]";
 assertEquals(expected, field.toString());
 }
}