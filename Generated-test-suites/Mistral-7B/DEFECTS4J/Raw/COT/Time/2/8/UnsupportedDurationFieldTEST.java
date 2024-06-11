import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 private UnsupportedDurationField getUnsupportedField(DurationFieldType type) {
 return UnsupportedDurationField.getInstance(type);
 }

 @Test
 public void testGetInstance() {
 DurationFieldType type = DurationFieldType. seconds();
 UnsupportedDurationField unsupported = getUnsupportedField(type);
 UnsupportedDurationField another = getUnsupportedField(type);
 assertEquals(unsupported, another);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 assertFalse(field.isSupported());
 }

 @Test
 public void testGetName() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 assertEquals("Seconds", field.getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 field.getValue(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 field.getValueAsLong(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 field.getValue(0, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 field.getValueAsLong(0, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 field.getMillis(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 field.getMillis(0, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 field.add(0, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAtInstant() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 field.add(0, 1, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 field.getDifference(0, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 field.getDifferenceAsLong(0, 0);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 assertEquals(0, field.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField field1 = getUnsupportedField(DurationFieldType. seconds());
 UnsupportedDurationField field2 = getUnsupportedField(DurationFieldType. seconds());
 assertEquals(0, field1.compareTo(field2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField field1 = getUnsupportedField(DurationFieldType. seconds());
 UnsupportedDurationField field2 = getUnsupportedField(DurationFieldType. seconds());
 UnsupportedDurationField field3 = getUnsupportedField(DurationFieldType. milliseconds());
 assertTrue(field1.equals(field1));
 assertTrue(field1.equals(field2));
 assertFalse(field1.equals(field3));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField field1 = getUnsupportedField(DurationFieldType. seconds());
 UnsupportedDurationField field2 = getUnsupportedField(DurationFieldType. seconds());
 UnsupportedDurationField field3 = getUnsupportedField(DurationFieldType. milliseconds());
 assertEquals(field1.hashCode(), field1.hashCode());
 assertEquals(field1.hashCode(), field2.hashCode());
 assertNotEquals(field1.hashCode(), field3.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField field = getUnsupportedField(DurationFieldType. seconds());
 assertEquals("UnsupportedDurationField[Seconds]", field.toString());
 }

 @Test
 public void testReadResolve() {
 UnsupportedDurationField field1 = getUnsupportedField(DurationFieldType. seconds());
 Object resolved = field1.readResolve();
 UnsupportedDurationField field2 = (UnsupportedDurationField) resolved;
 assertEquals(field1, field2);
 }

}