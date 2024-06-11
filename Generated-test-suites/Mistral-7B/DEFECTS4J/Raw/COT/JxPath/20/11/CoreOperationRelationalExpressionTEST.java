import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Tests for the CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstruct() {
        Expression[] args = new Expression[2];
        CoreOperation op = new CoreOperationRelationalExpression(args);
        Assert.assertNotNull(op);
    }

    @Test
    public void testComputeValue() {
        Expression left = new CoreBooleanExpression(true);
        Expression right = new CoreBooleanExpression(true);
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[]{left, right});
        EvalContext context = new EvalContext(new InitialContext(new Object()));
        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        left = new CoreBooleanExpression(true);
        right = new CoreBooleanExpression(false);
        op = new CoreOperationGreaterThan(new Expression[]{left, right});
        result = op.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        left = new CoreBooleanExpression(false);
        right = new CoreBooleanExpression(true);
        op = new CoreOperationGreaterThan(new Expression[]{left, right});
        result = op.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        left = new CoreBooleanExpression(false);
        right = new CoreBooleanExpression(false);
        op = new CoreOperationGreaterThan(new Expression[]{left, right});
        result = op.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[0]);
        Assert.assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, op.getPrecedence());
    }

    @Test(expected = AssertionError.class)
    public void testIsSymmetricWhenFalse() {
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[0]);
        op.isSymmetric();
    }

    @Test
    public void testEvaluateCompareTrue() {
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[0]);
        Assert.assertTrue(op.evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompareFalse() {
        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[0]);
        Assert.assertFalse(op.evaluateCompare(-1));
    }

    @Test
    public void testComputeWithIterators() {
        Collection left = new ArrayList<>();
        left.add(new CoreNumberExpression(1.0));
        left.add(new CoreNumberExpression(2.0));

        Collection right = new ArrayList<>();
        right.add(new CoreNumberExpression(2.0));
        right.add(new CoreNumberExpression(3.0));

        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[]{new CoreIteratorExpression(left.iterator()), new CoreIteratorExpression(right.iterator())});
        EvalContext context = new EvalContext(new InitialContext(new Object()));
        Object result = op.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        left.clear();
        left.add(new CoreNumberExpression(1.0));
        left.add(new CoreNumberExpression(2.0));

        right.clear();
        right.add(new CoreNumberExpression(2.0));
        right.add(new CoreNumberExpression(1.0));

        op = new CoreOperationGreaterThan(new Expression[]{new CoreIteratorExpression(left.iterator()), new CoreIteratorExpression(right.iterator())});
        result = op.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        left.clear();
        left.add(new CoreNumberExpression(3.0));
        left.add(new CoreNumberExpression(2.0));

        right.clear();
        right.add(new CoreNumberExpression(2.0));
        right.add(new CoreNumberExpression(3.0));

        op = new CoreOperationGreaterThan(new Expression[]{new CoreIteratorExpression(left.iterator()), new CoreIteratorExpression(right.iterator())});
        result = op.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testFindMatch() {
        Iterator left = new ArrayList<Object>()
                {{add(new CoreNumberExpression(1.0)); add(new CoreNumberExpression(2.0));}}.iterator();

        Iterator right = new ArrayList<Object>()
                {{add(new CoreNumberExpression(2.0)); add(new CoreNumberExpression(3.0));}}.iterator();

        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[]{new CoreIteratorExpression(left), new CoreIteratorExpression(right)});
        Assert.assertFalse(op.findMatch(left, right.next()));
        Assert.assertFalse(op.findMatch(left, right));
        Assert.assertTrue(op.findMatch(left, left.next()));
        Assert.assertTrue(op.findMatch(left, left));
    }

    @Test
    public void testContainsMatch() {
        Iterator left = new ArrayList<Object>()
                {{add(new CoreNumberExpression(1.0)); add(new CoreNumberExpression(2.0));}}.iterator();

        Object right = new CoreNumberExpression(2.0);

        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(new Expression[]{new CoreIteratorExpression(left), new CoreExpression(right)});
        Assert.assertFalse(op.containsMatch(left, right));
        Assert.assertTrue(op.containsMatch(left, left.next()));
    }

}