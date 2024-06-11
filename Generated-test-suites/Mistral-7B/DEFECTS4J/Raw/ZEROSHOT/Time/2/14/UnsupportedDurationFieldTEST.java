import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.dayOfMonth();
 private static final DurationFieldType HOURS = DurationFieldType.hourOfDay();
 private static final DurationFieldType MINUTES = DurationFieldType.minute();
 private static final DurationFieldType SECONDS = DurationFieldType.second();
 private static final DurationFieldType MILLIS = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 DurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 DurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 DurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 DurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(HOURS, unsupportedHours.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 assertEquals(MILLIS, unsupportedMillis.getType());
 }

 @Test
 public void testIsSupported() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 DurationField supportedMillis = new ChronoDurationField(MILLIS, 1);

 assertEquals(false, unsupportedDays.isSupported());
 assertEquals(true, supportedMillis.isSupported());
 }

 @Test
 public void testGetValue() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getValue(0));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getValueAsLong(0));
 }

 @Test
 public void testGetValueWithInstant() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getValue(0, 0));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getValueAsLong(0, 0));
 }

 @Test
 public void testGetMillis() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getMillis(0));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getMillis(0L));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getMillis(0, 0));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getMillis(0L, 0));
 }

 @Test
 public void testAdd() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.add(0, 1));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.add(0L, 1L));
 }

 @Test
 public void testGetDifference() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getDifference(0, 0));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.getDifferenceAsLong(0, 0));
 }

 @Test
 public void testGetUnitMillis() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 assertEquals(0, unsupportedDays.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 DurationField supportedMillis = new ChronoDurationField(MILLIS, 1);

 assertEquals(0, unsupportedDays.compareTo(supportedMillis));
 }

 @Test
 public void testEquals() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 DurationField unsupportedDays2 = UnsupportedDurationField.getInstance(DAYS);
 DurationField supportedMillis = new ChronoDurationField(MILLIS, 1);

 assertEquals(unsupportedDays, unsupportedDays2);
 assertNotEquals(unsupportedDays, supportedMillis);
 }

 @Test
 public void testHashCode() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 DurationField unsupportedDays2 = UnsupportedDurationField.getInstance(DAYS);
 DurationField supportedMillis = new ChronoDurationField(MILLIS, 1);

 assertEquals(unsupportedDays.hashCode(), unsupportedDays2.hashCode());
 assertNotEquals(unsupportedDays.hashCode(), supportedMillis.hashCode());
 }

 @Test
 public void testToString() {
 DurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 assertEquals("UnsupportedDurationField[dayOfMonth]", unsupportedDays.toString());
 }
}