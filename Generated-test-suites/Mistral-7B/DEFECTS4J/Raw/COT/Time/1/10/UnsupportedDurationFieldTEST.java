import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 private final DurationFieldType DAYS = DurationFieldType.days();
 private final DurationFieldType HALF_HOURS = DurationFieldType.halfHours();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHalfHours = UnsupportedDurationField.getInstance(HALF_HOURS);

 assertEquals("UnsupportedDurationField[days]", unsupportedDays.toString());
 assertEquals("UnsupportedDurationField[halfHours]", unsupportedHalfHours.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 assertEquals(false, unsupportedDays.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 unsupportedDays.getValue(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 unsupportedDays.getValueAsLong(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 unsupportedDays.getValue(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 unsupportedDays.getValueAsLong(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 unsupportedDays.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 unsupportedDays.getMillis(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 unsupportedDays.add(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAtInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 unsupportedDays.add(1, 1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 unsupportedDays.getDifference(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);

 unsupportedDays.getDifferenceAsLong(1, 1);
 }

 @Test
 public void testCompareToSupportedField() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 DurationField supportedMinutes = DurationField.standardMinutes();

 assertEquals(1, unsupportedDays.compareTo(supportedMinutes));
 }

 @Test
 public void testCompareToUnsupportedField() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(DurationFieldType.hours());

 assertEquals(0, unsupportedDays.compareTo(unsupportedHours));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedDays1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedDays2 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(DurationFieldType.hours());

 assertEquals(unsupportedDays1, unsupportedDays1);
 assertEquals(unsupportedDays1, unsupportedDays2);
 assertNotEquals(unsupportedDays1, unsupportedHours);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(DurationFieldType.hours());

 assertEquals(unsupportedDays.hashCode(), unsupportedDays.hashCode());
 assertNotEquals(unsupportedDays.hashCode(), unsupportedHours.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);UnsupportedDurationField unsupportedHours = UnsupportedDurationField.getInstance(DurationFieldType.hours());

 assertEquals("UnsupportedDurationField[days]", unsupportedDays.toString());
 assertEquals("UnsupportedDurationField[hours]", unsupportedHours.toString());
 }
}