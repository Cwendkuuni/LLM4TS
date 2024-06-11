import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 // Test constructor
 @Test
 public void testConstructor() {
 Frequency frequency = new Frequency();
 }

 // Test addValue method with different data types
 @Test
 public void testAddValue() {
 Frequency frequency = new Frequency();

 // Test int type
 frequency.addValue(1);
 frequency.addValue(1);
 long intCount = frequency.getCount(1);
 long intCumFreq = frequency.getCumFreq(1);
 long intCumPct = frequency.getCumPct(1);
 assertEquals(2, intCount);
 assertEquals(2, intCumFreq);
 assertEquals(1.0, (double) intCumPct / 100.0, 0.01);

 // Test long type
 frequency.addValue(2L);
 frequency.addValue(2L);
 long longCount = frequency.getCount(2);
 long longCumFreq = frequency.getCumFreq(2);
 long longCumPct = frequency.getCumPct(2);
 assertEquals(2, longCount);
 assertEquals(4, longCumFreq);
 assertEquals(2.0, (double) longCumPct / 100.0, 0.01);

 // Test char type
 frequency.addValue('a');
 frequency.addValue('a');
 long charCount = frequency.getCount('a');
 long charCumFreq = frequency.getCumFreq('a');
 long charCumPct = frequency.getCumPct('a');
 assertEquals(2, charCount);
 assertEquals(4, charCumFreq);
 assertEquals(2.0, (double) charCumPct / 100.0, 0.01);
 }

 // Test addValue method with different data types and exception handling
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithException() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue(1); // This will pass
 // This will throw an exception
 frequency.addValue('b');
 }

 // Test addValue method with the same data type but different values
 @Test
 public void testAddValueWithSameDataTypeDifferentValues() {
 Frequency frequency = new Frequency();

 // Test int type
 frequency.addValue(1);
 frequency.addValue(2);
 long intCount = frequency.getCount(1);
 long intCumFreq = frequency.getCumFreq(1);
 long intCumPct = frequency.getCumPct(1);
 assertEquals(1, intCount);
 assertEquals(1, intCumFreq);
 assertEquals(0.0, (double) intCumPct / 100.0, 0.01);

 long intCount2 = frequency.getCount(2);
 long intCumFreq2 = frequency.getCumFreq(2);
 long intCumPct2 = frequency.getCumPct(2);
 assertEquals(1, intCount2);
 assertEquals(2, intCumFreq2);
 assertEquals(0.5, (double) intCumPct2 / 100.0, 0.01);
 }

 // Test addValue method with different data types but comparable values
 @Test
 public void testAddValueWithDifferentDataTypesComparableValues() {
 Frequency frequency = new Frequency();

 // Test int and long types
 frequency.addValue(1);
 frequency.addValue(2L);
 long intCount = frequency.getCount(1);
 long intCumFreq = frequency.getCumFreq(1);
 long intCumPct = frequency.getCumPct(1);
 assertEquals(1, intCount);
 assertEquals(1, intCumFreq);
 assertEquals(0.0, (double) intCumPct / 100.0, 0.01);

 long longCount = frequency.getCount(2);
 long longCumFreq = frequency.getCumFreq(2);
 long longCumPct = frequency.getCumPct(2);
 assertEquals(1, longCount);
 assertEquals(1, longCumFreq);
 assertEquals(0.0, (double) longCumPct / 100.0, 0.01);

 // Test int and char types
 frequency.addValue(1);
 frequency.addValue('a');
 long intCount2 = frequency.getCount(1);
 long intCumFreq2 = frequency.getCumFreq(1);
 long intCumPct2 = frequency.getCumPct(1);
 assertEquals(1, intCount2);
 assertEquals(1, intCumFreq2);
 assertEquals(0.0, (double) intCumPct2 / 100.0, 0.01);

 long charCount = frequency.getCount('a');
 long charCumFreq = frequency.getCumFreq('a');
 long charCumPct = frequency.getCumPct('a');
 assertEquals(1, charCount);
 assertEquals(1, charCumFreq);
 assertEquals(0.0, (double) charCumPct / 100.0, 0.01);
 }

 // Test addValue method with different data types but non-comparable values
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithDifferentDataTypesNonComparableValues() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('a'); // This will pass
 // This will throw an exception
 frequency.addValue(2L);
 }

 // Test clear method
 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.clear();
 long count = frequency.getCount(1);
 long cumFreq = frequency.getCumFreq(1);
 long cumPct = frequency.getCumPct(1);
 assertEquals(0, count);
 assertEquals(0, cumFreq);
 assertEquals(0.0, (double) cumPct / 100.0, 0.01);
 }
}