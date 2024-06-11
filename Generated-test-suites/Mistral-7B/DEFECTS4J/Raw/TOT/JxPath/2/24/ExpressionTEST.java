package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Factory;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Node;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link Expression} class.
 */
public class ExpressionTest {

 private Expression expression;
 private EvalContext evalContext;
 private Factory factory;

 @Before
 public void setUp() {
 factory = new Factory();
 evalContext = new EvalContext();
 expression = new ConstantExpression(new Double(1.0));
 }

 /**
 * Test method for {@link Expression#isContextDependent()}.
 */
 @Test
 public void testIsContextDependent() {
 // Test context-independent expression
 assertTrue(expression.isContextDependent());
 expression = new ConstantExpression(new Double(1.0));
 assertFalse(expression.isContextDependent());
 // Test context-dependent expression
 expression = new VariableExpression("x");
 assertTrue(expression.isContextDependent());
 }

 /**
 * Test method for {@link Expression#computeValue(EvalContext)}.
 */
 @Test
 public void testComputeValue() {
 Node node = factory.createNode("test", null);
 evalContext.pushContext(new JXPathContext(node));
 expression = new ConstantExpression(new Double(1.0));
 Object value = expression.computeValue(evalContext);
 assertEquals(1.0, value.doubleValue(), 0.0);
 }

 /**
 * Test method for {@link Expression#computeValue(EvalContext)}.
 */
 @Test
 public void testComputeValueWithContext() {
 Node node = factory.createNode("test", null);
 Node childNode = factory.createNode("child", node);
 evalContext.pushContext(new JXPathContext(childNode));
 expression = new ConstantExpression(new Double(1.0));
 Object value = expression.computeValue(evalContext);
 assertEquals(1.0, value.doubleValue(), 0.0);
 }

 /**
 * Test method for {@link Expression#computeValue(EvalContext)}.
 */
 @Test(expected = RuntimeException.class)
 public void testComputeValueWithNullContext() {
 expression = new ConstantExpression(new Double(1.0));
 expression.computeValue(null);
 }

 /**
 * Test method for {@link Expression#compute(EvalContext)}.
 */
 @Test
 public void testCompute() {
 Node node = factory.createNode("test", null);
 evalContext.pushContext(new JXPathContext(node));
 expression = new ConstantExpression(new Double(1.0));
 Object value = expression.compute(evalContext);
 assertEquals(1.0, value.doubleValue(), 0.0);
 }

 /**
 * Test method for {@link Expression#compute(EvalContext)}.
 */
 @Test
 public void testComputeWithContext() {
 Node node = factory.createNode("test", null);
 Node childNode = factory.createNode("child", node);
 evalContext.pushContext(new JXPathContext(childNode));
 expression = new ConstantExpression(new Double(1.0));
 Object value = expression.compute(evalContext);
 assertEquals(1.0, value.doubleValue(), 0.0);
 }

 /**
 * Test method for {@link Expression#compute(EvalContext)}.
 */
 @Test
 public void testComputeWithNullContext() {
 expression = new ConstantExpression(new Double(1.0));
 assertNull(expression.compute(null));
 }

 /**
 * Test method for {@link Expression#iterate(EvalContext)}.
 */
 @Test
 public void testIterate() {
 Node node = factory.createNode("test", null);
 evalContext.pushContext(new JXPathContext(node));
 expression = new ConstantExpression(new Double(1.0));
 Iterator iterator = expression.iterate(evalContext);
 assertEquals(1, iterator.size());
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 /**
 * Test method for {@link Expression#iteratePointers(EvalContext)}.
 */
 @Test
 public void testIteratePointers() {
 Node node = factory.createNode("test", null);
 Node childNode = factory.createNode("child", node);
 evalContext.pushContext(new JXPathContext(childNode));
 expression = new ConstantExpression(new Double(1.0));
 Iterator<Pointer> iterator = expression.iteratePointers(evalContext);
 assertEquals(1, iterator.size());
 Pointer pointer = iterator.next();
 assertNull(pointer.getValue());
 assertTrue(pointer.getName().getLocalPart().equals("value"));
 assertNull(iterator.next());
 }
}