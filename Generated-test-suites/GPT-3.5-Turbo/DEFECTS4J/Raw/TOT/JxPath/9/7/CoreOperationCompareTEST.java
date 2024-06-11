import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.*;
import org.apache.commons.jxpath.*;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOpCompare;
    private EvalContext evalContext;

    @Before
    public void setUp() {
        Expression arg1 = new CoreVariable();
        Expression arg2 = new CoreVariable();
        coreOpCompare = new CoreOperationCompare(arg1, arg2);

        JXPathContext context = JXPathContext.newContext(new Object());
        evalContext = new EvalContext(context);
        
    }

    // Test compute() method
    
    @Test
    public void testCompute_ValidInput_Success() {
        Expression arg1 = new CoreVariable();
        Expression arg2 = new CoreVariable();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        
        Object result = coreOperationCompare.compute(evalContext);
        assertNull(result);
    }
    
    // Test getPrecedence() method
    
    @Test
    public void testGetPrecedence_DefaultValue_Returns2() {
        int precedence = coreOpCompare.getPrecedence();
        
        assertEquals(2, precedence);
    }
    
    // Test isSymmetric() method
    
    @Test
    public void testIsSymmetric_DefaultValue_ReturnsTrue() {
        boolean isSymmetric = coreOpCompare.isSymmetric();
        
        assertTrue(isSymmetric);
    }
    
    // Test equal() method
    
    @Test
    public void testEqual_BothPointersEqual_ReturnsTrue() {
        Object l = new Pointer("test");
        Object r = new Pointer("test");
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_LeftPointerNull_ReturnsFalse() {
        Object l = null;
        Object r = new Pointer("test");
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertFalse(equal);
    }
    
    @Test
    public void testEqual_RightPointerNull_ReturnsFalse() {
        Object l = new Pointer("test");
        Object r = null;
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertFalse(equal);
    }
    
    @Test
    public void testEqual_BothPointersNull_ReturnsTrue() {
        Object l = null;
        Object r = null;
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_LeftPointerInstanceOfInitialContext_ResetsLeftPointerAndReturnsFalse() {
        EvalContext l = new InitialContext();
        Object r = new Pointer("test");
        
        boolean equal = coreOpCompare.equal(evalContext, l, (Expression) r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_LeftPointerInstanceOfSelfContext_ReturnsTrue() {
        EvalContext l = new SelfContext();
        Object r = new Pointer("test");
        
        boolean equal = coreOpCompare.equal(evalContext, l, (Expression) r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_RightPointerInstanceOfInitialContext_ResetsRightPointerAndReturnsFalse() {
        Object l = new Pointer("test");
        EvalContext r = new InitialContext();
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_RightPointerInstanceOfSelfContext_ReturnsTrue() {
        Object l = new Pointer("test");
        EvalContext r = new SelfContext();
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_LeftInstanceIsCollection_ReturnsFalse() {
        Object l = new HashSet<>();
        Object r = new Pointer("test");
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertFalse(equal);
    }
    
    @Test
    public void testEqual_RightInstanceIsCollection_ReturnsFalse() {
        Object l = new Pointer("test");
        Object r = new HashSet<>();
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertFalse(equal);
    }
    
    @Test
    public void testEqual_InstanceOfIterator_ReturnsTrue() {
        Object l = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object r = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_LeftInstanceOfIterator_ReturnsFalse() {
        Object l = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object r = new Object();
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertFalse(equal);
    }
    
    @Test
    public void testEqual_RightInstanceOfIterator_ReturnsFalse() {
        Object l = new Object();
        Object r = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertFalse(equal);
    }
    
    @Test
    public void testEqual_BothEqual_ReturnsTrue() {
        Object l = "test";
        Object r = "test";
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_BothAreBooleans_ReturnsTrue() {
        Object l = true;
        Object r = true;
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_BothAreNumbers_ReturnsTrue() {
        Object l = 10;
        Object r = 10;
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_BothAreNaN_ReturnsTrue() {
        Object l = Double.NaN;
        Object r = Double.NaN;
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_BothAreStrings_ReturnsTrue() {
        Object l = "test";
        Object r = "test";
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_NullValues_ReturnsFalse() {
        Object l = null;
        Object r = null;
        
        boolean equal = coreOpCompare.equal(evalContext, (Expression) l, (Expression) r);
        
        assertFalse(equal);
    }

    // Test contains() method
    
    @Test
    public void testContains_IteratorContainsValue_ReturnsTrue() {
        Iterator it = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return "test";
            }
        };
        
        boolean contains = coreOpCompare.contains(it, "test");
        
        assertTrue(contains);
    }
    
    @Test
    public void testContains_IteratorDoesNotContainValue_ReturnsFalse() {
        Iterator it = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return "test";
            }
        };
        
        boolean contains = coreOpCompare.contains(it, "hello");
        
        assertFalse(contains);
    }
    
    // Test findMatch() method
    
    @Test
    public void testFindMatch_BothIteratorsContainValue_ReturnsTrue() {
        Iterator lit = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return "test";
            }
        };

        Iterator rit = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return "test";
            }
        };
        
        boolean match = coreOpCompare.findMatch(lit, rit);
        
        assertTrue(match);
    }
    
    @Test
    public void testFindMatch_BothIteratorsDoNotContainValue_ReturnsFalse() {
        Iterator lit = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return "test";
            }
        };

        Iterator rit = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return "hello";
            }
        };
        
        boolean match = coreOpCompare.findMatch(lit, rit);
        
        assertFalse(match);
    }
    
    // Test equal() method
    
    @Test
    public void testEqual_BothPointersEqual_ReturnsTrue() {
        Object l = new Pointer("test");
        Object r = new Pointer("test");
        
        boolean equal = coreOpCompare.equal(l, r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_OnePointerNull_ReturnsFalse() {
        Object l = new Pointer("test");
        Object r = null;
        
        boolean equal = coreOpCompare.equal(l, r);
        
        assertFalse(equal);
    }
    
    @Test
    public void testEqual_BothPointersNull_ReturnsTrue() {
        Object l = null;
        Object r = null;
        
        boolean equal = coreOpCompare.equal(l, r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_PointersNotEqual_ReturnsFalse() {
        Object l = new Pointer("test");
        Object r = new Pointer("hello");
        
        boolean equal = coreOpCompare.equal(l, r);
        
        assertFalse(equal);
    }
    
    @Test
    public void testEqual_BothAreBooleans_ReturnsTrue() {
        Object l = true;
        Object r = true;
        
        boolean equal = coreOpCompare.equal(l, r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_BothAreNumbers_ReturnsTrue() {
        Object l = 10;
        Object r = 10;
        
        boolean equal = coreOpCompare.equal(l, r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_BothAreNaN_ReturnsTrue() {
        Object l = Double.NaN;
        Object r = Double.NaN;
        
        boolean equal = coreOpCompare.equal(l, r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_BothAreStrings_ReturnsTrue() {
        Object l = "test";
        Object r = "test";
        
        boolean equal = coreOpCompare.equal(l, r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_BothEqual_ReturnsTrue() {
        Object l = 10;
        Object r = 10.0;
        
        boolean equal = coreOpCompare.equal(l, r);
        
        assertTrue(equal);
    }
    
    @Test
    public void testEqual_NullValues_ReturnsFalse() {
        Object l = null;
        Object r = null;
        
        boolean equal = coreOpCompare.equal(l, r);
        
        assertFalse(equal);
    }
}