package org.joda.time.field;
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
 public void testIsSupportedThrowsException() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 assertFalse(unsupportedDays.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNameReturnsCorrectName() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 assertEquals("DAYS", unsupportedDays.getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueThrowsException() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1L;
 unsupportedDays.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongThrowsException() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1L;
 unsupportedDays.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueThrowsExceptionWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1L;
 long instant = 1L;
 unsupportedDays.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongThrowsExceptionWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long duration = 1L;
 long instant = 1L;
 unsupportedDays.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisThrowsException() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 int value = 1;
 unsupportedDays.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisThrowsExceptionWithLongValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long value = 1L;
 unsupportedDays.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisThrowsExceptionWithValueAndInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 int value = 1;
 long instant = 1L;
 unsupportedDays.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisThrowsExceptionWithValueAndInstantLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long value = 1L;
 long instant = 1L;
 unsupportedDays.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddThrowsException() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long instant = 1L;
 int value = 1;
 unsupportedDays.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddThrowsExceptionWithLongValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long instant = 1L;
 long value = 1L;
 unsupportedDays.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceThrowsException() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 1L;
 long subtrahendInstant = 2L;
 unsupportedDays.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLongThrowsException() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 long minuendInstant = 1L;
 long subtrahendInstant = 2L;
 unsupportedDays.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillisReturnsZero() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(0, unsupportedDays.getUnitMillis());
 }

 @Test
 public void testCompareToReturnsZero() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 assertEquals(0, unsupportedDays.compareTo(unsupportedHours));
 }

 @Test
 public void testHashCodeReturnsNameHashCode() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(DAYS.getName().hashCode(), unsupportedDays.hashCode());
 }

 @Test
 public void testToStringReturnsCorrectString() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 assertEquals("UnsupportedDurationField[DAYS]", unsupportedDays.toString());
 }

}