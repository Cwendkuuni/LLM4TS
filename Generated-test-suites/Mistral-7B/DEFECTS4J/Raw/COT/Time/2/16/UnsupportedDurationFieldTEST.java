import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.days();
 private static final DurationFieldType HALF_HOURS = DurationFieldType.halfHours();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());
 UnsupportedDurationField unsupportedHalfHours = UnsupportedDurationField.getInstance(DurationFieldType.halfHours());

 Assert.assertSame(unsupportedDays, UnsupportedDurationField.getInstance(DurationFieldType.days()));
 Assert.assertSame(unsupportedHalfHours, UnsupportedDurationField.getInstance(DurationFieldType.halfHours()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsSupported() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 Assert.assertFalse(unsupportedDays.isSupported());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsPrecise() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 Assert.assertTrue(unsupportedDays.isPrecise());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValue() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.getValue(1L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValueAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.getValueAsLong(1L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValueWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.getValue(1L, 1234567890123L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetValueAsLongWithInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.getValueAsLong(1L, 1234567890123L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.getMillis(1);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetMillisWithLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.getMillis(1L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetMillisWithIntAndInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.getMillis(1, 1234567890123L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetMillisWithLongAndInstant() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.getMillis(1L, 1234567890123L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testadd() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.add(1234567890123L, 1);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testaddWithLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.add(1234567890123L, 1L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetDifference() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.getDifference(1234567890123L, 1234567890124L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testgetDifferenceAsLong() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 try {
 unsupportedDays.getDifferenceAsLong(1234567890123L, 1234567890124L);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testgetUnitMillis() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 Assert.assertEquals(0, unsupportedDays.getUnitMillis());
 }

 @Test
 public void testcompareTo() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());
 UnsupportedDurationField unsupportedHalfHours = UnsupportedDurationField.getInstance(DurationFieldType.halfHours());

 Assert.assertEquals(0, unsupportedDays.compareTo(unsupportedHalfHours));
 }

 @Test
 public void testequals() {
 UnsupportedDurationField unsupportedDays1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
 UnsupportedDurationField unsupportedDays2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
 UnsupportedDurationField unsupportedHalfHours = UnsupportedDurationField.getInstance(DurationFieldType.halfHours());

 Assert.assertTrue(unsupportedDays1.equals(unsupportedDays1));
 Assert.assertTrue(unsupportedDays1.equals(unsupportedDays2));
 Assert.assertFalse(unsupportedDays1.equals(unsupportedHalfHours));
 }

 @Test
 public void testhashCode() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 Assert.assertEquals(DAYS.hashCode(), unsupportedDays.hashCode());
 }

 @Test
 public void testtoString() {
 UnsupportedDurationField unsupportedDays = UnsupportedDurationField.getInstance(DurationFieldType.days());

 Assert.assertEquals("UnsupportedDurationField[days]", unsupportedDays.toString());
 }

 @Test
 public void testreadResolve() {
 UnsupportedDurationField unsupportedDays = new UnsupportedDurationField(DurationFieldType.days());

 Assert.assertSame(UnsupportedDurationField.getInstance(DurationFieldType.days()), unsupportedDays);
 }

}