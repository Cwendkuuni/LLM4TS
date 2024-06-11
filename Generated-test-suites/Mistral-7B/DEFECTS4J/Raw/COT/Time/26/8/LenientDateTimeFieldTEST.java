import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private DateTimeField field;
 private Chronology chronology;
 private LenientDateTimeField lenientField;

 @Before
 public void setUp() {
 field = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), 30, null);
 chronology = new org.joda.time.local.LocalDateTimeUtils.LocalDateTimeChronology();
 lenientField = LenientDateTimeField.getInstance(field, chronology);
 }

 @Test
 public void testGetInstance() {
 DateTimeField lenientFieldFromInstance = LenientDateTimeField.getInstance(field, chronology);
 assertSame(lenientField, lenientFieldFromInstance);
 }

 @Test
 public void testIsLenient() {
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSet_validValues() {
 long instant = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0, chronology).toInstant().getMillis();
 MutableDateTime mutableDateTime = new MutableDateTime(instant, chronology);

 int value = lenientField.get(mutableDateTime.toLocalDateTime().toDate());
 long newInstant = lenientField.set(mutableDateTime.toLocalDateTime().toDate().getTime(), value + 5);
 mutableDateTime.setInstant(newInstant);

 assertEquals(value + 5, lenientField.get(mutableDateTime.toLocalDateTime().toDate()));
 }

 @Test
 public void testSet_valueOutOfBounds() {
 long instant = new LocalDateTime(2022, 1, 1, 0, 0, 0, 0, chronology).toInstant().getMillis();
 MutableDateTime mutableDateTime = new MutableDateTime(instant, chronology);

 // Test setting a value smaller than the minimum value
 int minValue = lenientField.getMinimumValue();
 long newInstant = lenientField.set(mutableDateTime.toLocalDateTime().toDate().getTime(), minValue - 1);
 mutableDateTime.setInstant(newInstant);

 assertEquals(minValue, lenientField.get(mutableDateTime.toLocalDateTime().toDate()));

 // Test setting a value larger than the maximum value
 int maxValue = lenientField.getMaximumValue();
 long newInstant = lenientField.set(mutableDateTime.toLocalDateTime().toDate().getTime(), maxValue + 1);
 mutableDateTime.setInstant(newInstant);

 assertEquals(maxValue, lenientField.get(mutableDateTime.toLocalDateTime().toDate()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_invalidArgument() {
 MutableDateTime mutableDateTime = new MutableDateTime(System.currentTimeMillis(), chronology);
 lenientField.set(mutableDateTime.toLocalDateTime().toDate().getTime(), Integer.MIN_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_nullArgument() {
 lenientField.set(null, 0);
 }
}