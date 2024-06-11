import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Iterator;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {
    
    private CoreOperationCompare coreOperationCompare;
    private EvalContext evalContext;
    private Expression left;
    private Expression right;
    
    @Before
    public void setUp() {
        left = new Expression();
        right = new Expression();
        coreOperationCompare = new CoreOperationCompare(left, right);
        evalContext = new EvalContext();
    }
    
    @Test
    public void testEqual() {
        // Test when both expressions return null
        Object l = null;
        Object r = null;
        assertFalse(coreOperationCompare.equal(evalContext, left, right));
        
        // Test when one expression returns null and the other returns non-null object
        l = null;
        r = new Object();
        assertFalse(coreOperationCompare.equal(evalContext, left, right));
        
        left = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return "test";
            }
        };
        
        right = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return "test";
            }
        };
        
        // Test when expressions return equal non-null values
        assert(coreOperationCompare.equal(evalContext, left, right));
        
        right = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return "different";
            }
        };
        
        // Test when expressions return different non-null values
        assertFalse(coreOperationCompare.equal(evalContext, left, right));
    }
    
    @Test
    public void testContains() {
        Iterator it = new Iterator() {
            private int count = 0;
            
            @Override
            public boolean hasNext() {
                return count < 3;
            }
            
            @Override
            public Object next() {
                count++;
                if (count == 1) {
                return "test";
                } else if (count == 2) {
                    return "123";
                } else {
                    return 456;
                }
            }
        };
        
        // Test when iterator contains the value
        assertTrue(coreOperationCompare.contains(it, "test"));
        
        // Test when iterator does not contain the value
        assertFalse(coreOperationCompare.contains(it, "missing"));
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {
            private int count = 0;
            
            @Override
            public boolean hasNext() {
                return count < 3;
            }
            
            @Override
            public Object next() {
                count++;
                if (count == 1) {
                return "a";
                } else if (count == 2) {
                    return "b";
                } else {
                    return "c";
                }
            }
        };
        
        Iterator rit = new Iterator() {
            private int count = 0;
            
            @Override
            public boolean hasNext() {
                return count < 3;
            }
            
            @Override
            public Object next() {
                count++;
                if (count == 1) {
                return "test";
                } else if (count == 2) {
                    return "123";
                } else {
                    return 456;
                }
            }
        };
        
        // Test when there is a match between the iterators
        assertTrue(coreOperationCompare.findMatch(lit, rit));
        
        Iterator lit2 = new Iterator() {
            private int count = 0;
            
            @Override
            public boolean hasNext() {
                return count < 3;
            }
            
            @Override
            public Object next() {
                count++;
                if (count == 1) {
                return "a";
                } else if (count == 2) {
                    return "b";
                } else {
                    return "c";
                }
            }
        };
        
        Iterator rit2 = new Iterator() {
            private int count = 0;
            
            @Override
            public boolean hasNext() {
                return count < 3;
            }
            
            @Override
            public Object next() {
                count++;
                if (count == 1) {
                return "d";
                } else if (count == 2) {
                    return "e";
                } else {
                    return "f";
                }
            }
        };
        
        // Test when there is no match between the iterators
        assertFalse(coreOperationCompare.findMatch(lit2, rit2));
    }
    
    @Test
    public void testEqualWithPointers() {
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();
        
        // Test when pointers are equal
        assertTrue(coreOperationCompare.equal(pointer1, pointer1));
        
        // Test when pointers are different
        assertFalse(coreOperationCompare.equal(pointer1, pointer2));
    }
    
    @Test
    public void testEqualWithBooleans() {
        // Test when booleans are equal
        assertTrue(coreOperationCompare.equal(true, true));
        assertFalse(coreOperationCompare.equal(true, false));
        assertTrue(coreOperationCompare.equal(false, false));
        
        // Test when object is a boolean wrapper
        assertTrue(coreOperationCompare.equal(Boolean.TRUE, Boolean.TRUE));
        assertFalse(coreOperationCompare.equal(Boolean.TRUE, Boolean.FALSE));
        assertTrue(coreOperationCompare.equal(Boolean.FALSE, Boolean.FALSE));
        
        // Test when object is not a boolean or boolean wrapper
        assertFalse(coreOperationCompare.equal(true, "true"));
        assertFalse(coreOperationCompare.equal(true, "false"));
        assertFalse(coreOperationCompare.equal(true, 1));
        assertFalse(coreOperationCompare.equal(true, 0));
    }
    
    @Test
    public void testEqualWithNumbers() {
        // Test when numbers are equal
        assertTrue(coreOperationCompare.equal(1, 1));
        assertFalse(coreOperationCompare.equal(1, 2));
        assertTrue(coreOperationCompare.equal(1.0, 1.0));
        assertFalse(coreOperationCompare.equal(1.0, 2.0));
        
        // Test when object is a number wrapper
        assertTrue(coreOperationCompare.equal(Integer.valueOf(1), Integer.valueOf(1)));
        assertFalse(coreOperationCompare.equal(Integer.valueOf(1), Integer.valueOf(2)));
        assertTrue(coreOperationCompare.equal(Double.valueOf(1.0), Double.valueOf(1.0)));
        assertFalse(coreOperationCompare.equal(Double.valueOf(1.0), Double.valueOf(2.0)));
        
        // Test when object is not a number or number wrapper
        assertFalse(coreOperationCompare.equal(1, true));
        assertFalse(coreOperationCompare.equal(1, "1"));
    }
    
    @Test
    public void testEqualWithStrings() {
        // Test when strings are equal
        assertTrue(coreOperationCompare.equal("test", "test"));
        assertFalse(coreOperationCompare.equal("test", "different"));
        
        // Test when object is not a string
        assertFalse(coreOperationCompare.equal("test", 123));
    }
    
    @Test
    public void testEqualWithObjects() {
        // Test when objects are equal
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(coreOperationCompare.equal(obj1, obj2));
        
        // Test when objects are not equal
        Object obj3 = new Object();
        assertFalse(coreOperationCompare.equal(obj1, obj3));
    }
    
}