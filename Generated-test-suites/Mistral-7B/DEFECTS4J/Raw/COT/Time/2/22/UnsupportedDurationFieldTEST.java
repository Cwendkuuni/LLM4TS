import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

 private static final UnsupportedDurationField UNSUPPORTED_FIELD = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);

 @Test
 public void testGetInstance() {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
 assertEquals(DurationFieldType.MILLISECONDS, field.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 boolean isSupported = field.isSupported();
 assertEquals(false, isSupported);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 field.getValue(1000);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 field.getValueAsLong(1000);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 field.getValue(1000, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 field.getValueAsLong(1000, 0);
 }

 @Test
 public void testGetName() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 assertEquals("MILLISECONDS", field.getName());
 }

 @Test
 public void testIsPrecise() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 boolean isPrecise = field.isPrecise();
 assertEquals(true, isPrecise);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 field.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 field.getMillis(1, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 field.add(0, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAtInstant() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 field.add(0, 1, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 field.getDifference(0, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 field.getDifferenceAsLong(0, 0);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 long unitMillis = field.getUnitMillis();
 assertEquals(0, unitMillis);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField field1 = UNSUPPORTED_FIELD;
 UnsupportedDurationField field2 = UNSUPPORTED_FIELD;
 int comparisonResult = field1.compareTo(field2);
 assertEquals(0, comparisonResult);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField field1 = UNSUPPORTED_FIELD;
 UnsupportedDurationField field2 = UNSUPPORTED_FIELD;
 boolean equals = field1.equals(field2);
 assertEquals(true, equals);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 int hashCode = field.hashCode();
 }

 @Test
 public void testToString() {
 UnsupportedDurationField field = UNSUPPORTED_FIELD;
 String toString = field.toString();
 }

}