
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;
import java.util.Map;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new TestInstance();
        assertEquals(0, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new TestInstance();
        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1.0, iterator.next(), 0.001);
        assertTrue(iterator.hasNext());
        assertEquals(2.0, iterator.next(), 0.001);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new TestInstance();
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new TestInstance();
        instance.setClassValue("Test");
        assertEquals("Test", instance.classValue());
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance1 = new TestInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new TestInstance(new double[]{0.5, 1.0});
        AbstractInstance result = instance1.minus(instance2);
        assertEquals(0.5, result.value(0), 0.001);
        assertEquals(1.0, result.value(1), 0.001);
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new TestInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.minus(0.5);
        assertEquals(0.5, result.value(0), 0.001);
        assertEquals(1.5, result.value(1), 0.001);
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new TestInstance(new double[]{2.0, 4.0});
        AbstractInstance result = instance.divide(2.0);
        assertEquals(1.0, result.value(0), 0.001);
        assertEquals(2.0, result.value(1), 0.001);
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new TestInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.multiply(2.0);
        assertEquals(2.0, result.value(0), 0.001);
        assertEquals(4.0, result.value(1), 0.001);
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance = new TestInstance();
        assertEquals(0, instance.hashCode());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new TestInstance();
        AbstractInstance instance2 = new TestInstance();
        assertTrue(instance1.equals(instance1));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance1 = new TestInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new TestInstance(new double[]{2.0, 3.0});
        AbstractInstance result = instance1.multiply(instance2);
        assertEquals(2.0, result.value(0), 0.001);
        assertEquals(6.0, result.value(1), 0.001);
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance1 = new TestInstance(new double[]{2.0, 4.0});
        AbstractInstance instance2 = new TestInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance1.divide(instance2);
        assertEquals(2.0, result.value(0), 0.001);
        assertEquals(2.0, result.value(1), 0.001);
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new TestInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.add(0.5);
        assertEquals(1.5, result.value(0), 0.001);
        assertEquals(2.5, result.value(1), 0.001);
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance1 = new TestInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new TestInstance(new double[]{2.0, 3.0});
        AbstractInstance result = instance1.add(instance2);
        assertEquals(3.0, result.value(0), 0.001);
        assertEquals(5.0, result.value(1), 0.001);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new TestInstance(new double[]{4.0, 9.0});
        AbstractInstance result = instance.sqrt();
        assertEquals(2.0, result.value(0), 0.001);
        assertEquals(3.0, result.value(1), 0.001);
    }
    
    class TestInstance extends AbstractInstance {
        private double[] values;

        public TestInstance() {
            this.values = new double[]{1.0, 2.0};
        }

        public TestInstance(double[] values) {
            this.values = values;
        }

        @Override
        public int noAttributes() {
            return values.length;
        }

        @Override
        protected double value(int index) {
            return values[index];
        }

        @Override
        protected void put(int index, double value) {
            values[index] = value;
        }
    }
}

