import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }

            @Override
            public double value(int index) {
                return 0;
            }
        };
        assertEquals(0, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }

            @Override
            public double value(int index) {
                return 0;
            }
        };
        assertFalse(instance.iterator().hasNext());
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }

            @Override
            public double value(int index) {
                return 0;
            }
        };
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }

            @Override
            public double value(int index) {
                return 0;
            }
        };
        instance.setClassValue(10);
        assertEquals(10, instance.classValue());
    }

    @Test
    public void testMinus_Instance() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 5;
            }
        };
        AbstractInstance min = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 2;
            }
        };

        AbstractInstance result = instance.minus(min);
        assertEquals(3, result.value(0), 0.001);
        assertEquals(3, result.value(1), 0.001);
    }

    @Test
    public void testMinus_double() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 5;
            }
        };

        AbstractInstance result = instance.minus(2);
        assertEquals(3, result.value(0), 0.001);
        assertEquals(3, result.value(1), 0.001);
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 6;
            }
        };

        AbstractInstance result = instance.divide(2);
        assertEquals(3, result.value(0), 0.001);
        assertEquals(3, result.value(1), 0.001);
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 3;
            }
        };

        AbstractInstance result = instance.multiply(2);
        assertEquals(6, result.value(0), 0.001);
        assertEquals(6, result.value(1), 0.001);
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }

            @Override
            public double value(int index) {
                return 0;
            }
        };
        assertEquals(instance.hashCode(), instance.getID());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }

            @Override
            public double value(int index) {
                return 0;
            }
        };
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }

            @Override
            public double value(int index) {
                return 0;
            }
        };

        assertFalse(instance1.equals(null));
        assertTrue(instance1.equals(instance1));
        assertFalse(instance1.equals(new Object()));
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testMultiply_Instance() {
        AbstractInstance instance1 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 2;
            }
        };
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 3;
            }
        };

        AbstractInstance result = instance1.multiply(instance2);
        assertEquals(6, result.value(0), 0.001);
        assertEquals(6, result.value(1), 0.001);
    }

    @Test
    public void testDivide_Instance() {
        AbstractInstance instance1 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 6;
            }
        };
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 2;
            }
        };

        AbstractInstance result = instance1.divide(instance2);
        assertEquals(3, result.value(0), 0.001);
        assertEquals(3, result.value(1), 0.001);
    }

    @Test
    public void testAdd_double() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 5;
            }
        };

        AbstractInstance result = instance.add(2);
        assertEquals(7, result.value(0), 0.001);
        assertEquals(7, result.value(1), 0.001);
    }

    @Test
    public void testAdd_Instance() {
        AbstractInstance instance1 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 4;
            }
        };
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 2;
            }
        };

        AbstractInstance result = instance1.add(instance2);
        assertEquals(6, result.value(0), 0.001);
        assertEquals(6, result.value(1), 0.001);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return 9;
            }
        };

        AbstractInstance result = instance.sqrt();
        assertEquals(3, result.value(0), 0.001);
        assertEquals(3, result.value(1), 0.001);
    }

    @Test
    public void testIterator_HasNext() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 1;
            }

            @Override
            public double value(int index) {
                return 0;
            }
        };

        assertTrue(instance.iterator().hasNext());
    }

    @Test
    public void testIterator_Next() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public double value(int index) {
                return index + 1;
            }
        };

        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next(), 0.001);
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next(), 0.001);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 1;
            }

            @Override
            public double value(int index) {
                return 0;
            }
        };

        Iterator<Double> iterator = instance.iterator();
        iterator.remove();
    }
}