
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {};
        int expected = 0;
        int actual = instance.getID();
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {};
        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        instance.setClassValue("classValue");
        assertEquals("classValue", instance.classValue());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance1 = new AbstractInstance() {
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
                }
                return 0;
            }
        };
        
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
            
            @Override
            public double value(int index) {
                if (index == 0) {
                    return 0.5;
                } else if (index == 1) {
                    return 1.5;
                }
                return 0;
            }
        };

        Instance result = instance1.minus(instance2);

        assertEquals(0.5, result.value(0), 0.01);
        assertEquals(0.5, result.value(1), 0.01);
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new AbstractInstance() {
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
                }
                return 0;
            }
        };
        
        Instance result = instance.minus(0.5);

        assertEquals(0.5, result.value(0), 0.01);
        assertEquals(1.5, result.value(1), 0.01);
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
                if (index == 0) {
                    return 1.0;
                } else if (index == 1) {
                    return 2.0;
                }
                return 0;
            }
        };
        
        Instance result = instance.divide(2.0);

        assertEquals(0.5, result.value(0), 0.01);
        assertEquals(1.0, result.value(1), 0.01);
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
                if (index == 0) {
                    return 1.0;
                } else if (index == 1) {
                    return 2.0;
                }
                return 0;
            }
        };
        
        Instance result = instance.multiply(2.0);

        assertEquals(2.0, result.value(0), 0.01);
        assertEquals(4.0, result.value(1), 0.01);
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {};
        int expected = 0;
        int actual = instance.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        assertFalse(instance1.equals(instance2));
        assertTrue(instance1.equals(instance1));
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new AbstractInstance() {
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
                }
                return 0;
            }
        };
        
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
            
            @Override
            public double value(int index) {
                if (index == 0) {
                    return 0.5;
                } else if (index == 1) {
                    return 1.5;
                }
                return 0;
            }
        };

        Instance result = instance1.multiply(instance2);

        assertEquals(0.5, result.value(0), 0.01);
        assertEquals(3.0, result.value(1), 0.01);
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new AbstractInstance() {
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
                }
                return 0;
            }
        };
        
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
            
            @Override
            public double value(int index) {
                if (index == 0) {
                    return 0.5;
                } else if (index == 1) {
                    return 1.5;
                }
                return 0;
            }
        };

        Instance result = instance1.divide(instance2);

        assertEquals(2.0, result.value(0), 0.01);
        assertEquals(1.333, result.value(1), 0.01);
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new AbstractInstance() {
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
                }
                return 0;
            }
        };
        
        Instance result = instance.add(0.5);

        assertEquals(1.5, result.value(0), 0.01);
        assertEquals(2.5, result.value(1), 0.01);
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new AbstractInstance() {
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
                }
                return 0;
            }
        };
        
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
            
            @Override
            public double value(int index) {
                if (index == 0) {
                    return 0.5;
                } else if (index == 1) {
                    return 1.5;
                }
                return 0;
            }
        };

        Instance result = instance1.add(instance2);

        assertEquals(1.5, result.value(0), 0.01);
        assertEquals(3.5, result.value(1), 0.01);
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
                if (index == 0) {
                    return 4.0;
                } else if (index == 1) {
                    return 9.0;
                }
                return 0;
            }
        };
        
        Instance result = instance.sqrt();

        assertEquals(2.0, result.value(0), 0.01);
        assertEquals(3.0, result.value(1), 0.01);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInstanceValueIteratorRemove() {
        AbstractInstance instance = new AbstractInstance() {};
        Iterator<Double> iterator = instance.iterator();
        iterator.remove();
    }
}
