import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expression1, expression2);

        EvalContext context = new EvalContext();

        boolean result = coreOperationCompare.equal(context, expression1, expression2);

        Assert.assertEquals(false, result);

    }

    @Test
    public void testGetPrecedence() {
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expression1, expression2);

        int precedence = coreOperationCompare.getPrecedence();

        Assert.assertEquals(2, precedence);

    }

    @Test
    public void testIsSymmetric() {
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expression1, expression2);

        boolean symmetric = coreOperationCompare.isSymmetric();

        Assert.assertEquals(true, symmetric);

    }

    @Test
    public void testContains() {
        Expression expression = new Expression();
        Iterator iterator = expression.iterator();
        Object value = new Object();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expression, expression);

        boolean contains = coreOperationCompare.contains(iterator, value);

        Assert.assertEquals(false, contains);

    }

    @Test
    public void testFindMatch() {
        Expression expression1 = new Expression();
        Expression expression2 = new Expression();
        Iterator iterator1 = expression1.iterator();
        Iterator iterator2 = expression2.iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expression1, expression2);

        boolean match = coreOperationCompare.findMatch(iterator1, iterator2);

        Assert.assertEquals(false, match);

    }

    @Test
    public void testEqual() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());

        boolean result = coreOperationCompare.equal(obj1, obj2);

        Assert.assertEquals(false, result);

    }

}