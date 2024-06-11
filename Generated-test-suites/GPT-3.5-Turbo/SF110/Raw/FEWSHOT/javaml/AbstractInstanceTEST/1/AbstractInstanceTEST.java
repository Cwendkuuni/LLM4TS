package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {

    private AbstractInstance instance;
    
    @Before
    public void setup() {
        instance = new AbstractInstance() {
            
            @Override
            public int noAttributes() {
                return 0;
            }
            
            @Override
            public void put(int index, double value) {
                
            }
            
            @Override
            public Double value(int index) {
                return null;
            }
        };
    }

    @Test
    public void testGetID() {
        assertEquals(0, instance.getID());
    }
    
    @Test
    public void testIterator() {
        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(null, iterator.next());
        assertTrue(!iterator.hasNext());

    }
    
    @Test
    public void testClassValue() {
        instance.setClassValue("class");
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testMinusWithInstance() {
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(min, instance.minus(min));
    }
    
    @Test
    public void testMinusWithDouble() {
        double min = 0.0;
        assertEquals(instance, instance.minus(min));
    }
    
    @Test
    public void testDivide() {
        double min = 2.0;
        assertEquals(instance, instance.divide(min));
    }
    
    @Test
    public void testMultiplyWithDouble() {
        double value = 2.0;
        assertEquals(instance, instance.multiply(value));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, instance.hashCode());
    }
    
    @Test
    public void testEqualsWithSelf() {
        assertTrue(instance.equals(instance));
    }
    
    @Test
    public void testEqualsWithNull() {
        assertTrue(!instance.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        assertTrue(!instance.equals(new Object()));
    }
    
    @Test
    public void testEqualsWithDifferentInstance() {
        AbstractInstance other = new AbstractInstance() {
            
            @Override
            public int noAttributes() {
                return 0;
            }
            
            @Override
            public void put(int index, double value) {
                
            }
            
            @Override
            public Double value(int index) {
                return null;
            }
        };
        assertTrue(!instance.equals(other));
    }
    
    @Test
    public void testMultiplyWithInstance() {
        Instance value = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(instance, instance.multiply(value));
    }
    
    @Test
    public void testDivideWithInstance() {
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(instance, instance.divide(min));
    }
    
    @Test
    public void testAddWithDouble() {
        double min = 2.0;
        assertEquals(instance, instance.add(min));
    }
    
    @Test
    public void testAddWithInstance() {
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        assertEquals(instance, instance.add(min));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(instance, instance.sqrt());
    }
    
    @Test
    public void testInstanceValueIterator() {
        AbstractInstance.InstanceValueIterator iterator = instance.new InstanceValueIterator();
        assertTrue(iterator.hasNext());
        assertEquals(null, iterator.next());
        assertTrue(!iterator.hasNext());
    }
    
    
}