
package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {
    
    private AbstractInstance instance;
    
    @Before
    public void setUp() {
        instance = new AbstractInstanceImpl();
    }
    
    @Test
    public void testGetID() {
        assertEquals(0, instance.getID());
    }
    
    @Test
    public void testIterator() {
        assertNotNull(instance.iterator());
    }
    
    @Test
    public void testClassValue() {
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        instance.setClassValue("class");
        assertEquals("class", instance.classValue());
    }
    
    @Test
    public void testMinusInstance() {
        Instance min = new AbstractInstanceImpl();
        Instance result = instance.minus(min);
        assertNotNull(result);
        // TODO: Add assertions for the values in the resulting instance
    }
    
    @Test
    public void testMinusDouble() {
        double min = 1.0;
        Instance result = instance.minus(min);
        assertNotNull(result);
        // TODO: Add assertions for the values in the resulting instance
    }
    
    @Test
    public void testDivideDouble() {
        double min = 1.0;
        Instance result = instance.divide(min);
        assertNotNull(result);
        // TODO: Add assertions for the values in the resulting instance
    }
    
    @Test
    public void testMultiplyDouble() {
        double value = 2.0;
        Instance result = instance.multiply(value);
        assertNotNull(result);
        // TODO: Add assertions for the values in the resulting instance
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        AbstractInstance other = new AbstractInstanceImpl();
        assertEquals(true, instance.equals(other));
    }
    
    @Test
    public void testMultiplyInstance() {
        Instance value = new AbstractInstanceImpl();
        Instance result = instance.multiply(value);
        assertNotNull(result);
        // TODO: Add assertions for the values in the resulting instance
    }
    
    @Test
    public void testDivideInstance() {
        Instance min = new AbstractInstanceImpl();
        Instance result = instance.divide(min);
        assertNotNull(result);
        // TODO: Add assertions for the values in the resulting instance
    }
    
    @Test
    public void testAddDouble() {
        double min = 1.0;
        Instance result = instance.add(min);
        assertNotNull(result);
        // TODO: Add assertions for the values in the resulting instance
    }
    
    @Test
    public void testAddInstance() {
        Instance min = new AbstractInstanceImpl();
        Instance result = instance.add(min);
        assertNotNull(result);
        // TODO: Add assertions for the values in the resulting instance
    }
    
    @Test
    public void testSqrt() {
        Instance result = instance.sqrt();
        assertNotNull(result);
        // TODO: Add assertions for the values in the resulting instance
    }
    
    private class AbstractInstanceImpl extends AbstractInstance {
        
        private static final long serialVersionUID = 1L;
        
        @Override
        public int noAttributes() {
            return 0;
        }
        
        @Override
        public double value(int index) {
            return 0.0;
        }
        
        @Override
        public void put(int index, double value) {
            
        }
    }
}
