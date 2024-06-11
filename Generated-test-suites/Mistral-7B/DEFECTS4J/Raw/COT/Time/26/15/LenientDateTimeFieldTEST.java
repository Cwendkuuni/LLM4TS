import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.FieldUtils;
import org.joda.time.MutableDateTime;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

 private static final Chronology iBase = DateTimeFields.UTC;
 private static final DateTimeField field = DateTimeFields.MILLI_SECONDS;

 @Test
 public void testGetInstance() {
 DateTimeField lenientField = LenientDateTimeField.getInstance(field, iBase);
 assertEquals(lenientField.getClass(), LenientDateTimeField.class);
 }

 @Test
 public void testIsLenient() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 assertEquals(lenientField.isLenient(), true);
 }

 @Test
 public void testSetOutOfBoundsValue() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);

 long instant = 0L;
 long newValue = 1000; // out of bounds value
 long expectedResult = 1001; // the new value plus the current value

 MutableDateTime dateTime = new MutableDateTime(instant, iBase);
 long result = lenientField.set(instant, (int) newValue);

 assertEquals(result, expectedResult);
 assertEquals(lenientField.get(instant), newValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNullField() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 lenientField.set(null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNullValue() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 lenientField.set(0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeValue() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 lenientField.set(-1, 1);
 }

 @Test
 public void testSetValueAlreadySet() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 lenientField.set(0, 1);
 long result = lenientField.set(0, 1);
 assertEquals(result, 1);
 }

 @Test
 public void testSetValueSameAsInstant() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 long result = lenientField.set(0, 0);
 assertEquals(result, 0);
 }

 @Test
 public void testSetValueZero() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 long result = lenientField.set(0, 0);
 assertEquals(result, 0);
 }

 @Test
 public void testSetValueEqualToMaxValue() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 long maxValue = Long.MAX_VALUE;
 long result = lenientField.set(Long.MAX_VALUE, (int) maxValue);
 assertEquals(result, Long.MAX_VALUE);
 }

 @Test
 public void testSetValueEqualToMinValue() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 long minValue = Long.MIN_VALUE;
 long result = lenientField.set(Long.MIN_VALUE, (int) minValue);
 assertEquals(result, Long.MIN_VALUE);
 }

 @Test
 public void testSetValueGreaterThanMaxValue() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 long instant = Long.MAX_VALUE;
 long newValue = Long.MAX_VALUE + 1;
 long expectedResult = Long.MAX_VALUE + 1;

 MutableDateTime dateTime = new MutableDateTime(instant, iBase);
 long result = lenientField.set(instant, (int) newValue);

 assertEquals(result, expectedResult);
 assertEquals(lenientField.get(instant), newValue);
 }

 @Test
 public void testSetValueLessThanMinValue() {
 DateTimeField lenientField = new LenientDateTimeField(field, iBase);
 long instant = Long.MIN_VALUE;
 long newValue = Long.MIN_VALUE - 1;
 long expectedResult = Long.MIN_VALUE - 1;

 MutableDateTime dateTime = new MutableDateTime(instant, iBase);
 long result = lenientField.set(instant, (int) newValue);

 assertEquals(result, expectedResult);
 assertEquals(lenientField.get(instant), newValue);
 }
}