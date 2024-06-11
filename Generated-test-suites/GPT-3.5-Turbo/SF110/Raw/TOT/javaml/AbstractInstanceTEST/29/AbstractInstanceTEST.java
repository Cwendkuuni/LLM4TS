import static org.junit.Assert.*;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {
    private AbstractInstance instance;
    
    @Before
    public void setUp() {
        instance = new AbstractInstance() {};
    }
    
    @After
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testGetID() {
        assertEquals(0, instance.getID());
    }
    
    @Test
    public void testIterator_noAttributes() {
        assertFalse(instance.iterator().hasNext());
    }
    
    @Test
    public void testIterator_hasAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        
        Iterator<Double> iterator = instance.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(1.0, iterator.next(), 0.0);
        assertTrue(iterator.hasNext());
        assertEquals(2.0, iterator.next(), 0.0);
        assertTrue(iterator.hasNext());
        assertEquals(3.0, iterator.next(), 0.0);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAbstractInstance_nullClassValue() {
        assertNull(instance.classValue());
    }
    
    @Test
    public void testAbstractInstance_withClassValue() {
        Object classValue = new Object();
        
        instance = new AbstractInstance(classValue) {};
        
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        Object classValue = new Object();
        
        instance.setClassValue(classValue);
        
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testMinus_Instance() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        
        Instance result = instance1.minus(instance2);
        
        assertEquals(-2.0, result.get(0), 0.0);
        assertEquals(-2.0, result.get(1), 0.0);
    }
    
    @Test
    public void testMinus_Double() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        double min = 3.0;
        
        Instance result = instance1.minus(min);
        
        assertEquals(-2.0, result.get(0), 0.0);
        assertEquals(-1.0, result.get(1), 0.0);
    }
    
    @Test
    public void testDivide() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        double min = 3.0;
        
        Instance result = instance1.divide(min);
        
        assertEquals(1.0/3.0, result.get(0), 0.0);
        assertEquals(2.0/3.0, result.get(1), 0.0);
    }
    
    @Test
    public void testMultiply() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        double value = 3.0;
        
        Instance result = instance1.multiply(value);
        
        assertEquals(3.0, result.get(0), 0.0);
        assertEquals(6.0, result.get(1), 0.0);
    }
    
    @Test
    public void testHashCode() {
        assertEquals(instance.getID(), instance.hashCode());
    }
    
    @Test
    public void testEquals_sameObject() {
        assertTrue(instance.equals(instance));
    }
    
    @Test
    public void testEquals_nullObject() {
        assertFalse(instance.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        assertFalse(instance.equals(new Object()));
    }
    
    @Test
    public void testEquals_differentID() {
        Instance instance1 = new AbstractInstance() {};
        Instance instance2 = new AbstractInstance() {};
        
        assertFalse(instance1.equals(instance2));
    }
    
    @Test
    public void testEquals_sameID() {
        Instance instance1 = new AbstractInstance() {};
        instance1.setID(instance.getID());
        
        assertTrue(instance.equals(instance1));
    }
    
    @Test
    public void testMultiply_Instance() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        
        Instance result = instance1.multiply(instance2);
        
        assertEquals(3.0, result.get(0), 0.0);
        assertEquals(8.0, result.get(1), 0.0);
    }
    
    @Test
    public void testDivide_Instance() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        
        Instance result = instance1.divide(instance2);
        
        assertEquals(1.0/3.0, result.get(0), 0.0);
        assertEquals(2.0/4.0, result.get(1), 0.0);
    }
    
    @Test
    public void testAdd_Double() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        double min = 3.0;
        
        Instance result = instance1.add(min);
        
        assertEquals(4.0, result.get(0), 0.0);
        assertEquals(5.0, result.get(1), 0.0);
    }
    
    @Test
    public void testAdd_Instance() {
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        Instance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        
        Instance result = instance1.add(instance2);
        
        assertEquals(4.0, result.get(0), 0.0);
        assertEquals(6.0, result.get(1), 0.0);
    }
    
    @Test
    public void testSqrt() {
        instance.put(0, 1.0);
        instance.put(1, 4.0);
        
        Instance result = instance.sqrt();
        
        assertEquals(1.0, result.get(0), 0.0);
        assertEquals(2.0, result.get(1), 0.0);
    }
}