import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

 private static final Chronology DATE_TIME = DateTimeConstants.UTC;
 private static final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
 private static final DateTimeFieldType HOUR_OF_DAY = DateTimeFieldType.hourOfDay();

 @Test
 public void testGetInstance() {
 DateTimeField strictField = new DateTimeField(DAY_OF_MONTH, DATE_TIME);
 DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, DATE_TIME);
 assertNotNull(lenientField);
 assertFalse(lenientField.isLenient()); // lenient instance of a non-lenient field
 lenientField = LenientDateTimeField.getInstance(new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME), DATE_TIME);
 lenientField = LenientDateTimeField.getInstance(lenientField, DATE_TIME); // lenient instance of a lenient field
 assertNotNull(lenientField);
 assertFalse(lenientField.isLenient()); // lenient instance of a lenient field remains lenient
 }

 @Test
 public void testIsLenient() {
 LenientDateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSet_validValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 MutableDateTime testDate = new MutableDateTime(0, 0, 0, 12, 0, 0, 0, DATE_TIME);
 long newValue = 13;
 long result = lenientField.set(testDate.getMillis(), newValue);
 assertEquals(testDate.getMillis() + (newValue - lenientField.get(testDate.getMillis())), result);
 }

 @Test
 public void testSet_sameValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 MutableDateTime testDate = new MutableDateTime(0, 0, 0, 12, 0, 0, 0, DATE_TIME);
 long sameValue = 12;
 long result = lenientField.set(testDate.getMillis(), sameValue);
 assertEquals(testDate.getMillis(), result);
 }

 @Test
 public void testSet_outOfBoundsValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 MutableDateTime testDate = new MutableDateTime(0, 0, 0, 12, 0, 0, 0, DATE_TIME);
 long outOfBoundsValue = 24; // 25 hours
 long result = lenientField.set(testDate.getMillis(), outOfBoundsValue);
 assertTrue(result > testDate.getMillis());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_nullDateTime() {
 LenientDateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 long outOfBoundsValue = 25;
 assertThrows(IllegalArgumentException.class, () -> lenientField.set(null, outOfBoundsValue));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_nullField() {
 DateTimeField lenientField = null;
 Chronology base = DATE_TIME;
 assertThrows(IllegalArgumentException.class, () -> LenientDateTimeField.getInstance(lenientField, base));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_nullChronology() {
 LenientDateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 assertThrows(IllegalArgumentException.class, () -> lenientField.set(0, null));
 }

 @Test
 public void testSet_sameChronology() {
 LenientDateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 MutableDateTime testDate = new MutableDateTime(0, 0, 0, 12, 0, 0, 0, DATE_TIME);
 MutableDateTime testDate2 = new MutableDateTime(0, 0, 0, 12, 0, 0, 0, DATE_TIME);
 long newValue = 13;
 long result1 = lenientField.set(testDate.getMillis(), newValue);
 long result2 = lenientField.set(testDate2.getMillis(), newValue);
 assertEquals(result1, result2);
 }

 @Test
 public void testSet_differentChronology() {
 LenientDateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 MutableDateTime testDate = new MutableDateTime(0, 0, 0, 12, 0, 0, 0, DATE_TIME);
 Chronology differentChronology = ZoneUtils.UTC;
 long newValue = 13;
 long result1 = lenientField.set(testDate.getMillis(), newValue);
 MutableDateTime testDate2 = new MutableDateTime(0, 0, 0, 12, 0, 0, 0, differentChronology);
 long result2 = lenientField.set(testDate2.getMillis(), newValue);
 assertNotSame(result1, result2);
 }

 @Test
 public void testSet_additionWithNegativeValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 MutableDateTime testDate = new MutableDateTime(0, 0, 0, 12, 0, 0, 0, DATE_TIME);
 long negativeValue = -3;
 long result = lenientField.set(testDate.getMillis(), negativeValue);
 long expectedResult = testDate.getMillis() - negativeValue;
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_additionWithOverflowValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 MutableDateTime testDate = new MutableDateTime(0, 0, 0, 23, 59, 59, 59, DATE_TIME);
 long overflowValue = 1;
 long result = lenientField.set(testDate.getMillis(), overflowValue);
 assertTrue(result > testDate.getMillis());
 }
}