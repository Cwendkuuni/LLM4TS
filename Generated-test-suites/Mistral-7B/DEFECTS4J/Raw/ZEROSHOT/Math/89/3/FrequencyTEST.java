import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;

public class FrequencyTest {

 @Test
 public void testDefaultConstructor() {
 Frequency frequency = new Frequency();
 }

 @Test
 public void testComparatorConstructor() {
 Comparator comparator = new Comparator() {
 @Override
 public int compare(Object o1, Object o2) {
 return ((Comparable) o1).compareTo(o2);
 }
 };
 Frequency frequency = new Frequency(comparator);
 }

 @Test
 public void testToString() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 frequency.addValue(3);
 frequency.addValue(3);
 assertEquals(
 "Value \t Freq. \t Pct. \t Cum Pct.\n" +
 "1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n" +
 "2 \t 2 \t 0.3333333333333333 \t 0.6666666666666667\n" +
 "3 \t 2 \t 0.3333333333333333 \t 1.0\n",
 frequency.toString()
 );
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(new Object());
 }

 @Test
 public void testAddValueWithInteger() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 long totalCount = frequency.getSumFreq();
 assertEquals(6, totalCount);
 long countAtOne = frequency.getCount(1);
 assertEquals(2, countAtOne);
 double pctAtOne = frequency.getPct(1);
 assertEquals(0.3333333333333333, pctAtOne, 0.0000001);
 long cumFreqAtOne = frequency.getCumFreq(1);
 assertEquals(2, cumFreqAtOne);
 double cumPctAtOne = frequency.getCumPct(1);
 assertEquals(0.3333333333333333, cumPctAtOne, 0.0000001);
 }

 @Test
 public void testAddValueWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 frequency.addValue(3L);
 frequency.addValue(3L);
 long totalCount = frequency.getSumFreq();
 assertEquals(6, totalCount);
 long countAtOne = frequency.getCount(1L);
 assertEquals(2, countAtOne);
 double pctAtOne = frequency.getPct(1L);
 assertEquals(0.3333333333333333, pctAtOne, 0.0000001);
 long cumFreqAtOne = frequency.getCumFreq(1L);
 assertEquals(2, cumFreqAtOne);
 double cumPctAtOne = frequency.getCumPct(1L);
 assertEquals(0.3333333333333333, cumPctAtOne, 0.0000001);
 }

 @Test
 public void testAddValueWithCharacter() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 frequency.addValue('c');
 frequency.addValue('c');
 long totalCount = frequency.getSumFreq();
 assertEquals(6, totalCount);
 long countAtA = frequency.getCount('a');
 assertEquals(2, countAtA);
 double pctAtA = frequency.getPct('a');
 assertEquals(0.3333333333333333, pctAtA, 0.0000001);
 long cumFreqAtA = frequency.getCumFreq('a');
 assertEquals(2, cumFreqAtA);
 double cumPctAtA = frequency.getCumPct('a');
 assertEquals(0.3333333333333333, cumPctAtA, 0.0000001);
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
 frequency.addValue(3);
 frequency.addValue(3);
 Iterator values = frequency.valuesIterator();
 assertEquals(1, values.next().longValue());
 assertEquals(1, values.next().longValue());
 assertEquals(2, values.next().longValue());
 assertEquals(2, values.next().longValue());
 assertEquals(3, values.next().longValue());
 assertEquals(3, values.next().longValue());
 assertEquals(null, values.next());
 }

 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 frequency.addValue(3);
 frequency.addValue(3);
 long totalCount = frequency.getSumFreq();
 assertEquals(6, totalCount);
 }

 @Test
 public void testGetCount() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 frequency.addValue(3);
 frequency.addValue(3);
 long countAtOne = frequency.getCount(1);
 assertEquals(2, countAtOne);
 long countAtTwo = frequency.getCount(2);
 assertEquals(2, countAtTwo);
 long countAtThree = frequency.getCount(3);
 assertEquals(2, countAtThree);
 long countAtFour = frequency.getCount(4);
 assertEquals(0, countAtFour);
 }

 @Test
 public void testGetPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 frequency.addValue(3);
 frequency.addValue(3);
 double pctAtOne = frequency.getPct(1);
 assertEquals(0.3333333333333333, pctAtOne, 0.0000001);
 double pctAtTwo = frequency.getPct(2);
 assertEquals(0.3333333333333333, pctAtTwo, 0.0000001);
 double pctAtThree = frequency.getPct(3);
 assertEquals(0.3333333333333333, pctAtThree, 0.0000001);
 double pctAtFour = frequency.getPct(4);
 assertEquals(0.0, pctAtFour, 0.0000001);
 }

 @Test
 public void testGetCumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 frequency.addValue(3);
 frequency.addValue(3);
 long cumFreqAtOne = frequency.getCumFreq(1);
 assertEquals(1, cumFreqAtOne);
 long cumFreqAtTwo = frequency.getCumFreq(2);
 assertEquals(3, cumFreqAtTwo);
 long cumFreqAtThree = frequency.getCumFreq(3);
 assertEquals(6, cumFreqAtThree);
 long cumFreqAtFour = frequency.getCumFreq(4);
 assertEquals(6, cumFreqAtFour);
 }

 @Test
 public void testGetCumPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 frequency.addValue(3);
 frequency.addValue(3);
 double cumPctAtOne = frequency.getCumPct(1);
 assertEquals(0.3333333333333333, cumPctAtOne, 0.0000001);
 double cumPctAtTwo = frequency.getCumPct(2);
 assertEquals(0.5, cumPctAtTwo, 0.0000001);
 double cumPctAtThree = frequency.getCumPct(3);
 assertEquals(1.0, cumPctAtThree, 0.0000001);
 double cumPctAtFour = frequency.getCumPct(4);
 assertEquals(1.0, cumPctAtFour, 0.0000001);
 }
}