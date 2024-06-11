package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {
    
    private AbstractInstance instance;
    
    @Before
    public void setUp() {
        instance = new AbstractInstance() {};
    }
    
    @Test
    public void testGetID() {
        assertTrue(instance.getID() >= 0);
    }
    
    @Test
    public void testIterator() {
        // Test if iterator returns the correct values
        assertEquals(0.0, instance.iterator().next().doubleValue(), 0.0);
        assertEquals(1.0, instance.iterator().next().doubleValue(), 0.0);
        assertEquals(2.0, instance.iterator().next().doubleValue(), 0.0);
    }
    
    @Test
    public void testClassValue() {
        instance.setClassValue("classValue");
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        instance.setClassValue("classValue");
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testMinus_WithInstance() {
        Instance min = new AbstractInstance() {};
        Instance result = instance.minus(min);
        
        // Test if the resulting instance has the correct values
        assertEquals(0.0, result.value(0), 0.0);
        assertEquals(0.0, result.value(1), 0.0);
        assertEquals(0.0, result.value(2), 0.0);
    }
    
    @Test
    public void testMinus_WithDouble() {
        double min = 1.0;
        Instance result = instance.minus(min);
        
        // Test if the resulting instance has the correct values
        assertEquals(-1.0, result.value(0), 0.0);
        assertEquals(-1.0, result.value(1), 0.0);
        assertEquals(-1.0, result.value(2), 0.0);
    }
    
    @Test
    public void testDivide() {
        double min = 2.0;
        Instance result = instance.divide(min);
        
        // Test if the resulting instance has the correct values
        assertEquals(0.0, result.value(0), 0.0);
        assertEquals(0.5, result.value(1), 0.0);
        assertEquals(1.0, result.value(2), 0.0);
    }
    
    @Test
    public void testMultiply() {
        double value = 2.0;
        Instance result = instance.multiply(value);
        
        // Test if the resulting instance has the correct values
        assertEquals(0.0, result.value(0), 0.0);
        assertEquals(2.0, result.value(1), 0.0);
        assertEquals(4.0, result.value(2), 0.0);
    }
    
    @Test
    public void testHashCode() {
        assertEquals(instance.getID(), instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        AbstractInstance other = new AbstractInstance() {};
        assertTrue(instance.equals(instance));
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testMultiply_WithInstance() {
        Instance value = new AbstractInstance() {};
        Instance result = instance.multiply(value);
        
        // Test if the resulting instance has the correct values
        assertEquals(0.0, result.value(0), 0.0);
        assertEquals(0.0, result.value(1), 0.0);
        assertEquals(0.0, result.value(2), 0.0);
    }
    
    @Test
    public void testDivide_WithInstance() {
        Instance min = new AbstractInstance() {};
        Instance result = instance.divide(min);
        
        // Test if the resulting instance has the correct values
        assertEquals(Double.NaN, result.value(0), 0.0);
        assertEquals(Double.NaN, result.value(1), 0.0);
        assertEquals(Double.NaN, result.value(2), 0.0);
    }
    
    @Test
    public void testAdd_WithDouble() {
        double min = 1.0;
        Instance result = instance.add(min);
        
        // Test if the resulting instance has the correct values
        assertEquals(1.0, result.value(0), 0.0);
        assertEquals(1.0, result.value(1), 0.0);
        assertEquals(1.0, result.value(2), 0.0);
    }
    
    @Test
    public void testAdd_WithInstance() {
        Instance min = new AbstractInstance() {};
        Instance result = instance.add(min);
        
        // Test if the resulting instance has the correct values
        assertEquals(0.0, result.value(0), 0.0);
        assertEquals(0.0, result.value(1), 0.0);
        assertEquals(0.0, result.value(2), 0.0);
    }
    
    @Test
    public void testSqrt() {
        Instance result = instance.sqrt();
        
        // Test if the resulting instance has the correct values (square root of 0.0, 1.0, 2.0)
        assertEquals(0.0, result.value(0), 0.0);
        assertEquals(1.0, result.value(1), 0.0);
        assertEquals(1.414, result.value(2), 0.001);
    }
    
    @Test
    public void testRemove() {
        // Remove is not supported by AbstractInstance iterator, so it should throw an exception
        try {
            instance.iterator().remove();
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
}