import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.jxpath.util.ValueUtils.iterate;

public class ExpressionTest {

 private Expression expression;

 @Test
 public void testIsContextDependent_Default() {
 expression = new Expression() {};
 Assert.assertFalse(expression.isContextDependent());
 }

 @Test
 public void testIsContextDependent_SubclassReturnsTrue() {
 expression = new SubclassOfExpression() {};
 Assert.assertTrue(expression.isContextDependent());
 }

 @Test
 public void testIsContextDependent_SubclassReturnsFalse() {
 expression = new SubclassOfExpressionThatReturnsFalse() {};
 Assert.assertFalse(expression.isContextDependent());
 }

 @Test
 public void testComputeValue_ReturnsNull() {
 expression = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return null;
 }
 };
 EvalContext context = new EvalContext(new Object());
 Object result = expression.computeValue(context);
 Assert.assertNull(result);
 }

 @Test
 public void testComputeValue_ReturnsNumber() {
 expression = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return ZERO;
 }
 };
 EvalContext context = new EvalContext(new Object());
 Object result = expression.computeValue(context);
 Assert.assertEquals(ZERO, result);
 }

 @Test
 public void testComputeValue_ReturnsObject() {
 expression = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return new Object();
 }
 };
 EvalContext context = new EvalContext(new Object());
 Object result = expression.computeValue(context);
 Assert.assertNotNull(result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterate_ThrowsException() {
 expression = new Expression() {};
 Iterator iterator = expression.iterate(new EvalContext(new Object()));
 iterator.remove();
 }

 @Test
 public void testIteratePointers_ReturnsEmptyIterator() {
 expression = new Expression() {};
 Iterator iterator = expression.iteratePointers(new EvalContext(new Object()));
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratePointers_ReturnsIteratorWithSinglePointer() {
 expression = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new NodePointer(new QName(null, "test"), new Object(), Locale.ENGLISH);
 }
 };
 EvalContext context = new EvalContext(new Object());
 Iterator iterator = expression.iteratePointers(context);
 Assert.assertTrue(iterator.hasNext());
 Pointer pointer = (Pointer) iterator.next();
 Assert.assertNotNull(pointer);
 Assert.assertNotNull(pointer.getValue());
 }

 @Test
 public void testIteratePointers_ReturnsIteratorWithMultiplePointers() {
 expression = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new Object[] {new NodePointer(new QName(null, "test1"), new Object(), Locale.ENGLISH),
 new NodePointer(new QName(null, "test2"), new Object(), Locale.ENGLISH)};
 }
 };
 EvalContext context = new EvalContext(new Object());
 Iterator iterator = expression.iteratePointers(context);
 Assert.assertTrue(iterator.hasNext());
 Pointer pointer1 = (Pointer) iterator.next();
 Assert.assertNotNull(pointer1);
 Assert.assertNotNull(pointer1.getValue());
 Assert.assertTrue(iterator.hasNext());
 Pointer pointer2 = (Pointer) iterator.next();
 Assert.assertNotNull(pointer2);
 Assert.assertNotNull(pointer2.getValue());
 Assert.assertFalse(iterator.hasNext());
 }
}

class SubclassOfExpression {}
class SubclassOfExpressionThatReturnsFalse extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }
}