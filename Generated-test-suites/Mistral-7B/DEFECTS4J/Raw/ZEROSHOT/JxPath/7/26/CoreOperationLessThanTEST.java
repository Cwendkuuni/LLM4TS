package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestHelper;
import org.apache.commons.jxpath.ri.TestHelper.NodeInfo;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanTest {

 private CoreOperationLessThan op;
 private EvalContext context;
 private NodeInfo node1, node2, node3;

 @Before
 public void setUp() {
 TestHelper.init();
 context = new EvalContext(TestHelper.getContext(), null);
 node1 = TestHelper.newNode("1.5");
 node2 = TestHelper.newNode("2.5");
 node3 = TestHelper.newNode("3.0");
 }

 @Test
 public void testLessThan() {
 op = new CoreOperationLessThan(new CoreStringExpression("1.5"), new CoreNumberExpression(node1));
 assertTrue(op.computeValue(context).booleanValue());

 op = new CoreOperationLessThan(new CoreStringExpression("1.5"), new CoreNumberExpression(node2));
 assertFalse(op.computeValue(context).booleanValue());

 op = new CoreOperationLessThan(new CoreNumberExpression(node1), new CoreNumberExpression(node2));
 assertFalse(op.computeValue(context).booleanValue());

 op = new CoreOperationLessThan(new CoreNumberExpression(node1), new CoreNumberExpression(node3));
 assertTrue(op.computeValue(context).booleanValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArgNull() {
 op = new CoreOperationLessThan(null, new CoreNumberExpression(node1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArg2Null() {
 op = new CoreOperationLessThan(new CoreNumberExpression(node1), null);
 }

 @Test
 public void testSymbol() {
 assertEquals("<", op.getSymbol());
 }
}