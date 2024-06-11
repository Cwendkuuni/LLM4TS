import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

public class ExpressionTest {

 private EvalContext context;
 private Document document;

 @Before
 public void setup() {
 context = new EvalContext();
 document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 }

 @Test
 public void testContextDependentExpression() {
 // Create a context-dependent expression
 Expression contextDependentExpression = new ContextDependentExpression();

 // Test with different contexts
 Node rootNode = document.createElement("root");
 document.appendChild(rootNode);
 Node childNode = document.createElement("child");
 rootNode.appendChild(childNode);
 context.setCurrentNode(rootNode);
 assertFalse(contextDependentExpression.isContextDependent());
 assertNull(contextDependentExpression.computeValue(context));

 context.setCurrentNode(childNode);
 assertTrue(contextDependentExpression.isContextDependent());
 assertNull(contextDependentExpression.computeValue(context));
 }

 @Test
 public void testContextIndependentExpression() {
 // Create a context-independent expression
 Expression contextIndependentExpression = new ConstantExpression(1);

 // Test with different contexts
 Node rootNode = document.createElement("root");
 document.appendChild(rootNode);
 Node childNode = document.createElement("child");
 rootNode.appendChild(childNode);
 context.setCurrentNode(rootNode);
 assertEquals(1, contextIndependentExpression.computeValue(context).doubleValue());

 context.setCurrentNode(childNode);
 assertEquals(1, contextIndependentExpression.computeValue(context).doubleValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterateWithoutContext() {
 Expression expression = new ConstantExpression(1);
 Iterator iter = expression.iterate(null);
 iter.next();
 }

 @Test
 public void testIterateWithContext() {
 // Create a context and add some nodes
 Node rootNode = document.createElement("root");
 document.appendChild(rootNode);
 Node childNode1 = document.createElement("child1");
 Node childNode2 = document.createElement("child2");
 rootNode.appendChild(childNode1);
 rootNode.appendChild(childNode2);
 context.setCurrentNode(rootNode);

 // Test iterating over a context-independent expression
 Expression constantExpression = new ConstantExpression(1);
 Iterator iter = constantExpression.iterate(context);
 assertEquals(1, iter.next().doubleValue());
 assertNull(iter.next());

 // Test iterating over a context-dependent expression
 Expression contextDependentExpression = new ContextDependentExpression();
 iter = contextDependentExpression.iterate(context);
 assertEquals(1, ((NodePointer) iter.next()).getNode().getNodeValue().doubleValue(), 0.01);
 assertEquals(2, contextDependentExpression.iterate(context).size());
 }
}

class ConstantExpression extends Expression {
 private double value;

 public ConstantExpression(double value) {
 this.value = value;
 }

 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return value;
 }

 @Override
 public Object compute(EvalContext context) {
 return value;
 }
}

class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }
}