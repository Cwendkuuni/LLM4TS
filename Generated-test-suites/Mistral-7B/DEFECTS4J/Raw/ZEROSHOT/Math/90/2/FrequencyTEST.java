import org.apache.commons.math3.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

public class FrequencyTest {

 @Test
 public void testDefaultConstructor() {
 Frequency frequency = new Frequency();
 Assert.assertEquals(0, frequency.getSumFreq());
 Assert.assertNull(frequency.toString());
 }

 @Test
 public void testComparatorConstructor() {
 Comparator comparator = (o1, o2) -> o1.compareTo(o2);
 Frequency frequency = new Frequency(comparator);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);

 Assert.assertEquals(3, frequency.getSumFreq());
 Assert.assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.6666666666666666 \t 0.6666666666666666\n2 \t 1 \t 0.3333333333333333 \t 1.0\n", frequency.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithUncomparableValues() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('a');
 }

 @Test
 public void testAddValueWithComparableValues() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);

 Assert.assertEquals(4, frequency.getSumFreq());
 Assert.assertEquals(2, frequency.getCount(1));
 Assert.assertEquals(1, frequency.getCount(2));
 Assert.assertEquals(0, frequency.getCount('a'));
 }

 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.clear();
 Assert.assertEquals(0, frequency.getSumFreq());
 Assert.assertNull(frequency.toString());
 }

 @Test
 public void testValuesIterator() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);

 Iterator<Object> iterator = frequency.valuesIterator();
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(1, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(1, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(1, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(2, iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);

 Assert.assertEquals(5, frequency.getSumFreq());
 }

 @Test
 public void testGetCount() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);

 Assert.assertEquals(3, frequency.getCount(1));
 Assert.assertEquals(2, frequency.getCount(2));
 Assert.assertEquals(0, frequency.getCount('a'));
 }

 @Test
 public void testGetPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);

 Assert.assertEquals(0.6, frequency.getPct(1), 0.01);
 Assert.assertEquals(0.4, frequency.getPct(2), 0.01);
 Assert.assertEquals(Double.NaN, frequency.getPct('a'), 0.0);
 }

 @Test
 public void testGetCumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);

 Assert.assertEquals(1, frequency.getCumFreq(Integer.MIN_VALUE));
 Assert.assertEquals(1, frequency.getCumFreq(0));
 Assert.assertEquals(2, frequency.getCumFreq(1));
 Assert.assertEquals(3, frequency.getCumFreq(2));
 Assert.assertEquals(5, frequency.getCumFreq(Integer.MAX_VALUE));
 }

 @Test
 public void testGetCumPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);

 Assert.assertEquals(0.2, frequency.getCumPct(Integer.MIN_VALUE), 0.01);
 Assert.assertEquals(0.4, frequency.getCumPct(1), 0.01);
 Assert.assertEquals(0.6, frequency.getCumPct(2), 0.01);
 Assert.assertEquals(1.0, frequency.getCumPct(Integer.MAX_VALUE), 0.01);
 }
}