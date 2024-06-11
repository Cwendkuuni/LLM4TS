import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

 private UnsupportedDurationField unsupportedField_dayOfMonth;
 private UnsupportedDurationField unsupportedField_dayOfWeek;
 private UnsupportedDurationField unsupportedField_hourOfDay;

 @Before
 public void setUp() {
 unsupportedField_dayOfMonth = UnsupportedDurationField.getInstance(DurationFieldType.dayOfMonth());
 unsupportedField_dayOfWeek = UnsupportedDurationField.getInstance(DurationFieldType.dayOfWeek());
 unsupportedField_hourOfDay = UnsupportedDurationField.getInstance(DurationFieldType.hourOfDay());
 }

 @Test
 public void testGetType() {
 assertEquals(DurationFieldType.dayOfMonth(), unsupportedField_dayOfMonth.getType());
 assertEquals(DurationFieldType.dayOfWeek(), unsupportedField_dayOfWeek.getType());
 assertEquals(DurationFieldType.hourOfDay(), unsupportedField_hourOfDay.getType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 assertFalse(unsupportedField_dayOfMonth.isSupported());
 assertFalse(unsupportedField_dayOfWeek.isSupported());
 assertFalse(unsupportedField_hourOfDay.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 unsupportedField_dayOfMonth.getValue(1);
 unsupportedField_dayOfWeek.getValue(1);
 unsupportedField_hourOfDay.getValue(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 unsupportedField_dayOfMonth.getValueAsLong(1);
 unsupportedField_dayOfWeek.getValueAsLong(1);
 unsupportedField_hourOfDay.getValueAsLong(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstants() {
 unsupportedField_dayOfMonth.getValue(1, 1);
 unsupportedField_dayOfWeek.getValue(1, 1);
 unsupportedField_hourOfDay.getValue(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstants() {
 unsupportedField_dayOfMonth.getValueAsLong(1, 1);
 unsupportedField_dayOfWeek.getValueAsLong(1, 1);
 unsupportedField_hourOfDay.getValueAsLong(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 unsupportedField_dayOfMonth.getMillis(1);
 unsupportedField_dayOfWeek.getMillis(1);
 unsupportedField_hourOfDay.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValue() {
 unsupportedField_dayOfMonth.getMillis(1);
 unsupportedField_dayOfWeek.getMillis(1);
 unsupportedField_hourOfDay.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithValueAndInstants() {
 unsupportedField_dayOfMonth.getMillis(1, 1);
 unsupportedField_dayOfWeek.getMillis(1, 1);
 unsupportedField_hourOfDay.getMillis(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 unsupportedField_dayOfMonth.add(1, 1);
 unsupportedField_dayOfWeek.add(1, 1);
 unsupportedField_hourOfDay.add(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 unsupportedField_dayOfMonth.getDifference(1, 1);
 unsupportedField_dayOfWeek.getDifference(1, 1);
 unsupportedField_hourOfDay.getDifference(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 unsupportedField_dayOfMonth.getDifferenceAsLong(1, 1);
 unsupportedField_dayOfWeek.getDifferenceAsLong(1, 1);
 unsupportedField_hourOfDay.getDifferenceAsLong(1, 1);
 }

 @Test
 public void testGetUnitMillis() {
 assertEquals(0, unsupportedField_dayOfMonth.getUnitMillis());
 assertEquals(0, unsupportedField_dayOfWeek.getUnitMillis());
 assertEquals(0, unsupportedField_hourOfDay.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 DurationField supportedField = new ConstantDurationField(DurationFieldType.MINUTES, 60);
 int result = unsupportedField_dayOfMonth.compareTo(supportedField);
 int result2 = supportedField.compareTo(unsupportedField_dayOfMonth);
 int result3 = unsupportedField_dayOfMonth.compareTo(unsupportedField_dayOfMonth);
 assertEquals(1, result);
 assertEquals(-1, result2);
 assertEquals(0, result3);
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField identical = UnsupportedDurationField.getInstance(DurationFieldType.dayOfMonth());
 UnsupportedDurationField different = UnsupportedDurationField.getInstance(DurationFieldType.hourOfDay());
 UnsupportedDurationField sameNameButDifferentType = UnsupportedDurationField.getInstance(DurationFieldType.dayOfMonth());
 assertNotSame(unsupportedField_dayOfMonth, unsupportedField_dayOfWeek);
 assertNotSame(unsupportedField_dayOfMonth, unsupportedField_hourOfDay);
 assertTrue(unsupportedField_dayOfMonth.equals(unsupportedField_dayOfMonth));
 assertTrue(unsupportedField_dayOfMonth.equals(identical));
 assertFalse(unsupportedField_dayOfMonth.equals(unsupportedField_dayOfWeek));
 assertFalse(unsupportedField_dayOfMonth.equals(unsupportedField_hourOfDay));
 assertFalse(unsupportedField_dayOfMonth.equals(different));
 assertFalse(unsupportedField_dayOfMonth.equals(sameNameButDifferentType));
 }

 @Test
 public void testHashCode() {
 int hashCode1 = unsupportedField_dayOfMonth.hashCode();
 int hashCode2 = unsupportedField_dayOfMonth.hashCode();
 int hashCode3 = unsupportedField_dayOfWeek.hashCode();
 int hashCode4 = unsupportedField_dayOfWeek.hashCode();
 int hashCode5 = unsupportedField_hourOfDay.hashCode();
 int hashCode6 = unsupportedField_hourOfDay.hashCode();
 assertNotSame(hashCode1, hashCode3);
 assertNotSame(hashCode1, hashCode5);
 assertEquals(hashCode1, hashCode2);
 assertEquals(hashCode3, hashCode4);
 assertEquals(hashCode5, hashCode6);
 }

 @Test
 public void testToString() {
 assertEquals("UnsupportedDurationField[dayOfMonth]", unsupportedField_dayOfMonth.toString());
 assertEquals("UnsupportedDurationField[dayOfWeek]", unsupportedField_dayOfWeek.toString());
 assertEquals("UnsupportedDurationField[hourOfDay]", unsupportedField_hourOfDay.toString());
 }

 @Test
 public void testSingletonSerialization() {
 UnsupportedDurationField unsupportedField_dayOfMonth_serial = null;
 try {
 byte[] bytes = new ObjectOutputStream(new ByteArrayOutputStream()).writeObjects(unsupportedField_dayOfMonth);
 unsupportedField_dayOfMonth_serial = (UnsupportedDurationField) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 } catch (Exception e) {
 e.printStackTrace();
 }
 assertEquals(unsupportedField_dayOfMonth, unsupportedField_dayOfMonth_serial);
 }

}