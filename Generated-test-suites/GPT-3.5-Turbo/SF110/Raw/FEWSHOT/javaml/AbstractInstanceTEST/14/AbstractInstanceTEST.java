
package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstanceInstance();
        assertEquals(0, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstanceInstance();
        Iterator<Double> iterator = instance.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void testAbstractInstance() {
        AbstractInstance instance = new AbstractInstanceInstance();
        assertNotNull(instance);
    }

    @Test
    public void testAbstractInstanceObject() {
        AbstractInstance instance = new AbstractInstanceInstance("classValue");
        assertNotNull(instance);
        assertEquals("classValue", instance.classValue());
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstanceInstance("classValue");
        assertEquals("classValue", instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstanceInstance();
        instance.setClassValue("classValue");
        assertEquals("classValue", instance.classValue());
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new AbstractInstanceInstance();
        AbstractInstance min = new AbstractInstanceInstance();

        Instance result = instance.minus(min);
        assertNotNull(result);
        assertEquals(instance.noAttributes(), result.noAttributes());
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstanceInstance();

        Instance result = instance.minus(2.0);
        assertNotNull(result);
        assertEquals(instance.noAttributes(), result.noAttributes());
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstanceInstance();

        Instance result = instance.divide(2.0);
        assertNotNull(result);
        assertEquals(instance.noAttributes(), result.noAttributes());
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstanceInstance();

        Instance result = instance.multiply(2.0);
        assertNotNull(result);
        assertEquals(instance.noAttributes(), result.noAttributes());
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstanceInstance();
        assertEquals(0, instance.hashCode());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstanceInstance();
        AbstractInstance instance2 = new AbstractInstanceInstance();
        AbstractInstance instance3 = new AbstractInstanceInstance("classValue");

        assertEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance = new AbstractInstanceInstance();
        AbstractInstance value = new AbstractInstanceInstance();

        Instance result = instance.multiply(value);
        assertNotNull(result);
        assertEquals(instance.noAttributes(), result.noAttributes());
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new AbstractInstanceInstance();
        AbstractInstance value = new AbstractInstanceInstance();

        Instance result = instance.divide(value);
        assertNotNull(result);
        assertEquals(instance.noAttributes(), result.noAttributes());
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new AbstractInstanceInstance();

        Instance result = instance.add(2.0);
        assertNotNull(result);
        assertEquals(instance.noAttributes(), result.noAttributes());
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance = new AbstractInstanceInstance();
        AbstractInstance value = new AbstractInstanceInstance();

        Instance result = instance.add(value);
        assertNotNull(result);
        assertEquals(instance.noAttributes(), result.noAttributes());
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstanceInstance();

        Instance result = instance.sqrt();
        assertNotNull(result);
        assertEquals(instance.noAttributes(), result.noAttributes());
    }

    class AbstractInstanceInstance extends AbstractInstance {

        @Override
        public int noAttributes() {
            return 0;
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

