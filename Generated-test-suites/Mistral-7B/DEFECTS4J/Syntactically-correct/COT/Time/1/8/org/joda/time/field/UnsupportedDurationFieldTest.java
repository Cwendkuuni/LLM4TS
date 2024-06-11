import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

 private DurationField unsupportedField;

 @Before
 public void setup() {
 unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
 }

 @Test
 public void testGetInstance() {
 DurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
 DurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
 assertEquals(unsupportedField1, unsupportedField2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 assertEquals(false, unsupportedField.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 unsupportedField.getValue(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 unsupportedField.getValueAsLong(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAt() {
 unsupportedField.getValue(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLongAt() {
 unsupportedField.getValueAsLong(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 unsupportedField.getMillis(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisAt() {
 unsupportedField.getMillis(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 unsupportedField.add(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 unsupportedField.add(1, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 unsupportedField.getDifference(1, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 unsupportedField.getDifferenceAsLong(1, 1);
 }

 @Test
 public void testCompareTo() {
 DurationField supportedField = DurationField.standardFixedLengthMilliseconds(100);
 assertEquals(1, unsupportedField.compareTo(supportedField));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
 assertEquals(unsupportedField1, unsupportedField2);
 }

 @Test
 public void testHashCode() {
 String name = "HOURS";
 UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
 UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
 assertEquals(name.hashCode(), unsupportedField1.hashCode());
 assertEquals(name.hashCode(), unsupportedField2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("UnsupportedDurationField[HOURS]", unsupportedField.toString());
 }

}