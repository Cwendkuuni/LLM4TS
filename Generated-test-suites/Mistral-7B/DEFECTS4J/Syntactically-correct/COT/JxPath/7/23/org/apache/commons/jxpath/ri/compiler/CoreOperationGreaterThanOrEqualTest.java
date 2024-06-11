package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.test.AbstractExpressionTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

 /**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
 public class CoreOperationGreaterThanOrEqualTest extends AbstractExpressionTest {

 // List of all the public methods with their signatures
 /**
 * Constructor for CoreOperationGreaterThanOrEqual object.
 *
 * @param arg1 Expression
 * @param arg2 Expression
 */
 public CoreOperationGreaterThanOrEqualTest(Expression arg1, Expression arg2) {
 super(new CoreOperationGreaterThanOrEqual(arg1, arg2));
 }

 /**
 * Method to test the functionality of the computeValue method.
 */
 @Test
 public void testComputeValue() {
 // Basic test case
 InfoSet is = createInfoSet();
 EvalContext context = new EvalContext(is);
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(5.5), new ConstantExpression(5.5));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 result = expression.computeValue(context);
 assertEquals(Boolean.TRUE, result);

 // Edge cases and exception handling test cases
 Expression arg1 = new ConstantExpression(-1.0);
 Expression arg2 = new ConstantExpression(0.0);
 CoreOperationGreaterThanOrEqual testExpression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 result = testExpression.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);

 arg1 = new ConstantExpression(0.0);
 arg2 = new ConstantExpression(-1.0);
 testExpression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 result = testExpression.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);

 arg1 = new ConstantExpression(0.0);
 arg2 = new ConstantExpression(0.0);
 testExpression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 result = testExpression.computeValue(context);
 assertNotNull(result);
 assertFalse(result instanceof Boolean);
 result = testExpression.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 /**
 * Method to test the getSymbol method.
 */
 @Test
 public void testGetSymbol() {
 CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
 new ConstantExpression(5.5), new ConstantExpression(5.5));
 assertEquals(">=", expression.getSymbol());
 }
 }