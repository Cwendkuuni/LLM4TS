import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.CompilerUtil;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    private CoreOperationRelationalExpression expression;
    private EvalContext context;

    @Before
    public void setUp() {
        Expression[] args = new Expression[2];
        args[0] = CompilerUtil.compileExpression(context, new QName("left"));
        args[1] = CompilerUtil.compileExpression(context, new QName("right"));
        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue() {
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testCompute() {
        Object result = expression.compute("left", "right");
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testReduce() {
        Object result = expression.reduce("input");
        assertEquals("input", result);
    }

    @Test
    public void testContainsMatch() {
        NodePointer nodePointer = new NodePointer();
        boolean containsMatch = expression.containsMatch(nodePointer, "value");
        assertFalse(containsMatch);
    }

    @Test
    public void testFindMatch() {
        NodePointer left = new NodePointer();
        NodePointer right = new NodePointer();
        boolean containsMatch = expression.containsMatch(left, right);
        assertFalse(containsMatch);
    }
}