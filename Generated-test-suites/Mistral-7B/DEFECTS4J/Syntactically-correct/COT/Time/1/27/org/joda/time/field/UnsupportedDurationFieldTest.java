import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private static final UnsupportedDurationField DAYS = UnsupportedDurationField.getInstance(DurationFieldType.days());
 private static final UnsupportedDurationField HOURS = UnsupportedDurationField.getInstance(DurationFieldType.hours());
 private static final UnsupportedDurationField MINUTES = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
 private static final UnsupportedDurationField SECONDS = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
 private static final UnsupportedDurationField MILLIS = UnsupportedDurationField.getInstance(DurationFieldType.millis());

 @Test
 public void testGetInstance() {
 for (DurationFieldType type : DurationFieldType.values()) {
 UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
 assertEquals(type, field.getType());
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 BaseDateTime baseDateTime = new BaseDateTime();
 DAYS.getValue(baseDateTime.getMillis());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 BaseDateTime baseDateTime = new BaseDateTime();
 DAYS.getValueAsLong(baseDateTime.getMillis());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 BaseDateTime baseDateTime = new BaseDateTime();
 DAYS.getValue(baseDateTime.getMillis(), baseDateTime.getMillis());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 BaseDateTime baseDateTime = new BaseDateTime();
 DAYS.getValueAsLong(baseDateTime.getMillis(), baseDateTime.getMillis());
 }

 @Test
 public void testGetMillis() {
 assertEquals(0, DAYS.getMillis(0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValue() {
 DAYS.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithInstant() {
 BaseDateTime baseDateTime = new BaseDateTime();
 DAYS.getMillis(baseDateTime.getMillis(), baseDateTime.getMillis());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 BaseDateTime baseDateTime = new BaseDateTime();
 DAYS.add(baseDateTime.getMillis(), 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithInstant() {
 BaseDateTime baseDateTime = new BaseDateTime();
 DAYS.add(baseDateTime.getMillis(), 1, baseDateTime.getMillis());
 }

 @Test
 public void testGetDifference() {
 assertEquals(0, DAYS.getDifference(0, 0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceWithValues() {
 DAYS.getDifference(1, 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 assertEquals(0, DAYS.getDifferenceAsLong(0, 0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLongWithValues() {
 DAYS.getDifferenceAsLong(1, 0);
 }

 @Test
 public void testGetUnitMillis() {
 assertEquals(0, DAYS.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 assertFalse(DAYS.compareTo(HOURS) < 0);
 assertFalse(HOURS.compareTo(DAYS) < 0);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
 UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
 assertTrue(field1.equals(field2));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
 UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
 assertEquals(field1.hashCode(), field2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("UnsupportedDurationField[days]", DAYS.toString());
 }

}