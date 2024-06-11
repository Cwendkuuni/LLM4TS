import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneOffset;
import org.junit.Test;
import org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private static final Chronology ISO_CHRONO = Chronology.ISO;
 private static final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
 private static final DateTimeField STRICT_FIELD = DateTimeFieldType.dayOfMonth();

 @Test
 public void testGetInstance() {
 DateTimeField lenientField = LenientDateTimeField.getInstance(STRICT_FIELD, ISO_CHRONO);
 assertTrue(lenientField instanceof LenientDateTimeField);
 }

 @Test
 public void testIsLenient() {
 DateTimeField lenientField = new LenientDateTimeField(STRICT_FIELD, ISO_CHRONO);
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSetInBounds() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, ISO_CHRONO);
 MutableDateTime dateTime = new MutableDateTime(2022, 12, 31, 0, 0, 0, 0, ISO_CHRONO);
 long originalValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = originalValue;
 long newValueInLocalMilliseconds = lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 assertEquals(newValue, lenientField.get(newValueInLocalMilliseconds));
 assertEquals(newValue, lenientField.get(dateTime.toLocalMilliseconds()));
 }

 @Test
 public void testSetInBoundsWithAddition() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, ISO_CHRONO);
 MutableDateTime dateTime = new MutableDateTime(2022, 12, 31, 0, 0, 0, 0, ISO_CHRONO);
 long originalValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = originalValue + 1;
 long newValueInLocalMilliseconds = lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 assertEquals(newValue, lenientField.get(newValueInLocalMilliseconds));
 assertEquals(newValue, lenientField.get(dateTime.toLocalMilliseconds()));
 }

 @Test
 public void testSetOutOfBounds() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, ISO_CHRONO);
 MutableDateTime dateTime = new MutableDateTime(2022, 12, 31, 0, 0, 0, 0, ISO_CHRONO);
 long originalValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = originalValue + 2;
 long newValueInLocalMilliseconds = lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 assertEquals(newValue, lenientField.get(newValueInLocalMilliseconds));
 // Check that the new value is actually adjusted
 MutableDateTime adjustedDateTime = new MutableDateTime(newValueInLocalMilliseconds, ISO_CHRONO);
 assertEquals(newValue + 1, lenientField.get(adjustedDateTime.toLocalMilliseconds()));
 }

 @Test
 public void testSetOutOfBoundsWithNegativeValue() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, ISO_CHRONO);
 MutableDateTime dateTime = new MutableDateTime(2022, 12, 31, 0, 0, 0, 0, ISO_CHRONO);
 long originalValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = originalValue - 2;
 long newValueInLocalMilliseconds = lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 assertEquals(newValue, lenientField.get(newValueInLocalMilliseconds));
 // Check that the new value is actually adjusted
 MutableDateTime adjustedDateTime = new MutableDateTime(newValueInLocalMilliseconds, ISO_CHRONO);
 assertEquals(newValue - 1, lenientField.get(adjustedDateTime.toLocalMilliseconds()));
 }

 @Test
 public void testSetWithInvalidField() {
 DateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.hourOfDay(), ISO_CHRONO);
 MutableDateTime dateTime = new MutableDateTime(2022, 12, 31, 0, 0, 0, 0, ISO_CHRONO);
 long originalValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = originalValue + 1;
 try {
 lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}