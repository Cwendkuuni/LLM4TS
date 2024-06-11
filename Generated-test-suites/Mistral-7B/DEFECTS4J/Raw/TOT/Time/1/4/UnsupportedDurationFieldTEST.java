import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnsupportedDurationFieldTest {

 @Test
 public void testGetInstance() {
 DurationFieldType type = DurationFieldType.INSTANCE_DAY_OF_MONTH;
 UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
 assertEquals(type, unsupportedField.getType());
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertFalse(unsupportedField.isSupported());
 }

 @Test
 public void testGetName() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertEquals("dayOfMonth", unsupportedField.getName());
 }

 @Test
 public void testGetValue_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L));
 }

 @Test
 public void testGetValueAsLong_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1L));
 }

 @Test
 public void testGetValue_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L, 1L));
 }

 @Test
 public void testGetValueAsLong_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1L, 1L));
 }

 @Test
 public void testGetMillis_int() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1));
 }

 @Test
 public void testGetMillis_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L));
 }

 @Test
 public void testGetMillis_int_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1, 1L));
 }

 @Test
 public void testGetMillis_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L, 1L));
 }

 @Test
 public void testAdd_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1L));
 }

 @Test
 public void testAdd_long_int() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1));
 }

 @Test
 public void testGetDifference_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifference(1L, 1L));
 }

 @Test
 public void testGetDifferenceAsLong_long_long() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH);
 assertThrows(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(1L, 1L));
 }

 @Test
 public void testGetUnitMillis() {
 UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_ofMONTH); // Note: case sensitivity
 assertEquals(0, unsupportedField.getUnitMillis());
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.INSTANCE_DAY_OF_MONTH); UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.INSTANCE_HOURS());
 assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
 }
}