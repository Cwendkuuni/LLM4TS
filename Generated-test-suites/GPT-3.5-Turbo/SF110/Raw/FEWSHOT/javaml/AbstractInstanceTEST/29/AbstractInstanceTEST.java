package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        Iterator<Double> iterator = instance.iterator();
        
        assertEquals(false, iterator.hasNext());
        assertNull(iterator.next());
    }
    
    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue(new Object());
        assertNotNull(instance.classValue());
    }
    
    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.minus(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.000001);
        }
    }
    
    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        double min = 1.0;
        Instance result = instance.minus(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.000001);
        }
    }
    
    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstance() {};
        double min = 1.0;
        Instance result = instance.divide(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.000001);
        }
    }
    
    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstance() {};
        double value = 2.0;
        Instance result = instance.multiply(value);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.000001);
        }
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {};
        assertEquals(0, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        assertEquals(false, instance1.equals(instance2));
        
        AbstractInstance instance3 = instance1;
        assertEquals(true, instance1.equals(instance3));
    }
    
    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance value = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.multiply(value);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.000001);
        }
    }
    
    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.divide(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(Double.POSITIVE_INFINITY, result.get(i), 0.000001);
        }
    }
    
    @Test
    public void testAddDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        double min = 1.0;
        Instance result = instance.add(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(1.0, result.get(i), 0.000001);
        }
    }
    
    @Test
    public void testAddInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.add(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.000001);
        }
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance result = instance.sqrt();
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(Double.NaN, result.get(i), 0.000001);
        }
    }
}