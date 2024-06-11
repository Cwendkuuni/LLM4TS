import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.iter.CollectionIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.jxpath.ri.axes.Axis.CHILD;
import static org.apache.commons.jxpath.ri.axes.Axis.DESCENDANT;
import static org.apache.commons.jxpath.ri.axes.Axis.DESCENDANT_OR_SELF;
import static org.apache.commons.jxpath.ri.axes.Axis.SELF;
import static org.apache.commons.jxpath.ri.axes.Axis.selfAxis;

/**
 * Test file for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        // Test with numbers
        EvalContext context = new EvalContext();
        InitialContext leftContext = new InitialContext(context);
        leftContext.setNode(1.0);
        InitialContext rightContext = new InitialContext(context);
        rightContext.setNode(2.0);
        CoreOperationRelationalExpression greaterThan = new CoreOperationGreaterThan(new CoreExpressionNumber(leftContext),
                new CoreExpressionNumber(rightContext));
        Object result = greaterThan.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with Iterators
        List<Integer> leftList = new ArrayList<>();
        leftList.add(1);
        leftList.add(2);
        Iterate leftIter = new CollectionIterator(leftList);

        List<Integer> rightList = new ArrayList<>();
        rightList.add(2);
        rightList.add(3);
        Iterate rightIter = new CollectionIterator(rightList);

        CoreOperationRelationalExpression greaterThanIterator = new CoreOperationGreaterThan(leftIter, rightIter);
        result = greaterThanIterator.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        leftIter = new SingletonIterator(leftList.get(0));
        rightIter = new SingletonIterator(rightList.get(1));
        greaterThanIterator = new CoreOperationGreaterThan(leftIter, rightIter);
        result = greaterThanIterator.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        leftIter = new SingletonIterator(leftList.get(1));
        rightIter = new SingletonIterator(rightList.get(0));
        greaterThanIterator = new CoreOperationGreaterThan(leftIter, rightIter);
        result = greaterThanIterator.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with Axes
        Iterator leftAxis = context.getIterator(selfAxis(), context.getDocument());
        Iterator rightAxis = context.getIterator(DESCENDANT, context.getDocument());
        CoreOperationRelationalExpression greaterThanAxis = new CoreOperationGreaterThan(leftAxis, rightAxis);
        result = greaterThanAxis.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        // Test with numbers
        EvalContext context = new EvalContext();
        InitialContext leftContext = new InitialContext(context);
        leftContext.setNode(1.0);
        InitialContext rightContext = new InitialContext(context);
        rightContext.setNode(1.0);
        CoreOperationRelationalExpression greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionNumber(leftContext), new CoreExpressionNumber(rightContext));
        Object result = greaterThanOrEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        leftContext.setNode(1.0);
        rightContext.setNode(2.0);
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionNumber(leftContext), new CoreExpressionNumber(rightContext));
        result = greaterThanOrEqual.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with Iterators
        List<Integer> leftList = new ArrayList<>();
        leftList.add(1);
        leftList.add(2);
        Iterate leftIter = new CollectionIterator(leftList);

        List<Integer> rightList = new ArrayList<>();
        rightList.add(2);
        rightList.add(1);
        Iterate rightIter = new CollectionIterator(rightList);

        CoreOperationRelationalExpression greaterThanOrEqualIterator = new CoreOperationGreaterThanOrEqual(leftIter, rightIter);
        result = greaterThanOrEqualIterator.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        leftIter = new SingletonIterator(leftList.get(0));
        rightIter = new SingletonIterator(rightList.get(0));
        greaterThanOrEqualIterator = new CoreOperationGreaterThanOrEqual(leftIter, rightIter);
        result = greaterThanOrEqualIterator.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        leftIter = new SingletonIterator(leftList.get(1));
        rightIter = new SingletonIterator(rightList.get(1));
        greaterThanOrEqualIterator = new CoreOperationGreaterThanOrEqual(leftIter, rightIter);
        result = greaterThanOrEqualIterator.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with Axes
        Iterator leftAxis = context.getIterator(selfAxis(), context.getDocument());
        Iterator rightAxis = context.getIterator(DESCENDANT, context.getDocument());
        CoreOperationRelationalExpression greaterThanOrEqualAxis = new CoreOperationGreaterThanOrEqual(leftAxis, rightAxis);
        result = greaterthanOrEqualAxis.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThan() {
        // Test with numbers
        EvalContext context = new EvalContext();
        InitialContext leftContext = new InitialContext(context);
        leftContext.setNode(1.0);
        InitialContext rightContext = new InitialContext(context);
        rightContext.setNode(2.0);
        CoreOperationRelationalExpression lessThan = new CoreOperationLessThan(
                new CoreExpressionNumber(leftContext), new CoreExpressionNumber(rightContext));
        Object result = lessThan.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with Iterators
        List<Integer> leftList = new ArrayList<>();
        leftList.add(1);
        leftList.add(2);
        Iterate leftIter = new CollectionIterator(leftList);

        List<Integer> rightList = new ArrayList<>();
        rightList.add(2);
        rightList.add(1);
        Iterate rightIter = new CollectionIterator(rightList);

        CoreOperationRelationalExpression lessThanIterator = new CoreOperationLessThan(leftIter, rightIter);
        result = lessThanIterator.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        leftIter = new SingletonIterator(leftList.get(0));
        rightIter = new SingletonIterator(rightList.get(1));
        lessThanIterator = new CoreOperationLessThan(leftIter, rightIter);
        result = lessThanIterator.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        leftIter = new SingletonIterator(leftList.get(1));
        rightIter = new SingletonIterator(rightList.get(0));
        lessThanIterator = new CoreOperationLessThan(leftIter, rightIter);
        result = lessThanIterator.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with Axes
        Iterator leftAxis = context.getIterator(selfAxis(), context.getDocument());
        Iterator rightAxis = context.getIterator(DESCENDANT, context.getDocument());
        CoreOperationRelationalExpression lessThanAxis = new CoreOperationLessThan(leftAxis, rightAxis);
        result = lessThanAxis.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanOrEqual() {
        // Test with numbers
        EvalContext context = new EvalContext();
        InitialContext leftContext = new InitialContext(context);
        leftContext.setNode(1.0);
        InitialContext rightContext = new InitialContext(context);
        rightContext.setNode(1.0);
        CoreOperationRelationalExpression lessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreExpressionNumber(leftContext), new CoreExpressionNumber(rightContext));
        Object result = lessThanOrEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        leftContext.setNode(1.0);
        rightContext.setNode(2.0);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(
                new CoreExpressionNumber(leftContext), new CoreExpressionNumber(rightContext));
        result = lessThanOrEqual.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with Iterators
        List<Integer> leftList = new ArrayList<>();
        leftList.add(1);
        leftList.add(2);
        Iterate leftIter = new CollectionIterator(leftList);

        List<Integer> rightList = new ArrayList<>();
        rightList.add(2);
        rightList.add(1);
        Iterate rightIter = new CollectionIterator(rightList);

        CoreOperationRelationalExpression lessThanOrEqualIterator = new CoreOperationLessThanOrEqual(leftIter, rightIter);
        result = lessThanOrEqualIterator.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        leftIter = new SingletonIterator(leftList.get(0));
        rightIter = new SingletonIterator(rightList.get(0));
        lessThanOrEqualIterator = new CoreOperationLessThanOrEqual(leftIter, rightIter);
        result = lessthanOrEqualIterator.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        leftIter = new SingletonIterator(leftList.get(1));
        rightIter = new SingletonIterator(rightList.get(1));
        lessThanOrEqualIterator = new CoreOperationLessThanOrEqual(leftIter, rightIter);
        result = lessThanOrEqualIterator.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with Axes
        Iterator leftAxis = context.getIterator(selfAxis(), context.getDocument());
        Iterator rightAxis = context.getIterator(DESCENDANT, context.getDocument());
        CoreOperationRelationalExpression lessThanOrEqualAxis = new CoreOperationLessThanOrEqual(leftAxis, rightAxis);
        result = lessThanOrEqualAxis.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

}