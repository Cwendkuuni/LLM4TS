import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AbstractInstanceTest {
    
    private AbstractInstance abstractInstance;

    @Before
    public void setUp() {
        abstractInstance = new AbstractInstanceMock();
    }
    
    @Test
    public void testConstructorWithClassValue() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstanceMock(classValue);
        
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testConstructorWithoutClassValue() {
        AbstractInstance instance = new AbstractInstanceMock();
        
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        Object classValue = new Object();
        abstractInstance.setClassValue(classValue);
        
        assertEquals(classValue, abstractInstance.classValue());
    }
    
    @Test
    public void testMinusWithInstance() {
        Instance min = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        
        Instance result = abstractInstance.minus(min);
        
        assertNotNull(result);
        Iterator<Double> iterator = result.iterator();
        assertEquals(-1.0, iterator.next(), 0.001);
        assertEquals(-2.0, iterator.next(), 0.001);
        assertEquals(-3.0, iterator.next(), 0.001);
    }
    
    @Test
    public void testMinusWithDouble() {
        double min = 2.0;
        
        Instance result = abstractInstance.minus(min);
        
        assertNotNull(result);
        Iterator<Double> iterator = result.iterator();
        assertEquals(-2.0, iterator.next(), 0.001);
        assertEquals(-4.0, iterator.next(), 0.001);
        assertEquals(-6.0, iterator.next(), 0.001);
    }
    
    @Test
    public void testDivide() {
        double min = 2.0;
        
        Instance result = abstractInstance.divide(min);
        
        assertNotNull(result);
        Iterator<Double> iterator = result.iterator();
        assertEquals(0.5, iterator.next(), 0.001);
        assertEquals(1.0, iterator.next(), 0.001);
        assertEquals(1.5, iterator.next(), 0.001);
    }
    
    @Test
    public void testMultiply() {
        double value = 2.0;
        
        Instance result = abstractInstance.multiply(value);
        
        assertNotNull(result);
        Iterator<Double> iterator = result.iterator();
        assertEquals(2.0, iterator.next(), 0.001);
        assertEquals(4.0, iterator.next(), 0.001);
        assertEquals(6.0, iterator.next(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = System.identityHashCode(abstractInstance);
        
        int hashCode = abstractInstance.hashCode();
        
        assertEquals(expectedHashCode, hashCode);
    }
    
    @Test
    public void testEquals() {
        AbstractInstance otherInstance = new AbstractInstanceMock();
        
        assertTrue(abstractInstance.equals(abstractInstance));
        assertFalse(abstractInstance.equals(null));
        assertFalse(abstractInstance.equals(new Object()));
        assertFalse(abstractInstance.equals(otherInstance));
    }
    
    @Test
    public void testMultiplyWithInstance() {
        Instance value = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        
        Instance result = abstractInstance.multiply(value);
        
        assertNotNull(result);
        Iterator<Double> iterator = result.iterator();
        assertEquals(1.0, iterator.next(), 0.001);
        assertEquals(4.0, iterator.next(), 0.001);
        assertEquals(9.0, iterator.next(), 0.001);
    }
    
    @Test
    public void testDivideWithInstance() {
        Instance min = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        
        Instance result = abstractInstance.divide(min);
        
        assertNotNull(result);
        Iterator<Double> iterator = result.iterator();
        assertEquals(1.0, iterator.next(), 0.001);
        assertEquals(1.0, iterator.next(), 0.001);
        assertEquals(1.0, iterator.next(), 0.001);
    }
    
    @Test
    public void testAddWithDouble() {
        double min = 2.0;
        
        Instance result = abstractInstance.add(min);
        
        assertNotNull(result);
        Iterator<Double> iterator = result.iterator();
        assertEquals(2.0, iterator.next(), 0.001);
        assertEquals(4.0, iterator.next(), 0.001);
        assertEquals(6.0, iterator.next(), 0.001);
    }
    
    @Test
    public void testAddWithInstance() {
        Instance min = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        
        Instance result = abstractInstance.add(min);
        
        assertNotNull(result);
        Iterator<Double> iterator = result.iterator();
        assertEquals(2.0, iterator.next(), 0.001);
        assertEquals(4.0, iterator.next(), 0.001);
        assertEquals(6.0, iterator.next(), 0.001);
    }
    
    @Test
    public void testSqrt() {
        Instance result = abstractInstance.sqrt();
        
        assertNotNull(result);
        Iterator<Double> iterator = result.iterator();
        assertEquals(0.0, iterator.next(), 0.001);
        assertEquals(1.0, iterator.next(), 0.001);
        assertEquals(1.414, iterator.next(), 0.001);
    }
    
    private class AbstractInstanceMock extends AbstractInstance {
        
        private static final long serialVersionUID = 1L;

        protected AbstractInstanceMock() {
            super();
        }
        
        protected AbstractInstanceMock(Object classValue) {
            super(classValue);
        }

        @Override
        public double value(int index) {
            return index;
        }

        @Override
        public int noAttributes() {
            return 3;
        }
        
    }
}