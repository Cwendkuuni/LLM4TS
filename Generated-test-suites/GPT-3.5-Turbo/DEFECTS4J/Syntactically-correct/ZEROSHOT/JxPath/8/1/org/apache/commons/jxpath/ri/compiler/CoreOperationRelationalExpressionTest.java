import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue_True() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer("test");

        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_False() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer("");

        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
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
    public void testReduce() {
        Object o = new SelfContext(new EvalContext());
        Object result = expression.reduce(o);
        assertEquals(o, result);
    }

    @Test
    public void testContainsMatch_True() {
        Iterator iterator = new HashSet<String>().iterator();
        Object value = "test";

        boolean match = expression.containsMatch(iterator, value);
        assertTrue(match);
    }

    @Test
    public void testContainsMatch_False() {
        Iterator iterator = new HashSet<String>().iterator();
        Object value = "test";

        boolean match = expression.containsMatch(iterator, value);
        assertFalse(match);
    }

    @Test
    public void testFindMatch_True() {
        Iterator left = new HashSet<String>().iterator();
        Iterator right = new HashSet<String>().iterator();

        boolean match = expression.findMatch(left, right);
        assertTrue(match);
    }

    @Test
    public void testFindMatch_False() {
        Iterator left = new HashSet<String>().iterator();
        Iterator right = new HashSet<String>().iterator();

        boolean match = expression.findMatch(left, right);
        assertFalse(match);
    }
}