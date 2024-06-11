import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.MutableDateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private static final Chronology DATE_TIME = DateTimeConstants.ISO_DATE_TIME_CHRONOLOGY;
 private static final DateTimeFieldType DAY_OF_YEAR = DateTimeFieldType.dayOfYear();
 private static final DateTimeFieldType HOUR_OF_DAY = DateTimeFieldType.hourOfDay();
 private static final DateTimeFieldType MINUTE_OF_HOUR = DateTimeFieldType.minuteOfHour();
 private static final DateTimeFieldType SECOND_OF_MINUTE = DateTimeFieldType.secondOfMinute();

 @Test
 public void testGetInstance() {
 DateTimeField field = DateTimeFieldType.dayOfYear();
 DateTimeField lenientField = LenientDateTimeField.getInstance(field, DATE_TIME);
 assertTrue(lenientField instanceof LenientDateTimeField);
 }

 @Test
 public void testIsLenient() {
 DateTimeField field = new LenientDateTimeField(DateTimeFieldType.dayOfYear(), DATE_TIME);
 assertTrue(field.isLenient());
 }

 @Test
 public void testSet_ValidValues() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(1546304000000L, DATE_TIME);

 // Test valid field values
 long value = 1;
 long newValue = field.set(dateTime.toLocalDate().getMillis(), value);
 assertEquals(newValue, dateTime.getMillis() + value * DAY_OF_YEAR.getIncrement(DATE_TIME).getMillis());

 value = 366; // Out of bounds for a non-leap year
 newValue = field.set(dateTime.toLocalDate().getMillis(), value);
 assertEquals(newValue, dateTime.getMillis() + value * DAY_OF_YEAR.getIncrement(DATE_TIME).getMillis());
 }

 @Test
 public void testSet_NullDateTime() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 assertNull(field.set(null, 1));
 }

 @Test
 public void testSet_InvalidDateTime() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(-1, DATE_TIME);
 assertNotNull(field.set(dateTime.toLocalDate().getMillis(), 1));
 assertEquals(dateTime.getMillis(), field.get(new BaseDateTime(dateTime.toLocalDate().getMillis(), DATE_TIME).getMillis()));
 }

 @Test
 public void testSet_NullValue() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(1546304000000L, DATE_TIME);
 assertNotNull(field.set(dateTime.toLocalDate().getMillis(), null));
 assertEquals(dateTime.getMillis(), field.get(new BaseDateTime(dateTime.toLocalDate().getMillis(), DATE_TIME).getMillis()));
 }

 @Test
 public void testSet_OutOfBoundsValue() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(Long.MAX_VALUE, DATE_TIME);
 assertNotNull(field.set(dateTime.toLocalDate().getMillis(), Integer.MAX_VALUE));
 assertEquals(dateTime.getMillis(), field.get(new BaseDateTime(dateTime.toLocalDate().getMillis(), DATE_TIME).getMillis()));
 }

 @Test
 public void testSet_LargeValue() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(Long.MAX_VALUE, DATE_TIME);
 long largeValue = Long.MAX_VALUE / 2 + 1;
 long newValue = field.set(dateTime.toLocalDate().getMillis(), largeValue);
 assertEquals(newValue, dateTime.getMillis() + largeValue * DAY_OF_YEAR.getIncrement(DATE_TIME).getMillis());
 }

 @Test
 public void testSet_IncrementByOne() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(1546304000000L, DATE_TIME);
 long newValue = field.set(dateTime.toLocalDate().getMillis(), getValue(dateTime) + 1);
 assertEquals(newValue, dateTime.getMillis() + DAY_OF_YEAR.getIncrement(DATE_TIME).getMillis());
 }

 @Test
 public void testSet_IncrementByNegative() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(1546304000000L, DATE_TIME);
 long newValue = field.set(dateTime.toLocalDate().getMillis(), getValue(dateTime) - 1);
 assertEquals(newValue, dateTime.getMillis() - DAY_OF_YEAR.getIncrement(DATE_TIME).getMillis());
 }

 @Test
 public void testSet_DateWithMaxValueForField() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(Long.MAX_VALUE, DATE_TIME);
 long maxValue = getValue(dateTime) + DAY_OF_YEAR.getValues(DATE_TIME);
 long newValue = field.set(dateTime.toLocalDate().getMillis(), maxValue);
 assertEquals(newValue, dateTime.getMillis() + maxValue * DAY_OF_YEAR.getIncrement(DATE_TIME).getMillis());
 }

 @Test
 public void testSet_DateWithMinValueForField() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(Long.MIN_VALUE, DATE_TIME);
 long minValue = getValue(dateTime) - DAY_OF_YEAR.getValues(DATE_TIME);
 long newValue = field.set(dateTime.toLocalDate().getMillis(), minValue);
 assertEquals(newValue, dateTime.getMillis() + minValue * DAY_OF_YEAR.getIncrement(DATE_TIME).getMillis());
 }

 @Test
 public void testSet_DateWithSameValue() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(1546304000000L, DATE_TIME);
 long sameValue = getValue(dateTime);
 long newValue = field.set(dateTime.toLocalDate().getMillis(), sameValue);
 assertEquals(newValue, dateTime.getMillis());
 }

 @Test
 public void testSet_DifferentChronology() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTimeUTC = new MutableDateTime(1546304000000L, DateTimeConstants.UTC);
 long newValue = field.set(dateTimeUTC.toLocalDate().getMillis(), getValue(dateTimeUTC));
 assertEquals(newValue, dateTimeUTC.getMillis());
 }

 @Test
 public void testSet_DifferentFieldType() {
 DateTimeField field = new LenientDateTimeField(HOUR_OF_DAY, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(1546304000000L, DATE_TIME);
 long newValue = field.set(dateTime.toLocalDate().getMillis(), getValue(dateTime) + 1);
 assertEquals(newValue, dateTime.getMillis() + HOUR_OF_DAY.getIncrement(DATE_TIME).getMillis());
 }

 @Test
 public void testVerify_ValidValues() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(1546304000000L, DATE_TIME);
 LocalDateTimeFieldVerifier.verify(field, dateTime.toLocalDateTime(), getValue(dateTime), true);
 }

 @Test
 public void testVerify_InvalidValues() {
 DateTimeField field = new LenientDateTimeField(DAY_OF_YEAR, DATE_TIME);
 MutableDateTime dateTime = new MutableDateTime(Long.MIN_VALUE, DATE_TIME);
 LocalDateTimeFieldVerifier.verify(field, dateTime.toLocalDateTime(), getValue(dateTime), false);
 }

 private int getValue(MutableDateTime dateTime) {
 return (int) (get(dateTime.toLocalDate().getMillis(), field) / field.getIncrement(DATE_TIME).getMillis());
 }
}