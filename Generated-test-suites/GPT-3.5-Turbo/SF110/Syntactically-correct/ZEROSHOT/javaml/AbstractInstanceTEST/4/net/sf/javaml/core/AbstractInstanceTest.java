import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class AbstractInstanceTest {

    private AbstractInstance instance;

    @Before
    public void setUp() {
        instance = new AbstractInstanceTestExtension();
    }

    @Test
    public void testGetID() {
        int id = instance.getID();
        assertEquals(AbstractInstanceTestExtension.TEST_ID, id);
    }

    @Test
    public void testIterator() {
        Iterator<Double> iterator = instance.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Double value = iterator.next();
            assertNotNull(value);
            count++;
        }
        assertEquals(AbstractInstanceTestExtension.ATTRIBUTE_COUNT, count);
    }

    @Test
    public void testClassValue() {
        Object classValue = instance.classValue();
        assertNull(classValue);
    }

    @Test
    public void testSetClassValue() {
        Object testClassValue = new Object();
        instance.setClassValue(testClassValue);
        Object classValue = instance.classValue();
        assertEquals(testClassValue, classValue);
    }

    @Test
    public void testMinusWithInstance() {
        Instance min = new AbstractInstanceTestExtension();
        Instance result = instance.minus(min);
        assertNotNull(result);
        assertEquals(AbstractInstanceTestExtension.ATTRIBUTE_COUNT, result.noAttributes());
    }

    @Test
    public void testMinusWithDouble() {
        double min = 1.0;
        Instance result = instance.minus(min);
        assertNotNull(result);
        assertEquals(AbstractInstanceTestExtension.ATTRIBUTE_COUNT, result.noAttributes());
    }

    @Test
    public void testDivide() {
        double min = 2.0;
        Instance result = instance.divide(min);
        assertNotNull(result);
        assertEquals(AbstractInstanceTestExtension.ATTRIBUTE_COUNT, result.noAttributes());
    }

    @Test
    public void testMultiplyWithDouble() {
        double value = 2.0;
        Instance result = instance.multiply(value);
        assertNotNull(result);
        assertEquals(AbstractInstanceTestExtension.ATTRIBUTE_COUNT, result.noAttributes());
    }

    @Test
    public void testHashCode() {
        int hashCode = instance.hashCode();
        assertEquals(AbstractInstanceTestExtension.TEST_ID, hashCode);
    }

    @Test
    public void testEquals() {
        AbstractInstance other = new AbstractInstanceTestExtension();
        boolean equals = instance.equals(other);
        assertTrue(equals);
    }

    @Test
    public void testMultiplyWithInstance() {
        Instance value = new AbstractInstanceTestExtension();
        Instance result = instance.multiply(value);
        assertNotNull(result);
        assertEquals(AbstractInstanceTestExtension.ATTRIBUTE_COUNT, result.noAttributes());
    }

    @Test
    public void testDivideWithInstance() {
        Instance min = new AbstractInstanceTestExtension();
        Instance result = instance.divide(min);
        assertNotNull(result);
        assertEquals(AbstractInstanceTestExtension.ATTRIBUTE_COUNT, result.noAttributes());
    }

    @Test
    public void testAddWithDouble() {
        double min = 1.0;
        Instance result = instance.add(min);
        assertNotNull(result);
        assertEquals(AbstractInstanceTestExtension.ATTRIBUTE_COUNT, result.noAttributes());
    }

    @Test
    public void testAddWithInstance() {
        Instance min = new AbstractInstanceTestExtension();
        Instance result = instance.add(min);
        assertNotNull(result);
        assertEquals(AbstractInstanceTestExtension.ATTRIBUTE_COUNT, result.noAttributes());
    }

    @Test
    public void testSqrt() {
        Instance result = instance.sqrt();
        assertNotNull(result);
        assertEquals(AbstractInstanceTestExtension.ATTRIBUTE_COUNT, result.noAttributes());
    }

    private static class AbstractInstanceTestExtension extends AbstractInstance {

        public static final int TEST_ID = 1;
        public static final int ATTRIBUTE_COUNT = 5;

        public AbstractInstanceTestExtension() {
            super(null);
        }

        @Override
        public int noAttributes() {
            return ATTRIBUTE_COUNT;
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