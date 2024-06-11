import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {};

        int expected = 0;
        int actual = instance.getID();

        assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {};

        // Test iterator using sample data
        double[] values = {1.0, 2.0, 3.0};
        for (int i = 0; i < values.length; i++) {
            instance.put(i, values[i]);
        }
        int index = 0;
        for (Double value : instance) {
            double expected = values[index];
            double actual = value;

            assertEquals(expected, actual, 0.0001);
            index++;
        }

        // Test iterator with no attributes
        assertFalse(instance.iterator().hasNext());
    }

    @Test
    public void testConstructor() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstance(classValue) {};

        Object expected = classValue;
        Object actual = instance.classValue();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {};

        Object classValue = new Object();
        instance.setClassValue(classValue);

        Object expected = classValue;
        Object actual = instance.classValue();

        assertEquals(expected, actual);
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new AbstractInstance() {};

        // Test subtraction with sample data
        double[] values = {1.0, 2.0, 3.0};
        for (int i = 0; i < values.length; i++) {
            instance.put(i, values[i]);
        }
        AbstractInstance min = new AbstractInstance() {};
        double[] minValues = {0.5, 1.0, 1.5};
        for (int i = 0; i < minValues.length; i++) {
            min.put(i, minValues[i]);
        }
        Instance out = instance.minus(min);

        double[] expectedValues = {0.5, 1.0, 1.5};
        for (int i = 0; i < expectedValues.length; i++) {
            double expected = expectedValues[i];
            double actual = out.get(i);

            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstance() {};

        // Test subtraction with sample data
        double[] values = {1.0, 2.0, 3.0};
        for (int i = 0; i < values.length; i++) {
            instance.put(i, values[i]);
        }
        double min = 0.5;
        Instance out = instance.minus(min);

        double[] expectedValues = {0.5, 1.5, 2.5};
        for (int i = 0; i < expectedValues.length; i++) {
            double expected = expectedValues[i];
            double actual = out.get(i);

            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testDivideDouble() {
        AbstractInstance instance = new AbstractInstance() {};

        // Test division with sample data
        double[] values = {1.0, 2.0, 3.0};
        for (int i = 0; i < values.length; i++) {
            instance.put(i, values[i]);
        }
        double divisor = 2.0;
        Instance out = instance.divide(divisor);

        double[] expectedValues = {0.5, 1.0, 1.5};
        for (int i = 0; i < expectedValues.length; i++) {
            double expected = expectedValues[i];
            double actual = out.get(i);

            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testMultiplyDouble() {
        AbstractInstance instance = new AbstractInstance() {};

        // Test multiplication with sample data
        double[] values = {1.0, 2.0, 3.0};
        for (int i = 0; i < values.length; i++) {
            instance.put(i, values[i]);
        }
        double factor = 2.0;
        Instance out = instance.multiply(factor);

        double[] expectedValues = {2.0, 4.0, 6.0};
        for (int i = 0; i < expectedValues.length; i++) {
            double expected = expectedValues[i];
            double actual = out.get(i);

            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {};

        int expected = instance.getID();
        int actual = instance.hashCode();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};

        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
        assertTrue(instance1.equals(instance1));
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance = new AbstractInstance() {};

        // Test multiplication with sample data
        double[] values = {1.0, 2.0, 3.0};
        for (int i = 0; i < values.length; i++) {
            instance.put(i, values[i]);
        }
        AbstractInstance factor = new AbstractInstance() {};
        double[] factorValues = {2.0, 3.0, 4.0};
        for (int i = 0; i < factorValues.length; i++) {
            factor.put(i, factorValues[i]);
        }
        Instance out = instance.multiply(factor);

        double[] expectedValues = {2.0, 6.0, 12.0};
        for (int i = 0; i < expectedValues.length; i++) {
            double expected = expectedValues[i];
            double actual = out.get(i);

            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new AbstractInstance() {};

        // Test division with sample data
        double[] values = {2.0, 4.0, 6.0};
        for (int i = 0; i < values.length; i++) {
            instance.put(i, values[i]);
        }
        AbstractInstance divisor = new AbstractInstance() {};
        double[] divisorValues = {1.0, 2.0, 3.0};
        for (int i = 0; i < divisorValues.length; i++) {
            divisor.put(i, divisorValues[i]);
        }
        Instance out = instance.divide(divisor);

        double[] expectedValues = {2.0, 2.0, 2.0};
        for (int i = 0; i < expectedValues.length; i++) {
            double expected = expectedValues[i];
            double actual = out.get(i);

            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new AbstractInstance() {};

        // Test addition with sample data
        double[] values = {1.0, 2.0, 3.0};
        for (int i = 0; i < values.length; i++) {
            instance.put(i, values[i]);
        }
        double value = 2.0;
        Instance out = instance.add(value);

        double[] expectedValues = {3.0, 4.0, 5.0};
        for (int i = 0; i < expectedValues.length; i++) {
            double expected = expectedValues[i];
            double actual = out.get(i);

            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance = new AbstractInstance() {};

        // Test addition with sample data
        double[] values = {1.0, 2.0, 3.0};
        for (int i = 0; i < values.length; i++) {
            instance.put(i, values[i]);
        }
        AbstractInstance min = new AbstractInstance() {};
        double[] minValues = {2.0, 3.0, 4.0};
        for (int i = 0; i < minValues.length; i++) {
            min.put(i, minValues[i]);
        }
        Instance out = instance.add(min);

        double[] expectedValues = {3.0, 5.0, 7.0};
        for (int i = 0; i < expectedValues.length; i++) {
            double expected = expectedValues[i];
            double actual = out.get(i);

            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {};

        // Test square root with sample data
        double[] values = {4.0, 9.0, 16.0};
        for (int i = 0; i < values.length; i++) {
            instance.put(i, values[i]);
        }
        Instance out = instance.sqrt();

        double[] expectedValues = {2.0, 3.0, 4.0};
        for (int i = 0; i < expectedValues.length; i++) {
            double expected = expectedValues[i];
            double actual = out.get(i);

            assertEquals(expected, actual, 0.0001);
        }
    }
}