package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {
    
    private AbstractInstance instance;
    
    @Before
    public void setup() {
        instance = new ConcreteInstance();
    }
    
    @Test
    public void testGetID() {
        assertEquals(0, instance.getID());
    }
    
    @Test
    public void testIterator() {
        Iterator<Double> iterator = instance.iterator();
        assertEquals(true, iterator.hasNext());
        assertEquals(0.0, iterator.next(), 0.001);
        assertEquals(false, iterator.hasNext());
    }
    
    @Test
    public void testClassValue() {
        assertNull(instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        instance.setClassValue("classValue");
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testEquals() {
        AbstractInstance other = new ConcreteInstance();
        assertEquals(true, instance.equals(other));
        
        other = new ConcreteInstance("classValue");
        assertEquals(false, instance.equals(other));
        
        other = new ConcreteInstance();
        other.setClassValue("classValue");
        assertEquals(false, instance.equals(other));
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance other = new ConcreteInstance();
        assertEquals(instance.hashCode(), other.hashCode());
        
        other = new ConcreteInstance("classValue");
        assertNotEquals(instance.hashCode(), other.hashCode());
        
        other = new ConcreteInstance();
        other.setClassValue("classValue");
        assertNotEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testMinusInstance() {
        Map<Integer, Double> attr1 = Map.of(0, 1.0, 1, 2.0);
        Map<Integer, Double> attr2 = Map.of(0, 0.5, 1, 1.5);
        AbstractInstance min = new ConcreteInstance(attr2);
        
        AbstractInstance out = (AbstractInstance) instance.minus(min);
        assertEquals(0.5, out.value(0), 0.001);
        assertEquals(0.5, out.value(1), 0.001);
    }
    
    @Test
    public void testMinusDouble() {
        double min = 1.5;
        AbstractInstance out = (AbstractInstance) instance.minus(min);
        assertEquals(-0.5, out.value(0), 0.001);
        assertEquals(-0.5, out.value(1), 0.001);
    }
    
    @Test
    public void testDivideDouble() {
        double min = 2.0;
        AbstractInstance out = (AbstractInstance) instance.divide(min);
        assertEquals(0.5, out.value(0), 0.001);
        assertEquals(0.5, out.value(1), 0.001);
    }
    
    @Test
    public void testMultiplyDouble() {
        double value = 2.0;
        AbstractInstance out = (AbstractInstance) instance.multiply(value);
        assertEquals(2.0, out.value(0), 0.001);
        assertEquals(2.0, out.value(1), 0.001);
    }
    
    @Test
    public void testMultiplyInstance() {
        Map<Integer, Double> attr1 = Map.of(0, 1.0, 1, 2.0);
        Map<Integer, Double> attr2 = Map.of(0, 0.5, 1, 1.5);
        AbstractInstance value = new ConcreteInstance(attr2);
        
        AbstractInstance out = (AbstractInstance) instance.multiply(value);
        assertEquals(0.5, out.value(0), 0.001);
        assertEquals(3.0, out.value(1), 0.001);
    }
    
    @Test
    public void testDivideInstance() {
        Map<Integer, Double> attr1 = Map.of(0, 1.0, 1, 2.0);
        Map<Integer, Double> attr2 = Map.of(0, 0.5, 1, 1.5);
        AbstractInstance min = new ConcreteInstance(attr2);
        
        AbstractInstance out = (AbstractInstance) instance.divide(min);
        assertEquals(2.0, out.value(0), 0.001);
        assertEquals(4.0/3.0, out.value(1), 0.001);
    }
    
    @Test
    public void testAddDouble() {
        double min = 2.0;
        AbstractInstance out = (AbstractInstance) instance.add(min);
        assertEquals(2.0, out.value(0), 0.001);
        assertEquals(2.0, out.value(1), 0.001);
    }
    
    @Test
    public void testAddInstance() {
        Map<Integer, Double> attr1 = Map.of(0, 1.0, 1, 2.0);
        Map<Integer, Double> attr2 = Map.of(0, 0.5, 1, 1.5);
        AbstractInstance min = new ConcreteInstance(attr2);
        
        AbstractInstance out = (AbstractInstance) instance.add(min);
        assertEquals(1.5, out.value(0), 0.001);
        assertEquals(3.5, out.value(1), 0.001);
    }
    
    @Test
    public void testSqrt() {
        double value = 4.0;
        AbstractInstance out = (AbstractInstance) instance.sqrt();
        assertEquals(2.0, out.value(0), 0.001);
        assertEquals(2.0, out.value(1), 0.001);
    }
    
    private class ConcreteInstance extends AbstractInstance {
        
        public ConcreteInstance() {
            super();
        }
        
        public ConcreteInstance(Object classValue) {
            super(classValue);
        }
        
        public ConcreteInstance(Map<Integer, Double> attributes) {
            super();
            for (Map.Entry<Integer, Double> entry : attributes.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public int noAttributes() {
            return 2;
        }

        @Override
        public double value(int index) {
            if (index == 0) {
                return 1.0;
            } else if (index == 1) {
                return 2.0;
            } else {
                throw new IllegalArgumentException("Invalid index");
            }
        }

        @Override
        public void put(int index, double value) {
            // do nothing
        }
        
    }
   
}