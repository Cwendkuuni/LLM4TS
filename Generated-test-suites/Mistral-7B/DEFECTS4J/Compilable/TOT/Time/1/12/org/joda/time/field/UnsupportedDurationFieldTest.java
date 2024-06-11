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
 public void testGetValue() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 long duration = 1234L;
 try {
 unsupported.getValue(duration);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 long duration = 1234L;
 try {
 unsupported.getValueAsLong(duration);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 long duration = 1234L;
 long instant = 1234567890123L;
 try {
 unsupported.getValue(duration, instant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 long duration = 1234L;
 long instant = 1234567890123L;
 try {
 unsupported.getValueAsLong(duration, instant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 int value = 123;
 try {
 unsupported.getMillis(value);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValue() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 long value = 1234567L;
 try {
 unsupported.getMillis(value);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValueAndInstant() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 long value = 1234567L;
 long instant = 1234567890123L;
 try {
 unsupported.getMillis(value, instant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 long instant = 1234567890123L;
 int value = 123;
 try {
 unsupported.add(instant, value);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLongValue() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 long instant = 1234567890123L;
 long value = 1234567L;
 try {
 unsupported.add(instant, value);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1234567890124L;
 try {
 unsupported.getDifference(minuendInstant, subtrahendInstant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 long minuendInstant = 1234567890123L;
 long subtrahendInstant = 1234567890124L;
 try {
 unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DAYS);
 int result = unsupported1.compareTo(unsupported2);
 assertEquals(-1, result);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(MINUTES);
 assertEquals(unsupported1.hashCode(), unsupported2.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(MINUTES);
 assertEquals("UnsupportedDurationField[minutes]", unsupported.toString());
 }
}