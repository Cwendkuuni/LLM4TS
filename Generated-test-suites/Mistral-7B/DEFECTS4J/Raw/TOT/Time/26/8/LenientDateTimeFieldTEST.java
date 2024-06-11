import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

 private static final Chronology UTC = DateTimeUtils.UTC;
 private static final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
 private static final DateTimeFieldType HOUR_OF_DAY = DateTimeFieldType.hourOfDay();

 @Test
 public void testLenientDateTimeField() {
  DateTimeField strictField = DAY_OF_MONTH;
  DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, UTC);

  MutableDateTime dateTime = new MutableDateTime();
  long utcInstant = dateTime.getMillis();

  // test setting a valid value
  lenientField.set(utcInstant, 15);
  assertEquals(15, lenientField.get(utcInstant));

  // test setting a value that is out of bounds (earlier)
  lenientField.set(utcInstant, 32);
  assertEquals(1, lenientField.get(utcInstant)); // should wrap around

  // test setting a value that is out of bounds (later)
  lenientField.set(utcInstant, -10);
  assertEquals(31, lenientField.get(utcInstant)); // should wrap around

  // test setting values for different chronologies
  Chronology japaneseChronology = DateTimeUtils.getJapaneseLocalChronology();
  lenientField = LenientDateTimeField.getInstance(strictField, japaneseChronology);
  lenientField.set(utcInstant, 31);
  long japaneseInstant = japaneseChronology.getZone().getMillis(japaneseChronology.getUTC(new BaseDateTime(lenientField.get(utcInstant))));
  assertEquals(DateTimeUtils.getLocalMillis(ZoneId.of("Japan"), new LocalDateTime(2000, 1, 32, 0, 0, 0).toDateTimeAtStartOfDay()), japaneseInstant);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullField() {
  DateTimeField lenientField = LenientDateTimeField.getInstance(null, UTC);
  fail("Should not be able to create a LenientDateTimeField with a null field");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullChronology() {
  DateTimeField lenientField = LenientDateTimeField.getInstance(DAY_OF_MONTH, null);
  fail("Should not be able to create a LenientDateTimeField with a null chronology");
 }

 @Test
 public void testLenientDateTimeFieldWithTimeZone() {
  DateTimeField strictField = HOUR_OF_DAY;
  DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, UTC);

  MutableDateTime dateTime = new MutableDateTime();
  long utcInstant = dateTime.getMillis();
  long localInstant = dateTime.getZone().getMillis(dateTime.toLocalDateTime());

  // test setting a valid value
  lenientField.set(utcInstant, 14);
  assertEquals(14, lenientField.get(utcInstant));
  assertEquals(14, lenientField.get(localInstant));

  // test setting a value that is out of bounds (earlier)
  lenientField.set(utcInstant, 25);
  assertEquals(1, lenientField.get(utcInstant)); // should wrap around
  assertEquals(23, lenientField.get(localInstant)); // should not wrap around for the local value

  // test setting a value that is out of bounds (later)
  lenientField.set(utcInstant, -1);
  assertEquals(23, lenientField.get(utcInstant)); // should wrap around
  assertEquals(23, lenientField.get(localInstant)); // should not wrap around for the local value
 }
}