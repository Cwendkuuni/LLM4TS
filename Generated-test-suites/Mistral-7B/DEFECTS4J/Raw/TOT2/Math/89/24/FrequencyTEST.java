import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 @Test
 public void testFrequencyBasicFunctionality() {
 Frequency frequency = new Frequency();
 // Test adding values and getting frequencies
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 // Test getting frequencies
 assertEquals(2, frequency.getCount(1));
 assertEquals(2, frequency.getCount(2));
 assertEquals(0, frequency.getCount(3));
 // Test getting cumulative frequencies
 assertEquals(2, frequency.getCumFreq(1));
 assertEquals(4, frequency.getCumFreq(2));
 assertEquals(4, frequency.getCumFreq(3));
 // Test getting cumulative percentages
 assertEquals(0.5, frequency.getPct(1), 0.01);
 assertEquals(0.5, frequency.getPct(2), 0.01);
 assertEquals(0.0, frequency.getPct(3), 0.01);
 // Test getting the percentage cumulative distribution
 assertEquals(0.5, frequency.getCumPct(1), 0.01);
 assertEquals(1.0, frequency.getCumPct(2), 0.01);
 assertEquals(1.0, frequency.getCumPct(3), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithInvalidValueType() {
 Frequency frequency = new Frequency();
 frequency.addValue(new Object()); // should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithInvalidValues() {
 Frequency frequency = new Frequency();
 // Add valid values
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 // Attempt to add an invalid value
 frequency.addValue("invalidValue"); // should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithNonComparableValues() {
 Frequency frequency = new Frequency();
 // Add valid values
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 // Attempt to add non-comparable values
 frequency.addValue(new Object()); // should throw an IllegalArgumentException
 frequency.addValue(new Integer(1)); // should throw an IllegalArgumentException
 frequency.addValue(new Double(1.0)); // should throw an IllegalArgumentException
 }

 @Test
 public void testClearMethod() {
 Frequency frequency = new Frequency();
 // Test adding and removing values
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 frequency.clear();
 // Test getting frequencies after clearing
 assertEquals(0, frequency.getCount(1));
 assertEquals(0, frequency.getCount(2));
 }

 @Test
 public void testGetSumFreqMethod() {
 Frequency frequency = new Frequency();
 // Test adding values and getting sum frequency
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 long sumFreq = frequency.getSumFreq();
 assertEquals(4, sumFreq);
 }

 @Test
 public void testValuesIteratorMethod() {
 Frequency frequency = new Frequency();
 // Test adding values and getting values iterator
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 Iterator<Comparable<?>> iterator = frequency.valuesIterator();
 // Test iterator has expected values
 assertTrue(iterator.hasNext());
 assertEquals(1, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(1, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(2, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(2, iterator.next());
 // Test iterator has no more values
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testComparator() {
 Frequency frequency = new Frequency(new NaturalComparator());
 // Test adding values
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 // Test getting frequencies with comparator
 assertEquals(2, frequency.getCount(1));
 assertEquals(2, frequency.getCount(2));
 // Test getting cumulative frequencies with comparator
 assertEquals(2, frequency.getCumFreq(1));
 assertEquals(4, frequency.getCumFreq(2));
 }
}