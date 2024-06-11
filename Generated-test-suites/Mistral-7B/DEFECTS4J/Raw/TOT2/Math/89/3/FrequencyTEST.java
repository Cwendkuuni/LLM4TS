import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 // Test constructor and basic functionality
 @Test
 public void testFrequencyConstructor() {
 Frequency frequency = new Frequency();
 assertNotNull(frequency);
 }

 @Test
 public void testFrequencyAddValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals(3, frequency.getSumFreq());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFrequencyAddInvalidValue() {
 Frequency frequency = new Frequency();
 frequency.addValue("invalidValue"); // should throw an IllegalArgumentException
 }

 // Test getCount method
 @Test
 public void testGetCount() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals(3, frequency.getCount(1));
 assertEquals(0, frequency.getCount("invalidValue"));
 }

 // Test getPct method
 @Test
 public void testGetPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals(0.333, frequency.getPct(1), 0.001);
 assertEquals(Double.NaN, frequency.getPct("invalidValue"));
 }

 // Test getCumFreq method
 @Test
 public void testGetCumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals(1, frequency.getCumFreq(1));
 assertEquals(2, frequency.getCumFreq(2));
 assertEquals(3, frequency.getCumFreq(3));
 assertEquals(0, frequency.getCumFreq("invalidValue"));
 }

 // Test getCumPct method
 @Test
 public void testGetCumPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals(0.333, frequency.getCumPct(1), 0.001);
 assertEquals(0.667, frequency.getCumPct(2), 0.001);
 assertEquals(1.0, frequency.getCumPct(3), 0.001);
 assertEquals(Double.NaN, frequency.getCumPct("invalidValue"));
 }

 // Test clear method
 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.clear();
 assertEquals(0, frequency.getSumFreq());
 }

 // Test valuesIterator method
 @Test
 public void testValuesIterator() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 Iterator values = frequency.valuesIterator();
 assertEquals(1, values.next().longValue());
 assertEquals(1, values.next().longValue());
 assertEquals(2, values.next().longValue());
 assertEquals(null, values.next());
 }

 // Test addValue for different data types
 @Test
 public void testAddValueDifferentDataTypes() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue('a');
 frequency.addValue('b');
 assertEquals(5, frequency.getSumFreq());
 }

 // Test toString method
 @Test
 public void testToString() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals("Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.333 \t 0.333 \n2 \t 1 \t 0.2 \t 0.533 \n" +
         "Total: 3\n", frequency.toString());
 }
}