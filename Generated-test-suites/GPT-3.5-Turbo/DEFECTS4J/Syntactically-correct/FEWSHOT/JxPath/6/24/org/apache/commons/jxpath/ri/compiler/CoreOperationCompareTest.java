import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

        // Test case 1
        EvalContext context1 = new EvalContext();
        context1.setNodeSet(new HashSet<>());
        assertFalse(operation.equal(context1, arg1, arg2));

        // Test case 2
        EvalContext context2 = new EvalContext();
        context2.setNodeSet(null);
        assertFalse(operation.equal(context2, arg1, arg2));

        // Test case 3
        EvalContext context3 = new EvalContext();
        context3.setNodeSet(new HashSet<>());
        assertTrue(operation.equal(context3, arg1, arg1));
    }

    @Test
    public void testContains() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

        // Test case 1
        Iterator<Object> it1 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object value1 = new Object();
        assertFalse(operation.contains(it1, value1));

        // Test case 2
        Iterator<Object> it2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object value2 = new Object();
        assertFalse(operation.contains(it2, value2));

        // Test case 3
        Iterator<Object> it3 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object value3 = null;
        assertFalse(operation.contains(it3, value3));
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

        // Test case 1
        Iterator<Object> lit1 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Iterator<Object> rit1 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        assertFalse(operation.findMatch(lit1, rit1));

        // Test case 2
        Iterator<Object> lit2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Iterator<Object> rit2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        assertFalse(operation.findMatch(lit2, rit2));

        // Test case 3
        Iterator<Object> lit3 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Object();
            }
        };
        Iterator<Object> rit3 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        assertFalse(operation.findMatch(lit3, rit3));
    }

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare();
        // Test case 1
        Object l1 = null;
        Object r1 = null;
        assertTrue(operation.equal(l1, r1));

        // Test case 2
        Object l2 = new Object();
        Object r2 = new Object();
        assertFalse(operation.equal(l2, r2));

        // Test case 3
        Object l3 = new Pointer();
        Object r3 = new Pointer();
        assertTrue(operation.equal(l3, r3));
    }
}