import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 private static UnsupportedDurationField getUnsupportedField(DurationFieldType type) {
 return UnsupportedDurationField.getInstance(type);
 }

 @Test
 public void testGetInstance() {
 DurationFieldType type = DurationFieldType.DAYS;
 UnsupportedDurationField field = getUnsupportedField(type);
 assertEquals(type, field.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.isSupported();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.getValue(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.getValueAsLong(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.getValue(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.getValueAsLong(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLong() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.getMillis(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithIntAndLong() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.getMillis(1, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongAndLong() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.getMillis(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithInstant() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.add(1L, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLong() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.add(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceWithLongLong() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.getDifference(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLongWithLongLong() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 field.getDifferenceAsLong(1L, 1L);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 assertEquals(0, field.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField field1 = getUnsupportedField(DurationFieldType.DAYS);
 UnsupportedDurationField field2 = getUnsupportedField(DurationFieldType.DAYS);
 assertEquals(0, field1.compareTo(field2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField field1 = getUnsupportedField(DurationFieldType.DAYS);
 UnsupportedDurationField field2 = getUnsupportedField(DurationFieldType.DAYS);
 assertEquals(field1, field2);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 assertEquals(field.hashCode(), field.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType.DAYS);
 assertEquals("UnsupportedDurationField[DAYS]", field.toString());
 }
}