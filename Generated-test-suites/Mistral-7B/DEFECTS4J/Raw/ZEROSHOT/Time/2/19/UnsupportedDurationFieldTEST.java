import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

public class UnsupportedDurationFieldTest extends BaseTestCase {

 private static final DurationFieldType DAYS = DurationFieldType.dayOfYear();
 private static final DurationFieldType MINUTES = DurationFieldType.minute();
 private static final DurationFieldType SECONDS = DurationFieldType.second();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 assertFalse(unsupportedDays.isSupported());
 assertFalse(unsupportedMinutes.isSupported());
 assertFalse(unsupportedSeconds.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 long duration = 10;
 unsupportedDays.getValue(duration);
 unsupportedMinutes.getValue(duration);
 unsupportedSeconds.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 long duration = 10;
 unsupportedDays.getValueAsLong(duration);
 unsupportedMinutes.getValueAsLong(duration);
 unsupportedSeconds.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 long duration = 10;
 long instant = 1234567890L;
 unsupportedDays.getValue(duration, instant);
 unsupportedMinutes.getValue(duration, instant);
 unsupportedSeconds.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 long duration = 10;
 long instant = 1234567890L;
 unsupportedDays.getValueAsLong(duration, instant);
 unsupportedMinutes.getValueAsLong(duration, instant);
 unsupportedSeconds.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 int value = 10;
 long result = unsupportedDays.getMillis(value);
 result = unsupportedMinutes.getMillis(value);
 result = unsupportedSeconds.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithInstant() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 long value = 10;
 long instant = 1234567890L;
 long result = unsupportedDays.getMillis(value, instant);
 result = unsupportedMinutes.getMillis(value, instant);
 result = unsupportedSeconds.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 long instant = 1234567890L;
 long value = 10;
 long result = unsupportedDays.add(instant, value);
 result = unsupportedMinutes.add(instant, value);
 result = unsupportedSeconds.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithInstant() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 long instant = 1234567890L;
 long value = 10;
 long result = unsupportedDays.add(instant, value);
 result = unsupportedMinutes.add(instant, value);
 result = unsupportedSeconds.add(instant, value);
 }

 @Test
 public void testGetDifference() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1234567880L;
 int result = unsupportedDays.getDifference(minuendInstant, subtrahendInstant);
 result = unsupportedMinutes.getDifference(minuendInstant, subtrahendInstant);
 result = unsupportedSeconds.getDifference(minuendInstant, subtrahendInstant);

 assertEquals(0, result);
 }

 @Test
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1234567880L;
 long result = unsupportedDays.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 result = unsupportedMinutes.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 result = unsupportedSeconds.getDifferenceAsLong(minuendInstant, subtrahendInstant);

 assertEquals(0, result);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 long result = unsupportedDays.getUnitMillis();
 result = unsupportedMinutes.getUnitMillis();
 result = unsupportedSeconds.getUnitMillis();

 assertEquals(0, result);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedDays1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedDays2 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 int result = unsupportedDays1.compareTo(unsupportedDays2);
 result = unsupportedDays1.compareTo(unsupportedMinutes);
 result = unsupportedDays1.compareTo(unsupportedSeconds);
 result = unsupportedMinutes.compareTo(unsupportedDays1);
 result = unsupportedMinutes.compareTo(unsupportedMinutes);
 result = unsupportedMinutes.compareTo(unsupportedSeconds);
 result = unsupportedSeconds.compareTo(unsupportedDays1);
 result = unsupportedSeconds.compareTo(unsupportedMinutes);
 result = unsupportedSeconds.compareTo(unsupportedSeconds);

 assertEquals(0, result);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedDays1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedDays2 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 assertTrue(unsupportedDays1.equals(unsupportedDays1));
 assertTrue(unsupportedDays1.equals(unsupportedDays2));
 assertFalse(unsupportedDays1.equals(unsupportedMinutes));
 assertFalse(unsupportedDays1.equals(unsupportedSeconds));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 int hashCodeDays = unsupportedDays.hashCode();
 int hashCodeMinutes = unsupportedMinutes.hashCode();
 int hashCodeSeconds = unsupportedSeconds.hashCode();

 assertEquals(hashCodeDays, hashCodeMinutes);
 assertNotSame(hashCodeDays, hashCodeSeconds);
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedMinutes = new UnsupportedDurationField(MINUTES);
 UnsupportedDurationField unsupportedSeconds = new UnsupportedDurationField(SECONDS);

 String result = unsupportedDays.toString();
 result = unsupportedMinutes.toString();
 result = unsupportedSeconds.toString();

 assertEquals("UnsupportedDurationField[dayOfYear]", result);
 assertEquals("UnsupportedDurationField[minute]", result);
 assertEquals("UnsupportedDurationField[second]", result);
 }
}