package net.sf.javaml.core;

import static org.junit.Assert.*;

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
        assertFalse(instance.iterator().hasNext());
    }

    @Test
    public void testAbstractInstance() {
        AbstractInstance instance = new AbstractInstanceImpl();
        assertNull(instance.classValue());
    }

    @Test
    public void testAbstractInstanceObject() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstanceImpl(classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstanceImpl();
        instance.setClassValue(classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new AbstractInstanceImpl();
        AbstractInstance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance out = instance.minus(min);
        assertArrayEquals(new double[instance.noAttributes()], ((DenseInstance) out).toArray(), 0.0001);
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstanceImpl();
        double min = 1.0;
        Instance out = instance.minus(min);
        assertArrayEquals(new double[instance.noAttributes()], ((DenseInstance) out).toArray(), 0.0001);
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstanceImpl();
        double min = 1.0;
        Instance out = instance.divide(min);
        assertArrayEquals(new double[instance.noAttributes()], ((DenseInstance) out).toArray(), 0.0001);
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstanceImpl();
        double value = 1.0;
        Instance out = instance.multiply(value);
        assertArrayEquals(new double[instance.noAttributes()], ((DenseInstance) out).toArray(), 0.0001);
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new AbstractInstanceImpl();
        AbstractInstance instance2 = new AbstractInstanceImpl();
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEqualsObject() {
        AbstractInstance instance1 = new AbstractInstanceImpl();
        AbstractInstance instance2 = new AbstractInstanceImpl();
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance = new AbstractInstanceImpl();
        AbstractInstance value = new DenseInstance(new double[instance.noAttributes()]);
        Instance out = instance.multiply(value);
        assertArrayEquals(new double[instance.noAttributes()], ((DenseInstance) out).toArray(), 0.0001);
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new AbstractInstanceImpl();
        AbstractInstance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance out = instance.divide(min);
        assertArrayEquals(new double[instance.noAttributes()], ((DenseInstance) out).toArray(), 0.0001);
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new AbstractInstanceImpl();
        double min = 1.0;
        Instance out = instance.add(min);
        assertArrayEquals(new double[instance.noAttributes()], ((DenseInstance) out).toArray(), 0.0001);
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance = new AbstractInstanceImpl();
        AbstractInstance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance out = instance.add(min);
        assertArrayEquals(new double[instance.noAttributes()], ((DenseInstance) out).toArray(), 0.0001);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstanceImpl();
        Instance out = instance.sqrt();
        assertArrayEquals(new double[instance.noAttributes()], ((DenseInstance) out).toArray(), 0.0001);
    }

    class AbstractInstanceImpl extends AbstractInstance {

        public AbstractInstanceImpl() {
            super();
        }

        public AbstractInstanceImpl(Object classValue) {
            super(classValue);
        }

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

        @Override
        public Instance copy() {
            return null;
        }

    }

}