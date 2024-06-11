
import org.apache.commons.jxpath.ri.compiler.*;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }

            public int getPrecedence() {
                return 0;
            }

            public Expression[] getArguments() {
                return new Expression[0];
            }
        };

        Expression arg2 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }

            public int getPrecedence() {
                return 0;
            }

            public Expression[] getArguments() {
                return new Expression[0];
            }
        };

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();

        // Test case 1: both arg1 and arg2 are null
        boolean result = coreOperationCompare.equal(context, arg1, arg2);
        assertTrue(result);

        // Test case 2: arg1 is null and arg2 is not null
        result = coreOperationCompare.equal(context, arg1, new Expression() {
            public Object compute(EvalContext evalContext) {
                return new Object();
            }

            public int getPrecedence() {
                return 0;
            }

            public Expression[] getArguments() {
                return new Expression[0];
            }
        });
        assertFalse(result);

        // Test case 3: arg1 is not null and arg2 is null
        result = coreOperationCompare.equal(context, new Expression() {
            public Object compute(EvalContext evalContext) {
                return new Object();
            }

            public int getPrecedence() {
                return 0;
            }

            public Expression[] getArguments() {
                return new Expression[0];
            }
        }, arg2);
        assertFalse(result);

        // Test case 4: arg1 and arg2 are equal
        Object obj = new Object();
        result = coreOperationCompare.equal(context, new Expression() {
            public Object compute(EvalContext evalContext) {
                return obj;
            }

            public int getPrecedence() {
                return 0;
            }

            public Expression[] getArguments() {
                return new Expression[0];
            }
        }, new Expression() {
            public Object compute(EvalContext evalContext) {
                return obj;
            }

            public int getPrecedence() {
                return 0;
            }

            public Expression[] getArguments() {
                return new Expression[0];
            }
        });
        assertTrue(result);

        // Test case 5: arg1 and arg2 are not equal
        result = coreOperationCompare.equal(context, new Expression() {
            public Object compute(EvalContext evalContext) {
                return new Object();
            }

            public int getPrecedence() {
                return 0;
            }

            public Expression[] getArguments() {
                return new Expression[0];
            }
        }, new Expression() {
            public Object compute(EvalContext evalContext) {
                return new Object();
            }

            public int getPrecedence() {
                return 0;
            }

            public Expression[] getArguments() {
                return new Expression[0];
            }
        });
        assertFalse(result);
    }

    @Test
    public void testContains() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }

            public int getPrecedence() {
                return 0;
            }

            public Expression[] getArguments() {
                return new Expression[0];
            }
        };

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg1);

        Iterator it = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };

        Object value = new Object();

        boolean result = coreOperationCompare.contains(it, value);

        // Test case 1: it has no next element, so it should return false
        assertFalse(result);

        it = new Iterator() {
            int count = 1;

            public boolean hasNext() {
                return count > 0;
            }

            public Object next() {
                count--;
                return value;
            }
        };

        result = coreOperationCompare.contains(it, value);

        // Test case 2: it has next element which is equal to value, so it should return true
        assertTrue(result);

        it = new Iterator() {
            int count = 1;

            public boolean hasNext() {
                return count > 0;
            }

            public Object next() {
                return new Object();
            }
        };

        result = coreOperationCompare.contains(it, value);

        // Test case 3: it has next element which is not equal to value, so it should return false
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }

            public int getPrecedence() {
                return 0;
            }

            public Expression[] getArguments() {
                return new Expression[0];
            }
        };

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg1);

        Iterator lit = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };

        Iterator rit = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };

        boolean result = coreOperationCompare.findMatch(lit, rit);

        // Test case 1: both lit and rit have no next element, so it should return false
        assertFalse(result);

        lit = new Iterator() {
            int count = 1;

            public boolean hasNext() {
                return count > 0;
            }

            public Object next() {
                count--;
                return new Object();
            }
        };

        rit = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };

        result = coreOperationCompare.findMatch(lit, rit);

        // Test case 2: lit has next element but rit has no next element, so it should return false
        assertFalse(result);

        lit = new Iterator() {
            int count = 1;

            public boolean hasNext() {
                return count > 0;
            }

            public Object next() {
                count--;
                return new Object();
            }
        };

        rit = new Iterator() {
            int count = 1;

            public boolean hasNext() {
                return count > 0;
            }

            public Object next() {
                count--;
                return new Object();
            }
        };

        result = coreOperationCompare.findMatch(lit, rit);

        // Test case 3: lit and rit have elements which are not equal, so it should return false
        assertFalse(result);

        lit = new Iterator() {
            int count = 1;

            public boolean hasNext() {
                return count > 0;
            }

            public Object next() {
                count--;
                return new Object();
            }
        };

        rit = new Iterator() {
            int count = 1;

            public boolean hasNext() {
                return count > 0;
            }

            public Object next() {
                count--;
                return new Object();
            }
        };

        result = coreOperationCompare.findMatch(lit, rit);

        // Test case 4: lit and rit have elements which are equal, so it should return true
        assertTrue(result);
    }

    @Test
    public void testEqual2() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();

        // Test case 1: l and r are null
        boolean result = coreOperationCompare.equal(null, null);
        assertTrue(result);

        // Test case 2: l is null and r is not null
        result = coreOperationCompare.equal(null, new Object());
        assertFalse(result);

        // Test case 3: l is not null and r is null
        result = coreOperationCompare.equal(new Object(), null);
        assertFalse(result);

        // Test case 4: l and r are equal
        Object obj = new Object();
        result = coreOperationCompare.equal(obj, obj);
        assertTrue(result);

        // Test case 5: l and r are not equal
        result = coreOperationCompare.equal(new Object(), new Object());
        assertFalse(result);
    }

    @Test
    public void testEqual3() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();

        // Test case 1: l and r are Pointers which are equal
        Pointer l = new Pointer() {
            public boolean isContainer() {
                return false;
            }

            public Object getBaseValue() {
                return null;
            }

            public void setValue(Object value) {

            }

            public Object getValue() {
                return null;
            }

            public boolean isDynamicProperty() {
                return false;
            }

            public String getNamespaceURI() {
                return null;
            }

            public String getName() {
                return null;
            }
        };

        Pointer r = new Pointer() {
            public boolean isContainer() {
                return false;
            }

            public Object getBaseValue() {
                return null;
            }

            public void setValue(Object value) {

            }

            public Object getValue() {
                return null;
            }

            public boolean isDynamicProperty() {
                return false;
            }

            public String getNamespaceURI() {
                return null;
            }

            public String getName() {
                return null;
            }
        };

        boolean result = coreOperationCompare.equal(l, r);
        assertTrue(result);

        // Test case 2: l is Pointer and r is not Pointer
        result = coreOperationCompare.equal(l, new Object());
        assertFalse(result);

        // Test case 3: l is not Pointer and r is Pointer
        result = coreOperationCompare.equal(new Object(), r);
        assertFalse(result);

        // Test case 4: l and r are not equal Pointers
        result = coreOperationCompare.equal(new Object(), new Object());
        assertFalse(result);
    }
}
