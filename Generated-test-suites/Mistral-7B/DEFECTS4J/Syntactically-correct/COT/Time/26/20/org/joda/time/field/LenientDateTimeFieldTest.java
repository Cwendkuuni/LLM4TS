import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private DateTimeField field;
 private Chronology chronology;
 private LenientDateTimeField lenientField;

 @Before
 public void setUp() {
 field = DateTimeFields.millisecondOfMinute();
 chronology = BaseDateTime.UTC.withZoneRetainFields();
 lenientField = LenientDateTimeField.getInstance(field, chronology);
 }

 // Test method for LenientDateTimeField constructor
 @Test
 public void testLenientDateTimeFieldConstructor() {
 DateTimeField expectedField = field;
 Chronology expectedChronology = chronology;
 assertEquals(expectedField.getClass(), lenientField.getDelegate().getClass());
 assertEquals(expectedChronology, lenientField.iBase);
 }

 // Test method for isLenient()
 @Test
 public void testIsLenient() {
 assertTrue(lenientField.isLenient());
 }

 // Test method for getInstance()
 @Test
 public void testGetInstance() {
 DateTimeField expectedField = field;
 Chronology expectedChronology = chronology;
 DateTimeField lenientExpectedField = lenientField;
 DateTimeField result = LenientDateTimeField.getInstance(expectedField, expectedChronology);
 assertEquals(lenientExpectedField, result);
 }

 // Test method for set() - valid value
 @Test
 public void testSetValidValue() {
 long instant = 1516044223000L; // 25 Aug 2017 15:10:23.000Z
 int value = 15; // 15 minutes after the current value
 long expectedResult = 1516045723000L; // 25 Aug 2017 15:22:03.000Z
 long result = lenientField.set(instant, value);
 assertEquals(expectedResult, result);
 }

 // Test method for set() - out of bounds value
 @Test(expected = IllegalArgumentException.class)
 public void testSetOutOfBoundsValue() {
 long instant = 1516044223000L; // 25 Aug 2017 15:10:23.000Z
 int value = DateTimeFields.millisecondOfMinute().getRange() + 1;
 lenientField.set(instant, value);
 }

 // Test method for set() - negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeValue() {
 long instant = 1516044223000L; // 25 Aug 2017 15:10:23.000Z
 int value = -1;
 lenientField.set(instant, value);
 }

 // Test method for set() - value with the same instant
 @Test
 public void testSetSameInstantValue() {
 long instant = 1516044223000L; // 25 Aug 2017 15:10:23.000Z
 int value = lenientField.get(instant);
 long expectedResult = instant;
 long result = lenientField.set(instant, value);
 assertEquals(expectedResult, result);
 }

 // Test method for set() - zero value
 @Test
 public void testSetZeroValue() {
 long instant = 1516044223000L; // 25 Aug 2017 15:10:23.000Z
 int value = 0;
 long expectedResult = instant;
 long result = lenientField.set(instant, value);
 assertEquals(expectedResult, result);
 }

 // Test method for set() - value larger than range
 @Test(expected = IllegalArgumentException.class)
 public void testSetValueLargerThanRange() {
 long instant = 1516044223000L; // 25 Aug 2017 15:10:23.000Z
 int value = DateTimeFields.millisecondOfMinute().getRange() + 1;
 lenientField.set(instant, value);
 }

 // Test method for set() - setting value to a MutableDateTime
 @Test
 public void testSetValueToMutableDateTime() {
 MutableDateTime mutableDateTime = new MutableDateTime(1516044223000L, chronology);
 int value = 15; // 15 minutes after the current value
 lenientField.set(mutableDateTime.toInstant(), value);
 long expectedResult = mutableDateTime.getMilliseconds() + 15 * 60 * 1000;
 long result = mutableDateTime.getMilliseconds();
 assertEquals(expectedResult, result);
 }

 // Test method for set() - setting value to a LocalDateTime
 @Test
 public void testSetValueToLocalDateTime() {
 LocalDateTime localDateTime = new LocalDateTime(1516044223000L, chronology.getLocale());
 int value = 15; // 15 minutes after the current value
 lenientField.set(localDateTime.toInstant(), value);
 long expectedResult = localDateTime.toDate().getTime() + 15 * 60 * 1000;
 long result = localDateTime.toDate().getTime();
 assertEquals(expectedResult, result);
 }

 // Test method for set() - setting value to a BaseDateTime
 @Test
 public void testSetValueToBaseDateTime() {
 BaseDateTime baseDateTime = new BaseDateTime(1516044223000L, chronology);
 int value = 15; // 15 minutes after the current value
 lenientField.set(baseDateTime.toInstant(), value);
 long expectedResult = baseDateTime.getMillis() + 15 * 60 * 1000;
 long result = baseDateTime.getMillis();
 assertEquals(expectedResult, result);
 }
}