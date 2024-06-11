import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FrequencyTest {

 @Test
 public void testDefaultConstructor() {
 Frequency frequency = new Frequency();
 assertNotNull(frequency);
 }

 @Test
 public void testComparatorConstructor() {
 Comparator comparator = (o1, o2) -> ((Comparable<?>) o1).compareTo(o2);
 Frequency frequency = new Frequency(comparator);
 assertNotNull(frequency);
 }

 @Test
 public void testToString() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 String result = frequency.toString();
 String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.25 \t 0.5833333333333333\n\n";
 assertEquals(expected, result);
 }

 @Test
 public void testAddValueObject() {
 Frequency frequency = new Frequency();
 frequency.addValue("test");
 assertNull(frequency.getCount("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueObjectInvalid() {
 Frequency frequency = new Frequency();
 frequency.addValue('A');
 frequency.addValue(1);
 }

 @Test
 public void testAddValueComparable() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(2, frequency.getCount(1));
 }

 @Test
 public void testAddValueInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(3, frequency.getCount(1));
 }

 @Test
 public void testAddValueLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(1L);
 assertEquals(3, frequency.getCount(1L));
 }

 @Test
 public void testAddValueChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('A');
 frequency.addValue('A');
 frequency.addValue('B');
 assertEquals(2, frequency.getCount('A'));
 assertEquals(1, frequency.getCount('B'));
 }

 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.clear();
 assertNull(frequency.getCount(1));
 }

 @Test
 public void testValuesIterator() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 Iterator<Object> iterator = frequency.valuesIterator();
 Object[] values = new Object[3];
 for (int i = 0; i < 3; i++) {
 values[i] = iterator.next();
 }
 assertArrayEquals(new Object[] {1L, 1L, 1L}, values);
 }

 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(3, frequency.getSumFreq());
 }

 @Test
 public void testGetCount() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(3, frequency.getCount(1));
 }

 @Test
 public void testGetCountChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('A');
 frequency.addValue('A');
 frequency.addValue('B');
 assertEquals(2, frequency.getCount('A'));
 }

 @Test
 public void testGetCountInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(3, frequency.getCount(1));
 }

 @Test
 public void testGetCountLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(1L);
 assertEquals(3, frequency.getCount(1L));
 }

 @Test
 public void testGetPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(0.3333333333333333, frequency.getPct(1), 0.000001);
 }

 @Test
 public void testGetPctChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('A');
 frequency.addValue('A');
 frequency.addValue('B');
 assertEquals(0.5, frequency.getPct('A'), 0.001);
 }

 @Test
 public void testGetPctInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(0.3333333333333333, frequency.getPct(1), 0.000001);
 }

 @Test
 public void testGetPctLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(1L);
 assertEquals(0.3333333333333333, frequency.getPct(1L), 0.000001);
 }

 @Test
 public void testGetCumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(1, frequency.getCumFreq(1));
 assertEquals(2, frequency.getCumFreq(2));
 assertEquals(3, frequency.getCumFreq(3));
 }

 @Test
 public void testGetCumFreqChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('A');
 frequency.addValue('A');
 frequency.addValue('B');
 assertEquals(1, frequency.getCumFreq('A'));
 assertEquals(2, frequency.getCumFreq('A'));
 assertEquals(3, frequency.getCumFreq('B'));
 }

 @Test
 public void testGetCumFreqInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(1, frequency.getCumFreq(1));
 assertEquals(2, frequency.getCumFreq(2));
 assertEquals(3, frequency.getCumFreq(3));
 }

 @Test
 public void testGetCumFreqLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(1L);
 assertEquals(1, frequency.getCumFreq(1L));
 assertEquals(2, frequency.getCumFreq(2L));
 assertEquals(3, frequency.getCumFreq(3L));
 }

 @Test
 public void testGetCumPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(0.3333333333333333, frequency.getPct(1), 0.000001);
 assertEquals(0.6, frequency.getPct(2), 0.001);
 assertEquals(1, frequency.getPct(3), 0.001);
 }

 @Test
 public void testGetCumPctChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('A');
 frequency.addValue('A');
 frequency.addValue('B');
 assertEquals(0.5, frequency.getPct('A'), 0.001);
 assertEquals(0.6, frequency.getPct('A'), 0.001);
 assertEquals(1, frequency.getPct('B'), 0.001);
 }

 @Test
 public void testGetCumPctInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(0.3333333333333333, frequency.getPct(1), 0.000001);
 assertEquals(0.6, frequency.getPct(2), 0.001);
 assertEquals(1, frequency.getPct(3), 0.001);
 }

 @Test
 public void testGetCumPctLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(1L);
 assertEquals(0.3333333333333333, frequency.getPct(1L), 0.000001);
 assertEquals(0.6, frequency.getPct(2L), 0.001);
 assertEquals(1, frequency.getPct(3L), 0.001);
 }
}