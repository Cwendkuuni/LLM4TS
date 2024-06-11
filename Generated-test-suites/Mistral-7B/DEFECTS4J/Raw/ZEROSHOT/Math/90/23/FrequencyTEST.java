import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FrequencyTest {

 @Test
 public void testDefaultConstructor() {
 Frequency frequency = new Frequency();
 assertNotNull(frequency.freqTable);
 }

 @Test
 public void testComparatorConstructor() {
 Comparator comparator = new Comparator() {
 @Override
 public int compare(Object o1, Object o2) {
 return ((Comparable<?>) o1).compareTo(o2);
 }
 };
 Frequency frequency = new Frequency(comparator);
 assertNotNull(frequency.freqTable);
 }

 @Test
 public void testToString() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.4 \t 0.4\n2 \t 2 \t 0.4 \t 0.8\nTotal: 4\n", frequency.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithInvalidArgument() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 }

 @Test
 public void testAddValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 frequency.addValue(3);
 long count = frequency.getCount(1);
 assertEquals(2, count);
 long cumulativeCount = frequency.getCumFreq(1);
 assertEquals(2, cumulativeCount);
 long cumulativePercent = frequency.getCumPct(1);
 assertEquals(0.45, cumulativePercent, 0.01);
 long totalCount = frequency.getSumFreq();
 assertEquals(7, totalCount);
 }

 @Test
 public void testAddValueWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 frequency.addValue(3L);
 long count = frequency.getCount(1);
 assertEquals(2, count);
 long cumulativeCount = frequency.getCumFreq(1);
 assertEquals(2, cumulativeCount);
 long cumulativePercent = frequency.getCumPct(1);
 assertEquals(0.45, cumulativePercent, 0.01);
 long totalCount = frequency.getSumFreq();
 assertEquals(7, totalCount);
 }

 @Test
 public void testAddValueWithChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 long count = frequency.getCount('a');
 assertEquals(2, count);
 long cumulativeCount = frequency.getCumFreq('a');
 assertEquals(2, cumulativeCount);
 long cumulativePercent = frequency.getCumPct('a');
 assertEquals(0.45, cumulativePercent, 0.01);
 long totalCount = frequency.getSumFreq();
 assertEquals(4, totalCount);
 }

 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.clear();
 assertEquals(0, frequency.getSumFreq());
 }

 @Test
 public void testValuesIterator() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 Iterator iterator = frequency.valuesIterator();
 assertEquals(1, iterator.next().longValue());
 assertEquals(1, iterator.next().longValue());
 assertEquals(2, iterator.next().longValue());
 assertEquals(2, iterator.next().longValue());
 }

 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 long totalCount = frequency.getSumFreq();
 assertEquals(7, totalCount);
 }

 @Test
 public void testGetCount() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 long count = frequency.getCount(1);
 assertEquals(2, count);
 }

 @Test
 public void testGetCountWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 long count = frequency.getCount(1);
 assertEquals(2, count);
 }

 @Test
 public void testGetCountWithChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 long count = frequency.getCount('a');
 assertEquals(2, count);
 }

 @Test
 public void testGetPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 double pct = frequency.getPct(1);
 assertEquals(0.42857142857142855, pct, 0.000000000000001);
 }

 @Test
 public void testGetPctWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 double pct = frequency.getPct(1);
 assertEquals(0.42857142857142855, pct, 0.000000000000001);
 }

 @Test
 public void testGetPctWithChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 double pct = frequency.getPct('a');
 assertEquals(0.42857142857142855, pct, 0.000000000000001);
 }

 @Test
 public void testGetCumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 long cumulativeCount = frequency.getCumFreq(1);
 assertEquals(2, cumulativeCount);
 }

 @Test
 public void testGetCumFreqWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 long cumulativeCount = frequency.getCumFreq(1);
 assertEquals(2, cumulativeCount);
 }

 @Test
 public void testGetCumFreqWithChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 long cumulativeCount = frequency.getCumFreq('a');
 assertEquals(2, cumulativeCount);
 }

 @Test
 public void testGetCumPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 double cumulativePercent = frequency.getCumPct(1);
 assertEquals(0.42857142857142855, cumulativePercent, 0.000000000000001);
 }

 @Test
 public void testGetCumPctWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 double cumulativePercent = frequency.getCumPct(1);
 assertEquals(0.42857142857142855, cumulativePercent, 0.000000000000001);
 }

 @Test
 public void testGetCumPctWithChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 double cumulativePercent = frequency.getCumPct('a');
 assertEquals(0.42857142857142855, cumulativePercent, 0.000000000000001);
 }
}