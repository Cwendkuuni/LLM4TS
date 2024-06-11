import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private static final Chronology UTC = Chronology.UTC;

 @Test
 public void testLenientInstance() {
 DateTimeField field = LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC), UTC);
 assertTrue(field.isLenient());
 }

 @Test
 public void testGetInstanceWithNullField() {
 DateTimeField field = LenientDateTimeField.getInstance(null, UTC);
 assertNull(field);
 }

 @Test
 public void testLenientSetMethod() {
 DateTimeField field = new LenientDateTimeField(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC), UTC);
 MutableDateTime dateTime = new MutableDateTime(1356960000000L, UTC);
 long newValue = 31;
 long expectedValue = 31L;
 long actualValue = field.set(dateTime.getMillis(), newValue);
 assertEquals(expectedValue, actualValue);
 dateTime.setMillis(actualValue);
 assertEquals(newValue, field.get(dateTime.getMillis()));
 }

 @Test
 public void testLenientSetMethodWithOutOfBoundsValue() {
 DateTimeField field = new LenientDateTimeField(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC), UTC);
 MutableDateTime dateTime = new MutableDateTime(1356960000000L, UTC);
 int newValue = 32;
 long actualValue = field.set(dateTime.getMillis(), newValue);
 assertNotEquals(newValue, field.get(actualValue));
 assertGreaterThan(actualValue, dateTime.getMillis());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLenientSetMethodWithNullInstant() {
 DateTimeField field = new LenientDateTimeField(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC), UTC);
 field.set(null, 31);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLenientSetMethodWithNullValue() {
 DateTimeField field = new LenientDateTimeField(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC), UTC);
 long instant = 1356960000000L;
 field.set(instant, null);
 }

 @Test
 public void testLenientIsLenientMethod() {
 DateTimeField field = new LenientDateTimeField(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC), UTC);
 assertTrue(field.isLenient());
 }

 @Test
 public void testLenientGetInstanceWithSameFieldAndBase() {
 DateTimeField field1 = LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC), UTC);
 DateTimeField field2 = LenientDateTimeField.getInstance(field1, UTC);
 assertEquals(field1, field2);
 }

 @Test
 public void testLenientGetInstanceWithSameFieldButDifferentBase() {
 DateTimeField field1 = LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC), UTC);
 DateTimeField field2 = LenientDateTimeField.getInstance(field1, LocalDateTime.now().toDate().toInstant().toEpochMilli(), UTC);
 assertNotSame(field1, field2);
 }

 @Test
 public void testLenientGetInstanceWithDifferentFieldButSameBase() {
 DateTimeField field1 = LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC), UTC);
 DateTimeField field2 = LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.minuteOfHour(), UTC), UTC);
 assertNotSame(field1, field2);
 }

 @Test
 public void testLenientGetInstanceWithNullBase() {
 DateTimeField field = LenientDateTimeField.getInstance(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), UTC), null);
 assertNull(field);
 }
}