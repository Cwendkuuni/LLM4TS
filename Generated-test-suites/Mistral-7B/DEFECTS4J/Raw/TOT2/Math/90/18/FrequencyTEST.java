import org.apache.commons.math.stat.Frequency;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

private Frequency frequency;

@Before
public void setUp() {
frequency = new Frequency();
}

// Test case for addValue method with integer value
@Test
public void testAddValueInt() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
assertEquals(3, frequency.getCount(1));
assertEquals(1, frequency.getCount(2));
}

// Test case for addValue method with long value
@Test
public void testAddValueLong() {
frequency.addValue(1L);
frequency.addValue(1L);
frequency.addValue(2L);
assertEquals(3, frequency.getCount(1L));
assertEquals(1, frequency.getCount(2L));
}

// Test case for addValue method with character value
@Test
public void testAddValueChar() {
frequency.addValue('a');
frequency.addValue('a');
frequency.addValue('b');
assertEquals(3, frequency.getCount('a'));
assertEquals(1, frequency.getCount('b'));
}

// Test case for addValue method with non-comparable value
@Test(expected = IllegalArgumentException.class)
public void testAddValueNonComparableValue() {
frequency.addValue(new Object());
}

// Test case for clear method
@Test
public void testClear() {
frequency.addValue(1);
frequency.addValue(1);
frequency.clear();
assertEquals(0, frequency.getSumFreq());
}

// Test case for valuesIterator method
@Test
public void testValuesIterator() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
Iterator<Object> iterator = frequency.valuesIterator();
assertEquals(1, iterator.next());
assertEquals(1, iterator.next());
assertEquals(2, iterator.next());
assertFalse(iterator.hasNext());
}

// Test case for getSumFreq method
@Test
public void testGetSumFreq() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
assertEquals(4, frequency.getSumFreq());
}

// Test case for getCount method with integer value
@Test
public void testGetCountInt() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
assertEquals(3, frequency.getCount(1));
assertEquals(1, frequency.getCount(2));
}

// Test case for getCount method with long value
@Test
public void testGetCountLong() {
frequency.addValue(1L);
frequency.addValue(1L);
frequency.addValue(2L);
assertEquals(3, frequency.getCount(1L));
assertEquals(1, frequency.getCount(2L));
}

// Test case for getCount method with character value
@Test
public void testGetCountChar() {
frequency.addValue('a');
frequency.addValue('a');
frequency.addValue('b');
assertEquals(3, frequency.getCount('a'));
assertEquals(1, frequency.getCount('b'));
}

// Test case for getCount method with non-comparable value
@Test
public void testGetCountNonComparableValue() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
assertFalse(frequency.getCount(new Object()));
}

// Test case for getPct method with integer value
@Test
public void testGetPctInt() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
assertEquals(0.5, frequency.getPct(1), 0.01);
assertEquals(0.25, frequency.getPct(2), 0.01);
}

// Test case for getPct method with long value
@Test
public void testGetPctLong() {
frequency.addValue(1L);
frequency.addValue(1L);
frequency.addValue(2L);
assertEquals(0.5, frequency.getPct(1), 0.01);
assertEquals(0.25, frequency.getPct(2), 0.01);
}

// Test case for getPct method with character value
@Test
public void testGetPctChar() {
frequency.addValue('a');
frequency.addValue('a');
frequency.addValue('b');
assertEquals(0.6, frequency.getPct('a'), 0.01);
assertEquals(0.2, frequency.getPct('b'), 0.01);
}

// Test case for getPct method with non-comparable value
@Test
public void testGetPctNonComparableValue() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
assertNull(frequency.getPct(new Object()));
}

// Test case for getCumFreq method with integer value
@Test
public void testGetCumFreqInt() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
assertEquals(1, frequency.getCumFreq(1));
assertEquals(2, frequency.getCumFreq(2));
assertEquals(3, frequency.getCumFreq(3));
}

// Test case for getCumFreq method with long value
@Test
public void testGetCumFreqLong() {
frequency.addValue(1L);
frequency.addValue(1L);
frequency.addValue(2L);
assertEquals(1, frequency.getCumFreq(1L));
assertEquals(2, frequency.getCumFreq(2L));
assertEquals(3, frequency.getCumFreq(3L));
}

// Test case for getCumFreq method with character value
@Test
public void testGetCumFreqChar() {
frequency.addValue('a');
frequency.addValue('a');
frequency.addValue('b');
assertEquals(1, frequency.getCumFreq('a'));
assertEquals(2, frequency.getCumFreq('b'));
assertEquals(3, frequency.getCumFreq('c'));
}

// Test case for getCumFreq method with non-comparable value
@Test
public void testGetCumFreqNonComparableValue() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
assertEquals(0, frequency.getCumFreq(new Object()));
}

// Test case for getCumPct method with integer value
@Test
public void testGetCumPctInt() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
assertEquals(0.5, frequency.getCumPct(1), 0.01);
assertEquals(0.75, frequency.getCumPct(2), 0.01);
assertEquals(1.0, frequency.getCumPct(3), 0.01);
}

// Test case for getCumPct method with long value
@Test
public void testGetCumPctLong() {
frequency.addValue(1L);
frequency.addValue(1L);
frequency.addValue(2L);
assertEquals(0.5, frequency.getCumPct(1), 0.01);
assertEquals(0.75, frequency.getCumPct(2), 0.01);
assertEquals(1.0, frequency.getCumPct(3), 0.01);
}

// Test case for getCumPct method with character value
@Test
public void testGetCumPctChar() {
frequency.addValue('a');
frequency.addValue('a');
frequency.addValue('b');
assertEquals(0.6, frequency.getCumPct('a'), 0.01);
assertEquals(0.8, frequency.getCumPct('b'), 0.01);
assertEquals(1.0, frequency.getCumPct('c'), 0.01);
}

// Test case for getCumPct method with non-comparable value
@Test
public void testGetCumPctNonComparableValue() {
frequency.addValue(1);
frequency.addValue(1);
frequency.addValue(2);
assertEquals(0.0, frequency.getCumPct(new Object()), 0.01);
}
}