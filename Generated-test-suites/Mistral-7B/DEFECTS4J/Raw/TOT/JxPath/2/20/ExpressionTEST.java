import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private EvalContext context;

 @Before
 public void setup() {
 context = new EvalContext();
 context.setRootContext(new EvalContext.EmptyContext());
 }

 @Test
 public void testContextDependentExpression() {
 Expression expression = new ContextDependentExpression();
 assertTrue(expression.isContextDependent());
 Object result = expression.computeValue(context);
 assertNull(result);
 }

 @Test
 public void testContextIndependentExpression() {
 Expression expression = new ContextIndependentExpression();
 assertFalse(expression.isContextDependent());
 Object result = expression.computeValue(context);
 assertEquals(1.0, result, 0.0);
 }

 @Test
 public void testContextIndependentExpressionWithNodes() {
 Expression expression = new ContextIndependentExpressionWithNodes();
 assertFalse(expression.isContextDependent());
 context.setCurrentNodePointer(new NodePointer("document", null, null));
 Object result = expression.compute(context);
 assertEquals(1.0, result, 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterationOfContextDependentExpression() {
 Expression expression = new ContextDependentExpression();
 Iterator iterator = expression.iterate(context);
 iterator.next();
 }

 @Test
 public void testIterationOfContextIndependentExpression() {
 Expression expression = new ContextIndependentExpression();
 Iterator iterator = expression.iterate(context);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIterationOfContextIndependentExpressionWithNodes() {
 Expression expression = new ContextIndependentExpressionWithNodes();
 Iterator iterator = expression.iterate(context);
 assertTrue(iterator.hasNext());
 NodePointer nodePointer = (NodePointer) iterator.next();
 assertEquals("document", nodePointer.getNamespaceURI());
 assertNull(nodePointer.getLocalName());
 assertNull(nodePointer.getValue());
 }

 @Test
 public void testIterationOfContextIndependentExpressionWithNodesAndValues() {
 Expression expression = new ContextIndependentExpressionWithNodesAndValues();
 Iterator iterator = expression.iterate(context);
 assertTrue(iterator.hasNext());
 NodePointer nodePointer = (NodePointer) iterator.next();
 assertEquals("document", nodePointer.getNamespaceURI());
 assertNull(nodePointer.getLocalName());
 assertNotNull(nodePointer.getValue());
 assertEquals(1.0, nodePointer.getValue(), 0.0);
 }

 @Test
 public void testIterationOfContextDependentExpressionWithNodes() {
 Expression expression = new ContextDependentExpressionWithNodes();
 Iterator iterator = expression.iterate(context);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIterationOfContextDependentExpressionWithNodesAndValues() {
 Expression expression = new ContextDependentExpressionWithNodesAndValues();
 Iterator iterator = expression.iterate(context);
 assertFalse(iterator.hasNext());
 }
}

class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return context.getCurrentNodePointer().getValue();
 }
}

class ContextIndependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return 1.0;
 }
}

class ContextIndependentExpressionWithNodes extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return 1.0;
 }

 @Override
 public Iterator iterate(EvalContext context) {
 return new ValueIterator(Collections.singletonList(1.0).iterator());
 }
}

class ContextIndependentExpressionWithNodesAndValues extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return 1.0;
 }

 @Override
 public Iterator iterate(EvalContext context) {
 NodePointer nodePointer = new NodePointer("document", null, null);
 nodePointer.setValue(1.0);
 return new PointerIterator(Collections.singletonList(nodePointer).iterator(),
 new QName(null, "document"), context.getRootContext().getCurrentNodePointer().getLocale());
 }
}

class ContextDependentExpressionWithNodes extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return context.getCurrentNodePointer().getValue();
 }

 @Override
 public Iterator iterate(EvalContext context) {
 return Collections.emptyIterator();
 }
}

class ContextDependentExpressionWithNodesAndValues extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return context.getCurrentNodePointer().getValue();
 }

 @Override
 public Iterator iterate(EvalContext context) {
 return Collections.emptyIterator();
 }
}