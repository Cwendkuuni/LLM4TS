package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Nodeset;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThanNumberTrue() {
 CoreOperationLessThan op = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
 EvalContext context = new EvalContext(new InfoSet());
 Nodeset nodeset = op.computeValue(context);
 assertTrue(nodeset.getFirstNode().getFirstChild().getValue().equals(Boolean.TRUE));
 }

 @Test
 public void testLessThanNumberFalse() {
 CoreOperationLessThan op = new CoreOperationLessThan(new ConstantExpression(2.0), new ConstantExpression(1.0));
 EvalContext context = new EvalContext(new InfoSet());
 Nodeset nodeset = op.computeValue(context);
 assertFalse(nodeset.getFirstNode().getFirstChild().getValue().equals(Boolean.TRUE));
 }

 @Test
 public void testLessThanNumberZero() {
 CoreOperationLessThan op = new CoreOperationLessThan(new ConstantExpression(0.0), new ConstantExpression(1.0));
 EvalContext context = new EvalContext(new InfoSet());
 Nodeset nodeset = op.computeValue(context);
 assertTrue(nodeset.getFirstNode().getFirstChild().getValue().equals(Boolean.TRUE));
 }

 @Test
 public void testLessThanNumberEqual() {
 CoreOperationLessThan op = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(1.0));
 EvalContext context = new EvalContext(new InfoSet());
 Nodeset nodeset = op.computeValue(context);
 assertFalse(nodeset.getFirstNode().getFirstChild().getValue().equals(Boolean.TRUE));
 }

 @Test
 public void testLessThanNull() {
 CoreOperationLessThan op = new CoreOperationLessThan(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
 EvalContext context = new EvalContext(new InfoSet());
 Nodeset nodeset = op.computeValue(context);
 assertNull(nodeset);
 }

 @Test(expected = RuntimeException.class)
 public void testLessThanNullAndNumber() {
 CoreOperationLessThan op = new CoreOperationLessThan(new ConstantExpression(Double.NaN), new ConstantExpression(1.0));
 EvalContext context = new EvalContext(new InfoSet());
 try {
 Nodeset nodeset = op.computeValue(context);
 } catch (RuntimeException e) {
 assertEquals("Cannot compare NaN and non-NaN values", e.getMessage());
 }
 }

 @Test
 public void testLessThanNodeInfo() {
 CoreOperationLessThan op = new CoreOperationLessThan(new ConstantExpression(new NodeInfo("1")), new ConstantExpression(new NodeInfo("2")));
 EvalContext context = new EvalContext(new InfoSet());
 context.setCurrentNode(new NodeInfo("document"));
 context.setCurrentNode(context.getCurrentNode().getFirstChild());
 Nodeset nodeset = op.computeValue(context);
 assertFalse(nodeset.getFirstNode().getFirstChild().getValue().equals(Boolean.TRUE));
 }
}