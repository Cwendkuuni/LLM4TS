import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

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
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 assertFalse(unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 1000;
 throw unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 1000;
 throw unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 1000;
 long instant = 1234567890L;
 throw unsupportedField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 1000;
 long instant = 1234567890L;
 throw unsupportedField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_int() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 int value = 10;
 throw unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long value = 1000;
 throw unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_int_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 int value = 10;
 long instant = 1234567890L;
 throw unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long value = 1000;
 long instant = 1234567890L;
 throw unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_int() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long instant = 1234567890L;
 int value = 10;
 throw unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long instant = 1234567890L;
 long value = 1000;
 throw unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1234567880L;
 throw unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1234567880L;
 throw unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DAYS);
 int comparisonResult = unsupportedField1.compareTo(unsupportedField2);
 assertEquals(0, comparisonResult);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 int hashCode = unsupportedField.hashCode();
 int expectedHashCode = DAYS.hashCode();
 assertEquals(expectedHashCode, hashCode);
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 String toString = unsupportedField.toString();
 String expectedToString = "UnsupportedDurationField[days]";
 assertEquals(expectedToString, toString);
 }

}