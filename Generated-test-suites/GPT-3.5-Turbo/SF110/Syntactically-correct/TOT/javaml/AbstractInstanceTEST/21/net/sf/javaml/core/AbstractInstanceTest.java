import static org.junit.Assert.*;
import org.junit.*;

public class AbstractInstanceTest {

    // Test case for getID() method
    @Test
    public void testGetID() {
        AbstractInstance instance = new ConcreteInstance(123);
        assertEquals(0, instance.getID());
    }
    
    // Test case for iterator() method
    @Test
    public void testIterator() {
        AbstractInstance instance = new ConcreteInstance(123);
        assertNotNull(instance.iterator());
    }
    
    // Test case for classValue() method
    @Test
    public void testClassValue() {
        AbstractInstance instance = new ConcreteInstance(123);
        assertEquals(123, instance.classValue());
    }
    
    // Test case for setClassValue() method
    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new ConcreteInstance(123);
        instance.setClassValue(456);
        assertEquals(456, instance.classValue());
    }
    
    // Test case for minus(Instance) method
    @Test
    public void testMinusInstance() {
        AbstractInstance instance1 = new ConcreteInstance(2);
        AbstractInstance instance2 = new ConcreteInstance(1);
        Instance result = instance1.minus(instance2);
        assertEquals(1, result.classValue());
    }
    
    // Test case for minus(double) method
    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new ConcreteInstance(2);
        Instance result = instance.minus(1);
        assertEquals(1, result.classValue());
    }
    
    // Test case for divide(double) method
    @Test
    public void testDivideDouble() {
        AbstractInstance instance = new ConcreteInstance(2);
        Instance result = instance.divide(2);
        assertEquals(1, result.classValue());
    }
    
    // Test case for multiply(double) method
    @Test
    public void testMultiplyDouble() {
        AbstractInstance instance = new ConcreteInstance(2);
        Instance result = instance.multiply(2);
        assertEquals(4, result.classValue());
    }
    
    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        AbstractInstance instance = new ConcreteInstance(123);
        assertEquals(0, instance.hashCode());
    }
    
    // Test case for equals(Object) method
    @Test
    public void testEquals() {
        AbstractInstance instance1 = new ConcreteInstance(123);
        AbstractInstance instance2 = new ConcreteInstance(123);
        assertTrue(instance1.equals(instance2));
    }
    
    // Test case for multiply(Instance) method
    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance1 = new ConcreteInstance(2);
        AbstractInstance instance2 = new ConcreteInstance(3);
        Instance result = instance1.multiply(instance2);
        assertEquals(6, result.classValue());
    }
    
    // Test case for divide(Instance) method
    @Test
    public void testDivideInstance() {
        AbstractInstance instance1 = new ConcreteInstance(6);
        AbstractInstance instance2 = new ConcreteInstance(2);
        Instance result = instance1.divide(instance2);
        assertEquals(3, result.classValue());
    }
    
    // Test case for add(double) method
    @Test
    public void testAddDouble() {
        AbstractInstance instance = new ConcreteInstance(2);
        Instance result = instance.add(1);
        assertEquals(3, result.classValue());
    }
    
    // Test case for add(Instance) method
    @Test
    public void testAddInstance() {
        AbstractInstance instance1 = new ConcreteInstance(2);
        AbstractInstance instance2 = new ConcreteInstance(3);
        Instance result = instance1.add(instance2);
        assertEquals(5, result.classValue());
    }
    
    // Test case for sqrt() method
    @Test
    public void testSqrt() {
        AbstractInstance instance = new ConcreteInstance(4);
        Instance result = instance.sqrt();
        assertEquals(2, result.classValue());
    }

    // Custom test case
    // Test case for iterator() method with no attributes
    @Test
    public void testIteratorWithNoAttributes() {
        AbstractInstance instance = new ConcreteInstance();
        assertFalse(instance.iterator().hasNext());
    }
    
    // Custom test case
    // Test case for remove() method in Iterator
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        AbstractInstance instance = new ConcreteInstance();
        instance.iterator().remove();
    }
    
    // Custom test case
    // Test case for equals(Object) method with different class
    @Test
    public void testEqualsWithDifferentClass() {
        AbstractInstance instance = new ConcreteInstance(123);
        assertFalse(instance.equals("123"));
    }
    
}

class ConcreteInstance extends AbstractInstance {
    public ConcreteInstance() {
        super();
    }
    
    public ConcreteInstance(Object classValue) {
        super(classValue);
    }
}