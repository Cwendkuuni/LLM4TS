import org.apache.commons.jxpath.ri. compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(4));
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext()));
    }

    @Test
    public void testGivenGreaterThanWithInvalidInput() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber("String"), new CoreNumber(4));
        assertFalse(expression.computeValue(new EvalContext()) instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(5));
        assertTrue(expression.computeValue(new EvalContext()));
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(4), new CoreNumber(5));
        assertFalse(expression.computeValue(new EvalContext()));
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(5));
        assertTrue(expression.computeValue(new EvalContext()));
        CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(4));
        assertFalse(expression2.computeValue(new EvalContext()));
    }

    @Test(expected = ClassCastException.class)
    public void testFindMatchWithIteratorAndNonIterable() {
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(it, new CoreNumber(1));
        expression.findMatch(it, new CoreNumber(2));
    }

    @Test(expected = ClassCastException.class)
    public void testFindMatchWithIteratorAndNonIterator() {
        Iterator it = new Object();
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(it, rit);
        expression.findMatch(it, new CoreNumber(1));
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullEvalContext() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreNumber(5), new CoreNumber(5));
        expression.computeValue(null);
    }
}