import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 private static UnsupportedDurationField getUnsupportedDurationField(DurationFieldType type) {
 return UnsupportedDurationField.getInstance(type);
 }

 @Test
 public void testGetInstance() {
 DurationFieldType type = DurationFieldType.DAYS;
 UnsupportedDurationField unsupported = getUnsupportedDurationField(type);
 UnsupportedDurationField another = getUnsupportedDurationField(type);
 assertEquals(unsupported, another);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 assertFalse(unsupported.isSupported());
 }

 @Test
 public void testGetName() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 assertEquals("days", unsupported.getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long duration = 1;
 unsupported.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long duration = 1;
 unsupported.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAt() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long duration = 1;
 long instant = 0;
 unsupported.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAt() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long duration = 1;
 long instant = 0;
 unsupported.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 int value = 1;
 unsupported.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLong() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long value = 1;
 unsupported.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisIntLong() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 int value = 1;
 long instant = 0;
 unsupported.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLongLong() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long value = 1;
 long instant = 0;
 unsupported.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long instant = 0;
 long value = 1;
 unsupported.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long instant = 0;
 long value = 1;
 unsupported.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long minuendInstant = 0;
 long subtrahendInstant = 1;
 unsupported.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long minuendInstant = 0;
 long subtrahendInstant = 1;
 unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 long millis = unsupported.getUnitMillis();
 assertEquals(0, millis);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 DurationField supported = DurationField.standardDays(1);
 int result = unsupported.compareTo(supported);
 assertEquals(1, result);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupported1 = getUnsupportedDurationField(DurationFieldType.DAYS);
 UnsupportedDurationField unsupported2 = getUnsupportedDurationField(DurationFieldType.DAYS);
 UnsupportedDurationField unsupported3 = getUnsupportedDurationField(DurationFieldType.MINUTES);
 Object obj = new Object();
 assertTrue(unsupported1.equals(unsupported1));
 assertTrue(unsupported1.equals(unsupported2));
 assertFalse(unsupported1.equals(unsupported3));
 assertFalse(unsupported1.equals(obj));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 int hashCode = unsupported.hashCode();
 assertEquals("days", unsupported.getName()); // for test consistency
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 String toString = unsupported.toString();
 assertEquals("UnsupportedDurationField[days]", toString);
 }

 @Test
 public void testReadResolve() {
 UnsupportedDurationField unsupported = getUnsupportedDurationField(DurationFieldType.DAYS);
 Object readResolve = unsupported.readResolve();
 assertEquals(unsupported.getName(), readResolve.getName());
 }

}