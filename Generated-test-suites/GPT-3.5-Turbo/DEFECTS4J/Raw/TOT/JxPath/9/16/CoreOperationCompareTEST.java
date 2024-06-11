package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private CoreOperationCompare operationCompare;
    private EvalContext context;
    private Expression left;
    private Expression right;
    
    @Before
    public void setUp() {
        left = new Expression() {
            public Object compute(EvalContext context) {
                return "left";
            }
        };
        right = new Expression() {
            public Object compute(EvalContext context) {
                return "right";
            }
        };
        operationCompare = new CoreOperationCompare(left, right) {
            public Object compute(EvalContext context) {
                return null;
            }
        };
        context = new EvalContext() {
            public Pointer getSingleNodePointer() {
                return null;
            }
            public void reset() {
                
            }
        };
    }
    
    @Test
    public void testGetPrecedence() {
        int expected = 2;
        int actual = operationCompare.getPrecedence();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsSymmetric() {
        boolean expected = true;
        boolean actual = operationCompare.isSymmetric();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_PointersEqual_ReturnsTrue() {
        Pointer pointer1 = new Pointer() {
            public Object getValue() {
                return null;
            }
        };
        Pointer pointer2 = new Pointer() {
            public Object getValue() {
                return null;
            }
        };
        boolean expected = true;
        boolean actual = operationCompare.equal(context, pointer1, pointer2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_PointerIsNull_ReturnsTrue() {
        Pointer pointer1 = null;
        Pointer pointer2 = null;
        boolean expected = true;
        boolean actual = operationCompare.equal(context, pointer1, pointer2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_BooleanValuesEqual_ReturnsTrue() {
        Object boolean1 = true;
        Object boolean2 = true;
        boolean expected = true;
        boolean actual = operationCompare.equal(context, boolean1, boolean2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_DoubleValuesEqual_ReturnsTrue() {
        Object double1 = 1.0;
        Object double2 = 1.0;
        boolean expected = true;
        boolean actual = operationCompare.equal(context, double1, double2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_DoubleValuesDifferent_ReturnsFalse() {
        Object double1 = 1.0;
        Object double2 = 2.0;
        boolean expected = false;
        boolean actual = operationCompare.equal(context, double1, double2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_StringValuesEqual_ReturnsTrue() {
        Object string1 = "string";
        Object string2 = "string";
        boolean expected = true;
        boolean actual = operationCompare.equal(context, string1, string2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_StringValuesDifferent_ReturnsFalse() {
        Object string1 = "string1";
        Object string2 = "string2";
        boolean expected = false;
        boolean actual = operationCompare.equal(context, string1, string2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_ValueIsNull_ReturnsFalse() {
        Object value = null;
        Object pointer = new Pointer() {
            public Object getValue() {
                return value;
            }
        };
        boolean expected = false;
        boolean actual = operationCompare.equal(context, pointer, value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_ValuesEqual_ReturnsTrue() {
        Object value = "value";
        Object pointer = new Pointer() {
            public Object getValue() {
                return value;
            }
        };
        boolean expected = true;
        boolean actual = operationCompare.equal(context, pointer, value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContains_IteratorContainsValue_ReturnsTrue() {
        Object value = "value";
        Iterator iterator = new Iterator() {
            public boolean hasNext() {
                return true;
            }
            public Object next() {
                return value;
            }
        };
        boolean expected = true;
        boolean actual = operationCompare.contains(iterator, value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContains_IteratorDoesNotContainValue_ReturnsFalse() {
        Object value = "value";
        Iterator iterator = new Iterator() {
            public boolean hasNext() {
                return false;
            }
            public Object next() {
                return null;
            }
        };
        boolean expected = false;
        boolean actual = operationCompare.contains(iterator, value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFindMatch_MatchExists_ReturnsTrue() {
        Iterator lit = new Iterator() {
            public boolean hasNext() {
                return true;
            }
            public Object next() {
                return "left";
            }
        };
        Iterator rit = new Iterator() {
            public boolean hasNext() {
                return true;
            }
            public Object next() {
                return "right";
            }
        };
        boolean expected = true;
        boolean actual = operationCompare.findMatch(lit, rit);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFindMatch_MatchDoesNotExist_ReturnsFalse() {
        Iterator lit = new Iterator() {
            public boolean hasNext() {
                return true;
            }
            public Object next() {
                return "left";
            }
        };
        Iterator rit = new Iterator() {
            public boolean hasNext() {
                return true;
            }
            public Object next() {
                return "wrong";
            }
        };
        boolean expected = false;
        boolean actual = operationCompare.findMatch(lit, rit);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_BothValuesAreIterators_ReturnsTrue() {
        Iterator iterator1 = new Iterator() {
            public boolean hasNext() {
                return true;
            }
            public Object next() {
                return "value";
            }
        };
        Iterator iterator2 = new Iterator() {
            public boolean hasNext() {
                return true;
            }
            public Object next() {
                return "value";
            }
        };
        boolean expected = true;
        boolean actual = operationCompare.equal(context, iterator1, iterator2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_OneValueIsIterator_ReturnsTrue() {
        Object value = "value";
        Iterator iterator = new Iterator() {
            public boolean hasNext() {
                return true;
            }
            public Object next() {
                return value;
            }
        };
        boolean expected = true;
        boolean actual = operationCompare.equal(context, iterator, value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEqual_ValuesAreNotEqual_ReturnsFalse() {
        Object value1 = "value1";
        Object value2 = "value2";
        boolean expected = false;
        boolean actual = operationCompare.equal(value1, value2);
        assertEquals(expected, actual);
    }
}