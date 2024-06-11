import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new ConcreteInstance(); // ConcreteInstance class extends AbstractInstance
        int ID = instance.getID();
        assertEquals(ID, 0);
    }
    
    @Test
    public void testIterator() {
        AbstractInstance instance = new ConcreteInstance();
        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), 0.0, 0.001);
    }
    
    @Test
    public void testClassValue() {
        AbstractInstance instance = new ConcreteInstance();
        Object classValue = instance.classValue();
        assertNull(classValue);
    }
    
    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new ConcreteInstance();
        instance.setClassValue("sampleValue");
        Object classValue = instance.classValue();
        assertEquals(classValue, "sampleValue");
    }
    
    @Test
    public void testMinus_Instance() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = new ConcreteInstance();
        
        for (int i = 0; i < instance1.noAttributes(); i++) {
            instance1.put(i, (double) i);
            instance2.put(i, (double) (i + 1));
        }
        
        Instance result = instance1.minus(instance2);
        
        Iterator<Double> resultIterator = result.iterator();
        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(resultIterator.next(), (double) -1, 0.001);
        }
    }
    
    @Test
    public void testMinus_Double() {
        AbstractInstance instance = new ConcreteInstance();
        double min = 2.5;
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            instance.put(i, (double) i);
        }
        
        Instance result = instance.minus(min);
        
        Iterator<Double> resultIterator = result.iterator();
        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(resultIterator.next(), (double) -2.5, 0.001);
        }
    }
    
    @Test
    public void testDivide_Double() {
        AbstractInstance instance = new ConcreteInstance();
        double min = 2.5;
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            instance.put(i, (double) i);
        }
        
        Instance result = instance.divide(min);
        
        Iterator<Double> resultIterator = result.iterator();
        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(resultIterator.next(), (double) i/2.5, 0.001);
        }
    }
    
    @Test
    public void testMultiply_Double() {
        AbstractInstance instance = new ConcreteInstance();
        double value = 2.5;
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            instance.put(i, (double) i);
        }
        
        Instance result = instance.multiply(value);
        
        Iterator<Double> resultIterator = result.iterator();
        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(resultIterator.next(), (double) i*2.5, 0.001);
        }
    }
    
    @Test
    public void testEquals_SameObject() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = instance1;
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testEquals_NullObject() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = null;
        assertFalse(instance1.equals(instance2));
    }
    
    @Test
    public void testEquals_DifferentClasses() {
        AbstractInstance instance = new ConcreteInstance();
        String differentClass = "sampleClass";
        assertFalse(instance.equals(differentClass));
    }
    
    @Test
    public void testEquals_DifferentIDs() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = new ConcreteInstance();
        assertFalse(instance1.equals(instance2));
    }
    
    @Test
    public void testEquals_SameID() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = new ConcreteInstance();
        instance2.setID(instance1.getID());
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testMultiply_Instance() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = new ConcreteInstance();
        
        for (int i = 0; i < instance1.noAttributes(); i++) {
            instance1.put(i, (double) i);
            instance2.put(i, (double) (i + 1));
        }
        
        Instance result = instance1.multiply(instance2);
        
        Iterator<Double> resultIterator = result.iterator();
        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(resultIterator.next(), (double) i*(i+1), 0.001);
        }
    }
    
    @Test
    public void testDivide_Instance() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = new ConcreteInstance();
        
        for (int i = 0; i < instance1.noAttributes(); i++) {
            instance1.put(i, (double) i);
            instance2.put(i, (double) (i + 1));
        }
        
        Instance result = instance1.divide(instance2);
        
        Iterator<Double> resultIterator = result.iterator();
        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(resultIterator.next(), (double) i/(i+1), 0.001);
        }
    }
    
    @Test
    public void testAdd_Double() {
        AbstractInstance instance = new ConcreteInstance();
        double min = 2.5;
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            instance.put(i, (double) i);
        }
        
        Instance result = instance.add(min);
        
        Iterator<Double> resultIterator = result.iterator();
        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(resultIterator.next(), (double) i+2.5, 0.001);
        }
    }
    
    @Test
    public void testAdd_Instance() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = new ConcreteInstance();
        
        for (int i = 0; i < instance1.noAttributes(); i++) {
            instance1.put(i, (double) i);
            instance2.put(i, (double) (i + 1));
        }
        
        Instance result = instance1.add(instance2);
        
        Iterator<Double> resultIterator = result.iterator();
        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(resultIterator.next(), (double) i+(i+1), 0.001);
        }
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new ConcreteInstance();
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            instance.put(i, (double) i*i);
        }
        
        Instance result = instance.sqrt();
        
        Iterator<Double> resultIterator = result.iterator();
        for (int i = 0; i < result.noAttributes(); i++) {
            assertEquals(resultIterator.next(), Math.sqrt((double) i*i), 0.001);
        }
    }
    
    // Additional Tests:
    
    @Test
    public void testRemove_InIterator() {
        AbstractInstance instance = new ConcreteInstance();
        Iterator<Double> iterator = instance.iterator();
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals(e.getMessage(), "Cannot remove from instance using the iterator.");
        }
    }
    
    @Test
    public void testInstanceValueIterator() {
        AbstractInstance instance = new ConcreteInstance();
        AbstractInstance.InstanceValueIterator iterator = instance.new InstanceValueIterator();
        assertFalse(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), 0.0, 0.001);
    }
    
    @Test
    public void testNoAttributes() {
        AbstractInstance instance = new ConcreteInstance();
        int noAttributes = instance.noAttributes();
        // Assuming this is a valid scenario
        assertTrue(noAttributes >= 0);
    }
    
    private class ConcreteInstance extends AbstractInstance {
        ConcreteInstance() {
            super();
        }
    }
}