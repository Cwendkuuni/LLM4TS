import org.joda.time.BaseTestCase;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;

public class UnsupportedDurationFieldTest extends BaseTestCase {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HALF_HOURS = DurationFieldType.halfHours();

 /**
 * Test constructor.
 */
 @Test
 public void testConstructor() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(DAYS, unsupportedField.getType());
 }

 /**
 * Test getInstance method.
 */
 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(unsupportedField1, unsupportedField2);
 }

 /**
 * Test getName method.
 */
 @Test
 public void testGetName() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 assertEquals("days", unsupportedField.getName());
 }

 /**
 * Test isSupported method.
 */
 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 assertFalse(unsupportedField.isSupported());
 }

 /**
 * Test isPrecise method.
 */
 @Test
 public void testIsPrecise() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 assertTrue(unsupportedField.isPrecise());
 }

 /**
 * Test getValue method.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.getValue(1L);
 }

 /**
 * Test getValueAsLong method.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.getValueAsLong(1L);
 }

 /**
 * Test getValue method with instant.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueWithInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.getValue(1L, 1L);
 }

 /**
 * Test getValueAsLong method with instant.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.getValueAsLong(1L, 1L);
 }

 /**
 * Test getMillis method.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.getMillis(1);
 }

 /**
 * Test getMillis method with long value.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.getMillis(1L);
 }

 /**
 * Test getMillis method with int value and instant.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithIntValueAndInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.getMillis(1, 1L);
 }

 /**
 * Test getMillis method with long value and instant.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisWithLongValueAndInstant() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.getMillis(1L, 1L);
 }

 /**
 * Test add method.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.add(1L, 1);
 }

 /**
 * Test add method with long value.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testAddWithLongValue() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.add(1L, 1L);
 }

 /**
 * Test getDifference method.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.getDifference(1L, 1L);
 }

 /**
 * Test getDifferenceAsLong method.
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 unsupportedField.getDifferenceAsLong(1L, 1L);
 }

 /**
 * Test getUnitMillis method.
 */
 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(0, unsupportedField.getUnitMillis());
 }

 /**
 * Test compareTo method.
 */
 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(0, unsupportedField1.compareTo(unsupportedField2));
 }

 /**
 * Test equals method.
 */
 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(HALF_HOURS);
 assertTrue(unsupportedField1.equals(unsupportedField1));
 assertTrue(unsupportedField1.equals(unsupportedField2));
 assertFalse(unsupportedField1.equals(unsupportedField3));
 }

 /**
 * Test hashCode method.
 */
 @Test
 public void testHashCode() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField unsupportedField3 = UnsupportedDurationField.getInstance(HALF_HOURS);
 assertEquals(unsupportedField1.hashCode(), unsupportedField1.hashCode());
 assertEquals(unsupportedField1.hashCode(), unsupportedField2.hashCode());
 assertNotSame(unsupportedField1.hashCode(), unsupportedField3.hashCode());
 }

 /**
 * Test toString method.
 */
 @Test
 public void testToString() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 assertEquals("UnsupportedDurationField[days]", unsupportedField.toString());
 }

 /**
 * Test readResolve method.
 */
 @Test
 public void testReadResolve() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DAYS);
 assertEquals(DAYS, unsupportedField.getType());
 }

 /**
 * Test unsupported method.
 */
 @Test
 public void testUnsupported() {
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedOperationException exception = unsupportedField.unsupported();
 assertEquals("days field is unsupported", exception.getMessage());
 }
}