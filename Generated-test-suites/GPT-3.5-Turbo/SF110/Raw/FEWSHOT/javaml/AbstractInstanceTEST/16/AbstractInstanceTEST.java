package net.sf.javaml.core.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import java.util.Iterator;

public class AbstractInstanceTest {
    
    @Test
    public void testGetID() {
        AbstractInstance a = new AbstractInstance() {};
        assertEquals(0, a.getID());
    }
    
    @Test
    public void testIterator() {
        AbstractInstance a = new AbstractInstance() {};
        Iterator<Double> iterator = a.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0.0, iterator.next(), 0.0001);
    }
    
    @Test
    public void testAbstractInstance() {
        AbstractInstance a = new AbstractInstance() {};
        assertNull(a.classValue());
    }
    
    @Test
    public void testAbstractInstanceWithValue() {
        Object classValue = new Object();
        AbstractInstance a = new AbstractInstance(classValue) {};
        assertEquals(classValue, a.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        AbstractInstance a = new AbstractInstance() {};
        Object classValue = new Object();
        a.setClassValue(classValue);
        assertEquals(classValue, a.classValue());
    }
    
    @Test
    public void testMinusWithInstance() {
        AbstractInstance a = new AbstractInstance() {};
        AbstractInstance min = new AbstractInstance() {};
        AbstractInstance result = (AbstractInstance) a.minus(min);
        for (int i = 0; i < a.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.0001);
        }
    }
    
    @Test
    public void testMinusWithDouble() {
        AbstractInstance a = new AbstractInstance() {};
        double min = 1.0;
        AbstractInstance result = (AbstractInstance) a.minus(min);
        for (int i = 0; i < a.noAttributes(); i++) {
            assertEquals(-1.0, result.value(i), 0.0001);
        }
    }
    
    @Test
    public void testDivide() {
        AbstractInstance a = new AbstractInstance() {};
        double min = 2.0;
        AbstractInstance result = (AbstractInstance) a.divide(min);
        for (int i = 0; i < a.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.0001);
        }
    }
    
    @Test
    public void testMultiply() {
        AbstractInstance a = new AbstractInstance() {};
        double value = 2.0;
        AbstractInstance result = (AbstractInstance) a.multiply(value);
        for (int i = 0; i < a.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.0001);
        }
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance a = new AbstractInstance() {};
        assertEquals(0, a.hashCode());
    }
    
    @Test
    public void testEquals() {
        AbstractInstance a = new AbstractInstance() {};
        AbstractInstance b = new AbstractInstance() {};
        assertEquals(true, a.equals(a));
        assertEquals(false, a.equals(null));
        assertEquals(false, a.equals(b));
    }
    
    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance a = new AbstractInstance() {};
        AbstractInstance value = new AbstractInstance() {};
        AbstractInstance result = (AbstractInstance) a.multiply(value);
        for (int i = 0; i < a.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.0001);
        }
    }
    
    @Test
    public void testDivideWithInstance() {
        AbstractInstance a = new AbstractInstance() {};
        AbstractInstance value = new AbstractInstance() {};
        AbstractInstance result = (AbstractInstance) a.divide(value);
        for (int i = 0; i < a.noAttributes(); i++) {
            assertEquals(Double.NaN, result.value(i), 0.0001);
        }
    }
    
    @Test
    public void testAddWithDouble() {
        AbstractInstance a = new AbstractInstance() {};
        double min = 1.0;
        AbstractInstance result = (AbstractInstance) a.add(min);
        for (int i = 0; i < a.noAttributes(); i++) {
            assertEquals(1.0, result.value(i), 0.0001);
        }
    }
    
    @Test
    public void testAddWithInstance() {
        AbstractInstance a = new AbstractInstance() {};
        AbstractInstance min = new AbstractInstance() {};
        AbstractInstance result = (AbstractInstance) a.add(min);
        for (int i = 0; i < a.noAttributes(); i++) {
            assertEquals(0.0, result.value(i), 0.0001);
        }
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance a = new AbstractInstance() {};
        AbstractInstance result = (AbstractInstance) a.sqrt();
        for (int i = 0; i < a.noAttributes(); i++) {
            assertEquals(Double.NaN, result.value(i), 0.0001);
        }
    }
}