import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

public class UnsupportedDurationFieldTest extends BaseTestCase {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();
 private static final DurationFieldType MILLISECONDS = DurationFieldType.mills();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMilliseconds = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(HOURS, unsupportedHours.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 assertEquals(MILLISECONDS, unsupportedMilliseconds.getType());
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 assertFalse(unsupported.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 unsupported.getValue(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 unsupported.getValueAsLong(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue_long() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 unsupported.getValue(1L, 2L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong_long() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 unsupported.getValueAsLong(1L, 2L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(MILLISECONDS);
 unsupported.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(MILLISECONDS);
 unsupported.getMillis(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_int_long() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(MILLISECONDS);
 unsupported.getMillis(1, 2L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long_long() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(MILLISECONDS);
 unsupported.getMillis(1L, 2L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 unsupported.add(1L, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_long() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 unsupported.add(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 unsupported.getDifference(1L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 unsupported.getDifferenceAsLong(1L, 1L);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(MILLISECONDS);
 assertEquals(0, unsupported.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupported2 = new UnsupportedDurationField(HOURS);
 assertTrue(unsupported1.compareTo(unsupported2) > 0);
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
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedHours = new UnsupportedDurationField(HOURS);
 int hashCode1 = unsupportedDays.hashCode();
 int hashCode2 = unsupportedDays.hashCode();
 int hashCode3 = unsupportedHours.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(DAYS);
 assertEquals("UnsupportedDurationField[days]", unsupported.toString());
 }
}