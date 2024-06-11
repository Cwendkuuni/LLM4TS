import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private DateTimeField lenientYearOfMonth;
 private DateTimeField strictYearOfMonth;
 private Chronology gregorian;

 @Before
 public void setUp() {
 gregorian = Chronology.getInstance("ISO");
 lenientYearOfMonth = DateTimeField.lenientLocalDatePartOfDay(DateTimeFieldType.yearOfMonth(), gregorian);
 strictYearOfMonth = DateTimeFieldType.yearOfMonth();
 }

 @Test
 public void testGetInstance() {
 DateTimeField lenient = LenientDateTimeField.getInstance(strictYearOfMonth, gregorian);
 assertTrue(lenient instanceof LenientDateTimeField);
 assertEquals(lenientYearOfMonth, lenient);

 DateTimeField strict = LenientDateTimeField.getInstance(lenientYearOfMonth, gregorian);
 assertEquals(lenientYearOfMonth, strict);
 }

 @Test
 public void testIsLenient() {
 assertTrue(lenientYearOfMonth.isLenient());
 assertFalse(strictYearOfMonth.isLenient());
 }

 @Test
 public void testSet_valid() {
 MutableDateTime date = new MutableDateTime();
 date.set(2012, 1, 1, 0, 0, 0, 0, gregorian);
 long instant = date.getMillis();
 int value = 2;

 long expected = instant + (lenientYearOfMonth.getIncrement(value) * lenientYearOfMonth.getMillisPerValue());
 long result = lenientYearOfMonth.set(instant, value);
 assertEquals(expected, result);
 }

 @Test
 public void testSet_outOfBounds_positive() {
 MutableDateTime date = new MutableDateTime();
 date.set(2012, 1, 1, 0, 0, 0, 0, gregorian);
 long instant = date.getMillis();
 int value = 13;

 long expected = instant + (lenientYearOfMonth.getIncrement(value) * lenientYearOfMonth.getMillisPerValue());
 long result = lenientYearOfMonth.set(instant, value);
 assertEquals(expected, result);
 }

 @Test
 public void testSet_outOfBounds_negative() {
 MutableDateTime date = new MutableDateTime();
 date.set(2012, 1, 1, 0, 0, 0, 0, gregorian);
 long instant = date.getMillis();
 int value = -3;

 long expected = instant - (lenientYearOfMonth.getIncrement(value) * lenientYearOfMonth.getMillisPerValue());
 long result = lenientYearOfMonth.set(instant, value);
 assertEquals(expected, result);
 }

 @Test
 public void testSet_sameValue() {
 MutableDateTime date = new MutableDateTime();
 date.set(2012, 1, 1, 0, 0, 0, 0, gregorian);
 long instant = date.getMillis();
 int value = 2;

 long result = lenientYearOfMonth.set(instant, value);
 assertEquals(instant, result);
 }

 @Test
 public void testSet_sameValue_differentInstance() {
 MutableDateTime date1 = new MutableDateTime();
 MutableDateTime date2 = new MutableDateTime();

 date1.set(2012, 1, 1, 0, 0, 0, 0, gregorian);
 long instant1 = date1.getMillis();
 int value = 2;

 date2.set(2012, 1, 1, 0, 0, 0, 0, gregorian);
 long instant2 = date2.getMillis();

 assertEquals(instant1, lenientYearOfMonth.set(instant1, value));
 assertEquals(instant2, lenientYearOfMonth.set(instant2, value));
 }

 @Test
 public void testSet_nullInstant() {
 assertNull(lenientYearOfMonth.set(null, 2));
 }

 @Test
 public void testSet_nullValue() {
 long instant = 1234567890L;
 assertEquals(instant, lenientYearOfMonth.set(instant, (int)Short.MIN_VALUE));
 }

 @Test
 public void testSet_valueOutOfRange() {
 long instant = 1234567890L;
 try {
 lenientYearOfMonth.set(instant, Integer.MAX_VALUE);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testConvertLocalToUTC() {
 BaseDateTime localDate = new BaseDateTime(3, 3, 2012, 12, 30, 15, 45, gregorian);
 long localInstant = localDate.getMillis();
 long expected = localInstant + (lenientYearOfMonth.getIncrement(1) * lenientYearOfMonth.getMillisPerValue());
 long result = lenientYearOfMonth.set(localInstant, 1);
 long utcInstant = lenientYearOfMonth.getField(gregorian.withUTC()).getField(new BaseDateTime(result)).getMillis();
 assertEquals(expected, utcInstant);
 }
}