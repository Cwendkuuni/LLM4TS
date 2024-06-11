import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;

public class UnsupportedDurationFieldTest extends BaseTestCase {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(HOURS, unsupportedHours.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 assertFalse(unsupported.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 long duration = 123456L;
 throwunexpected(unsupported.getValue(duration));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 long duration = 123456L;
 throwunexpected(unsupported.getValueAsLong(duration));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 long duration = 123456L;
 long instant = 123456789L;
 throwunexpected(unsupported.getValue(duration, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 long duration = 123456L;
 long instant = 123456789L;
 throwunexpected(unsupported.getValueAsLong(duration, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 int value = 123;
 throwunexpected(unsupported.getMillis(value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAsLong() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 long value = 123456L;
 throwunexpected(unsupported.getMillis(value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 int value = 123;
 long instant = 123456789L;
 throwunexpected(unsupported.getMillis(value, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAsLongAtInstant() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 long value = 123456L;
 long instant = 123456789L;
 throwunexpected(unsupported.getMillis(value, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 long instant = 123456789L;
 long value = 123456L;
 throwunexpected(unsupported.add(instant, value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 long instant = 123456789L;
 long value = 123456L;
 throwunexpected(unsupported.add(instant, value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 long minuendInstant = 123456789L;
 long subtrahendInstant = 123456L;
 throwunexpected(unsupported.getDifference(minuendInstant, subtrahendInstant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 long minuendInstant = 123456789L;
 long subtrahendInstant = 123456L;
 throwunexpected(unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 assertEquals(0, unsupported.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 DurationField durationField = DurationField.standardHours(1);
 assertEquals(0, unsupported.compareTo(durationField));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupported3 = new UnsupportedDurationField(HOURS);

 assertTrue(unsupported1.equals(unsupported1));
 assertTrue(unsupported1.equals(unsupported2));
 assertFalse(unsupported1.equals(unsupported3));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 assertEquals(DAYS.getName().hashCode(), unsupported.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 assertEquals("UnsupportedDurationField[days]", unsupported.toString());
 }

 @Test
 public void testReadResolve() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 assertSame(UnsupportedDurationField.getInstance(DAYS), unsupported.readResolve());
 }

 private void throwunexpected(Object obj) {
 fail("Expected UnsupportedOperationException, but got: " + obj.getClass().getName());
 }

}