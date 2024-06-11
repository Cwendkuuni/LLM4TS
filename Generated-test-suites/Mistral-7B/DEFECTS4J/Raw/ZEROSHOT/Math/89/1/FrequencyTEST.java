import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();

        // Test addValue methods with different data types
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue("one"); // should throw an exception
        frequency.addValue('a');
        frequency.addValue(1.0);

        // Test clear method
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());

        // Test getCount methods with different data types
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue("one"); // should throw an exception
        frequency.addValue('a');
        frequency.addValue(1.0);
        frequency.addValue(1);
        assertEquals(3, frequency.getCount(1));
        try {
            frequency.getCount("one");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCount('a');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCount(1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test getPct methods with different data types
        assertEquals(0.3333333333333333, frequency.getPct(1), 0.0001);
        try {
            frequency.getPct("one");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getPct('a');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getPct(1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test getCumFreq methods with different data types
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
        try {
            frequency.getCumFreq("one");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCumFreq('a');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCumFreq(1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test getCumPct methods with different data types
        assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.0001);
        assertEquals(0.6666666666666667, frequency.getCumPct(2), 0.0001);
        assertEquals(1.0, frequency.getCumPct(3), 0.0001);
        try {
            frequency.getCumPct("one");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCumPct('a');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCumPct(1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFrequencyWithComparator() {
        Comparator<Object> comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);

        // Test addValue methods with different data types
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue("one"); // should throw an exception
        frequency.addValue('a');
        frequency.addValue(1.0);

        // Test clear method
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());

        // Test getCount methods with different data types
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue("one"); // should throw an exception
        frequency.addValue('a');
        frequency.addValue(1.0);
        frequency.addValue(1);
        assertEquals(3, frequency.getCount(1));
        try {
            frequency.getCount("one");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCount('a');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCount(1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test getPct methods with different data types
        assertEquals(0.3333333333333333, frequency.getPct(1), 0.0001);
        try {
            frequency.getPct("one");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getPct('a');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getPct(1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test getCumFreq methods with different data types
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(3, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));
        try {
            frequency.getCumFreq("one");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCumFreq('a');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCumFreq(1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test getCumPct methods with different data types
        assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.0001);
        assertEquals(0.6666666666666667, frequency.getCumPct(2), 0.0001);
        assertEquals(1.0, frequency.getCumPct(3), 0.0001);
        try {
            frequency.getCumPct("one");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCumPct('a');
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            frequency.getCumPct(1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}