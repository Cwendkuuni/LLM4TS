import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOperationRelationalExpression;

    @Test
    public void testComputeValue_GreaterThan() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{expression1, expression2}) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext evalContext = new EvalContext();
        evalContext.setNodeSet(nodeSet1);
        evalContext.setSingleNodePointer(singleNodePointer1);
        Object result = coreOperationRelationalExpression.computeValue(evalContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_GreaterThanOrEqual() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{expression1, expression2}) {
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        EvalContext evalContext = new EvalContext();
        evalContext.setNodeSet(nodeSet2);
        evalContext.setSingleNodePointer(singleNodePointer2);

        Object result = coreOperationRelationalExpression.computeValue(evalContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LessThan() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{expression1, expression2}) {
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        EvalContext evalContext = new EvalContext();
        evalContext.setNodeSet(nodeSet3);
        evalContext.setSingleNodePointer(singleNodePointer3);

        Object result = coreOperationRelationalExpression.computeValue(evalContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LessThanOrEqual() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{expression1, expression2}) {
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        EvalContext evalContext = new EvalContext();
        evalContext.setNodeSet(nodeSet4);
        evalContext.setSingleNodePointer(singleNodePointer4);

        Object result = coreOperationRelationalExpression.computeValue(evalContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_Equal() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{expression1, expression2}) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext evalContext = new EvalContext();
        evalContext.setNodeSet(nodeSet5);
        evalContext.setSingleNodePointer(singleNodePointer5);

        Object result = coreOperationRelationalExpression.computeValue(evalContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_NotEqual() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{expression1, expression2}) {
            protected boolean evaluateCompare(int compare) {
                return compare != 0;
            }
        };

        EvalContext evalContext = new EvalContext();
        evalContext.setNodeSet(nodeSet6);
        evalContext.setSingleNodePointer(singleNodePointer6);

        Object result = coreOperationRelationalExpression.computeValue(evalContext);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_NaN() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{expression1, expression2}) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext evalContext = new EvalContext();
        evalContext.setNodeSet(nodeSet6);
        evalContext.setSingleNodePointer(singleNodePointerSix);

        Object result = coreOperationRelationalExpression.computeValue(evalContext);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    // TEST DATA
    private Expression expression1 = new Expression() {
        @Override
        public Object computeValue(EvalContext evalContext) {
            return 10;
        }
    };

    private Expression expression2 = new Expression() {
        @Override
        public Object computeValue(EvalContext evalContext) {
            return 5;
        }
    };

    private Collection nodeSet1 = new HashSet();

    private Collection nodeSet2 = new HashSet();

    private Collection nodeSet3 = new HashSet();

    private Collection nodeSet4 = new HashSet();

    private Collection nodeSet5 = new HashSet();

    private Collection nodeSet6 = new HashSet();

    private SelfContext singleNodePointer1 = new SelfContext(null);

    private SelfContext singleNodePointer2 = new SelfContext(null);

    private SelfContext singleNodePointer3 = new SelfContext(null);

    private SelfContext singleNodePointer4 = new SelfContext(null);

    private SelfContext singleNodePointer5 = new SelfContext(null);

    private SelfContext singleNodePointer6 = new SelfContext(null);

}