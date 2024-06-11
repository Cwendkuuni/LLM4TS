package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

 @Test
 public void testCoreOperationGreaterThan() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstExpression(new Double(5.0)), new ConstExpression(new Double(3.0)));
 Expression context = new EvaluationContext();
 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeImpl(new QName("root"), null, null, null, null, null));
 context.setInfoSet(infoSet);
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCoreOperationGreaterThanFalse() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstExpression(new Double(3.0)), new ConstExpression(new Double(5.0)));
 Expression context = new EvaluationContext();
 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeImpl(new QName("root"), null, null, null, null, null));
 context.setInfoSet(infoSet);
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCoreOperationGreaterThanNullArg1() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, new ConstExpression(new Double(5.0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCoreOperationGreaterThanNullArg2() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstExpression(new Double(5.0)), null);
 }

 @Test
 public void testCoreOperationGreaterThanWithMixedTypes() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstExpression(new Double(5.0)), new ConstExpression(new Integer(3)));
 Expression context = new EvaluationContext();
 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeImpl(new QName("root"), null, null, null, null, null));
 context.setInfoSet(infoSet);
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse(result instanceof Number);
 }

 @Test
 public void testCoreOperationGreaterThanWithStrings() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new StringConstant("5"), new StringConstant("3"));
 Expression context = new EvaluationContext();
 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeImpl(new QName("root"), null, null, null, null, null));
 context.setInfoSet(infoSet);
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse(result instanceof String);
 }

 @Test
 public void testCoreOperationGreaterThanWithDifferentTypes() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstExpression(new Double(5.0)), new ConstExpression(new String("3")));
 Expression context = new EvaluationContext();
 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeImpl(new QName("root"), null, null, null, null, null));
 context.setInfoSet(infoSet);
 Object result = operation.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testCoreOperationGreaterThanWithSameTypesAndSameValues() {
 CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstExpression(new Double(5.0)), new ConstExpression(new Double(5.0)));
 Expression context = new EvaluationContext();
 InfoSet infoSet = new InfoSet();
 infoSet.setRoot(new NodeImpl(new QName("root"), null, null, null, null, null));
 context.setInfoSet(infoSet);
 Object result = operation.computeValue(context);
 assertFalse(result instanceof Boolean);
 }
}