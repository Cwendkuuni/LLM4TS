package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractInstanceTest {
    
    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        int expectedID = 0;
        int actualID = instance.getID();
        assertEquals(expectedID, actualID);
    }
    
    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        Iterator<Double> iterator = instance.iterator();
        assertNotNull(iterator);
    }
    
    @Test
    public void testClassValue() {
        Object classValue = new Object();
        AbstractInstance instance = new AbstractInstance(classValue) {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        Object expectedClassValue = classValue;
        Object actualClassValue = instance.classValue();
        assertEquals(expectedClassValue, actualClassValue);
    }
    
    @Test
    public void setClassValue() {
        Object newClassValue = new Object();
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        instance.setClassValue(newClassValue);
        Object expectedClassValue = newClassValue;
        Object actualClassValue = instance.classValue();
        assertEquals(expectedClassValue, actualClassValue);
    }
    
    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        Instance min = new Instance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
            public void put(int index, double value) {
            	// do nothing
            }
        };
        Instance expectedInstance = instance;
        Instance actualInstance = instance.minus(min);
        assertEquals(expectedInstance, actualInstance);
    }
    
    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        double min = 0;
        Instance expectedInstance = instance;
        Instance actualInstance = instance.minus(min);
        assertEquals(expectedInstance, actualInstance);
    }
    
    @Test
    public void testDivide() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        double min = 0;
        Instance expectedInstance = instance;
        Instance actualInstance = instance.divide(min);
        assertEquals(expectedInstance, actualInstance);
    }
    
    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        double value = 0;
        Instance expectedInstance = instance;
        Instance actualInstance = instance.multiply(value);
        assertEquals(expectedInstance, actualInstance);
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        int expectedHashCode = instance.getID();
        int actualHashCode = instance.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        AbstractInstance instance2 = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        boolean expectedEquals = false;
        boolean actualEquals = instance1.equals(instance2);
        assertEquals(expectedEquals, actualEquals);
    }
    
    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        Instance value = new Instance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
            public void put(int index, double value) {
            	// do nothing
            }
        };
        Instance expectedInstance = instance;
        Instance actualInstance = instance.multiply(value);
        assertEquals(expectedInstance, actualInstance);
    }
    
    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        Instance min = new Instance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
            public void put(int index, double value) {
            	// do nothing
            }
        };
        Instance expectedInstance = instance;
        Instance actualInstance = instance.divide(min);
        assertEquals(expectedInstance, actualInstance);
    }
    
    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        double min = 0;
        Instance expectedInstance = instance;
        Instance actualInstance = instance.add(min);
        assertEquals(expectedInstance, actualInstance);
    }
    
    @Test
    public void testAddWithInstance() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        Instance min = new Instance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
            public void put(int index, double value) {
            	// do nothing
            }
        };
        Instance expectedInstance = instance;
        Instance actualInstance = instance.add(min);
        assertEquals(expectedInstance, actualInstance);
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {
        	public int noAttributes() {
        		return 0;
        	}
            
            public double value(int index) {
                return 0;
            }
        };
        Instance expectedInstance = instance;
        Instance actualInstance = instance.sqrt();
        assertEquals(expectedInstance, actualInstance);
    }
    
}