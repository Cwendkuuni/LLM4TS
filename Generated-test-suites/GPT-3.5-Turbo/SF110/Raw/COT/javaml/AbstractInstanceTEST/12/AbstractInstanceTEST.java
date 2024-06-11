
package net.sf.javaml.core;

import org.junit.Test;
import java.util.Iterator;

import static org.junit.Assert.*;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {};
        int id = instance.getID();
        assertEquals(0, id);
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {};

        Iterator<Double> iterator = instance.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstance() {};

        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {};

        Object classValue = new Object();
        instance.setClassValue(classValue);

        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testMinus_WithInstance() {
        AbstractInstance instance = new AbstractInstance() {};

        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance output = instance.minus(min);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, output.get(i), 0.0);
        }
    }

    @Test
    public void testMinus_WithDouble() {
        AbstractInstance instance = new AbstractInstance() {};

        double min = 0.0;
        Instance output = instance.minus(min);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, output.get(i), 0.0);
        }
    }

    @Test
    public void testDivide_WithDouble() {
        AbstractInstance instance = new AbstractInstance() {};

        double min = 1.0;
        Instance output = instance.divide(min);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, output.get(i), 0.0);
        }
    }

    @Test
    public void testMultiply_WithDouble() {
        AbstractInstance instance = new AbstractInstance() {};

        double value = 1.0;
        Instance output = instance.multiply(value);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, output.get(i), 0.0);
        }
    }

    @Test
    public void testMultiply_WithInstance() {
        AbstractInstance instance = new AbstractInstance() {};

        Instance value = new DenseInstance(new double[instance.noAttributes()]);
        Instance output = instance.multiply(value);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, output.get(i), 0.0);
        }
    }

    @Test
    public void testDivide_WithInstance() {
        AbstractInstance instance = new AbstractInstance() {};

        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance output = instance.divide(min);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(Double.POSITIVE_INFINITY, output.get(i), 0.0);
        }
    }

    @Test
    public void testAdd_WithDouble() {
        AbstractInstance instance = new AbstractInstance() {};

        double min = 0.0;
        Instance output = instance.add(min);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, output.get(i), 0.0);
        }
    }

    @Test
    public void testAdd_WithInstance() {
        AbstractInstance instance = new AbstractInstance() {};

        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance output = instance.add(min);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, output.get(i), 0.0);
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {};

        Instance output = instance.sqrt();

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(Double.NaN, output.get(i), 0.0);
        }
    }
}
