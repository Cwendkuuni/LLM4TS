import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 private static final UnsupportedDurationField DURATION_FIELD = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);

 @Test
 public void testGetType() {
     assertEquals(DurationFieldType.MILLISECONDS, DURATION_FIELD.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
     assertFalse(DURATION_FIELD.isSupported());
 }

 @Test
 public void testGetName() {
     assertEquals("milliseconds", DURATION_FIELD.getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValue() {
     long duration = 10L;
     DURATION_FIELD.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValueAsLong() {
     long duration = 10L;
     DURATION_FIELD.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValue_long_long() {
     long duration = 10L;
     long instant = 20L;
     DURATION_FIELD.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValueAsLong_long_long() {
     long duration = 10L;
     long instant = 20L;
     DURATION_FIELD.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetMillis_int() {
     int value = 10;
     DURATION_FIELD.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetMillis_long() {
     long value = 10L;
     DURATION_FIELD.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetMillis_int_long() {
     int value = 10;
     long instant = 20L;
     DURATION_FIELD.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetMillis_long_long() {
     long value = 10L;
     long instant = 20L;
     DURATION_FIELD.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testadd_long_int() {
     long instant = 10L;
     int value = 5;
     DURATION_FIELD.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testadd_long_long() {
     long instant = 10L;
     long value = 5L;
     DURATION_FIELD.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference_long_long() {
     long minuendInstant = 10L;
     long subtrahendInstant = 5L;
     DURATION_FIELD.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong_long_long() {
     long minuendInstant = 10L;
     long subtrahendInstant = 5L;
     DURATION_FIELD.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
     assertEquals(0, DURATION_FIELD.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
     assertEquals(0, DURATION_FIELD.compareTo(DURATION_FIELD));
 }

 @Test
 public void testEquals() {
     UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
     assertTrue(DURATION_FIELD.equals(other));
 }

 @Test
 public void testHashCode() {
     int hashcode1 = DURATION_FIELD.hashCode();
     UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
     int hashcode2 = other.hashCode();
     assertEquals(hashcode1, hashcode2);
 }

 @Test
 public void testToString() {
     assertEquals("UnsupportedDurationField[milliseconds]", DURATION_FIELD.toString());
 }

}