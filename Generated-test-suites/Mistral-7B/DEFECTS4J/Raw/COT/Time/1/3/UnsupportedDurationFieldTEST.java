import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private UnsupportedDurationField getInstance(DurationFieldType type) {
 return UnsupportedDurationField.getInstance(type);
 }

 @Test
 public void testGetInstance() {
 DurationFieldType type = DurationFieldType.MILLISECONDS;
 UnsupportedDurationField unsupported = getInstance(type);
 assertEquals(type, unsupported.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 assertFalse(unsupported.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 long duration = 10L;
 unsupported.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 long duration = 10L;
 unsupported.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAt() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 long instant = 10L;
 unsupported.getValue(instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAt() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 long instant = 10L;
 unsupported.getValueAsLong(instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 int value = 10;
 unsupported.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLong() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 long value = 10L;
 unsupported.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisIntLong() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 int value = 10;
 long instant = 10L;
 unsupported.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLongLong() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 long value = 10L;
 long instant = 10L;
 unsupported.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 long instant = 10L;
 long value = 10L;
 unsupported.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 long instant = 10L;
 long value = 10L;
 unsupported.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 long minuendInstant = 10L;
 long subtrahendInstant = 10L;
 unsupported.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 long minuendInstant = 10L;
 long subtrahendInstant = 10L;
 unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 assertEquals(0, unsupported.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupported1 = getInstance(DurationFieldType.MILLISECONDS); UnsupportedDurationField unsupported2 = getInstance(DurationFieldType.MILLISECONDS); UnsupportedDurationField supported = new DurationField(DurationFieldType.MILLISECONDS, true);
 assertEquals(1, unsupported1.compareTo(supported));
 assertEquals(1, supported.compareTo(unsupported1));
 assertEquals(0, unsupported1.compareTo(unsupported2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupported1 = getInstance(DurationFieldType.MILLISECONDS);UnsupportedDurationField unsupported2 = getInstance(DurationFieldType.MILLISECONDS);
 UnsupportedDurationField unsupported3 = getInstance(DurationFieldType.SECONDS);
 assertEquals(unsupported1, unsupported1);
 assertNotEquals(unsupported1, unsupported2);
 assertNotEquals(unsupported1, unsupported3);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupported1 = getInstance(DurationFieldType.MILLISECONDS);UnsupportedDurationField unsupported2 = getInstance(DurationFieldType.MILLISECONDS);
 assertEquals(unsupported1.hashCode(), unsupported1.hashCode());
 assertNotEquals(unsupported1.hashCode(), unsupported2.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupported = getInstance(DurationFieldType.MILLISECONDS);
 assertEquals("UnsupportedDurationField[MILLISECONDS]", unsupported.toString());
 }
}