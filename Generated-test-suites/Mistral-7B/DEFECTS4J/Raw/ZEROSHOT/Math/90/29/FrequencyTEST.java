import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 @Test
 public void testDefaultConstructor() {
 Frequency frequency = new Frequency();
 assertNotNull(frequency);
 }

 @Test
 public void testComparatorConstructor() {
 Comparator comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
 Frequency frequency = new Frequency(comparator);
 assertNotNull(frequency);
 }

 @Test
 public void testToString() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333 \n2 \t 2 \t 0.3333333333333333 \t 0.6666666666666667 \n";
 String actual = frequency.toString();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue("notComparable");
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testAddValueWithInteger() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 long count = frequency.getCount(1);
 assertEquals(1, count);
 }

 @Test
 public void testAddValueWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 long count = frequency.getCount(1L);
 assertEquals(1, count);
 }

 @Test
 public void testAddValueWithCharacter() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 long count = frequency.getCount('a');
 assertEquals(1, count);
 }

 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.clear();
 long count = frequency.getCount(1);
 assertEquals(0, count);
 }

 @Test
 public void testValuesIterator() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 Iterator<Object> iterator = frequency.valuesIterator();
 assertEquals(1, iterator.next().intValue());
 assertEquals(1, iterator.next().intValue());
 assertEquals(2, iterator.next().intValue());
 assertEquals(2, iterator.next().intValue());
 }

 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 long sumFreq = frequency.getSumFreq();
 assertEquals(4, sumFreq);
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
 long count2 = frequency.getCount(2);
 assertEquals(2, count2);
 long count3 = frequency.getCount('a');
 assertEquals(0, count3);
 }

 @Test
 public void testGetPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 double pct = frequency.getPct(1);
 assertEquals(0.3333333333333333, pct, 0.0000001);
 double pct2 = frequency.getPct(2);
 assertEquals(0.3333333333333333, pct2, 0.0000001);
 double pct3 = frequency.getPct('a');
 assertEquals(Double.NaN, pct3);
 }

 @Test
 public void testGetCumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 long cumFreq = frequency.getCumFreq(1);
 assertEquals(2, cumFreq);
 long cumFreq2 = frequency.getCumFreq(2);
 assertEquals(4, cumFreq2);
 long cumFreq3 = frequency.getCumFreq('a');
 assertEquals(0, cumFreq3);
 }

 @Test
 public void testGetCumPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 double cumPct = frequency.getCumPct(1);
 assertEquals(0.3333333333333333, cumPct, 0.0000001);
 double cumPct2 = frequency.getCumPct(2);
 assertEquals(0.6666666666666667, cumPct2, 0.0000001);
 double cumPct3 = frequency.getCumPct('a');
 assertEquals(Double.NaN, cumPct3);
 }
}