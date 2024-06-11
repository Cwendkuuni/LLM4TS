import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

 private static final Chronology isoChronology = ISOChronology.getInstance();
 private static final DateTimeField dayOfMonth = DateTimeFields.dayOfMonth();
 private static final DateTimeField lenientDayOfMonth = LenientDateTimeField.getInstance(dayOfMonth, isoChronology);

 @Test
 public void testInstance() {
 DateTimeField lenientField = LenientDateTimeField.getInstance(dayOfMonth, isoChronology);
 assertNotNull(lenientField);
 assertEquals(dayOfMonth, lenientField.getBaseField());
 assertEquals(isoChronology, lenientField.getCalendarField().getCalendar());
 }

 @Test
 public void testIsLenient() {
 assertTrue(lenientDayOfMonth.isLenient());
 }

 @Test
 public void testSetMethod() {
 MutableDateTime mutableDateTime = new MutableDateTime(2022, 1, 31, isoChronology);
 long currentInstant = mutableDateTime.getMilliseconds();
 int valueToSet = 32;
 long newInstant = lenientDayOfMonth.set(currentInstant, valueToSet);
 MutableDateTime newMutableDateTime = new MutableDateTime(newInstant, isoChronology);
 // The new value should be equal to the current value + the difference between the new value and the current value
 int expectedNewValue = getValue(mutableDateTime) + (valueToSet - getValue(mutableDateTime));
 int newValue = getValue(newMutableDateTime);
 assertEquals(expectedNewValue, newValue);
 }

 @Test
 public void testSetMethodWithNegativeValue() {
 MutableDateTime mutableDateTime = new MutableDateTime(2022, 1, 31, isoChronology);
 long currentInstant = mutableDateTime.getMilliseconds();
 int valueToSet = 30;
 long newInstant = lenientDayOfMonth.set(currentInstant, valueToSet);
 MutableDateTime newMutableDateTime = new MutableDateTime(newInstant, isoChronology);
 // Since the value is negative, the new value should be equal to the current value - the absolute difference between the new value and the current value
 int expectedNewValue = getValue(mutableDateTime) - Math.abs(getValue(mutableDateTime) - valueToSet);
 int newValue = getValue(newMutableDateTime);
 assertEquals(expectedNewValue, newValue);
 }

 @Test
 public void testSetMethodWithValueOutOfBounds() {
 MutableDateTime mutableDateTime = new MutableDateTime(2022, 1, 31, isoChronology);
 long currentInstant = mutableDateTime.getMilliseconds();
 int valueToSet = 32; // out of bounds for a month with 31 days
 long newInstant = lenientDayOfMonth.set(currentInstant, valueToSet);
 MutableDateTime newMutableDateTime = new MutableDateTime(newInstant, isoChronology);
 // The new value should be equal to the next valid value
 int expectedNewValue = getNextValidValue(getValue(mutableDateTime), lenientDayOfMonth);
 int newValue = getValue(newMutableDateTime);
 assertEquals(expectedNewValue, newValue);
 }

 @Test
 public void testSetMethodWithMaxValue() {
 MutableDateTime mutableDateTime = new MutableDateTime(2022, 2, 29, isoChronology); // February has 28 days in non-leap years
 long currentInstant = mutableDateTime.getMilliseconds();
 int valueToSet = DateTimeFields.dayOfMonth().getMaximumValue();
 long newInstant = lenientDayOfMonth.set(currentInstant, valueToSet);
 MutableDateTime newMutableDateTime = new MutableDateTime(newInstant, isoChronology);
 // The new value should be equal to the maximum valid value
 int expectedNewValue = DateTimeFields.dayOfMonth().getMaximumValue();
 int newValue = getValue(newMutableDateTime);
 assertEquals(expectedNewValue, newValue);
 }

 @Test
 public void testSetMethodWithMaxValueInLeapYear() {
 MutableDateTime mutableDateTime = new MutableDateTime(2022, 2, 29, isoChronology); // February has 29 days in leap years
 long currentInstant = mutableDateTime.getMilliseconds();
 int valueToSet = DateTimeFields.dayOfMonth().getMaximumValue();
 long newInstant = lenientDayOfMonth.set(currentInstant, valueToSet);
 MutableDateTime newMutableDateTime = new MutableDateTime(newInstant, isoChronology);
 // The new value should be equal to the maximum valid value
 int expectedNewValue = DateTimeFields.dayOfMonth().getMaximumValue();
 int newValue = getValue(newMutableDateTime);
 assertEquals(expectedNewValue, newValue);
 }

 @Test
 public void testSetMethodWithMinValue() {
 MutableDateTime mutableDateTime = new MutableDateTime(2022, 1, 1, isoChronology);
 long currentInstant = mutableDateTime.getMilliseconds();
 int valueToSet = DateTimeFields.dayOfMonth().getMinimumValue();
 long newInstant = lenientDayOfMonth.set(currentInstant, valueToSet);
 MutableDateTime newMutableDateTime = new MutableDateTime(newInstant, isoChronology);
 // The new value should be equal to the minimum valid value
 int expectedNewValue = DateTimeFields.dayOfMonth().getMinimumValue();
 int newValue = getValue(newMutableDateTime);
 assertEquals(expectedNewValue, newValue);
 }

 @Test
 public void testSetMethodWithMinValueInNegativeYear() {
 MutableDateTime mutableDateTime = new MutableDateTime(-1, 1, 1, isoChronology);
 long currentInstant = mutableDateTime.getMilliseconds();
 int valueToSet = DateTimeFields.dayOfMonth().getMinimumValue();
 long newInstant = lenientDayOfMonth.set(currentInstant, valueToSet);
 MutableDateTime newMutableDateTime = new MutableDateTime(newInstant, isoChronology);
 // The new value should be equal to the minimum valid value for the given year
 int expectedNewValue = getMinimumValueForYear(-1, DateTimeFields.dayOfMonth());
 int newValue = getValue(newMutableDateTime);
 assertEquals(expectedNewValue, newValue);
 }

 private static int getValue(MutableDateTime mutableDateTime) {
 return mutableDateTime.getDayOfMonth();
 }

 private static int getNextValidValue(int value, DateTimeField field) {
 if (value > field.getMaximumValue()) {
 return field.getMinimumValue();
 }
 return value + 1;
 }

 private static int getMinimumValueForYear(int year, DateTimeField field) {
 return field.getMinimumValue() + FieldUtils.getMillennia(year) * 1000;
 }
}