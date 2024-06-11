import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private static final Chronology ISO_UTC = DateTimeConstants.ISO_UTC;

 @Test
 public void testGetInstance() {
 DateTimeField field = DateTimeFieldType.dayOfMonth();
 LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, ISO_UTC);
 assertSame(field, lenientField);

 field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), ISO_UTC);
 lenientField = LenientDateTimeField.getInstance(field, ISO_UTC);
 assertNotSame(field, lenientField);
 }

 @Test
 public void testIsLenient() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), ISO_UTC);
 assertTrue(lenientField.isLenient());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullInstance() {
 LenientDateTimeField lenientField = new LenientDateTimeField((DateTimeField) null, ISO_UTC);
 }

 @Test(expected = NullPointerException.class)
 public void testSetNullBase() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), (Chronology) null);
 }

 @Test
 public void testSetWithinBounds() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), ISO_UTC);
 MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, ISO_UTC);
 long value = 1;
 long result = lenientField.set(dateTime.getLong(), value);
 assertEquals(dateTime.getLong() + value, result);
 }

 @Test
 public void testSetOutOfBoundsNegative() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), ISO_UTC);
 MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, ISO_UTC);
 long value = -1;
 long result = lenientField.set(dateTime.getLong(), value);
 // The result should be the same as the original value plus the difference
 long difference = lenientField.get(dateTime.getLong()) - value;
 long expectedResult = dateTime.getLong() + difference;
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetOutOfBoundsPositive() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), ISO_UTC);
 MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, ISO_UTC);
 long value = 2;
 long result = lenientField.set(dateTime.getLong(), value);
 long expectedResult = dateTime.getLong() + value;
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetOutOfBoundsLargeNegative() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), ISO_UTC);
 MutableDateTime dateTime = new MutableDateTime(Long.MAX_VALUE, 0, 0, 0, 0, 0, 0, ISO_UTC);
 long value = -(DateTimeFieldType.dayOfMonth().getRange(DateTimeConstants.MAX_VALUE) / 2);
 long result = lenientField.set(dateTime.getLong(), value);
 long expectedResult = dateTime.getLong() + value;
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetOutOfBoundsLargePositive() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), ISO_UTC);
 MutableDateTime dateTime = new MutableDateTime(Long.MIN_VALUE, 0, 0, 0, 0, 0, 0, ISO_UTC);
 long value = DateTimeFieldType.dayOfMonth().getRange(DateTimeConstants.MIN_VALUE) / 2 + 1;
 long result = lenientField.set(dateTime.getLong(), value);
 long expectedResult = dateTime.getLong() + value;
 assertEquals(expectedResult, result);
 }
}