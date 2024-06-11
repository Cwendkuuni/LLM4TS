import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.FrequencyTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 private Frequency frequency;

 @Test
 public void testDefaultConstructor() {
 frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals("Incorrect frequency count for value 1.", 2, frequency.getCount(1));
 assertEquals("Incorrect frequency count for value 2.", 1, frequency.getCount(2));
 assertEquals("Incorrect total frequency count.", 3, frequency.getSumFreq());
 }

 @Test
 public void testConstructorWithComparator() {
 Comparator comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
 frequency = new Frequency(comparator);
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 Iterator iterator = frequency.valuesIterator();
 assertEquals("Incorrect order of values in frequency distribution.", 1, ((Comparable) iterator.next()).compareTo(1));
 assertEquals("Incorrect order of values in frequency distribution.", 1, ((Comparable) iterator.next()).compareTo(1));
 assertEquals("Incorrect order of values in frequency distribution.", 2, ((Comparable) iterator.next()).compareTo(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithNonComparableObject() {
 frequency = new Frequency();
 frequency.addValue(new Object());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithNonComparableValues() {
 frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(new Object());
 }

 @Test
 public void testClearMethod() {
 frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.clear();
 assertEquals("Clearing the frequency distribution did not reset the frequency count.", 0, frequency.getSumFreq());
 }

 @Test
 public void testGetValuesIterator() {
 frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 Iterator iterator = frequency.valuesIterator();
 assertEquals("Incorrect first value in frequency distribution.", 1, ((Comparable) iterator.next()).compareTo(1));
 assertEquals("Incorrect second value in frequency distribution.", 1, ((Comparable) iterator.next()).compareTo(1));
 assertEquals("Incorrect third value in frequency distribution.", 2, ((Comparable) iterator.next()).compareTo(1));
 assertTrue("Iterator did not return all elements in the frequency distribution.", iterator.hasNext() == false);
 }

 @Test
 public void testGetCountMethod() {
 frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 assertEquals("Incorrect frequency count for value 1.", 3, frequency.getCount(1));
 assertEquals("Incorrect frequency count for value 2.", 1, frequency.getCount(2));
 }

 @Test
 public void testGetPctMethod() {
 frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 assertEquals("Incorrect percentage of values equal to 1.", 0.6, frequency.getPct(1), 0.001);
 assertEquals("Incorrect percentage of values equal to 2.", 0.2, frequency.getPct(2), 0.001);
 }

 @Test
 public void testGetCumFreqMethod() {
 frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 assertEquals("Incorrect cumulative frequency for value 1.", 2, frequency.getCumFreq(1));
 assertEquals("Incorrect cumulative frequency for value 2.", 3, frequency.getCumFreq(2));
 }

 @Test
 public void testGetCumPctMethod() {
 frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 assertEquals("Incorrect cumulative percentage for value 1.", 0.5, frequency.getCumPct(1), 0.001);
 assertEquals("Incorrect cumulative percentage for value 2.", 0.8, frequency.getCumPct(2), 0.001);
 }

}