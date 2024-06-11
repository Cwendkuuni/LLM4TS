import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DurationField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology iChronology = new BasicChronology("ISO", null);
 private static final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);

 @Test
 public void testGet() {
 int month = field.get(DateTimeConstants.MINUTE_OF_DAY.getMillis(new DateTime()));
 assertEquals("Current month should be equal to 1", 1, month);
 }

 @Test
 public void testSet() {
 long instant = DateTimeConstants.MILLIS_PER_DAY;
 long newInstant = field.set(instant, 12);
 assertEquals("Setting month to 12 should change instant", DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_YEAR * 11, newInstant);
 }

 @Test
 public void testIsLeap() {
 LocalDate date = new LocalDate(2000, 2, 29); //February 29, 2000 is a leap day
 assertTrue("2000 is a leap year", field.isLeap(date.toInstant().getMillis()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetInvalidMonth() {
 long instant = DateTimeConstants.MILLIS_PER_DAY;
 int invalidMonth = -1;
 long newInstant = field.set(instant, invalidMonth);
 fail("Setting month to negative value should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxMonth() {
 long instant = DateTimeConstants.MILLIS_PER_DAY;
 int maxMonth = 13;
 long newInstant = field.set(instant, maxMonth);
 fail("Setting month to greater than maximum value should throw IllegalArgumentException");
 }

 @Test
 public void testAdd() {
 long instant = DateTimeConstants.MILLIS_PER_DAY;
 long newInstant = field.add(instant, 12);
 assertEquals("Adding 12 months should change instant", DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_YEAR * 11, newInstant);
 }

 @Test
 public void testAddSmallNegativeNumber() {
 long instant = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_YEAR;
 long newInstant = field.add(instant, -3);
 assertEquals("Subtracting 3 months from end of year should change instant to start of previous year", DateTimeConstants.MILLIS_PER_DAY, newInstant);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddLargeNegativeNumber() {
 long instant = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_YEAR;
 long newInstant = field.add(instant, -DateTimeConstants.DAYS_PER_YEAR * 11);
 fail("Subtracting 11 years from end of year should throw IllegalArgumentException");
 }

 @Test
 public void testAddLargePositiveNumber() {
 long instant = DateTimeConstants.MILLIS_PER_DAY;
 long newInstant = field.add(instant, DateTimeConstants.DAYS_PER_YEAR * 12);
 assertEquals("Adding 12 years should change instant to start of next cycle", (DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_YEAR * 12), newInstant);
 }

 @Test
 public void testAddWrap() {
 long instant = DateTimeConstants.MILLIS_PER_DAY + DateTimeConstants.DAYS_PER_YEAR * 11;
 long newInstant = field.addWrapField(instant, -1);
 assertEquals("Wrapping around from December to January should change instant", DateTimeConstants.MILLIS_PER_DAY, newInstant);
 }

 @Test
 public void testAddMonthsWithReadablePartial() {
 LocalDateTime date = new LocalDateTime(DateTimeConstants.MILLIS_PER_DAY);
 int[] values = new int[3];
 values[0] = DateTimeConstants.MONTH_OF_YEAR;
 values[1] = 2;
 values[2] = 1;
 int[] result = field.add(date.toLocalDate().toReadablePartial(DateTimeFieldType.monthOfYear()), 0, values, 2);
 assertEquals("Adding 2 months to December should change month to February", DateTimeFieldType.monthOfYear().getValues(result[0]), 2);
 assertEquals("Adding 2 months to December should change year to January of next year", values[1], result[1]);
 assertEquals("Adding 2 months to December should change day of month to 1", values[2], result[2]);
 }
}