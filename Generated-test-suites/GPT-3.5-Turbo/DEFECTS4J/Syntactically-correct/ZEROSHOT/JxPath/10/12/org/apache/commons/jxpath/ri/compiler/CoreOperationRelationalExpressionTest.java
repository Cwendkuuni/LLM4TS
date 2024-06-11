import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class CoreOperationRelationalExpressionTest {

    private static final double DELTA = 0.00001;

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpressionMock(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        EvalContext context = new EvalContextMock();

        // Test when compute returns true
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test when compute returns false
        operation = new CoreOperationRelationalExpressionMock(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpressionMock(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        int precedence = operation.getPrecedence();
        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpressionMock(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        boolean isSymmetric = operation.isSymmetric();
        Assert.assertFalse(isSymmetric);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpressionMock(new Expression[]{new ExpressionMock(), new ExpressionMock()});

        // Test when compare is equal
        boolean result = operation.evaluateCompare(0);
        Assert.assertTrue(result);

        // Test when compare is lower
        result = operation.evaluateCompare(-1);
        Assert.assertFalse(result);

        // Test when compare is higher
        result = operation.evaluateCompare(1);
        Assert.assertFalse(result);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpressionMock(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        Object left = "left";
        Object right = "right";

        // Test when left is InitialContext
        left = new InitialContext();
        right = new InitialContext();
        boolean result = operation.compute(left, right);
        Assert.assertFalse(result);

        // Test when left and right are Iterator
        left = new IteratorMock();
        right = new IteratorMock();
        result = operation.compute(left, right);
        Assert.assertFalse(result);

        // Test when left is Iterator
        left = new IteratorMock();
        right = "right";
        result = operation.compute(left, right);
        Assert.assertFalse(result);

        // Test when right is Iterator
        left = "left";
        right = new IteratorMock();
        result = operation.compute(left, right);
        Assert.assertFalse(result);

        // Test when left and right are double
        left = 2.0;
        right = 2.0;
        result = operation.compute(left, right);
        Assert.assertTrue(result);

        // Test when left and right are NaN
        left = Double.NaN;
        right = Double.NaN;
        result = operation.compute(left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpressionMock(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        Object o = new SelfContext();
        Object result = operation.reduce(o);
        Assert.assertEquals(((SelfContext) o).getSingleNodePointer(), result);

        o = new ArrayList<>();
        result = operation.reduce(o);
        Assert.assertEquals(((ArrayList) o).iterator(), result);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpressionMock(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        Iterator it = new IteratorMock();
        Object value = new Object();

        // Test when while loop is done and no matches
        boolean result = operation.containsMatch(it, value);
        Assert.assertFalse(result);

        // Test when match is found
        it = new IteratorMock(Collections.singletonList(value).iterator());
        result = operation.containsMatch(it, value);
        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpressionMock(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        Iterator lit = new IteratorMock();
        Iterator rit = new IteratorMock();

        // Test when left contains match
        boolean result = operation.findMatch(lit, rit);
        Assert.assertTrue(result);

        // Test when left does not contain match
        lit = new IteratorMock(new LinkedList<>().iterator());
        rit = new IteratorMock();
        result = operation.findMatch(lit, rit);
        Assert.assertFalse(result);
    }
}

class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
    public CoreOperationRelationalExpressionMock(Expression[] args) { super(args); }
    protected boolean evaluateCompare(int compare) { return compare == 0; }
}

class ExpressionMock extends Expression {
    public Object computeValue(EvalContext context) { return null; }
    public int getPrecedence() { return 0; }
    public boolean isSymmetric() { return false; }
}

class EvalContextMock extends EvalContext {
    public Iterator getSingleNodeIterator() { return null; }
    public Object getSingleNodePointer() { return null; }
    public Object getBean() { return null; }
    public void setNamespaceContext(NamespaceContext var1) {}
    public NamespaceContext getNamespaceContext() { return null; }
    public void setExtensionContext(ExtensionContext var1) {}
    public ExtensionContext getExtensionContext() { return null; }
}

class IteratorMock implements Iterator {
    private Iterator iterator;

    public IteratorMock() { this.iterator = new LinkedList<>().iterator(); }
    public IteratorMock(Iterator iterator) { this.iterator = iterator; }

    public boolean hasNext() { return iterator.hasNext(); }
    public Object next() { return iterator.next(); }
}