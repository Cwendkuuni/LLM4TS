import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();
 private static final DurationFieldType MILLISECONDS = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMillis = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(HOURS, unsupportedHours.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 assertEquals(MILLISECONDS, unsupportedMillis.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 5L;
 int value = unsupportedDays.getValue(duration);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 5L;
 long value = unsupportedDays.getValueAsLong(duration);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 5L;
 long instant = 1234L;
 int value = unsupportedDays.getValue(duration, instant);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 5L;
 long instant = 1234L;
 long value = unsupportedDays.getValueAsLong(duration, instant);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 int value = unsupportedDays.getMillis(5);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long value = unsupportedDays.getMillis(5L);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 int value = unsupportedDays.getMillis(5, 1234L);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndLongInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long value = unsupportedDays.getMillis(5L, 1234L);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long instant = 5L;
 long value = unsupportedDays.add(instant, 1);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLongValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long instant = 5L;
 long value = unsupportedDays.add(instant, 1L);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 5L;
 long subtrahendInstant = 2L;
 int difference = unsupportedDays.getDifference(minuendInstant, subtrahendInstant);
 fail("Should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 5L;
 long subtrahendInstant = 2L;
 long difference = unsupportedDays.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 fail("Should throw UnsupportedOperationException");
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long unitMillis = unsupportedDays.getUnitMillis();
 assertEquals(0, unitMillis);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 int compareResult = unsupportedDays.compareTo(unsupportedHours);
 assertEquals(1, compareResult);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedDays1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedDays2 = UnsupportedDurationField.getInstance(DAYS);
 assertTrue(unsupportedDays1.equals(unsupportedDays2));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedDays1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedDays2 = UnsupportedDurationField.getInstance(DAYS);
 int hashCode1 = unsupportedDays1.hashCode();
 int hashCode2 = unsupportedDays2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 String toString = unsupportedDays.toString();
 assertTrue(toString.contains("UnsupportedDurationField"));
 }
}