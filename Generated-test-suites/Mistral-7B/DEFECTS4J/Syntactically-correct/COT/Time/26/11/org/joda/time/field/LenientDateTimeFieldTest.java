import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.DateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private static final Chronology base = org.joda.time.DateTimeUtils.forUTC();

 @Test
 public void testGetInstance() {
 DateTimeField field = DateTimeFieldType.dayOfMonth();
 LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
 assertTrue(lenientField.isLenient());
 assertEquals(field, lenientField.getDelegate());
 }

 @Test
 public void testSet_ValidValues() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), base);
 MutableDateTime mutableDateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, base);
 long instant = mutableDateTime.getLongValue();

 int value = 1;
 long result = lenientField.set(instant, value);
 mutableDateTime.set(lenientField, value);
 assertEquals(instant + value, result);

 value = 10;
 result = lenientField.set(instant, value);
 mutableDateTime.set(lenientField, value);
 assertEquals(instant + (value - get(instant)), result);
 }

 @Test
 public void testSet_NegativeValues() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), base);
 MutableDateTime mutableDateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, base);
 long instant = mutableDateTime.getLongValue();

 int value = -1;
 long result = lenientField.set(instant, value);
 mutableDateTime.set(lenientField, value);
 assertEquals(instant + value, result);

 value = -10;
 result = lenientField.set(instant, value);
 mutableDateTime.set(lenientField, value);
 assertEquals(instant + (value + get(instant)), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_NullField() {
 LenientDateTimeField lenientField = new LenientDateTimeField(null, base);
 MutableDateTime mutableDateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, base);
 long instant = mutableDateTime.getLongValue();
 lenientField.set(instant, 1);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testSet_NullChronology() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), null);
 MutableDateTime mutableDateTime = new MutableDateTime(0, 0, 0, 0, 0, 0, 0, base);
 long instant = mutableDateTime.getLongValue();
 lenientField.set(instant, 1);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testIsLenient() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), base);
 assertTrue(lenientField.isLenient());
 }
}