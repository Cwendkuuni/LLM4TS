import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.dayOfMonth();
 private static final DurationFieldType HOURS = DurationFieldType.hourOfDay();
 private static final DurationFieldType MINUTES = DurationFieldType.minuteOfHour();
 private static final DurationFieldType SECONDS = DurationFieldType.secondOfMinute();
 private static final DurationFieldType MILLIS = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(HOURS, unsupportedHours.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 assertEquals(MILLIS, unsupportedMillis.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 assertEquals(false, unsupportedDays.isSupported());
 assertEquals(false, unsupportedHours.isSupported());
 assertEquals(false, unsupportedMinutes.isSupported());
 assertEquals(false, unsupportedSeconds.isSupported());
 assertEquals(false, unsupportedMillis.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 long duration = 1;

 assertEquals(-1, unsupportedDays.getValue(duration));
 assertEquals(-1, unsupportedHours.getValue(duration));
 assertEquals(-1, unsupportedMinutes.getValue(duration));
 assertEquals(-1, unsupportedSeconds.getValue(duration));
 assertEquals(0, unsupportedMillis.getValue(duration));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 long duration = 1;

 assertEquals(-1, unsupportedDays.getValueAsLong(duration));
 assertEquals(-1, unsupportedHours.getValueAsLong(duration));
 assertEquals(-1, unsupportedMinutes.getValueAsLong(duration));
 assertEquals(0, unsupportedSeconds.getValueAsLong(duration));
 assertEquals(0, unsupportedMillis.getValueAsLong(duration));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 long duration = 1;
 long instant = 1;

 assertEquals(-1, unsupportedDays.getValue(duration, instant));
 assertEquals(-1, unsupportedHours.getValue(duration, instant));
 assertEquals(-1, unsupportedMinutes.getValue(duration, instant));
 assertEquals(0, unsupportedSeconds.getValue(duration, instant));
 assertEquals(0, unsupportedMillis.getValue(duration, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 long duration = 1;
 long instant = 1;

 assertEquals(-1, unsupportedDays.getValueAsLong(duration, instant));
 assertEquals(-1, unsupportedHours.getValueAsLong(duration, instant));
 assertEquals(-1, unsupportedMinutes.getValueAsLong(duration, instant));
 assertEquals(0, unsupportedSeconds.getValueAsLong(duration, instant));
 assertEquals(0, unsupportedMillis.getValueAsLong(duration, instant));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 int value = 1;

 assertEquals(0, unsupportedDays.getMillis(value));
 assertEquals(0, unsupportedHours.getMillis(value));
 assertEquals(0, unsupportedMinutes.getMillis(value));
 assertEquals(0, unsupportedSeconds.getMillis(value));
 assertEquals(0, unsupportedMillis.getMillis(value));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 long value = 1;
 long instant = 1;

 assertEquals(0, unsupportedDays.getMillis(value, instant));
 assertEquals(0, unsupportedHours.getMillis(value, instant));
 assertEquals(0, unsupportedMinutes.getMillis(value, instant));
 assertEquals(0, unsupportedSeconds.getMillis(value, instant));
 assertEquals(0, unsupportedMillis.getMillis(value, instant));
 }

 @Test
 public void testAdd() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 long instant = 1;
 int value = 1;

 assertThrows(UnsupportedOperationException.class, () -> unsupportedDays.add(instant, value));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedHours.add(instant, value));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMinutes.add(instant, value));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedSeconds.add(instant, value));
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillis.add(instant, value));
 }

 @Test
 public void testGetDifference() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 long minuendInstant = 1;
 long subtrahendInstant = 2;

 assertEquals(0, unsupportedDays.getDifference(minuendInstant, subtrahendInstant));
 assertEquals(0, unsupportedHours.getDifference(minuendInstant, subtrahendInstant));
 assertEquals(0, unsupportedMinutes.getDifference(minuendInstant, subtrahendInstant));
 assertEquals(0, unsupportedSeconds.getDifference(minuendInstant, subtrahendInstant));
 assertEquals(0, unsupportedMillis.getDifference(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 long minuendInstant = 1;
 long subtrahendInstant = 2;

 assertEquals(0, unsupportedDays.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 assertEquals(0, unsupportedHours.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 assertEquals(0, unsupportedMinutes.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 assertEquals(0, unsupportedSeconds.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 assertEquals(0, unsupportedMillis.getDifferenceAsLong(minuendInstant, subtrahendInstant));
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 assertEquals(0, unsupportedDays.getUnitMillis());
 assertEquals(0, unsupportedHours.getUnitMillis());
 assertEquals(0, unsupportedMinutes.getUnitMillis());
 assertEquals(0, unsupportedSeconds.getUnitMillis());
 assertEquals(0, unsupportedMillis.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLIS);

 assertEquals(0, unsupportedDays.compareTo(unsupportedDays));
 assertEquals(0, unsupportedHours.compareTo(unsupportedHours));
 assertEquals(0, unsupportedMinutes.compareTo(unsupportedMinutes));
 assertEquals(0, unsupportedSeconds.compareTo(unsupportedSeconds));
 assertEquals(0, unsupportedMillis.compareTo(unsupportedMillis));
 }

}