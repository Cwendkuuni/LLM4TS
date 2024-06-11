import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

 private static final Chronology UTC = Chronology.utc();
 private static final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
 private MutableDateTime testDate;
 private DateTimeField lenientField;
 private DateTimeField strictField;

 @Test
 public void testLenientDateTimeField() {
 strictField = DAY_OF_MONTH;
 lenientField = LenientDateTimeField.getInstance(strictField, UTC);
 assertEquals("Lenient instance should be of LenientDateTimeField class", LenientDateTimeField.class, lenientField.getClass());
 assertEquals("Lenient instance should have same value type as original", DAY_OF_MONTH, lenientField.getValueType());
 assertEquals("Lenient instance should have same base as original", UTC, lenientField.getBaseDateTime());
 assertNotSame("Lenient instance should not be the same as original", strictField, lenientField);
 }

 @Test
 public void testSetLenientDateTimeField() {
 testDate = new MutableDateTime();
 testDate.setZone(UTC);
 lenientField = DAY_OF_MONTH;
 lenientField = LenientDateTimeField.getInstance(lenientField, UTC);

 // Test setting a valid value
 testDate.setField(lenientField, 1);
 assertEquals("Valid value should be set correctly", 1, testDate.getFieldValue(lenientField));

 // Test setting an out-of-bounds value (day of month < 1)
 testDate.setField(lenientField, -1);
 int day = testDate.getFieldValue(DAY_OF_MONTH);
 assertEquals("Out-of-bounds value should be adjusted by adding difference", 31 + (31 - day), testDate.getMillisecond());

 // Test setting an out-of-bounds value (day of month > number of days in month)
 testDate.setField(lenientField, 32);
 day = testDate.getFieldValue(DAY_OF_MONTH);
 assertEquals("Out-of-bounds value should be adjusted by adding difference", 1, testDate.getFieldValue(lenientField) - day);
 }

 @Test
 public void testLenientDateTimeFieldWithLocalDateTime() {
 testDate = new LocalDateTime();
 lenientField = DAY_OF_MONTH;
 lenientField = LenientDateTimeField.getInstance(lenientField, UTC);

 // Test setting a valid value
 testDate.setField(lenientField, 1);
 assertEquals("Valid value should be set correctly", 1, testDate.getFieldValue(lenientField));

 // Test setting an out-of-bounds value (day of month < 1)
 testDate.setField(lenientField, -1);
 int day = testDate.getFieldValue(DAY_OF_MONTH);
 MutableDateTime adjustedDate = new MutableDateTime(testDate.toDate().getTime());
 adjustedDate.setField(lenientField, 31 + (31 - day));
 assertEquals("Out-of-bounds value should be adjusted by adding difference", adjustedDate.toLocalDateTime().getDayOfMonth(), testDate.getFieldValue(lenientField));

 // Test setting an out-of-bounds value (day of month > number of days in month)
 testDate.setField(lenientField, 32);
 day = testDate.getFieldValue(DAY_OF_MONTH);
 MutableDateTime adjustedDate2 = new MutableDateTime(testDate.toDate().getTime());
 adjustedDate2.setField(lenientField, 1);
 assertEquals("Out-of-bounds value should be adjusted by subtracting difference", adjustedDate2.toLocalDateTime().getDayOfMonth() - day, testDate.getFieldValue(lenientField));
 }

 @Test
 public void testLenientDateTimeFieldWithBaseZoneOffset() {
 testDate = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, UTC);
 lenientField = DAY_OF_MONTH;
 lenientField = LenientDateTimeField.getInstance(lenientField, UTC);

 // Test setting a valid value
 testDate.setField(lenientField, 1);
 assertEquals("Valid value should be set correctly", 1, testDate.getFieldValue(lenientField));

 // Test setting an out-of-bounds value (day of month < 1)
 testDate.setField(lenientField, -1);
 int day = testDate.getFieldValue(DAY_OF_MONTH);
 long millis = testDate.getMillis();
 long adjustment = FieldUtils.safeSubtract(31, day);
 long adjustedMillis = testDate.getMillis() + adjustment * 24 * 60 * 60 * 1000;
 assertEquals("Out-of-bounds value should be adjusted by adding difference", adjustedMillis, testDate.getMillis());

 // Test setting an out-of-bounds value (day of month > number of days in month)
 testDate.setField(lenientField, 32);
 day = testDate.getFieldValue(DAY_OF_MONTH);
 long millis2 = testDate.getMillis();
 long adjustment2 = FieldUtils.safeSubtract(1, day);
 long offset = UTC.getOffset(testDate.toDate().getTime());
 long adjustedMillis2 = testDate.getMillis() - (adjustment2 * 24 * 60 * 60 * 1000 + offset);
 assertEquals("Out-of-bounds value should be adjusted by subtracting difference", adjustedMillis2, testDate.getMillis());
 }

 @Test
 public void testLenientDateTimeFieldWithBaseUTCDateTime() {
 testDate = new MutableDateTime(ZoneUtils.utcDate().getTime());
 lenientField = DAY_OF_MONTH;
 lenientField = LenientDateTimeField.getInstance(lenientField, UTC);

 // Test setting a valid value
 testDate.setField(lenientField, 1);
 assertEquals("Valid value should be set correctly", 1, testDate.getFieldValue(lenientField));

 // Test setting an out-of-bounds value (day of month < 1)
 testDate.setField(lenientField, -1);
 int day = testDate.getFieldValue(DAY_OF_MONTH);
 assertEquals("Out-of-bounds value should be adjusted by adding difference", 31 + (31 - day), testDate.getMillisecond());

 // Test setting an out-of-bounds value (day of month > number of days in month)
 testDate.setField(lenientField, 32);
 day = testDate.getFieldValue(DAY_OF_MONTH);
 assertEquals("Out-of-bounds value should be adjusted by subtracting difference", 1, testDate.getFieldValue(lenientField) - day);
 }
}