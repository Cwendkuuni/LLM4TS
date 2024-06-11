import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 @Test
 public void testUnsupportedFieldInstance() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 DurationFieldType type = unsupportedField.getType();
 assertEquals("days", type.getName());
 assertFalse(unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueUnsupportedField() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 5L;
 int value = unsupportedField.getValue(duration);
 fail("Should have thrown UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongUnsupportedField() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 5L;
 long value = unsupportedField.getValueAsLong(duration);
 fail("Should have thrown UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueInstantsUnsupportedField() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 5L;
 long instant = 1234567890123L;
 int value = unsupportedField.getValue(duration, instant);
 fail("Should have thrown UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongInstantsUnsupportedField() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long duration = 5L;
 long instant = 1234567890123L;
 long value = unsupportedField.getValueAsLong(duration, instant);
 fail("Should have thrown UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisUnsupportedField() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 int value = unsupportedField.getDifference(0, 1);
 long millis = unsupportedField.getMillis(value);
 fail("Should have thrown UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLongUnsupportedField() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long value = 1L;
 long millis = unsupportedField.getMillis(value);
 fail("Should have thrown UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisInstantsUnsupportedField() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long value = 1L;
 long instant = 1234567890123L;
 long millis = unsupportedField.getMillis(value, instant);
 fail("Should have thrown UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddUnsupportedField() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long instant = 1234567890123L;
 long value = 5L;
 long result = unsupportedField.add(instant, value);
 fail("Should have thrown UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLongUnsupportedField() {
 DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 long instant = 1234567890123L;
 long value = 5L;
 long result = unsupportedField.add(instant, value);
 fail("Should have thrown UnsupportedOperationException");
 }

 @Test
 public void testCompareToUnsupportedField() {
 DurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 DurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 int compare = unsupportedField1.compareTo(unsupportedField2);
 assertEquals(0, compare);
 }
}