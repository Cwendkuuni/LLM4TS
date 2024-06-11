package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri. NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

 private CoreOperationGreaterThan operation;
 private EvalContext context;
 private InfoSet infoSet;
 private NodeInfo arg1Node;
 private NodeInfo arg2Node;
 private SimpleNode arg1;
 private SimpleNode arg2;

 @Before
 public void setUp() {
 arg1 = new SimpleNode("number1", new Double(5.5));
 arg2 = new SimpleNode("number2", new Double(3.3));
 arg1Node = new NodeInfo(arg1);
 arg2Node = new NodeInfo(arg2);
 infoSet = new InfoSet();
 context = new EvalContext(infoSet, new Path.Context(), null);
 operation = new CoreOperationGreaterThan(arg1, arg2);
 }

 @Test
 public void testComputeValueTrue() {
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueFalse() {
 arg1 = new SimpleNode("number1", new Double(3.3));
 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueNull() {
 arg1 = new SimpleNode("number1", null);
 Object result = operation.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testComputeValueNullArg2() {
 arg2 = new SimpleNode("number2", null);
 Object result = operation.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testSymbol() {
 String symbol = operation.getSymbol();
 assertEquals(">", symbol);
 }

 @Test
 public void testCombineExpressions() {
 Expression arg1Exp = operation.args[0];
 Expression arg2Exp = operation.args[1];
 Expression combinedExp = operation.combine(context, arg1Exp, arg2Exp);
 assertTrue(combinedExp instanceof CoreOperationGreaterThan);
 }
}