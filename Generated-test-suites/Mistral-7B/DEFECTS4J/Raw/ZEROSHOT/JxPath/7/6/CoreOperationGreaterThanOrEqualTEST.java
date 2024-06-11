package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.core.Expression;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 private CoreOperationGreaterThanOrEqual operation;
 private EvalContext context;
 private Expression arg1;
 private Expression arg2;
 private InfoSet infoSet;

 @Before
 public void setup() {
 infoSet = new InfoSet();
 context = new EvalContext(infoSet);
 arg1 = new LiteralExpression(new Double(5.0));
 arg2 = new LiteralExpression(new Double(3.0));
 operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 }

 @Test
 public void testComputeValueTrue() {
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueFalse() {
 arg1 = new LiteralExpression(new Double(3.0));
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueWithDifferentTypes() {
 arg1 = new LiteralExpression(new Double(5.0));
 arg2 = new LiteralExpression(new Integer(3));
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArguments() {
 operation = new CoreOperationGreaterThanOrEqual(null, arg2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullRightArgument() {
 operation = new CoreOperationGreaterThanOrEqual(arg1, null);
 }
}