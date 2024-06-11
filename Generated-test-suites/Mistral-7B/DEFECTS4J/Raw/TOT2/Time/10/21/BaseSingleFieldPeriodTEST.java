import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 // Test method for BaseSingleFieldPeriod construction
 @Test
 public void testBaseSingleFieldPeriodConstruction() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
 DurationFieldType fieldType = DurationFieldType.days();
 assertEquals(fieldType, period.getFieldType());
 assertEquals(7, period.getValue());
 }

 // Test method for getting the single duration field type
 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
 DurationFieldType expectedFieldType = DurationFieldType.days();
 DurationFieldType actualFieldType = period.getFieldType();
 assertEquals(expectedFieldType, actualFieldType);
 }

 // Test method for getting the period type which matches the duration field type
 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
 PeriodType expectedPeriodType = PeriodType.standard();
 PeriodType actualPeriodType = period.getPeriodType();
 assertEquals(expectedPeriodType, actualPeriodType);
 }

 // Test method for getting the number of fields that this period supports
 @Test
 public void testSize() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
 int expectedSize = 1;
 int actualSize = period.size();
 assertEquals(expectedSize, actualSize);
 }

 // Test method for getting the value of a duration field represented by this period
 @Test
 public void testGet() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
 DurationFieldType fieldType = DurationFieldType.days();
 int expectedValue = 7;
 int actualValue = period.get(fieldType);
 assertEquals(expectedValue, actualValue);
 }

 // Test method for checking whether the duration field is supported by this period
 @Test
 public void testIsSupported() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
 DurationFieldType fieldType = DurationFieldType.days();
 boolean expectedResult = true;
 boolean actualResult = period.isSupported(fieldType);
 assertEquals(expectedResult, actualResult);
 }

 // Test method for converting this period to a Period object
 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
 Period expectedPeriod = new Period(7, 0, 0, 0);
 Period actualPeriod = period.toPeriod();
 assertEquals(expectedPeriod, actualPeriod);
 }

 // Test method for converting this period to a MutablePeriod object
 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
 MutablePeriod expectedMutablePeriod = new MutablePeriod(7, 0, 0, 0);
 MutablePeriod actualMutablePeriod = period.toMutablePeriod();
 assertEquals(expectedMutablePeriod, actualMutablePeriod);
 }

 // Test method for checking equality based on the value of each field
 @Test
 public void testEquals() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);
 BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(8);
 BaseSingleFieldPeriod period4 = null;
 assertEquals(period1, period1); // tests self-reference
 assertEquals(period1, period2); // tests equal values
 assertNotEquals(period1, period3); // tests not equal values
 assertNull(period1.equals(period4)); // tests null
 }

 // Test method for testing hash code
 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);
 BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(8);
 BaseSingleFieldPeriod period4 = null;
 int expectedHashCode1 = 17 * 27 + 7; // 273
 int expectedHashCode2 = 17 * 27 + 7; // 273
 int expectedHashCode3 = 17 * 27 + 8; // 274
 int expectedHashCode4 = 0;
 int actualHashCode1 = period1.hashCode();
 int actualHashCode2 = period2.hashCode();
 int actualHashCode3 = period3.hashCode();
 int actualHashCode4 = period4.hashCode();
 assertEquals(expectedHashCode1, actualHashCode1);
 assertEquals(expectedHashCode2, actualHashCode2);
 assertNotEquals(expectedHashCode1, actualHashCode3);
 assertNotEquals(expectedHashCode1, actualHashCode4);
 }

 // Test method for comparing this period to another object of the same class
 @Test
 public void testCompareTo() {
 BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7);
 BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);
 BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(8);
 int expectedResult1 = 0;
 int expectedResult2 = 0;
 int expectedResult3 = -1;
 int actualResult1 = period1.compareTo(period1);
 int actualResult2 = period1.compareTo(period2);
 int actualResult3 = period1.compareTo(period3);
 assertEquals(expectedResult1, actualResult1);
 assertEquals(expectedResult2, actualResult2);
 assertEquals(expectedResult3, actualResult3);
 }

 // Test method for edge cases: minimum and maximum values
 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCasesMinimum() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEdgeCasesMaximum() {
 BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(Integer.MAX_VALUE);
 }
}