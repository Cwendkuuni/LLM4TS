import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;
 private EvalContext context;
 private Document document;

 @Before
 public void setUp() throws Exception {
 document = JXPathContext.newDocument("src/test/resources/test.xml");
 context = new EvalContext(document);
 expression = new ConstantExpression(new NodePointer(document.getDocumentElement()));
 }

 @Test
 public void testIsContextDependent_ConstantExpression() {
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testComputeValue_ConstantExpression() {
 Object value = expression.computeValue(context);
 assertEquals(document.getDocumentElement(), value);
 }

 @Test
 public void testComputeValue_ContextDependentExpression() {
 expression = new ContextDependentExpression();
 assertTrue(expression.isContextDependent());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testComputeValue_ContextDependentExpression_NoContext() {
 expression.computeValue(null);
 }

 @Test
 public void testComputeValue_ContextDependentExpression_WithContext() {
 expression.setContextDependency(false);
 context = new EvalContext(document);
 context.setCurrentNodePointer(document.getDocumentElement().getElementsByTagName("root").item(0));
 Object value = expression.computeValue(context);
 assertSame(document.getDocumentElement().getElementsByTagName("child").item(0), value);
 }

 @Test
 public void testComputeValue_ContextDependentExpression_WithContext_DifferentNode() {
 context = new EvalContext(document);
 context.setCurrentNodePointer(document.getDocumentElement().getElementsByTagName("root").item(1));
 Object value = expression.computeValue(context);
 assertNull(value);
 }

 @Test
 public void testIterate_ConstantExpression() {
 Iterator it = expression.iterate(context);
 assertEquals(1, it.next().hashCode());
 assertFalse(it.hasNext());
 }

 @Test
 public void testIterate_ContextDependentExpression() {
 expression = new ContextDependentExpression();
 Iterator it = expression.iterate(context);
 assertSame(document.getDocumentElement().getElementsByTagName("root").item(0), it.next().getValue());
 assertFalse(it.hasNext());
 }

 @Test
 public void testIteratePointers_ConstantExpression() {
 Iterator it = expression.iteratePointers(context);
 assertEquals(document.getDocumentElement(), it.next().getNode());
 assertFalse(it.hasNext());
 }

 @Test
 public void testIteratePointers_ContextDependentExpression() {
 expression = new ContextDependentExpression();
 Iterator it = expression.iteratePointers(context);
 assertEquals(document.getDocumentElement().getElementsByTagName("root").item(0), it.next().getNode());
 assertFalse(it.hasNext());
 }
}