import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        EvalContext context = new EvalContext(new HashMap<>());
        context.setSingleNodePointer(new InitialContext(context));
        Object result = expr.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        int result = expr.getPrecedence();
        assertEquals(7, result);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        boolean result = expr.isSymmetric();
        assertEquals(false, result);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object left = null;
        Object right = null;
        boolean result = expr.compute(left, right);
        assertEquals(false, result);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object o = new InitialContext(new EvalContext(new HashMap<>()));
        Object result = expr.reduce(o);
        assertEquals(o, result);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Iterator it = new HashSet<>().iterator();
        Object value = null;
        boolean result = expr.containsMatch(it, value);
        assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Iterator lit = new HashSet<>().iterator();
        Iterator rit = new HashSet<>().iterator();
        boolean result = expr.findMatch(lit, rit);
        assertEquals(false, result);
    }

}