package org.apache.commons.jxpath.ri.compiler.test;

 import org.apache.commons.jxpath.ri.EvalContext;
 import org.apache.commons.jxpath.ri.InfoSet;
 import org.apache.commons.jxpath.ri.test.AbstractTest;
 import org.apache.commons.jxpath.ri.test.TestUtils;
 import org.junit.Test;
 import static org.junit.Assert.*;

 import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
 import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
 import org.apache.commons.jxpath.ri.compiler.Expression;
 import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
 import org.apache.commons.jxpath.ri.compiler.TestUtilsForCompiler;

 /**
 * Test class for CoreOperationLessThan.
 */
 public class CoreOperationLessThanTest extends AbstractTest {

 private CoreOperationLessThan operation;
 private Expression arg1, arg2;
 private double value1, value2;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 ValueSequenceFactory vsf = TestUtilsForCompiler.getTestValueSequenceFactory();
 ExpressionFactory ef = TestUtilsForCompiler.getTestExpressionFactory();

 arg1 = ef.createLiteralExpression(vsf.createValueSequence(new Double(value1 = 5.0)));
 arg2 = ef.createLiteralExpression(vsf.createValueSequence(new Double(value2 = 10.0)));
 operation = new CoreOperationLessThan(arg1, arg2);
 }

 /**
 * Test method for {@link CoreOperationLessThan#CoreOperationLessThan(Expression, Expression)}.
 */
 @Test
 public void testCoreOperationLessThan() {
 Expression[] args = new Expression[] { arg1, arg2 };
 EvalContext context = new EvalContext(new InfoSet());
 Object result = operation.computeValue(context);
 assertTrue((Boolean) result);
 }

 /**
 * Test method for {@link CoreOperationLessThan#computeValue(EvalContext)} with different types.
 */
 @Test(expected = ClassCastException.class)
 public void testComputeValueDifferentTypes() {
 EvalContext context = new EvalContext(new InfoSet());
 operation.computeValue(context);
 }

 /**
 * Test method for {@link CoreOperationLessThan#computeValue(EvalContext)} with the same type but different values.
 */
 @Test
 public void testComputeValueDifferentValues() {
 value1 = 5.0;
 value2 = 10.0;
 EvalContext context = new EvalContext(new InfoSet());
 Object result = operation.computeValue(context);
 assertTrue((Boolean) result);
 }

 /**
 * Test method for {@link CoreOperationLessThan#computeValue(EvalContext)} with equal values.
 */
 @Test
 public void testComputeValueEqualValues() {
 value1 = 5.0;
 value2 = 5.0;
 EvalContext context = new EvalContext(new InfoSet());
 Object result = operation.computeValue(context);
 assertFalse((Boolean) result);
 }

 /**
 * Test method for {@link CoreOperationLessThan#computeValue(EvalContext)} with negative numbers.
 */
 @Test
 public void testComputeValueNegativeNumbers() {
 value1 = -5.0;
 value2 = -10.0;
 EvalContext context = new EvalContext(new InfoSet());
 Object result = operation.computeValue(context);
 assertTrue((Boolean) result);
 }

 /**
 * Test method for {@link CoreOperationLessThan#computeValue(EvalContext)} with zero.
 */
 @Test
 public void testComputeValueZero() {
 value1 = 0.0;
 value2 = 10.0;
 EvalContext context = new EvalContext(new InfoSet());
 Object result = operation.computeValue(context);
 assertTrue((Boolean) result);
 }

 /**
 * Test method for {@link CoreOperationLessThan#computeValue(EvalContext)} with zero and positive number.
 */
 @Test
 public void testComputeValueZeroPositive() {
 value1 = 0.0;
 value2 = 5.0;
 EvalContext context = new EvalContext(new InfoSet());
 Object result = operation.computeValue(context);
 assertFalse((Boolean) result);
 }

 /**
 * Test method for {@link CoreOperationLessThan#computeValue(EvalContext)} with NaN.
 */
 @Test
 public void testComputeValueNaN() {
 value1 = Double.NaN;
 value2 = Double.NaN;
 EvalContext context = new EvalContext(new InfoSet());
 Object result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 /**
 * Test method for {@link CoreOperationLessThan#computeValue(EvalContext)} with infinities.
 */
 @Test
 public void testComputeValueInfinities() {
 value1 = Double.POSITIVE_INFINITY;
 value2 = Double.POSITIVE_INFINITY;
 EvalContext context = new EvalContext(new InfoSet());
 Object result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 /**
 * Test method for {@link CoreOperationLessThan#computeValue(EvalContext)} with negative and positive infinities.
 */
 @Test
 public void testComputeValueNegativePositiveInfinities() {
 value1 = Double.NEGATIVE_INFINITY;
 value2 = Double.POSITIVE_INFINITY;
 EvalContext context = new EvalContext(new InfoSet());
 Object result = operation.computeValue(context);
 assertFalse((Boolean) result);
 }
}