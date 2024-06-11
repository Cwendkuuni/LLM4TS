package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {

    private AbstractInstance instance;
    private Object classValue;

    @Before
    public void setUp() {
        classValue = new Object();
        instance = new AbstractInstance(classValue) {
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
    }

    @Test
    public void testGetID() {
        int id = instance.getID();
        assertNotNull(id);
    }

    @Test
    public void testIterator() {
        Iterator<Double> iterator = instance.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void testClassValue() {
        Object value = instance.classValue();
        assertEquals(classValue, value);
    }

    @Test
    public void testSetClassValue() {
        Object newValue = new Object();
        instance.setClassValue(newValue);
        Object value = instance.classValue();
        assertEquals(newValue, value);
    }

    @Test
    public void testMinusInstance() {
        Instance min = new AbstractInstance(null) {

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

        Instance result = instance.minus(min);
        assertNotNull(result);
    }

    @Test
    public void testMinusDouble() {
        double min = 0;

        Instance result = instance.minus(min);
        assertNotNull(result);
    }

    @Test
    public void testDivide() {
        double divisor = 0;

        Instance result = instance.divide(divisor);
        assertNotNull(result);
    }

    @Test
    public void testMultiply() {
        double value = 0;

        Instance result = instance.multiply(value);
        assertNotNull(result);
    }

    @Test
    public void testHashCode() {
        int hashCode = instance.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testEquals() {
        AbstractInstance obj = new AbstractInstance(null) {
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

        boolean equals = instance.equals(obj);
        assertTrue(equals);
    }

    @Test
    public void testMultiplyInstance() {
        Instance value = new AbstractInstance(null) {
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

        Instance result = instance.multiply(value);
        assertNotNull(result);
    }

    @Test
    public void testDivideInstance() {
        Instance min = new AbstractInstance(null) {
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

        Instance result = instance.divide(min);
        assertNotNull(result);
    }

    @Test
    public void testAddDouble() {
        double min = 0;

        Instance result = instance.add(min);
        assertNotNull(result);
    }

    @Test
    public void testAddInstance() {
        Instance min = new AbstractInstance(null) {
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

        Instance result = instance.add(min);
        assertNotNull(result);
    }

    @Test
    public void testSqrt() {
        Instance result = instance.sqrt();
        assertNotNull(result);
    }

    @Test
    public void testInstanceValueIteratorHasNext() {
        AbstractInstance.InstanceValueIterator iterator = instance.new InstanceValueIterator();
        boolean hasNext = iterator.hasNext();
        assertNotNull(hasNext);
    }

    @Test
    public void testInstanceValueIteratorNext() {
        AbstractInstance.InstanceValueIterator iterator = instance.new InstanceValueIterator();
        Double next = iterator.next();
        assertNotNull(next);
    }

    @Test
    public void testInstanceValueIteratorRemove() {
        AbstractInstance.InstanceValueIterator iterator = instance.new InstanceValueIterator();
        try {
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot remove from instance using the iterator.", e.getMessage());
        }
    }
}