import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AbstractInstanceTest {
    
    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {};
        assertEquals(0, instance.getID());
    }
    
    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {};
        assertFalse(instance.iterator().hasNext());
    }
    
    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("class");
        assertEquals("class", instance.classValue());
    }
    
    @Test
    public void testMinusInstance() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        instance2.put(0, 2.0);
        instance2.put(1, 1.0);
        Instance result = instance1.minus(instance2);
        assertEquals(0.0, result.value(0), 0.0001);
        assertEquals(-1.0, result.value(1), 0.0001);
    }
    
    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.put(0, 2.0);
        instance.put(1, 1.0);
        Instance result = instance.minus(1.0);
        assertEquals(1.0, result.value(0), 0.0001);
        assertEquals(0.0, result.value(1), 0.0001);
    }
    
    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.put(0, 2.0);
        instance.put(1, 1.0);
        Instance result = instance.divide(2.0);
        assertEquals(1.0, result.value(0), 0.0001);
        assertEquals(0.5, result.value(1), 0.0001);
    }
    
    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.put(0, 2.0);
        instance.put(1, 1.0);
        Instance result = instance.multiply(2.0);
        assertEquals(4.0, result.value(0), 0.0001);
        assertEquals(2.0, result.value(1), 0.0001);
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {};
        assertEquals(0, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(instance2));
        assertTrue(instance1.equals(instance1));
    }
    
    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        instance2.put(0, 2.0);
        instance2.put(1, 1.0);
        Instance result = instance1.multiply(instance2);
        assertEquals(0.0, result.value(0), 0.0001);
        assertEquals(0.0, result.value(1), 0.0001);
    }
    
    @Test
    public void testDivideInstance() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        instance2.put(0, 2.0);
        instance2.put(1, 1.0);
        Instance result = instance1.divide(instance2);
        assertEquals(0.0, result.value(0), 0.0001);
        assertEquals(0.0, result.value(1), 0.0001);
    }
    
    @Test
    public void testAddDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.put(0, 2.0);
        instance.put(1, 1.0);
        Instance result = instance.add(1.0);
        assertEquals(3.0, result.value(0), 0.0001);
        assertEquals(2.0, result.value(1), 0.0001);
    }
    
    @Test
    public void testAddInstance() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        instance2.put(0, 2.0);
        instance2.put(1, 1.0);
        Instance result = instance1.add(instance2);
        assertEquals(2.0, result.value(0), 0.0001);
        assertEquals(1.0, result.value(1), 0.0001);
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.put(0, 4.0);
        instance.put(1, 9.0);
        Instance result = instance.sqrt();
        assertEquals(2.0, result.value(0), 0.0001);
        assertEquals(3.0, result.value(1), 0.0001);
    }
}