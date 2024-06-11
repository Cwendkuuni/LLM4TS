import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final UnsupportedDurationField DURATION_FIELD = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);

 @Test
 public void testGetInstance() {
 DurationField unsupportedMilliseconds = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 DurationField unsupportedMinutes = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);

 assertEquals("Unequal UnsupportedDurationField instances", unsupportedMilliseconds, UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS));
 assertEquals("Unequal UnsupportedDurationField instances", unsupportedMinutes, UnsupportedDurationField.getInstance(DurationFieldType.MINUTES));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 assertEquals(false, DURATION_FIELD.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 assertEquals(0, DURATION_FIELD.getValue(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 assertEquals(0, DURATION_FIELD.getValueAsLong(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 assertEquals(0, DURATION_FIELD.getValue(1, 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 assertEquals(0, DURATION_FIELD.getValueAsLong(1, 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 assertEquals(0, DURATION_FIELD.getMillis(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithInstant() {
 assertEquals(0, DURATION_FIELD.getMillis(1, 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 assertEquals(0, DURATION_FIELD.add(1, 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithInstant() {
 assertEquals(0, DURATION_FIELD.add(1, 1, 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 assertEquals(0, DURATION_FIELD.getDifference(1, 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 assertEquals(0, DURATION_FIELD.getDifferenceAsLong(1, 1));
 }

 @Test
 public void testGetUnitMillis() {
 assertEquals(0, DURATION_FIELD.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 assertEquals(0, DURATION_FIELD.compareTo(DURATION_FIELD));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEqualsNull() {
 assertFalse(DURATION_FIELD.equals(null));
 }

 @Test
 public void testHashCode() {
 int hashCode = DURATION_FIELD.hashCode();
 assertEquals(hashCode, DURATION_FIELD.hashCode());
 }

 @Test
 public void testToString() {
 String toString = DURATION_FIELD.toString();
 assertEquals("UnsupportedDurationField[MILLISECONDS]", toString);
 }

 @Test
 public void testSerialize() {
 UnsupportedDurationField unsupportedMillisecondsSerialized = (UnsupportedDurationField) DURATION_FIELD.readResolve();
 assertEquals(unsupportedMillisecondsSerialized, DURATION_FIELD);
 }
}