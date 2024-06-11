import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.InfoSetFactory;
import org.apache.commons.jxpath.ri.info.NodeInfo;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

import static org.apache.commons.jxpath.ri.info.NodeInfo.createDoubleNode;
import static org.apache.commons.jxpath.ri.info.NodeInfo.createNode;
import static org.apache.commons.jxpath.ri.info.NodeInfo.createStringNode;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(InfoSetFactory.createEmptyInfoSet());
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(6.0)));
        EvalContext context = new EvalContext(InfoSetFactory.createEmptyInfoSet());
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testEqual() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(InfoSetFactory.createEmptyInfoSet());
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedTypes() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new String("5"))) ;
        EvalContext context = new EvalContext(InfoSetFactory.createEmptyInfoSet());
        operation.computeValue(context);
    }

    @Test
    public void testNegativeNumbers() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(new Double(-5.0)),
                new CoreConstantExpression(new Double(-6.0)));
        EvalContext context = new EvalContext(InfoSetFactory.createEmptyInfoSet());
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(!((Boolean) result).booleanValue());
    }

    @Test
    public void testInfinityAndFiniteNumber() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(new Double(Double.POSITIVE_INFINITY)),
                new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(InfoSetFactory.createEmptyInfoSet());
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testNaNAndFiniteNumber() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(new Double(Double.NaN)),
                new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(InfoSetFactory.createEmptyInfoSet());
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testNaNAndNaN() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreConstantExpression(new Double(Double.NaN)),
                new CoreConstantExpression(new Double(Double.NaN)));
        EvalContext context = new EvalContext(InfoSetFactory.createEmptyInfoSet());
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}