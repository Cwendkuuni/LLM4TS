package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

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
        Iterator<Double> iterator = instance.iterator();
        assertNotNull(iterator);
    }
    
    @Test
    public void testClassValue() {
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        Object classValue = new Object();
        instance.setClassValue(classValue);
        assertSame(classValue, instance.classValue());
    }
    
    @Test
    public void testMinusWithInstance() {
        DenseInstance min = new DenseInstance(new double[]{1.0, 2.0});
        Instance result = instance.minus(min);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }
    
    @Test
    public void testMinusWithDouble() {
        double min = 1.0;
        Instance result = instance.minus(min);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }
    
    @Test
    public void testDivide() {
        double min = 2.0;
        Instance result = instance.divide(min);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }
    
    @Test
    public void testMultiply() {
        double value = 2.0;
        Instance result = instance.multiply(value);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        AbstractInstance instance2 = new AbstractInstanceImpl();
        assertTrue(instance.equals(instance2));
    }
    
    @Test
    public void testEqualsWithNull() {
        assertFalse(instance.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentType() {
        assertFalse(instance.equals(new Object()));
    }
    
    @Test
    public void testMultiplyWithInstance() {
        DenseInstance value = new DenseInstance(new double[]{2.0, 3.0});
        Instance result = instance.multiply(value);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }
    
    @Test
    public void testDivideWithInstance() {
        DenseInstance min = new DenseInstance(new double[]{2.0, 3.0});
        Instance result = instance.divide(min);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }
    
    @Test
    public void testAddWithDouble() {
        double min = 2.0;
        Instance result = instance.add(min);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }
    
    @Test
    public void testAddWithInstance() {
        DenseInstance min = new DenseInstance(new double[]{2.0, 3.0});
        Instance result = instance.add(min);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }
    
    @Test
    public void testSqrt() {
        Instance result = instance.sqrt();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }
    
    class AbstractInstanceImpl extends AbstractInstance {
        
        public AbstractInstanceImpl() {
            super();
        }

        @Override
        public double get(int index) {
            return 0.0;
        }

        @Override
        public void put(int index, double value) {
            
        }

        @Override
        public void remove(int index) {
            
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public double value(int index) {
            return 0.0;
        }
        
    }

}