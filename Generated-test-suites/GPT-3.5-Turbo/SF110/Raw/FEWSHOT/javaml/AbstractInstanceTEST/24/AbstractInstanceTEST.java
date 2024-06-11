package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {
        };
        assertEquals(0, instance.getID());
    }
    
    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {
        };
        assertFalse(instance.iterator().hasNext());
        instance.put(0, 1.0);
        assertTrue(instance.iterator().hasNext());
        assertEquals(Double.valueOf(1.0), instance.iterator().next());
    }
    
    @Test
    public void testAbstractInstance() {
        AbstractInstance instance = new AbstractInstance() {
        };
        assertNull(instance.classValue());
    }
    
    @Test
    public void testAbstractInstanceWithClassValue() {
        Object obj = new Object();
        AbstractInstance instance = new AbstractInstance(obj) {
        };
        assertEquals(obj, instance.classValue());
    }
    
    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {
        };
        Object obj = new Object();
        instance.setClassValue(obj);
        assertEquals(obj, instance.classValue());
    }
    
    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance = new AbstractInstance() {
        };
        AbstractInstance min = new AbstractInstance() {
        };
        AbstractInstance result = (AbstractInstance) instance.minus(min);
        assertNotNull(result);
    }
    
    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new AbstractInstance() {
        };
        double min = 1.0;
        AbstractInstance result = (AbstractInstance) instance.minus(min);
        assertNotNull(result);
    }
    
    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstance() {
        };
        double div = 2.0;
        AbstractInstance result = (AbstractInstance) instance.divide(div);
        assertNotNull(result);
    }
    
    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstance() {
        };
        double mul = 2.0;
        AbstractInstance result = (AbstractInstance) instance.multiply(mul);
        assertNotNull(result);
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {
        };
        assertEquals(0, instance.hashCode());
    }
    
    @Test
    public void testEqualsWithSameObject() {
        AbstractInstance instance = new AbstractInstance() {
        };
        assertTrue(instance.equals(instance));
    }
    
    @Test
    public void testEqualsWithNull() {
        AbstractInstance instance = new AbstractInstance() {
        };
        assertFalse(instance.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        AbstractInstance instance = new AbstractInstance() {
        };
        assertFalse(instance.equals(new Object()));
    }
    
    @Test
    public void testEquals() {
        AbstractInstance instance = new AbstractInstance() {
        };
        AbstractInstance other = new AbstractInstance() {
        };
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance = new AbstractInstance() {
        };
        AbstractInstance value = new AbstractInstance() {
        };
        AbstractInstance result = (AbstractInstance) instance.multiply(value);
        assertNotNull(result);
    }
    
    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance = new AbstractInstance() {
        };
        AbstractInstance value = new AbstractInstance() {
        };
        AbstractInstance result = (AbstractInstance) instance.divide(value);
        assertNotNull(result);
    }
    
    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new AbstractInstance() {
        };
        double add = 1.0;
        AbstractInstance result = (AbstractInstance) instance.add(add);
        assertNotNull(result);
    }
    
    @Test
    public void testAddWithInstance() {
        AbstractInstance instance = new AbstractInstance() {
        };
        AbstractInstance add = new AbstractInstance() {
        };
        AbstractInstance result = (AbstractInstance) instance.add(add);
        assertNotNull(result);
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {
        };
        AbstractInstance result = (AbstractInstance) instance.sqrt();
        assertNotNull(result);
    }
}