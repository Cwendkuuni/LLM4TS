import static org.junit.Assert.*;
import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;

public class FrequencyTest {

 private Frequency frequency;

 @Before
 public void setup() {
 frequency = new Frequency();
 }

 @Test
 public void testToStringDefaultComparator() {
 // Test empty Frequency instance
 String expectedToString = "Value \t Freq. \t Pct. \t Cum Pct.\n";
 String actualToString = frequency.toString();
 assertEquals(expectedToString, actualToString);

 // Test adding values and verifying toString method
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 expectedToString += "1 \t 2 \t 20.0% \t 20.0% \n" +
                     "2 \t 1 \t 10.0% \t 30.0% \n" +
                     "1 \t 1 \t 10.0% \t 40.0% \n" +
                     "Total: 4 values, 4 frequencies, and 100.0% cumulative frequency.";
 actualToString = frequency.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testToStringCustomComparator() {
 // Test empty Frequency instance with custom comparator
 Comparator comparator = new NaturalComparator();
 Frequency frequencyWithCustomComparator = new Frequency(comparator);
 String expectedToString = "Value \t Freq. \t Pct. \t Cum Pct.\n";
 String actualToString = frequencyWithCustomComparator.toString();
 assertEquals(expectedToString, actualToString);

 // Test adding values and verifying toString method with custom comparator
 frequencyWithCustomComparator.addValue('z');
 frequencyWithCustomComparator.addValue('a');
 frequencyWithCustomComparator.addValue('z');
 expectedToString += "a \t 1 \t 25.0% \t 25.0% \n" +
                     "z \t 2 \t 50.0% \t 75.0% \n" +
                     "z \t 1 \t 25.0% \t 100.0% \n" +
                     "Total: 3 values, 3 frequencies, and 100.0% cumulative frequency.";
 actualToString = frequencyWithCustomComparator.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testAddValueInt() {
 frequency.addValue(1);
 long expectedCount = 1;
 long actualCount = frequency.getCount(1);
 assertEquals(expectedCount, actualCount);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueNotComparable() {
 frequency.addValue('a'); // 'a' is not comparable to integers
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueNotComparableWithCustomComparator() {
 Comparator comparator = new NaturalComparator();
 Frequency frequencyWithCustomComparator = new Frequency(comparator);
 frequencyWithCustomComparator.addValue('a'); // 'a' is not comparable to 'z'
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testAddValueLong() {
 frequency.addValue(1L);
 long expectedCount = 1;
 long actualCount = frequency.getCount(1L);
 assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testAddValueChar() {
 frequency.addValue('a');
 char expectedValue = 'a';
 long expectedCount = 1;
 long actualCount = frequency.getCount(expectedValue);
 assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testClear() {
 frequency.clear();
 long expectedSumFreq = 0;
 long actualSumFreq = frequency.getSumFreq();
 assertEquals(expectedSumFreq, actualSumFreq);
 }

 @Test
 public void testValuesIterator() {
 // Test empty Frequency instance
 Iterator<Object> expectedValuesIterator = frequency.valuesIterator();
 assertFalse(expectedValuesIterator.hasNext());

 // Test adding values and verifying valuesIterator method
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(1);
 Iterator<Object> actualValuesIterator = frequency.valuesIterator();
 Object expectedValue = 1;
 assertTrue(actualValuesIterator.hasNext());
 assertEquals(expectedValue, actualValuesIterator.next());
 expectedValue = 2;
 assertTrue(actualValuesIterator.hasNext());
 assertEquals(expectedValue, actualValuesIterator.next());
 assertTrue(actualValuesIterator.hasNext());
 expectedValue = 1;
 assertEquals(expectedValue, actualValuesIterator.next());
 assertFalse(actualValuesIterator.hasNext());
 }

 @Test
 public void testGetSumFreq() {
 long expectedSumFreq = 4;
 long actualSumFreq = frequency.getSumFreq();
 assertEquals(expectedSumFreq, actualSumFreq);
 }

 @Test
 public void testGetCountInt() {
 long expectedCount = 2;
 long actualCount = frequency.getCount(1);
 assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testGetCountLong() {
 long expectedCount = 2;
 long actualCount = frequency.getCount(1L);
 assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testGetCountChar() {
 long expectedCount = 1;
 long actualCount = frequency.getCount('a');
 assertEquals(expectedCount, actualCount);
 }

 @Test
 public void testGetPctInt() {
 double expectedPct = 20.0;
 double actualPct = frequency.getPct(1);
 assertEquals(expectedPct, actualPct, 0.001);
 }

 @Test
 public void testGetPctLong() {
 double expectedPct = 20.0;
 double actualPct = frequency.getPct(1L);
 assertEquals(expectedPct, actualPct, 0.001);
 }

 @Test
 public void testGetPctChar() {
 double expectedPct = 10.0;
 double actualPct = frequency.getPct('a');
 assertEquals(expectedPct, actualPct, 0.001);
 }

 @Test
 public void testGetCumFreqInt() {
 long expectedCumulativeFreq = 2;
 long actualCumulativeFreq = frequency.getCumFreq(1);
 assertEquals(expectedCumulativeFreq, actualCumulativeFreq);
 }

 @Test
 public void testGetCumFreqLong() {
 long expectedCumulativeFreq = 2;
 long actualCumulativeFreq = frequency.getCumFreq(1L);
 assertEquals(expectedCumulativeFreq, actualCumulativeFreq);
 }

 @Test
 public void testGetCumFreqChar() {
 long expectedCumulativeFreq = 3;
 long actualCumulativeFreq = frequency.getCumFreq('a');
 assertEquals(expectedCumulativeFreq, actualCumulativeFreq);
 }

 @Test
 public void testGetCumPctInt() {
 double expectedCumulativePct = 20.0;
 double actualCumulativePct = frequency.getCumPct(1);
 assertEquals(expectedCumulativePct, actualCumulativePct, 0.001);
 }

 @Test
 public void testGetCumPctLong() {
 double expectedCumulativePct = 20.0;
 double actualCumulativePct = frequency.getCumPct(1L);
 assertEquals(expectedCumulativePct, actualCumulativePct, 0.001);
 }

 @Test
 public void testGetCumPctChar() {
 double expectedCumulativePct = 25.0;
 double actualCumulativePct = frequency.getCumPct('a');
 assertEquals(expectedCumulativePct, actualCumulativePct, 0.001);
 }

 @Test
 public void testGetCumPctSumFreq() {
 double expectedCumulativePct = 100.0;
 double actualCumulativePct = frequency.getCumPct(3);
 assertEquals(expectedCumulativePct, actualCumulativePct, 0.001);
 }

}