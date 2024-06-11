import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 private DurationFieldType dayOfWeekInMonth = DurationFieldType.dayOfWeekInMonth();
 private DurationFieldType dayOfYear = DurationFieldType.dayOfYear();
 private DurationFieldType millisecond = DurationFieldType.millis();
 private UnsupportedDurationField unsupportedDayOfWeekInMonth;
 private UnsupportedDurationField unsupportedDayOfYear;
 private UnsupportedDurationField unsupportedMillisecond;

 @Before
 public void setUp() {
 unsupportedDayOfWeekInMonth = UnsupportedDurationField.getInstance(dayOfWeekInMonth);
 unsupportedDayOfYear = UnsupportedDurationField.getInstance(dayOfYear);
 unsupportedMillisecond = UnsupportedDurationField.getInstance(millisecond);
 }

 @Test
 public void testGetInstance() {
 assertEquals(dayOfWeekInMonth, unsupportedDayOfWeekInMonth.getType());
 assertEquals(dayOfYear, unsupportedDayOfYear.getType());
 assertEquals(millisecond, unsupportedMillisecond.getType());
 }

 @Test
 public void testIsSupported() {
 assertEquals(false, unsupportedDayOfWeekInMonth.isSupported());
 assertEquals(false, unsupportedDayOfYear.isSupported());
 assertEquals(false, unsupportedMillisecond.isSupported());
 }

 @Test
 public void testGetName() {
 assertEquals("dayOfWeekInMonth", unsupportedDayOfWeekInMonth.getName());
 assertEquals("dayOfYear", unsupportedDayOfYear.getName());
 assertEquals("millis", unsupportedMillisecond.getName());
 }

 @Test
 public void testGetValue_LongThrowsException() {
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillisecond.getValue(1L));
 }

 @Test
 public void testGetValueAsLong_LongThrowsException() {
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillisecond.getValueAsLong(1L));
 }

 @Test
 public void testAdd_LongLongThrowsException() {
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillisecond.add(1L, 1L));
 }

 @Test
 public void testGetDifference_LongLongThrowsException() {
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillisecond.getDifference(1L, 2L));
 }

 @Test
 public void testGetDifferenceAsLong_LongLongThrowsException() {
 assertThrows(UnsupportedOperationException.class, () -> unsupportedMillisecond.getDifferenceAsLong(1L, 2L));
 }

 @Test
 public void testGetUnitMillis() {
 assertEquals(0, unsupportedMillisecond.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 assertEquals(0, unsupportedMillisecond.compareTo(unsupportedMillisecond));
 }

 @Test
 public void testEquals_Object() {
 UnsupportedDurationField unsupportedMillisecondCopy = UnsupportedDurationField.getInstance(millisecond);
 assertEquals(unsupportedMillisecond, unsupportedMillisecondCopy);
 }

 @Test
 public void testHashCode() {
 int hashCode = unsupportedMillisecond.hashCode();
 assertEquals(hashCode, unsupportedMillisecond.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("UnsupportedDurationField[millis]", unsupportedMillisecond.toString());
 }

}