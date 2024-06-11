import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

 private static final Chronology ISO_CHRONO = org.joda.time.chrono.ISOChronology.getInstance();
 private static final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
 private static final DateTimeFieldType HOUR_OF_DAY = DateTimeFieldType.hourOfDay();
 private static final DateTimeFieldType MINUTE_OF_HOUR = DateTimeFieldType.minuteOfHour();
 private static final DateTimeFieldType SECOND_OF_MINUTE = DateTimeFieldType.secondOfMinute();

 @Test
 public void testGetInstance() {
 DateTimeField field = new LocalDateTimeField(HOUR_OF_DAY, ISO_CHRONO);
 DateTimeField lenientField = LenientDateTimeField.getInstance(field, ISO_CHRONO);
 assertSame(lenientField, LenientDateTimeField.getInstance(field, ISO_CHRONO));
 assertFalse(lenientField.isLenient());

 field = new StrictLenientDateTimeField(DAY_OF_MONTH, ISO_CHRONO);
 lenientField = LenientDateTimeField.getInstance(field, ISO_CHRONO);
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSet_ValidValues() {
 LenientDateTimeField lenientField = new LenientDateTimeField(SECOND_OF_MINUTE, ISO_CHRONO);
 BaseDateTime dt = new BaseDateTime(0, ISO_CHRONO);
 long instant = dt.getMillis();

 int value = get(instant, lenientField);
 long result = lenientField.set(instant, value);
 assertEquals(instant, result);

 value += lenientField.getRange();
 result = lenientField.set(instant, value);
 assertEquals(instant + lenientField.getRange(), result);
 }

 @Test
 public void testSet_OutOfBoundsValues() {
 LenientDateTimeField lenientField = new LenientDateTimeField(SECOND_OF_MINUTE, ISO_CHRONO);
 BaseDateTime dt = new BaseDateTime(0, ISO_CHRONO);
 long instant = dt.getMillis();

 int value = get(instant, lenientField) - lenientField.getRange();
 long result = lenientField.set(instant, value);
 assertEquals(instant, result);

 value -= lenientField.getRange() * 2;
 result = lenientField.set(instant, value);
 assertEquals(instant - (2 * lenientField.getRange()), result);
 }

 @Test
 public void testSet_MixedOutOfBoundsValues() {
 LenientDateTimeField lenientField = new LenientDateTimeField(SECOND_OF_MINUTE, ISO_CHRONO);
 BaseDateTime dt = new BaseDateTime(0, ISO_CHRONO);
 long instant = dt.getMillis();

 int value = get(instant, lenientField) - lenientField.getRange() * 2;
 long result = lenientField.set(instant, value);
 assertEquals(instant - (2 * lenientField.getRange()), result);

 value += lenientField.getRange();
 result = lenientField.set(instant, value);
 assertEquals(instant, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_NullInstant() {
 LenientDateTimeField lenientField = new LenientDateTimeField(SECOND_OF_MINUTE, ISO_CHRONO);
 lenientField.set(null, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_NullValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(SECOND_OF_MINUTE, ISO_CHRONO);
 lenientField.set(0, null);
 }

 @Test
 public void testIsLenient() {
 LenientDateTimeField lenientField = new LenientDateTimeField(SECOND_OF_MINUTE, ISO_CHRONO);
 assertTrue(lenientField.isLenient());
 }

 private int get(long instant, DateTimeField field) {
 return (int) FieldUtils.safeGetValue(field, new BaseDateTime(instant, field.getChronology()));
 }
}