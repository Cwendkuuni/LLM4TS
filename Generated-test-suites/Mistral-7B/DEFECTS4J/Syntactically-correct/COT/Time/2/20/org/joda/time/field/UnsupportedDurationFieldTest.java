import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 private DurationFieldType type = DurationFieldType.MILLISECONDS;

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 UnsupportedDurationField another = UnsupportedDurationField.getInstance(type);
 assertEquals(unsupported, another);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 assertFalse(unsupported.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 long duration = 1234567L;
 unsupported.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 long duration = 1234567L;
 unsupported.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue_long_long() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 long duration = 1234567L;
 long instant = 123456789012345L;
 unsupported.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong_long_long() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 long duration = 1234567L;
 long instant = 123456789012345L;
 unsupported.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_int() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 int value = 123;
 unsupported.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 long value = 123456789012345L;
 unsupported.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_int_long() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 int value = 123;
 long instant = 123456789012345L;
 unsupported.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis_long_long() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 long value = 123456789012345L;
 long instant = 123456789012345L;
 unsupported.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_int() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 long instant = 123456789012345L;
 int value = 123;
 unsupported.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_long_long() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 long instant = 123456789012345L;
 long value = 1234567L;
 unsupported.add(instant, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference_long_long() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 long minuendInstant = 123456789012345L;
 long subtrahendInstant = 123456789012342L;
 unsupported.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong_long_long() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 long minuendInstant = 123456789012345L;
 long subtrahendInstant = 123456789012342L;
 unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 assertEquals(0, unsupported.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 DurationField another = DurationFieldType.MILLISECONDS;
 assertEquals(0, unsupported.compareTo(another));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 assertEquals(unsupported, unsupported);
 UnsupportedDurationField another = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
 assertFalse(unsupported.equals(another));
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 assertEquals(type.hashCode(), unsupported.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
 assertEquals("UnsupportedDurationField[MILLISECONDS]", unsupported.toString());
 }

 @Test
 public void testReadResolve() {
 UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
 UnsupportedDurationField readResolve = (UnsupportedDurationField) unsupported.readResolve();
 assertEquals(unsupported, readResolve);
 }

}