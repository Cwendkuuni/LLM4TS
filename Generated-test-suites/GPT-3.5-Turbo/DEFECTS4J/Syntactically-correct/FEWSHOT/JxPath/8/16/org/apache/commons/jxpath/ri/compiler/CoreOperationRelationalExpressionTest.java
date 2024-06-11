import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        final Expression[] args = new Expression[2];
        final CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        final EvalContext context = new EvalContext(null);

        args[0] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 1;
            }
        };
        args[1] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };
        assertTrue(expression.computeValue(context));

        args[0] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };
        args[1] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 1;
            }
        };
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        final CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        final CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        final CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse(expression.compute(1, 2));
        assertTrue(expression.compute(2, 1));
        assertFalse(expression.compute(3, 3));
    }

    @Test
    public void testReduce() {
        final CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object o = new SelfContext();
        assertEquals(((EvalContext) o).getSingleNodePointer(), expression.reduce(o));

        o = new HashSet<>();
        assertEquals(((Collection) o).iterator(), expression.reduce(o));
    }

    @Test
    public void testContainsMatch() {
        final CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        final Iterator it = new Iterator() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public Object next() {
                return index++;
            }
        };

        assertTrue(expression.containsMatch(it, 2));
        assertFalse(expression.containsMatch(it, 5));
    }

    @Test
    public void testFindMatch() {
        final CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        final Iterator lit = new Iterator() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public Object next() {
                return index++;
            }
        };

        final Iterator rit = new Iterator() {
            private int index = 2;

            @Override
            public boolean hasNext() {
                return index < 6;
            }

            @Override
            public Object next() {
                return index++;
            }
        };

        assertTrue(expression.findMatch(lit, rit));
        assertFalse(expression.findMatch(null, rit));
    }
}