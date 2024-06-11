import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression createExpression(Class<? extends CoreOperationRelationalExpression> clazz, Object... args) {
 return (CoreOperationRelationalExpression) ReflectionUtils.newInstance(clazz, args);
 }

 @Test
 public void testComputeValue_BooleanBoolean() {
 CoreOperationRelationalExpression expression = createExpression(CoreGreaterExpression.class, new Boolean(true), new Boolean(false));
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreGreaterEqualExpression.class, new Boolean(true), new Boolean(false));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreLessExpression.class, new Boolean(true), new Boolean(false));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreLessEqualExpression.class, new Boolean(true), new Boolean(false));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_ObjectObject() {
 CoreOperationRelationalExpression expression = createExpression(CoreGreaterExpression.class, new Integer(5), new Integer(4));
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreGreaterExpression.class, new Double(5.5), new Double(4.5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreGreaterEqualExpression.class, new Integer(5), new Integer(5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreGreaterEqualExpression.class, new Integer(5), new Integer(6));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreLessExpression.class, new Integer(5), new Integer(4));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessExpression.class, new Integer(5), new Integer(5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreLessEqualExpression.class, new Integer(5), new Integer(5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessEqualExpression.class, new Integer(5), new Integer(4));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessEqualExpression.class, new Integer(5), new Integer(6));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreGreaterExpression.class, new Double(5.5), new Double(4.5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreGreaterEqualExpression.class, new Double(5.5), new Double(5.5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreGreaterEqualExpression.class, new Double(5.5), new Double(6.5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreLessExpression.class, new Double(5.5), new Double(4.5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessEqualExpression.class, new Double(5.5), new Double(4.5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessEqualExpression.class, new Double(5.5), new Double(5.5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessEqualExpression.class, new Double(5.5), new Double(6.5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValue_CollectionIterator() {
 CoreOperationRelationalExpression expression = createExpression(CoreGreaterExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), createIterator(Arrays.asList(1, 2, 3, 6, 7)));
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreGreaterExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), createIterator(Arrays.asList(1, 2, 3, 4, 6)));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreGreaterEqualExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), createIterator(Arrays.asList(1, 2, 3, 4, 5)));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), createIterator(Arrays.asList(1, 2, 3, 4, 6)));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessEqualExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), createIterator(Arrays.asList(1, 2, 3, 4, 5)));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessEqualExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), createIterator(Arrays.asList(1, 2, 3, 4, 6)));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValue_IteratorObject() {
 CoreOperationRelationalExpression expression = createExpression(CoreGreaterExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(3));
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 Object result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreGreaterExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(4));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreGreaterExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(6));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreGreaterEqualExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(3));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreGreaterEqualExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(4));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreGreaterEqualExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(3));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(4));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreLessExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.FALSE, result);

 expression = createExpression(CoreLessEqualExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(3));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessEqualExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(4));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);

 expression = createExpression(CoreLessEqualExpression.class, createIterator(Arrays.asList(1, 2, 3, 4, 5)), new Integer(5));
 result = expression.computeValue(context);
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValue_InvalidArguments() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationRelationalExpression.class, new Object[] { new Boolean(true), new Integer(5) });
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 expression.computeValue(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValue_InvalidArguments2() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationRelationalExpression.class, new Object[] { new Integer(5), new Boolean(true) });
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 expression.computeValue(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValue_InvalidArguments3() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationRelationalExpression.class, new Object[] { new SelfContext(), new SelfContext() });
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 expression.computeValue(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValue_InvalidArguments4() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationRelationalExpression.class, new Object[] { new SelfContext(), new InitialContext() });
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 expression.computeValue(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValue_InvalidArguments5() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationRelationalExpression.class, new Object[] { new InitialContext(), new InitialContext() });
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 expression.computeValue(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValue_InvalidArguments6() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationRelationalExpression.class, new Object[] { new Iterator[0], new Iterator[0] });
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 expression.computeValue(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValue_InvalidArguments7() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationRelationalExpression.class, new Object[] { new Collection[0], new Iterator[0] });
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 expression.computeValue(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValue_InvalidArguments8() {
 CoreOperationRelationalExpression expression = createExpression(CoreOperationRelationalExpression.class, new Object[] { new Iterator[0], new Collection[0] });
 EvalContext context = new EvalContext(new Object[] { new SelfContext() });
 expression.computeValue(context);
 }

 private Iterator createIterator(Collection collection) {
 return collection.iterator();
 }

}