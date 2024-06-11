import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression leftExpression = new Expression() {
            public Object compute(EvalContext context) {
                return "test";
            }
        };

        Expression rightExpression = new Expression() {
            public Object compute(EvalContext context) {
                return "test";
            }
        };

        CoreOperationCompare operation = new CoreOperationCompare(leftExpression, rightExpression);
        EvalContext context = null; // Set your test EvalContext object here
        boolean result = operation.equal(context, leftExpression, rightExpression);

        Assert.assertTrue(result);
    }

    @Test
    public void testContains() {
        Iterator iterator = new Iterator() {
            int index = 0;

            public boolean hasNext() {
                return index < 3;
            }

            public Object next() {
                index++;
                return index;
            }
        };

        Object value = 2;

        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.contains(iterator, value);

        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {
            int index = 0;

            public boolean hasNext() {
                return index < 3;
            }

            public Object next() {
                index++;
                return index;
            }
        };

        Iterator rit = new Iterator() {
            int index = 0;

            public boolean hasNext() {
                return index < 3;
            }

            public Object next() {
                index++;
                return index + 1;
            }
        };

        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.findMatch(lit, rit);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithValuePointer() {
        Object l = new Pointer() {
            public Object getValue() {
                return "test";
            }
        };

        Object r = new Pointer() {
            public Object getValue() {
                return "test";
            }
        };

        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithBooleanValues() {
        Object l = true;
        Object r = false;

        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithNumberValues() {
        Object l = 1;
        Object r = 1.0;

        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithStringValues() {
        Object l = "test";
        Object r = "test";

        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithNullValues() {
        Object l = null;
        Object r = null;

        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(l, r);

        Assert.assertTrue(result);
    }

}