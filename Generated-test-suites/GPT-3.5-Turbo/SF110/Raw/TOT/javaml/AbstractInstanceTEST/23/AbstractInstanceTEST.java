import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractInstanceTest {
    
    AbstractInstance instance;
    
    @Before
    public void setUp() {
        instance = new ConcreteInstance(); // ConcreteInstance is a subclass of AbstractInstance
    }
    
    @Test
    public void testGetID() {
        int id = instance.getID();
        
        // Ensure the ID is non-negative
        assertTrue(id >= 0);
    }
    
    @Test
    public void testIterator() {
        Iterator<Double> iterator = instance.iterator();
        
        // Ensure the iterator has the same number of attributes as the instance
        assertEquals(instance.noAttributes(), count(iterator));
    }
    
    @Test
    public void testClassValue() {
        Object classValue = instance.classValue();
        
        // Ensure the classValue is correctly set or is null if not set
        assertNull(classValue);
    }
    
    @Test
    public void testSetClassValue() {
        Object newClassValue = new Object();
        instance.setClassValue(newClassValue);
        
        // Ensure the classValue is correctly set
        assertEquals(newClassValue, instance.classValue());
    }
    
    @Test
    public void testMinusInstance() {
        // Create sample instances
        ConcreteInstance min = new ConcreteInstance();
        for (int i = 0; i < instance.noAttributes(); i++) {
            min.put(i, 5.0); // Set all attribute values to 5.0
        }
        
        Instance result = instance.minus(min);
        
        // Ensure the resulting instance has correct attribute values based on subtraction
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.00001); // Values should be 0.0
        }
    }
    
    @Test
    public void testMinusDouble() {
        double min = 5.0;
        
        Instance result = instance.minus(min);
        
        // Ensure the resulting instance has correct attribute values based on subtraction
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(-5.0, result.get(i), 0.00001); // Values should be -5.0
        }
    }
    
    @Test
    public void testDivide() {
        double div = 2.0;
        
        Instance result = instance.divide(div);
        
        // Ensure the resulting instance has correct attribute values based on division
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.00001); // Values should be 0.0
        }
    }
    
    @Test
    public void testMultiply() {
        double mul = 3.0;
        
        Instance result = instance.multiply(mul);
        
        // Ensure the resulting instance has correct attribute values based on multiplication
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.00001); // Values should be 0.0
        }
    }
    
    @Test
    public void testHashCode() {
        int hashCode = instance.hashCode();
        
        // Ensure the hashCode is the same as the instance ID
        assertEquals(instance.getID(), hashCode);
    }
    
    @Test
    public void testEquals() {
        // Create a new instance with the same ID as the original instance
        ConcreteInstance sameInstance = new ConcreteInstance();
        sameInstance.setID(instance.getID());
        
        // Create a new instance with a different ID
        ConcreteInstance differentInstance = new ConcreteInstance();
        
        // Ensure that the two instances with the same ID are considered equal
        assertTrue(instance.equals(sameInstance));
        
        // Ensure that the two instances with different IDs are not considered equal
        assertFalse(instance.equals(differentInstance));
    }
    
    @Test
    public void testMultiplyInstance() {
        // Create sample instances
        ConcreteInstance value = new ConcreteInstance();
        for (int i = 0; i < instance.noAttributes(); i++) {
            value.put(i, 3.0); // Set all attribute values to 3.0
        }
        
        Instance result = instance.multiply(value);
        
        // Ensure the resulting instance has correct attribute values based on multiplication
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.00001); // Values should be 0.0
        }
    }
    
    @Test
    public void testDivideInstance() {
        // Create sample instances
        ConcreteInstance value = new ConcreteInstance();
        for (int i = 0; i < instance.noAttributes(); i++) {
            value.put(i, 2.0); // Set all attribute values to 2.0
        }
        
        Instance result = instance.divide(value);
        
        // Ensure the resulting instance has correct attribute values based on division
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.00001); // Values should be 0.0
        }
    }
    
    @Test
    public void testAddDouble() {
        double add = 5.0;
        
        Instance result = instance.add(add);
        
        // Ensure the resulting instance has correct attribute values based on addition
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(5.0, result.get(i), 0.00001); // Values should be 5.0
        }
    }
    
    @Test
    public void testAddInstance() {
        // Create sample instances
        ConcreteInstance add = new ConcreteInstance();
        for (int i = 0; i < instance.noAttributes(); i++) {
            add.put(i, 5.0); // Set all attribute values to 5.0
        }
        
        Instance result = instance.add(add);
        
        // Ensure the resulting instance has correct attribute values based on addition
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(5.0, result.get(i), 0.00001); // Values should be 5.0
        }
    }
    
    @Test
    public void testSqrt() {
        // Create sample instance
        for (int i = 0; i < instance.noAttributes(); i++) {
            instance.put(i, 4.0); // Set all attribute values to 4.0
        }
        
        Instance result = instance.sqrt();
        
        // Ensure the resulting instance has correct attribute values based on square root
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(2.0, result.get(i), 0.00001); // Values should be 2.0
        }
    }
    
    private int count(Iterator<?> iterator) {
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
}