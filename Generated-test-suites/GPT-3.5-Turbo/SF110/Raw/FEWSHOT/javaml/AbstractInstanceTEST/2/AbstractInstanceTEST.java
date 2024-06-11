package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(instance.iterator().hasNext());
    }
    
    @Test
    public void testIterator_next() {
        assertEquals(Double.valueOf(0), instance.iterator().next());
    }
    
    @Test
    public void testIterator_remove() {
        UnsupportedOperationException exception = null;
        try {
            instance.iterator().remove();
        } catch (UnsupportedOperationException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    @Test
    public void testClassValue() {
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        instance.setClassValue("classValue");
        assertEquals("classValue", instance.classValue());
    }

    @Test
    public void testMinus_min_Instance() {
        AbstractInstance min = new AbstractInstanceImpl();
        Instance result = instance.minus(min);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testMinus_min_double() {
        double min = 1.0;
        Instance result = instance.minus(min);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testDivide() {
        double div = 1.0;
        Instance result = instance.divide(div);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testMultiply() {
        double value = 1.0;
        Instance result = instance.multiply(value);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testHashCode() {
        assertEquals(0, instance.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
        AbstractInstance same = new AbstractInstanceImpl();
        assertTrue(instance.equals(same));
    }

    @Test
    public void testMultiply_Instance() {
        AbstractInstance value = new AbstractInstanceImpl();
        Instance result = instance.multiply(value);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testDivide_Instance() {
        AbstractInstance min = new AbstractInstanceImpl();
        Instance result = instance.divide(min);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(Double.POSITIVE_INFINITY, result.value(i), 0.001);
        }
    }

    @Test
    public void testAdd_double() {
        double min = 1.0;
        Instance result = instance.add(min);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(1.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testAdd_Instance() {
        AbstractInstance min = new AbstractInstanceImpl();
        Instance result = instance.add(min);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.001);
        }
    }

    @Test
    public void testSqrt() {
        Instance result = instance.sqrt();
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.001);
        }
    }
    
    private class AbstractInstanceImpl extends AbstractInstance {
        @Override
        public double value(int arg0) {
            return 0.0;
        }
        
        @Override
        public int noAttributes() {
            return 1;
        }
    }
}