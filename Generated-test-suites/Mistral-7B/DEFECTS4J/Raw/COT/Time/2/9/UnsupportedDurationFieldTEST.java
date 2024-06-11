import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DURATION_FIELD_TYPE = DurationFieldType.HOURS();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(DURATION_FIELD_TYPE);
 assertEquals(DURATION_FIELD_TYPE, unsupportedDurationField.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 assertFalse(unsupportedDurationField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long duration = 1000L;
 unsupportedDurationField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long duration = 1000L;
 unsupportedDurationField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long duration = 1000L;
 long instant = 0L;
 unsupportedDurationField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long duration = 1000L;
 long instant = 0L;
 unsupportedDurationField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 int value = 1;
 unsupportedDurationField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValue() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long value = 1L;
 unsupportedDurationField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 int value = 1;
 long instant = 0L;
 unsupportedDurationField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndLongInstant() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long value = 1L;
 long instant = 0L;
 unsupportedDurationField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long instant = 0L;
 long value = 1L;
 unsupportedDurationField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLongValue() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long instant = 0L;
 long value = 1L;
 unsupportedDurationField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long minuendInstant = 0L;
 long subtrahendInstant = 1L;
 unsupportedDurationField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long minuendInstant = 0L;
 long subtrahendInstant = 1L;
 unsupportedDurationField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 long unitMillis = unsupportedDurationField.getUnitMillis();
 assertEquals(0, unitMillis);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedDurationField1 = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 UnsupportedDurationField unsupportedDurationField2 = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 int result = unsupportedDurationField1.compareTo(unsupportedDurationField2);
 assertEquals(0, result);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedDurationField1 = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 UnsupportedDurationField unsupportedDurationField2 = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 UnsupportedDurationField unsupportedDurationField3 = new UnsupportedDurationField(DurationFieldType.MINUTES());
 boolean result1 = unsupportedDurationField1.equals(unsupportedDurationField2);
 assertTrue(result1);
 UnsupportedDurationField unsupportedDurationField4 = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 boolean result2 = unsupportedDurationField1.equals(unsupportedDurationField4);
 assertTrue(result2);
 boolean result3 = unsupportedDurationField1.equals(unsupportedDurationField3);
 assertFalse(result3);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 int hashCode = unsupportedDurationField.hashCode();
 assertEquals(hashCode, unsupportedDurationField.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedDurationField = new UnsupportedDurationField(DURATION_FIELD_TYPE);
 String toString = unsupportedDurationField.toString();
 assertEquals("UnsupportedDurationField[HOURS]", toString);
 }

}