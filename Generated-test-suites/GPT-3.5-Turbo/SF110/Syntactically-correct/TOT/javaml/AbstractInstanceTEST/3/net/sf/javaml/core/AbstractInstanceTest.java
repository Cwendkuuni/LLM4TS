import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;
import static org.junit.Assert.*;

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
                return index + 1;
            }
        };
    }

    @Test
    public void testGetID() {
        int id = instance.getID();
        assertEquals(0, id);
    }

    @Test
    public void testIterator() {
        Iterator<Double> iterator = instance.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(1.0, iterator.next(), 0.001);
        assertTrue(iterator.hasNext());
        assertEquals(2.0, iterator.next(), 0.001);
        assertTrue(iterator.hasNext());
        assertEquals(3.0, iterator.next(), 0.001);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testClassValue() {
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        instance.setClassValue("Class A");
        assertEquals("Class A", instance.classValue());
    }

    @Test
    public void testMinusInstance() {
        Instance min = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }

            @Override
            public double value(int index) {
                return index + 2;
            }
        };
        Instance result = instance.minus(min);
        assertNotNull(result);
        assertEquals(-1.0, result.value(0), 0.001);
        assertEquals(-1.0, result.value(1), 0.001);
        assertEquals(-1.0, result.value(2), 0.001);
    }

    @Test
    public void testMinusDouble() {
        double min = 2.0;
        Instance result = instance.minus(min);
        assertNotNull(result);
        assertEquals(-1.0, result.value(0), 0.001);
        assertEquals(0.0, result.value(1), 0.001);
        assertEquals(1.0, result.value(2), 0.001);
    }

    @Test
    public void testDivideDouble() {
        double div = 2.0;
        Instance result = instance.divide(div);
        assertNotNull(result);
        assertEquals(0.5, result.value(0), 0.001);
        assertEquals(1.0, result.value(1), 0.001);
        assertEquals(1.5, result.value(2), 0.001);
    }

    @Test
    public void testMultiplyDouble() {
        double mul = 2.0;
        Instance result = instance.multiply(mul);
        assertNotNull(result);
        assertEquals(2.0, result.value(0), 0.001);
        assertEquals(4.0, result.value(1), 0.001);
        assertEquals(6.0, result.value(2), 0.001);
    }

    @Test
    public void testHashCode() {
        int hashCode = instance.hashCode();
        assertEquals(0, hashCode);
    }

    @Test
    public void testEquals() {
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
        AbstractInstance differentInstance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }

            @Override
            public double value(int index) {
                return index + 2;
            }
        };
        assertTrue(instance.equals(instance));
        assertTrue(instance.equals(sameInstance));
        assertFalse(instance.equals(differentInstance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals("string"));
    }

    @Test
    public void testMultiplyInstance() {
        Instance value = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }

            @Override
            public double value(int index) {
                return index + 2;
            }
        };
        Instance result = instance.multiply(value);
        assertNotNull(result);
        assertEquals(1.0, result.value(0), 0.001);
        assertEquals(4.0, result.value(1), 0.001);
        assertEquals(9.0, result.value(2), 0.001);
    }

    @Test
    public void testDivideInstance() {
        Instance min = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }

            @Override
            public double value(int index) {
                return index + 2;
            }
        };
        Instance result = instance.divide(min);
        assertNotNull(result);
        assertEquals(0.5, result.value(0), 0.001);
        assertEquals(0.666, result.value(1), 0.001);
        assertEquals(0.75, result.value(2), 0.001);
    }

    @Test
    public void testAddDouble() {
        double add = 2.0;
        Instance result = instance.add(add);
        assertNotNull(result);
        assertEquals(3.0, result.value(0), 0.001);
        assertEquals(4.0, result.value(1), 0.001);
        assertEquals(5.0, result.value(2), 0.001);
    }

    @Test
    public void testAddInstance() {
        Instance min = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }

            @Override
            public double value(int index) {
                return index + 2;
            }
        };
        Instance result = instance.add(min);
        assertNotNull(result);
        assertEquals(3.0, result.value(0), 0.001);
        assertEquals(4.0, result.value(1), 0.001);
        assertEquals(5.0, result.value(2), 0.001);
    }

    @Test
    public void testSqrt() {
        Instance result = instance.sqrt();
        assertNotNull(result);
        assertEquals(1.0, result.value(0), 0.001);
        assertEquals(1.414, result.value(1), 0.001);
        assertEquals(1.732, result.value(2), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        Iterator<Double> iterator = instance.iterator();
        iterator.remove();
    }

}