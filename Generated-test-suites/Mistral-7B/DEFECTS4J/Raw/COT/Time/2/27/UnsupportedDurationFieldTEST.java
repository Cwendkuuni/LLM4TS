import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HALF_HOURS = DurationFieldType.halfHours();
 private static final DurationFieldType MILLISECONDS = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(HALF_HOURS);
 UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertEquals(DAYS, field1.getType());
 assertEquals(HALF_HOURS, field2.getType());
 assertEquals(MILLISECONDS, field3.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 unsupportedField.isSupported();
 }

 @Test
 public void testGetName() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 assertEquals("days", field.getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getValue(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getValueAsLong(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getValue(1, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getValueAsLong(1, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField field = new UnsupportedDurationField(MILLISECONDS);
 field.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValue() {
 UnsupportedDurationField field = new UnsupportedDurationField(MILLISECONDS);
 field.getMillis(1000);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField field = new UnsupportedDurationField(MILLISECONDS);
 field.getMillis(1000, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.add(0, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithInstant() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.add(0, 1, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getDifference(0, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getDifferenceAsLong(0, 0);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField field = new UnsupportedDurationField(MILLISECONDS);
 assertEquals(0, field.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField field1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field2 = new UnsupportedDurationField(HALF_HOURS);
 assertEquals(0, field1.compareTo(field2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField field1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field2 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field3 = new UnsupportedDurationField(HALF_HOURS);

 assertEquals(field1, field1);
 assertEquals(field1, field1);
 assertNotEquals(field1, field2);
 assertNotEquals(field1, field3);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField field1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field2 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field3 = new UnsupportedDurationField(HALF_HOURS);

 assertEquals(field1.hashCode(), field1.hashCode());
 assertEquals(field1.hashCode(), field1.hashCode());
 assertNotEquals(field1.hashCode(), field2.hashCode());
 assertNotEquals(field1.hashCode(), field3.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 assertEquals("UnsupportedDurationField[days]", field.toString());
 }
}