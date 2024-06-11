package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {
    
    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstanceImpl();
        assertEquals(0, instance.getID());
    }
    
    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Iterator<Double> iterator = instance.iterator();
        List<Double> values = new ArrayList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }
        assertEquals(0, values.size());
    }
    
    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstanceImpl();
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstanceImpl();
        instance.setClassValue("class");
        assertEquals("class", instance.classValue());
    }
    
    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new AbstractInstanceImpl();
        AbstractInstance min = new AbstractInstanceImpl();
        Instance result = instance.minus(min);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstanceImpl();
        double min = 0.0;
        Instance result = instance.minus(min);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstanceImpl();
        double divisor = 1.0;
        Instance result = instance.divide(divisor);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstanceImpl();
        double value = 1.0;
        Instance result = instance.multiply(value);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstanceImpl();
        assertEquals(0, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        AbstractInstance instance = new AbstractInstanceImpl();
        AbstractInstance other = new AbstractInstanceImpl();
        assertEquals(false, instance.equals(other));
    }
    
    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance = new AbstractInstanceImpl();
        AbstractInstance value = new AbstractInstanceImpl();
        Instance result = instance.multiply(value);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new AbstractInstanceImpl();
        AbstractInstance min = new AbstractInstanceImpl();
        Instance result = instance.divide(min);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    @Test
    public void testAddDouble() {
        AbstractInstance instance = new AbstractInstanceImpl();
        double min = 0.0;
        Instance result = instance.add(min);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    @Test
    public void testAddInstance() {
        AbstractInstance instance = new AbstractInstanceImpl();
        AbstractInstance min = new AbstractInstanceImpl();
        Instance result = instance.add(min);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance result = instance.sqrt();
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(Math.sqrt(0.0), result.get(i), 0.0001);
        }
    }

    private class AbstractInstanceImpl extends AbstractInstance {
        
        @Override
        public int noAttributes() {
            return 1;
        }
        
        @Override
        public double value(int index) {
            return 0.0;
        }
        
        @Override
        public double get(int index) {
            return 0.0;
        }
        
        @Override
        public void put(int index, double value) {
            
        }
    }
}