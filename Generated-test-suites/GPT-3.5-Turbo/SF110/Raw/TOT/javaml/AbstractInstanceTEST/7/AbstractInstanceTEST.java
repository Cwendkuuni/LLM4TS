
package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

public class AbstractInstanceTest {

    private AbstractInstance instance;

    @Before
    public void setup() {
        instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }

            @Override
            public double value(int index) {
                double[] values = {1.0, 2.0, 3.0};
                return values[index];
            }

            @Override
            public void put(int index, double value) {
                throw new UnsupportedOperationException("Cannot put value in instance.");
            }

            @Override
            public void remove(int index) {
                throw new UnsupportedOperationException("Cannot remove value from instance.");
            }
        };
    }

    @Test
    public void testGetID() {
        int expectedID = 0;
        int actualID = instance.getID();

        Assert.assertEquals(expectedID, actualID);
    }

    @Test
    public void testIterator() {
        Iterator<Double> iterator = instance.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1.0, iterator.next(), 1e-5);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2.0, iterator.next(), 1e-5);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(3.0, iterator.next(), 1e-5);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testClassValue() {
        Object expectedClassValue = null;
        Object actualClassValue = instance.classValue();

        Assert.assertEquals(expectedClassValue, actualClassValue);
    }

    @Test
    public void testSetClassValue() {
        Object expectedClassValue = "class1";
        instance.setClassValue(expectedClassValue);
        Object actualClassValue = instance.classValue();

        Assert.assertEquals(expectedClassValue, actualClassValue);
    }

    @Test
    public void testMinus_WithInstance() {
        Instance min = new DenseInstance(new double[]{0.5, 0.5, 0.5});

        Instance expectedInstance = new DenseInstance(new double[]{0.5, 1.5, 2.5});
        Instance actualInstance = instance.minus(min);

        Assert.assertEquals(expectedInstance, actualInstance);
    }

    @Test
    public void testMinus_WithDouble() {
        double min = 0.5;

        Instance expectedInstance = new DenseInstance(new double[]{0.5, 1.5, 2.5});
        Instance actualInstance = instance.minus(min);

        Assert.assertEquals(expectedInstance, actualInstance);
    }

    @Test
    public void testDivide() {
        double divisor = 2.0;

        Instance expectedInstance = new DenseInstance(new double[]{0.5, 1.0, 1.5});
        Instance actualInstance = instance.divide(divisor);

        Assert.assertEquals(expectedInstance, actualInstance);
    }

    @Test
    public void testMultiply() {
        double multiplier = 2.0;

        Instance expectedInstance = new DenseInstance(new double[]{2.0, 4.0, 6.0});
        Instance actualInstance = instance.multiply(multiplier);

        Assert.assertEquals(expectedInstance, actualInstance);
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 0;
        int actualHashCode = instance.hashCode();

        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testEquals_SameInstance() {
        Assert.assertEquals(instance, instance);
    }

    @Test
    public void testEquals_DifferentInstance() {
        Assert.assertNotEquals(instance, new DenseInstance(new double[]{1.0, 2.0, 3.0}));
    }

    @Test
    public void testMultiply_WithInstance() {
        Instance value = new DenseInstance(new double[]{2.0, 2.0, 2.0});

        Instance expectedInstance = new DenseInstance(new double[]{2.0, 4.0, 6.0});
        Instance actualInstance = instance.multiply(value);

        Assert.assertEquals(expectedInstance, actualInstance);
    }

    @Test
    public void testDivide_WithInstance() {
        Instance min = new DenseInstance(new double[]{2.0, 2.0, 2.0});

        Instance expectedInstance = new DenseInstance(new double[]{0.5, 1.0, 1.5});
        Instance actualInstance = instance.divide(min);

        Assert.assertEquals(expectedInstance, actualInstance);
    }

    @Test
    public void testAdd_Double() {
        double min = 2.0;

        Instance expectedInstance = new DenseInstance(new double[]{3.0, 4.0, 5.0});
        Instance actualInstance = instance.add(min);

        Assert.assertEquals(expectedInstance, actualInstance);
    }

    @Test
    public void testAdd_Instance() {
        Instance min = new DenseInstance(new double[]{2.0, 2.0, 2.0});

        Instance expectedInstance = new DenseInstance(new double[]{3.0, 4.0, 5.0});
        Instance actualInstance = instance.add(min);

        Assert.assertEquals(expectedInstance, actualInstance);
    }

    @Test
    public void testSqrt() {
        Instance expectedInstance = new DenseInstance(new double[]{1.0, 1.4142135623730951, 1.7320508075688772});
        Instance actualInstance = instance.sqrt();

        Assert.assertEquals(expectedInstance, actualInstance);
    }

}
