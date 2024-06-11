import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {

    private AbstractInstance instance;
    
    @Before
    public void setUp() {
        instance = new AbstractInstance() {
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
        };
    }
    
    @Test
    public void testGetID() {
        assertEquals(0, instance.getID());
    }
    
    @Test
    public void testIterator() {
        Iterator<Double> iterator = instance.iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testClassValue() {
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        Object classValue = new Object();
        instance.setClassValue(classValue);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testMinusWithInstance() {
        Instance min = new DenseInstance(new double[0]);
        Instance out = instance.minus(min);
        assertEquals(min.noAttributes(), out.noAttributes());
    }
    
    @Test
    public void testMinusWithDouble() {
        double min = 0;
        Instance out = instance.minus(min);
        assertEquals(instance.noAttributes(), out.noAttributes());
    }
    
    @Test
    public void testDivide() {
        double min = 0;
        Instance out = instance.divide(min);
        assertEquals(instance.noAttributes(), out.noAttributes());
    }
    
    @Test
    public void testMultiply() {
        double value = 0;
        Instance out = instance.multiply(value);
        assertEquals(instance.noAttributes(), out.noAttributes());
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
        
        AbstractInstance other = new AbstractInstance() {
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
        };
        
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testMultiplyWithInstance() {
        Instance value = new DenseInstance(new double[0]);
        Instance out = instance.multiply(value);
        assertEquals(instance.noAttributes(), out.noAttributes());
    }
    
    @Test
    public void testDivideWithInstance() {
        Instance min = new DenseInstance(new double[0]);
        Instance out = instance.divide(min);
        assertEquals(instance.noAttributes(), out.noAttributes());
    }
    
    @Test
    public void testAddWithDouble() {
        double min = 0;
        Instance out = instance.add(min);
        assertEquals(instance.noAttributes(), out.noAttributes());
    }
    
    @Test
    public void testAddWithInstance() {
        Instance min = new DenseInstance(new double[0]);
        Instance out = instance.add(min);
        assertEquals(instance.noAttributes(), out.noAttributes());
    }
    
    @Test
    public void testSqrt() {
        Instance out = instance.sqrt();
        assertEquals(instance.noAttributes(), out.noAttributes());
    }
    
    class DenseInstance implements Instance {
        private double[] values;
        
        public DenseInstance(double[] values) {
            this.values = values;
        }

        @Override
        public int noAttributes() {
            return values.length;
        }

        @Override
        public double value(int index) {
            return values[index];
        }

        @Override
        public void put(int index, double value) {
            values[index] = value;
        }
        
        @Override
        public int getID() {
            return 0;
        }

        @Override
        public Iterator<Double> iterator() {
            return null;
        }

        @Override
        public Object classValue() {
            return null;
        }

        @Override
        public void setClassValue(Object classValue) {
            
        }

        @Override
        public Instance minus(Instance min) {
            return null;
        }

        @Override
        public Instance divide(double min) {
            return null;
        }

        @Override
        public Instance multiply(double value) {
            return null;
        }

        @Override
        public Instance sqrt() {
            return null;
        }

        @Override
        public Instance add(double min) {
            return null;
        }

        @Override
        public Instance add(Instance min) {
            return null;
        }

        @Override
        public Instance divide(Instance min) {
            return null;
        }

        @Override
        public Instance multiply(Instance value) {
            return null;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
}