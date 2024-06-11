import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_GreaterThan() {
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        left.setValue(10);
        right.setValue(5);
        Assert.assertEquals(true, expression.computeValue(context));

        left.setValue(5);
        right.setValue(10);
        Assert.assertEquals(false, expression.computeValue(context));
    }

    @Test
    public void testComputeValue_GreaterThanOrEqual() {
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        left.setValue(10);
        right.setValue(5);
        Assert.assertEquals(true, expression.computeValue(context));

        left.setValue(5);
        right.setValue(5);
        Assert.assertEquals(true, expression.computeValue(context));

        left.setValue(5);
        right.setValue(10);
        Assert.assertEquals(false, expression.computeValue(context));
    }

    @Test
    public void testComputeValue_LessThan() {
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        left.setValue(5);
        right.setValue(10);
        Assert.assertEquals(true, expression.computeValue(context));

        left.setValue(10);
        right.setValue(5);
        Assert.assertEquals(false, expression.computeValue(context));
    }

    @Test
    public void testComputeValue_LessThanOrEqual() {
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        left.setValue(5);
        right.setValue(10);
        Assert.assertEquals(true, expression.computeValue(context));

        left.setValue(5);
        right.setValue(5);
        Assert.assertEquals(true, expression.computeValue(context));

        left.setValue(10);
        right.setValue(5);
        Assert.assertEquals(false, expression.computeValue(context));
    }

    @Test
    public void testCompute_Collection() {
        Expression left = new Expression();
        Expression right = new Expression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        HashSet<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3));

        left.setValue(set);
        right.setValue(2);
        Assert.assertEquals(true, expression.compute(context));

        left.setValue(set);
        right.setValue(4);
        Assert.assertEquals(false, expression.compute(context));
    }

    @Test
    public void testCompute_Iterators() {
        Expression left = new Expression();
        Expression right = new Expression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator<Integer> it1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> it2 = Arrays.asList(4, 5, 6).iterator();

        left.setValue(it1);
        right.setValue(it2);
        Assert.assertEquals(false, expression.compute(context));

        it1 = Arrays.asList(1, 2, 3).iterator();
        it2 = Arrays.asList(2, 3, 4).iterator();

        left.setValue(it1);
        right.setValue(it2);
        Assert.assertEquals(true, expression.compute(context));
    }

    @Test
    public void testCompute_NaNValues() {
        Expression left = new Expression();
        Expression right = new Expression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {left, right}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        left.setValue(Double.NaN);
        right.setValue(2);
        Assert.assertEquals(false, expression.compute(context));

        left.setValue(2);
        right.setValue(Double.NaN);
        Assert.assertEquals(false, expression.compute(context));
    }
}