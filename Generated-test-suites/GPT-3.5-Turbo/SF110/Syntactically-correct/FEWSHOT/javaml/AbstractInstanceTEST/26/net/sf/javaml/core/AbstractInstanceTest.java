package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        assertEquals(0.0, instance.iterator().next(), 0.001);
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
    public void testMinusWithStringParameter() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.minus(min);
        assertEquals(0.0, result.value(0), 0.001);
    }

    @Test
    public void testMinusWithDoubleParameter() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance result = instance.minus(5.0);
        assertEquals(0.0, result.value(0), 0.001);
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance result = instance.divide(5.0);
        assertEquals(0.0, result.value(0), 0.001);
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance result = instance.multiply(5.0);
        assertEquals(0.0, result.value(0), 0.001);
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstanceImpl();
        assertEquals(0, instance.hashCode());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstanceImpl();
        AbstractInstance instance2 = new AbstractInstanceImpl();
        assertEquals(true, instance1.equals(instance2));
    }

    @Test
    public void testMultiplyWithInstanceParameter() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance value = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.multiply(value);
        assertEquals(0.0, result.value(0), 0.001);
    }

    @Test
    public void testDivideWithInstanceParameter() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance value = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.divide(value);
        assertEquals(0.0, result.value(0), 0.001);
    }

    @Test
    public void testAddWithDoubleParameter() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance result = instance.add(5.0);
        assertEquals(0.0, result.value(0), 0.001);
    }

    @Test
    public void testAddWithInstanceParameter() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance value = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.add(value);
        assertEquals(0.0, result.value(0), 0.001);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance result = instance.sqrt();
        assertEquals(0.0, result.value(0), 0.001);
    }

    private class AbstractInstanceImpl extends AbstractInstance {
        @Override
        public int noAttributes() {
            return 1;
        }

        @Override
        public void put(int index, double value) {
            
        }

        @Override
        public double value(int index) {
            return 0.0;
        }
    }
}