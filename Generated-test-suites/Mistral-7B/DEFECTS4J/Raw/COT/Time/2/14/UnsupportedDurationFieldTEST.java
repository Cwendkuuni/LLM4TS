import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private UnsupportedDurationField unsupportedField;

 @Before
 public void setup() {
 unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 }

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 assertEquals(unsupportedField1, unsupportedField2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 assertFalse(unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 long duration = 1000L;
 unsupportedField.getValue(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 long duration = 1000L;
 unsupportedField.getValueAsLong(duration);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 long duration = 1000L;
 long instant = 1234567890L;
 unsupportedField.getValue(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 long duration = 1000L;
 long instant = 1234567890L;
 unsupportedField.getValueAsLong(duration, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 int value = 10;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValue() {
 long value = 1000L;
 unsupportedField.getMillis(value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant() {
 int value = 10;
 long instant = 1234567890L;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstant2() {
 long value = 1000L;
 long instant = 1234567890L;
 unsupportedField.getMillis(value, instant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 long instant = 1234567890L;
 unsupportedField.add(instant, 10);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 long instant = 1234567890L;
 unsupportedField.add(instant, 1000L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1234567880L;
 unsupportedField.getDifference(minuendInstant, subtrahendInstant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 long minuendInstant = 1234567890L;
 long subtrahendInstant = 1234567880L;
 unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
 }

 @Test
 public void testGetUnitMillis() {
 assertEquals(0, unsupportedField.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 assertEquals(0, unsupportedField.compareTo(unsupportedField));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
 assertFalse(unsupportedField1.equals(unsupportedField3));
 assertTrue(unsupportedField1.equals(unsupportedField1));
 assertTrue(unsupportedField1.equals(unsupportedField2));
 }

 @Test
 public void testHashCode() {
 int hashCode1 = unsupportedField.hashCode();
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 assertEquals(hashCode1, unsupportedField2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("UnsupportedDurationField[DAYS]", unsupportedField.toString());
 }

 @Test
 public void testReadResolve() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
 UnsupportedDurationField unsupportedField2 = (UnsupportedDurationField) unsupportedField1.readResolve();
 assertEquals(unsupportedField1, unsupportedField2);
 }

}