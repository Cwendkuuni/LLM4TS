import org.joda.time.BasicMonthOfYearDateTimeField;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.chrono.BasicChronology;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicChronology CHRONOLOGY = new BasicChronology("BasicMonthOfYearDateTimeFieldTest", DateTimeFieldType.monthOfYear(), DateTimeConstants.MIN_YEAR, DateTimeConstants.MAX_YEAR) {
 @Override
 public int getDaysInYearMonth(int year, int month) {
 switch (month) {
 case 2:
 return isLeapYear(year) ? 29 : 28;
 default:
 return 30;
 }
 }
 };

 @Test
 public void testBasicMethods() {
 // Testing basic methods
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
 assertEquals(0, field.get(0));
 assertEquals(1, field.get(1));
 assertEquals(12, field.get(DateTimeConstants.MAX_INSTANT));
 assertEquals(1, field.get(DateTimeConstants.MAX_INSTANT - 1));
 }

 @Test
 public void testIsLenient() {
 // Testing isLenient method
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet() {
 // Testing get method
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 assertEquals(1, field.get(DateTimeConstants.MIN_INSTANT));
 assertEquals(2, field.get(DateTimeConstants.MIN_INSTANT + 30));
 assertEquals(3, field.get(DateTimeConstants.MIN_INSTANT + 61));
 assertEquals(11, field.get(DateTimeConstants.MAX_INSTANT - 30));
 assertEquals(12, field.get(DateTimeConstants.MAX_INSTANT));
 }

 @Test
 public void testAdd() {
 // Testing add method with positive values
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long testValue = DateTimeConstants.MIN_INSTANT;
 long result = field.add(testValue, 1);
 assertEquals(DateTimeConstants.MIN_INSTANT + 30, result);

 // Testing add method with negative values
 result = field.add(testValue, -1);
 assertEquals(DateTimeConstants.MIN_INSTANT, result);
 }

 @Test
 public void testAddMonths() {
 // Testing addMonths method
 LocalDateTime testDateTime = new LocalDateTime();
 testDateTime = testDateTime.withMonthOfYearSet(1).withDayOfMonth(1);
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long testInstant = testDateTime.getMillis();
 long result = field.add(testInstant, 3);
 LocalDateTime expectedDateTime = testDateTime.plusMonths(3);
 assertEquals(expectedDateTime.getMillis(), result);
 }

 @Test
 public void testAddWithLimits() {
 // Testing add method with limits
 long testValue = DateTimeConstants.MIN_INSTANT;
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long maxValue = DateTimeConstants.MAX_INSTANT;
 long result;

 // Testing adding one month to minimum value
 result = field.add(testValue, 1);
 assertEquals(DateTimeConstants.MIN_INSTANT + 30, result);

 // Testing adding one month to maximum value
 result = field.add(maxValue - 1, 1);
 assertEquals(maxValue, result);

 // Testing adding more months to maximum value
 result = field.add(maxValue, 10);
 assertEquals(maxValue, result);
 }

 @Test
 public void testAddWrapField() {
 // Testing addWrapField method
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long minusOneYear = DateTimeConstants.MIN_INSTANT - (DateTimeConstants.MAX_YEAR - DateTimeConstants.MIN_YEAR) * 12L;
 long result = field.addWrapField(minuendInstant, -12);
 assertEquals(DateTimeConstants.MIN_INSTANT, result);

 result = field.addWrapField(minuendInstant, 12);
 assertEquals(DateTimeConstants.MAX_INSTANT, result);
 }

 @Test
 public void testGetDifferenceAsLong() {
 // Testing getDifferenceAsLong method
 long minuendInstant = DateTimeConstants.MIN_INSTANT;
 long subtrahendInstant = minuendInstant + (DateTimeConstants.MAX_YEAR - DateTimeConstants.MIN_YEAR) * 12L;
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long result = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 assertEquals(-12, result);
 }

 @Test
 public void testSet() {
 // Testing set method
 long testValue = DateTimeConstants.MIN_INSTANT;
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long result = field.set(testValue, 12);
 assertEquals(DateTimeConstants.MAX_INSTANT, result);

 // Testing setting month to minimum value
 result = field.set(testValue, 1);
 assertEquals(testValue, result);

 // Testing setting month to maximum value
 result = field.set(testValue, 13);
 assertEquals(testValue, result);
 }

 @Test
 public void testGetRangeDurationField() {
 // Testing getRangeDurationField method
 DurationField result = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2).getRangeDurationField();
 assertEquals(DurationField.YEARS, result.getFieldType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetInvalidMonth() {
 // Testing setting invalid month
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long testValue = DateTimeConstants.MIN_INSTANT;
 field.set(testValue, 13);
 }

 @Test
 public void testIsLeap() {
 // Testing isLeap method
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 LocalDate testDate = new LocalDate(2000, 2, 29);
 boolean result = field.isLeap(testDate.getMillis());
 assertTrue(isLeapYear(2000));
 assertEquals(true, result);

 testDate = new LocalDate(2000, 2, 28);
 result = field.isLeap(testDate.getMillis());
 assertEquals(false, result);
 }

 @Test
 public void testGetLeapAmount() {
 // Testing getLeapAmount method
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 LocalDate testDate = new LocalDate(2000, 2, 29);
 int result = field.getLeapAmount(testDate.getMillis());
 assertEquals(1, result);

 testDate = new LocalDate(2000, 2, 28);
 result = field.getLeapAmount(testDate.getMillis());
 assertEquals(0, result);
 }

 @Test
 public void testGetLeapDurationField() {
 // Testing getLeapDurationField method
 DurationField result = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2).getLeapDurationField();
 assertEquals(DurationField.DAYS, result.getFieldType());
 }

 @Test
 public void testGetMinimumValue() {
 // Testing getMinimumValue method
 assertEquals(DateTimeConstants.MIN_MONTH_OF_YEAR, new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2).getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 // Testing getMaximumValue method
 assertEquals(DateTimeConstants.MAX_MONTH_OF_YEAR, new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2).getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 // Testing roundFloor method
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long testValue = DateTimeConstants.MIN_INSTANT - 10;
 long result = field.roundFloor(testValue);
 assertEquals(DateTimeConstants.MIN_INSTANT, result);

 testValue += 10;
 result = field.roundFloor(testValue);
 assertEquals(DateTimeConstants.MIN_INSTANT, result);

 testValue += 1;
 result = field.roundFloor(testValue);
 assertEquals(DateTimeConstants.MIN_INSTANT + 30, result);
 }

 @Test
 public void testRemainder() {
 // Testing remainder method
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);
 long testValue = DateTimeConstants.MIN_INSTANT;
 long result = field.remainder(testValue);
 assertEquals(0, result);

 testValue += 1;
 result = field.remainder(testValue);
 assertEquals(DateTimeConstants.MIN_MONTHS, result);
 }
}