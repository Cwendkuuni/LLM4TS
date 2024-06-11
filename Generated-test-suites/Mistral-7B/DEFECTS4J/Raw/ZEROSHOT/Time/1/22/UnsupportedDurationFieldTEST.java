import org.joda.time.BaseDateTime;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.dayOfMonth();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();
 private static final DurationFieldType MILLISECONDS = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedFieldDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedFieldHours = UnsupportedDurationField.getInstance(HOURS);
 UnsupportedDurationField unsupportedFieldMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedFieldSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedFieldMilliseconds = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertEquals(DAYS, unsupportedFieldDays.getType());
 assertEquals(HOURS, unsupportedFieldHours.getType());
 assertEquals(MINUTES, unsupportedFieldMinutes.getType());
 assertEquals(SECONDS, unsupportedFieldSeconds.getType());
 assertEquals(MILLISECONDS, unsupportedFieldMilliseconds.getType());
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.dayOfWeek());

 assertEquals(false, unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 long duration = 123456789;

 unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 long duration = 123456789;

 unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 long instant = 123456789;
 int value = 1;

 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 long instant = 123456789;
 long value = 1;

 unsupportedField.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 long minuendInstant = 123456789;
 long subtrahendInstant = 123456788;

 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 long minuendInstant = 123456789;
 long subtrahendInstant = 123456788;

 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 DurationField supportedField = new BaseDateTime(0L).getField(MINUTES);

 assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
 assertEquals(0, unsupportedField1.compareTo(supportedField));
 assertEquals(1, supportedField.compareTo(unsupportedField1));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 UnsupportedDurationField unsupportedField3 = new UnsupportedDurationField(DurationFieldType.hours());

 assertEquals(unsupportedField1, unsupportedField1);
 assertEquals(unsupportedField1, unsupportedField2);
 assertNotEquals(unsupportedField1, unsupportedField3);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.dayOfWeek());
 UnsupportedDurationField unsupportedField3 = new UnsupportedDurationField(DurationFieldType.hours());

 assertEquals(unsupportedField1.hashCode(), unsupportedField1.hashCode());
 assertEquals(unsupportedField1.hashCode(), unsupportedField2.hashCode());
 assertNotEquals(unsupportedField1.hashCode(), unsupportedField3.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.dayOfWeek());

 assertEquals("UnsupportedDurationField[dayOfWeek]", unsupportedField.toString());
 }

}