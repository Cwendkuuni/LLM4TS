import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()});
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertNotNull(result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()});

        int precedence = expression.getPrecedence();

        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()});

        boolean isSymmetric = expression.isSymmetric();

        assertFalse(isSymmetric);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()});

        boolean result = expression.evaluateCompare(0);

        assertTrue(result);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()});
        Object left = new Object();
        Object right = new Object();

        boolean result = expression.compute(left, right);

        assertFalse(result);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()});
        Object o = new Object();

        Object result = expression.reduce(o);

        assertNotNull(result);
    }
    
    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()});
        Iterator it = new Iterator();
        Object value = new Object();

        boolean result = expression.containsMatch(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()});
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();

        boolean result = expression.findMatch(lit, rit);

        assertFalse(result);
    }
}