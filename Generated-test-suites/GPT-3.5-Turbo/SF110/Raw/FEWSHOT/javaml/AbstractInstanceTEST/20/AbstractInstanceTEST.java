package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        int expected = 0;
        int result = instance.getID();
        assertEquals(expected, result);
    }
    
    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        // Test iterator functionality
        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        Double nextValue = iterator.next();
        assertNotNull(nextValue);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        Object expected = "example";
        Object result = instance.classValue();
        assertEquals(expected, result);
    }
    
    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        Object expected = "new example";
        instance.setClassValue("new example");
        Object result = instance.classValue();
        assertEquals(expected, result);
    }
    
    @Test
    public void testMinus_Instance() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        AbstractInstance min = new AbstractInstance() {};
        min.setClassValue("example");

        Instance result = instance.minus(min);
        assertNotNull(result);
    }
    
    @Test
    public void testMinus_double() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        double min = 0.0;

        Instance result = instance.minus(min);
        assertNotNull(result);
    }
    
    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        double min = 0.0;

        Instance result = instance.divide(min);
        assertNotNull(result);
    }
    
    @Test
    public void testMultiply_double() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        double value = 1.0;

        Instance result = instance.multiply(value);
        assertNotNull(result);
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        int expected = instance.getID();
        int result = instance.hashCode();
        assertEquals(expected, result);
    }
    
    @Test
    public void testEquals() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        AbstractInstance other = new AbstractInstance() {};
        other.setClassValue("example");

        boolean result = instance.equals(other);
        assertTrue(result);
    }
    
    @Test
    public void testMultiply_Instance() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        AbstractInstance value = new AbstractInstance() {};
        value.setClassValue("example");

        Instance result = instance.multiply(value);
        assertNotNull(result);
    }
    
    @Test
    public void testDivide_Instance() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        AbstractInstance min = new AbstractInstance() {};
        min.setClassValue("example");

        Instance result = instance.divide(min);
        assertNotNull(result);
    }
    
    @Test
    public void testAdd_double() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        double min = 0.0;

        Instance result = instance.add(min);
        assertNotNull(result);
    }
    
    @Test
    public void testAdd_Instance() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        AbstractInstance min = new AbstractInstance() {};
        min.setClassValue("example");

        Instance result = instance.add(min);
        assertNotNull(result);
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        Instance result = instance.sqrt();
        assertNotNull(result);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testRemove() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("example");
        
        Iterator<Double> iterator = instance.iterator();
        iterator.remove();
    }

}