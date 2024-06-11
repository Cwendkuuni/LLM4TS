
package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        int expected = 0;
        int actual = instance.getID();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        Iterator<Double> iterator = instance.iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testClassValue() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstance(classValue) {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        Object expected = classValue;
        Object actual = instance.classValue();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetClassValue() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        instance.setClassValue(classValue);
        Object expected = classValue;
        Object actual = instance.classValue();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMinus_1() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        Instance min = new DenseInstance(new double[0]);
        Instance expected = new DenseInstance(new double[0]);
        Instance actual = instance.minus(min);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMinus_2() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        double min = 0.0;
        Instance expected = new DenseInstance(new double[0]);
        Instance actual = instance.minus(min);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        double min = 0.0;
        Instance expected = new DenseInstance(new double[0]);
        Instance actual = instance.divide(min);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        double value = 0.0;
        Instance expected = new DenseInstance(new double[0]);
        Instance actual = instance.multiply(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        int expected = instance.getID();
        int actual = instance.hashCode();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_1() {
        AbstractInstance instance1 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        AbstractInstance instance2 = instance1;
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testEquals_2() {
        AbstractInstance instance1 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        assertFalse(instance1.equals(instance2));
    }
    
    @Test
    public void testMultiply_2() {
        AbstractInstance instance1 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        Instance expected = new DenseInstance(new double[0]);
        Instance actual = instance1.multiply(instance2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDivide_2() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        Instance min = new DenseInstance(new double[0]);
        Instance expected = new DenseInstance(new double[0]);
        Instance actual = instance.divide(min);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAdd_1() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        double min = 0.0;
        Instance expected = new DenseInstance(new double[0]);
        Instance actual = instance.add(min);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAdd_2() {
        AbstractInstance instance1 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        Instance expected = new DenseInstance(new double[0]);
        Instance actual = instance1.add(instance2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
            @Override
            public double value(int index) {
                return 0;
            }
            @Override
            public void put(int index, double value) {
                
            }
        };
        Instance expected = new DenseInstance(new double[0]);
        Instance actual = instance.sqrt();
        assertEquals(expected, actual);
    }
    
}
