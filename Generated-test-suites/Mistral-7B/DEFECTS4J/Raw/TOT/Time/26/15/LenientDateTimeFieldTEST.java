import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.ZoneId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

 private static final Chronology DAYS_2000 = org.joda.time.Days.getInstance(2000);

 @Test
 public void testLenientDateTimeField() {
 DateTimeField lenientField = LenientDateTimeField.getInstance(DateTimeFieldType.dayOfMonth(), DAYS_2000);

 // Test setting valid value
 MutableDateTime mutableDateTime = new MutableDateTime(DAYS_2000, 2000, 1, 1, 0, 0, 0);
 long initialValue = mutableDateTime.getLong();
 lenientField.set(initialValue, 15);
 MutableDateTime resultDateTime = new MutableDateTime(initialValue + (15 - lenientField.get(initialValue)));
 assertEquals(resultDateTime.getLong(), mutableDateTime.getLong());

 // Test setting out of bounds value
 MutableDateTime mutableDateTimeOutOfBounds = new MutableDateTime(DAYS_2000, 2000, 1, 1, 0, 0, 0);
 lenientField.set(mutableDateTimeOutOfBounds.getLong(), 32);
 MutableDateTime resultDateTimeOutOfBounds = new MutableDateTime(mutableDateTimeOutOfBounds.getLong() + (32 - lenientField.get(mutableDateTimeOutOfBounds.getLong())));
 assertEquals(resultDateTimeOutOfBounds.getLong(), mutableDateTimeOutOfBounds.getLong());

 // Test setting value beyond maximum value
 MutableDateTime mutableDateTimeMax = new MutableDateTime(DAYS_2000, 2000, 1, 1, 0, 61, 0);
 lenientField.set(mutableDateTimeMax.getLong(), 62);
 MutableDateTime resultDateTimeMax = new MutableDateTime(mutableDateTimeMax.getLong() + (62 - lenientField.get(mutableDateTimeMax.getLong())));
 assertEquals(resultDateTimeMax.getLong(), mutableDateTimeMax.getLong());

 // Test setting value beyond minimum value
 MutableDateTime mutableDateTimeMin = new MutableDateTime(DAYS_2000, 2000, 1, 1, 0, 0, -1);
 lenientField.set(mutableDateTimeMin.getLong(), -2);
 MutableDateTime resultDateTimeMin = new MutableDateTime(mutableDateTimeMin.getLong() + (2 + lenientField.get(mutableDateTimeMin.getLong())));
 assertEquals(resultDateTimeMin.getLong(), mutableDateTimeMin.getLong());

 // Test setting value for DateTimeFieldType.minuteOfHour
 DateTimeField lenientMinuteField = LenientDateTimeField.getInstance(DateTimeFieldType.minuteOfHour(), DAYS_2000);
 MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime(DAYS_2000, 2000, 1, 1, 0, 0, 0);
 lenientMinuteField.set(mutableLocalDateTime.getLong(), 63);
 MutableLocalDateTime resultLocalDateTime = new MutableLocalDateTime(mutableLocalDateTime.getLong() + (63 - lenientMinuteField.get(mutableLocalDateTime.getLong())), 0);
 assertEquals(resultLocalDateTime.getLong(), mutableLocalDateTime.getLong());

 // Test setting value for DateTimeFieldType.hourOfDay
 DateTimeField lenientHourField = LenientDateTimeField.getInstance(DateTimeFieldType.hourOfDay(), DAYS_2000);
 MutableLocalDateTime mutableLocalDateTimeHour = new MutableLocalDateTime(DAYS_2000, 2000, 1, 1, 0, 0, 0);
 lenientHourField.set(mutableLocalDateTimeHour.getLong(), 26);
 MutableLocalDateTime resultLocalDateTimeHour = new MutableLocalDateTime(mutableLocalDateTimeHour.getLong() + (26 - lenientHourField.get(mutableLocalDateTimeHour.getLong())), 0);
 assertEquals(resultLocalDateTimeHour.getLong(), mutableLocalDateTimeHour.getLong());
 }
}