import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class LenientDateTimeFieldTest {

 private static final Chronology UTC = Chronology.UTC;
 private static final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
 private static final DateTimeFieldType HOUR_OF_DAY = DateTimeFieldType.hourOfDay();
 private static final DateTimeFieldType MINUTE_OF_HOUR = DateTimeFieldType.minuteOfHour();
 private static final DateTimeFieldType SECOND_OF_MINUTE = DateTimeFieldType.secondOfMinute();

 @Test
 public void testGetInstance() {
 DateTimeField field = DateTimeFieldType.dayOfMonth();
 DateTimeField lenientField = LenientDateTimeField.getInstance(field, UTC);
 assertTrue(lenientField instanceof LenientDateTimeField);
 assertSame(field, lenientField.getWrappedField());
 assertSame(UTC, lenientField.getBase());
 }

 @Test
 public void testIsLenient() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSet_ValidValue() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = 10;
 long expectedResult = 1561134800000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_ValidValue_Negative() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = -5;
 long expectedResult = 1561119800000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_OutOfBoundsValue_Positive() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = 60;
 long expectedResult = 1561133600000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_OutOfBoundsValue_Negative() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = -60;
 long expectedResult = 1561120800000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_SameValue() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = 10;
 long expectedResult = 1561124800000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_ValueGreaterThanMaximum() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(Long.MAX_VALUE, UTC);
 long newValue = 62;
 long expectedResult = Long.MAX_VALUE;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_ValueLessThanMinimum() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_of_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(Long.MIN_VALUE, UTC);
 long newValue = -61;
 long expectedResult = Long.MIN_VALUE;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_ValueSameAsInstant() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_of_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = 10;
 long expectedResult = 1561124800000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet_ImmutableInstance() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_of_MONTH, UTC);
 BaseDateTime immutableDateTime = new BaseDateTime(1561124800000L, UTC);
 long newValue = 10;
 lenientField.set(immutableDateTime.toLocalDateTime().toMillis(), newValue);
 fail("Should throw UnsupportedOperationException");
 }

 @Test
 public void testSet_WrongType() {
 DateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = 12;
 long expectedResult = 1561125560000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_WithChronologyDifferentThanBase() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableLocalDateTime localDateTime = new MutableLocalDateTime(LocalDate.of(2023, 1, 1), new LocalTime(12, 30, 0));
 Chronology base = Chronology.of("America/Los_Angeles");
 long newValue = 10;
 long expectedResult = 1672137200000L;
 long result = lenientField.set(localDateTime.toDateTimeAtOffset().getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_WithChronologySameAsBase() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableLocalDateTime localDateTime = new MutableLocalDateTime(LocalDate.of(2023, 1, 1), new LocalTime(12, 30, 0));
 Chronology base = UTC;
 long newValue = 10;
 long expectedResult = 1672137200000L;
 long result = lenientField.set(localDateTime.toDateTimeAtOffset().getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_InstantOutOfRange() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(Long.MIN_VALUE - 1, UTC);
 long newValue = 10;
 lenientField.set(dateTime.getMillis(), newValue);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_InstantTooLarge() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(Long.MAX_VALUE + 1, UTC);
 long newValue = 10;
 lenientField.set(dateTime.getMillis(), newValue);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_ValueGreaterThanMaximumWithChronology() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(Long.MAX_VALUE, UTC);
 long newValue = 63;
 lenientField.set(dateTime.getMillis(), newValue);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_ValueLessThanMinimumWithChronology() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(Long.MIN_VALUE, UTC);
 long newValue = -62;
 lenientField.set(dateTime.getMillis(), newValue);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testSet_WithSecondOfMinuteOutOfBounds() {
 DateTimeField lenientField = new LenientDateTimeField(SECOND_OF_MINUTE, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = 61;
 long expectedResult = 1561124901000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_WithSecondOfMinuteOutOfBoundsNegative() {
 DateTimeField lenientField = new LenientDateTimeField(SECOND_OF_MINUTE, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = -61;
 long expectedResult = 1561124739000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_WithMinuteOfHourOutOfBounds() {
 DateTimeField lenientField = new LenientDateTimeField(MINUTE_OF_HOUR, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = 61;
 long expectedResult = 1561125460000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_WithMinuteOfHourOutOfBoundsNegative() {
 DateTimeField lenientField = new LenientDateTimeField(MINUTE_OF_HOUR, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = -61;
 long expectedResult = 1561123260000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_WithHourOfDayOutOfBounds() {
 DateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = 25;
 long expectedResult = 1561133000000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_WithHourOfDayOutOfBoundsNegative() {
 DateTimeField lenientField = new LenientDateTimeField(HOUR_OF_DAY, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = -24;
 long expectedResult = 1561109600000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_WithDayOfMonthOutOfBounds() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = 32;
 long expectedResult = 1561140800000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSet_WithDayOfMonthOutOfBoundsNegative() {
 DateTimeField lenientField = new LenientDateTimeField(DAY_OF_MONTH, UTC);
 MutableDateTime dateTime = new MutableDateTime(1561124800000L, UTC);
 long newValue = -3;
 long expectedResult = 1561124512000L;
 long result = lenientField.set(dateTime.getMillis(), newValue);
 assertEquals(expectedResult, result);
 }
}