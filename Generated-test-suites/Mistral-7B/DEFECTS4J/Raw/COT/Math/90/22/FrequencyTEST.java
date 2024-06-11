import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 private Frequency frequency;

 @Before
 public void setup() {
 frequency = new Frequency();
 }

 @Test
 public void testAddValue() {
 // Test adding an int value
 frequency.addValue(1);
 assertEquals(1, frequency.getCount(1));
 // Test adding a long value
 frequency.addValue(2L);
 assertEquals(2, frequency.getCount(1L));
 assertEquals(2, frequency.getCount(2L));
 // Test adding a char value
 frequency.addValue('a');
 assertEquals(1, frequency.getCount('a'));
 // Test adding a String value (should throw an IllegalArgumentException)
 try {
 frequency.addValue("test");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAddValueWithComparator() {
 // Create a Frequency object with a custom Comparator
 Comparator<Object> comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
 Frequency frequencyWithComparator = new Frequency(comparator);
 // Test adding values in a different order
 frequencyWithComparator.addValue(2);
 frequencyWithComparator.addValue(1);
 assertEquals(1, frequencyWithComparator.getCount(1));
 assertEquals(1, frequencyWithComparator.getCount(2));
 }

 @Test
 public void testClear() {
 frequency.clear();
 assertTrue(frequency.getSumFreq() == 0);
 }

 @Test
 public void testGetSumFreq() {
 // Test getting sum frequency before adding values
 assertEquals(0, frequency.getSumFreq());
 // Test getting sum frequency after adding values
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 assertEquals(4, frequency.getSumFreq());
 }

 @Test
 public void testGetCount() {
 // Test getting count of a value that has not been added
 assertEquals(0, frequency.getCount(1));
 // Test getting count of a value that has been added
 frequency.addValue(1);
 assertEquals(1, frequency.getCount(1));
 }

 @Test
 public void testGetCountWithDifferentTypes() {
 // Test getting count of an int value
 frequency.addValue(1);
 assertEquals(1, frequency.getCount(1));
 // Test getting count of a long value
 frequency.addValue(2L);
 assertEquals(1, frequency.getCount(2L));
 // Test getting count of a char value
 frequency.addValue('a');
 assertEquals(1, frequency.getCount('a'));
 // Test getting count of an Integer value
 frequency.addValue(new Integer(1));
 assertEquals(2, frequency.getCount(1));
 // Test getting count of a Long value
 frequency.addValue(new Long(2));
 assertEquals(2, frequency.getCount(2L));
 // Test getting count of a Character value
 frequency.addValue(new Character('a'));
 assertEquals(2, frequency.getCount('a'));
 }

 @Test
 public void testGetPct() {
 // Test getting percentage before adding values
 assertEquals(0, frequency.getPct(1), 0.0);
 // Test getting percentage after adding values
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 double pct = frequency.getPct(1);
 double sumFreq = frequency.getSumFreq();
 assertEquals(pct, 1.0 / sumFreq, 0.01);
 }

 @Test
 public void testGetPctWithDifferentTypes() {
 // Test getting percentage of an int value
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 double pct = frequency.getPct(1);
 double sumFreq = frequency.getSumFreq();
 assertEquals(pct, 1.0 / sumFreq, 0.01);
 // Test getting percentage of a long value
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(1L);
 pct = frequency.getPct(1L);
 sumFreq = frequency.getSumFreq();
 assertEquals(pct, 1.0 / sumFreq, 0.01);
 // Test getting percentage of a char value
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('a');
 pct = frequency.getPct('a');
 sumFreq = frequency.getSumFreq();
 assertEquals(pct, 2.0 / sumFreq, 0.01);
 }

 @Test
 public void testGetCumFreq() {
 // Test getting cumulative frequency of a value that has not been added
 assertEquals(0, frequency.getCumFreq(1));
 // Test getting cumulative frequency of a value that has been added
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 assertEquals(1, frequency.getCumFreq(1));
 assertEquals(3, frequency.getCumFreq(2));
 assertEquals(4, frequency.getCumFreq(2)); // This should be the same as getSumFreq()
 }

 @Test
 public void testGetCumPct() {
 // Test getting cumulative percentage before adding values
 assertEquals(0, frequency.getCumPct(1), 0.0);
 // Test getting cumulative percentage after adding values
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 double cumPct = frequency.getCumPct(1);
 double sumFreq = frequency.getSumFreq();
 assertEquals(cumPct, 1.0 / sumFreq, 0.01);
 }

 @Test
 public void testGetCumPctWithDifferentTypes() {
 // Test getting cumulative percentage of an int value
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 double cumPct = frequency.getCumPct(1);
 double sumFreq = frequency.getSumFreq();
 assertEquals(cumPct, 1.0 / sumFreq, 0.01);
 // Test getting cumulative percentage of a long value
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(1L);
 cumPct = frequency.getCumPct(1L);
 sumFreq = frequency.getSumFreq();
 assertEquals(cumPct, 1.0 / sumFreq, 0.01);
 // Test getting cumulative percentage of a char value
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('a');
 cumPct = frequency.getCumPct('a');
 sumFreq = frequency.getSumFreq();
 assertEquals(cumPct, 2.0 / sumFreq, 0.01);
 }
}