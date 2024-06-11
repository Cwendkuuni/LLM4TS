import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FrequencyTest {

    @Test
    public void testAddValueNormal() {
        Frequency frequency = new Frequency();
        Object value = new Integer(1);
        frequency.addValue(value);
        long expectedCount = 1;
        long actualCount = frequency.getCount(value);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testAddValueMultipleTimes() {
        Frequency frequency = new Frequency();
        Object value = new Integer(1);
        for (int i = 0; i < 10; i++) {
            frequency.addValue(value);
        }
        long expectedCount = 10;
        long actualCount = frequency.getCount(value);
        assertEquals(expectedCount, actualCount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueDifferentTypes() {
        Frequency frequency = new Frequency();
        Object value1 = new Integer(1);
        Object value2 = 'a';
        frequency.addValue(value1);
        frequency.addValue(value2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullValue() {
        Frequency frequency = new Frequency();
        Object value = null;
        frequency.addValue(value);
    }

    @Test(expected = ClassCastException.class)
    public void testAddValueNonComparableValue() {
        Frequency frequency = new Frequency();
        Object value = new Object() {
            @Override
            public int compareTo(Object o) {
                throw new UnsupportedOperationException();
            }
        };
        frequency.addValue(value);
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long expectedSumFreq = 3;
        long actualSumFreq = frequency.getSumFreq();
        assertEquals(expectedSumFreq, actualSumFreq);
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long expectedCount = 2;
        long actualCount = frequency.getCount(1);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetCountForDifferentValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long expectedCount1 = 1;
        long expectedCount2 = 1;
        long actualCount1 = frequency.getCount(1);
        long actualCount2 = frequency.getCount(2);
        assertEquals(expectedCount1, actualCount1);
        assertEquals(expectedCount2, actualCount2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCountNullValue() {
        Frequency frequency = new Frequency();
        Object value = null;
        frequency.addValue(value);
        long actualCount = frequency.getCount(value);
    }

    @Test(expected = ClassCastException.class)
    public void testGetCountNonCompatibleValue() {
        Frequency frequency = new Frequency();
        Object value = new Object() {
            @Override
            public int compareTo(Object o) {
                throw new UnsupportedOperationException();
            }
        };
        frequency.addValue(value);
        long actualCount = frequency.getCount(value);
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        double expectedPct = 0.333;
        double actualPct = frequency.getPct(2);
        assertEquals(expectedPct, actualPct, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPctNullValue() {
        Frequency frequency = new Frequency();
        Object value = null;
        frequency.addValue(value);
        double actualPct = frequency.getPct(value);
    }

    @Test(expected = ClassCastException.class)
    public void testGetPctNonCompatibleValue() {
        Frequency frequency = new Frequency();
        Object value = new Object() {
            @Override
            public int compareTo(Object o) {
                throw new UnsupportedOperationException();
            }
        };
        frequency.addValue(value);
        double actualPct = frequency.getPct(value);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        long expectedCumFreq = 2;
        long actualCumFreq = frequency.getCumFreq(1);
        assertEquals(expectedCumFreq, actualCumFreq);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCumFreqNullValue() {
        Frequency frequency = new Frequency();
        Object value = null;
        long actualCumFreq = frequency.getCumFreq(value);
    }

    @Test(expected = ClassCastException.class)
    public void testGetCumFreqNonCompatibleValue() {
        Frequency frequency = new Frequency();
        Object value = new Object() {
            @Override
            public int compareTo(Object o) {
                throw new UnsupportedOperationException();
            }
        };
        frequency.addValue(value);
        long actualCumFreq = frequency.getCumFreq(value);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        double expectedCumPct = 0.667;
        double actualCumPct = frequency.getCumPct(2);
        assertEquals(expectedCumPct, actualCumPct, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCumPctNullValue() {
        Frequency frequency = new Frequency();
        Object value = null;
        double actualCumPct = frequency.getCumPct(value);
    }

    @Test(expected = ClassCastException.class)
    public void testGetCumPctNonCompatibleValue() {
        Frequency frequency = new Frequency();
        Object value = new Object() {
            @Override
            public int compareTo(Object o) {
                throw new UnsupportedOperationException();
            }
        };
        frequency.addValue(value);
        double actualCumPct = frequency.getCumPct(value);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        long expectedCount = 0;
        long actualCount = frequency.getCount(1);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> valuesIterator = frequency.valuesIterator();
        Object value = valuesIterator.next();
        long expectedValue = 1;
        long actualValue = ((Number) value).longValue();
        assertEquals(expectedValue, actualValue);
    }
}