package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {
    
    private AbstractInstance instance;
    private Object classValue;
    private int ID;
    
    @Before
    public void setUp() {
        classValue = new Object();
        instance = new AbstractInstanceImpl(classValue);
        ID = 0;
    }
    
    @Test
    public void testGetID() {
        assertEquals(ID, instance.getID());
    }
    
    @Test
    public void testIterator() {
        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0.0, iterator.next(), 0.00001);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testClassValue() {
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        Object newClassValue = new Object();
        instance.setClassValue(newClassValue);
        assertEquals(newClassValue, instance.classValue());
    }
    
    @Test
    public void testMinusInstance() {
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance expected = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(expected, instance.minus(min));
    }
    
    @Test
    public void testMinusDouble() {
        double min = 0.0;
        Instance expected = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(expected, instance.minus(min));
    }
    
    @Test
    public void testDivide() {
        double value = 0.0;
        Instance expected = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(expected, instance.divide(value));
    }
    
    @Test
    public void testMultiply() {
        double value = 0.0;
        Instance expected = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(expected, instance.multiply(value));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(ID, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        Object obj = new AbstractInstanceImpl(classValue);
        assertTrue(instance.equals(obj));
    }
    
    @Test
    public void testMultiplyInstance() {
        Instance value = new DenseInstance(new double[instance.noAttributes()]);
        Instance expected = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(expected, instance.multiply(value));
    }
    
    @Test
    public void testDivideInstance() {
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance expected = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(expected, instance.divide(min));
    }
    
    @Test
    public void testAddDouble() {
        double min = 0.0;
        Instance expected = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(expected, instance.add(min));
    }
    
    @Test
    public void testAddInstance() {
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance expected = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(expected, instance.add(min));
    }
    
    @Test
    public void testSqrt() {
        Instance expected = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(expected, instance.sqrt());
    }
    
    class AbstractInstanceImpl extends AbstractInstance {
        
        public AbstractInstanceImpl(Object classValue) {
            super(classValue);
        }
        
        @Override
        public int noAttributes() {
            return 1;
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