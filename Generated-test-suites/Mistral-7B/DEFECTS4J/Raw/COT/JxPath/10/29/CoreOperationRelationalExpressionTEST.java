import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new CoreConstantExpression(new Integer(5));
        Expression arg2 = new CoreConstantExpression(new Integer(10));
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreGTExpression(new CoreExpression[] {arg1, arg2});

        Object result = coreOperationRelationalExpression.computeValue(new EvalContext(new SelfContext(null)));

        assert (result instanceof Boolean);
        assert (result == Boolean.FALSE);

        CoreOperationRelationalExpression coreOperationRelationalExpression2 = new CoreGEExpression(new CoreExpression[] {arg1, arg2});

        result = coreOperationRelationalExpression2.computeValue(new EvalContext(new SelfContext(null)));

        assert (result instanceof Boolean);
        assert (result == Boolean.FALSE);

        arg1 = new CoreConstantExpression(new Integer(5));
        arg2 = new CoreConstantExpression(new Integer(5));
        coreOperationRelationalExpression = new CoreGTExpression(new CoreExpression[] {arg1, arg2});

        result = coreOperationRelationalExpression.computeValue(new EvalContext(new SelfContext(null)));

        assert (result instanceof Boolean);
        assert (result == Boolean.FALSE);

        coreOperationRelationalExpression2 = new CoreGEExpression(new CoreExpression[] {arg1, arg2});

        result = coreOperationRelationalExpression2.computeValue(new EvalContext(new SelfContext(null)));

        assert (result instanceof Boolean);
        assert (result == Boolean.TRUE);

        arg1 = new CoreConstantExpression(new Integer(5));
        arg2 = new CoreConstantExpression(new Integer(5));
        coreOperationRelationalExpression = new CoreEQExpression(new CoreExpression[] {arg1, arg2});

        result = coreOperationRelationalExpression.computeValue(new EvalContext(new SelfContext(null)));

        assert (result instanceof Boolean);
        assert (result == Boolean.TRUE);
    }

    @Test(expected = AssertionError.class)
    public void testComputeValueWithNullArg() {
        Expression arg1 = new CoreConstantExpression(null);
        Expression arg2 = new CoreConstantExpression(new Integer(1));
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreGTExpression(new CoreExpression[] {arg1, arg2});

        coreOperationRelationalExpression.computeValue(new EvalContext(new SelfContext(null)));
    }

    @Test(expected = AssertionError.class)
    public void testComputeValueWithNullArg2() {
        Expression arg1 = new CoreConstantExpression(new Integer(1));
        Expression arg2 = new CoreConstantExpression(null);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreGTExpression(new CoreExpression[] {arg1, arg2});

        coreOperationRelationalExpression.computeValue(new EvalContext(new SelfContext(null)));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreGTExpression(new CoreExpression[0]);

        int result = coreOperationRelationalExpression.getPrecedence();

        assert (result == 3);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreGTExpression(new CoreExpression[0]);

        boolean result = coreOperationRelationalExpression.isSymmetric();

        assert (result == false);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreGTExpression(new CoreExpression[0]);

        boolean result = coreOperationRelationalExpression.evaluateCompare(1);

        assert (result == false);

        coreOperationRelationalExpression = new CoreGEExpression(new CoreExpression[0]);

        result = coreOperationRelationalExpression.evaluateCompare(0);

        assert (result == true);

        coreOperationRelationalExpression = new CoreNEExpression(new CoreExpression[0]);

        result = coreOperationRelationalExpression.evaluateCompare(0);

        assert (result == false);

        result = coreOperationRelationalExpression.evaluateCompare(1);

        assert (result == true);
    }

    @Test
    public void testReduce() {
        Expression arg = new CoreConstantExpression(new Integer(5));

        Object result = new CoreOperationRelationalExpression(new CoreExpression[] {arg}).reduce(arg);

        assert (result instanceof Iterator);
        assert (result == arg.iterator());

        arg = new CoreConstantExpression(new ArrayList<>());

        result = new CoreOperationRelationalExpression(new CoreExpression[] {arg}).reduce(arg);

        assert (result instanceof Collection);
        assert (result == arg);

        arg = new CoreConstantExpression(new SelfContext(null));

        result = new CoreOperationRelationalExpression(new CoreExpression[] {arg}).reduce(arg);

        assert (result instanceof InitialContext);
        assert (result == arg);
    }

    @Test
    public void testContainsMatch() {
        Iterator iterator = new ArrayList<Object>().iterator();
        Object value = new Integer(5);

        boolean result = new CoreOperationRelationalExpression(new CoreExpression[] {new CoreConstantExpression(new Integer(1)), new CoreConstantExpression(new Integer(2))}).containsMatch(iterator, value);

        assert (result == false);

        iterator = new ArrayList<Object>() {{
            add(value);
            addAll(Collections.singletonList(iterator));
        }}.iterator();

        result = new CoreOperationRelationalExpression(new CoreExpression[] {iterator, new CoreConstantExpression(new Integer(5))}).containsMatch(iterator, value);

        assert (result == true);

        iterator = new ArrayList<Object>() {{
            add(value);
            addAll(Collections.singletonList(iterator));
        }}.iterator();

        result = new CoreOperationRelationalExpression(new CoreExpression[] {new CoreConstantExpression(new Integer(5)), iterator}).containsMatch(iterator, value);

        assert (result == true);

        iterator = new ArrayList<Object>() {{
            add(value);
            addAll(Collections.singletonList(iterator));
        }}.iterator();

        result = new CoreOperationRelationalExpression(new CoreExpression[] {iterator, new CoreConstantExpression(new Integer(5))}).containsMatch(iterator, value);

        assert (result == false);
    }

    @Test
    public void testFindMatch() {
        Iterator iterator1 = new ArrayList<Object>() {{
            add(new Integer(1));
            add(new Integer(5));
            addAll(Collections.singletonList(iterator2));
        }}.iterator();
        Iterator iterator2 = new ArrayList<Object>() {{
            add(new Integer(5));
            addAll(Collections.singletonList(iterator1));
        }}.iterator();

        boolean result = new CoreOperationRelationalExpression(new CoreExpression[] {iterator1, iterator2}).findMatch(iterator1, iterator2);

        assert (result == true);

        iterator1 = new ArrayList<Object>() {{
            add(new Integer(1));
            addAll(Collections.singletonList(iterator2));
        }}.iterator();
        iterator2 = new ArrayList<Object>() {{
            addAll(Collections.singletonList(iterator1));
            add(new Integer(5));
        }}.iterator();

        result = new CoreOperationRelationalExpression(new CoreExpression[] {iterator1, iterator2}).findMatch(iterator1, iterator2);

        assert (result == false);
    }
}