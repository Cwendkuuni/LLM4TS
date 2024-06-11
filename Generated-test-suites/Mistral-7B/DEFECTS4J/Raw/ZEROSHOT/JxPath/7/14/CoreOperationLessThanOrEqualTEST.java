package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.nodes.DoubleNode;

public class CoreOperationLessThanOrEqualTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationLessThanOrEqual op;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 arg1 = ExpressionFactory.createConstant(1.5);
 arg2 = ExpressionFactory.createConstant(2.5);
 op = new CoreOperationLessThanOrEqual(arg1, arg2);
 }

 @Test
 public void testComputeValueTrue() throws Exception {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueFalse() throws Exception {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 arg2 = ExpressionFactory.createConstant(1.5);
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testComputeValueWithDifferentTypes() throws Exception {
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 arg2 = ExpressionFactory.createStringConstant("2");
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testSymbol() throws Exception {
 String symbol = op.getSymbol();
 assertEquals("<=", symbol);
 }

 @Test
 public void testCombineExpressions() throws Exception {
 Expression arg1_1 = ExpressionFactory.createConstant(1.5);
 Expression arg2_1 = ExpressionFactory.createConstant(2.5);
 CoreOperationRelationalExpression op_1 = new CoreOperationLessThanOrEqual(arg1_1, arg2_1);
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Expression arg1 = new DoubleNode(1.5);
 Expression arg2 = new DoubleNode(2.5);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);
 Object result = op.computeValue(context);
 assertEquals(op_1.computeValue(context), result);
 }
}