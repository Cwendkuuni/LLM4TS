import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 UnsupportedDurationField unsupportedDayOfMonth = UnsupportedDurationField.getInstance(DurationFieldType.dayOfMonth());

 assertEquals("dayOfWeek", DurationFieldType.dayOfWeek(), unsupportedDayOfWeek.getType());
 assertEquals("dayOfMonth", DurationFieldType.dayOfMonth(), unsupportedDayOfMonth.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 long duration = 1000L;

 unsupportedDayOfWeek.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 long duration = 1000L;

 unsupportedDayOfWeek.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAtInstant() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 long duration = 1000L;
 long instant = 1234567890000L;

 unsupportedDayOfWeek.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAtInstant() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 long duration = 1000L;
 long instant = 1234567890000L;

 unsupportedDayOfWeek.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 int value = 1;

 unsupportedDayOfWeek.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAtInstant() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 long value = 1234567890000L;
 long instant = 1234567890000L;

 unsupportedDayOfWeek.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 long instant = 1234567890000L;
 int value = 1;

 unsupportedDayOfWeek.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 long instant = 1234567890000L;
 long value = 1234567890000L;

 unsupportedDayOfWeek.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 long minuendInstant = 1234567890000L;
 long subtrahendInstant = 1234567880000L;

 unsupportedDayOfWeek.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 long minuendInstant = 1234567890000L;
 long subtrahendInstant = 1234567880000L;

 unsupportedDayOfWeek.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());

 assertEquals(0, unsupportedDayOfWeek.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedDayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 DurationField test = new TestDurationField();

 assertEquals(0, unsupportedDayOfWeek.compareTo(unsupportedDayOfWeek));
 assertEquals(0, unsupportedDayOfWeek.compareTo(test));
 }
}

class TestDurationField implements DurationField {
 @Override
 public DurationFieldType getType() {
 return null;
 }

 @Override
 public String getName() {
 return "test";
 }

 @Override
 public boolean isSupported() {
 return false;
 }

 @Override
 public boolean isPrecise() {
 return true;
 }

 @Override
 public int getValue(long duration) {
 return 0;
 }

 @Override
 public long getValueAsLong(long duration) {
 return 0;
 }

 @Override
 public int getValue(long duration, long instant) {
 return 0;
 }

 @Override
 public long getValueAsLong(long duration, long instant) {
 return 0;
 }

 @Override
 public long getMillis(int value) {
 return 0;
 }

 @Override
 public long getMillis(long value) {
 return 0;
 }

 @Override
 public long getMillis(int value, long instant) {
 return 0;
 }

 @Override
 public long getMillis(long value, long instant) {
 return 0;
 }

 @Override
 public long add(long instant, int value) {
 return 0;
 }

 @Override
 public long add(long instant, long value) {
 return 0;
 }

 @Override
 public int getDifference(long minuendInstant, long subtrahendInstant) {
 return 0;
 }

 @Override
 public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
 return 0;
 }

 @Override
 public long getUnitMillis() {
 return 0;
 }

 @Override
 public int compareTo(DurationField durationField) {
 return 0;
 }
}