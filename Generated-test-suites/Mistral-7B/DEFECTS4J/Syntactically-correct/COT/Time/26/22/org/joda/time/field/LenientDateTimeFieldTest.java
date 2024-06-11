import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Assert;
import org.junit.Test;

public class LenientDateTimeFieldTest {

 private static final Chronology UTC = Chronology.UTC;

 @Test
 public void testGetInstance() {
 DateTimeField field = DateTimeFieldType.dayOfMonth();
 LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, UTC);
 Assert.assertSame(lenientField, LenientDateTimeField.getInstance(lenientField, UTC));
 Assert.assertNotSame(lenientField, DateTimeFieldType.dayOfMonth());
 }

 @Test
 public void testIsLenient() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), UTC);
 Assert.assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSet_ValidValues() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), UTC);
 MutableDateTime dateTime = new MutableDateTime(2022, 1, 1, 0, 0, 0, 0, UTC);
 long currentValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = currentValue + 1;
 long expectedValue = currentValue + 1;
 long result = lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 Assert.assertEquals(expectedValue, result);
 }

 @Test
 public void testSet_OutOfBoundsValues() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), UTC);
 MutableDateTime dateTime = new MutableDateTime(2022, 1, 1, 0, 0, 0, 0, UTC);
 long currentValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = currentValue - 1;
 long expectedValue = currentValue + 30; // 30 days difference
 long result = lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 Assert.assertEquals(expectedValue, result);
 }

 @Test
 public void testSet_NegativeValues() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), UTC);
 MutableDateTime dateTime = new MutableDateTime(2022, 1, 1, 0, 0, 0, 0, UTC);
 long currentValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = currentValue - 31; // 31 days difference in the past
 long result = lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 long expectedValue = result - (31 * 24 * 60 * 60 * 1000); // 31 days difference in the past
 Assert.assertEquals(expectedValue, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_NullDateTime() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), UTC);
 lenientField.set(null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_NullValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), UTC);
 lenientField.set(new BaseDateTime(0L, UTC), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_InvalidValue() {
 LenientDateTimeField lenientField = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), UTC);
 MutableDateTime dateTime = new MutableDateTime(2022, 1, 1, 0, 0, 0, 0, UTC);
 lenientField.set(dateTime.toLocalMilliseconds(), 32);
 }
}