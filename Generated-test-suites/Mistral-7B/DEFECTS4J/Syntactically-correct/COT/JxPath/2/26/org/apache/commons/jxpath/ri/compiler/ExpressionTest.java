import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

 private Expression expression;

 @Test
 public void testIsContextDependent() {
 expression = new Expression() {
 @Override
 public boolean computeContextDependent() {
 return true;
 }
 };
 assertTrue(expression.isContextDependent());

 expression = new Expression() {
 @Override
 public boolean computeContextDependent() {
 return false;
 }
 };
 assertFalse(expression.isContextDependent());
 }

 @Test
 public void testComputeValue() {
 expression = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return null;
 }
 };
 assertNull(expression.computeValue(new EvalContext()));
 }

 @Test
 public void testComputeValueWithNodeSet() {
 expression = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 return new NodePointer("test");
 }
 };
 assertSame(expression.computeValue(new EvalContext()).getClass(), NodePointer.class);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testComputeValueWithEvalContext() {
 expression = new Expression() {
 @Override
 public Object computeValue(EvalContext context) {
 throw new UnsupportedOperationException();
 }
 };
 expression.computeValue(new EvalContext());
 }

 @Test
 public void testCompute() {
 expression = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return null;
 }
 };
 assertNull(expression.compute(new EvalContext()));
 }

 @Test
 public void testComputeWithNodeSet() {
 expression = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 return new NodePointer("test");
 }
 };
 assertSame(expression.compute(new EvalContext()).getClass(), NodePointer.class);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testComputeWithEvalContext() {
 expression = new Expression() {
 @Override
 public Object compute(EvalContext context) {
 throw new UnsupportedOperationException();
 }
 };
 expression.compute(new EvalContext());
 }

 @Test
 public void testIterate() {
 expression = new Expression() {
 @Override
 public Iterator iterate(EvalContext context) {
 return new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Object() {};
 }
 };
 }
 };
 assertFalse(expression.iterate(new EvalContext()).hasNext());
 }

 @Test
 public void testIterateWithIterator() {
 expression = new Expression() {
 @Override
 public Iterator iterate(EvalContext context) {
 return new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Object() {};
 }
 }.iterator();
 }
 };
 assertFalse(expression.iterate(new EvalContext()).hasNext());
 }

 @Test
 public void testIterateWithNodeSet() {
 expression = new Expression() {
 @Override
 public Iterator iterate(EvalContext context) {
 return new NodePointer("test").iterator();
 }
 };
 assertFalse(expression.iterate(new EvalContext()).hasNext());
 }

 @Test
 public void testIterateWithValueSet() {
 expression = new Expression() {
 @Override
 public Iterator iterate(EvalContext context) {
 return ValueUtils.iterate(new Object[]{"test1", "test2"}).iterator();
 }
 };
 assertFalse(expression.iterate(new EvalContext()).hasNext());
 }
}