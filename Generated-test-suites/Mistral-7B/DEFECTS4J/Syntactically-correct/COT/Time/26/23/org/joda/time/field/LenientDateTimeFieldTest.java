import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

 private static final Chronology UTC = Chronology.UTC;
 private static final DateTimeFieldType FIELD_TYPE = DateTimeFieldType.dayOfMonth();

 @Test
 public void testGetInstance() {
 DateTimeField field = new StrictDateTimeField(FIELD_TYPE, UTC);
 DateTimeField lenientField = LenientDateTimeField.getInstance(field, UTC);
 assertEquals(lenientField, LenientDateTimeField.getInstance(lenientField, UTC));
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceNullField() {
 LenientDateTimeField.getInstance(null, UTC);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceNullBase() {
 LenientDateTimeField.getInstance(new StrictDateTimeField(FIELD_TYPE, UTC), null);
 }

 @Test
 public void testIsLenient() {
 DateTimeField lenientField = new LenientDateTimeField(new StrictDateTimeField(FIELD_TYPE, UTC), UTC);
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSet() {
 DateTimeField field = new StrictDateTimeField(FIELD_TYPE, UTC);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, UTC);

 MutableDateTime dateTime = new MutableDateTime(2022, 1, 1, 0, 0, 0, 0, UTC);
 long oldValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = oldValue + 1;
 long expectedResult = dateTime.getLocalMilliseconds();

 lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 long result = dateTime.getLocalMilliseconds();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetNegativeValue() {
 DateTimeField field = new StrictDateTimeField(FIELD_TYPE, UTC);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, UTC);

 MutableDateTime dateTime = new MutableDateTime(2022, 1, 1, 0, 0, 0, 0, UTC);
 long oldValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = oldValue - 1;
 long expectedResult = dateTime.getLocalMilliseconds();

 lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 long result = dateTime.getLocalMilliseconds();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetMaxValue() {
 DateTimeField field = new StrictDateTimeField(FIELD_TYPE, UTC);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, UTC);

 MutableDateTime dateTime = new MutableDateTime(LocalDate.Max.getValue(), LocalTime.Max.getValue(), 0, 0, 0, 0, 0, UTC);
 long oldValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = oldValue + 1;
 long expectedResult = dateTime.getLocalMilliseconds();

 lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 long result = dateTime.getLocalMilliseconds();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSetMinValue() {
 DateTimeField field = new StrictDateTimeField(FIELD_TYPE, UTC);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, UTC);

 MutableDateTime dateTime = new MutableDateTime(LocalDate.Min.getValue(), LocalTime.Min.getValue(), 0, 0, 0, 0, 0, UTC);
 long oldValue = lenientField.get(dateTime.toLocalMilliseconds());
 int newValue = oldValue - 1;
 long expectedResult = dateTime.getLocalMilliseconds();

 lenientField.set(dateTime.toLocalMilliseconds(), newValue);
 long result = dateTime.getLocalMilliseconds();
 assertEquals(expectedResult, result);
 }
}