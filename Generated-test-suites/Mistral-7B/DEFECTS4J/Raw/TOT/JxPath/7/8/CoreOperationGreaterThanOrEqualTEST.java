package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.nodes.Expression;
import org.apache.commons.jxpath.ri.nodes.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqual() {
 Expression arg1 = new LiteralExpression(5.0);
 Expression arg2 = new LiteralExpression(5.0);

 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 EvalContext context = new EvalContext(new InfoSet());

 Object result = op.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanButNotEqual() {
 Expression arg1 = new LiteralExpression(5.0);
 Expression arg2 = new LiteralExpression(4.0);

 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 EvalContext context = new EvalContext(new InfoSet());

 Object result = op.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotGreaterThan() {
 Expression arg1 = new LiteralExpression(4.0);
 Expression arg2 = new LiteralExpression(5.0);

 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 EvalContext context = new EvalContext(new InfoSet());

 Object result = op.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testNullArg1() {
 Expression arg1 = null;
 Expression arg2 = new LiteralExpression(5.0);

 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 EvalContext context = new EvalContext(new InfoSet());

 try {
 op.computeValue(context);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testNullArg2() {
 Expression arg1 = new LiteralExpression(5.0);
 Expression arg2 = null;

 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 EvalContext context = new EvalContext(new InfoSet());

 try {
 op.computeValue(context);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testMixedTypes() {
 Expression arg1 = new LiteralExpression(5.0);
 Expression arg2 = new LiteralExpression("5");

 CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 EvalContext context = new EvalContext(new InfoSet());

 try {
 op.computeValue(context);
 } catch (ClassCastException e) {
 // expected
 }
 }
}