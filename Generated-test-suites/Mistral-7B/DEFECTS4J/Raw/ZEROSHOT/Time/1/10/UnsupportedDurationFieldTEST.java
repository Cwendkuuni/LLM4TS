import org.joda.time.base.AbstractBaseTest;import org.joda.time.base.BasicFieldType;import org.joda.time.duration.DurationFieldType;import org.junit.Test;import static org.junit.Assert.assertFalse;import static org.junit.Assert.assertNull;import static org.junit.Assert.assertEquals;import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest extends AbstractBaseTest {

 private static final DurationFieldType DAYS = BasicFieldType.days();
 private static final DurationFieldType MINUTES = BasicFieldType.minutes();
 private static final DurationFieldType SECONDS = BasicFieldType.seconds();
 private static final DurationFieldType MILLISECONDS = BasicFieldType.millis();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMilliseconds = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertNotNull(unsupportedDays);
 assertNotNull(unsupportedMinutes);
 assertNotNull(unsupportedSeconds);
 assertNotNull(unsupportedMilliseconds);
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMilliseconds = UnsupportedDurationField.getInstance(MILLISECONDS);

 assertFalse(unsupportedDays.isSupported());
 assertFalse(unsupportedMinutes.isSupported());
 assertFalse(unsupportedSeconds.isSupported());
 assertFalse(unsupportedMilliseconds.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;

 assertNull(testInstance.getValue(1));

 testInstance = unsupportedSeconds;
 assertNull(testInstance.getValue(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;

 assertNull(testInstance.getValueAsLong(1));

 testInstance = unsupportedSeconds;
 assertNull(testInstance.getValueAsLong(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;

 assertThrows(UnsupportedOperationException.class, () -> testInstance.getValue(1, 1));

 testInstance = unsupportedSeconds;
 assertThrows(UnsupportedOperationException.class, () -> testInstance.getValue(1, 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;

 assertThrows(UnsupportedOperationException.class, () -> testInstance.getValueAsLong(1, 1));

 testInstance = unsupportedSeconds;
 assertThrows(UnsupportedOperationException.class, () -> testInstance.getValueAsLong(1, 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;

 assertThrows(UnsupportedOperationException.class, () -> testInstance.getMillis(1));

 testInstance = unsupportedSeconds;
 assertThrows(UnsupportedOperationException.class, () -> testInstance.getMillis(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;

 assertThrows(UnsupportedOperationException.class, () -> testInstance.getMillis(1));

 test Institute = unsupportedSeconds;
 assertThrows(UnsupportedOperationException.class, () -> testInstance.getMillis(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;

 assertThrows(UnsupportedOperationException.class, () -> testInstance.getMillis(1, 1));

 testInstance = unsupportedSeconds;
 assertThrows(UnsupportedOperationException.class, () -> testInstance.getMillis(1, 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;

 assertThrows(UnsupportedOperationException.class, () -> testInstance.add(1, 1));

 testInstance = unsupportedSeconds;
 assertThrows(UnsupportedOperationException.class, () -> testInstance.add(1, 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;

 assertThrows(UnsupportedOperationException.class, () -> testInstance.add(1, 1, 1));

 testInstance = unsupportedSeconds;
 assertThrows(UnsupportedOperationException.class, () -> testInstance.add(1, 1, 1));
 }

 @Test
 public void testGetDifference() {
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedSeconds;

 assertEquals(0, testInstance.getDifference(0, 0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceWithInstant() {
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField testInstance = unsupportedSeconds;

 assertThrows(UnsupportedOperationException.class, () -> testInstance.getDifference(0, 0));
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMilliseconds = UnsupportedDurationField.getInstance(MILLISECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;

 assertEquals(0, testInstance.getUnitMillis());

 testInstance = unsupportedMinutes;
 assertEquals(60000, testInstance.getUnitMillis());

 testInstance = unsupportedSeconds;
 assertEquals(1000, testInstance.getUnitMillis());

 testInstance = unsupportedMilliseconds;
 assertEquals(1, testInstance.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedMinutes = UnsupportedDurationField.getInstance(MINUTES);
 UnsupportedDurationField unsupportedSeconds = UnsupportedDurationField.getInstance(SECONDS);
 UnsupportedDurationField unsupportedMilliseconds = UnsupportedDurationField.getInstance(MILLISECONDS);
 UnsupportedDurationField testInstance = unsupportedDays;
 UnsupportedDurationField testInstance2 = unsupportedMinutes;

 assertEquals(1, testInstance.compareTo(testInstance2));
 }

}