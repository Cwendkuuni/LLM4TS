import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_LessThan() {
        EvalContext context = new EvalContext();
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        arg1.setValue(context, 5);
        arg2.setValue(context, 10);
        Assert.assertEquals(Boolean.TRUE, op.computeValue(context));

        arg1.setValue(context, 10);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));

        arg1.setValue(context, 5);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));

        arg1.setValue(context, null);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));

        arg1.setValue(context, null);
        arg2.setValue(context, null);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));
    }

    @Test
    public void testComputeValue_LessThanOrEquals() {
        EvalContext context = new EvalContext();
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };

        arg1.setValue(context, 5);
        arg2.setValue(context, 10);
        Assert.assertEquals(Boolean.TRUE, op.computeValue(context));

        arg1.setValue(context, 10);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));

        arg1.setValue(context, 5);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.TRUE, op.computeValue(context));

        arg1.setValue(context, null);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));

        arg1.setValue(context, null);
        arg2.setValue(context, null);
        Assert.assertEquals(Boolean.TRUE, op.computeValue(context));
    }

    @Test
    public void testComputeValue_GreaterThan() {
        EvalContext context = new EvalContext();
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        arg1.setValue(context, 5);
        arg2.setValue(context, 10);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));

        arg1.setValue(context, 10);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.TRUE, op.computeValue(context));

        arg1.setValue(context, 5);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));

        arg1.setValue(context, null);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));

        arg1.setValue(context, null);
        arg2.setValue(context, null);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));
    }

    @Test
    public void testComputeValue_GreaterThanOrEquals() {
        EvalContext context = new EvalContext();
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        arg1.setValue(context, 5);
        arg2.setValue(context, 10);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));

        arg1.setValue(context, 10);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.TRUE, op.computeValue(context));

        arg1.setValue(context, 5);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.TRUE, op.computeValue(context));

        arg1.setValue(context, null);
        arg2.setValue(context, 5);
        Assert.assertEquals(Boolean.FALSE, op.computeValue(context));

        arg1.setValue(context, null);
        arg2.setValue(context, null);
        Assert.assertEquals(Boolean.TRUE, op.computeValue(context));
    }

    @Test
    public void testReduce_SingleSelfContext() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(null) {};
        EvalContext evalContext = new EvalContext();
        SelfContext selfContext = new SelfContext(evalContext.getSingleNodePointer());
        Assert.assertSame(evalContext.getSingleNodePointer(), op.reduce(selfContext));
    }

    @Test
    public void testReduce_SingleCollection() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(null) {};

        Collection<Integer> collection = new ArrayList<>(Arrays.asList(1, 2, 3));
        Iterator<Integer> iterator = collection.iterator();

        Assert.assertSame(iterator, op.reduce(collection));
    }

    @Test
    public void testReduce_SingleIterator() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(null) {};

        Collection<Integer> collection = new ArrayList<>(Arrays.asList(1, 2, 3));
        Iterator<Integer> iterator = collection.iterator();

        Assert.assertSame(iterator, op.reduce(iterator));
    }

    @Test
    public void testReduce_Null() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(null) {};

        Assert.assertNull(op.reduce(null));
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(null) {};

        Assert.assertTrue(op.evaluateCompare(-1));
        Assert.assertFalse(op.evaluateCompare(0));
        Assert.assertFalse(op.evaluateCompare(1));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(null) {};
        List<String> list = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = list.iterator();

        Assert.assertTrue(op.containsMatch(iterator, "two"));
        Assert.assertFalse(op.containsMatch(iterator, "four"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression op = new CoreOperationRelationalExpression(null) {};
        List<String> list1 = Arrays.asList("one", "two", "three");
        List<String> list2 = Arrays.asList("two", "four", "six");
        Iterator<String> iterator1 = list1.iterator();
        Iterator<String> iterator2 = list2.iterator();

        Assert.assertTrue(op.findMatch(iterator1, iterator2));
        Assert.assertFalse(op.findMatch(iterator1, iterator2));
    }

    private static class MockExpression extends Expression {

        private Object value;

        public void setValue(EvalContext context, Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

    }
}