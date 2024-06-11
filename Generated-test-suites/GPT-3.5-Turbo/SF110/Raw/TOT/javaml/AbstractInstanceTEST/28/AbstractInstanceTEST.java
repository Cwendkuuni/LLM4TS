import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {

    private AbstractInstance instance;

    @Before
    public void setUp() {
        instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }
        
            @Override
            public double value(int index) {
                return index + 1;
            }
        };
    }

    @Test
    public void testGetID() {
        assertEquals(0, instance.getID());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Double value : instance) {
            assertEquals((double)(count + 1), value, 0.001);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testConstructorWithNullClassValue() {
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        instance.setClassValue("Class1");
        assertEquals("Class1", instance.classValue());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance min = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }
        
            @Override
            public double value(int index) {
                return index + 2;
            }
        };
        AbstractInstance result = instance.minus(min);
        assertEquals(-1.0, result.value(0), 0.001);
        assertEquals(-1.0, result.value(1), 0.001);
        assertEquals(-1.0, result.value(2), 0.001);
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance result = instance.minus(2.0);
        assertEquals(-1.0, result.value(0), 0.001);
        assertEquals(0.0, result.value(1), 0.001);
        assertEquals(1.0, result.value(2), 0.001);
    }

    @Test
    public void testDivide() {
        AbstractInstance result = instance.divide(2.0);
        assertEquals(0.5, result.value(0), 0.001);
        assertEquals(1.0, result.value(1), 0.001);
        assertEquals(1.5, result.value(2), 0.001);
    }

    @Test
    public void testMultiply() {
        AbstractInstance result = instance.multiply(2.0);
        assertEquals(2.0, result.value(0), 0.001);
        assertEquals(4.0, result.value(1), 0.001);
        assertEquals(6.0, result.value(2), 0.001);
    }

    @Test
    public void testHashCode() {
        assertEquals(0, instance.hashCode());
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(instance.equals(null));
    }

    @Test
    public void testEqualsWithSameInstance() {
        assertTrue(instance.equals(instance));
    }

    @Test
    public void testEqualsWithDifferentClassInstance() {
        assertFalse(instance.equals("Some other instance"));
    }

    @Test
    public void testEqualsWithSameClassInstance() {
        AbstractInstance sameInstance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }
        
            @Override
            public double value(int index) {
                return index + 1;
            }
        };
        assertTrue(instance.equals(sameInstance));
        assertTrue(sameInstance.equals(instance));
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance value = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }
        
            @Override
            public double value(int index) {
                return index + 1;
            }
        };
        AbstractInstance result = instance.multiply(value);
        assertEquals(1.0, result.value(0), 0.001);
        assertEquals(4.0, result.value(1), 0.001);
        assertEquals(9.0, result.value(2), 0.001);
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance min = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }
        
            @Override
            public double value(int index) {
                return index + 1;
            }
        };
        AbstractInstance result = instance.divide(min);
        assertEquals(1.0, result.value(0), 0.001);
        assertEquals(1.0, result.value(1), 0.001);
        assertEquals(1.0, result.value(2), 0.001);
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance result = instance.add(2.0);
        assertEquals(3.0, result.value(0), 0.001);
        assertEquals(4.0, result.value(1), 0.001);
        assertEquals(5.0, result.value(2), 0.001);
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance min = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }
        
            @Override
            public double value(int index) {
                return index + 2;
            }
        };
        AbstractInstance result = instance.add(min);
        assertEquals(3.0, result.value(0), 0.001);
        assertEquals(4.0, result.value(1), 0.001);
        assertEquals(5.0, result.value(2), 0.001);
    }

    @Test
    public void testSqrt() {
        AbstractInstance result = instance.sqrt();
        assertEquals(1.0, result.value(0), 0.001);
        assertEquals(Math.sqrt(2.0), result.value(1), 0.001);
        assertEquals(Math.sqrt(3.0), result.value(2), 0.001);
    }

    @Test
    public void testIteratorWithNoAttributes() {
        instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
        
            @Override
            public double value(int index) {
                return index + 1;
            }
        };
        assertFalse(instance.iterator().hasNext());
    }
    
    // Additional test cases for edge and error scenarios
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        instance.iterator().remove();
    }

    @Test
    public void testEmptyConstructor() {
        instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
        
            @Override
            public double value(int index) {
                return index + 1;
            }
        };
        assertNull(instance.classValue());
        assertEquals(0, instance.getID());
        assertFalse(instance.iterator().hasNext());
    }
}