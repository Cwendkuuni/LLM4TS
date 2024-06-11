import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 // Test constructor
 @Test
 public void testFrequencyConstructor() {
 Frequency frequency = new Frequency();
 assertNotNull(frequency);
 }

 // Test addValue method with integral values
 @Test
 public void testAddValueInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 long count = frequency.getCount(1);
 assertEquals(3, count);
 }

 // Test addValue method with long value
 @Test
 public void testAddValueLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(2L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 long count = frequency.getCount(2L);
 assertEquals(3, count);
 }

 // Test addValue method with char value
 @Test
 public void testAddValueChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('a');
 long count = frequency.getCount('a');
 assertEquals(3, count);
 }

 // Test addValue method with non-comparable value
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(new Object());
 }

 // Test clear method
 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.clear();
 long count = frequency.getCount(1);
 assertEquals(0, count);
 }

 // Test valuesIterator method
 @Test
 public void testValuesIterator() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 Iterator iterator = frequency.valuesIterator();
 assertEquals(1, iterator.next().longValue());
 assertEquals(2, iterator.next().longValue());
 assertEquals(1, iterator.next().longValue());
 }

 // Test getSumFreq method
 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 long sumFreq = frequency.getSumFreq();
 assertEquals(4, sumFreq);
 }

 // Test getCount method with integral values
 @Test
 public void testGetCountInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 long count = frequency.getCount(1);
 assertEquals(3, count);
 }

 // Test getCount method with long value
 @Test
 public void testGetCountLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(2L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 long count = frequency.getCount(2L);
 assertEquals(3, count);
 }

 // Test getCount method with char value
 @Test
 public void testGetCountChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('a');
 long count = frequency.getCount('a');
 assertEquals(3, count);
 }

 // Test getCount method with non-comparable value
 @Test
 public void testGetCountNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 long count = frequency.getCount(new Object());
 assertEquals(0, count);
 }

 // Test getPct method with integral values
 @Test
 public void testGetPctInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 double pct = frequency.getPct(1);
 assertEquals(0.6, pct, 0.01);
 }

 // Test getPct method with long value
 @Test
 public void testGetPctLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(2L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 double pct = frequency.getPct(2L);
 assertEquals(0.75, pct, 0.01);
 }

 // Test getPct method with char value
 @Test
 public void testGetPctChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('a');
 double pct = frequency.getPct('a');
 assertEquals(0.75, pct, 0.01);
 }

 // Test getPct method with non-comparable value
 @Test
 public void testGetPctNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 double pct = frequency.getPct(new Object());
 assertEquals(Double.NaN, pct);
 }

 // Test getCumFreq method with integral values
 @Test
 public void testGetCumFreqInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 long cumFreq = frequency.getCumFreq(1);
 assertEquals(3, cumFreq);
 }

 // Test getCumFreq method with long value
 @Test
 public void testGetCumFreqLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(2L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 long cumFreq = frequency.getCumFreq(2L);
 assertEquals(6, cumFreq);
 }

 // Test getCumFreq method with char value
 @Test
 public void testGetCumFreqChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('a');
 long cumFreq = frequency.getCumFreq('a');
 assertEquals(3, cumFreq);
 }

 // Test getCumFreq method with non-comparable value
 @Test
 public void testGetCumFreqNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 long cumFreq = frequency.getCumFreq(new Object());
 assertEquals(0, cumFreq);
 }

 // Test getCumPct method with integral values
 @Test
 public void testGetCumPctInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 double cumPct = frequency.getCumPct(1);
 assertEquals(0.75, cumPct, 0.01);
 }

 // Test getCumPct method with long value
 @Test
 public void testGetCumPctLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(2L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 double cumPct = frequency.getCumPct(2L);
 assertEquals(1.0, cumPct, 0.01);
 }

 // Test getCumPct method with char value
 @Test
 public void testGetCumPctChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('a');
 double cumPct = frequency.getCumPct('a');
 assertEquals(1.0, cumPct, 0.01);
 }

 // Test getCumPct method with non-comparable value
 @Test
 public void testGetCumPctNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 double cumPct = frequency.getCumPct(new Object());
 assertEquals(Double.NaN, cumPct);
 }
}