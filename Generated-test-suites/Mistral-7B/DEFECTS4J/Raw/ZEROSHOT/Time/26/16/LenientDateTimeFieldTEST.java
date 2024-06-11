import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneOffset;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private static final Chronology ISO_8601_UTC_CHRONOLOGY = Chronology.UTC;

 @Test
 public void testLenientDateTimeFieldInstance() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, ISO_8601_UTC_CHRONOLOGY);
 assertTrue(lenientField instanceof LenientDateTimeField);
 assertEquals(lenientField.getBaseField(), field);
 assertEquals(lenientField.getChronology(), ISO_8601_UTC_CHRONOLOGY);
 }

 @Test
 public void testLenientDateTimeFieldIsLenient() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSetLenientDateTimeField() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 MutableDateTime dateTime = new MutableDateTime();
 long instant = dateTime.getMilliseconds();
 int value = 365;
 long expectedResult = instant + (24L * 60L * 60L * 365L); // 1 year

 long result = lenientField.set(instant, value);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetLenientDateTimeFieldNegativeValue() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 MutableDateTime dateTime = new MutableDateTime();
 long instant = dateTime.getMilliseconds();
 int value = -30;

 long result = lenientField.set(instant, value);
 long expectedResult = instant - 30;

 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetLenientDateTimeFieldMaxValue() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 MutableDateTime dateTime = new MutableDateTime();
 long instant = dateTime.getMilliseconds();
 int value = 31; // maximum value for day of month

 long result = lenientField.set(instant, value);
 long expectedResult = FieldUtils.safeAdd(instant, 24 * 60 * 60 * 30 * 1000); // 1 month

 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetLenientDateTimeFieldMaxValueWithLimit() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 MutableDateTime dateTime = new MutableDateTime();
 long instant = dateTime.getMilliseconds();
 int value = 32; // value exceeding the limit

 long result = lenientField.set(instant, value);
 long expectedResult = instant;

 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetLenientDateTimeFieldMinValue() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 MutableDateTime dateTime = new MutableDateTime();
 long instant = dateTime.getMilliseconds();
 int value = 0;

 long result = lenientField.set(instant, value);
 long expectedResult = instant;

 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetLenientDateTimeFieldMinValueNegative() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 MutableDateTime dateTime = new MutableDateTime();
 long instant = dateTime.getMilliseconds();
 int value = -31; // value below the minimum

 long result = lenientField.set(instant, value);
 long expectedResult = instant;

 assertEquals(expectedResult, result);
 }

 @Test
 public void testLenientDateTimeFieldGetValue() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 MutableDateTime dateTime = new MutableDateTime();
 dateTime.set(2022, 1, 1, 0, 0, 0, 0, ISO_8601_UTC_CHRONOLOGY);
 long instant = dateTime.getMilliseconds();
 int expectedValue = 1;

 int result = lenientField.get(instant);
 assertEquals(expectedValue, result);
 }

 @Test
 public void testLenientDateTimeFieldGetValueOutOfBounds() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 MutableDateTime dateTime = new MutableDateTime();
 dateTime.set(2022, 1, 1, 0, 0, 0, 0, ISO_8601_UTC_CHRONOLOGY);
 long instant = dateTime.getMilliseconds();
 int value = 32; // value exceeding the limit

 long result = lenientField.set(instant, value);
 int expectedValue = 1; // value is adjusted to the next valid one

 int actualValue = lenientField.get(result);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testLenientDateTimeFieldGetValueNegative() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 MutableDateTime dateTime = new MutableDateTime();
 dateTime.set(2022, 1, 1, 0, 0, 0, 0, ISO_8601_UTC_CHRONOLOGY);
 long instant = dateTime.getMilliseconds();
 int value = -31; // value below the minimum

 long result = lenientField.set(instant, value);
 int expectedValue = 31; // value is adjusted to the previous valid one

 int actualValue = lenientField.get(result);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testLenientDateTimeFieldGetValueInBetween() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 MutableDateTime dateTime = new MutableDateTime();
 dateTime.set(2022, 1, 15, 0, 0, 0, 0, ISO_8601_UTC_CHRONOLOGY);
 long instant = dateTime.getMilliseconds();
 int value = 13;

 long result = lenientField.set(instant, value);
 int expectedValue = 13;

 int actualValue = lenientField.get(result);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testLenientDateTimeFieldValueVerifier() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 LocalDateTime dateTime = new LocalDateTime();
 LocalDateTimeFieldVerifier verifier = new LocalDateTimeFieldVerifier(lenientField, dateTime);

 dateTime.setField(LenientDateTimeField.class, 15);
 verifier.assertValidValue(15);

 dateTime.setField(LenientDateTimeField.class, 32);
 verifier.assertValidValue(16);

 dateTime.setField(LenientDateTimeField.class, -31);
 verifier.assertValidValue(31);
 }

 @Test
 public void testLenientDateTimeFieldValueVerifierOutOfRange() {
 DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), ISO_8601_UTC_CHRONOLOGY);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ISO_8601_UTC_CHRONOLOGY);

 LocalDateTime dateTime = new LocalDateTime();
 LocalDateTimeFieldVerifier verifier = new LocalDateTimeFieldVerifier(lenientField, dateTime);

 dateTime.setField(LenientDateTimeField.class, Integer.MIN_VALUE);
 try {
 verifier.assertValidValue(Integer.MIN_VALUE);
 fail();
 } catch (AssertionError e) {
 // expected
 }

 dateTime.setField(LenientDateTimeField.class, Integer.MAX_VALUE);
 try {
 verifier.assertValidValue(Integer.MAX_VALUE);
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }
}