import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOperationRelationalExpression;
    private Object left;
    private Object right;

    @Before
    public void setUp() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpressionMock(new Expression[] {});
        left = new Object();
        right = new Object();
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = coreOperationRelationalExpression.computeValue(context);
        assertNotNull(result);
    }

    @Test
    public void testGetPrecedence() {
        int result = coreOperationRelationalExpression.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        boolean result = coreOperationRelationalExpression.isSymmetric();
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare() {
        boolean result = coreOperationRelationalExpression.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testCompute() {
        boolean result = coreOperationRelationalExpression.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testReduceSelfContext() {
        Object result = coreOperationRelationalExpression.reduce(new SelfContext());
        assertNotNull(result);
    }

    @Test
    public void testReduceCollection() {
        Object o = new HashSet<>();
        Object result = coreOperationRelationalExpression.reduce(o);
        assertNotNull(result);
    }

    @Test
    public void testContainsMatch() {
        Iterator it = new Iterator() {
            public boolean hasNext() { return false; }
            public Object next() { return null; }
        };
        boolean result = coreOperationRelationalExpression.containsMatch(it, left);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {
            public boolean hasNext() { return false; }
            public Object next() { return null; }
        };
        Iterator rit = new Iterator() {
            public boolean hasNext() { return false; }
            public Object next() { return null; }
        };
        boolean result = coreOperationRelationalExpression.findMatch(lit, rit);
        assertFalse(result);
    }

    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
        public CoreOperationRelationalExpressionMock(Expression[] args) {
            super(args);
        }

        protected boolean evaluateCompare(int compare) {
            return compare == 0;
        }
    }
}