package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;

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
        assertEquals(false, instance.iterator().hasNext());
    }
    
    @Test
    public void testAbstractInstance() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance("test") {};
        assertEquals(null, instance1.classValue());
        assertEquals("test", instance2.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("test");
        assertEquals("test", instance.classValue());
    }
    
    @Test
    public void testMinus(Instance min) {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        AbstractInstance instance3 = instance1.minus(instance2);
        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(0, instance3.get(i), 0.001);
        }
    }
    
    @Test
    public void testMinus(double min) {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = instance1.minus(1.0);
        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(0, instance2.get(i), 0.001);
        }
    }
    
    @Test
    public void testDivide(double min) {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = instance1.divide(2.0);
        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(0, instance2.get(i), 0.001);
        }
    }
    
    @Test
    public void testMultiply(double value) {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = instance1.multiply(2.0);
        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(0, instance2.get(i), 0.001);
        }
    }
    
    @Test
    public void testEquals(Object obj) {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        assertEquals(true, instance1.equals(instance1));
        assertEquals(false, instance1.equals(null));
        assertEquals(false, instance1.equals(instance2));
    }
    
    @Test
    public void testMultiply(Instance value) {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        AbstractInstance instance3 = instance1.multiply(instance2);
        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(0, instance3.get(i), 0.001);
        }
    }
    
    @Test
    public void testDivide(Instance min) {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        AbstractInstance instance3 = instance1.divide(instance2);
        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(0, instance3.get(i), 0.001);
        }
    }
    
    @Test
    public void testAdd(double min) {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = instance1.add(1.0);
        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(0, instance2.get(i), 0.001);
        }
    }
    
    @Test
    public void testAdd(Instance min) {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        AbstractInstance instance3 = instance1.add(instance2);
        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(0, instance3.get(i), 0.001);
        }
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {};
        AbstractInstance sqrtInstance = instance.sqrt();
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0, sqrtInstance.get(i), 0.001);
        }
    }
}