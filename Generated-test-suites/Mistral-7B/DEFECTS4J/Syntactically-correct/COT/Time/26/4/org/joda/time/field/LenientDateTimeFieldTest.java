import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private static final Chronology DATE_TIME_CHRONO = ZoneUtils.UTC;
 private static final DateTimeField DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();

 @Test
 public void testGetInstance() {
 DateTimeField field = DAY_OF_MONTH;
 DateTimeField lenientField = LenientDateTimeField.getInstance(field, DATE_TIME_CHRONO);
 assertNotNull(lenientField);
 assertEquals(field, lenientField);
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testConstructors() {
 DateTimeField field = DAY_OF_MONTH;
 Chronology base = DATE_TIME_CHRONO;
 LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
 assertNotNull(lenientField);
 assertEquals(field, lenientField.getDelegate());
 assertEquals(base, lenientField.iBase);
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testIsLenient() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 assertTrue(lenientField.isLenient());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullInstant() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 lenientField.set(null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 0, 0, 0, 0).getMillis();
 lenientField.set(instant, -1);
 }

 @Test
 public void testSetOutOfBoundsValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 0, 0, 0, 0).getMillis();
 long newValue = 32;
 long result = lenientField.set(instant, newValue);
 assertEquals(instant + (newValue - get(instant)), result);
 }

 @Test
 public void testSetWithinBoundsValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 0, 0, 0, 0).getMillis();
 long newValue = get(instant);
 long result = lenientField.set(instant, newValue);
 assertEquals(instant, result);
 }

 @Test
 public void testSetValueInPast() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 0, 0, 0, 0).getMillis();
 long newValue = 0;
 long result = lenientField.set(instant, newValue);
 assertEquals(instant, result);
 }

 @Test
 public void testSetValueInFuture() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 0, 0, 0, 0).getMillis();
 long newValue = LocalDate.of(2023, 1, 32).toDateTimeAtStartOfDay().getMillis();
 long result = lenientField.set(instant, newValue);
 assertEquals(instant + (newValue - get(instant)), result);
 }

 @Test
 public void testSetValueInSameDay() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 0, 0, 0, 0).getMillis();
 long newValue = get(instant);
 long result = lenientField.set(instant, newValue);
 assertEquals(instant, result);
 }

 @Test
 public void testSetValueInSameSecond() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 12, 30, 0, 0).getMillis();
 long newValue = get(instant) + 10;
 long result = lenientField.set(instant, newValue);
 assertEquals(instant + (newValue - get(instant)), result);
 }

 @Test
 public void testSetValueInSameMillisecond() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 12, 30, 59, 999).getMillis();
 long newValue = get(instant);
 long result = lenientField.set(instant, newValue);
 assertEquals(instant, result);
 }

 @Test
 public void testSetValueInLastSecondOfDay() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 12, 59, 59, 0).getMillis();
 long newValue = get(instant) + 1;
 long result = lenientField.set(instant, newValue);
 assertEquals(instant + ((newValue - get(instant)) * 1000 + 1), result);
 }

 @Test
 public void testSetValueWithinRange() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 12, 30, 0, 0).getMillis();
 long newValue = get(instant) + 15;
 long result = lenientField.set(instant, newValue);
 assertEquals(instant + (newValue - get(instant)), result);
 }

 @Test
 public void testSetValueOutOfRangeWithinADay() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 12, 30, 0, 0).getMillis();
 long newValue = get(instant) + 32;
 long result = lenientField.set(instant, newValue);
 assertEquals(instant + (newValue - get(instant)), result);
 }

 @Test
 public void testSetValueAtMidnightOfNextDay() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 0, 0, 0, 0).getMillis();
 long newValue = get(instant) + 1;
 long result = lenientField.set(instant, newValue);
 assertEquals(instant + ((newValue - get(instant)) * 24 * 60 * 60 * 1000 + 1), result);
 }

 @Test
 public void testSetValueAtMidnightOfNextMonth() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 0, 0, 0, 0).getMillis();
 long newValue = get(instant) + 32 * 28 * 24 * 60 * 60 * 1000;
 long result = lenientField.set(instant, newValue);
 assertEquals(newValue, result);
 }

 @Test
 public void testSetValueAtMidnightOfNextYear() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, DATE_TIME_CHRONO);
 long instant = new MutableDateTime(2023, 1, 1, 0, 0, 0, 0).getMillis();
 long newValue = get(instant) + 365 * 24 * 60 * 60 * 1000;
 long result = lenientField.set(instant, newValue);
 assertEquals(newValue, result);
 }
}