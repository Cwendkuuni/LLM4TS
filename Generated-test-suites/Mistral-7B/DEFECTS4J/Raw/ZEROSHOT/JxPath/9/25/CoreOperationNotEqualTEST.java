package org.apache.commons.jxpath.ri.compiler;

 import static org.junit.Assert.*;

 import org.apache.commons.jxpath.ri.EvalContext;
 import org.apache.commons.jxpath.ri.Expression;
 import org.apache.commons.jxpath.ri.ExpressionFactory;
 import org.apache.commons.jxpath.ri.treeprocessor.StaticContext;
 import org.junit.Before;
 import org.junit.Test;

 /**
 * Test class for CoreOperationNotEqual.
 */
 public class CoreOperationNotEqualTest {

 private CoreOperationNotEqual expression;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @Before
 public void setUp() {
 arg1 = ExpressionFactory.createStringLiteral("test1");
 arg2 = ExpressionFactory.createStringLiteral("test2");
 context = new StaticContext();
 expression = new CoreOperationNotEqual(arg1, arg2);
 }

 @Test
 public void testComputeValue() {
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);

 arg1 = ExpressionFactory.createNumberLiteral(1);
 arg2 = ExpressionFactory.createNumberLiteral(2);
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArguments() {
 expression = new CoreOperationNotEqual(null, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArg2() {
 expression = new CoreOperationNotEqual(arg1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSameArguments() {
 arg1 = arg2;
 expression = new CoreOperationNotEqual(arg1, arg2);
 }

 @Test
 public void testGetSymbol() {
 String symbol = expression.getSymbol();
 assertEquals("!=", symbol);
 }
 }