import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathContextFactory;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testEqual_SameObjects() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Integer(1)),
 new ConstantExpression(new Integer(1)));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertTrue(result);
 }

 @Test
 public void testEqual_DifferentObjects_False() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Integer(1)),
 new ConstantExpression(new Integer(2)));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertFalse(result);
 }

 @Test
 public void testEqual_DifferentObjects_True_WithInitialContext() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new InitialContext()),
 new ConstantExpression(new InitialContext()));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertTrue(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqual_WrongArgumentTypes() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Integer(1)),
 new ConstantExpression(new String("abc")));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 try {
 compare.evaluate(jxpathContext);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEqual_SelfContext() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new SelfContext(),
 new SelfContext());
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertTrue(result);
 }

 @Test
 public void testNotEqual_SameObjects() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Integer(1)),
 new ConstantExpression(new Integer(1)));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertFalse(result);
 }

 @Test
 public void testNotEqual_DifferentObjects_True() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Integer(1)),
 new ConstantExpression(new Integer(2)));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertTrue(result);
 }

 @Test
 public void testNotEqual_SameObjects_WithSelfContext() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new SelfContext(),
 new SelfContext());
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertFalse(result);
 }

 @Test
 public void testNotEqual_DifferentObjects_False() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Integer(1)),
 new ConstantExpression(new Integer(1)));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertFalse(result);
 }

 @Test
 public void testContains_NullInput() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(null),
 new ConstantExpression(new String("abc")));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertFalse(result);
 }

 @Test
 public void testContains_EmptyCollection() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Integer[0]),
 new ConstantExpression(new Integer(1)));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertFalse(result);
 }

 @Test
 public void testContains_SimpleCollection() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Integer[] { 1, 2, 3 }),
 new ConstantExpression(new Integer(2)));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertTrue(result);
 }

 @Test
 public void testContains_EmptyIterator() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Iterator[] { new Iterator() {
 @Override public boolean hasNext() { return false; }
 @Override public Object next() { return null; }
 }}),
 new ConstantExpression(new Integer(1)));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertFalse(result);
 }

 @Test
 public void testContains_SimpleIterator() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Iterator() {
 @Override public boolean hasNext() { return true; }
 @Override public Object next() { return new Integer(2); }
 }),
 new ConstantExpression(new Integer(2)));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertTrue(result);
 }

 @Test
 public void testFindMatch_SimpleCollections() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Integer[] { 1, 2, 3 }),
 new ConstantExpression(new Integer[] { 2, 3, 4 }));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertFalse(result);
 }

 @Test
 public void testFindMatch_CollectionsWithMatchingElement() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new ConstantExpression(new Integer[] { 1, 2, 3 }),
 new ConstantExpression(new Integer[] { 2, 3, 3 }));
 ExpressionContext context = new ExpressionContext();
 JXPathContext jxpathContext = JXPathContextFactory.newContext(context);
 boolean result = compare.evaluate(jxpathContext);
 assertTrue(result);
 }
}