import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 // Test constructor
 @Test
 public void testFrequencyConstructor() {
 Frequency frequency = new Frequency();
 }

 // Test addValue method with integer value
 @Test
 public void testAddValueInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 long actual = frequency.getCount(1);
 long expected = 2;
 assertEquals(expected, actual);
 }

 // Test addValue method with long value
 @Test
 public void testAddValueLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(1L);
 long actual = frequency.getCount(1L);
 long expected = 2;
 assertEquals(expected, actual);
 }

 // Test addValue method with char value
 @Test
 public void testAddValueChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 long actual = frequency.getCount('a');
 long expected = 2;
 assertEquals(expected, actual);
 }

 // Test addValue method with non-comparable value
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueNonComparable() {
 Frequency frequency = new Frequency();
 frequency.addValue(new Object());
 }

 // TesttoString method
 @Test
 public void testToString() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 String actual = frequency.toString();
 String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.1666666666666667 \t 0.4999999999999999\n";
 assertEquals(expected, actual);
 }

 // Test clear method
 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.clear();
 long actual = frequency.getSumFreq();
 long expected = 0;
 assertEquals(expected, actual);
 }

 // Test valuesIterator method
 @Test
 public void testValuesIterator() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 Iterator valuesIterator = frequency.valuesIterator();
 long actual = 0;
 while (valuesIterator.hasNext()) {
 long value = ((Long) valuesIterator.next()).longValue();
 actual += value;
 }
 long expected = 3;
 assertEquals(expected, actual);
 }

 // Test getSumFreq method
 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 long actual = frequency.getSumFreq();
 long expected = 4;
 assertEquals(expected, actual);
 }

 // Test getCount method with integer value
 @Test
 public void testGetCountInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 long actual = frequency.getCount(1);
 long expected = 2;
 assertEquals(expected, actual);
 }

 // Test getCount method with long value
 @Test
 public void testGetCountLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(1L);
 long actual = frequency.getCount(1L);
 long expected = 2;
 assertEquals(expected, actual);
 }

 // Test getCount method with char value
 @Test
 public void testGetCountChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 long actual = frequency.getCount('a');
 long expected = 2;
 assertEquals(expected, actual);
 }

 // Test getCount method with non-comparable value
 @Test
 public void testGetCountNonComparable() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 long actual = frequency.getCount(new Object());
 long expected = 0;
 assertEquals(expected, actual);
 }

 // Test getPct method with integer value
 @Test
 public void testGetPctInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 double actual = frequency.getPct(1);
 double expected = 0.3333333333333333;
 assertEquals(expected, actual, 0.00001);
 }

 // Test getPct method with long value
 @Test
 public void testGetPctLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(1L);
 double actual = frequency.getPct(1L);
 double expected = 0.3333333333333333;
 assertEquals(expected, actual, 0.00001);
 }

 // Test getPct method with char value
 @Test
 public void testGetPctChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 double actual = frequency.getPct('a');
 double expected = 0.3333333333333333;
 assertEquals(expected, actual, 0.00001);
 }

 // Test getPct method with non-comparable value
 @Test
 public void testGetPctNonComparable() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 double actual = frequency.getPct(new Object());
 double expected = Double.NaN;
 assertEquals(expected, actual, 0.0);
 }

 // Test getCumFreq method with integer value
 @Test
 public void testGetCumFreqInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 long actual = frequency.getCumFreq(1);
 long expected = 2;
 assertEquals(expected, actual);
 }

 // Test getCumFreq method with long value
 @Test
 public void testGetCumFreqLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(1L);
 long actual = frequency.getCumFreq(1L);
 long expected = 2;
 assertEquals(expected, actual);
 }

 // Test getCumFreq method with char value
 @Test
 public void testGetCumFreqChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 long actual = frequency.getCumFreq('a');
 long expected = 2;
 assertEquals(expected, actual);
 }

 // Test getCumFreq method with non-comparable value
 @Test
 public void testGetCumFreqNonComparable() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 long actual = frequency.getCumFreq(new Object());
 long expected = 0;
 assertEquals(expected, actual);
 }

 // Test getCumPct method with integer value
 @Test
 public void testGetCumPctInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 double actual = frequency.getCumPct(1);
 double expected = 0.3333333333333333;
 assertEquals(expected, actual, 0.00001);
 }

 // Test getCumPct method with long value
 @Test
 public void testGetCumPctLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(1L);
 double actual = frequency.getCumPct(1L);
 double expected = 0.3333333333333333;
 assertEquals(expected, actual, 0.00001);
 }

 // Test getCumPct method with char value
 @Test
 public void testGetCumPctChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 double actual = frequency.getCumPct('a');
 double expected = 0.3333333333333333;
 assertEquals(expected, actual, 0.00001);
 }

 // Test getCumPct method with non-comparable value
 @Test
 public void testGetCumPctNonComparable() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 double actual = frequency.getCumPct(new Object());
 double expected = Double.NaN;
 assertEquals(expected, actual, 0.0);
 }

 // Test NaturalComparator class
 @Test
 public void testNaturalComparator() {
 Comparator naturalComparator = new Frequency.NaturalComparator();
 int result = naturalComparator.compare(1, 2);
 int expected = -1;
 assertEquals(expected, result);
 result = naturalComparator.compare(2, 1);
 expected = 1;
 assertEquals(expected, result);
 result = naturalComparator.compare(1, 1);
 expected = 0;
 assertEquals(expected, result);
 result = naturalComparator.compare('a', 'z');
 expected = -1;
 assertEquals(expected, result);
 result = naturalComparator.compare('z', 'a');
 expected = 1;
 assertEquals(expected, result);
 result = naturalComparator.compare('a', 'a');
 expected = 0;
 assertEquals(expected, result);
 }
}