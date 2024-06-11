package org.apache.commons.jxpath.ri.compiler.ExpressionTest;

import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link Expression} class.
 */
public class ExpressionTest {

 private static final String XML_TEST_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <root><element1 contextDependent=\"true\">1</element1>" +
         "<element2 contextDependent=\"false\">2</element2></root>";
 private Document document;
 private EvalContext context;

 @Before
 public void setup() throws Exception {
 document = JaxpHelper.newDocument(XML_TEST_DATA);
 context = new EvalContext(document);
 }

 @Test
 public void testExpressionContextDependent() {
 Expression expression = new ContextDependentExpression();
 assertTrue(expression.isContextDependent());
 }

 @Test
 public void testExpressionContextIndependent() {
 Expression expression = new ContextIndependentExpression();
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testComputeValueContextDependent() {
 ContextDependentExpression expression = new ContextDependentExpression();
 assertEquals("1", expression.computeValue(context).toString());
 context = new EvalContext(document.getDocumentElement().getNextSibling());
 assertEquals("null", expression.computeValue(context).toString());
 }

 @Test
 public void testComputeValueContextIndependent() {
 ContextIndependentExpression expression = new ContextIndependentExpression();
 assertEquals("2", expression.computeValue(context).toString());
 context = new EvalContext(document.getDocumentElement().getNextSibling());
 assertEquals("2", expression.computeValue(context).toString());
 }

 @Test
 public void testIterateContextDependent() {
 ContextDependentExpression expression = new ContextDependentExpression();
 Iterator<NodePointer> iterator = expression.iterate(context);
 NodePointer first = iterator.next();
 assertEquals("element1", first.getName().getLocalPart());
 assertEquals(document.getDocumentElement().getFirstChild(), first.getNode());
 assertFalse(iterator.hasNext());
 context = new EvalContext(document.getDocumentElement().getNextSibling());
 iterator = expression.iterate(context);
 assertNull(iterator.next());
 }

 @Test
 public void testIterateContextIndependent() {
 ContextIndependentExpression expression = new ContextIndependentExpression();
 Iterator<NodePointer> iterator = expression.iterate(context);
 NodePointer first = iterator.next();
 assertEquals("element2", first.getName().getLocalPart());
 assertEquals(document.getDocumentElement().getElementsByTagName("element2").item(0), first.getNode());
 assertTrue(iterator.hasNext());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveFromIterator() {
 ContextDependentExpression expression = new ContextDependentExpression();
 Iterator<NodePointer> iterator = expression.iterate(context);
 iterator.next();
 iterator.remove();
 }
}

class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return context.getCurrentNodePointer().getNodeValue();
 }
}

class ContextIndependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return context.getRootContext().getCurrentNodePointer().getNodeValue();
 }
}