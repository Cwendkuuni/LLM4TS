import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.w3c.dom.DOMStringing;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.apache.commons.jxpath.ri.test.TestUtils.newDoubleValue;
import static org.apache.commons.jxpath.ri.test.TestUtils.newDoubleValueInfoSet;
import static org.apache.commons.jxpath.ri.test.TestUtils.newExpression;
import static org.apache.commons.jxpath.ri.test.TestUtils.newInfoSet;
import static org.apache.commons.jxpath.ri.test.TestUtils.newNodeInfo;
import static org.apache.commons.jxpath.ri.test.TestUtils.newVariable;

public class CoreOperationGreaterThanTest {

    @Test
    public void testGreaterThanDoublePositive() {
        Expression arg1 = newVariable(newDoubleValue(5.0));
        Expression arg2 = newVariable(newDoubleValue(3.0));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertTrue(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanDoubleNegative() {
        Expression arg1 = newVariable(newDoubleValue(-3.0));
        Expression arg2 = newVariable(newDoubleValue(-5.0));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertFalse(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanDoubleEqual() {
        Expression arg1 = newVariable(newDoubleValue(5.0));
        Expression arg2 = newVariable(newDoubleValue(5.0));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertFalse(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanNaN() {
        Expression arg1 = newVariable(new Double("NaN"));
        Expression arg2 = newVariable(new Double("NaN"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertFalse(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanBigDecimalPositive() {
        Expression arg1 = newVariable(new BigDecimal("5.0"));
        Expression arg2 = newVariable(new BigDecimal("3.0"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertTrue(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanBigDecimalNegative() {
        Expression arg1 = newVariable(new BigDecimal("-3.0"));
        Expression arg2 = newVariable(new BigDecimal("-5.0"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertFalse(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanBigDecimalEqual() {
        Expression arg1 = newVariable(new BigDecimal("5.0"));
        Expression arg2 = newVariable(new BigDecimal("5.0"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertFalse(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanBigDecimalNaN() {
        Expression arg1 = newVariable(new BigDecimal("NaN"));
        Expression arg2 = newVariable(new BigDecimal("NaN"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertFalse(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanBigIntegerPositive() {
        Expression arg1 = newVariable(new BigInteger("5"));
        Expression arg2 = newVariable(new BigInteger("3"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertTrue(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanBigIntegerNegative() {
        Expression arg1 = newVariable(new BigInteger("-3"));
        Expression arg2 = newVariable(new BigInteger("-5"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertFalse(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanBigIntegerEqual() {
        Expression arg1 = newVariable(new BigInteger("5"));
        Expression arg2 = newVariable(new BigInteger("5"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertFalse(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanBigIntegerNaN() {
        Expression arg1 = newVariable(new BigInteger("NaN"));
        Expression arg2 = newVariable(new BigInteger("NaN"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertFalse(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanString() {
        Expression arg1 = newVariable(new DOMStringing("5"));
        Expression arg2 = newVariable(new DOMStringing("3"));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertThrows(ClassCastException.class, () -> op.computeValue(newEvalContext()));
    }

    @Test
    public void testGreaterThanNull() {
        Expression arg1 = newVariable(null);
        Expression arg2 = newVariable(new Double(5.0));
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertFalse(op.computeValue(newEvalContext()).booleanValue());
    }

    @Test
    public void testGreaterThanNull2() {
        Expression arg1 = newVariable(new Double(5.0));
        Expression arg2 = newVariable(null);
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertTrue(op.computeValue(newEvalContext()).booleanValue());
    }

    private static class EvalContext extends org.apache.commons.jxpath.ri.EvalContext {

        private EvalContext() {
            super(newInfoSet(null));
        }

        static EvalContext newEvalContext() {
            return new EvalContext();
        }
    }

    private static InfoSet newInfoSet(NodeInfo root) {
        return newInfoSet(new EvalContext(), root);
    }
}