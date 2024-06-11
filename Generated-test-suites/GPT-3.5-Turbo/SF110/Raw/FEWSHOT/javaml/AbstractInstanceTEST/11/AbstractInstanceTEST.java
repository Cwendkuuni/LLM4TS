package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new ConcreteInstance();
        assertEquals(0, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new ConcreteInstance();
        assertNotNull(instance.iterator());
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new ConcreteInstance();
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new ConcreteInstance();
        Object classValue = new Object();
        instance.setClassValue(classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new ConcreteInstance();
        AbstractInstance min = new ConcreteInstance();
        assertNotNull(instance.minus(min));
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new ConcreteInstance();
        double min = 0.0;
        assertNotNull(instance.minus(min));
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new ConcreteInstance();
        double min = 0.0;
        assertNotNull(instance.divide(min));
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new ConcreteInstance();
        double value = 0.0;
        assertNotNull(instance.multiply(value));
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance = new ConcreteInstance();
        assertTrue(instance.hashCode() >= 0);
    }

    @Test
    public void testEquals() {
        AbstractInstance instance = new ConcreteInstance();
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
        assertTrue(instance.equals(new ConcreteInstance()));
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance = new ConcreteInstance();
        AbstractInstance min = new ConcreteInstance();
        assertNotNull(instance.add(min));
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new ConcreteInstance();
        double min = 0.0;
        assertNotNull(instance.add(min));
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new ConcreteInstance();
        assertNotNull(instance.sqrt());
    }

    private class ConcreteInstance extends AbstractInstance {

        public ConcreteInstance() {
            super();
        }
    }

}