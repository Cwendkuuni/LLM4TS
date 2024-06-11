import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {
    
    private AbstractInstance instance;
    private AbstractInstance instanceWithClassValue;
    
    @Before
    public void setUp() {
        instance = new AbstractInstanceTest.TestInstance();
        instanceWithClassValue = new AbstractInstanceTest.TestInstance("classValue");
    }
    
    @Test
    public void testGetID() {
        assertEquals(0, instance.getID());
        assertEquals(1, instanceWithClassValue.getID());
    }
    
    @Test
    public void testIterator() {
        assertTrue(instance.iterator().hasNext());
        assertEquals(Double.valueOf(0.0), instance.iterator().next());
        
        assertTrue(instanceWithClassValue.iterator().hasNext());
        assertEquals(Double.valueOf(0.0), instanceWithClassValue.iterator().next());
    }
    
    @Test
    public void testClassValue() {
        assertNull(instance.classValue());
        assertEquals("classValue", instanceWithClassValue.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        instance.setClassValue("newClassValue");
        assertEquals("newClassValue", instance.classValue());
        
        instanceWithClassValue.setClassValue(null);
        assertNull(instanceWithClassValue.classValue());
    }
    
    @Test
    public void testMinus_Instance() {
        // TODO: Implement test cases for minus with instance
    }
    
    @Test
    public void testMinus_double() {
        // TODO: Implement test cases for minus with double
    }
    
    @Test
    public void testDivide_double() {
        // TODO: Implement test cases for divide with double
    }
    
    @Test
    public void testMultiply_double() {
        // TODO: Implement test cases for multiply with double
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, instance.hashCode());
        assertEquals(1, instanceWithClassValue.hashCode());
    }
    
    @Test
    public void testEquals() {
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals("string"));
        
        AbstractInstance sameInstance = new AbstractInstanceTest.TestInstance();
        assertTrue(instance.equals(sameInstance));
        
        assertFalse(instance.equals(instanceWithClassValue));
    }
    
    @Test
    public void testMultiply_Instance() {
        // TODO: Implement test cases for multiply with instance
    }
    
    @Test
    public void testDivide_Instance() {
        // TODO: Implement test cases for divide with instance
    }
    
    @Test
    public void testAdd_double() {
        // TODO: Implement test cases for add with double
    }
    
    @Test
    public void testAdd_Instance() {
        // TODO: Implement test cases for add with instance
    }
    
    @Test
    public void testSqrt() {
        // TODO: Implement test cases for sqrt
    }
    
    private class TestInstance extends AbstractInstance {
        public TestInstance() {
            super();
        }
        
        public TestInstance(Object classValue) {
            super(classValue);
        }
        
        @Override
        public int noAttributes() {
            return 1;
        }
        
        @Override
        public Double value(int index) {
            return 0.0;
        }
        
        @Override
        public void put(int index, Double value) {
            // Do nothing, as this is a test class
        }
    }
}