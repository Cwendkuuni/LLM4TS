import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

 private Expression expression;

 @Test
 public void testExpressionIsContextDependent() {
 expression = new ContextDependentExpression();
 Assert.assertTrue(expression.isContextDependent());

 expression = new ContextIndependentExpression();
 Assert.assertFalse(expression.isContextDependent());
 }

 @Test
 public void testContextDependentExpressionEvaluation() {
 expression = new ContextDependentExpression();
 EvalContext context = new EvalContext(new NodePointer(new QName("root"), null, null), null);

 Assert.assertEquals(new Integer(1), expression.computeValue(context).intValue());
 Assert.assertEquals(new Integer(2), expression.computeValue(new EvalContext(new NodePointer(new QName("root2"), null, null), context.getRootContext())).intValue());
 }

 @Test
 public void testContextIndependentExpressionEvaluation() {
 expression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(new NodePointer(new QName("root"), null, null), null);

 Assert.assertEquals(new Integer(1), expression.computeValue(context).intValue());
 Assert.assertEquals(new Integer(1), expression.computeValue(new EvalContext(new NodePointer(new QName("root2"), null, null), context.getRootContext())).intValue());
 }

 @Test
 public void testIterate() {
 expression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(new NodePointer(new QName("root"), null, null), null);

 Iterator<Object> iterator = expression.iterate(context);
 Assert.assertTrue(iterator.hasNext());
 Assert.assertNotNull(iterator.next());
 }

 @Test
 public void testIterateWithEmptyResult() {
 expression = new ContextIndependentEmptyExpression();
 EvalContext context = new EvalContext(new NodePointer(new QName("root"), null, null), null);

 Iterator<Object> iterator = expression.iterate(context);
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratePointers() {
 expression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(new NodePointer(new QName("root"), null, null), null);

 Iterator<Pointer> iterator = expression.iteratePointers(context);
 Assert.assertTrue(iterator.hasNext());
 Assert.assertNotNull(iterator.next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratePointersUnsupportedRemove() {
 expression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(new NodePointer(new QName("root"), null, null), null);

 Iterator<Pointer> iterator = expression.iteratePointers(context);
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
 return new Integer(1);
 }
}

class ContextIndependentEmptyExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return null;
 }
}