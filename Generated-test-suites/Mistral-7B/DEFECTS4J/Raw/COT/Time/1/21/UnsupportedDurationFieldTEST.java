import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final UnsupportedDurationField DURATION_FIELD = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

 @Test
 public void testGetType() {
  assertEquals(DurationFieldType.DAYS, DURATION_FIELD.getType());
 }

 @Test
 public void testGetName() {
  assertEquals("days", DURATION_FIELD.getName());
 }

 @Test
 public void testIsSupported() {
  assertFalse(DURATION_FIELD.isSupported());
 }

 @Test
 public void testGetValue_long() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.getValue(0L));
 }

 @Test
 public void testGetValueAsLong_long() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.getValueAsLong(0L));
 }

 @Test
 public void testGetValue_long_long() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.getValue(0L, 0L));
 }

 @Test
 public void testGetValueAsLong_long_long() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.getValueAsLong(0L, 0L));
 }

 @Test
 public void testGetMillis_int() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.getMillis(0));
 }

 @Test
 public void testGetMillis_long() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.getMillis(0L));
 }

 @Test
 public void testGetMillis_int_long() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.getMillis(0, 0L));
 }

 @Test
 public void testGetMillis_long_long() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.getMillis(0L, 0L));
 }

 @Test
 public void testAdd_long_int() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.add(0L, 0));
 }

 @Test
 public void testAdd_long_long() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.add(0L, 0L));
 }

 @Test
 public void testGetDifference_long_long() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.getDifference(0L, 0L));
 }

 @Test
 public void testGetDifferenceAsLong_long_long() {
  assertThrows(UnsupportedOperationException.class, () -> DURATION_FIELD.getDifferenceAsLong(0L, 0L));
 }

 @Test
 public void testGetUnitMillis() {
  assertEquals(0, DURATION_FIELD.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
  DurationField supportedField = new DurationField(1, DurationFieldType.DAYS);
  assertTrue(supportedField.compareTo(DURATION_FIELD) > 0);
 }

 @Test
 public void testEquals_Object() {
  UnsupportedDurationField equal = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
  assertEquals(equal, DURATION_FIELD);
 }

 @Test
 public void testHashCode() {
  assertEquals(DURATION_FIELD.hashCode(), DURATION_FIELD.hashCode());
 }

 @Test
 public void testToString() {
  assertEquals("UnsupportedDurationField[days]", DURATION_FIELD.toString());
 }

}