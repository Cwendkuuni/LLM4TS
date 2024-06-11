import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}){
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        // Test when compare returns positive value
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Number(5));
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test when compare returns negative value
        context.setSingleNodePointer(new Number(3));
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test when compare returns 0
        context.setSingleNodePointer(new Number(5));
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test when left and right are iterators with no common values
        Iterator it1 = new HashSet<Number>().iterator();
        Iterator it2 = new HashSet<Number>().iterator();
        result = expression.findMatch(it1, it2);
        Assert.assertEquals(false, result);

        // Test when left and right are iterators with common values
        HashSet<Number> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        set1.add(3);
        set1.add(4);

        HashSet<Number> set2 = new HashSet<>();
        set2.add(5);
        set2.add(6);
        set2.add(7);
        set2.add(3);

        it1 = set1.iterator();
        it2 = set2.iterator();
        result = expression.findMatch(it1, it2);
        Assert.assertEquals(true, result);

        // Test when only left is iterator and right is value
        it1 = set1.iterator();
        result = expression.containsMatch(it1, 3);
        Assert.assertEquals(true, result);

        // Test when only right is iterator and left is value
        it2 = set2.iterator();
        result = expression.containsMatch(it2, 3);
        Assert.assertEquals(true, result);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}){
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        int precedence = expression.getPrecedence();
        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}){
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        boolean isSymmetric = expression.isSymmetric();
        Assert.assertEquals(false, isSymmetric);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        // Test when input is SelfContext
        EvalContext context = new EvalContext();
        Object result = expression.reduce(new SelfContext(context));
        Assert.assertEquals(context.getSingleNodePointer(), result);

        // Test when input is Collection
        HashSet<Number> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        result = expression.reduce(set);
        Assert.assertTrue(result instanceof Iterator);
    }

}