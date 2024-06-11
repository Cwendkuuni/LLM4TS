import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(field1, field2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueInt() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getValue(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueLong() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getValueAsLong(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueIntLong() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getValue(1, 2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueLongLong() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getValueAsLong(1, 2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisInt() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLong() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getMillis(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisIntLong() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getMillis(1, 2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLongLong() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.getMillis(1L, 2L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.add(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLongLong() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 field.add(1, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceInt() {
 UnsupportedDurationField field1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field2 = new UnsupportedDurationField(DAYS);
 field1.getDifference(1, 2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceLong() {
 UnsupportedDurationField field1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field2 = new UnsupportedDurationField(DAYS);
 field1.getDifferenceAsLong(1, 2);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 assertEquals(0, field.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField field1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field2 = new UnsupportedDurationField(HOURS);
 assertEquals(0, field1.compareTo(field2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField field1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field2 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field3 = new UnsupportedDurationField(HOURS);
 assertEquals(field1, field1);
 assertEquals(field1, field2);
 assertNotEquals(field1, field3);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField field1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField field2 = new UnsupportedDurationField(DAYS);
 assertEquals(field1.hashCode(), field2.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 assertEquals("UnsupportedDurationField[days]", field.toString());
 }

 @Test
 public void testEqualsWithNull() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 assertFalse(field.equals(null));
 }

 @Test
 public void testEqualsWithOtherClass() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 assertFalse(field.equals(new Object()));
 }

 @Test
 public void testGetType() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 assertEquals(DAYS, field.getType());
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 assertFalse(field.isSupported());
 }

 @Test
 public void testIsPrecise() {
 UnsupportedDurationField field = new UnsupportedDurationField(DAYS);
 assertTrue(field.isPrecise());
 }

}