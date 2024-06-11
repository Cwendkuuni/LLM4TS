import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new ConcreteInstance();
        assertEquals(0, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new ConcreteInstance();
        assertNotNull(instance.iterator());
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new ConcreteInstance();
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new ConcreteInstance();
        instance.setClassValue("testClassValue");
        assertEquals("testClassValue", instance.classValue());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance1 = new ConcreteInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new ConcreteInstance(new double[]{0.5, 1.0, 1.5});

        AbstractInstance result = instance1.minus(instance2);

        assertNotNull(result);
        assertEquals(1.0 - 0.5, result.value(0), 0.01);
        assertEquals(2.0 - 1.0, result.value(1), 0.01);
        assertEquals(3.0 - 1.5, result.value(2), 0.01);
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new ConcreteInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.minus(0.5);

        assertNotNull(result);
        assertEquals(1.0 - 0.5, result.value(0), 0.01);
        assertEquals(2.0 - 0.5, result.value(1), 0.01);
        assertEquals(3.0 - 0.5, result.value(2), 0.01);
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new ConcreteInstance(new double[]{2.0, 4.0, 6.0});

        AbstractInstance result = instance.divide(2.0);

        assertNotNull(result);
        assertEquals(2.0 / 2.0, result.value(0), 0.01);
        assertEquals(4.0 / 2.0, result.value(1), 0.01);
        assertEquals(6.0 / 2.0, result.value(2), 0.01);
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new ConcreteInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.multiply(2.0);

        assertNotNull(result);
        assertEquals(1.0 * 2.0, result.value(0), 0.01);
        assertEquals(2.0 * 2.0, result.value(1), 0.01);
        assertEquals(3.0 * 2.0, result.value(2), 0.01);
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = new ConcreteInstance();
        assertEquals(instance1.getID(), instance2.getID());
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = new ConcreteInstance();
        AbstractInstance instance3 = new ConcreteInstance(new double[]{1.0, 2.0, 3.0});

        assertTrue(instance1.equals(instance1));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new ConcreteInstance(new double[]{1.0, 2.5, 3.0});
        AbstractInstance instance2 = new ConcreteInstance(new double[]{2.0, 3.0, 4.0});

        AbstractInstance result = instance1.multiply(instance2);

        assertNotNull(result);
        assertEquals(1.0 * 2.0, result.value(0), 0.01);
        assertEquals(2.5 * 3.0, result.value(1), 0.01);
        assertEquals(3.0 * 4.0, result.value(2), 0.01);
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new ConcreteInstance(new double[]{2.0, 4.0, 6.0});
        AbstractInstance instance2 = new ConcreteInstance(new double[]{1.0, 1.0, 1.0});

        AbstractInstance result = instance1.divide(instance2);

        assertNotNull(result);
        assertEquals(2.0 / 1.0, result.value(0), 0.01);
        assertEquals(4.0 / 1.0, result.value(1), 0.01);
        assertEquals(6.0 / 1.0, result.value(2), 0.01);
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new ConcreteInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.add(0.5);

        assertNotNull(result);
        assertEquals(1.0 + 0.5, result.value(0), 0.01);
        assertEquals(2.0 + 0.5, result.value(1), 0.01);
        assertEquals(3.0 + 0.5, result.value(2), 0.01);
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new ConcreteInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new ConcreteInstance(new double[]{0.5, 1.0, 1.5});

        AbstractInstance result = instance1.add(instance2);

        assertNotNull(result);
        assertEquals(1.0 + 0.5, result.value(0), 0.01);
        assertEquals(2.0 + 1.0, result.value(1), 0.01);
        assertEquals(3.0 + 1.5, result.value(2), 0.01);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new ConcreteInstance(new double[]{4.0, 9.0, 25.0});

        AbstractInstance result = instance.sqrt();

        assertNotNull(result);
        assertEquals(Math.sqrt(4.0), result.value(0), 0.01);
        assertEquals(Math.sqrt(9.0), result.value(1), 0.01);
        assertEquals(Math.sqrt(25.0), result.value(2), 0.01);
    }

    class ConcreteInstance extends AbstractInstance {

        private double[] values;

        public ConcreteInstance() {
            this.values = new double[3];
        }

        public ConcreteInstance(double[] values) {
            this.values = values;
        }

        @Override
        public double value(int index) {
            return values[index];
        }

        @Override
        public int noAttributes() {
            return values.length;
        }

        @Override
        public void put(int index, double value) {
            values[index] = value;
        }

        @Override
        public double get(int index) {
            return values[index];
        }

    }
}