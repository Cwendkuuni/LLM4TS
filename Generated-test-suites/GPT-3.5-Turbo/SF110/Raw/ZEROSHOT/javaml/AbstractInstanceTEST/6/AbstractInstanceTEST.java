import org.junit.Test;
import static org.junit.Assert.*;

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
        Iterator<Double> iterator = instance.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("classValue");
        assertEquals("classValue", instance.classValue());
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance min = new DenseInstance();
        Instance result = instance.minus(min);
        assertNotNull(result);
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        double min = 0.0d;
        Instance result = instance.minus(min);
        assertNotNull(result);
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstance() {};
        double min = 1.0d;
        Instance result = instance.divide(min);
        assertNotNull(result);
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstance() {};
        double value = 1.0d;
        Instance result = instance.multiply(value);
        assertNotNull(result);
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
        AbstractInstance instance = new AbstractInstance() {};
        assertTrue(instance.equals(instance));
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance value = new DenseInstance();
        Instance result = instance.multiply(value);
        assertNotNull(result);
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance min = new DenseInstance();
        Instance result = instance.divide(min);
        assertNotNull(result);
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        double min = 0.0d;
        Instance result = instance.add(min);
        assertNotNull(result);
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance min = new DenseInstance();
        Instance result = instance.add(min);
        assertNotNull(result);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance result = instance.sqrt();
        assertNotNull(result);
    }

}