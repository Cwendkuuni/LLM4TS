import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.dayOfMonth();
 private static final DurationFieldType HOURS = DurationFieldType.hourOfDay();
 private static final DurationFieldType MINUTES = DurationFieldType.minute();
 private static final DurationFieldType SECONDS = DurationFieldType.second();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);

 assertEquals(DAYS, unsupportedDays.getType());
 assertEquals(HOURS, unsupportedHours.getType());
 assertEquals(MINUTES, unsupportedMinutes.getType());
 assertEquals(SECONDS, unsupportedSeconds.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 assertFalse(unsupportedField.isSupported());
 }

 @Test
 public void testGetName() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 assertEquals("dayOfMonth", unsupportedField.getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 12345;
 unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 12345;
 unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 12345;
 long instant = 123456789;
 unsupportedField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long duration = 12345;
 long instant = 123456789;
 unsupportedField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 int value = 12345;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValue() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long value = 12345;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 int value = 12345;
 long instant = 123456789;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValueAndInstant() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long value = 12345;
 long instant = 123456789;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long instant = 123456789;
 int value = 12345;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLongValue() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long instant = 123456789;
 long value = 12345;
 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long minuendInstant = 123456789;
 long subtrahendInstant = 123456788;
 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 long minuendInstant = 123456789;
 long subtrahendInstant = 123456788;
 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 assertEquals(0, unsupportedField.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DAYS);
 assertEquals(0, unsupportedField1.compareTo(unsupportedField2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField unsupportedField3 = new UnsupportedDurationField(HOURS);
 assertTrue(unsupportedField1.equals(unsupportedField1));
 assertTrue(unsupportedField1.equals(unsupportedField2));
 assertFalse(unsupportedField1.equals(unsupportedField3));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 int hashCode = unsupportedField.hashCode();
 assertEquals(hashCode, unsupportedField.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 assertEquals("UnsupportedDurationField[dayOfMonth]", unsupportedField.toString());
 }

 @Test
 public void testReadResolve() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 UnsupportedDurationField readResolve = (UnsupportedDurationField) unsupportedField.readResolve();
 assertEquals(DAYS, readResolve.getType());
 }

}