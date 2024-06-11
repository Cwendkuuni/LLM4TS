
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {
    
    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNotEquals(0, instance.getID());
    }
    
    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNotNull(instance.iterator());
    }
    
    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("value");
        assertEquals("value", instance.classValue());
    }
    
    @Test
    public void testMinus() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        assertNotNull(instance.minus(min));
    }
    
    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNotNull(instance.minus(0.0));
    }
    
    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNotNull(instance.divide(1.0));
    }
    
    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNotNull(instance.multiply(1.0));
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNotNull(instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        assertFalse(instance1.equals(instance2));
        assertTrue(instance1.equals(instance1));
    }
    
    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        assertNotNull(instance1.multiply(instance2));
    }
    
    @Test
    public void testDivideInstance() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        assertNotNull(instance1.divide(instance2));
    }
    
    @Test
    public void testAddDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNotNull(instance.add(0.0));
    }
    
    @Test
    public void testAddInstance() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        assertNotNull(instance1.add(instance2));
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNotNull(instance.sqrt());
    }
    
    @Test
    public void testInstanceValueIteratorHasNext() {
        AbstractInstance instance = new AbstractInstance() {};
        AbstractInstance.InstanceValueIterator iterator = instance.new InstanceValueIterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testInstanceValueIteratorNext() {
        AbstractInstance instance = new AbstractInstance() {};
        AbstractInstance.InstanceValueIterator iterator = instance.new InstanceValueIterator();
        assertNull(iterator.next());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testInstanceValueIteratorRemove() {
        AbstractInstance instance = new AbstractInstance() {};
        AbstractInstance.InstanceValueIterator iterator = instance.new InstanceValueIterator();
        iterator.remove();
    }
    
}
