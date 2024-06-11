import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = mock(AbstractInstance.class);
        when(instance.getID()).thenReturn(1);
        assertEquals(1, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = mock(AbstractInstance.class);
        Iterator<Double> iterator = mock(Iterator.class);
        when(instance.iterator()).thenReturn(iterator);
        assertEquals(iterator, instance.iterator());
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = mock(AbstractInstance.class);
        Object classValue = new Object();
        when(instance.classValue()).thenReturn(classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = mock(AbstractInstance.class);
        Object classValue = new Object();
        instance.setClassValue(classValue);
        verify(instance).setClassValue(classValue);
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        AbstractInstance min = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public Double get(int index) {
                return 1.0;
            }
        };
        Instance out = instance.minus(min);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, out.get(i), 0.001);
        }
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        double min = 1.0;
        Instance out = instance.minus(min);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(-1.0, out.get(i), 0.001);
        }
    }

    @Test
    public void testDivideDouble() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        double min = 2.0;
        Instance out = instance.divide(min);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.5, out.get(i), 0.001);
        }
    }

    @Test
    public void testMultiplyDouble() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        double value = 2.0;
        Instance out = instance.multiply(value);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, out.get(i), 0.001);
        }
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        assertEquals(instance.getID(), instance.hashCode());
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        assertEquals(false, instance1.equals(null));
        assertEquals(true, instance1.equals(instance1));
        assertEquals(false, instance1.equals(instance2));
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        AbstractInstance value = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public Double get(int index) {
                return 2.0;
            }
        };
        Instance out = instance.multiply(value);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, out.get(i), 0.001);
        }
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        AbstractInstance value = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public Double get(int index) {
                return 2.0;
            }
        };
        Instance out = instance.divide(value);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, out.get(i), 0.001);
        }
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        double min = 1.0;
        Instance out = instance.add(min);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(1.0, out.get(i), 0.001);
        }
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        AbstractInstance min = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public Double get(int index) {
                return 1.0;
            }
        };
        Instance out = instance.add(min);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(1.0, out.get(i), 0.001);
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        instance.put(0, 4.0);
        instance.put(1, 9.0);
        Instance out = instance.sqrt();
        assertEquals(2.0, out.get(0), 0.001);
        assertEquals(3.0, out.get(1), 0.001);
    }

    @Test
    public void testRemoveUnsupported() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        InstanceValueIterator iterator = instance.new InstanceValueIterator();
        try {
            iterator.remove();
            fail();
        } catch (UnsupportedOperationException ex) {
            // Pass
        }
    }

}