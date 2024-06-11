import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time. MutableDateTime;
import org.joda.time.DateTimeField;
import static org.junit.Assert.*;

import org.junit.Test;

public class LenientDateTimeFieldTest {

 private static final Chronology ISO_CHRONO = Chronology.ISO;
 private DateTimeField lenientField;
 private DateTimeField strictField;

 @Before
 public void setUp() {
 lenientField = LenientDateTimeField.getInstance(DateTimeFieldType.dayOfYear(), ISO_CHRONO);
 strictField = new DayOfYearField(1, ISO_CHRONO);
 }

 @Test
 public void testGetInstanceWithNullField() {
 assertNull(LenientDateTimeField.getInstance(null, ISO_CHRONO));
 }

 @Test
 public void testGetInstanceWithNullBase() {
 assertNull(LenientDateTimeField.getInstance(strictField, null));
 }

 @Test
 public void testGetInstanceWithLenientField() {
DateTimeField lenientLenientField = LenientDateTimeField.getInstance(lenientField, ISO_CHRONO);
assertTrue(lenientLenientField instanceof LenientDateTimeField);
assertEquals(lenientField, lenientLenientField.getDelegate());
 }

 @Test
 public void testGetInstanceWithStrictField() {
 DateTimeField lenientStrictField = LenientDateTimeField.getInstance(strictField, ISO_CHRONO);
 assertFalse(lenientStrictField instanceof LenientDateTimeField);
 assertEquals(strictField, lenientStrictField.getDelegate());
 }

 @Test
 public void testIsLenient() {
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSetWithValidValue() {
 MutableDateTime dateTime = new MutableDateTime(1234567890000L, ISO_CHRONO);
 long newValue = 15;
 long expectedResult = dateTime.getLocalMillis() + FieldUtils.safeSubtract(newValue, lenientField.get(dateTime.toLocalDate()));
 long result = lenientField.set(dateTime.getLocalMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetWithValueGreaterThanMaximum() {
 MutableDateTime dateTime = new MutableDateTime(1234567890000L, ISO_CHRONO);
 long newValue = 366;
 long expectedResult = dateTime.getLocalMillis() + FieldUtils.safeSubtract(newValue, lenientField.get(dateTime.toLocalDate())) + 24 * 60 * 60 * 1000;
 long result = lenientField.set(dateTime.getLocalMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetWithValueLessThanMinimum() {
 MutableDateTime dateTime = new MutableDateTime(1234567890000L, ISO_CHRONO);
 long newValue = -366;
 long expectedResult = dateTime.getLocalMillis() + FieldUtils.safeSubtract(newValue, lenientField.get(dateTime.toLocalDate())) - 24 * 60 * 60 * 1000;
 long result = lenientField.set(dateTime.getLocalMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithNullInstant() {
 lenientField.set(null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetWithNullValue() {
 lenientField.set(1234567890000L, null);
 }

 @Test
 public void testSetWithValidValueInUTC() {
 BaseDateTime utcDateTime = new BaseDateTime(1234567890000L, ISO_CHRONO);
 long newValue = 15;
 long expectedResult = utcDateTime.getMillis() + FieldUtils.safeSubtract(newValue, lenientField.get(utcDateTime.toLocalDate()));
 long result = lenientField.set(utcDateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetWithValueGreaterThanMaximumInUTC() {
 BaseDateTime utcDateTime = new BaseDateTime(1234567890000L, ISO_CHRONO);
 long newValue = 366;
 long expectedResult = utcDateTime.getMillis() + FieldUtils.safeSubtract(newValue, lenientField.get(utcDateTime.toLocalDate())) + 24 * 60 * 60 * 1000;
 long result = lenientField.set(utcDateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetWithValueLessThanMinimumInUTC() {
 BaseDateTime utcDateTime = new BaseDateTime(1234567890000L, ISO_CHRONO);
 long newValue = -366;
 long expectedResult = utcDateTime.getMillis() + FieldUtils.safeSubtract(newValue, lenientField.get(utcDateTime.toLocalDate())) - 24 * 60 * 60 * 1000;
 long result = lenientField.set(utcDateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }
}