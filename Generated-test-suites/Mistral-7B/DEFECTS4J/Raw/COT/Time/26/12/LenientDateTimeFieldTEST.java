import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time. PeroableDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

 private DateTimeField field;
 private Chronology chronology;

 @Before
 public void setup() {
 field = LenientDateTimeField.getInstance(new LocalDateTimeField(DateTimeFieldType.dayOfMonth(), null), null);
 chronology = BaseDateTime.UTC;
 }

 @Test
 public void testGetInstance() {
 DateTimeField lenientField = LenientDateTimeField.getInstance(new LocalDateTimeField(DateTimeFieldType.dayOfMonth(), null), null);
 DateTimeField strictField = new LocalDateTimeField(DateTimeFieldType.dayOfMonth(), null);
 DateTimeField lenientInstance = LenientDateTimeField.getInstance(strictField, null);
 assertTrue(lenientField.isLenient());
 assertTrue(lenientInstance.isLenient());
 }

 @Test
 public void testIsLenient() {
 assertTrue(field.isLenient());
 }

 @Test
 public void testSet_validValues() {
 MutableDateTime dateTime = new MutableDateTime();
 dateTime.setDateTime(0);
 long result = field.set(dateTime.getLong(), 1);
 assertEquals(dateTime.getLong() + field.getLength(), result);
 dateTime.setDateTime(result);
 }

 @Test
 public void testSet_outOfBoundsValues() {
 MutableDateTime dateTime = new MutableDateTime();
 dateTime.setDateTime(0);
 long result = field.set(dateTime.getLong(), 32);
 assertEquals(dateTime.getLong() + field.getLength() * 32, result);
 dateTime.setDateTime(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_nullDateTime() {
 field.set(null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_nullValue() {
 field.set(new MutableDateTime().toDate(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_invalidValue() {
 MutableDateTime dateTime = new MutableDateTime();
 dateTime.setDateTime(0);
 try {
 field.set(dateTime.getLong(), Integer.MIN_VALUE);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_invalidValue2() {
 MutableDateTime dateTime = new MutableDateTime();
 dateTime.setDateTime(0);
 try {
 field.set(dateTime.getLong(), Integer.MAX_VALUE);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got " + e.getMessage());
 }
 }

 @Test
 public void testSet_validValueOutOfRange() {
 MutableDateTime dateTime = new MutableDateTime();
 dateTime.setDateTime(0);
 long result = field.set(dateTime.getLong(), Integer.MIN_VALUE + field.getLength());
 assertEquals(dateTime.getLong() + (field.getLength() * 2), result);
 dateTime.setDateTime(result);
 }

 @Test
 public void testSet_validValueOutOfRange2() {
 MutableDateTime dateTime = new MutableDateTime();
 dateTime.setDateTime(0);
 long result = field.set(dateTime.getLong(), Integer.MAX_VALUE - field.getLength());
 assertEquals(dateTime.getLong() + (field.getLength() * -1), result);
 dateTime.setDateTime(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testSet_invalidChronology() {
 field = LenientDateTimeField.getInstance(new LocalDateTimeField(DateTimeFieldType.dayOfMonth(), null), null);
 chronology = PeroableDateTime.CUSTOM_CHRONOLOGY;
 try {
 field.set(new MutableDateTime().toDate(), 1);
 } catch (IllegalStateException e) {
 fail("Expected IllegalStateException but got " + e.getMessage());
 }
 }
}