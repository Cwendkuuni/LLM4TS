import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private DurationFieldType dayOfWeekInMonth = DurationFieldType.dayOfWeekInMonth();
 private DurationFieldType hours = DurationFieldType.hours();
 private DurationFieldType minutes = DurationFieldType.minutes();
 private DurationFieldType seconds = DurationFieldType.seconds();
 private DurationFieldType millis = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(dayOfWeekInMonth);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(dayOfWeekInMonth);
 assertEquals(unsupportedField1, unsupportedField2);
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(dayOfWeekInMonth);
 assertFalse(unsupportedField.isSupported());
 }

 @Test
 public void testIsPrecise() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(dayOfWeekInMonth);
 assertTrue(unsupportedField.isPrecise());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(dayOfWeekInMonth);
 unsupportedField.getValue(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(dayOfWeekInMonth);
 unsupportedField.getValueAsLong(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValue_long_long() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(dayOfWeekInMonth);
 unsupportedField.getValue(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValueAsLong_long_long() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(dayOfWeekInMonth);
 unsupportedField.getValueAsLong(1L, 1L);
 }

 @Test
 public void testgetMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(millis);
 assertEquals(0, unsupportedField.getMillis(0));
 }

 @Test
 public void testcompareTo() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(hours);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(minutes);
 assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(hours);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(hours);
 assertTrue(unsupportedField1.equals(unsupportedField2));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(hours);
 int hashCode = unsupportedField.hashCode();
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(hours);
 assertEquals(hashCode, unsupportedField2.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(seconds);
 assertEquals("UnsupportedDurationField[seconds]", unsupportedField.toString());
 }

 @Test
 public void testSingletonSerialization() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(hours);
 byte[] bytes = serialize(unsupportedField);
 UnsupportedDurationField unsupportedFieldDeserialized = deserialize(bytes, UnsupportedDurationField.class);
 assertEquals(unsupportedField, unsupportedFieldDeserialized);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_long() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(hours);
 unsupportedField.add(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_int() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(hours);
 unsupportedField.add(1L, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(hours);
 unsupportedField.getDifference(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(hours);
 unsupportedField.getDifferenceAsLong(1L, 1L);
 }
}