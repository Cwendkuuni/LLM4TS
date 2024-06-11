import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.Duration;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology CHRONOLOGY = DateTimeUtils.UTC;
 private static final BasicMonthOfYearDateTimeField MONTH_FIELD =
         new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);

 @Test
 public void testBasicGet() {
     DateTime testDate = new DateTime(2022, 3, 1);
     int month = MONTH_FIELD.get(testDate.getMillis());
     assertEquals(3, month);
 }

 @Test
 public void testBasicSet() {
     DateTime testDate = new DateTime(2022, 3, 1);
     long newMillis = MONTH_FIELD.set(testDate.getMillis(), 5);
     DateTime newDate = new DateTime(newMillis);
     assertEquals(5, MONTH_FIELD.get(newMillis));
     assertEquals(2022, newDate.getYear());
     assertEquals(5, newDate.getMonthOfYear());
 }

 @Test
 public void testBasicAdd() {
     DateTime testDate = new DateTime(2022, 3, 1);
     long newMillis = MONTH_FIELD.add(testDate.getMillis(), 3);
     DateTime newDate = new DateTime(newMillis);
     assertEquals(6, MONTH_FIELD.get(newMillis));
     assertEquals(2022, newDate.getYear());
     assertEquals(6, newDate.getMonthOfYear());
 }

 @Test
 public void testAddWrapField() {
     DateTime testDate = new DateTime(2022, 13, 1); // 13 is not a valid month
     long newMillis = MONTH_FIELD.addWrapField(testDate.getMillis(), 3);
     DateTime newDate = new DateTime(newMillis);
     assertEquals(4, MONTH_FIELD.get(newMillis));
     assertEquals(2022, newDate.getYear());
     assertEquals(4, newDate.getMonthOfYear());
 }

 @Test
 public void testAddLeapYear() {
     DateTime testDate = new DateTime(2022, 2, 29); // 29 is not a valid day for a common year
     long newMillis = MONTH_FIELD.add(testDate.getMillis(), 12);
     DateTime newDate = new DateTime(newMillis);
     assertEquals(3, MONTH_FIELD.get(newMillis)); // February of leap year has 29 days
     assertEquals(2023, newDate.getYear());
 }

 @Test
 public void testLeap() {
     DateTime testDate = new DateTime(2024, 2, 29);
     assertTrue(MONTH_FIELD.isLeap(testDate.getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
     DateTime testDate = new DateTime(2024, 2, 29);
     assertEquals(1, MONTH_FIELD.getLeapAmount(testDate.getMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
     DurationField leapDuration = MONTH_FIELD.getLeapDurationField();
     assertEquals(DurationFieldType.DAYS, leapDuration.getFieldType());
     assertEquals(1, leapDuration.getLength());
 }

 @Test
 public void testMinValue() {
     long minMillis = MONTH_FIELD.getMinimumValue();
     DateTime minDate = new DateTime(minMillis);
     assertEquals(1, MONTH_FIELD.get(minDate.getMillis()));
     assertEquals(1, minDate.getMonthOfYear());
 }

 @Test
 public void testMaxValue() {
     long maxMillis = MONTH_FIELD.getMaximumValue() * 12 * 30 * 24 * 60 * 60 * 1000;
     DateTime maxDate = new DateTime(maxMillis);
     assertEquals(12, MONTH_FIELD.get(maxMillis));
     assertEquals(12, maxDate.getMonthOfYear());
 }

 @Test
 public void testGetDifferenceAsLong() {
     DateTime firstDate = new DateTime(2022, 1, 1);
     DateTime secondDate = new DateTime(2022, 2, 28);
     long difference = MONTH_FIELD.getDifferenceAsLong(firstDate.getMillis(), secondDate.getMillis());
     assertEquals(1, difference);
 }

 @Test
 public void testRoundFloor() {
     DateTime testDate = new DateTime(2022, 3, 1);
     long roundedMillis = MONTH_FIELD.roundFloor(testDate.getMillis());
     DateTime roundedDate = new DateTime(roundedMillis);
     assertEquals(3, MONTH_FIELD.get(roundedMillis));
     assertEquals(2022, roundedDate.getYear());
     assertEquals(3, roundedDate.getMonthOfYear());
 }

 @Test
 public void testRemainder() {
     DateTime testDate = new DateTime(2022, 3, 1);
     long remainderMillis = MONTH_FIELD.remainder(testDate.getMillis());
     long remainingDays = MONTH_FIELD.getLeapDurationField().get(remainderMillis);
     assertEquals(remainingDays, remainderMillis / (24 * 60 * 60 * 1000));
 }
}